package kpmm.htl.weatherstation.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Observable;
import java.util.Observer;

import kpmm.htl.weatherstation.R;
import kpmm.htl.weatherstation.fragments.DiagramsFragment;
import kpmm.htl.weatherstation.fragments.NotificationsFragment;
import kpmm.htl.weatherstation.fragments.OverviewFragment;
import kpmm.htl.weatherstation.model.Model;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Observer {

    public static int colorAccent;
    public static int colorPrimary;
    public static int colorTransparent;
    public static int colorTime;
    public static int colorSnow;
    public static int colorEclipse;
    public static int colorRainfall;
    public static int colorTemperature;

    public static MainActivity mainActivity;

    private DiagramsFragment diagramsFragment;
    private OverviewFragment overviewFragment;
    private NotificationsFragment notificationsFragment;
    Model model;
    static FragmentSelection fragmentSelection = FragmentSelection.OVERVIEW;

    FrameLayout frameLayoutContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        model = Model.getInstance();
        model.addObserver(this);
        model.setContext(getApplicationContext());
        mainActivity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_overview);

        frameLayoutContent = (FrameLayout) findViewById(R.id.content_main);

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
        navigationView.getMenu().getItem(0).setChecked(true);

        changeFragment();
    }

    private void loadResources() {
        colorAccent = ContextCompat.getColor(getApplicationContext(), R.color.colorAccent);
        colorPrimary = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary);
        colorTime = ContextCompat.getColor(getApplicationContext(), R.color.colorTime);
        colorTransparent = ContextCompat.getColor(getApplicationContext(), R.color.colorTransparent);
        colorSnow = ContextCompat.getColor(getApplicationContext(), R.color.colorSnow);
        colorEclipse = ContextCompat.getColor(getApplicationContext(), R.color.colorEclipse);
        colorRainfall = ContextCompat.getColor(getApplicationContext(), R.color.colorRainfall);
        colorTemperature = ContextCompat.getColor(getApplicationContext(), R.color.colorTemperature);
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
                fragmentSelection = FragmentSelection.OVERVIEW;
                changeFragment();
            }
        } else if (id == R.id.nav_diagrams) {
            if (fragmentSelection != FragmentSelection.DIAGRAMS) {
                fragmentSelection = FragmentSelection.DIAGRAMS;
                changeFragment();
            }
        } else if (id == R.id.nav_notifications) {
            if (fragmentSelection != FragmentSelection.NOTIFICATIONS) {
                fragmentSelection = FragmentSelection.NOTIFICATIONS;
                changeFragment();
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
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content_main, fragment).commit();
    }

    private void changeFragment() {
        switch (fragmentSelection) {
            case OVERVIEW:
                setTitle(R.string.title_overview);
                changeFragment(overviewFragment == null ? overviewFragment = new OverviewFragment() : overviewFragment);
                break;
            case DIAGRAMS:
                setTitle(R.string.title_diagrams);
                changeFragment(diagramsFragment == null ? diagramsFragment = new DiagramsFragment() : diagramsFragment);
                break;
            case NOTIFICATIONS:
                setTitle(R.string.title_notifications);
                changeFragment(notificationsFragment == null ? notificationsFragment = new NotificationsFragment() : notificationsFragment);
                break;
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.action_refresh);
        menuItem.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private enum FragmentSelection {
        OVERVIEW,
        RECENT,
        DIAGRAMS,
        NOTIFICATIONS,
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
