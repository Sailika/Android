package com.paradigmcreatives.apspeak.user;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;

/**
 * Show user profile in a separate activity
 * 
 * @author robin
 * 
 */
public class UserProfileActivity extends FragmentActivity {
	private Fragment fragment;
	public static final String USER_ID = "user_id";
	private User user = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_activity_layout);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		createFragmentBasedOnIntentValues();
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (fragment != null && fragmentManager != null) {
			fragmentManager.beginTransaction()
					.replace(R.id.child_content_frame, fragment).commit();
		}
	}

	/**
	 * Reads the intent extras values and creates a valid fragment to display
	 */
	private void createFragmentBasedOnIntentValues() {
		String userID = null;
		Bundle data = getIntent().getExtras();
		if (data != null) {
			if (data.containsKey(USER_ID)) {
				userID = data.getString(USER_ID);
			}
		}
		// Decrease the notifications count
		int notificationsCount = AppPropertiesUtil
				.getNotificationsCount(UserProfileActivity.this);
		if (notificationsCount > 0) {
			--notificationsCount;
		}
		AppPropertiesUtil.setNotificationsCount(UserProfileActivity.this,
				notificationsCount);
		// Launch Profile Screen
		fragment = new ProfileFragment(userID);
		// Show a toast if there is no internet
		if (!Util.isOnline(this)) {
			Toast.makeText(this, getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getApplicationContext(), Constants.FACEBOOK_APPID);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to action bar's Up/Home button
		case android.R.id.home:
			/*
			 * TODO: Uncomment for Google Analytics
			 * GoogleAnalytics.sendEventTrackingInfoToGA
			 * (UserProfileActivity.this
			 * ,GoogleAnalyticsConstants.FRIENDS_PROFILE_SCREEN,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.FRIEND_PROFILE_FOLLOW_BUTTON);
			 */
			/*
			 * Intent upIntent = NavUtils.getParentActivityIntent(this); if
			 * (NavUtils.shouldUpRecreateTask(this, upIntent)) { // This
			 * activity is NOT part of this app's task, so create a new // task
			 * // when navigating up, with a synthesized back stack.
			 * TaskStackBuilder.create(this) // Add all of this activity's
			 * parents to the back stack .addNextIntentWithParentStack(upIntent)
			 * // Navigate up to the closest parent .startActivities();
			 * 
			 * 
			 * 
			 * } else { // This activity is part of this app's task, so simply
			 * // navigate up to the logical parent activity.
			 * NavUtils.navigateUpTo(this, upIntent); }
			 */
			super.onBackPressed();
			return true;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public User getCurrentUser() {
		return user;
	}

	public void setCurrentUser(User user) {
		this.user = user;
	}

	/**
	 * Sets user stream
	 * 
	 * @param streamAssets
	 */
	public void setUserStream(ArrayList<StreamAsset> streamAssets) {
		// this.streamAssets = streamAssets;
		if (fragment != null) {
			if (fragment instanceof ProfileFragment) {
				((ProfileFragment) fragment).setUserStream(streamAssets);
			}
		}
	}
}// end of class