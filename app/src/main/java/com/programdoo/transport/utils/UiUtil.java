package com.programdoo.transport.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.programdoo.transport.R;
import com.programdoo.transport.ui.adapters.BaseRecyclerViewAdapter;
import com.programdoo.transport.ui.callbacks.SwipeDeleteCallback;
import com.programdoo.transport.ui.decorators.ListItemDecoration;
import com.programdoo.transport.ui.callbacks.OnItemClickListener;
import com.programdoo.transport.ui.pages.BaseFragment;
import com.programdoo.transport.ui.views.MultiSelectDialog;
import com.programdoo.transport.ui.views.ProgramInputField;
import com.programdoo.transport.ui.views.SelectDialog;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

public class UiUtil {
    public static void makeToast(
            @NonNull Activity activity,
            @NonNull Context context,
            @NonNull String message,
            int length) {
        activity.runOnUiThread(()
                -> Toast.makeText(context, message, length).show());
    }
    public static void makeToast(
            @NonNull Activity activity,
            @NonNull Context context,
            @NonNull String message) {
        activity.runOnUiThread(()
                -> Toast.makeText(context, message, Toast.LENGTH_LONG).show());
    }
    /**
     *
     * @param context kontekst fragmenta ili activity-ja
     * @param adapter konkretan adapter u koji pravi listu
     * @param select select objekat iz binding-a
     * @param clickListener funkcija koja se desava prilikom klika
     * @param <T> tip entity-ja u listi
     *           <p>
     *              helper funkcija koja konfigurise select dialog
     *           </p>
     */
    public static <T> void selectSetup(
            @NonNull Context context,
            BaseRecyclerViewAdapter<T, ?> adapter,
            SelectDialog select,
            OnItemClickListener<T> clickListener) {
        select.listView.setLayoutManager(new LinearLayoutManager(context));
        adapter.setOnClickListener(clickListener);
        select.setAdapter(adapter);
        select.listView.addItemDecoration(new ListItemDecoration(context,
                context.getColor(R.color.tertiaryLighter),
                1));
    }
    public static <T> void multiSelectSetup(
            @NonNull Context context,
            BaseRecyclerViewAdapter<T, ?> adapter,
            MultiSelectDialog select,
            OnItemClickListener<T> clickListener) {
        select.listView.setLayoutManager(new LinearLayoutManager(context));
        adapter.setOnClickListener(clickListener);
        select.setAdapter(adapter);
        select.listView.addItemDecoration(new ListItemDecoration(context,
                context.getColor(R.color.tertiaryLighter),
                1));
    }

    public static void enableSwipeDelete(
            RecyclerView recyclerView,
            SwipeDeleteCallback swipeCallback) {
        ItemTouchHelper ith = new ItemTouchHelper(swipeCallback);
        ith.attachToRecyclerView(recyclerView);
    }

    public static void createDialogWindow(
            Context context,
            Dialog dialog,
            @LayoutRes int layoutResId) {
        dialog.setContentView(layoutResId);

        if (dialog.getWindow() != null) {
            Window window = dialog.getWindow();
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            window.getAttributes().setBlurBehindRadius(50);
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = (int) (metrics.heightPixels * .7);
            window.setLayout(width, height);
            window.setGravity(Gravity.BOTTOM);
        }
    }

