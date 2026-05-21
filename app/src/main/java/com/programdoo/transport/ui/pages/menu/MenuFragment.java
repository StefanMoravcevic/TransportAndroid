package com.programdoo.transport.ui.pages.menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.databinding.FragmentMenuBinding;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.pages.appointments.AppointmentsActivity;
import com.programdoo.transport.ui.pages.login.LoginActivity;
import com.programdoo.transport.ui.pages.memberships.MembershipActivity;
import com.programdoo.transport.ui.pages.scannedpackages.ScannedPackageActivity;
import com.programdoo.transport.ui.pages.settings.SettingsActivity;
import com.programdoo.transport.ui.pages.trainees.TraineesActivity;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.ui.viewmodels.MenuViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MenuFragment extends BaseFragment {
    FragmentMenuBinding binding;
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
        ((BaseActivity) requireActivity()).setToolbarTitle(getString(R.string.label_menu));

       /* binding.tvTrainees.setOnClickListener(v -> {
            *//* intent se koristi kad se prelazi iz jednog activity-ja u drugi.
             * prvi argument je instanca trenutnog activity-ja, drugi je klasa ciljnog activity-ja.
             * moguce je postaviti podatke u intent ako je potrebno preneti ih iz jednog u drugi
             * activity pomocu intent.putExtra() i intent.putExtras() *//*
            Intent intent = new Intent(requireActivity(), TraineesActivity.class);
            startActivity(intent);
        });
        binding.tvAppointments.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), AppointmentsActivity.class);
            startActivity(intent);
        });

        binding.tvMemberships.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), MembershipActivity.class);
            startActivity(intent);
        });

        binding.tvSettings.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), SettingsActivity.class);
            startActivity(intent);
        });*/

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
    }
}
