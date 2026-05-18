package com.programdoo.transport.ui.pages.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.dtos.activities.ActivityDto;
import com.programdoo.transport.data.models.requests.activities.SearchActivitiesParams;
import com.programdoo.transport.databinding.FragmentActivitiesListBinding;
import com.programdoo.transport.ui.adapters.ActivitiesRecyclerViewAdapter;
import com.programdoo.transport.ui.adapters.ToolbarAction;
import com.programdoo.transport.ui.callbacks.SwipeDeleteCallback;
import com.programdoo.transport.ui.decorators.ListItemDecoration;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.pages.trainees.EditTraineeBasicInfoFragment;
import com.programdoo.transport.ui.pages.trainees.TraineeInfoFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.activities.ActivitiesListViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ActivitiesListFragment extends BaseFragment {

    private FragmentActivitiesListBinding binding;
    private ActivitiesRecyclerViewAdapter adapter;
    private ActivitiesListViewModel viewModel;

    @Override
    public String TAG() {
        return Constants.FRAG_ACTIVITIES_LIST;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(ActivitiesListViewModel.class);
        binding = FragmentActivitiesListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new ActivitiesRecyclerViewAdapter(requireContext(), new ArrayList<>());
        adapter.setUnselectedIconTint(requireContext(), R.color.primaryLighter);
        binding.rvActivities.addItemDecoration(new ListItemDecoration(requireContext(), requireContext().getColor(R.color.primaryLighter), 1));

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

        // postavi adapter za recycler view
        binding.rvActivities.setAdapter(adapter);
        // postavi manager za layout, u suprotnom ne zna kako da ih crta
        binding.rvActivities.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvActivities.smoothScrollToPosition(0);

        SearchActivitiesParams params = new SearchActivitiesParams();
        if (getArguments() != null && getArguments().containsKey(Constants.ARG_TRAINEE_ID)) {
            params.traineeId = getArguments().getInt(Constants.ARG_TRAINEE_ID);
        }
        var traineeName = getArguments().getString(Constants.ARG_TRAINEE_NAME, "");
        viewModel.getTraineesRepository().searchActivities(params);
        if (params.traineeId != null && params.traineeId > 0){
            ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.title_activitiesList));
            ((BaseActivity) requireActivity()).setToolbarSubtitle(traineeName);
        }
        else {
            ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.title_activitiesList));
        }
        /* postavi akciju na item click u adapteru (to jest, u listi) */
        adapter.setOnClickListener((v, position, activity) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.ARG_ACTIVITY_ID, activity.getId());
            NavigationUtil.navigate(this, R.id.fragmentFrame, new TraineeInfoFragment(), bundle);
        });
        /* isto za long click */
        adapter.setOnLongClickListener((v, position, activity) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.ARG_ACTIVITY_ID, activity.getId());
            NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeBasicInfoFragment(), bundle);
        });
        // postavlja akciju na dugme
        binding.fabNewActivity.setOnClickListener(v ->
                NavigationUtil.navigate(this, R.id.fragmentFrame, new EditTraineeBasicInfoFragment(), null));
        /* registrovanje observera nad LiveData promenljivom. vise o tome u TraineesListViewModel
           i EditTraineeViewModel klasi. ovaj observer prima listu vezbaca od API-ja i postavlja
           je u adapter. */
        viewModel.getActivities().observe(getViewLifecycleOwner(), data
                -> adapter.setData(data));
        /* registrovanje observera nad SingleLiveEvent promenljivom, koja se moze naci unutar
         * BaseViewModel klase. vise o tome u SingleLiveEvent. */
        viewModel.getToastEvent().observe(getViewLifecycleOwner(), msgId
                -> UiUtil.makeToast(getActivity(), getContext(), getString(msgId)));
        /* ovo i automatski dovlaci vezbace */
        UiUtil.enableSwipeDelete(binding.rvActivities, new SwipeDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                ActivitiesRecyclerViewAdapter adapter = (ActivitiesRecyclerViewAdapter) binding.rvActivities.getAdapter();
                ActivityDto activity = adapter.getItemAt(position);
                viewModel.deleteActivity(activity.getId(), viewModel.getSession().getUserId());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}