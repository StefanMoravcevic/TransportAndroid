package com.programdoo.transport.ui.pages.travelOrders;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.programdoo.transport.data.models.dtos.travelOrders.TravelOrderDto;
import com.programdoo.transport.databinding.FragmentTravelOrdersListBinding;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.adapters.TravelOrderRecyclerListAdapter;
import com.programdoo.transport.ui.viewmodels.documents.DocumentsViewModel;
import com.programdoo.transport.ui.viewmodels.travelOrders.TravelOrdersListViewModel;
import com.programdoo.transport.utils.Constants;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;

@AndroidEntryPoint
public class TravelOrdersListFragment extends BaseFragment {

    private FragmentTravelOrdersListBinding binding;
    private TravelOrdersListViewModel viewModel;

    private DocumentsViewModel documentsViewModel;
    private TravelOrderRecyclerListAdapter adapter;

    private CompositeDisposable disposables = new CompositeDisposable();

    private ActivityResultLauncher<Uri> takePictureLauncher;
    private Uri imageUri;

    private TravelOrderDto selectedItem;

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

        setupToolbar();
        setupRecyclerView();
        observeData();
        loadData();

        binding.rvTravelOrders.addItemDecoration(
                new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        );

        initCamera();

        documentsViewModel.getUploadResult()
                .observe(getViewLifecycleOwner(), result -> {

                    if (result != null) {

                        Toast.makeText(requireContext(),
                                "Upload successful",
                                Toast.LENGTH_SHORT).show();

                        Log.d("UPLOAD", "SUCCESS ID: " + result);

                        loadData();
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

    // =========================
    // TOOLBAR
    // =========================
    private void setupToolbar() {
        ((BaseActivity) requireActivity()).setToolbarTitle("Travel orders");
        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }

    // =========================
    // RECYCLER + ADAPTER
    // =========================
    private void setupRecyclerView() {

        adapter = new TravelOrderRecyclerListAdapter();

        binding.rvTravelOrders.setLayoutManager(
                new LinearLayoutManager(getContext())
        );

        binding.rvTravelOrders.setAdapter(adapter);

        // 🔥 CAMERA CLICK FROM ITEM
        adapter.setOnCameraClickListener(item -> {
            openCamera(item);
        });
    }

    // =========================
    // LOAD DATA
    // =========================
    private void loadData() {

        int employeeId = viewModel.getSession().getEntityId();

        var params = new com.programdoo.transport.data.models.requests.travelOrders.SearchTravelOrdersParams();
        params.setEmployeeId(employeeId);

        viewModel.searchTravelOrders(params);
    }

    // =========================
    // OBSERVE
    // =========================
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

                        }, throwable -> {
                            Log.e("TRAVEL_DEBUG", "STREAM ERROR", throwable);
                        })
        );
    }

    // =========================
    // CAMERA INIT
    // =========================
    private void initCamera() {

        takePictureLauncher =
                registerForActivityResult(new ActivityResultContracts.TakePicture(), success -> {

                    if (success && imageUri != null) {

                        if (selectedItem != null) {

                            documentsViewModel.uploadDocument(
                                    imageUri,
                                    selectedItem,
                                    viewModel.getSession().getUserId(),
                                    requireContext()
                            );
                        }
                    }
                });
    }

    // =========================
    // OPEN CAMERA
    // =========================
    private void openCamera(TravelOrderDto item) {

        selectedItem = item;

        imageUri = createImageUri();
        takePictureLauncher.launch(imageUri);
    }

    // =========================
    // URI CREATION
    // =========================
    private Uri createImageUri() {

        File file = new File(
                requireContext().getCacheDir(),
                "camera_" + System.currentTimeMillis() + ".jpg"
        );

        return androidx.core.content.FileProvider.getUriForFile(
                requireContext(),
                requireContext().getPackageName() + ".fileprovider",
                file
        );
    }

    // =========================
    // CLEANUP
    // =========================
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
        binding = null;
    }
}