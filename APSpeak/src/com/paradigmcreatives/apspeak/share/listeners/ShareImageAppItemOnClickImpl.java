package com.paradigmcreatives.apspeak.share.listeners;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.model.ShareAppInfo;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.share.adapters.ShareWhatsayImageAdapter;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsFragment;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;

public class ShareImageAppItemOnClickImpl implements OnItemClickListener {
    private static final String TAG = "ShareImageAppItemClickImpl";
    Activity activity;
    private String imagePath;
    private String url;
    private boolean isLinkShare;
    private String folderPath;
    Dialog dialog;

    public ShareImageAppItemOnClickImpl(Activity activity, Dialog dialog, String imgpath, String url,
	    String folderPath, boolean isLinkShare) {
	this.activity = activity;
	this.imagePath = imgpath;
	this.isLinkShare = isLinkShare;
	this.url = url;
	this.dialog = dialog;
	this.folderPath = folderPath;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View item, int position, long id) {
	if (parent.getAdapter() instanceof ShareWhatsayImageAdapter) {
	    ShareWhatsayImageAdapter adapter = (ShareWhatsayImageAdapter) parent.getAdapter();
	    LinearLayout layout = (LinearLayout) item.findViewById(R.id.app_item);
	    if (layout != null) {
		ShareAppInfo shareAppInfo = adapter.getItem(position);
		String appPackage = shareAppInfo.getApp_package();
		recordGoogleAnalyticsEvent(shareAppInfo);
		if (!isLinkShare) {
		    /*
		    if (TextUtils.equals(appPackage, Constants.FACEBOOK_APP_PACKAGE)) {
			if (FacebookManager.isFacebookAppInstalled(activity)) {
			    if (activity instanceof CanvasActivity) {
				((CanvasActivity) activity).setShareMode(DoodleShareMode.LINK_SHARE);
				((CanvasActivity) activity).setImageAsLink(true);

			    }

			    ShareDoodlesThread shareDoodleThread = new ShareDoodlesThread(activity,
				    new ShareDoodlesHandler(activity), DoodleShareMode.LINK_SHARE,
				    ((CanvasActivity) activity).getSelectedTransactionId(), null, folderPath);

			    Thread t = new Thread(shareDoodleThread);
			    t.start();

			} else {
			    Intent intent = new Intent((CanvasActivity) activity, PublishOnWallActivity.class);
			    intent.putExtra("bitmap", imagePath);
			    intent.putExtra("path", url);
			    activity.startActivity(intent);
			}
		    } else {
			*/
			Intent imageSend = new Intent();
			imageSend.setAction(Intent.ACTION_SEND);
			imageSend.setType("image/*");
			imageSend.setPackage(appPackage);
			imageSend.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + imagePath));
			activity.startActivity(imageSend);
		   // }
		}
	    } else {
		Logger.warn(TAG, "layout is null");
	    }
	    dialog.dismiss();
	} else {
	    Logger.warn(TAG, "Invalid adapter");
	}
    }

    /**
     * Records the event to Google Analytics
     * @param appInfo
     */
    private void recordGoogleAnalyticsEvent(ShareAppInfo appInfo){
	if(appInfo != null && activity != null){
	    String packageName = appInfo.getApp_package();
	    if(!TextUtils.isEmpty(packageName)){
		if(packageName.contains(Constants.FACEBOOK_APP_PACKAGE)){
			GoogleAnalyticsHelper.sendEventToGA(activity,
				GoogleAnalyticsConstants.SHARING_SCREEN, GoogleAnalyticsConstants.ACTION_BUTTON,
				GoogleAnalyticsConstants.SHARING_FACEBOOK_BUTTON);
		}else if(packageName.contains(Constants.WHATSAPP_APP_PACKAGE)){
			GoogleAnalyticsHelper.sendEventToGA(activity,
				GoogleAnalyticsConstants.SHARING_SCREEN, GoogleAnalyticsConstants.ACTION_BUTTON,
				GoogleAnalyticsConstants.SHARING_WHATSAPP_BUTTON);
		}
	    }
	}
    }
}
