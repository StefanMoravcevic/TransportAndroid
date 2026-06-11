package com.programdoo.transport.ui.pages.driverVehicleIssues;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.documents.DocumentDto;
import com.programdoo.transport.data.models.dtos.driverVehicleIssues.SaveDriverVehicleIssueRequestModel;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.data.settings.Settings;
import com.programdoo.transport.databinding.FragmentCreateDriverVehicleIssueBinding;
import com.programdoo.transport.databinding.FragmentCreateTravelOrderBinding;
import com.programdoo.transport.ui.adapters.EmployeesRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.ImagesAdapter;
import com.programdoo.transport.ui.adapters.MasterDataRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.documents.DocumentsViewModel;
import com.programdoo.transport.ui.viewmodels.driverVehicleIssues.CreateDriverVehicleIssueViewModel;
import com.programdoo.transport.ui.viewmodels.travelOrders.CreateTravelOrderViewModel;
import com.programdoo.transport.utils.CameraHelper;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.UiUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

@AndroidEntryPoint
public class CreateDriverVehicleIssueFragment extends BaseFragment {

    private FragmentCreateDriverVehicleIssueBinding binding;
    private MasterDataRecyclerViewAdapter masterDataAdapter;
    private ImagesAdapter imagesAdapter;

    private CreateDriverVehicleIssueViewModel viewModel;
    private DocumentsViewModel documentsViewModel;

    private CameraHelper cameraHelper;

    private Integer savedItem;



    private long employeeId;

    private MasterDataDto selectedVehicleDefectType;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public String TAG() {
        return Constants.FRAG_DRIVER_VEHICLE_ISSUE_ADD;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentCreateDriverVehicleIssueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreateDriverVehicleIssueViewModel.class);
        documentsViewModel = new ViewModelProvider(this).get(DocumentsViewModel.class);
        masterDataAdapter = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());
        imagesAdapter = new ImagesAdapter(requireContext());
        setupToolbar();
        setupUI();
        readArguments();
        setupObservers();
        bindSelectInputs();
        loadData();
        observeUpload();
        initCamera();
    }

    private void bindSelectInputs() {
        UiUtil.selectSetup(
                requireContext(),
                masterDataAdapter,
                binding.selVehicleDefectType,
                (view, position, item) -> {
                    binding.selVehicleDefectType.toggleSelected(position);
                    if (item instanceof MasterDataDto) {
                        selectedVehicleDefectType = (MasterDataDto) item;
                    }
                }
        );


    }

    private void initCamera() {

        cameraHelper = new CameraHelper();

        cameraHelper.init(this, uri -> {

            if (savedItem != 0) {

                documentsViewModel.uploadDocument(
                        uri,
                        savedItem,
                        viewModel.getSession().getUserId(),
                        requireContext(),
                        2026
                );
            }
        });
    }
    private void readArguments() {
        if (getArguments() != null) {
            employeeId = getArguments().getLong("employeeId", -1);
        }
    }

    private void setupUI() {

        binding.buttonCancel.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        binding.buttonSave.setOnClickListener(v -> saveDriverVehicleIssue());

        binding.btnOpenCamera.setOnClickListener(v -> {

            if (savedItem == null || savedItem == 0) {
                Toast.makeText(requireContext(),
                        "Prvo sačuvaj zapis",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            cameraHelper.openCamera(requireContext(), "issue_image");
        });

        binding.rvImages.setLayoutManager(
                new androidx.recyclerview.widget.GridLayoutManager(requireContext(), 3)
        );

        binding.rvImages.setAdapter(imagesAdapter);
    }

    @SuppressLint("AutoDispose")
    private void setupObservers() {

        disposables.add(
                viewModel.getSaveResult()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {

                            Snackbar snackbar = Snackbar.make(
                                    binding.getRoot(),
                                    getString(R.string.label_succesfullySaved),
                                    Snackbar.LENGTH_SHORT
                            );

                            savedItem = result;

                            snackbar.setBackgroundTint(Color.parseColor("#2E7D32")); // green
                            snackbar.setTextColor(Color.WHITE);
                            snackbar.show();

                            binding.cardImages.setVisibility(View.VISIBLE);

                            binding.scrollView.post(() ->
                                    binding.scrollView.fullScroll(View.FOCUS_DOWN)
                            );


                        }, Throwable::printStackTrace)
        );
    }

    @SuppressLint("AutoDispose")
    private void saveDriverVehicleIssue() {
        SaveDriverVehicleIssueRequestModel req = new SaveDriverVehicleIssueRequestModel();


        req.employeeId = viewModel.getSession().getEntityId();
        req.description = binding.selDescription.getText().toString();
        req.vehicleDefectTypeId = selectedVehicleDefectType.getValue();

        viewModel.saveDriverVehicleIssue(req);

    }

    private void setupToolbar() {
        ((BaseActivity) requireActivity())
                .setToolbarTitle(getString(R.string.label_create_driver_vehicle_issue));

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }

    private void loadData() {
        viewModel.getVehicleDefectTypes().observe(getViewLifecycleOwner(), vehicleDefectTypes -> {
            masterDataAdapter.setData(vehicleDefectTypes);
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        disposables.clear();

        binding = null;
    }

    private void observeUpload() {

        documentsViewModel.getUploadResult()
                .observe(getViewLifecycleOwner(), result -> {

                    if (result != null) {
                        Toast.makeText(requireContext(),
                                getString(R.string.label_succesfullyUploaded),
                                Toast.LENGTH_SHORT).show();

                        documentsViewModel.loadDocuments(
                                2026,
                                savedItem
                        );
                        observeDocuments();
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

    private void observeDocuments() {

        documentsViewModel.getDocuments()
                .observe(getViewLifecycleOwner(), documents -> {

                    if (documents == null || documents.isEmpty())
                        return;

                    imagesAdapter.setData(documents);

                    binding.rvImages.setVisibility(View.VISIBLE);
                });
    }

}