    public static void datePickerSetup(
            BaseFragment frag,
            ProgramInputField pif) {
        /* ne prikazuj tastaturu */
        pif.getEt().setInputType(InputType.TYPE_NULL);

        LocalDateTime initDate = DateUtil.parse(pif.getText());
        if (initDate == null) initDate = LocalDateTime.now();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                .datePicker()
                .setSelection(initDate.toInstant(ZoneOffset.UTC).toEpochMilli())
                .setTitleText(frag.requireContext().getString(R.string.label_select_date))
                .build();
        pif.getEt().setOnClickListener(v -> {
            picker.show(frag.getParentFragmentManager(), Constants.FRAG_DATE_PICKER);
            pif.getEt().setError(null);
        });
        pif.getEt().setOnFocusChangeListener((v, isFocused) -> {
            if (isFocused) {
                picker.show(frag.getParentFragmentManager(), Constants.FRAG_DATE_PICKER);
                pif.getEt().setError(null);
            }
        });
        picker.addOnPositiveButtonClickListener(selection -> {
            LocalDateTime selectedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(selection), TimeZone.getDefault().toZoneId());
            pif.setText(DateUtil.format(selectedDate));
        });
    }

    public static void datePickerSetupCallback(
            BaseFragment frag,
            TextView tv,
            Runnable onDateSelected
    ) {
        tv.setClickable(true);
        tv.setFocusable(false);

        LocalDateTime initDate = DateUtil.parse(tv.getText().toString());
        if (initDate == null) initDate = LocalDateTime.now();

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder
                .datePicker()
                .setSelection(initDate.toInstant(ZoneOffset.UTC).toEpochMilli())
                .setTitleText(frag.requireContext().getString(R.string.label_select_date))
                .build();

        tv.setOnClickListener(v -> {
            picker.show(frag.getParentFragmentManager(), Constants.FRAG_DATE_PICKER);
        });

        picker.addOnPositiveButtonClickListener(selection -> {

            LocalDateTime selectedDate =
                    LocalDateTime.ofInstant(
                            Instant.ofEpochMilli(selection),
                            TimeZone.getDefault().toZoneId()
                    );

            tv.setText(DateUtil.format(selectedDate));

            if (onDateSelected != null) {
                onDateSelected.run();
            }
        });
    }
    public static void timePickerSetup(
            BaseFragment frag,
            ProgramInputField pif) {
        /* ne prikazuj tastaturu */
        pif.getEt().setInputType(InputType.TYPE_NULL);

        LocalTime initTime = TimeUtil.parse(pif.getText());
        if (initTime == null) initTime = LocalTime.now();

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(initTime.getHour())
                .setMinute(initTime.getMinute())
                .setTitleText(frag.requireContext().getString(R.string.label_select_time))
                .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                .build();
        pif.getEt().setOnClickListener(v -> {
            picker.show(frag.getParentFragmentManager(), Constants.FRAG_TIME_PICKER);
            pif.getEt().setError(null);
        });
        pif.getEt().setOnFocusChangeListener((v, isFocused) -> {
            if (isFocused) {
                picker.show(frag.getParentFragmentManager(), Constants.FRAG_DATE_PICKER);
                pif.getEt().setError(null);
            }
        });
        picker.addOnPositiveButtonClickListener(dialog -> {
            int hour = picker.getHour();
            int minute = picker.getMinute();
            LocalTime selectedTime = LocalTime.of(hour, minute);
            pif.setText(TimeUtil.format(selectedTime));
        });
    }

    public static void dateTimePickerSetup(BaseFragment frag, ProgramInputField pif) {

        pif.getEt().setInputType(InputType.TYPE_NULL);
        pif.getEt().setShowSoftInputOnFocus(false);

        final LocalDate[] selectedDate = new LocalDate[1];

        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Select date")
                .build();

        MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setTitleText("Select time")
                .build();

        pif.getEt().setOnClickListener(v ->
                datePicker.show(frag.getParentFragmentManager(), "DATE")
        );

        datePicker.addOnPositiveButtonClickListener(selection -> {

            selectedDate[0] = Instant.ofEpochMilli(selection)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            timePicker.show(frag.getParentFragmentManager(), "TIME");
        });

        timePicker.addOnPositiveButtonClickListener(dialog -> {

            if (selectedDate[0] == null) {
                selectedDate[0] = LocalDate.now();
            }

            LocalTime time = LocalTime.of(
                    timePicker.getHour(),
                    timePicker.getMinute()
            );

            LocalDateTime result = LocalDateTime.of(selectedDate[0], time);

            // 🔥 FIX: koristi datetime formatter, ne DateUtil.format()
            pif.setText(formatDateTime(result));
        });
    }
    public static String formatDateTime(LocalDateTime dt) {
        if (dt == null) return "";
        return dt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
    }

    public static void applySystemBarInsets(
            View view,
            boolean applyTop,
            boolean applyBottom) {
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());


            int left = v.getPaddingLeft();
            int right = v.getPaddingRight();
            int top = applyTop ? bars.top : v.getPaddingTop();
            int bottom = applyBottom ? bars.bottom : v.getPaddingBottom();

            v.setPadding(left, top, right, bottom);

            return insets;
        });

        view.requestApplyInsets();
    }

    public static void updateEndIcon(
            TextInputLayout til,
            @NonNull Drawable hasTextIcon,
            @Nullable Drawable noTextIcon) {
        EditText et = til.getEditText();
        if (et == null) return;

        boolean hasText = et.getText() != null && et.getText().length() > 0;
        til.setEndIconDrawable(hasText ? hasTextIcon : noTextIcon);
    }

    public static void setTextInputLayoutStateColors(
            Context context,
            TextInputLayout til,
            @Nullable int[] colors) {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_focused },
                new int[] { }
        };
        if (colors == null)
            colors = new int[] {
                    ContextCompat.getColor(context, R.color.primary),
                    ContextCompat.getColor(context, R.color.secondary)
            };

        ColorStateList colorStateList = new ColorStateList(states, colors);
        til.setDefaultHintTextColor(colorStateList);
        til.setBoxStrokeColorStateList(colorStateList);
    }

    public static void chipSetup(Context context, Chip chip) {
        chip.setClickable(true);
        chip.setCheckable(true);
        chip.setCheckedIconVisible(true);
        chip.setChipBackgroundColor(ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.chip_background)));
        chip.setChipStrokeColor(ContextCompat.getColorStateList(context, R.color.chip_stroke));
        chip.setCheckedIcon(ContextCompat.getDrawable(context, R.drawable.icon_checked_circle));
        chip.setCheckedIconTint(ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.secondary)));
    }

    public static void showLoading() {}
    public static void hideLoading() {}
}
