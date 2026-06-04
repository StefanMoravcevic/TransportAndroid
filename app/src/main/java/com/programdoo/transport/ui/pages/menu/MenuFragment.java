package com.programdoo.transport.ui.pages.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.databinding.FragmentMenuBinding;
import com.programdoo.transport.ui.adapters.ToolbarAction;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.pages.appointments.AppointmentsActivity;
import com.programdoo.transport.ui.pages.login.LoginActivity;
import com.programdoo.transport.ui.pages.memberships.MembershipActivity;
import com.programdoo.transport.ui.pages.notifications.NotificationActivity;
import com.programdoo.transport.ui.pages.poolCarReservations.PoolCarReservationsActivity;
import com.programdoo.transport.ui.pages.poolCarReservations.PoolCarReservationsListActivity;
import com.programdoo.transport.ui.pages.scannedpackages.ScannedPackageActivity;
import com.programdoo.transport.ui.pages.settings.SettingsActivity;
import com.programdoo.transport.ui.pages.trainees.TraineesActivity;
import com.programdoo.transport.ui.pages.travelOrders.TravelOrdersActivity;
import com.programdoo.transport.ui.viewmodels.employees.ExpiringDocumentsViewModel;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.ui.viewmodels.MenuViewModel;

import java.util.Collections;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MenuFragment extends BaseFragment {
    FragmentMenuBinding binding;

    private ToolbarAction notificationAction;
    private ExpiringDocumentsViewModel expiringViewModel;
    MenuViewModel viewModel;

    @Override
    public String TAG() {
        return Constants.FRAG_MENU;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            @Nullable Bundle savedStateInstance) {
        /* build-a binding. svaki layout ce da generise svoju klasu. ako je ime layout-a
         * activity_x (fragment_x), imace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a.  */
        binding = FragmentMenuBinding.inflate(inflater, container, false);
        /* ovde ne dovlacimo view model jer nam nije potreban. ovaj fragment sluzi samo kao
         * prelaz na activity-je u kojim se zapravo izvrsava logika. */
        viewModel = new ViewModelProvider(this).get(MenuViewModel.class);

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedStateInstance) {

        ((BaseActivity) requireActivity())
                .setToolbarTitle(getString(R.string.label_menu));

        expiringViewModel = new ViewModelProvider(this)
                .get(ExpiringDocumentsViewModel.class);

        int employeeId = expiringViewModel.getLoggedEmployeeId();
        expiringViewModel.loadExpiringNotificationDocuments(employeeId);

        notificationAction = new ToolbarAction(
                R.id.action_notifications,
                R.drawable.icon_notification,
                R.string.notifications,
                MenuItem.SHOW_AS_ACTION_ALWAYS,
                R.color.primary,
                item -> {
                    Intent intent = new Intent(requireActivity(), NotificationActivity.class);
                    startActivity(intent);
                    return true;
                }
        );

        ((BaseActivity) requireActivity())
                .setToolbarActions(Collections.singletonList(notificationAction));

        expiringViewModel.getUnreadCount()
                .observe(getViewLifecycleOwner(), count -> {

                    int unread = count != null ? count : 0;

                    ((BaseActivity) requireActivity())
                            .setNotificationCount(unread);
                });

        binding.tvLogout.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), LoginActivity.class);
            viewModel.getPreferences().clearTokens();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        binding.tvScanPackages.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), ScannedPackageActivity.class);
            startActivity(intent);
        });

        binding.tvPoolCars.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), PoolCarReservationsActivity.class);
            startActivity(intent);
        });

        binding.tvChargedVehicles.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), PoolCarReservationsListActivity.class);
            startActivity(intent);
        });

        binding.tvTravelOrders.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), TravelOrdersActivity.class);
            startActivity(intent);
        });

        binding.tvSettings.setOnClickListener(v -> {
            Intent i = new Intent(requireActivity(), SettingsActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        int employeeId = expiringViewModel.getLoggedEmployeeId();
        expiringViewModel.loadExpiringNotificationDocuments(employeeId);
    }
}

