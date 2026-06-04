package com.programdoo.transport.ui.pages.poolCarReservations;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import com.google.android.material.snackbar.Snackbar;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.PoolCarReservationDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.vehicles.VehicleDto;
import com.programdoo.transport.databinding.FragmentCreatePoolcarReservationBinding;
import com.programdoo.transport.ui.adapters.EmployeesRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.VehiclesRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.poolCarReservations.CreatePoolCarReservationViewModel;
import com.programdoo.transport.ui.viewmodels.poolCarReservations.PoolCarReservationCalendarViewModel;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.UiUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CreatePoolCarReservationFragment extends BaseFragment {

    private FragmentCreatePoolcarReservationBinding binding;
    private CreatePoolCarReservationViewModel viewModel;

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final CompositeDisposable disposablesVehicles = new CompositeDisposable();

    private long employeeId;

    private List<EmployeeDto> employees = new ArrayList<>();
    private List<VehicleDto> vehicles = new ArrayList<>();

    private EmployeeDto selectedEmployee;
    private VehicleDto selectedVehicle;

    @Override
    public String TAG() {
        return "FRAG_CREATE_POOL_CAR_CALENDAR";
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCreatePoolcarReservationBinding
                .inflate(inflater, container, false);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this)
                .get(CreatePoolCarReservationViewModel.class);

        readArguments();
        setupUI();
        setupObservers();
        setupToolbar();
        loadData();
        UiUtil.dateTimePickerSetup(this, binding.validFrom);
        UiUtil.dateTimePickerSetup(this, binding.validTo);
    }


    private void readArguments() {
        if (getArguments() != null) {
            employeeId = getArguments().getLong("employeeId", -1);
        }
    }

    private void setupUI() {

        binding.btnCancel.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        binding.btnSave.setOnClickListener(v -> saveReservation());
    }

    // ---------------- LOAD ----------------

    private void loadData() {
        viewModel.loadEmployees();
        viewModel.loadVehicles();
    }

    // ---------------- OBSERVERS ----------------

    @SuppressLint("AutoDispose")
    private void setupObservers() {

        disposables.add(
                viewModel.getEmployees()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(employeeList -> {

                            employees = employeeList;

                            bindEmployees();

                            // automatski selektuj ulogovanog zaposlenog
                            for (int i = 0; i < employees.size(); i++) {

                                EmployeeDto employee = employees.get(i);

                                if (employee.getId() == employeeId) {

                                    selectedEmployee = employee;

                                    binding.selEmployee.toggleSelected(i);

                                    break;
                                }
                            }

                        }, Throwable::printStackTrace)
        );

        disposablesVehicles.add(
                viewModel.getVehicles()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(vehiclesList -> {

                            Log.d("VEHICLES_DEBUG", "SIZE: " +
                                    (vehiclesList != null ? vehiclesList.size() : -1));

                            Log.d("VEHICLES_DEBUG", "DATA: " + vehiclesList);

                            vehicles = vehiclesList;

                            bindVehicles();

                        }, Throwable::printStackTrace)
        );

        disposables.add(
                viewModel.getSaveResult()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {

                            Snackbar snackbar = Snackbar.make(
                                    binding.getRoot(),
                                    "Rezervacija uspešno sačuvana!",
                                    Snackbar.LENGTH_SHORT
                            );

                            snackbar.setBackgroundTint(Color.parseColor("#2E7D32")); // green
                            snackbar.setTextColor(Color.WHITE);

                            snackbar.show();

                            binding.getRoot().postDelayed(() -> {
                                requireActivity()
                                        .getSupportFragmentManager()
                                        .popBackStack();
                            }, 500);

                        }, Throwable::printStackTrace)
        );
    }
    // ---------------- BIND VEHICLES ----------------

    private void bindVehicles()
    {
        VehiclesRecyclerViewAdapter adapter =
                new VehiclesRecyclerViewAdapter(
                        requireContext(),
                        vehicles
                );

        UiUtil.selectSetup(
                requireContext(),
                adapter,
                binding.selVehicle,
                (view, position, item) -> {

                    binding.selVehicle.toggleSelected(position);

                    if (item instanceof VehicleDto) {
                        selectedVehicle = (VehicleDto) item;
                    }
                }
        );
    }

    // ---------------- BIND EMPLOYEES ----------------

    private void bindEmployees() {

        EmployeesRecyclerViewAdapter adapter =
                new EmployeesRecyclerViewAdapter(
                        requireContext(),
                        employees
                );

        UiUtil.selectSetup(
                requireContext(),
                adapter,
                binding.selEmployee,
                (view, position, item) -> {

                    binding.selEmployee.toggleSelected(position);

                    if (item instanceof EmployeeDto) {
                        selectedEmployee = (EmployeeDto) item;
                    }
                }
        );
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        disposables.clear();

        binding = null;
    }

    // ---------------- SAVE ----------------
    private void saveReservation() {

        if (selectedEmployee == null) {
            binding.selEmployee.setError("Employee required");
            return;
        }

        if (selectedVehicle == null) {
            binding.selVehicle.setError("Vehicle required");
            return;
        }

        SavePoolCarReservationRequestModel req =
                new SavePoolCarReservationRequestModel();

        req.employeeId = selectedEmployee.getId();
        req.vehicleId = selectedVehicle.getId();

        req.note = binding.etNote.getText().toString();

        String dateFromText = binding.validFrom.getText();

        LocalDateTime dateFrom = DateUtil.parseClientDateTime(dateFromText);

        String dateToText = binding.validTo.getText();

        LocalDateTime dateTo = DateUtil.parseClientDateTime(dateToText);

        req.dateFrom = LocalDateTime.parse(DateUtil.apiFormatNew(dateFrom));
        req.dateTo = LocalDateTime.parse(DateUtil.apiFormatNew(dateTo));

        req.createdBy = 2;
        req.deletedBy = null;
        req.isDivorced = false;
        req.isDeleted = false;

        viewModel.savePoolCarReservation(req);
    }
    private void setupToolbar() {
        ((BaseActivity) requireActivity())
                .setToolbarTitle("Create reservation");

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }
}