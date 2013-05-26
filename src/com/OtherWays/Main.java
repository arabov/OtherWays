package com.OtherWays;

import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.*;
import android.widget.*;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import com.google.android.maps.*;
import com.google.gson.*;

import com.microsoft.windowsazure.mobileservices.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Locale;


public class Main extends SherlockFragmentActivity {

    private Integer USER_ID = null;
    private String USER_LOGIN = "";
    private String USER_PASSWORD = "";
    public TextView userId;
    public TextView userName;
    public TextView status;
    private Integer STATE = 0;

    private MapFragment mMapFragment;
    private SignInFragment mSignInFragment;
    private AutorizedFragment mAutorizedFragment;
    private RegistrationFragment mRegistrationFragment;
    private LayersFragment mLayersFragment;
    private RoutesFragment mRoutesFragment;
    private AddMarker mAddMarker;
    private Fragment mVisible = null;

    private Context context = this;

    private MobileServiceClient mClient;
    private MobileServiceJsonTable markersTable;
    private MobileServiceJsonTable usersTable;

    public Location lastLocation;
    public LocationManager locationManager;
    public Float clickedLatitude = null;
    public Float clickedLongitude = null;

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

        Exchanger.mMapView = new MapView(this, "0BhdX4jIPYsj2IzVimXgILU8ICs51b2hhRZnVjQ");

        try {
            mClient = new MobileServiceClient(
                    "https://otherways2.azure-mobile.net/",
                    "YMFLmydQzPIIPatUiccGjqfsoyjESW43",
                    this
            );
            markersTable = mClient.getTable("Markers");
            usersTable = mClient.getTable("Users");
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        setupFragments();
        showFragment(mSignInFragment);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setupFragments() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);
        if (mMapFragment == null) {
            mMapFragment = new MapFragment();
            ft.add(R.id.fragment_container, mMapFragment, MapFragment.TAG);
        }
        ft.hide(mMapFragment);

        mSignInFragment = (SignInFragment) getSupportFragmentManager().findFragmentByTag(SignInFragment.TAG);
        if (mSignInFragment == null) {
            mSignInFragment = new SignInFragment();
            ft.add(R.id.fragment_container, mSignInFragment, SignInFragment.TAG);
        }
        ft.hide(mSignInFragment);

        mAutorizedFragment = (AutorizedFragment) getSupportFragmentManager().findFragmentByTag(AutorizedFragment.TAG);
        if (mAutorizedFragment == null) {
            mAutorizedFragment = new AutorizedFragment();
            ft.add(R.id.fragment_container, mAutorizedFragment, AutorizedFragment.TAG);
        }
        ft.hide(mAutorizedFragment);

        mRegistrationFragment = (RegistrationFragment) getSupportFragmentManager().findFragmentByTag(RegistrationFragment.TAG);
        if (mRegistrationFragment == null) {
            mRegistrationFragment = new RegistrationFragment();
            ft.add(R.id.fragment_container, mRegistrationFragment, RegistrationFragment.TAG);
        }
        ft.hide(mRegistrationFragment);

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

