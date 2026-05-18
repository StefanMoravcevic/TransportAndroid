package com.programdoo.transport.ui.pages.trainees;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.companies.OrgUnitDto;
import com.programdoo.transport.data.models.dtos.employees.EmployeeDto;
import com.programdoo.transport.data.models.dtos.trainees.TraineeDto;
import com.programdoo.transport.data.models.requests.companies.SearchOrgUnitsParams;
import com.programdoo.transport.data.models.requests.employees.SearchEmployeesParams;
import com.programdoo.transport.databinding.FragmentTraineesListBinding;
import com.programdoo.transport.ui.adapters.ToolbarAction;
import com.programdoo.transport.ui.adapters.TraineesRecyclerViewAdapter;
import com.programdoo.transport.data.models.requests.trainees.SearchTraineesParams;
import com.programdoo.transport.ui.callbacks.SwipeDeleteCallback;
import com.programdoo.transport.ui.decorators.ListItemDecoration;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.SimpleTextWatcher;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.trainees.TraineesListViewModel;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TraineesListFragment extends BaseFragment {
    private FragmentTraineesListBinding binding;
    /**
     * adapter koji definise kako se prikazuje list item vezbaca. pogledaj BaseRecyclerViewAdapter
     * za vise informacija o adapterima.
     */
    private TraineesRecyclerViewAdapter adapter;
    private TraineesListViewModel viewModel;
    private Runnable filterRunnable;

    @Override
    public String TAG() { return Constants.FRAG_TRAINEES_LIST; }

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(TraineesListViewModel.class);
        /* build-a binding. svaki layout ce da generise binding klasu. ako je ime layout-a
         * activity_x (fragment_x), generisace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a. */
        binding = FragmentTraineesListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new TraineesRecyclerViewAdapter(requireContext(), new ArrayList<>());
        adapter.setUnselectedIconTint(requireContext(), R.color.primaryLighter);
        // postavi adapter za recycler view
        binding.rvTrainees.setAdapter(adapter);
        // postavi manager za layout, u suprotnom ne zna kako da ih crta
        binding.rvTrainees.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvTrainees.smoothScrollToPosition(0);
        /* item decoration je pomocna klasa za recycler view koja definise "dekoraciju" koja se crta
         * za item-e. ovaj konkretan dodaje divider liniju ispod svakog itema osim poslednjeg.
         * za vise info pogledati BaseItemDecoration */

        binding.rvTrainees.addItemDecoration(new ListItemDecoration(getContext(), getContext().getColor(R.color.primaryLighter), 1));

        ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.title_traineesList));
        ((BaseActivity) requireActivity()).clearToolbarSubtitle();

        ((BaseActivity) requireActivity()).setToolbarActions(List.of(
                new ToolbarAction(
                        R.id.action_search,
                        R.drawable.icon_search,
                        R.string.label_search,
                        MenuItem.SHOW_AS_ACTION_ALWAYS,
                        R.color.primaryLighter,
                        item -> {
                            binding.searchbar.setVisibility(
                                    binding.searchbar.getVisibility() == View.GONE ?
                                            View.VISIBLE : View.GONE
                            );
                            return true;
                        }
                )
        ));

        binding.searchbar.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                Handler handler = view.getHandler();
                if (filterRunnable != null) {
                    handler.removeCallbacks(filterRunnable);
                }
                filterRunnable = () -> adapter.getFilter().filter(editable.toString());
                handler.postDelayed(filterRunnable, 300);
            }
        });

        binding.swipeLayout.setOnRefreshListener(() -> viewModel.refreshData());
        binding.swipeLayout.setColorSchemeResources(R.color.primary, R.color.secondary);
        viewModel.getRefreshTraineesCompleted().observe(getViewLifecycleOwner(), o ->
                binding.swipeLayout.setRefreshing(false));

        /* postavi akciju na item click u adapteru (to jest, u listi) */
        adapter.setOnClickListener((v, position, trainee) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_TRAINEE_ID, trainee.getId());
            NavigationUtil.navigate(this, R.id.fragmentFrame, new TraineeInfoFragment(), bundle);
        });
        /* isto za long click */
        adapter.setOnLongClickListener((v, position, trainee) -> {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.ARG_TRAINEE_ID, trainee.getId());
            bundle.putBoolean(Constants.ARG_EDIT_MODE, true);
            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeBasicInfoFragment(), bundle);
        });
        // postavlja akciju na dugme
        binding.fabNewTrainee.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putBoolean(Constants.ARG_EDIT_MODE, false);
            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeBasicInfoFragment(), bundle);
        });
        /* registrovanje observera nad LiveData promenljivom. vise o tome u TraineesListViewModel
           i EditTraineeViewModel klasi. ovaj observer prima listu vezbaca od API-ja i postavlja
           je u adapter. */
        viewModel.getTrainees().observe(getViewLifecycleOwner(), data
                -> adapter.setData(data));
        /* registrovanje observera nad SingleLiveEvent promenljivom, koja se moze naci unutar
         * BaseViewModel klase. vise o tome u SingleLiveEvent. */
        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msgId ->
                UiUtil.makeToast(requireActivity(), requireContext(), getString(msgId)));
        /* ovo i automatski dovlaci vezbace */
        setupChips();
        UiUtil.enableSwipeDelete(binding.rvTrainees, new SwipeDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                TraineesRecyclerViewAdapter adapter = (TraineesRecyclerViewAdapter) binding.rvTrainees.getAdapter();
                TraineeDto trainee = adapter.getItemAt(position);
                viewModel.deleteTrainee(trainee.getId(), viewModel.getSession().getUserId());
            }
        });
    }

    private void setupChips() {
        binding.cAllOrgUnits.setOnClickListener(v -> {
            viewModel.setCurrentOrgUnit(null);
            SearchTraineesParams searchParams = new SearchTraineesParams();
//            int employeeId = viewModel.getPreferences().getInt(Constants.KEY_EMPLOYEE_ID);
//            if (employeeId > 0) searchParams.trainerId = employeeId;
            viewModel.getTraineesRepository().searchTrainees(searchParams);

            SearchEmployeesParams employeesParams = new SearchEmployeesParams();
            viewModel.getEmployeesRepository().searchEmployees(employeesParams);
        });

        viewModel.getOrgUnits().observe(getViewLifecycleOwner(), orgUnits -> {
            if (orgUnits != null && !orgUnits.isEmpty()) {
                binding.cgOrgUnitFilters.removeAllViews();
                binding.cgOrgUnitFilters.addView(binding.cAllOrgUnits);

                for (OrgUnitDto unit: orgUnits) {
                    Chip chip = new Chip(getContext());
                    chip.setText(unit.getName());
                    UiUtil.chipSetup(getContext(), chip);

                    chip.setOnClickListener(v -> {
                        viewModel.setCurrentOrgUnit(unit.getId());
                        SearchTraineesParams traineesParams = new SearchTraineesParams();
                        traineesParams.orgUnitId = unit.getId();
                        adapter.extendedFilter(traineesParams);

                        binding.cAllEmployees.setChecked(true);
                        SearchEmployeesParams employeesParams = new SearchEmployeesParams();
                        employeesParams.orgUnitId = unit.getId();
                        viewModel.getEmployeesRepository().searchEmployees(employeesParams);
                    });

                    binding.cgOrgUnitFilters.addView(chip);
                }

                /* dovuci inicijalnu listu vezbaca */
                binding.cAllOrgUnits.performClick();
            }
        });

        binding.cAllEmployees.setOnClickListener(v -> {
            viewModel.setCurrentEmployee(null);
            SearchTraineesParams params = new SearchTraineesParams();
            if (viewModel.getCurrentOrgUnit() != null) {
                params.orgUnitId = viewModel.getCurrentOrgUnit();
            }
            adapter.extendedFilter(params);
        });

        viewModel.getEmployees().observe(getViewLifecycleOwner(), employees -> {
            if (employees != null && !employees.isEmpty()) {
                binding.cgEmployeeFilters.removeAllViews();
                binding.cgEmployeeFilters.addView(binding.cAllEmployees);
                binding.cAllEmployees.setChecked(true);

                for (EmployeeDto employee: employees) {
                    Chip chip = new Chip(getContext());
                    chip.setText(employee.getFullNameShort());
                    UiUtil.chipSetup(getContext(), chip);

                    chip.setOnClickListener(v -> {
                        viewModel.setCurrentEmployee(employee.getId());
                        SearchTraineesParams searchParams = new SearchTraineesParams();
                        searchParams.trainerId = employee.getId();
                        if (viewModel.getCurrentOrgUnit() != null) {
                            searchParams.orgUnitId = viewModel.getCurrentOrgUnit();
                        }
                        adapter.extendedFilter(searchParams);
                    });

                    binding.cgEmployeeFilters.addView(chip);
                }
            }

            binding.swipeLayout.setRefreshing(false);
        });

        SearchOrgUnitsParams searchParams = new SearchOrgUnitsParams();
        int employeeId = viewModel.getPreferences().getInt(Constants.KEY_EMPLOYEE_ID);
        if (employeeId > 0) {
            searchParams.employeeId = employeeId;
        }
        viewModel.getCompaniesRepository().searchOrgUnits(searchParams);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}