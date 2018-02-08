package kpmm.htl.weatherstation.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import kpmm.htl.weatherstation.R;
import kpmm.htl.weatherstation.model.Model;

/**
 * Created by ak47tehaxor on 29.03.17.
 */

public class NotificationsFragment extends Fragment {
    Model model;

    private NumberPicker numberPickerMonday;
    private NumberPicker numberPickerTuesday;
    private NumberPicker numberPickerWednesday;
    private NumberPicker numberPickerThursday;
    private NumberPicker numberPickerFriday;

    private LinearLayout linearLayoutHours;

    private Switch switchSmoking;
    private Switch switchMain;

    private ScrollView scrollView;

    private TextView textViewDisabled;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_notifications, container, false);

        model = Model.getInstance();

        numberPickerMonday = (NumberPicker) view.findViewById(R.id.notifications_content_number_picker_monday);
        numberPickerTuesday = (NumberPicker) view.findViewById(R.id.notifications_content_number_picker_tuesday);
        numberPickerWednesday = (NumberPicker) view.findViewById(R.id.notifications_content_number_picker_wednesday);
        numberPickerThursday = (NumberPicker) view.findViewById(R.id.notifications_content_number_picker_thursday);
        numberPickerFriday = (NumberPicker) view.findViewById(R.id.notifications_content_number_picker_friday);
        linearLayoutHours = (LinearLayout) view.findViewById(R.id.notifications_content_linear_layout_hours);
        switchSmoking = (Switch) view.findViewById(R.id.notifications_content_switch_smoking);
        switchMain = (Switch) view.findViewById(R.id.notifications_content_switch_main);
        scrollView = (ScrollView) view.findViewById(R.id.notifications_content_scroll_view);
        textViewDisabled = (TextView) view.findViewById(R.id.notifications_content_text_view_disabled);

        numberPickerMonday.setMaxValue(10);
        numberPickerMonday.setMinValue(0);
        numberPickerTuesday.setMaxValue(10);
        numberPickerTuesday.setMinValue(0);
        numberPickerWednesday.setMaxValue(10);
        numberPickerWednesday.setMinValue(0);
        numberPickerThursday.setMaxValue(10);
        numberPickerThursday.setMinValue(0);
        numberPickerFriday.setMaxValue(10);
        numberPickerFriday.setMinValue(0);
        numberPickerMonday.setValue(model.getMonday());
        numberPickerTuesday.setValue(model.getTuesday());
        numberPickerWednesday.setValue(model.getWednesday());
        numberPickerThursday.setValue(model.getThursday());
        numberPickerFriday.setValue(model.getFriday());
        switchSmoking.setChecked(model.isGoingOutside());
        switchMain.setChecked(model.isNotifications());


        if (model.isGoingOutside())
            linearLayoutHours.setVisibility(View.VISIBLE);
        else
            linearLayoutHours.setVisibility(View.GONE);
        if (model.isNotifications()) {
            textViewDisabled.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        } else {
            scrollView.setVisibility(View.GONE);
            textViewDisabled.setVisibility(View.VISIBLE);
        }

        switchSmoking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    model.setGoingOutside(true);
                    linearLayoutHours.setVisibility(View.VISIBLE);
                } else {
                    model.setGoingOutside(false);
                    linearLayoutHours.setVisibility(View.GONE);
                }
            }
        });
        switchMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    model.setNotifications(true);
                    textViewDisabled.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                } else {
                    model.setNotifications(false);
                    scrollView.setVisibility(View.GONE);
                    textViewDisabled.setVisibility(View.VISIBLE);
                }
            }
        });
        numberPickerMonday.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                model.setMonday((byte) newVal);
            }
        });
        numberPickerTuesday.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                model.setTuesday((byte) newVal);
            }
        });
        numberPickerWednesday.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                model.setWednesday((byte) newVal);
            }
        });
        numberPickerThursday.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                model.setThursday((byte) newVal);
            }
        });
        numberPickerFriday.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                model.setFriday((byte) newVal);
            }
        });

        return view;
    }
}
