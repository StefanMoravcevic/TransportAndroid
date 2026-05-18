package com.programdoo.transport.ui.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.programdoo.transport.R;

import lombok.Getter;

public class ProgramFloatingActionButton extends FrameLayout {

    @Getter
    private FloatingActionButton fab;
    private FrameLayout root;

    private boolean movable = false;

    private float startX;
    private float touchStartX;

    private float minAllowedPercent = 0.0f;
    private float maxAllowedPercent = 0.8f;

    public ProgramFloatingActionButton(
            @NonNull Context context) {
        super(context);
        init(context, null);
    }

    public ProgramFloatingActionButton(
            @NonNull Context context,
            @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgramFloatingActionButton(
            @NonNull Context context,
            @Nullable AttributeSet attrs,
            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(
            Context context,
            AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_program_floating_action_button, this, true);
        root = findViewById(R.id.root);
        fab = findViewById(R.id.fab);

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, new int[] {
                    android.R.attr.backgroundTint
            });

            ColorStateList tint = a.getColorStateList(0);
            fab.setBackgroundTintList(tint);
            fab.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.white)));
        }

        fab.setOnLongClickListener(v -> {
            movable = true;
            fab.animate().scaleX(1.12f).scaleY(1.12f).setDuration(120).start();
            return true;
        });

        fab.setOnTouchListener((v, event) -> handleTouch(event));
    }

    public void setOnClickListener(OnClickListener listener) {
        fab.setOnClickListener(listener);
    }

    private boolean handleTouch(MotionEvent event) {
        View parentView = (View) getParent();
        if (parentView == null) return false;

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                if (!movable) return false;

                startX = getX();
                touchStartX = event.getRawX();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (!movable) return false;

                float deltaX = event.getRawX() - touchStartX;
                float newX = startX + deltaX;

                float maxX = parentView.getWidth() - getWidth();
                newX = Math.max(0, Math.min(newX, maxX));

                setX(newX);
                return true;

            case MotionEvent.ACTION_UP:
                if (!movable) return false;

                fab.animate().scaleX(1f).scaleY(1f).setDuration(120).start();

                float currentX = getX();
                float width = parentView.getWidth();

                float minAllowed = width * minAllowedPercent;
                float maxAllowed = width * maxAllowedPercent;

                if (currentX < minAllowed || currentX > maxAllowed) {
                    animateBackToDefault(parentView);
                }

                movable = false;
                return true;
        }

        return false;
    }

    private void animateBackToDefault(View parent) {
        float targetX = (parent.getWidth() - getWidth()) / 2f;

        animate()
                .x(targetX)
                .setDuration(250)
                .setInterpolator(new OvershootInterpolator())
                .start();
    }

    public void setAllowedRegion(float minPercent, float maxPercent) {
        this.minAllowedPercent = minPercent;
        this.maxAllowedPercent = maxPercent;
    }
}