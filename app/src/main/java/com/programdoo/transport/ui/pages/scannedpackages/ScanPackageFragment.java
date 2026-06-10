package com.programdoo.transport.ui.pages.scannedpackages;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.data.models.requests.scannedPackages.SearchScannedPackagesParams;
import com.programdoo.transport.ui.adapters.ScannedPackagesPagerAdapter;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.scannedPackages.ScanPackageViewModel;
import com.programdoo.transport.ui.viewmodels.scannedPackages.ScannedPackagesSharedViewModel;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

public class ScanPackageFragment extends BaseFragment {

    private ScannedPackagesSharedViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;

    private ViewPager2 viewPager;
    private ImageView success;
    private EditText input;
    private TextView txtGpsStatus;

    private double currentLat = 0;
    private double currentLng = 0;

    private boolean isSaving = false;
    private String lastScannedCode = null;

    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan_package, container, false);

        success = view.findViewById(R.id.imgSuccess);
        input = view.findViewById(R.id.etScanInput);
        txtGpsStatus = view.findViewById(R.id.txtGpsStatus);
        viewPager = view.findViewById(R.id.viewPagerPackages);

        input.setEnabled(false);
        input.requestFocus();

        viewModel = new ViewModelProvider(this).get(ScannedPackagesSharedViewModel.class);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());

        viewModel.getPackageIdsLiveData().observe(getViewLifecycleOwner(), listaIdjeva -> {
            if (listaIdjeva != null && !listaIdjeva.isEmpty()) {
                ScannedPackagesPagerAdapter adapter =
                        new ScannedPackagesPagerAdapter(this, listaIdjeva);
                viewPager.setAdapter(adapter);
            }
        });

        setupEnter();
        observeEvents();
        observeData();
        startLocationUpdates();

        return view;
    }

    private void setupEnter() {

        input.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (isSaving) return true;

                String code = v.getText().toString().trim();

                if (code.isEmpty()) return true;

                isSaving = true;
                lastScannedCode = code;

                SaveScannedPackagesRequestModel model = new SaveScannedPackagesRequestModel();
                model.setPackageNo(code);
                model.setUserId(viewModel.getLoggedUserId());
                model.setScannedDateTime(LocalDateTime.now());
                model.setLatitude(currentLat);
                model.setLongitude(currentLng);

                viewModel.saveScannedPackage(model);

                input.setText("");
                input.requestFocus();

                return true;
            }
            return false;
        });
    }

    private void observeEvents() {

        viewModel.getToastEvent().observe(getViewLifecycleOwner(), id -> {

            isSaving = false;

            if (id == 1) {

                success.setVisibility(View.VISIBLE);
                success.setScaleX(0f);
                success.setScaleY(0f);

                success.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .start();

                success.postDelayed(() -> {
                    success.animate()
                            .alpha(0f)
                            .setDuration(300)
                            .withEndAction(() -> {
                                success.setVisibility(View.GONE);
                                success.setAlpha(1f);
                            })
                            .start();
                }, 500);

                if (lastScannedCode != null) {
                    loadPackageByNo(lastScannedCode);
                }
            }

            if (id == 2) {
                Toast.makeText(getContext(), "Error saving", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("AutoDispose")
    private void observeData() {

        viewModel.getScannedPackages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            if (response == null || response.getPayload() == null) return;


                        }, throwable ->
                                Log.e("TRAVEL_DEBUG", "STREAM ERROR", throwable)
                );
    }

    private void loadPackageByNo(String packageNo) {

        SearchScannedPackagesParams params = new SearchScannedPackagesParams();
        params.setPackageNo(packageNo);

        viewModel.searchScannedPackages(params);

        //int trenutniId = viewModel.getSelectedPackageId().getValue();
        // int prethodniId = trenutniId - 1; //
        // List<Integer> ids = Arrays.asList(trenutniId, prethodniId);
        // viewModel.packageIdsLiveData.setValue(ids);
    }

    private void startLocationUpdates() {

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);

            return;
        }

        locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                5000
        ).setMinUpdateIntervalMillis(2000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null) return;

                android.location.Location location =
                        locationResult.getLastLocation();

                if (location != null) {

                    currentLat = location.getLatitude();
                    currentLng = location.getLongitude();

                    requireActivity().runOnUiThread(() -> {
                        txtGpsStatus.setVisibility(View.GONE);
                        input.setEnabled(true);
                        input.requestFocus();
                    });
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                requireActivity().getMainLooper()
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public String TAG() {
        return "ScanPackageFragment";
    }
}