        mAddMarker = (AddMarker) getSupportFragmentManager().findFragmentByTag(AddMarker.TAG);
        if (mAddMarker == null) {
            mAddMarker = new AddMarker();
            ft.add(R.id.fragment_container, mAddMarker, AddMarker.TAG);
        }
        ft.hide(mAddMarker);


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
                switch (STATE) {
                    case 0:
                        showFragment(mSignInFragment);
                        break;
                    case 2:
                        showFragment(mAutorizedFragment);
                        break;
                }
                return true;
            case R.id.ic_map:
                if (USER_ID == null) {
                    switch (STATE) {
                        case 0:
                            showFragment(mSignInFragment);
                            break;
                        case 2:
                            showFragment(mAutorizedFragment);
                            break;
                    }
                } else {
                    showFragment(mMapFragment);
                }
                return true;
            case R.id.ic_settings:
                startActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        String tag = mVisible.getTag();
                        if (tag.equals(MapFragment.TAG)) {
                            menu.add("Add").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Center").setIcon(R.drawable.marker).setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Markers").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                            menu.add("Routes").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                        } else if (tag.equals(SignInFragment.TAG)) {
                        }
                        return true;
                    }
                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }
                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        if (item.toString().equals("Add")) {
                            //showFragment(mAddMarker);
                            Exchanger.mMapView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                                        Projection proj = Exchanger.mMapView.getProjection();
                                        GeoPoint loc = proj.fromPixels((int)event.getX(), (int)event.getY());
                                        clickedLatitude = ((float)loc.getLongitudeE6())/1000000;
                                        clickedLongitude = ((float)loc.getLatitudeE6())/1000000;
                                        Exchanger.mMapView.setOnTouchListener(null);
                                        showFragment(mAddMarker);
                                    }
                                    return true;
                                }
                            });
                        }
                        if (item.toString().equals("Center")) {
                            if (lastLocation != null) {
                                Exchanger.mMapView.getController().animateTo(
                                        new GeoPoint(
                                                (int) (lastLocation.getLatitude() * 1E6),
                                                (int) (lastLocation.getLongitude() * 1E6)
                                        )
                                );
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

            markersTable.where().top(200).execute(new TableJsonQueryCallback() {
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

    public class SignInFragment extends SherlockFragment {
        public static final String TAG = "SignInFragment";

        public SignInFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            return inflater.inflate(R.layout.autification, vg, false);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);

            Button loginButton = (Button)findViewById(R.id.loginbutton);
            Button regButton = (Button)findViewById(R.id.regbutton);

            status = (TextView)findViewById(R.id.status);
            final EditText login = (EditText)findViewById(R.id.login);
            final EditText pass = (EditText)findViewById(R.id.password);

            regButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFragment(mRegistrationFragment);
                }
            });

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loginStr = login.getText().toString();
                    String passStr = pass.getText().toString();


                    usersTable.where().field("Name").eq(loginStr).and().field("Password").eq(passStr).execute(new TableJsonQueryCallback() {
                        @Override
                        public void onCompleted(JsonElement result, int count, Exception exception, ServiceFilterResponse response) {
                            if (exception != null) {
                                Log.d("---AZURE LOGIN FAILED---", exception.toString());
                                status.setText("Exception!");
                            } else {
                                Log.d("---AZURE LOGIN---", result.toString());
                                JsonArray jsonArray = result.getAsJsonArray();
                                if (jsonArray.size() == 0) {
                                    status.setText("Wrong Login or Password");
                                } else {
                                    status.setText("Login success");
                                    JsonObject object = jsonArray.get(0).getAsJsonObject();
                                    Integer id = object.get("id").getAsInt();
                                    String name = object.get("Name").getAsString();
                                    String password = object.get("Password").getAsString();
                                    USER_ID = id;
                                    USER_LOGIN = name;
                                    USER_PASSWORD = password;
                                    userId.setText("Your id: " + USER_ID);
                                    userName.setText("Your name: " + USER_LOGIN);
                                    STATE = 2;
                                    showFragment(mAutorizedFragment);
                                }
                            }
                        }
                    });


                }
            });
        }

    }

    public class RegistrationFragment extends SherlockFragment {
        public static final String TAG = "RegistrationFragment";

        public RegistrationFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            return inflater.inflate(R.layout.registation, vg, false);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);

            Button regButton = (Button)findViewById(R.id.registerbutton);
            final EditText regLogin = (EditText)findViewById(R.id.reglogin);
            final EditText regPass = (EditText)findViewById(R.id.regpassword);

            regButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String login = regLogin.getText().toString();
                    String pass = regPass.getText().toString();

                    final JsonObject json = new JsonObject();
                    json.addProperty("Name", login);
                    json.addProperty("Password", pass);

                    usersTable.insert(json, new TableJsonOperationCallback() {
                        @Override
                        public void onCompleted(JsonObject jsonObject, Exception exception, ServiceFilterResponse response) {
                            if (exception != null) {
                                Log.d("---AZURE USER FAILED---", exception.toString());
                            } else {
                                Log.d("---AZURE USER INSERTED---", json.toString());
                            }
                        }
                    });
                    STATE = 0;
                    showFragment(mSignInFragment);
                }
            });
        }

    }

    public class AutorizedFragment extends SherlockFragment {
        public static final String TAG = "AutorizedFragment";

        public AutorizedFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            return inflater.inflate(R.layout.autorized, vg, false);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);

            Button logOutbutton = (Button)findViewById(R.id.logoutbutton);
            userId = (TextView)findViewById(R.id.userId);
            userName = (TextView)findViewById(R.id.userName);

            logOutbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    USER_ID = null;
                    USER_LOGIN = "";
                    USER_PASSWORD = "";
                    STATE = 0;
                    status.setText("");
                    showFragment(mSignInFragment);
                }
            });
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

    public class AddMarker extends SherlockFragment {

        public static final String TAG = "AddMarker";

        public AddMarker() {}

        Geocoder geoCoder = new Geocoder(getBaseContext(), Locale.getDefault());

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            // Inflate the ListView layout file.
            return inflater.inflate(R.layout.addmarker, vg, false);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);

            Button button = (Button)findViewById(R.id.submitbutton);
            final EditText name = (EditText)findViewById(R.id.placename);
            final EditText address = (EditText)findViewById(R.id.placeaddress);
            final EditText subway = (EditText)findViewById(R.id.placesubway);
            final EditText time = (EditText)findViewById(R.id.placetime);
            final EditText description = (EditText)findViewById(R.id.placedescription);
            final Spinner placetype = (Spinner)findViewById(R.id.placetype);

            if (clickedLongitude != null && clickedLatitude != null) {
                try {
                    List<Address> addresses = geoCoder.getFromLocation(clickedLatitude, clickedLongitude, 1);
                    if (addresses.size() > 0) {
                        address.setText(addresses.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String desc = description.getText().toString();
                    String nm = name.getText().toString();
                    String addr = address.getText().toString();
                    String tm = time.getText().toString();
                    String sb = subway.getText().toString();

                    final JsonObject json = new JsonObject();
                    json.addProperty("Name", nm);
                    json.addProperty("Description", desc);
                    json.addProperty("Adress", addr);
                    json.addProperty("WorkTime", tm);
                    json.addProperty("Subway", sb);
                    json.addProperty("lng", clickedLatitude);
                    json.addProperty("lat", clickedLongitude);

                    String fullDesc = "";
                    if (!desc.equals("")) {
                        fullDesc += desc + "\n";
                    }
                    if (!addr.equals("")) {
                        fullDesc += "Адрес: " + addr;
                    }
                    if (!sb.equals("")) {
                        fullDesc += "\n" + "Метро: " + sb;
                    }
                    if (!tm.equals("")) {
                        fullDesc += "\n" + "Время работы: " + tm;
                    }

                    String s = placetype.getSelectedItem().toString();
                    if (s.equals("Достопримечательности")) {
                        json.addProperty("MarkerType_id", 0);
                        oAttractions.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Развлечения")) {
                        json.addProperty("MarkerType_id", 1);
                        oEntertainments.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Театры")) {
                        json.addProperty("MarkerType_id", 2);
                        oTheaters.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Кинотеатры")) {
                        json.addProperty("MarkerType_id", 3);
                        oCinemas.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Музеи")) {
                        json.addProperty("MarkerType_id", 4);
                        oMuseums.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Клубы")) {
                        json.addProperty("MarkerType_id", 5);
                        oClubs.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Концертные залы")) {
                        json.addProperty("MarkerType_id", 6);
                        oConcerthalls.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Парки")) {
                        json.addProperty("MarkerType_id", 7);
                        oParks.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Магазины")) {
                        json.addProperty("MarkerType_id", 8);
                        oShops.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Отели и транспорт")) {
                        json.addProperty("MarkerType_id", 9);
                        oHotels.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Аэропорты")) {
                        json.addProperty("MarkerType_id", 10);
                        oAirports.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    } else if (s.equals("Вокзалы")) {
                        json.addProperty("MarkerType_id", 11);
                        oRailways.addOverlay(new OverlayItem(new GeoPoint( (int)(clickedLatitude * 1E6), (int)(clickedLongitude * 1E6) ), nm, fullDesc));
                    }

                    if (null != USER_ID) {
                        json.addProperty("User_id", USER_ID);
                    } else {
                        json.addProperty("User_id", 0);
                    }

                    json.addProperty("IsChecked", 0);

                    markersTable.insert(json, new TableJsonOperationCallback() {
                        @Override
                        public void onCompleted(JsonObject jsonObject, Exception exception, ServiceFilterResponse response) {
                            if (exception != null) {
                                Log.d("---AZURE MARKER FAILED---", exception.toString());
                            } else {
                                Log.d("---AZURE MARKER INSERTED---", json.toString());
                            }
                        }
                    });

                    Exchanger.mMapView.setOnTouchListener(null);
                    Exchanger.mMapView.invalidate();
                    Exchanger.mMapView.postInvalidate();
                    showFragment(mMapFragment);

                    name.setText("");
                    address.setText("");
                    subway.setText("");
                    time.setText("");
                    description.setText("");
                }
            });

        }
    }
}