package com.programdoo.transport.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.programdoo.transport.R;
import com.programdoo.transport.utils.UiUtil;

import lombok.Getter;

public class ProgramInputField extends LinearLayout {
    private TextInputLayout til;
    @Getter
    private TextInputEditText et;

    private Drawable clearIcon;

    public ProgramInputField(
            Context context) {
        super(context);
        init(context, null);
    }

    public ProgramInputField(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgramInputField(
            Context context,
            AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(
            Context context,
            AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_program_input_field, this, true);

        til = findViewById(R.id.til);
        et = findViewById(R.id.etField);

        clearIcon = AppCompatResources.getDrawable(til.getContext(), R.drawable.icon_clear);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[]{
                    android.R.attr.hint, android.R.attr.inputType, android.R.attr.imeOptions,
                    android.R.attr.enabled, android.R.attr.visibility
            });

            UiUtil.setTextInputLayoutStateColors(context, til, null);

            CharSequence hint = a.getText(0);
            if (hint != null) {
                til.setHint(hint);
            }

            int inputType = a.getInt(1, -1);
            if (inputType != -1) {
                et.setInputType(inputType);
            }

            int imeOptions = a.getInt(2, -1);
            if (imeOptions != -1) {
                et.setImeOptions(imeOptions);
            }

            boolean enabled = a.getBoolean(3, true);
            et.setEnabled(enabled);

            int visibility = a.getInt(4, -1);
            if (visibility != -1) {
                et.setVisibility(visibility);
            }

            a.recycle();

            TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.ProgramInputField, 0, 0);
            try {
                boolean multiline = b.getBoolean(R.styleable.ProgramInputField_multiline, false);
                if (multiline) {
                    et.setMinLines(3);
                    et.setMaxLines(5);
                    et.setGravity(Gravity.TOP | Gravity.START);

                    if (isInEditMode()) {
                        et.setMinLines(3);
                        et.setMaxLines(5);
                        et.setGravity(Gravity.TOP | Gravity.START);
                    }
                }
            }
            finally {
                b.recycle();
            }
        }

        til.setEndIconOnClickListener(v -> setText(""));

        et.addTextChangedListener(internalWatcher);

        UiUtil.updateEndIcon(til, clearIcon, null);
    }

    public void setText(String value) {
        et.setText(value);
        UiUtil.updateEndIcon(til, clearIcon, null);
    }
    public String getText() {
        return et.getText() != null ? et.getText().toString() : "";
    }

    public void setError(String msg) {
        et.setError(msg);
    }
    public String getError() {
        return et.getError().toString();
    }

    private final TextWatcher internalWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable editable) { UiUtil.updateEndIcon(til, clearIcon, null); }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
    };
}
