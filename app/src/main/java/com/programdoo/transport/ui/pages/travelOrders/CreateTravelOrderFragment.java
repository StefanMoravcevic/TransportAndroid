package com.programdoo.transport.ui.pages.travelOrders;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.snackbar.Snackbar;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.masterData.MasterDataDto;
import com.programdoo.transport.data.models.dtos.memberships.MembershipCardDto;
import com.programdoo.transport.data.models.dtos.poolCarReservations.SavePoolCarReservationRequestModel;
import com.programdoo.transport.data.models.dtos.travelOrders.SaveTravelOrderRequestModel;
import com.programdoo.transport.databinding.FragmentCreateTravelOrderBinding;
import com.programdoo.transport.databinding.FragmentTravelOrdersListBinding;
import com.programdoo.transport.ui.adapters.EmployeesRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.MasterDataRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.viewmodels.documents.DocumentsViewModel;
import com.programdoo.transport.ui.viewmodels.travelOrders.CreateTravelOrderViewModel;
import com.programdoo.transport.ui.viewmodels.travelOrders.TravelOrdersListViewModel;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.DateUtil;
import com.programdoo.transport.utils.UiUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@AndroidEntryPoint
public class CreateTravelOrderFragment extends BaseFragment {

    private FragmentCreateTravelOrderBinding binding;

    private MasterDataRecyclerViewAdapter masterDataAdapterTVehiclesAdapter;
    private MasterDataRecyclerViewAdapter masterDataStatesAdapter;
    private MasterDataRecyclerViewAdapter masterDataStatusesAdapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private CreateTravelOrderViewModel viewModel;

    private long employeeId;

    private List<EmployeeDto> employees = new ArrayList<>();

    private EmployeeDto selectedEmployee;
    private MasterDataDto selectedTransportationVehicle;
    private MasterDataDto selectedState;
    private MasterDataDto selectedStatus;

    @Override
    public String TAG() {
        return Constants.FRAG_TRAVEL_ORDER_CREATE;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        binding = FragmentCreateTravelOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(CreateTravelOrderViewModel.class);
        masterDataAdapterTVehiclesAdapter = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());
        masterDataStatesAdapter = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());
        masterDataStatusesAdapter = new MasterDataRecyclerViewAdapter(requireContext(), new ArrayList<>());
        setupToolbar();
        setupUI();
        readArguments();
        setupObservers();
        loadData();
        UiUtil.dateTimePickerSetup(this, binding.selDepartureDate);
        UiUtil.dateTimePickerSetup(this, binding.selReturnDate);
        UiUtil.dateTimePickerSetup(this, binding.selBorderCrossingFromDate);
        UiUtil.dateTimePickerSetup(this, binding.selBorderCrossingToDate);
        binding.cbForeignExpensesPaid.setChecked(true);





    }
    private void readArguments() {
        if (getArguments() != null) {
            employeeId = getArguments().getLong("employeeId", -1);
        }
    }

    private void loadData() {

        viewModel.loadEmployees();

        viewModel.getTransportationVehicles().observe(getViewLifecycleOwner(), transportationVehicles -> {
            masterDataAdapterTVehiclesAdapter.setData(transportationVehicles);
        });

        viewModel.getStates().observe(getViewLifecycleOwner(), states -> {
            masterDataStatesAdapter.setData(states);
        });


    }

    private void bindSelectInputs() {

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

        UiUtil.selectSetup(
                requireContext(),
                masterDataAdapterTVehiclesAdapter,
                binding.selTransportationVehicles,
                (view, position, item) -> {
                    binding.selTransportationVehicles.toggleSelected(position);

                    if (item instanceof MasterDataDto) {
                        selectedTransportationVehicle = (MasterDataDto) item;
                    }
                }
        );

        UiUtil.selectSetup(
                requireContext(),
                masterDataStatesAdapter,
                binding.selStates,
                (view, position, item) -> {
                    binding.selStates.toggleSelected(position);
                    if (item instanceof MasterDataDto) {
                        selectedState = (MasterDataDto) item;
                    }
                }
        );


    }

    private void setupUI() {

        binding.buttonCancel.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        binding.buttonSave.setOnClickListener(v -> saveTravelOrder());
    }

    @SuppressLint("AutoDispose")
    private void setupObservers() {

        disposables.add(
                viewModel.getEmployees()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(employeeList -> {

                            employees = employeeList;

                            bindSelectInputs();

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

        disposables.add(
                viewModel.getSaveResult()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(result -> {

                            Snackbar snackbar = Snackbar.make(
                                    binding.getRoot(), getString(R.string.label_succesfullySaved),
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

    @SuppressLint("AutoDispose")
    private void saveTravelOrder() {

        if (selectedEmployee == null) {
            binding.selEmployee.setError("Employee required");
            return;
        }

        viewModel.getNewDocumentNumber(3, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(number -> {

                    SaveTravelOrderRequestModel req = new SaveTravelOrderRequestModel();

                    req.travelOrderNumber = number;

                    req.employeeId = selectedEmployee.getId();
                    req.transportationVehicleId = selectedTransportationVehicle.getValue();
                    req.stateId = selectedState.getValue();
                    req.travelOrderStatusId = 1;
                    req.travelGoal = binding.selTravelGoal.getText().toString();
                    req.destination = binding.selTravelPlace.getText().toString();
                    req.payedCosts = binding.cbForeignExpensesPaid.isChecked();

                    String dateFromText = binding.selDepartureDate.getText();
                    String dateToText = binding.selReturnDate.getText();
                    String borderCrossingFromText = binding.selBorderCrossingFromDate.getText();
                    String borderCrossingToText = binding.selBorderCrossingToDate.getText();

                    LocalDateTime dateFrom = DateUtil.parseClientDateTime(dateFromText);
                    LocalDateTime dateTo = DateUtil.parseClientDateTime(dateToText);
                    LocalDateTime borderCrossingFrom = DateUtil.parseClientDateTime(borderCrossingFromText);
                    LocalDateTime borderCrossingTo = DateUtil.parseClientDateTime(borderCrossingToText);

                    req.date = LocalDateTime.parse(DateUtil.apiFormatNew(dateFrom));
                    req.returnDate = LocalDateTime.parse(DateUtil.apiFormatNew(dateTo));
                    req.borderCrossingFromDate = LocalDateTime.parse(DateUtil.apiFormatNew(borderCrossingFrom));
                    req.borderCrossingToDate = LocalDateTime.parse(DateUtil.apiFormatNew(borderCrossingTo));

                    req.userId = viewModel.getSession().getUserId();

                    viewModel.saveTravelOrder(req);

                }, throwable -> {

                    Log.e("SAVE_TRAVEL_ORDER", "Failed to get document number", throwable);

                });
    }

    private void setupToolbar() {
        ((BaseActivity) requireActivity())
                .setToolbarTitle(getString(R.string.label_createTravelOrder));

        ((BaseActivity) requireActivity()).clearToolbarSubtitle();
        ((BaseActivity) requireActivity()).clearToolbarActions();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        disposables.clear();

        binding = null;
    }
}
