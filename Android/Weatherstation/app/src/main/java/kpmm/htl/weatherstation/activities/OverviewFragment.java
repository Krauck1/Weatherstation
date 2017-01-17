package kpmm.htl.weatherstation.activities;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import kpmm.htl.weatherstation.R;
import kpmm.htl.weatherstation.model.Measurement;
import kpmm.htl.weatherstation.model.Model;

/**
 * Created by ak47tehaxor on 16.01.17.
 */

public class OverviewFragment extends Fragment implements Observer {

    MainActivity mainActivity;

    Measurement lastMeasurement;
    Measurement compareMeasurement;
    Model model;

    TextView textViewCurrentTemperature;
    TextView textViewCurrentHumidity;
    TextView textViewCurrentRain;
    TextView textViewCompareTemperature;
    TextView textViewCompareHumidity;
    TextView textViewCompareRain;
    TextView textViewHeadingCurrentWeather;
    TextView textViewSmoking;

    SwipeRefreshLayout swipeRefreshLayout;

    ImageView imageViewCurrentWeather;
    ImageView imageViewSmoking;

    public OverviewFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        model = Model.getInstance();
        model.addObserver(this);
        View view = inflater.inflate(R.layout.content_overview, container, false);
        mainActivity = MainActivity.mainActivity;
        textViewCurrentTemperature = (TextView) view.findViewById(R.id.overview_content_text_view_current_temperature);
        textViewCurrentHumidity = (TextView) view.findViewById(R.id.overview_content_text_view_current_humidity);
        textViewCurrentRain = (TextView) view.findViewById(R.id.overview_content_text_view_current_rain);
        textViewCompareTemperature = (TextView) view.findViewById(R.id.overview_content_text_view_compare_temperature);
        textViewCompareHumidity = (TextView) view.findViewById(R.id.overview_content_text_view_compare_humidity);
        textViewCompareRain = (TextView) view.findViewById(R.id.overview_content_text_view_compare_rain);
        textViewHeadingCurrentWeather = (TextView) view.findViewById(R.id.overview_content_text_view_heading_current_weather);
        textViewSmoking = (TextView) view.findViewById(R.id.overview_content_text_view_smoking);

        imageViewCurrentWeather = (ImageView) view.findViewById(R.id.overview_content_image_view_current_weather);
        imageViewSmoking = (ImageView) view.findViewById(R.id.overview_content_image_view_smoking);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.overview_content_swipe_refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(mainActivity.colorAccent);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mainActivity.isNetworkAvailable())
                    model.requestLastMeasurement();
                else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(mainActivity.getApplicationContext(), "No Internet Connection Avaiable", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void update(Observable observable, Object o) {
        swipeRefreshLayout.setRefreshing(false);
        if (!model.isSuccess()) {
            Toast.makeText(mainActivity, "Server Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        lastMeasurement = model.getLastMeasurement();
        compareMeasurement = model.getMeasurementFromNow(1000);

        textViewCurrentTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", lastMeasurement.getAmbientTemperature()));
        textViewCurrentHumidity.setText(String.format(Locale.ENGLISH, "%.2f %%", lastMeasurement.getHumidity()));
        textViewCurrentRain.setText(String.format(Locale.ENGLISH, "%.2f mm³", lastMeasurement.getRainfall()));
        if (compareMeasurement.getAmbientTemperature() > lastMeasurement.getAmbientTemperature()) {
            textViewCompareTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", compareMeasurement.getAmbientTemperature() - lastMeasurement.getAmbientTemperature()));

        } else if (compareMeasurement.getAmbientTemperature() < lastMeasurement.getAmbientTemperature()) {
            textViewCompareTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", lastMeasurement.getAmbientTemperature() - compareMeasurement.getAmbientTemperature()));
        } else {
            textViewCompareTemperature.setText(0);
        }
        textViewCompareHumidity.setText(String.format(Locale.ENGLISH, "%.2f %%", compareMeasurement.getHumidity()));
        textViewCompareRain.setText(String.format(Locale.ENGLISH, "%.2f mm³", compareMeasurement.getRainfall()));

        if (lastMeasurement.getRainfall() < 1) {
            imageViewCurrentWeather.setImageResource(R.drawable.ic_sunny);
            textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(MainActivity.colorTemperature));
        } else {
            if (lastMeasurement.getAmbientTemperature() < 0) {
                imageViewCurrentWeather.setImageResource(R.drawable.ic_snowflake);
                textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(MainActivity.colorSnow));

            } else if (lastMeasurement.getAmbientTemperature() > 30) {
                imageViewCurrentWeather.setImageResource(R.drawable.ic_eclipse);
                textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(MainActivity.colorEclipse));
            } else {
                imageViewCurrentWeather.setImageResource(R.drawable.ic_rain);
                textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(MainActivity.colorRainfall));
                imageViewSmoking.setImageResource(R.drawable.ic_no_smoking);
                textViewSmoking.setText(R.string.bad_time);
            }
        }
    }
}
