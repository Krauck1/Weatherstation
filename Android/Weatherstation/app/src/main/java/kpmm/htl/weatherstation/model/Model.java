package kpmm.htl.weatherstation.model;


import android.content.Context;
import android.util.JsonReader;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;


/**
 * Created by ak47tehaxor on 12.01.17.
 */

public class Model extends Observable {

    private final String IPADRESS = "http://172.18.3.74:8080";
    private final String LAST = "/last";
    private final String ALL = "/all";
    private final long DELAY = 5 * 60 * 1000;

    private static Model instance;

    private List<Measurement> measurementList = new LinkedList<>();
    private Context context;
    private boolean success = true;

    public static Model getInstance() {
        return instance == null ? (instance = new Model()) : instance;
    }

    public void setContext(Context context) {
        this.context = context;
        requestLastMeasurement();
    }

    private Model() {

    }

    private void receiveDataFromREST(String url) {
        if (this.context == null)
            return;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(JsonArrayRequest.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                System.out.println("t");
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Measurement measurement = new Measurement(
                                (float) jsonObject.getDouble("ambient_temperature"),
                                (float) jsonObject.getDouble("ground_temperature"),
                                (float) jsonObject.getDouble("air_quality"),
                                (float) jsonObject.getDouble("air_pressure"),
                                (float) jsonObject.getDouble("humidity"),
                                (float) jsonObject.getDouble("wind_speed"),
                                (float) jsonObject.getDouble("wind_gust_speed"),
                                (float) jsonObject.getDouble("rainfall"),
                                Timestamp.valueOf(jsonObject.getString("created")));
                        measurementList.add(0, measurement);
                    }
                } catch (JSONException ex) {
                    System.out.println(ex.getMessage());
                }
                success = true;
                Collections.sort(measurementList);
                setChanged();
                notifyObservers();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                success = false;
                setChanged();
                notifyObservers();
            }
        });
        requestQueue.add(jsonArrayRequest);
        requestQueue.start();
    }

    public void requestLastMeasurement() {
        receiveDataFromREST(IPADRESS + LAST);
    }

    public void requestAllMeasurements(){
        receiveDataFromREST(IPADRESS+ALL);
    }

    public void requestMeasurementFromNow() {

    }

    public Measurement getLastMeasurement() {
        return measurementList.isEmpty() ? null : measurementList.get(0);
    }

    public Measurement getMeasurementFromNow(int minutes) {
        long millis = System.currentTimeMillis() - minutes * 60 * 1000;
        Measurement last = null;
        for (Measurement measurement : measurementList) {
            long difference = millis - measurement.getTime().getTime();
            if (difference < 0)
                last = measurement;
            else
                return last == null ? measurement : last.getTime().getTime() - millis < difference ? last : measurement;
        }
        return last;
    }

    public boolean isSuccess() {
        return success;
    }
}
