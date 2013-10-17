package se.persandstrom.heysend;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ItemListActivity extends FragmentActivity {

	private static final String TAG = ItemListActivity.class.getSimpleName();

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;

	private ListView leftDrawer;

	private final List<String> leftDrawerList = new ArrayList<String>();
	{
		leftDrawerList.add("123123");
		leftDrawerList.add("321312");
	}

	private String mTitle = "HeySend";

	private GcmHandler gcmHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		gcmHandler = new GcmHandler(this);

		setContentView(R.layout.drawer_layout);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_action_backspace, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			@Override
			public void onDrawerOpened(View drawerView) {
//				getActionBar().setTitle(mDrawerTitle);
			}
		};

		leftDrawer = (ListView) findViewById(R.id.left_drawer);
		leftDrawer.setAdapter(new LeftViewAdapter(this, leftDrawerList));
		leftDrawer.setOnItemClickListener(new DrawerItemClickListener());

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		loadMainFragment();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "onOptionsItemSelected");

		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle your other action bar items...

		return super.onOptionsItemSelected(item);
	}

	private class LeftViewAdapter extends ArrayAdapter<String> {

		public LeftViewAdapter(Context context, List<String> objects) {
			super(context, R.layout.drawer_item, R.id.text, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getView(position, convertView, parent);
		}

	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// Create a new fragment and specify the planet to show based on position

			switch (position) {
			case 0:
				loadMainFragment();
				break;
			case 1:
				// do something
				break;
			default:
				throw new RuntimeException("wtf position: " + position);
			}

			// Highlight the selected item, update the title, and close the drawer
//            mDrawerList.setItemChecked(position, true);
//            setTitle(mPlanetTitles[position]);
			mDrawerLayout.closeDrawer(leftDrawer);
		}
	}

	private void loadMainFragment() {
		Fragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putInt("temp", 1);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
	}

	public GcmHandler getGcmHandler() {
		return gcmHandler;
	}
	
	
}