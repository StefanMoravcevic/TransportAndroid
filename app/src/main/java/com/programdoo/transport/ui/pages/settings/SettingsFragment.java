package com.programdoo.transport.ui.pages.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.enums.Languages;
import com.programdoo.transport.databinding.FragmentEditSettingsBinding;
import com.programdoo.transport.ui.adapters.EnumRecyclerViewAdapter;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.settings.EditSettingsViewModel;

import java.util.Arrays;

public class SettingsFragment extends BaseFragment {
    private FragmentEditSettingsBinding binding;
    private EditSettingsViewModel viewModel;

    private EnumRecyclerViewAdapter langAdapter;

    @Override
    public String TAG() { return Constants.FRAG_EDIT_SETTINGS; }

    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedStateInstance) {
        viewModel = new ViewModelProvider(this).get(EditSettingsViewModel.class);
        binding = FragmentEditSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        langAdapter = new EnumRecyclerViewAdapter(requireContext(), Arrays.asList(Languages.values()));
        ((SettingsActivity) requireActivity()).setToolbarTitle(getString(R.string.label_settings));

        UiUtil.selectSetup(getContext(), langAdapter, binding.selLanguage, (v,position,item)
                -> binding.selLanguage.toggleSelected(position));

        binding.buttonSave.setOnClickListener(v -> {
            if (binding.selLanguage.isSelected()) {
                LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(binding.selLanguage.getSelectedDescription());
                AppCompatDelegate.setApplicationLocales(appLocale);
                requireActivity().finish();
            }
        });

        binding.buttonCancel.setOnClickListener(v -> requireActivity().finish());
    }
}
