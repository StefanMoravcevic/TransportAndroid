package com.programdoo.transport.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Filterable;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.programdoo.transport.R;
import com.programdoo.transport.ui.adapters.BaseRecyclerViewAdapter;
import com.programdoo.transport.utils.UiUtil;

import java.util.List;

import lombok.Getter;

public abstract class SelectBase extends LinearLayout {
    protected TextInputLayout til;
    protected TextInputEditText et;
    protected String hint;

    public RecyclerView listView;
    private ProgramSearchBar searchbar;
    private final Dialog dialog;
    private boolean isClickable = true;

    @Getter
    BaseRecyclerViewAdapter<?,?> adapter;

    private final Handler handler = new Handler();
    private Runnable filterRunnable;

    private final Drawable dropdownIcon;
    private final Drawable clearIcon;
    protected boolean multi = false;

    public SelectBase(
            Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_select_dialog, this, true);
        et = findViewById(R.id.tvSelectedItem);
        et.setFocusable(false);
        et.setFocusableInTouchMode(false);
        et.setCursorVisible(false);
        et.setKeyListener(null);
        til = findViewById(R.id.tilSelectedItem);

        dropdownIcon = AppCompatResources.getDrawable(til.getContext(), R.drawable.icon_dropdown);
        clearIcon = AppCompatResources.getDrawable(til.getContext(), R.drawable.icon_clear);

        listView = new RecyclerView(context);

        // prikazivanje hint-a ukoliko je postavljen
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.hint});
            hint = a.getString(0);
            a.recycle();
        }

        UiUtil.setTextInputLayoutStateColors(context, til, null);

        if (hint != null) {
            til.setHint(hint);
        }
        // inicijalizacija dijaloga
        dialog = new Dialog(context);
        UiUtil.createDialogWindow(context, dialog, R.layout.select_dialog);

        searchbar = dialog.findViewById(R.id.searchbar);
        listView = dialog.findViewById(R.id.lvSelectItems);
        // definise sta se desava prilikom unosa teksa u edit text
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int count, int after) {
                UiUtil.updateEndIcon(til, clearIcon, dropdownIcon);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                // ukoliko je ranije postavljen runnable, obrisi ga jer nismo jos zavrsili sa unosom teksta
                if (filterRunnable != null) {
                    handler.removeCallbacks(filterRunnable);
                }
                // napravi nov runnable koji poziva filter adaptera
                filterRunnable = () -> {
                    RecyclerView.Adapter adapter = listView.getAdapter();
                    if (adapter instanceof Filterable) {
                        // mora da se cast-uje u Filterable, jer genericki adapter nema filtriranje.
                        // svi nasi adapteri ce da implementiraju basic filter keyword-om po opisu
                        // tako da ovo uvek radi
                        ((Filterable) adapter).getFilter().filter(editable.toString());
                    }
                };
                // prosledi ga handler-u da ga izvrsi i postavi delay
                handler.postDelayed(filterRunnable, 300);
            }
        });

        et.setOnClickListener(v -> {
            if (!isInEditMode() && this.isClickable)
                this.dialog.show();
        });

        til.setEndIconOnClickListener(v -> {
            Editable text = et.getText();
            if (text != null && text.length() > 0 && this.isClickable)
                adapter.clearSelection();
        });

        UiUtil.updateEndIcon(til, clearIcon, dropdownIcon);
    }

    public void setAdapter(BaseRecyclerViewAdapter<?,?> adapter) {
        this.adapter = adapter;
        this.listView.setAdapter(adapter);
        this.adapter.setOnSelectionChangedListener(this::updateState);
    }

    private void updateState(List<String> descriptionList) {
        updateDescription(descriptionList);
        UiUtil.updateEndIcon(til, clearIcon, dropdownIcon);
        if (!multi && this.dialog.isShowing())
            this.dialog.dismiss();
    }
    abstract protected void updateDescription(List<String> descriptionList);

    public void addPendingSelection(int id) {
        adapter.addPendingSelect(id);
    }
    public void toggleSelected(int position) {
        int id = adapter.getSelectedItemId(position);
        String description = adapter.getSelectedItemDescription(position);
        adapter.toggleSelected(id, description, !multi);
    }

    public String getSelectedDescription() {
        if (this.et.getText() != null) return this.et.getText().toString();
        else return "";
    }

    @Override
    public void setClickable(boolean value) {
        this.isClickable = value;
    }

    @Override
    public void setFocusable(boolean value) {
        this.isClickable = value;
    }

    public boolean isSelected() {
        return adapter.isSelected();
    }
    public void setError(String errorMsg) {
        this.til.setError(errorMsg);
    }
}
