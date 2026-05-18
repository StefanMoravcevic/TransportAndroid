package com.programdoo.transport.ui.pages.menu;

import android.os.Bundle;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ClientMenuActivity extends BaseActivity {
    @Override
    protected void onCreate(
            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationUtil.navigate(this, R.id.fragmentFrame, new ClientMenuFragment(), null);
    }
}
