package com.programdoo.transport.ui.pages;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuProvider;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.ViewModelProvider;

import com.programdoo.transport.R;
import com.programdoo.transport.databinding.ActivityBaseBinding;
import com.programdoo.transport.ui.adapters.ToolbarAction;
import com.programdoo.transport.ui.pages.login.LoginActivity;
import com.programdoo.transport.utils.UiUtil;
import com.programdoo.transport.ui.viewmodels.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BaseActivity extends AppCompatActivity {

    private int lastUnreadCount = 0;
    private ActivityBaseBinding binding;
    private BaseViewModel viewModel;

    private TextView notificationBadge;
    private List<ToolbarAction> actions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedStateInstance) {
        super.onCreate(savedStateInstance);
        viewModel = new ViewModelProvider(this).get(BaseViewModel.class);
        /* build-a binding. svaki layout ce da generise svoju klasu. ako je ime layout-a
         * activity_x (fragment_x), imace binding ActivityXBinding (FragmentXBinding).
         * preko binding-a moze da se pristupi svim ui komponentama iz xml-a. */
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* listener koji pokrece ciscenje activity-ja cim se svi njegovi fragmenti
         * ociste iz back stack-a. drugim recima, kad idemo unazad kroz fragmente
         * ka meniju, na poslednjem back-u ce preskociti activity i ici ce direktno na
         * meni (inace bi stao na praznoj activity stranici, sto bi zahtevalo jos jedan
         * back click) */
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
            }
        });

        viewModel.getNavigateToLoginEvent().observe(this, x -> {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        });

        /* crtaj edge-to-edge i ispod system bar-ova */
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.primary));

        WindowInsetsControllerCompat insetsController =
                new WindowInsetsControllerCompat(getWindow(), getWindow().getDecorView());
        insetsController.setAppearanceLightNavigationBars(true);
        insetsController.setAppearanceLightStatusBars(true);
        /* napravi padding za system bars */
        UiUtil.applySystemBarInsets(binding.getRoot(), true, true);

        setSupportActionBar(binding.toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(true);
        }

        addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
                menu.clear();

                for (ToolbarAction action : actions) {

                    MenuItem item = menu.add(
                            Menu.NONE,
                            action.id,
                            Menu.NONE,
                            getString(action.titleRes)
                    );

                    Drawable icon = ContextCompat.getDrawable(
                            BaseActivity.this,
                            action.iconRes
                    );

                    if (icon != null) {
                        icon = DrawableCompat.wrap(icon);
                        DrawableCompat.setTint(icon, getColor(action.tintColor));
                    }

                    item.setShowAsAction(action.showAsAction);

                    if (action.id == R.id.action_notifications) {

                        item.setActionView(R.layout.toolbar_notification_action);
                        View actionView = item.getActionView();

                        ImageButton button = actionView.findViewById(R.id.actionIcon);
                        TextView badge = actionView.findViewById(R.id.badge);

                        button.setImageDrawable(icon);

                        button.setOnClickListener(v -> {
                            if (action.handler != null) {
                                action.handler.onClick(item);
                            }
                        });

                        badge.setVisibility(View.GONE);

                        notificationBadge = badge;

                        continue;
                    }

                    item.setActionView(R.layout.toolbar_action_view);
                    View actionView = item.getActionView();

                    ImageButton button = actionView.findViewById(R.id.actionIcon);

                    button.setImageDrawable(icon);
                    button.setContentDescription(getString(action.titleRes));

                    if (action.onClickHint != null) {
                        button.setOnClickListener(v -> action.onClickHint.run());
                    }

                    if (action.onLongClick != null) {
                        button.setOnLongClickListener(v -> {
                            action.onLongClick.run();
                            return true;
                        });
                    }
                }
                restoreNotificationBadge();
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem item) {
                for (ToolbarAction action : actions) {
                    if (action.id == item.getItemId()) {
                        return action.handler.onClick(item);
                    }
                }

                return false;
            }
        });
    }

    public void setNotificationCount(int count) {

        lastUnreadCount = count;

        if (notificationBadge == null) return;

        if (count > 0) {
            notificationBadge.setVisibility(View.VISIBLE);
            notificationBadge.setText(String.valueOf(count));
        } else {
            notificationBadge.setVisibility(View.GONE);
        }
    }

    private void restoreNotificationBadge() {

        if (notificationBadge == null) return;

        if (lastUnreadCount > 0) {
            notificationBadge.setVisibility(View.VISIBLE);
            notificationBadge.setText(String.valueOf(lastUnreadCount));
        } else {
            notificationBadge.setVisibility(View.GONE);
        }
    }

    public void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(title);
    }

    public void setToolbarSubtitle(String subtitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setSubtitle(subtitle);
    }

    public void clearToolbarSubtitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setSubtitle("");
    }

    public void setToolbarColor(int colorId) {
        binding.toolbar.setBackgroundColor(ContextCompat.getColor(this, colorId));
    }

    public void setToolbarActions(List<ToolbarAction> actions) {
        this.actions.clear();
        this.actions.addAll(actions);
        invalidateOptionsMenu();
    }

    public void clearToolbarActions() {
        this.actions.clear();
        invalidateOptionsMenu();
    }
}
