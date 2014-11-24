package com.paradigmcreatives.apspeak.globalstream;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.autosend.AutoSendStatusBroadcastReceiver;
import com.paradigmcreatives.apspeak.discovery.fragments.SettingsFragment;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;

/**
 * Activity that acts as secondary/child activity of the Application. Can be
 * used to show a fragment of Global Stream Screen fragment, etc.
 * 
 * @author Dileep | neuv
 * 
 */
public class AppNewChildActivity extends FragmentActivity {

	public static final int SUBMIT_EXPRESSION = 99;
	private Fragment fragment;
	private AutoSendStatusBroadcastReceiver mAutoSendBroadcastReceiver;
	private TextView globelTxtView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newchild_activity_layout);
		globelTxtView = (TextView) findViewById(R.id.globel_header_text);
		createFragmentBasedOnIntentValues();
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (fragment != null && fragmentManager != null) {
			fragmentManager.beginTransaction()
					.replace(R.id.newchild_content_frame, fragment).commit();
		}
	}

	/**
	 * Reads the intent extras values and creates a valid fragment to display
	 */
	private void createFragmentBasedOnIntentValues() {
		Bundle data = getIntent().getExtras();
		if (data != null) {
			if (data.containsKey(Constants.LAUNCH_GLOBALSTREAM_SCREEN)
					&& data.getBoolean(Constants.LAUNCH_GLOBALSTREAM_SCREEN)
					&& data.containsKey(Constants.CUE_OBJECT)) {
				Campaigns cue = data.getParcelable(Constants.CUE_OBJECT);
				if (cue != null) {
					globelTxtView.setText(getResources().getString(
							R.string.poll_your_opinion));
					fragment = new GlobalStreamsFragment(cue);
				}
			} else if (data
					.containsKey(Constants.LAUNCH_INVITE_FBFRIENDS_SCREEN)
					&& data.getBoolean(Constants.LAUNCH_INVITE_FBFRIENDS_SCREEN)) {
				globelTxtView.setText(getResources().getString(
						R.string.invite_friends_heading));
				fragment = new UserNetworkFragment(
						UserNetwork.FACEBOOK_FRIENDS,
						AppPropertiesUtil.getUserID(this), false, false);
				((UserNetworkFragment) fragment)
						.setProgressDialogVisibilityFlag(false);
			} else if (data.containsKey(Constants.LAUNCH_SETTINGS_SCREEN)
					&& data.getBoolean(Constants.LAUNCH_SETTINGS_SCREEN)) {
				fragment = new SettingsFragment();
			}
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		AppEventsLogger.activateApp(getApplicationContext(),
				Constants.FACEBOOK_APPID);
		mAutoSendBroadcastReceiver = new AutoSendStatusBroadcastReceiver(this);
		registerReceiver(mAutoSendBroadcastReceiver, new IntentFilter(
				Constants.AUTOSEND_STATUS_BRAODCAST_ACTION));
	}
	@Override
	public void setTitle(int titleId) {
		super.setTitle(titleId);
		globelTxtView.setText(titleId);
	}

	@Override
	protected void onStop() {
		if (mAutoSendBroadcastReceiver != null) {
			unregisterReceiver(mAutoSendBroadcastReceiver);
		}
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case SUBMIT_EXPRESSION:
				if (fragment != null
						&& fragment instanceof GlobalStreamsFragment) {
					((GlobalStreamsFragment) fragment).refreshQueueLayout();
				}
				break;
			case Constants.ASSET_DETAILS_RESULT_CODE:
				if (fragment != null
						&& fragment instanceof GlobalStreamsFragment) {
					((GlobalStreamsFragment) fragment).refreshStream();
				}
				break;

			default:
				break;
			}
		}
	}

	/**
	 * Refreshes queue layout in GlobalStreamsFragment, that represents the
	 * number of expressions that are available in DB for auto-submit to Whatsay
	 * server
	 */
	public void refreshQueueLayout() {
		if (fragment != null && fragment instanceof GlobalStreamsFragment) {
			((GlobalStreamsFragment) fragment).refreshQueueLayout();
		}
	}
}
