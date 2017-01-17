package kpmm.htl.weatherstation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import kpmm.htl.weatherstation.R;
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

    private final int TEMPERETURE_MAX = 40;
    private final int TEMPERETURE_MIN = -20;

    LineChartView lineChartViewTemperature;
    LineChartView lineChartViewRainfall;

    SwipeRefreshLayout swipeRefreshLayout;

    Model model;

    Line temperatureLine;
    Line rainfallLine;

    public DiagramsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        model = Model.getInstance();
        model.addObserver(this);

        View view = inflater.inflate(R.layout.content_diagrams, container, false);
        lineChartViewTemperature = (LineChartView) view.findViewById(R.id.diagrams_content_line_chart_view_temperature);
        lineChartViewRainfall = (LineChartView) view.findViewById(R.id.diagrams_content_line_chart_view_rainfall);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.diagrams_content_swipe_refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(MainActivity.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                model.requestAllMeasurements();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //region Set Temperature Properties
        List<PointValue> valueRangeTemperature = new ArrayList<>();
        valueRangeTemperature.add(new PointValue(0, TEMPERETURE_MAX));
        valueRangeTemperature.add(new PointValue(0, TEMPERETURE_MIN));

        Line lineRangeTemperature = new Line(valueRangeTemperature).setColor(MainActivity.colorTransparent).setCubic(true).setFilled(true).setHasPoints(false);
        temperatureLine = new Line().setColor(MainActivity.colorAccent).setCubic(true).setAreaTransparency(150).setFilled(false).setHasPoints(false);

        List<Line> linesTemperature = new ArrayList<>();
        linesTemperature.add(temperatureLine);
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
        viewportTemperature.top = TEMPERETURE_MAX;
        viewportTemperature.bottom = TEMPERETURE_MIN;
        lineChartViewTemperature.setLineChartData(lineChartDataTemperature);
        lineChartViewTemperature.setZoomType(ZoomType.HORIZONTAL);
        lineChartViewTemperature.setCurrentViewport(viewportTemperature);
        //endregion

        //region Set Rainfall Properties
        List<PointValue> valueRangeRainfall = new ArrayList<>();
        valueRangeRainfall.add(new PointValue(0, TEMPERETURE_MAX));
        valueRangeRainfall.add(new PointValue(0, TEMPERETURE_MIN));

        Line lineRangeRainfall = new Line(valueRangeRainfall).setColor(MainActivity.colorTransparent).setCubic(true).setFilled(true).setHasPoints(false);
        rainfallLine = new Line().setColor(MainActivity.colorAccent).setCubic(true).setAreaTransparency(150).setFilled(false).setHasPoints(false);

        List<Line> linesRainfall = new ArrayList<>();
        linesRainfall.add(rainfallLine);
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
        viewportRainfall.top = TEMPERETURE_MAX;
        viewportRainfall.bottom = TEMPERETURE_MIN;
        lineChartViewRainfall.setLineChartData(lineChartDataRainfall);
        lineChartViewRainfall.setZoomType(ZoomType.HORIZONTAL);
        lineChartViewRainfall.setCurrentViewport(viewportRainfall);
        //endregion
        
        return view;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
