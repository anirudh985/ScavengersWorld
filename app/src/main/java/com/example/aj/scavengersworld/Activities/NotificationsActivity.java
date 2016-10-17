package com.example.aj.scavengersworld.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.aj.scavengersworld.R;

/**
 * Created by Jennifer on 10/17/2016.
 */
public class NotificationsActivity extends AppCompatActivity {
	Toolbar toolbar;

	private final String LOG_TAG = getClass().getSimpleName();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(LOG_TAG, "onCreate()");
		setContentView(R.layout.activity_notifications);
		configureToolbar();
	}
	//protected abstract String getScreenName(); TODO??

	private void configureToolbar() {
		Log.d(LOG_TAG, "configureToolbar()");
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			//getSupportActionBar().setTitle(getScreenName()); TODO??

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(LOG_TAG, "onCreateOptionsMenu()");
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(LOG_TAG, "onOptionsItemSelected()");
		switch (item.getItemId()) {
			case android.R.id.home:
				FragmentManager fm = getSupportFragmentManager();
				if (fm != null && fm.getBackStackEntryCount() > 0) {
					fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
				} else {
					finish();
				}
				return true;
			case R.id.signout:
				signout();
				return true;
			case R.id.profile:
				openProfileActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void openProfileActivity(){
		Log.d(LOG_TAG, "openProfileActivity()");
		//TODO: use SessionManager to retrieve username and query for Profile Information

	}

	private void signout(){
		Log.d(LOG_TAG, "signout()");
		//TODO: use SessionManager to retireve username and signout
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(LOG_TAG, "******* onStart() **********");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(LOG_TAG, "******* onResume() **********");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(LOG_TAG, "******* onPause() **********");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(LOG_TAG, "******* onStop() **********");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(LOG_TAG, "******* onDestroy() **********");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(LOG_TAG, "******* onRestart() **********");
	}
}
