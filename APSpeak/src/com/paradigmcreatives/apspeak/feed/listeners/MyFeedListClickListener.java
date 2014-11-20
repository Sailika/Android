package com.paradigmcreatives.apspeak.feed.listeners;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.feed.fragments.MyFeedFragment;
import com.paradigmcreatives.apspeak.stream.AppChildActivity;
import com.paradigmcreatives.apspeak.user.UserProfileActivity;

public class MyFeedListClickListener implements OnItemClickListener {

    private Context context = null;
    private Fragment fragment = null;

    public MyFeedListClickListener(Context context, Fragment fragment) {
	this.context = context;
	this.fragment = fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
	if (context != null && fragment != null) {
	    Object obj = adapterView.getItemAtPosition(position);

	    if (obj instanceof MyFeedBean) {
		MyFeedBean bean = (MyFeedBean) obj;
		String type = bean.getType();
		if (!TextUtils.isEmpty(type)) {
		    String userId = null;
		    String assetId = null;
		    Intent intent = null;
		    // Write separate if cases for each PUSH Notification type. As in future, there may be
		    // customizations for
		    // individual notification
		    if (type.equals(Constants.USER_FOLLOWED) || type.equals(Constants.ACCEPTED)
			    || type.equals(Constants.FOLLOW)) {
			userId = bean.getUserId();
			if (!TextUtils.isEmpty(userId)) {
			    intent = new Intent(context, UserProfileActivity.class);
			    intent.putExtra(Constants.USERID, userId);
			}
		    } else if (type.equals(Constants.NEW_FRIEND) || type.equals(Constants.JOIN) || type.equals(Constants.USER)) {
			userId = bean.getUserId();
			if (!TextUtils.isEmpty(userId)) {
			    intent = new Intent(context, UserProfileActivity.class);
			    intent.putExtra(Constants.USERID, userId);
			}
		    } else if (type.equals(Constants.ASSET_LOVED) || type.equals(Constants.EMOTE)) {
			assetId = bean.getAssetId();
			if (!TextUtils.isEmpty(assetId)) {
			    intent = new Intent(context, AppChildActivity.class);
			    intent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
			    intent.putExtra(Constants.ASSETID, assetId);
			}
		    } else if (type.equals(Constants.ASSET_COMMENTED)) {
			assetId = bean.getAssetId();
			if (!TextUtils.isEmpty(assetId)) {
			    intent = new Intent(context, AppChildActivity.class);
			    intent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
			    intent.putExtra(Constants.ASSETID, assetId);
			}
		    } else if (type.equals(Constants.NEW_EXPRESSION) || type.equals(Constants.EXPRESSION)) {
			assetId = bean.getAssetId();
			if (!TextUtils.isEmpty(assetId)) {
			    intent = new Intent(context, AppChildActivity.class);
			    intent.putExtra(Constants.LAUNCH_ASSET_DETAILS, true);
			    intent.putExtra(Constants.ASSETID, assetId);
			}
		    } else if (type.equals(Constants.ANNOUNCEMENT)) {
			try {
			    String jsonString = bean.getMyFeedAsJSON();
			    if (!TextUtils.isEmpty(jsonString)) {
				JSONObject feedJSON = new JSONObject(jsonString);
				if (feedJSON != null && feedJSON.has(JSONConstants.ANNOUNCEMENT_ID)) {
				    String announcementId = feedJSON.getString(JSONConstants.ANNOUNCEMENT_ID);
				    if (!TextUtils.isEmpty(announcementId) && fragment instanceof MyFeedFragment) {
					View announcementView = ((MyFeedFragment) fragment)
						.constructAnnouncementContentView();
					((MyFeedFragment) fragment).showAnnouncementContentDialog(announcementView);
					((MyFeedFragment) fragment).fetchAnnouncementContentFromServer(announcementId);
				    }
				}
			    }
			} catch (Exception e) {

			}
		    }
		    if (intent != null) {
			context.startActivity(intent);
		    }
		}
	    }
	}
    }

}
