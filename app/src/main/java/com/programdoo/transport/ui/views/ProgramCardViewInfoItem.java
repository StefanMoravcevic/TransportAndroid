package com.programdoo.transport.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.programdoo.transport.R;
import com.programdoo.transport.utils.StringUtil;

public class ProgramCardViewInfoItem extends LinearLayout {
    private TextView tvLabel;
    private TextView tvText;
    private String defaultText = "";

    public ProgramCardViewInfoItem(
            Context context) {
        super(context);
        init(context, null);
    }

    public ProgramCardViewInfoItem(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgramCardViewInfoItem(
            Context context,
            AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_program_card_view_info_item, this, true);
        setOrientation(VERTICAL);

        tvLabel = findViewById(R.id.tvLabel);
        tvText = findViewById(R.id.tvText);

        String label = "Label";
        String text = "Text";

        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, R.styleable.ProgramCardViewInfoItem, 0, 0);

            try {
                boolean multiline = a.getBoolean(R.styleable.ProgramCardViewInfoItem_itemMultiline, false);
                tvText.setSingleLine(!multiline);

                label = a.getString(R.styleable.ProgramCardViewInfoItem_itemLabel);
                text = a.getString(R.styleable.ProgramCardViewInfoItem_itemText);

                if (label != null) {
                    tvLabel.setText(label);
                }
                if (text != null) {
                    tvText.setText(text);
                }

                String defaultText = a.getString(R.styleable.ProgramCardViewInfoItem_defaultText);
                if (defaultText != null) {
                    this.defaultText = defaultText;
                    if (StringUtil.isNullOrEmpty(tvText.getText().toString())) {
                        tvText.setText(defaultText);
                    }
                }
            }
            finally {
                a.recycle();
            }
        }

        tvLabel.setText(label);
        tvText.setText(text);
    }

    public void setLabel(String value) {
        tvLabel.setText(value);
    }

    public void setText(String value) {
        if (StringUtil.isNullOrEmpty(value)) {
            tvText.setText(defaultText);
        }
        else {
            tvText.setText(value);
        }
    }

    public String getLabel() {
        return tvLabel.getText().toString();
    }

    public String getText() {
        return tvText.getText().toString();
    }
}
