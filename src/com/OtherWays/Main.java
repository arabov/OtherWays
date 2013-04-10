package com.OtherWays;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.Toast;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.*;

import com.OtherWays.Overlay.*;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class Main extends SherlockFragmentActivity {

    private MapFragment mMapFragment;
    private MyListFragment mMyListFragment;
    private HomeFragment mHomeFragment;
    private Fragment mVisible = null;
    private Context context = this;

    private DBcontroller dbHelper =  new DBcontroller(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Exchanger.mMapView = new MapView(this, "0BhdX4jIPYsj2IzVimXgILU8ICs51b2hhRZnVjQ");

        setupFragments();
        showFragment(mHomeFragment);
    }

    /**
     * This method does the setting up of the Fragments. It basically checks if
     * the fragments exist and if they do, we'll hide them. If the fragments
     * don't exist, we create them, add them to the FragmentManager and hide
     * them.
     */
    private void setupFragments() {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        mMapFragment = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);
        if (mMapFragment == null) {
            mMapFragment = new MapFragment();
            ft.add(R.id.fragment_container, mMapFragment, MapFragment.TAG);
        }
        ft.hide(mMapFragment);

        mMyListFragment = (MyListFragment) getSupportFragmentManager().findFragmentByTag(MyListFragment.TAG);
        if (mMyListFragment == null) {
            mMyListFragment = new MyListFragment();
            ft.add(R.id.fragment_container, mMyListFragment, MyListFragment.TAG);
        }
        ft.hide(mMyListFragment);

        mHomeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        if (mHomeFragment == null) {
            mHomeFragment = new HomeFragment();
            ft.add(R.id.fragment_container, mHomeFragment, HomeFragment.TAG);
        }
        ft.hide(mHomeFragment);
        ft.commit();
    }

    /**
     * This method shows the given Fragment and if there was another visible
     * fragment, it gets hidden. We can just do this because we know that both
     * the mMyListFragment and the mMapFragment were added in the Activity's
     * onCreate, so we just create the fragments once at first and not every
     * time. This will avoid facing some problems with the MapView.
     *
     * @param fragmentIn
     *            The fragment to show.
     */
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
            case R.id.ic_list:
                showFragment(mMyListFragment);
                return true;
            case R.id.ic_map:
                showFragment(mMapFragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class Exchanger {
        public static MapView mMapView;
    }


    public static class MyListFragment extends SherlockListFragment {
        public static final String TAG = "listFragment";
        private final String[] mItems = { "Item 1", "Item 2",
                "Item 3", "Item 4", "Item 5", "Item 6",
                "Item 7", "Item 8", "Item 9", "Item 10" };

        public MyListFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            // Inflate the ListView layout file.
            return inflater.inflate(R.layout.list_fragment, null);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);
            setListAdapter(new ArrayAdapter<String>(getSherlockActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, mItems));
        }
    }

    public class MapFragment extends SherlockFragment {

        public static final String TAG = "mapFragment";

        public MapFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {

            Exchanger.mMapView.setClickable(true);
            Exchanger.mMapView.setBuiltInZoomControls(true);

            Overlay itemizedoverlay = new Overlay(this.getResources().getDrawable(R.drawable.attraction), context);

            Cursor cursor = dbHelper.getAllPlaces();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex("PlaceName"));
                String desc = cursor.getString(cursor.getColumnIndex("PlaceDesc"));
                String under = cursor.getString(cursor.getColumnIndex("PlaceUnder"));
                String work = cursor.getString(cursor.getColumnIndex("PlaceWork"));
                String decription = desc + "\n" + "Метро: " + under + "\n" + "Время работы: " + work;
                Double lat = cursor.getDouble(cursor.getColumnIndex("PlaceLat"));
                Double lng = cursor.getDouble(cursor.getColumnIndex("PlaceLng"));

                itemizedoverlay.addOverlay(new OverlayItem(new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6)), name, decription));
                cursor.moveToNext();
            }
            cursor.close();

            Exchanger.mMapView.getOverlays().add(itemizedoverlay);

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


            return Exchanger.mMapView;
        }
    }

    public static class HomeFragment extends SherlockFragment {
        public static final String TAG = "homeFragment";

        public HomeFragment() {}

        @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            setRetainInstance(true);
        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
            // Inflate the ListView layout file.
            return inflater.inflate(R.layout.autification, null);
        }
        @Override
        public void onViewCreated(View arg0, Bundle arg1) {
            super.onViewCreated(arg0, arg1);
        }
    }
}
