package com.programdoo.transport.ui.pages.trainees;

import android.os.Bundle;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TraineesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* fragment transaction se koristi za prelazenje po fragmentima.
         * dobija se iz fragment managera.
         * getSupportFragmentManager() se koristi u activity-ju.
         * u fragmentu, koristi se getParentFragmentManager() - jer activity drzi fragment
         * manager i zaduzen je za prelazenje po fragmentima.
         * fragment transaction mora da pocne sa beginTransaction i da se zavrsi sa commit.
         * u ovom slucaju se prelazi iz activity-ja u fragment, pa je dovoljno pozvati
         * replace(fragmentContainerResourceId, destinationFragmentInstance).
         * NavigationUtil klasa sadrzi pomocne funkcije za navigaciju izmedju dva fragmenta. */
        NavigationUtil.navigate(this, R.id.fragmentFrame, new TraineesListFragment(), null);
    }
}
