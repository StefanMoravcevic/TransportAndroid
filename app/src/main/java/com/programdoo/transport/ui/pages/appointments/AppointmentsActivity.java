package com.programdoo.transport.ui.pages.appointments;

import android.content.Intent;
import android.os.Bundle;

import com.programdoo.transport.R;
import com.programdoo.transport.ui.pages.BaseActivity;
import com.programdoo.transport.utils.Constants;
import com.programdoo.transport.utils.NavigationUtil;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AppointmentsActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);

        Intent i = getIntent();
        int traineeId = i.getIntExtra(Constants.ARG_TRAINEE_ID, 0);
        if (traineeId != 0) {
            NavigationUtil.navigate(this, R.id.fragmentFrame, new AppointmentsListFragment(), null);
        }
        else
            NavigationUtil.navigate(this, R.id.fragmentFrame, new AppointmentsCalendarFragment(), null);
    }
}
