package com.programdoo.transport.ui.pages.receiptScan;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.receipts.ReceiptModel;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.poolCarReservations.CreatePoolCarReservationViewModel;
import com.programdoo.transport.ui.viewmodels.receipts.ReceiptsViewModel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;

public class ReceiptScanFragment extends BaseFragment {

    private ReceiptsViewModel viewModel;
    private EditText scanInput;

    private Disposable saveDisposable;
    private static final String TAG = "ReceiptScan";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_receipt_scan, container, false);
        init(view);
        viewModel = new ViewModelProvider(this)
                .get(ReceiptsViewModel.class);

        viewModel.getToastEvent().observe(getViewLifecycleOwner(), value -> {

            if (value == 1) {
                Toast.makeText(requireContext(),
                        "Račun uspešno sačuvan",
                        Toast.LENGTH_SHORT).show();
            }
            else if (value == 2) {
                Toast.makeText(requireContext(),
                        "Greška prilikom čuvanja",
                        Toast.LENGTH_SHORT).show();
            }

        });


        return view;

    }

    private void init(View view) {

        scanInput = view.findViewById(R.id.scanInput);
        scanInput.requestFocus();

        scanInput.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER) {

                String value = scanInput.getText().toString().trim();

                if (!value.isEmpty()) {
                    handleScan(value);
                }

                scanInput.setText("");
                return true;
            }

            return false;
        });
    }


    private void handleScan(String value) {

        Log.d(TAG, "RAW SCAN: " + value);

        if (value.startsWith("https://suf.purs.gov.rs")) {
            processReceipt(value);
        }
    }

    private void processReceipt(String url) {

        fetchAndParseAll(url);
    }


    private void fetchAndParseAll(String url) {

        new Thread(() -> {
            try {

                Document doc = Jsoup.connect(url)
                        .timeout(10000)
                        .get();

                String html = doc.html();

                String tin = getById(doc, "tinLabel");
                String shop = getById(doc, "shopFullNameLabel");
                String address = getById(doc, "addressLabel");
                String city = getById(doc, "cityLabel");
                String municipality = getById(doc, "administrativeUnitLabel");
                String customerId = getById(doc, "buyerIdLabel");
                String totalAmount = getById(doc, "totalAmountLabel");
                String invoiceNumber = getById(doc, "invoiceNumberLabel");
                String date = getById(doc, "sdcDateTimeLabel");

                String token = extractToken(html);

                ReceiptModel receipt = new ReceiptModel();
                receipt.setTin(tin);
                receipt.setShop(shop);
                receipt.setAddress(address);
                receipt.setCity(city);
                receipt.setMunicipality(municipality);
                receipt.setCustomerId(customerId);
                receipt.setTotalAmount(totalAmount);
                receipt.setInvoiceNumber(invoiceNumber);
                receipt.setDate(date);
                receipt.setToken(token);
                receipt.setEmployeeId(viewModel.getSession().getEntityId());

                viewModel.parseSufAndSave(receipt);

            } catch (Exception e) {
                Log.e(TAG, "ERROR: " + e.getMessage(), e);
            }
        }).start();
    }


    // ===================== HELPERS =====================

    private String getById(Document doc, String id) {
        return doc.getElementById(id) != null
                ? doc.getElementById(id).text().trim()
                : null;
    }

    private String extractToken(String html) {

        String key = "viewModel.Token('";
        int start = html.indexOf(key);

        if (start == -1) return null;

        start += key.length();

        int end = html.indexOf("'", start);

        if (end == -1) return null;

        return html.substring(start, end);
    }


    @Override
    public String TAG() {
        return "ReceiptScanFragment";
    }
}