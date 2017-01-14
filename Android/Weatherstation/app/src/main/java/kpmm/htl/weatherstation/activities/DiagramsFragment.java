package kpmm.htl.weatherstation.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kpmm.htl.weatherstation.R;

/**
 * Created by ak47tehaxor on 10.01.17.
 */

public class DiagramsFragment extends Fragment {

    public DiagramsFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_diagramms,container,false);
    }
}
