package com.paradigmcreatives.apspeak.share.listeners;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.ShareAppInfo;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.share.adapters.ShareAppsInfoAdapter;

/**
 * Listener class to handle Share Via Apps list item click
 * 
 * @author Dileep | neuv
 * 
 */
public class ShareAppItemOnClickListener implements OnItemClickListener {
	private static final String TAG = "ShareImageAppItemClickImpl";
	Activity activity;
	private String url;
	private boolean isLinkShare;
	Dialog dialog;

	public ShareAppItemOnClickListener(Activity activity, Dialog dialog, String url, boolean isLinkShare) {
		this.activity = activity;
		this.isLinkShare = isLinkShare;
		this.url = url;
		this.dialog = dialog;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
		if (parent.getAdapter() instanceof ShareAppsInfoAdapter) {
			ShareAppsInfoAdapter adapter = (ShareAppsInfoAdapter) parent.getAdapter();
			LinearLayout layout = (LinearLayout) item.findViewById(R.id.app_item);
			if (layout != null) {
				ShareAppInfo shareImageInfo = adapter.getItem(position);

				/* TODO: Uncomment for Google Analytics
				if (activity != null && shareImageInfo != null) {
					GoogleAnalytics.sendEventTrackingInfoToGA(activity, GoogleAnalyticsConstants.DOODLE_SHARE_CAT_NAME,
							GoogleAnalyticsConstants.EVENT_ACTION_BUTTON, shareImageInfo.getName());

				}
				*/
				String appPackage = shareImageInfo.getApp_package();
				if (isLinkShare && activity != null) {
					Intent intent = new Intent(android.content.Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.setPackage(appPackage);
					intent.putExtra(android.content.Intent.EXTRA_TEXT, url);
					try {
						activity.startActivity(intent);
					} catch (android.content.ActivityNotFoundException ex) {
						Toast.makeText(activity, "There are no related applications installed.", Toast.LENGTH_SHORT)
								.show();
					}

				}

			} else {
				Logger.warn(TAG, "layout is null");
			}
			dialog.dismiss();
		} else {
			Logger.warn(TAG, "Invalid adapter");
		}
	}

}
