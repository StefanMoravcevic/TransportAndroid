package com.programdoo.transport.ui.pages.travelOrders;

import android.os.Bundle;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.ui.pages.trainees.TraineesListFragment;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TravelOrdersActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NavigationUtil.navigate(this, R.id.fragmentFrame, new TravelOrdersListFragment(), null);
    }
}
