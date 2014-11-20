package com.paradigmcreatives.apspeak.app.util.share;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.ShareAppInfo;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;

/**
 * Utility class to fetch all the Applications list those are capable of sharing an asset URL
 * 
 * @author Dileep | neuv
 *
 */
public class ShareUtil {
	public static int fbId = -1;
	public static int twitterId = -1;
	public static int whatsappId = -1;
	public static int doodly_doo_Id = -1;
	public static int printrestAppId = -1;
	public static int messagingAppId = -1;

	public static List<ShareAppInfo> getLinkShareAppsList(Context context) {
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		//intent.setType("text/plain");
		intent.setType("image/*");
		List<ResolveInfo> resInfo = context.getPackageManager()
				.queryIntentActivities(intent, 0);
		PackageManager pm = context.getPackageManager();
		List<ShareAppInfo> preferredAppsList = new ArrayList<ShareAppInfo>();
		List<ShareAppInfo> shareImageAppsList = new ArrayList<ShareAppInfo>();

		if (!resInfo.isEmpty()) {
			for (ResolveInfo resolveInfo : resInfo) {
				String appName = (String) resolveInfo.loadLabel(pm);
				Drawable appIcon = resolveInfo.loadIcon(pm);
				String appPackage = resolveInfo.activityInfo.packageName;
				ShareAppInfo shareImageInfo = new ShareAppInfo(appName,
						appIcon, appPackage);
				shareImageAppsList.add(shareImageInfo);
				if (appPackage.equals(Constants.FACEBOOK_APP_PACKAGE)) {
					fbId = shareImageAppsList.indexOf(shareImageInfo);
				}
				if (appPackage.equals(Constants.MESSAGING_APP_PACKAGE)) {
					messagingAppId = shareImageAppsList.indexOf(shareImageInfo);
				}
				if (appPackage.equals(Constants.WHATSAPP_APP_PACKAGE)) {
					whatsappId = shareImageAppsList.indexOf(shareImageInfo);
				}
				if (appPackage.equals(Constants.TWITTER_APP_PACKAGE)) {
					twitterId = shareImageAppsList.indexOf(shareImageInfo);
				}

			}
		}
		int preferredAppsCount = 0;
		if (messagingAppId >= 0) {
			preferredAppsList.add(preferredAppsCount++,
					shareImageAppsList.get(messagingAppId));
		}
		if (whatsappId >= 0) {
			preferredAppsList.add(preferredAppsCount++,
					shareImageAppsList.get(whatsappId));
		}

		if (twitterId >= 0) {
			preferredAppsList.add(preferredAppsCount++,
					shareImageAppsList.get(twitterId));
		}
		int size = shareImageAppsList.size();
		for (int i = 0; i < size; i++) {
			if (i == fbId || i == messagingAppId || i == twitterId
					|| i == whatsappId) {
				// do nothing, as they are already added to preferredAppsList
			} else {
				preferredAppsList.add(preferredAppsCount++,
						shareImageAppsList.get(i));
			}

		}

		if(fbId >= 0){
			preferredAppsList.add(0, new ShareAppInfo("Facebook", context
					.getResources().getDrawable(R.drawable.fb_icon),
					Constants.FACEBOOK_APP_PACKAGE));
		}

		return preferredAppsList;

	}
}
