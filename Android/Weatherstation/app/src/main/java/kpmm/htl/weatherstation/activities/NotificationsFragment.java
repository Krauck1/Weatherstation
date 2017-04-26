package kpmm.htl.weatherstation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import kpmm.htl.weatherstation.R;

/**
 * Created by ak47tehaxor on 29.03.17.
 */

public class NotificationsFragment extends Fragment {

    NumberPicker numberPickerMonday;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_notifications, container, false);

        numberPickerMonday = (NumberPicker) view.findViewById(R.id.notifications_content_number_picker_monday);

        numberPickerMonday.setMaxValue(10);
        numberPickerMonday.setMinValue(0);



        return view;
    }
}
