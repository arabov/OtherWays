package com.OtherWays;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import java.util.ArrayList;

public class Main extends SherlockFragmentActivity {

    private MapFragment mMapFragment;
    private MyListFragment mMyListFragment;
    private HomeFragment mHomeFragment;

    private Fragment mVisible = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Exchanger.mMapView = new MapView(this, "0BhdX4jIPYsj2IzVimXgILU8ICs51b2hhRZnVjQ");

        setupFragments();
        // We manually show the list Fragment.
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

		/*
		 * If the activity is killed while in BG, it's possible that the
		 * fragment still remains in the FragmentManager, so, we don't need to
		 * add it again.
		 */
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
        // Inflate the menu with the options to show the Map and the List.

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
        // We will use this MapView always.
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
            // The Activity created the MapView for us, so we can do some init stuff.
            Exchanger.mMapView.setClickable(true);
            Exchanger.mMapView.setBuiltInZoomControls(true); // If you want.

            MapController cMapController = Exchanger.mMapView.getController();

			/*
			 * If you're getting Exceptions saying that the MapView already has
			 * a parent, uncomment the next lines of code, but I think that it
			 * won't be necessary. In other cases it was, but in this case I
			 * don't this should happen.
			 */
			/*
			 * final ViewGroup parent = (ViewGroup) Exchanger.mMapView.getParent();
			 * if (parent != null) parent.removeView(Exchanger.mMapView);
			 */

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
