package com.OtherWays;

import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.google.android.maps.*;
import com.google.gson.*;

import com.microsoft.windowsazure.mobileservices.*;

import com.facebook.Session;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;


public class Main extends SherlockFragmentActivity {

    private MapFragment mMapFragment;
    private UserLogined mUserLogined;
    private HomeFragment mHomeFragment;
    private LayersFragment mLayersFragment;
    private RoutesFragment mRoutesFragment;
    private Fragment mVisible = null;

    private Context context = this;

    private MobileServiceClient mClient;
    private MobileServiceJsonTable mTable;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback =
            new Session.StatusCallback() {
                @Override
                public void call(Session session, SessionState state, Exception exception) {
                    onSessionStateChange(session, state, exception);
                }
            };

    public Location lastLocation;
    public LocationManager locationManager;
    public Overlay item;

    public Overlay oAttractions;
    public Overlay oEntertainments;
    public Overlay oTheaters;
    public Overlay oCinemas;
    public Overlay oMuseums;
    public Overlay oClubs;
    public Overlay oConcerthalls;
    public Overlay oParks;
    public Overlay oShops;
    public Overlay oHotels;
    public Overlay oAirports;
    public Overlay oRailways;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        Exchanger.mMapView = new MapView(this, "0BhdX4jIPYsj2IzVimXgILU8ICs51b2hhRZnVjQ");

