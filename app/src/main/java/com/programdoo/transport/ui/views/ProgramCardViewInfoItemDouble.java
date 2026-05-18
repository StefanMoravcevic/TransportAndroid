package com.programdoo.transport.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.programdoo.transport.R;
import com.programdoo.transport.utils.StringUtil;

public class ProgramCardViewInfoItemDouble extends ConstraintLayout {
    private TextView tvFirstLabel;
    private TextView tvFirstText;
    private TextView tvSecondLabel;
    private TextView tvSecondText;
    private String defaultText = "";

    public ProgramCardViewInfoItemDouble(
            Context context) {
        super(context);
        init(context, null);
    }

    public ProgramCardViewInfoItemDouble(
            Context context,
            AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgramCardViewInfoItemDouble(
            Context context,
            AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(
            Context context,
            AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_program_card_view_info_item_double, this, true);

        tvFirstLabel = findViewById(R.id.tvFirstLabel);
        tvFirstText = findViewById(R.id.tvFirstText);
        tvSecondLabel = findViewById(R.id.tvSecondLabel);
        tvSecondText = findViewById(R.id.tvSecondText);

        String firstLabel = "Label";
        String secondLabel = "Label";
        String firstText = "Text";
        String secondText = "Text";

        if (attrs != null) {
            TypedArray a = context.getTheme().
                    obtainStyledAttributes(attrs,
                            R.styleable.ProgramCardViewInfoItemDouble, 0, 0);

            try {
                firstLabel = a.getString(R.styleable.ProgramCardViewInfoItemDouble_firstItemLabel);
                firstText = a.getString(R.styleable.ProgramCardViewInfoItemDouble_firstItemText);
                secondLabel = a.getString(R.styleable.ProgramCardViewInfoItemDouble_secondItemLabel);
                secondText = a.getString(R.styleable.ProgramCardViewInfoItemDouble_secondItemText);
                String defaultText = a.getString(R.styleable.ProgramCardViewInfoItemDouble_defaultText);

                if (defaultText != null) {
                    this.defaultText = defaultText;
                    if (StringUtil.isNullOrEmpty(tvFirstText.getText().toString()))
                        tvFirstText.setText(defaultText);
                    if (StringUtil.isNullOrEmpty(tvSecondText.getText().toString()))
                        tvSecondText.setText(defaultText);
                }

                if (firstLabel != null) tvFirstLabel.setText(firstLabel);
                if (firstText != null) tvFirstText.setText(firstText);
                if (secondLabel != null) tvSecondLabel.setText(secondLabel);
                if (secondText != null) tvSecondText.setText(secondText);

                int dividerColor = a.getColor(R.styleable.ProgramCardViewInfoItemDouble_dividerColor, context.getColor(R.color.primaryLighter));
//                findViewById(R.id.divider).setBackground(new ColorDrawable(dividerColor));
                findViewById(R.id.divider).setBackgroundColor(dividerColor);
            }
            finally {
                a.recycle();
            }
        }

        tvFirstLabel.setText(firstLabel);
        tvFirstText.setText(firstText);
        tvSecondLabel.setText(secondLabel);
        tvSecondText.setText(secondText);
    }

    public void setFirstLabel(String value) {
        tvFirstLabel.setText(value);
    }
    public void setSecondLabel(String value) {
        tvSecondLabel.setText(value);
    }
    public String getFirstLabel() {
        return tvFirstLabel.getText().toString();
    }
    public String getSecondLabel() {
        return tvSecondLabel.getText().toString();
    }

    public void setFirstText(String value) {
        if (StringUtil.isNullOrEmpty(value))
            tvFirstText.setText(defaultText);
        else
            tvFirstText.setText(value);
    }
    public void setSecondText(String value) {
        if (StringUtil.isNullOrEmpty(value))
            tvSecondText.setText(defaultText);
        else
            tvSecondText.setText(value);
    }
    public String getFirstText() {
        return tvFirstText.getText().toString();
    }
    public String getSecondText() {
        return tvSecondText.getText().toString();
    }


}
