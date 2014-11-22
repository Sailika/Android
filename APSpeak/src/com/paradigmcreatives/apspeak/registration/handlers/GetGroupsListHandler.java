package com.paradigmcreatives.apspeak.registration.handlers;

import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.discovery.fragments.SettingsFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;

/**
 * Message handler for a thread that fetches Whatsay Groups List
 * 
 * @author Dileep | neuv
 * 
 */
public class GetGroupsListHandler extends Handler {

	private static final int SUCCESS = 1;
	private static final int FAILURE = 2;
	private Fragment fragment;

	public GetGroupsListHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS:
			if (fragment != null) {
				if (fragment instanceof FacebookConnectFragment) {
					try {

						if (msg.obj != null) {
							if (fragment instanceof FacebookConnectFragment) {
								((FacebookConnectFragment) fragment)
										.setGroupsList((ArrayList<GroupBean>) msg.obj);

								((FacebookConnectFragment) fragment)
										.loadGroupsData();
							}
						}
					} catch (Exception e) {

					}
				} else if (fragment instanceof FacebookConnectAnimationFragment) {
					try {
						FacebookConnectAnimationFragment facebookConnectAnimationFragment = ((FacebookConnectAnimationFragment) fragment);
						if (msg.obj != null) {
							facebookConnectAnimationFragment
									.setGroupsList((ArrayList<GroupBean>) msg.obj);
						}
						facebookConnectAnimationFragment.loadGroupsData();
					} catch (Exception e) {

					}
				} else if (fragment instanceof ProfileFragment) {
					if (msg.obj != null) {
						((ProfileFragment) fragment)
								.onSuccessfulUserGroupFetch(fragment,(ArrayList<GroupBean>) msg.obj);
					}
				} else if (fragment instanceof SettingsFragment) {
					try {
						if (msg.obj != null) {
							((SettingsFragment) fragment)
									.extractAndProvideCollegeListView((ArrayList<GroupBean>) msg.obj);
						}

					} catch (Exception e) {

					}
				}
			}
			break;

		case FAILURE:

			break;

		default:
			break;
		}
		super.handleMessage(msg);
	}

	public void didFetchComplete(ArrayList<GroupBean> groupsList) {
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = groupsList;
		sendMessage(msg);
	}

	public void didFail() {
		Message msg = new Message();
		msg.what = FAILURE;
		sendMessage(msg);
	}
}
