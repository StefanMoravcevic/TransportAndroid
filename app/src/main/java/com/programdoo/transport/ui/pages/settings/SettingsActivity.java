package com.programdoo.transport.ui.pages.settings;

import android.os.Bundle;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        NavigationUtil.navigate(this, R.id.fragmentFrame, new SettingsFragment(), null);
    }
}
