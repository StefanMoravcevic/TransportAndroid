package com.programdoo.transport.ui.pages;

import android.content.Intent;
import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.ui.pages.login.LoginActivity;
import com.programdoo.transport.ui.pages.menu.ClientMenuActivity;
import com.programdoo.transport.ui.pages.menu.MenuActivity;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

public class MainActivity extends BaseActivity {
    BaseViewModel viewModel;
    @Override
    public void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);

        viewModel = new ViewModelProvider(this).get(BaseViewModel.class);

        viewModel.getSession().initialize();
        if (viewModel.getSession().isUserStaff()) {
            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
        }
        else if (viewModel.getSession().isUserClient()) {
            Intent i = new Intent(this, ClientMenuActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
        else {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

        finish();
    }
}
