package com.paradigmcreatives.apspeak.registration.handlers;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;

/**
 * Message handler for a thread that fetches Whatsay Groups List
 * 
 * @author Dileep | neuv
 * 
 */
public class AddUserToGroupHandler extends Handler {

	private static final int SUCCESS = 1;
	private static final int FAILURE = 2;

	private Activity activity;
	private Fragment fragment;

	public AddUserToGroupHandler(Fragment fragment) {
		super();
		this.fragment = fragment;
	}

	public AddUserToGroupHandler(Activity activity) {
		super();
		this.activity = activity;
	}

	@Override
	public void handleMessage(Message msg) {
		switch (msg.what) {
		case SUCCESS:
			if (fragment != null) {
				try {
					String groupId = (String) msg.obj;
					AppPropertiesUtil.setGroupID(fragment.getActivity(),
							groupId);
					AppPropertiesUtil.setUserAddedToGroup(
							fragment.getActivity(), true);
				} catch (Exception e) {

				}
			} else if (activity != null) {
				if (activity instanceof AppNewHomeActivity) {
					try {
						String groupId = (String) msg.obj;
						AppPropertiesUtil.setGroupID(fragment.getActivity(),
								groupId);
						AppPropertiesUtil.setUserAddedToGroup(
								fragment.getActivity(), true);
					} catch (Exception e) {

					}
					((AppNewHomeActivity) activity).userAddedSuccessfully();
				}
			}
			break;

		case FAILURE:
			if (fragment != null
					&& fragment instanceof FacebookConnectAnimationFragment) {
				((FacebookConnectAnimationFragment) fragment).showError("",
						Constants.USER_ADDITON_TO_GROUP_FAILED_ERROR);
			} else if (activity != null
					&& activity instanceof AppNewHomeActivity) {
				((AppNewHomeActivity) activity).showError("",
						Constants.USER_ADDITON_TO_GROUP_FAILED_ERROR);
			}
			break;

		default:
			break;
		}
		super.handleMessage(msg);
	}

	public void didAddComplete(String groupId) {
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = groupId;
		sendMessage(msg);
	}

	public void didFail() {
		Message msg = new Message();
		msg.what = FAILURE;
		sendMessage(msg);
	}
}
