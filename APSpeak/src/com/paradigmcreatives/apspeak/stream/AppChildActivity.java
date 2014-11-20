package com.paradigmcreatives.apspeak.stream;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;

/**
 * Activity that acts as secondary/child activity of the Application. It
 * provides action bar UP/back navigation. Can be used to show a fragment of
 * asset liked friends list, asset details fragment, etc.
 * 
 * @author Dileep | neuv
 * 
 */
public class AppChildActivity extends FragmentActivity {

	private Fragment fragment;
	private ImageView mShareIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.child_activity_layout);
		/* getActionBar().setDisplayHomeAsUpEnabled(false); */

		initUI();

		createFragmentBasedOnIntentValues();
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (fragment != null && fragmentManager != null) {
			fragmentManager.beginTransaction()
					.replace(R.id.child_content_frame, fragment).commit();
		}
	}

	public void initUI() {
		mShareIcon = (ImageView) findViewById(R.id.share_image);
		mShareIcon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (fragment != null
						&& fragment instanceof AssetDetailsWithCommentsFragment) {
					((AssetDetailsWithCommentsFragment) fragment).shareAsset();
				}
			}
		});
	}

	/**
	 * Reads the intent extras values and creates a valid fragment to display
	 */
	private void createFragmentBasedOnIntentValues() {
		Bundle data = getIntent().getExtras();
		if (data != null) {
			if ((data.containsKey(Constants.LAUNCH_ASSETLIKED_USERSLIST) && data
					.getBoolean(Constants.LAUNCH_ASSETLIKED_USERSLIST))
					|| (data.containsKey(Constants.LAUNCH_ASSETREPOSTED_USERSLIST) && data
							.getBoolean(Constants.LAUNCH_ASSETREPOSTED_USERSLIST))) {
				/*
				 * 
				 * Need to create a fragment that display list of users who
				 * liked asset OR who reposted asset (depends on Intent value)
				 */
				UserNetwork listTypeToFetch = UserNetwork.LOVED_USERS;
				if (data.containsKey(Constants.LAUNCH_ASSETREPOSTED_USERSLIST)) {
					listTypeToFetch = UserNetwork.REPOSTED_USERS;
				}
				String assetAsJSON = null;
				if (data.containsKey(Constants.ASSET_AS_JSON)) {
					assetAsJSON = data.getString(Constants.ASSET_AS_JSON);
				}
				if (!TextUtils.isEmpty(assetAsJSON)) {
					try {
						fragment = new UserNetworkFragment(new JSONObject(
								assetAsJSON), listTypeToFetch, false, false);
					} catch (JSONException e) {

					}
				}

				if (data.containsKey(Constants.LIKED_USERS)) {
					ArrayList<Friend> list = data
							.getParcelableArrayList(Constants.LIKED_USERS);
					fragment = new UserNetworkFragment(UserNetwork.LOVED_USERS,
							list, false);
				}
			} else if (data.containsKey(Constants.LAUNCH_ASSET_DETAILS)
					&& data.containsKey(Constants.ASSET_OBJECT)) {
				StreamAsset asset = data.getParcelable(Constants.ASSET_OBJECT);
				if (asset != null) {
					// Decrease the notifications count
					int notificationsCount = AppPropertiesUtil.getNotificationsCount(AppChildActivity.this);
					if(notificationsCount > 0){
						--notificationsCount;
					}
					AppPropertiesUtil.setNotificationsCount(AppChildActivity.this, notificationsCount);
					// Launch Asset Details screen
					fragment = new AssetDetailsWithCommentsFragment(asset);
					if (mShareIcon != null) {
						mShareIcon.setVisibility(View.VISIBLE);
					}
				}
				// Show a toast if there is no internet
				if (!Util.isOnline(this)) {
					Toast.makeText(this,
							getResources().getString(R.string.no_network),
							Toast.LENGTH_SHORT).show();
				}
			} else if (data.containsKey(Constants.LAUNCH_ASSET_DETAILS)
					&& data.containsKey(Constants.ASSETID)) {
				if (data.getBoolean(Constants.LAUNCH_ASSET_DETAILS)) {
					String assetId = data.getString(Constants.ASSETID);
					if (!TextUtils.isEmpty(assetId)) {
						// Decrease the notifications count
						int notificationsCount = AppPropertiesUtil.getNotificationsCount(AppChildActivity.this);
						if(notificationsCount > 0){
							--notificationsCount;
						}
						AppPropertiesUtil.setNotificationsCount(AppChildActivity.this, notificationsCount);
						// Launch Asset Details screen
						fragment = new AssetDetailsWithCommentsFragment(assetId);
						if (mShareIcon != null) {
							mShareIcon.setVisibility(View.VISIBLE);
						}
					}
					// Show a toast if there is no internet
					if (!Util.isOnline(this)) {
						Toast.makeText(this,
								getResources().getString(R.string.no_network),
								Toast.LENGTH_SHORT).show();
					}
				}
			}

		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getApplicationContext(), Constants.FACEBOOK_APPID);
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu items MenuInflater inflater = getMenuInflater();
	 * inflater.inflate(R.menu.assetdetails_menu, menu); return
	 * super.onCreateOptionsMenu(menu); }
	 */
	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { switch
	 * (item.getItemId()) { // Respond to action bar's Up/Home button case
	 * android.R.id.home: if(fragment != null){ if(fragment instanceof
	 * AssetDetailsWithCommentsFragment){
	 * GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
	 * GoogleAnalyticsConstants.STREAM_DETAIL_SCREEN,
	 * GoogleAnalyticsConstants.ACTION_BUTTON,
	 * GoogleAnalyticsConstants.STREAMDETAIL_BACK_BUTTON); } }
	 * 
	 * 
	 * Intent upIntent = NavUtils.getParentActivityIntent(this); if
	 * (NavUtils.shouldUpRecreateTask(this, upIntent)) { // This activity is NOT
	 * part of this app's task, so create a new // task // when navigating up,
	 * with a synthesized back stack. TaskStackBuilder.create(this) // Add all
	 * of this activity's parents to the back stack
	 * .addNextIntentWithParentStack(upIntent) // Navigate up to the closest
	 * parent .startActivities(); } else { // This activity is part of this
	 * app's task, so simply // navigate up to the logical parent activity.
	 * NavUtils.navigateUpTo(this, upIntent); }
	 * 
	 * super.onBackPressed(); return true; case R.id.assetDetailsShare:
	 * if(fragment != null){ if(fragment instanceof AssetDetailsFragment){
	 * ((AssetDetailsFragment)fragment).shareAsset(); } else if(fragment
	 * instanceof AssetDetailsWithCommentsFragment){
	 * GoogleAnalyticsHelper.sendEventToGA(fragment.getActivity(),
	 * GoogleAnalyticsConstants.STREAM_DETAIL_SCREEN,
	 * GoogleAnalyticsConstants.ACTION_BUTTON,
	 * GoogleAnalyticsConstants.STREAMDETAIL_REPOST_BUTTON);
	 * ((AssetDetailsWithCommentsFragment)fragment).shareAsset(); } } break;
	 * default: break; } return super.onOptionsItemSelected(item); }
	 */

	/**
	 * Returns height of the action bar dynamically
	 * 
	 * @return
	 */
	/*
	 * public int getActionBarHeight(){ return getActionBar().getHeight(); }
	 */

	/**
	 * Returns status bar/notification bar height dynamically
	 * 
	 * @return
	 */
	public int getStatusBarHeight() {
		int result = 0;
		int resourceId = getResources().getIdentifier("status_bar_height",
				"dimen", "android");
		if (resourceId > 0) {
			result = getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
