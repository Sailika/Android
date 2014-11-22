package com.paradigmcreatives.apspeak.app.util.dialogs;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.ShareAppInfo;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImagePreviewFragment;
import com.paradigmcreatives.apspeak.share.adapters.ShareAppsInfoAdapter;
import com.paradigmcreatives.apspeak.share.adapters.ShareWhatsayImageAdapter;
import com.paradigmcreatives.apspeak.share.listeners.ShareAppItemOnClickListener;
import com.paradigmcreatives.apspeak.share.listeners.ShareImageAppItemOnClickImpl;
import com.paradigmcreatives.apspeak.textstyles.TypeFontAssets;

/**
 * This Class contains methods to display various dialogs.
 * 
 * @author
 * 
 */
public class WhatsayDialogsUtil {

	/**
	 * This dialog is shown when app wants to exit.
	 * 
	 * @param mContext
	 * @param title
	 * @return
	 */
	public static Dialog exitDialog(Context mContext, String title) {
		
		TypeFontAssets typeFontAssets = new TypeFontAssets(mContext);
		
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View emptyCanvasDialog = inflater.inflate(R.layout.empty_canvas_dialog,
				null);
		final Dialog dialog = new Dialog(mContext,
				android.R.style.Theme_Translucent_NoTitleBar);

		Button okButton = (Button) emptyCanvasDialog
				.findViewById(R.id.brush_close);
		TextView dialogTitle = (TextView) emptyCanvasDialog
				.findViewById(R.id.empty_doodle_title);
		Typeface myTypeface = Typeface.createFromAsset(mContext.getAssets(),
				"Roboto-Regular.ttf");
		dialogTitle.setText(title);
		dialogTitle.setTypeface(typeFontAssets.boldFont);
		okButton.setTypeface(myTypeface);
		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				System.exit(0);
			}
		});
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(emptyCanvasDialog);
		return dialog;
	}

	/**
	 * Dialog to show social apps which share a picture
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog shareDoodleDialog(final Activity activity, String url,
			List<ShareAppInfo> list, boolean isLinkShare) {
		View shareDialog = (View) LayoutInflater.from((Context) activity)
				.inflate(R.layout.social_share_dialog, null);
		final Dialog dialog = new Dialog(activity,
				android.R.style.Theme_Translucent_NoTitleBar);
		ListView apps_view = (ListView) shareDialog
				.findViewById(R.id.share_image_apps_list);
		TextView heading_view = (TextView) shareDialog
				.findViewById(R.id.invite_friends_heading);
		if (isLinkShare) {
			heading_view.setText(activity.getString(R.string.share_title_text));
		}
		apps_view.setAdapter(new ShareAppsInfoAdapter(activity, list));
		apps_view.setOnItemClickListener(new ShareAppItemOnClickListener(
				activity, dialog, url, isLinkShare));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(shareDialog);
		return dialog;
	}

	/**
	 * Dialog to show social apps which share a picture
	 * 
	 * @param context
	 * @return
	 */
	public static Dialog shareDoodleDialog(final Activity activity,
			final String thumbnailPath, String url, String folderPath,
			List<ShareAppInfo> list, boolean isLinkShare) {
		View shareDialog = (View) LayoutInflater.from((Context) activity)
				.inflate(R.layout.social_share_dialog, null);
		final Dialog dialog = new Dialog(activity,
				android.R.style.Theme_Translucent_NoTitleBar);

		ListView apps_view = (ListView) shareDialog
				.findViewById(R.id.share_image_apps_list);
		TextView heading_view = (TextView) shareDialog
				.findViewById(R.id.invite_friends_heading);
		if (isLinkShare) {
			heading_view.setText("Share doodle link");
		}
		apps_view.setAdapter(new ShareWhatsayImageAdapter(activity, list));
		apps_view.setOnItemClickListener(new ShareImageAppItemOnClickImpl(
				activity, dialog, thumbnailPath, url, folderPath, isLinkShare));
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(shareDialog);
		return dialog;
	}

	public static Dialog submitFailedDialog(final Activity activity,
			final ImagePreviewFragment fragment) {
		View shareDialog = (View) LayoutInflater.from((Context) activity)
				.inflate(R.layout.submit_failed, null);
		final Dialog dialog = new Dialog(activity,
				android.R.style.Theme_Translucent_NoTitleBar);
		LinearLayout retry = (LinearLayout) shareDialog
				.findViewById(R.id.send_retry);
		retry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				fragment.startSubmit();
				if(dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}

			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(shareDialog);
		return dialog;

	}

}
