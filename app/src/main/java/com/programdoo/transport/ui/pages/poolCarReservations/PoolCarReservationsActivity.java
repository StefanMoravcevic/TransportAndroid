package com.programdoo.transport.ui.pages.poolCarReservations;

import android.content.Intent;
import android.os.Bundle;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PoolCarReservationsActivity extends  BaseActivity {
    @Override
    public void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        Intent i = getIntent();
        NavigationUtil.navigate(this, R.id.fragmentFrame, new PoolCarReservationsCalendarFragment(), null);
    }
}
