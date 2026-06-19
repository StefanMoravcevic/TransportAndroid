package com.programdoo.transport.ui.pages.travelOrders;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.databinding.FragmentTravelOrdersListBinding;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.adapters.TravelOrderRecyclerListAdapter;
import com.programdoo.transport.ui.viewmodels.documents.DocumentsViewModel;
import com.programdoo.transport.ui.viewmodels.travelOrders.TravelOrdersListViewModel;
import com.programdoo.transport.utils.CameraHelper;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.UiUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class TravelOrdersListFragment extends BaseFragment {

    private FragmentTravelOrdersListBinding binding;
    private TravelOrdersListViewModel viewModel;
    private DocumentsViewModel documentsViewModel;

    private TravelOrderRecyclerListAdapter adapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private CameraHelper cameraHelper;
    private TravelOrderDto selectedItem;

    private Integer currentStatus = null;

    @Override
    public String TAG() {
        return Constants.FRAG_TRAVEL_ORDERS_LIST;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(TravelOrdersListViewModel.class);
        documentsViewModel = new ViewModelProvider(this).get(DocumentsViewModel.class);

        binding = FragmentTravelOrdersListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDefaultDateRange();
        applyFilters();
        setupToolbar();
        setupRecyclerView();
        observeData();
        observeUpload();

        setupFilters();

        applyFilters();

        initCamera();
    }

    // ================= FILTERS =================

    private void setupFilters() {

        // STATUS CHIPS
        binding.chipAll.setOnClickListener(v -> {
            currentStatus = null;
            applyFilters();
        });

        binding.chipZakazan.setOnClickListener(v -> {
            currentStatus = 1;
            applyFilters();
        });

        binding.chipRealizovan.setOnClickListener(v -> {
            currentStatus = 2;
            applyFilters();
        });

        binding.chipObracunat.setOnClickListener(v -> {
            currentStatus = 3;
            applyFilters();
        });

        binding.chipOtkazan.setOnClickListener(v -> {
            currentStatus = 4;
            applyFilters();
        });

        // DATE PICKERS (VAŽNO: callback)
        UiUtil.datePickerSetupCallback(
                this,
                binding.tvDateFrom,
                this::applyFilters
        );

        UiUtil.datePickerSetupCallback(
                this,
                binding.tvDateTo,
                this::applyFilters
        );
    }

    private void applyFilters() {
        loadData(currentStatus);
    }

    private void initDefaultDateRange() {

        LocalDate now = LocalDate.now();

        LocalDate firstDay = now.withDayOfMonth(1);
        LocalDate lastDay = now.withDayOfMonth(now.lengthOfMonth());

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy");

        binding.tvDateFrom.setText(firstDay.format(formatter));
        binding.tvDateTo.setText(lastDay.format(formatter));
    }

    // ================= DATA =================

    private void loadData(Integer travelOrderStatusId) {

        int employeeId = viewModel.getSession().getEntityId();

        var params = new com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams();
        params.setEmployeeId(employeeId);
        params.setTravelOrderStatusId(travelOrderStatusId);

        params.setDateFrom(getDateFrom());
        params.setDateTo(getDateTo());

        viewModel.searchTravelOrders(params);
    }

    // ================= RECYCLER =================

    private void setupRecyclerView() {

        adapter = new TravelOrderRecyclerListAdapter();

        adapter.setOnCameraClickListener(item -> {

            selectedItem = item;

            cameraHelper.openCamera(
                    requireContext(),
                    "travel_order_" + item.getId()
            );

        });

        binding.rvTravelOrders.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        binding.rvTravelOrders.setAdapter(adapter);

        binding.rvTravelOrders.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        binding.fabNewTravelOrder.setOnClickListener(v -> openCreateTravelOrder());
    }

    // ================= OBSERVE =================

    @SuppressLint("AutoDispose")
    private void observeData() {

        disposables.add(
                viewModel.getTravelOrders()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(response -> {

                                    if (response == null || response.getPayload() == null) {
                                        adapter.submitList(null);
                                        return;
                                    }

                                    adapter.submitList(response.getPayload());

                                }, throwable ->
                                        Log.e("TRAVEL_DEBUG", "STREAM ERROR", throwable)
                        )
        );
    }

    private void observeUpload() {

        documentsViewModel.getUploadResult()
                .observe(getViewLifecycleOwner(), result -> {
                    if (result != null) {
                        Toast.makeText(requireContext(),
                                "Upload successful ✔",
                                Toast.LENGTH_SHORT).show();

                        applyFilters();
                    }
                });

        documentsViewModel.getUploadError()
                .observe(getViewLifecycleOwner(), error -> {
                    if (error != null) {
                        Toast.makeText(requireContext(),
                                "Upload failed: " + error,
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    // ================= DATE PARSING =================

    private LocalDateTime getDateFrom() {

        String value = binding.tvDateFrom.getText().toString();

        if (value.isEmpty() || value.equals("Od datuma"))
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return LocalDate.parse(value, formatter)
                .atStartOfDay();
    }

    private LocalDateTime getDateTo() {

        String value = binding.tvDateTo.getText().toString();

        if (value.isEmpty() || value.equals("Do datuma"))
            return null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        return LocalDate.parse(value, formatter)
                .atTime(23, 59, 59);
    }

    // ================= CAMERA =================

    private void initCamera() {

        cameraHelper = new CameraHelper();

        cameraHelper.init(this, uri -> {

            if (selectedItem != null) {

                documentsViewModel.uploadDocument(
                        uri,
                        selectedItem.getId(),
                        viewModel.getSession().getUserId(),
                        requireContext(),
                        1025
                );
            }
        });
    }

    // ================= LIFECYCLE =================

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
        binding = null;
    }

    private void setupToolbar() {
        ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.label_travelOrders));
        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }

    private void openCreateTravelOrder() {

        CreateTravelOrderFragment fragment = new CreateTravelOrderFragment();

        Bundle args = new Bundle();
        args.putLong("employeeId", viewModel.getSession().getEntityId());

        fragment.setArguments(args);

        requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentFrame, fragment)
                .addToBackStack(null)
                .commit();
    }
}