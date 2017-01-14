package kpmm.htl.weatherstation.activities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import kpmm.htl.weatherstation.R;
import kpmm.htl.weatherstation.model.Measurement;
import kpmm.htl.weatherstation.model.Model;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer {
    private static int colorAccent;


    Model model;
    FragmentSelection fragmentSelection = FragmentSelection.OVERVIEW;


    Measurement lastMeasurement;
    Measurement compareMeasurement;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        model = Model.getInstance();
        model.addObserver(this);
        model.setContext(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_overview);

        loadResources();

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        textViewCurrentTemperature = (TextView) findViewById(R.id.overview_content_text_view_current_temperature);
        textViewCurrentHumidity = (TextView) findViewById(R.id.overview_content_text_view_current_humidity);
        textViewCurrentRain = (TextView) findViewById(R.id.overview_content_text_view_current_rain);
        textViewCompareTemperature = (TextView) findViewById(R.id.overview_content_text_view_compare_temperature);
        textViewCompareHumidity = (TextView) findViewById(R.id.overview_content_text_view_compare_humidity);
        textViewCompareRain = (TextView) findViewById(R.id.overview_content_text_view_compare_rain);
        textViewHeadingCurrentWeather = (TextView) findViewById(R.id.overview_content_text_view_heading_current_weather);
        textViewSmoking = (TextView) findViewById(R.id.overview_content_text_view_smoking);

        imageViewCurrentWeather = (ImageView) findViewById(R.id.overview_content_image_view_current_weather);
        imageViewSmoking = (ImageView) findViewById(R.id.overview_content_image_view_smoking);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.overview_content_swipe_refresh_layout);

        swipeRefreshLayout.setColorSchemeColors(colorAccent);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isNetworkAvailable())
                    model.requestLastMeasurement();
                else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "No Internet Connection Avaiable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadResources() {
        colorAccent = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_overview) {
            if (fragmentSelection != FragmentSelection.OVERVIEW) {

            }
        } else if (id == R.id.nav_diagrams) {
            if (fragmentSelection != FragmentSelection.DIAGRAMMS) {
                changeFragment(new DiagramsFragment());
            }
        } else if (id == R.id.nav_notifications) {
            if (fragmentSelection != FragmentSelection.NOTIFICATIONS) {

            }
        } else if (id == R.id.nav_recent) {
            if (fragmentSelection != FragmentSelection.RECENT) {

            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragment(Fragment fragment) {
        int replace;
        switch (fragmentSelection){
            case OVERVIEW:
                replace = R.id.content_overview;
                break;
            default:
                return;
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(replace, fragment).commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_refresh);
        menuItem.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void update(Observable observable, Object o) {
        swipeRefreshLayout.setRefreshing(false);
        if (!model.isSuccess()) {
            Toast.makeText(this, "Server Connection Failed", Toast.LENGTH_SHORT).show();
            return;
        }
        lastMeasurement = model.getLastMeasurement();
        compareMeasurement = model.getMeasurementFromNow(1000);

        textViewCurrentTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", lastMeasurement.getAmbientTemperature()));
        textViewCurrentHumidity.setText(String.format(Locale.ENGLISH, "%.2f %%", lastMeasurement.getHumidity()));
        textViewCurrentRain.setText(String.format(Locale.ENGLISH, "%.2f mm³", lastMeasurement.getRainfall()));

        textViewCompareTemperature.setText(String.format(Locale.ENGLISH, "%.2f °C", compareMeasurement.getAmbientTemperature()));
        textViewCompareHumidity.setText(String.format(Locale.ENGLISH, "%.2f %%", compareMeasurement.getHumidity()));
        textViewCompareRain.setText(String.format(Locale.ENGLISH, "%.2f mm³", compareMeasurement.getRainfall()));

        if (lastMeasurement.getRainfall() < 1) {
            imageViewCurrentWeather.setImageResource(R.drawable.ic_sunny);
            textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFC107")));
        } else {
            if (lastMeasurement.getAmbientTemperature() < 0) {
                imageViewCurrentWeather.setImageResource(R.drawable.ic_snowflake);
                textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(Color.parseColor("#26C6DA")));

            } else if (lastMeasurement.getAmbientTemperature() > 30) {
                imageViewCurrentWeather.setImageResource(R.drawable.ic_eclipse);
                textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFC107")));
            } else {
                imageViewCurrentWeather.setImageResource(R.drawable.ic_rain);
                textViewHeadingCurrentWeather.setTextColor(ColorStateList.valueOf(Color.parseColor("#1A237E")));
                imageViewSmoking.setImageResource(R.drawable.ic_no_smoking);
                textViewSmoking.setText(R.string.bad_time);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private enum FragmentSelection {
        OVERVIEW,
        RECENT,
        DIAGRAMMS,
        NOTIFICATIONS,
    }
}
