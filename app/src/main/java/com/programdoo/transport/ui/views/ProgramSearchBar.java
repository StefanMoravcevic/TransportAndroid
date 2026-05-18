package com.programdoo.transport.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.programdoo.transport.R;
import com.programdoo.transport.utils.UiUtil;

public class ProgramSearchBar extends LinearLayout {
    private TextInputLayout til;
    private TextInputEditText et;
    private MaterialCardView cardView;
    private Drawable searchIcon;
    private Drawable clearIcon;

    public ProgramSearchBar(
            Context context) {
        super(context);
        init(context, null);
    }

    public ProgramSearchBar(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgramSearchBar(
            Context context,
            AttributeSet attrs,
            int defStyleAttrs) {
        super(context, attrs, defStyleAttrs);
        init(context, attrs);
    }

    private void init(
            Context context,
            AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_program_search_bar, this, true);

        til = findViewById(R.id.til);
        et = findViewById(R.id.et);
        cardView = findViewById(R.id.cardView);

        UiUtil.setTextInputLayoutStateColors(context, til,
                new int[] {
                        ContextCompat.getColor(context, R.color.secondary),
                        ContextCompat.getColor(context, R.color.secondaryLightest)
                });

        searchIcon = AppCompatResources.getDrawable(til.getContext(), R.drawable.icon_search);
        clearIcon = AppCompatResources.getDrawable(til.getContext(), R.drawable.icon_clear);

        UiUtil.updateEndIcon(til, clearIcon, searchIcon);

        if (attrs != null) {
            TypedArray b = context.obtainStyledAttributes(attrs, new int[] {
                    android.R.attr.hint
            });

            CharSequence hint = b.getString(0);
            if (hint != null) {
                til.setHint(hint);
            }

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgramSearchBar);
            try {
                int cardColor = a.getColor(R.styleable.ProgramSearchBar_cardBackgroundColor, Color.TRANSPARENT);
                if (cardColor != -1) {
                    cardView.setCardBackgroundColor(cardColor);
                    cardView.setStrokeColor(cardColor);
                    til.setBoxBackgroundColor(cardColor);
                }

                int expandedHintColor = a.getColor(R.styleable.ProgramSearchBar_expandedHintColor, context.getColor(R.color.primary));
                til.setHintTextColor(ColorStateList.valueOf(expandedHintColor));
            }
            finally {
                a.recycle();
            }
        }

        til.setEndIconOnClickListener(v -> setText(""));

        et.addTextChangedListener(internalWatcher);
    }

    public void setText(String value) {
        et.setText(value);
        UiUtil.updateEndIcon(til, clearIcon, searchIcon);
    }
    public String getText() {
        return et.getText() != null ? et.getText().toString() : "";
    }

    public void addTextChangedListener(TextWatcher tw) {
        et.addTextChangedListener(tw);
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
