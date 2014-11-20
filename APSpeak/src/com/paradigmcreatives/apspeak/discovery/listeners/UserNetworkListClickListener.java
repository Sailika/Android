package com.paradigmcreatives.apspeak.discovery.listeners;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.user.UserProfileActivity;

public class UserNetworkListClickListener implements OnItemClickListener {

	private Context context = null;
	private Fragment fragment = null;
	private UserNetwork network = null;

	public UserNetworkListClickListener(Context context, Fragment fragment,
			UserNetwork network) {
		this.context = context;
		this.fragment = fragment;
		this.network = network;
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		if (context != null && fragment != null) {
			if (network != null && network == UserNetwork.FACEBOOK_FRIENDS) {
				if (view != null) {
					Friend friend = ((Friend) adapterView
							.getItemAtPosition(position));
					if (!TextUtils.isEmpty(friend.getUserId())) {
						Intent intent = new Intent(context,
								UserProfileActivity.class);
						intent.putExtra(UserProfileActivity.USER_ID,
								friend.getUserId());
						context.startActivity(intent);
					} else {
						try {
							ImageView followUnFollowIcon = (ImageView) view
									.findViewById(R.id.friend_invite_pic);
							followUnFollowIcon.performClick();
						} catch (Exception e) {

						}
					}

				}
			} else {
				Object obj = adapterView.getItemAtPosition(position);
				if (obj instanceof Friend) {
					String userID = ((Friend) obj).getUserId();
					if (!TextUtils.isEmpty(userID)) {
						Intent intent = new Intent(context,
								UserProfileActivity.class);
						intent.putExtra(UserProfileActivity.USER_ID, userID);
						context.startActivity(intent);
					}
				}
			}
		}
	}

}
