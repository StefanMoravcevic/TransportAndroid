package com.programdoo.transport.ui.pages.scannedpackages;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.scannedpackages.SaveScannedPackagesRequestModel;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.scannedPackages.ScanPackageViewModel;

import java.time.LocalDateTime;
import java.util.Date;

public class ScanPackageFragment extends BaseFragment {

    private ScanPackageViewModel viewModel;
    private FusedLocationProviderClient fusedLocationClient;

    private ImageView success;
    private EditText input;
    private double currentLat = 0;
    private double currentLng = 0;

    private TextView txtGpsStatus;
    private boolean locationLoaded = false;

    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private boolean isSaving = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan_package, container, false);

        success = view.findViewById(R.id.imgSuccess);
        input = view.findViewById(R.id.etScanInput);

        input.requestFocus();
        txtGpsStatus = view.findViewById(R.id.txtGpsStatus);
        input = view.findViewById(R.id.etScanInput);
        input.setEnabled(false);
        viewModel = new ViewModelProvider(this).get(ScanPackageViewModel.class);

        fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(requireActivity());

        setupEnter();
        observeEvents();
        startLocationUpdates();
        return view;
    }

    private void setupEnter() {

        input.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE) {

                if (isSaving) return true;

                String code = v.getText().toString().trim();

                if (code.isEmpty()) {
                    return true;
                }

                isSaving = true;

                SaveScannedPackagesRequestModel model =
                        new SaveScannedPackagesRequestModel();

                model.setPackageNo(code);
                model.setUserId(viewModel.getLoggedUserId());
                LocalDateTime now = LocalDateTime.now();
                model.setScannedDateTime(now);

                // koristi poslednju poznatu lokaciju (instant)
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
            }

            if (id == 2) {
                Toast.makeText(getContext(), "Error saving", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public String TAG() {
        return "ScanPackageFragment";
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
        )
                .setMinUpdateIntervalMillis(2000)
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

                    locationLoaded = true;

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
}