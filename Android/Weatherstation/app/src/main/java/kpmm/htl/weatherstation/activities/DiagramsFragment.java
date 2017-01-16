package kpmm.htl.weatherstation.activities;

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
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by ak47tehaxor on 10.01.17.
 */

public class DiagramsFragment extends Fragment implements Observer {

    LineChartView lineChartView;


    public DiagramsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_diagrams, container, false);
        lineChartView = (LineChartView)view.findViewById(R.id.testchart);
        List<PointValue> values = new ArrayList<>();
        values.add(new PointValue(0, 2));
        values.add(new PointValue(1, 4));
        values.add(new PointValue(2, 3));
        values.add(new PointValue(3, 4));
        values.add(new PointValue(4, 3));

        Line line = new Line(values).setColor(MainActivity.colorAccent).setCubic(true).setAreaTransparency(0);
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        LineChartData data = new LineChartData();


        Axis timeAxis = new Axis();
        timeAxis.setName("Time");
        timeAxis.setMaxLabelChars(10);
        timeAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("km".toCharArray()));
        timeAxis.setHasLines(true);
        timeAxis.setHasTiltedLabels(true);
        data.setAxisXBottom(timeAxis);
        Axis valueAxis = new Axis();
        valueAxis.setName("Value");
        valueAxis.setMaxLabelChars(10);
        valueAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("val".toCharArray()));
        valueAxis.setHasLines(true);
        valueAxis.setHasTiltedLabels(true);

        data.setLines(lines);
        data.setAxisXBottom(timeAxis);
        data.setAxisYLeft(valueAxis);

        lineChartView.setLineChartData(data);

        return view;
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