        try {
            mClient = new MobileServiceClient(
                    "https://otherways2.azure-mobile.net/",
                    "YMFLmydQzPIIPatUiccGjqfsoyjESW43",
                    this
            );
            mTable = mClient.getTable("Markers");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        setupFragments();
        showFragment(mHomeFragment);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }
    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {

        } else if (state.isClosed()) {

        }
    }

    private void setupFragments() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);
        if (mMapFragment == null) {
            mMapFragment = new MapFragment();
            ft.add(R.id.fragment_container, mMapFragment, MapFragment.TAG);
        }
        ft.hide(mMapFragment);

        mUserLogined = (UserLogined) getSupportFragmentManager().findFragmentByTag(UserLogined.TAG);
        if (mUserLogined == null) {
            mUserLogined = new UserLogined();
            ft.add(R.id.fragment_container, mUserLogined, UserLogined.TAG);
        }
        ft.hide(mUserLogined);

        mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            ft.add(R.id.fragment_container, mHomeFragment, HomeFragment.TAG);
        }
        ft.hide(mHomeFragment);

        mLayersFragment = (LayersFragment) getSupportFragmentManager().findFragmentByTag(LayersFragment.TAG);
        if (mLayersFragment == null) {
            mLayersFragment = new LayersFragment();
            ft.add(R.id.fragment_container, mLayersFragment, LayersFragment.TAG);
        }
        ft.hide(mLayersFragment);

        mRoutesFragment = (RoutesFragment) getSupportFragmentManager().findFragmentByTag(RoutesFragment.TAG);
        if (mRoutesFragment == null) {
            mRoutesFragment = new RoutesFragment();
            ft.add(R.id.fragment_container, mRoutesFragment, RoutesFragment.TAG);
        }
        ft.hide(mRoutesFragment);


        ft.commit();
    }

    private void showFragment(Fragment fragmentIn) {
        if (fragmentIn == null) return;

        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

        if (mVisible != null) ft.hide(mVisible);

        ft.show(fragmentIn).commit();
        mVisible = fragmentIn;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_home:
                showFragment(mHomeFragment);
                return true;
            case R.id.ic_map:
                showFragment(mMapFragment);
                return true;
            case R.id.ic_settings:
                startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        String tag = mVisible.getTag();
                        if (tag.equals(MapFragment.TAG)) {
                            menu.add("Center").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Markers").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Routes").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                        } else if (tag.equals(HomeFragment.TAG)) {
                        }
                        return true;
                    }
                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }
                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.toString().equals("Center")) {
                            if (lastLocation != null) {
                                Exchanger.mMapView.getController().animateTo(
                                        new GeoPoint(
                                                (int) (lastLocation.getLatitude() * 1E6),
                                                (int) (lastLocation.getLongitude() * 1E6)
                                        )
                                );
                                //Exchanger.mMapView.getController().zoomIn();
                            }
                        }
                        if (item.toString().equals("Markers")) {
                            showFragment(mLayersFragment);
                        }
                        if (item.toString().equals("Routes")) {
                            showFragment(mRoutesFragment);
                        }


                        mode.finish();
                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode actionMode) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class Exchanger {
        public static MapView mMapView;
    }

    public class MapFragment extends SherlockFragment {

        public static final String TAG = "mapFragment";

        MapController mapController = Exchanger.mMapView.getController();
        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

        public MapFragment() {}

        private void updateLoc(Location loc) {
            GeoPoint locGeoPoint = new GeoPoint( (int)(loc.getLatitude()*1E6), (int)(loc.getLongitude()*1E6));
            mapController.animateTo(locGeoPoint);
            Exchanger.mMapView.getOverlays().remove(item);
            item = new Overlay(this.getResources().getDrawable(R.drawable.marker), context);

            String addr = "";
            try {
                List<Address> addresses = geoCoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                if (addresses.size() > 0) {
                    addr += addresses.get(0).getAddressLine(0);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            item.addOverlay(new OverlayItem(locGeoPoint, "You here!", addr));
            Exchanger.mMapView.getOverlays().add(item);
            Exchanger.mMapView.invalidate();
        }

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);

            //LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            //Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastLocation != null) {
                updateLoc(lastLocation);
            }
        }
        @Override
        public void onResume() {
            super.onResume();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, myLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, myLocationListener);
        }

        @Override
        public void onPause() {
            super.onPause();
            locationManager.removeUpdates(myLocationListener);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {

            Exchanger.mMapView.setClickable(true);
            Exchanger.mMapView.setBuiltInZoomControls(true);

            oAttractions = new Overlay(this.getResources().getDrawable(R.drawable.attractions), context);
            oEntertainments = new Overlay(this.getResources().getDrawable(R.drawable.entertainment), context);
            oTheaters = new Overlay(this.getResources().getDrawable(R.drawable.theaters), context);
            oCinemas = new Overlay(this.getResources().getDrawable(R.drawable.cinemas), context);
            oMuseums = new Overlay(this.getResources().getDrawable(R.drawable.museums), context);
            oClubs = new Overlay(this.getResources().getDrawable(R.drawable.clubs), context);
            oConcerthalls = new Overlay(this.getResources().getDrawable(R.drawable.concerthalls), context);
            oParks = new Overlay(this.getResources().getDrawable(R.drawable.parks), context);
            oShops = new Overlay(this.getResources().getDrawable(R.drawable.shops), context);
            oHotels = new Overlay(this.getResources().getDrawable(R.drawable.hotels), context);
            oAirports = new Overlay(this.getResources().getDrawable(R.drawable.airports), context);
            oRailways = new Overlay(this.getResources().getDrawable(R.drawable.railways), context);

            mTable.where().top(166).execute(new TableJsonQueryCallback() {
                @Override
                public void onCompleted(JsonElement result, int count, Exception exception, ServiceFilterResponse response) {
                    if (exception != null) {
                        Log.d("QUERY_EXCEPTION", exception.toString());
                    } else {
                        Log.d("---AZURE---", result.toString());
                        JsonArray jsonArray = result.getAsJsonArray();
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JsonObject object = jsonArray.get(i).getAsJsonObject();

                            String name = object.get("Name").getAsString();
                            String desc = object.get("Description").getAsString();
                            String address = object.get("Adress").getAsString();
                            String workTime = object.get("WorkTime").getAsString();
                            String subway = object.get("Subway").getAsString();
                            Float lng = object.get("lng").getAsFloat();
                            Float lat = object.get("lat").getAsFloat();
                            Integer type = object.get("MarkerType_id").getAsInt();

                            String fullDesc = "";
                            if (!desc.equals("")) {
                                fullDesc += desc + "\n";
                            }
                            if (!address.equals("")) {
                                fullDesc += "Адрес: " + address;
                            }
                            if (!subway.equals("")) {
                                fullDesc += "\n" + "Метро: " + subway;
                            }
                            if (!workTime.equals("")) {
                                fullDesc += "\n" + "Время работы: " + workTime;
                            }
                            switch (type) {
                                case 0:
                                    oAttractions.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 1:
                                    oEntertainments.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 2:
                                    oTheaters.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 3:
                                    oCinemas.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 4:
                                    oMuseums.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 5:
                                    oClubs.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 6:
                                    oConcerthalls.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 7:
                                    oParks.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 8:
                                    oShops.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 9: case 12:
                                    oHotels.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 10:
                                    oAirports.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                case 11:
                                    oRailways.addOverlay(new OverlayItem(new GeoPoint( (int)(lat * 1E6), (int)(lng * 1E6) ), name, fullDesc));
                                    break;
                                default:
                                    break;
                            }
                        }
                        Exchanger.mMapView.getOverlays().add(oAttractions);
                        Exchanger.mMapView.getOverlays().add(oEntertainments);
                        Exchanger.mMapView.getOverlays().add(oTheaters);
                        Exchanger.mMapView.getOverlays().add(oCinemas);
                        Exchanger.mMapView.getOverlays().add(oMuseums);
                        Exchanger.mMapView.getOverlays().add(oClubs);
                        Exchanger.mMapView.getOverlays().add(oConcerthalls);
                        Exchanger.mMapView.getOverlays().add(oParks);
                        Exchanger.mMapView.getOverlays().add(oShops);
                        Exchanger.mMapView.getOverlays().add(oHotels);
                        Exchanger.mMapView.getOverlays().add(oAirports);
                        Exchanger.mMapView.getOverlays().add(oRailways);
                    }
                }
            });

            return Exchanger.mMapView;
        }
        private LocationListener myLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLoc(location);
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

        };
    }

    public static class HomeFragment extends SherlockFragment {
        public static final String TAG = "homeFragment";

        public HomeFragment() {}


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            return inflater.inflate(R.layout.autification, vg, false);
        }

    }

    public static class UserLogined extends SherlockFragment {
        public static final String TAG = "UserLogined";

        public UserLogined() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            // Inflate the ListView layout file.
            return inflater.inflate(R.layout.fbuser, vg, false);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);
        }
    }

    public class LayersFragment extends SherlockFragment {

        public static final String TAG = "LayersFragment";

        public LayersFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            // Inflate the ListView layout file.
            return inflater.inflate(R.layout.layers, vg, false);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);

            Button button = (Button)findViewById(R.id.layersButton);

            final CheckBox attractionsCheck = (CheckBox)findViewById(R.id.attractions);
            final CheckBox entertainmentsCheck = (CheckBox)findViewById(R.id.entertainments);
            final CheckBox theatersCheck = (CheckBox)findViewById(R.id.theaters);
            final CheckBox cinemasCheck = (CheckBox)findViewById(R.id.cinemas);
            final CheckBox museumsCheck = (CheckBox)findViewById(R.id.museums);
            final CheckBox clubsCheck = (CheckBox)findViewById(R.id.clubs);
            final CheckBox concerthallsCheck = (CheckBox)findViewById(R.id.concerthalls);
            final  CheckBox parksCheck = (CheckBox)findViewById(R.id.parks);
            final CheckBox shopsCheck = (CheckBox)findViewById(R.id.shops);
            final CheckBox hotelsCheck = (CheckBox)findViewById(R.id.hotels);
            final CheckBox airportsCheck = (CheckBox)findViewById(R.id.airports);
            final CheckBox railwaysCheck = (CheckBox)findViewById(R.id.railways);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (attractionsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oAttractions); }
                    else { Exchanger.mMapView.getOverlays().remove(oAttractions); }
                    if (entertainmentsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oEntertainments); }
                    else { Exchanger.mMapView.getOverlays().remove(oEntertainments); }
                    if (theatersCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oTheaters); }
                    else { Exchanger.mMapView.getOverlays().remove(oTheaters); }
                    if (cinemasCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oCinemas); }
                    else { Exchanger.mMapView.getOverlays().remove(oCinemas); }
                    if (museumsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oMuseums); }
                    else { Exchanger.mMapView.getOverlays().remove(oMuseums); }
                    if (clubsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oClubs); }
                    else { Exchanger.mMapView.getOverlays().remove(oClubs); }
                    if (concerthallsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oConcerthalls); }
                    else { Exchanger.mMapView.getOverlays().remove(oConcerthalls); }
                    if (parksCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oParks); }
                    else { Exchanger.mMapView.getOverlays().remove(oParks); }
                    if (shopsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oShops); }
                    else { Exchanger.mMapView.getOverlays().remove(oShops); }
                    if (hotelsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oHotels); }
                    else { Exchanger.mMapView.getOverlays().remove(oHotels); }
                    if (airportsCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oAirports); }
                    else { Exchanger.mMapView.getOverlays().remove(oAirports); }
                    if (railwaysCheck.isChecked()) { Exchanger.mMapView.getOverlays().add(oRailways); }
                    else { Exchanger.mMapView.getOverlays().remove(oRailways); }
                    Exchanger.mMapView.invalidate();
                    showFragment(mMapFragment);
                }
            });
        }
    }

    public class RoutesFragment extends SherlockFragment {

        public static final String TAG = "RoutesFragment";

        public RoutesFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            // Inflate the ListView layout file.
            return inflater.inflate(R.layout.routes, vg, false);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);

            Button button = (Button)findViewById(R.id.routesButton);
                final CheckBox attractionsroutes = (CheckBox)findViewById(R.id.attractionsroutes);
                final CheckBox entertainmentsroutes = (CheckBox)findViewById(R.id.entertainmentsroutes);
                final CheckBox childroutes = (CheckBox)findViewById(R.id.childroutes);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        if (attractionsroutes.isChecked()) {

                        } else {

                        }
                        if (entertainmentsroutes.isChecked()) {
                        } else {

                        }
                        if (childroutes.isChecked()) {

                        } else {

                        }
                    Exchanger.mMapView.invalidate();
                    showFragment(mMapFragment);
                }
            });
        }
    }
}