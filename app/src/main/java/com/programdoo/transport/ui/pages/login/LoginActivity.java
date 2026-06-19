package com.programdoo.transport.ui.pages.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.data.models.requests.accounts.TokenRequestModel;
import com.programdoo.transport.data.services.LocationTrackingService;
import com.programdoo.transport.databinding.ActivityLoginBinding;
import com.programdoo.transport.ui.pages.menu.ClientMenuActivity;
import com.programdoo.transport.ui.pages.menu.MenuActivity;
import com.programdoo.transport.utils.SimpleTextWatcher;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.login.LoginViewModel;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * login activity nema svoj fragment jer je jedina stvar koja se desava u njemu logovanje,
 * pa je sve prebaceno ovde.
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* build-a binding. svaki layout ce da generise svoju klasu. ako je ime layout-a
         * activity_x (fragment_x), imace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a.  */
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* crtaj edge-to-edge i ispod system bar-ova */
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary));

        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        insetsController.setAppearanceLightNavigationBars(true);
        insetsController.setAppearanceLightStatusBars(true);
        /* napravi padding za system bars */
        UiUtil.applySystemBarInsets(binding.getRoot(), true, true);

        /* dovuci view model. activity i fragment imaju po ViewModelStore koji cuva view model.
         * argument za ViewModelProvider je owner view modela. detaljnije se moze videti u
         * BaseViewModel. */
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        /* dovlaci edit text za username iz bindinga. postavlja text watcher koji prati promene
         * vrednosti u ovom polju i reaguje na njih. */
        binding.etUsername.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.tilUsername.setError(getString(R.string.msg_required));
                }
                else {
                    binding.tilUsername.setError(null);
                }
            }
        });
        binding.etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().isEmpty()) {
                    binding.tilPassword.setError(getString(R.string.msg_required));
                }
                else {
                    binding.tilPassword.setError(null);
                }
            }
        });
        binding.buttonLogin.setOnClickListener(view -> {
            if (areInputsValid()) {
                TokenRequestModel tokenModel = new TokenRequestModel(
                        binding.etUsername.getText().toString(),
                        binding.etPassword.getText().toString());
                viewModel.login(tokenModel);
            }
        });

        viewModel.getIntentEvent().observe(this, t -> {

            Intent serviceIntent = new Intent(LoginActivity.this, LocationTrackingService.class);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            /* intent se koristi kad se prelazi iz jednog activity-ja u drugi.
             * prvi argument je instanca trenutnog activity-ja, drugi je klasa ciljnog activity-ja.
             * moguce je postaviti podatke u intent ako je potrebno preneti ih iz jednog u drugi
             * activity pomocu intent.putExtra() i intent.putExtras() */
            if (viewModel.getSession().isUserStaff()) {
                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            else {
                Intent i = new Intent(LoginActivity.this, ClientMenuActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }

            finish();
        });
        viewModel.getToastEvent().observe(this, msgId -> {
            UiUtil.makeToast(this, getApplicationContext(), getString(msgId));
        });
    }

    private boolean areInputsValid() {
        boolean valid = true;

        if (binding.etUsername.getText().toString().isEmpty()) {
            binding.tilUsername.setError(getString(R.string.msg_required));
            valid = false;
        }
        if (binding.etPassword.getText().toString().isEmpty()) {
            binding.tilPassword.setError(getString(R.string.msg_required));
            valid = false;
        }

        return valid;
    }
}