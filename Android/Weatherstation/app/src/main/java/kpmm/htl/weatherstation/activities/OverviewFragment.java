package kpmm.htl.weatherstation.activities;

import android.content.res.ColorStateList;
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
    TextView textViewCompareRainfall;
    TextView textViewHeadingCurrentWeather;
    TextView textViewSmoking;

    SwipeRefreshLayout swipeRefreshLayout;

    ImageView imageViewCurrentWeather;
    ImageView imageViewSmoking;
    ImageView imageViewCompareTemperatureDifference;
    ImageView imageViewCompareHumidityDifference;
    ImageView imageViewCompareRainfallDifference;

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
        textViewCompareRainfall = (TextView) view.findViewById(R.id.overview_content_text_view_compare_rainfall);
        textViewHeadingCurrentWeather = (TextView) view.findViewById(R.id.overview_content_text_view_heading_current_weather);
        textViewSmoking = (TextView) view.findViewById(R.id.overview_content_text_view_smoking);

        imageViewCurrentWeather = (ImageView) view.findViewById(R.id.overview_content_image_view_current_weather);
        imageViewSmoking = (ImageView) view.findViewById(R.id.overview_content_image_view_smoking);
        imageViewCompareTemperatureDifference = (ImageView) view.findViewById(R.id.overview_content_image_view_compare_temperature_difference);
        imageViewCompareHumidityDifference = (ImageView) view.findViewById(R.id.overview_content_image_view_compare_humidity_difference);
        imageViewCompareRainfallDifference = (ImageView) view.findViewById(R.id.overview_content_image_view_compare_rainfall_difference);


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

        //region SET COMPARE
        if (compareMeasurement.getAmbientTemperature() > lastMeasurement.getAmbientTemperature()) {
            textViewCompareTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", compareMeasurement.getAmbientTemperature() - lastMeasurement.getAmbientTemperature()));
            imageViewCompareTemperatureDifference.setImageResource(R.drawable.ic_arrow_upward);
        } else if (compareMeasurement.getAmbientTemperature() < lastMeasurement.getAmbientTemperature()) {
            textViewCompareTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", lastMeasurement.getAmbientTemperature() - compareMeasurement.getAmbientTemperature()));
            imageViewCompareTemperatureDifference.setImageResource(R.drawable.ic_arrow_downward);
        } else {
            textViewCompareTemperature.setText("0");
            imageViewCompareTemperatureDifference.setImageResource(R.drawable.ic_nothing);
        }

        if (compareMeasurement.getRainfall() > lastMeasurement.getRainfall()) {
            textViewCompareRainfall.setText(String.format(Locale.ENGLISH, "%.2f °C", compareMeasurement.getRainfall() - lastMeasurement.getRainfall()));
            imageViewCompareRainfallDifference.setImageResource(R.drawable.ic_arrow_upward);
        } else if (compareMeasurement.getRainfall() < lastMeasurement.getRainfall()) {
            textViewCompareRainfall.setText(String.format(Locale.ENGLISH, "%.2f °C", lastMeasurement.getRainfall() - compareMeasurement.getRainfall()));
            imageViewCompareRainfallDifference.setImageResource(R.drawable.ic_arrow_downward);
        } else {
            textViewCompareRainfall.setText("0");
            imageViewCompareRainfallDifference.setImageResource(R.drawable.ic_nothing);
        }

        if (compareMeasurement.getHumidity() > lastMeasurement.getHumidity()) {
            textViewCompareRainfall.setText(String.format(Locale.ENGLISH, "%.2f °C", compareMeasurement.getHumidity() - lastMeasurement.getHumidity()));
            imageViewCompareHumidityDifference.setImageResource(R.drawable.ic_arrow_upward);
        } else if (compareMeasurement.getHumidity() < lastMeasurement.getHumidity()) {
            textViewCompareRainfall.setText(String.format(Locale.ENGLISH, "%.2f °C", lastMeasurement.getHumidity() - compareMeasurement.getHumidity()));
            imageViewCompareHumidityDifference.setImageResource(R.drawable.ic_arrow_downward);
        } else {
            textViewCompareHumidity.setText("0");
            imageViewCompareHumidityDifference.setImageResource(R.drawable.ic_nothing);
        }
        //endregion

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
