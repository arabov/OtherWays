package com.OtherWays;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;


public class Main extends SherlockFragmentActivity {
    private Boolean SESSION = true;

    private MapFragment mMapFragment;
    private UserLogined mUserLogined;
    private HomeFragment mHomeFragment;
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
            SESSION = true;
        } else if (state.isClosed()) {
            SESSION = false;
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
                if (SESSION) {
                    showFragment(mUserLogined);
                } else {
                    showFragment(mHomeFragment);
                }
                return true;
            case R.id.ic_map:
                showFragment(mMapFragment);
                /*
                if (SESSION) {
                    showFragment(mMapFragment);
                } else {
                    showFragment(mHomeFragment);
                }
                */
                //showFragment(mMapFragment);
                return true;
            case R.id.ic_settings:
                startActionMode(new ActionMode.Callback() {
                    Boolean attractions = true;
                    Boolean entertainments = true;
                    Boolean theaters = true;
                    Boolean cinemas = true;
                    Boolean museums = true;
                    Boolean clubs = true;
                    Boolean concerthalls = true;
                    Boolean parks = true;
                    Boolean shops = true;
                    Boolean hotels = true;
                    Boolean airports = true;
                    Boolean railways = true;


                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        String tag = mVisible.getTag();
                        if (tag.equals(MapFragment.TAG)) {
                            menu.add("Center").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            //menu.add("Layer").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Достопримечательности").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Развлечения").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Театры").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Кинотеатры").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Музеи").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Клубы").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Концертные залы").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Парки").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Магазины").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Отели и транспорт").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Аэропорты").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Вокзалы").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
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
                        if (item.toString().equals("Достопримечательности")) {
                            if (attractions) { attractions = false; Exchanger.mMapView.getOverlays().remove(oAttractions); }
                            else { attractions = true; Exchanger.mMapView.getOverlays().add(oAttractions); }
                        }
                        if (item.toString().equals("Развлечения")) {
                            if (entertainments) { entertainments = false; Exchanger.mMapView.getOverlays().remove(oEntertainments); }
                            else { entertainments = true; Exchanger.mMapView.getOverlays().add(oEntertainments); }
                        }
                        if (item.toString().equals("Театры")) {
                            if (theaters) { theaters = false; Exchanger.mMapView.getOverlays().remove(oTheaters); }
                            else { theaters = true; Exchanger.mMapView.getOverlays().add(oTheaters); }
                        }
                        if (item.toString().equals("Кинотеатры")) {
                            if (cinemas) { cinemas = false; Exchanger.mMapView.getOverlays().remove(oCinemas); }
                            else { cinemas = true; Exchanger.mMapView.getOverlays().add(oCinemas); }
                        }
                        if (item.toString().equals("Музеи")) {
                            if (museums) { museums = false; Exchanger.mMapView.getOverlays().remove(oMuseums); }
                            else { museums = true; Exchanger.mMapView.getOverlays().add(oMuseums); }
                        }
                        if (item.toString().equals("Клубы")) {
                            if (clubs) { clubs = false; Exchanger.mMapView.getOverlays().remove(oClubs); }
                            else { clubs = true; Exchanger.mMapView.getOverlays().add(oClubs); }
                        }
                        if (item.toString().equals("Концертные залы")) {
                            if (concerthalls) { concerthalls = false; Exchanger.mMapView.getOverlays().remove(oConcerthalls); }
                            else { concerthalls = true; Exchanger.mMapView.getOverlays().add(oConcerthalls); }
                        }
                        if (item.toString().equals("Парки")) {
                            if (parks) { parks = false; Exchanger.mMapView.getOverlays().remove(oParks); }
                            else { parks = true; Exchanger.mMapView.getOverlays().add(oParks); }
                        }
                        if (item.toString().equals("Магазины")) {
                            if (shops) { shops = false; Exchanger.mMapView.getOverlays().remove(oShops); }
                            else { shops = true; Exchanger.mMapView.getOverlays().add(oShops); }
                        }
                        if (item.toString().equals("Отели и транспорт")) {
                            if (hotels) { hotels = false; Exchanger.mMapView.getOverlays().remove(oHotels); }
                            else { hotels = true; Exchanger.mMapView.getOverlays().add(oHotels); }
                        }
                        if (item.toString().equals("Аэропорты")) {
                            if (airports) { airports = false; Exchanger.mMapView.getOverlays().remove(oAirports); }
                            else { airports = true; Exchanger.mMapView.getOverlays().add(oAirports); }
                        }
                        if (item.toString().equals("Вокзалы")) {
                            if (railways) { railways = false; Exchanger.mMapView.getOverlays().remove(oRailways); }
                            else { railways = true; Exchanger.mMapView.getOverlays().add(oRailways); }
                        }

                        Exchanger.mMapView.invalidate();
                        //mode.finish();
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
            /*
            Exchanger.mMapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    long startTime = 0;
                    long endTime = 0;

                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        startTime = motionEvent.getEventTime();
                    }else if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                        endTime = motionEvent.getEventTime();
                    }
                    Log.d(TAG, "Time: " + (endTime - startTime));
                    if(endTime - startTime > 1000){
                        Log.d(TAG, "Clicked");
                        return true; //notify that you handled this event (do not propagate)
                    } else {
                        return false;
                    }
                }
            });
            */
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
            // Inflate the ListView layout file.
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
}
