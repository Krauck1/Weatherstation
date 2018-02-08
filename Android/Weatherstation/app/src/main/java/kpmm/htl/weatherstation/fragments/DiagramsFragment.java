package kpmm.htl.weatherstation.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import kpmm.htl.weatherstation.R;
import kpmm.htl.weatherstation.activities.MainActivity;
import kpmm.htl.weatherstation.model.Measurement;
import kpmm.htl.weatherstation.model.Model;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by ak47tehaxor on 10.01.17.
 */

public class DiagramsFragment extends Fragment implements Observer {

    private final int TEMPERATURE_MAX = 40;
    private final int TEMPERATURE_MIN = -20;


    private final int RAINFALL_MAX = 100;
    private final int RAINFALL_MIN = 0;

    private

    LineChartView lineChartViewTemperature;
    LineChartView lineChartViewRainfall;

    SwipeRefreshLayout swipeRefreshLayout;

    ImageView imageViewTemperature;
    ImageView imageViewRainfall;

    Model model;

    Line lineTemperature;
    Line lineRangeTemperature;
    Line lineRainfall;
    Line lineRangeRainfall;

    List<Measurement> measurementList;

    public DiagramsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        model = Model.getInstance();
        model.addObserver(this);

        View view = inflater.inflate(R.layout.content_diagrams, container, false);
        lineChartViewTemperature = (LineChartView) view.findViewById(R.id.diagrams_content_line_chart_view_temperature);
        lineChartViewRainfall = (LineChartView) view.findViewById(R.id.diagrams_content_line_chart_view_rainfall);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.diagrams_content_swipe_refresh_layout);

        imageViewTemperature = (ImageView) view.findViewById(R.id.diagrams_content_image_view_temperature);
        imageViewRainfall = (ImageView) view.findViewById(R.id.diagrams_content_image_view_rainfall);

        swipeRefreshLayout.setColorSchemeColors(MainActivity.colorPrimary);

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            imageViewTemperature.setBackground(new RippleDrawable(ColorStateList.valueOf(Color.GRAY), null, null));
            imageViewRainfall.setBackground(new RippleDrawable(ColorStateList.valueOf(Color.GRAY), null, null));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.requestAllMeasurements();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        imageViewTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageViewRainfall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //region Set Temperature Properties
        List<PointValue> valueRangeTemperature = new ArrayList<>();
        valueRangeTemperature.add(new PointValue(0, TEMPERATURE_MAX));
        valueRangeTemperature.add(new PointValue(0, TEMPERATURE_MIN));

        lineRangeTemperature = new Line(valueRangeTemperature).setColor(MainActivity.colorTransparent).setCubic(true).setFilled(true).setHasPoints(false);
        lineTemperature = new Line().setColor(MainActivity.colorAccent).setCubic(true).setAreaTransparency(150).setFilled(false).setHasPoints(false);

        List<Line> linesTemperature = new ArrayList<>();
        linesTemperature.add(lineTemperature);
        linesTemperature.add(lineRangeTemperature);

        LineChartData lineChartDataTemperature = new LineChartData();

        Axis xAxisTemperature = new Axis();
        xAxisTemperature.setName("Time");
        xAxisTemperature.setMaxLabelChars(4);
        xAxisTemperature.setTextColor(Color.BLACK);
        xAxisTemperature.setLineColor(Color.BLACK);
        xAxisTemperature.setFormatter(new SimpleAxisValueFormatter().setAppendedText("min".toCharArray()));
        xAxisTemperature.setHasLines(true);
        lineChartDataTemperature.setAxisXBottom(xAxisTemperature);
        Axis yAxisTemperature = new Axis();
        yAxisTemperature.setMaxLabelChars(3);
        yAxisTemperature.setTextColor(Color.BLACK);
        yAxisTemperature.setLineColor(Color.BLACK);
        yAxisTemperature.setInside(false);
        yAxisTemperature.setFormatter(new SimpleAxisValueFormatter().setAppendedText("°C".toCharArray()));
        yAxisTemperature.setHasLines(true);

        lineChartDataTemperature.setLines(linesTemperature);
        lineChartDataTemperature.setAxisXBottom(xAxisTemperature);
        lineChartDataTemperature.setAxisYLeft(yAxisTemperature);

        Viewport viewportTemperature = new Viewport(lineChartViewTemperature.getMaximumViewport());
        viewportTemperature.left = 0;
        viewportTemperature.right = 20;
        viewportTemperature.top = TEMPERATURE_MAX;
        viewportTemperature.bottom = TEMPERATURE_MIN;
        lineChartViewTemperature.setLineChartData(lineChartDataTemperature);
        lineChartViewTemperature.setZoomType(ZoomType.HORIZONTAL);
        lineChartViewTemperature.setCurrentViewport(viewportTemperature);
        //endregion

        //region Set Rainfall Properties
        List<PointValue> valueRangeRainfall = new ArrayList<>();
        valueRangeRainfall.add(new PointValue(0, RAINFALL_MAX));
        valueRangeRainfall.add(new PointValue(0, RAINFALL_MIN));

        lineRangeRainfall = new Line(valueRangeRainfall).setColor(MainActivity.colorTransparent).setCubic(true).setFilled(true).setHasPoints(false);
        lineRainfall = new Line().setColor(MainActivity.colorAccent).setCubic(true).setAreaTransparency(150).setFilled(false).setHasPoints(false);

        List<Line> linesRainfall = new ArrayList<>();
        linesRainfall.add(lineRainfall);
        linesRainfall.add(lineRangeRainfall);

        LineChartData lineChartDataRainfall = new LineChartData();

        Axis xAxisRainfall = new Axis();
        xAxisRainfall.setName("Time");
        xAxisRainfall.setMaxLabelChars(4);
        xAxisRainfall.setTextColor(Color.BLACK);
        xAxisRainfall.setLineColor(Color.BLACK);
        xAxisRainfall.setFormatter(new SimpleAxisValueFormatter().setAppendedText("min".toCharArray()));
        xAxisRainfall.setHasLines(true);
        lineChartDataRainfall.setAxisXBottom(xAxisRainfall);
        Axis yAxisRainfall = new Axis();
        yAxisRainfall.setMaxLabelChars(3);
        yAxisRainfall.setTextColor(Color.BLACK);
        yAxisRainfall.setLineColor(Color.BLACK);
        yAxisRainfall.setInside(false);
        yAxisRainfall.setFormatter(new SimpleAxisValueFormatter().setAppendedText("mm".toCharArray()));
        yAxisRainfall.setHasLines(true);

        lineChartDataRainfall.setLines(linesRainfall);
        lineChartDataRainfall.setAxisXBottom(xAxisRainfall);
        lineChartDataRainfall.setAxisYLeft(yAxisRainfall);

        Viewport viewportRainfall = new Viewport(lineChartViewRainfall.getMaximumViewport());
        viewportRainfall.left = 0;
        viewportRainfall.right = 20;
        viewportRainfall.top = TEMPERATURE_MAX;
        viewportRainfall.bottom = TEMPERATURE_MIN;
        lineChartViewRainfall.setLineChartData(lineChartDataRainfall);
        lineChartViewRainfall.setZoomType(ZoomType.HORIZONTAL);
        lineChartViewRainfall.setCurrentViewport(viewportRainfall);
        //endregion
        model.requestAllMeasurements();
        return view;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!model.isSuccess()) {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(MainActivity.mainActivity, "Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;

        LineChartData data = new LineChartData(lineChartViewTemperature.getLineChartData());
        data.getLines().clear();
        Line line = new Line().setColor(MainActivity.colorAccent).setCubic(true).setAreaTransparency(150).setFilled(false).setHasPoints(false);
        for (Measurement measurement : model.getMeasurementList()) {
            line.getValues().add(new PointValue(i, measurement.getAmbientTemperature()));
            i += 5;
        }
        data.getLines().add(line);
        data.getLines().add(lineRangeTemperature);
        lineChartViewTemperature.setLineChartData(data);

        data = new LineChartData(lineChartViewRainfall.getLineChartData());
        data.getLines().clear();
        line = new Line().setColor(MainActivity.colorRainfall).setCubic(true).setAreaTransparency(150).setFilled(false).setHasPoints(false);
        for (Measurement measurement : model.getMeasurementList()) {
            line.getValues().add(new PointValue(i, measurement.getRainfall()));
            i += 5;
        }
        data.getLines().add(line);
        data.getLines().add(lineRangeRainfall);
        lineChartViewRainfall.setLineChartData(data);
    }
}
