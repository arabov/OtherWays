package com.OtherWays;

/**
 *
 */
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.ActionMode;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class ActionModes extends SherlockActivity {
    ActionMode mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(SampleList.THEME); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_modes);

        ((Button)findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMode = startActionMode(new AnActionModeOfEpicProportions());
            }
        });
        /*
        ((Button)findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMode != null) {
                    mMode.finish();
                }
            }
        });
        */
    }

    private final class AnActionModeOfEpicProportions implements ActionMode.Callback {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            //Used to put dark icons on light action bar
            boolean isLight = SampleList.THEME == R.style.Theme_Sherlock_Light;

            menu.add("Save")
                    .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Search")
                    .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Refresh")
                    .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Save")
                    .setIcon(isLight ? R.drawable.ic_compose_inverse : R.drawable.ic_compose)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Search")
                    .setIcon(isLight ? R.drawable.ic_search_inverse : R.drawable.ic_search)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            menu.add("Refresh")
                    .setIcon(isLight ? R.drawable.ic_refresh_inverse : R.drawable.ic_refresh)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            Toast.makeText(ActionModes.this, "Got click: " + item, Toast.LENGTH_SHORT).show();
            mode.finish();
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
        }
    }
}
