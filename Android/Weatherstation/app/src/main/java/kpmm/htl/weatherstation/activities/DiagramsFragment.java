package kpmm.htl.weatherstation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import kpmm.htl.weatherstation.R;
import lecho.lib.hellocharts.formatter.AxisValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by ak47tehaxor on 10.01.17.
 */

public class DiagramsFragment extends Fragment implements Observer {

    private final int TEMPERETURE_MAX = 40;
    private final int TEMPERETURE_MIN = -15;

    LineChartView lineChartView;


    public DiagramsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_diagrams, container, false);
        lineChartView = (LineChartView)view.findViewById(R.id.testchart);


        List<PointValue> valuesRange = new ArrayList<>();
        List<PointValue> values = new ArrayList<>();

        // Height line, add it as first line to be drawn in the background.


        values.add(new PointValue(0, 1));
        values.add(new PointValue(1, 2));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 5));
        values.add(new PointValue(4, 5));
        values.add(new PointValue(5, 4));
        values.add(new PointValue(6, 3));
        values.add(new PointValue(7, 3));
        values.add(new PointValue(8, 3));
        values.add(new PointValue(9, 2));
        values.add(new PointValue(50, 2));


        valuesRange.add(new PointValue(0, TEMPERETURE_MAX));
        valuesRange.add(new PointValue(0, TEMPERETURE_MIN));
        Line lineRange = new Line(valuesRange).setColor(Color.parseColor("#00000000")).setCubic(true).setFilled(true).setHasPoints(false);
        Line line = new Line(values).setColor(MainActivity.colorAccent).setCubic(true).setAreaTransparency(150).setFilled(false).setHasPoints(false);



        List<Line> lines = new ArrayList<>();
        lines.add(line);
        lines.add(lineRange);

        LineChartData data = new LineChartData();


        Axis timeAxis = new Axis();
        timeAxis.setName("Time");
        timeAxis.setMaxLabelChars(4);
        timeAxis.setTextColor(MainActivity.colorTime);
        timeAxis.setLineColor(MainActivity.colorTime);
        timeAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("min".toCharArray()));
        timeAxis.setHasLines(true);
        timeAxis.setHasTiltedLabels(true);
        data.setAxisXBottom(timeAxis);
        Axis valueAxis = new Axis();
        valueAxis.setMaxLabelChars(3);
        valueAxis.setTextColor(MainActivity.colorPrimary);
        valueAxis.setLineColor(MainActivity.colorPrimary);
        valueAxis.setInside(false);
        valueAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("°C".toCharArray()));
        valueAxis.setHasLines(true);
        valueAxis.setHasTiltedLabels(true);


        data.setLines(lines);
        data.setAxisXBottom(timeAxis);
        data.setAxisYLeft(valueAxis);

        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.left = 0;
        viewport.right = 20;
        viewport.top = 40;
        viewport.bottom = -15;
        lineChartView.setLineChartData(data);
        lineChartView.setZoomType(ZoomType.HORIZONTAL);
        lineChartView.setCurrentViewport(viewport);

        return view;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

}
