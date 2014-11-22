package com.paradigmcreatives.apspeak.app.util.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.logging.Logger;

public class WhatsAppManager {
	private static String TAG = "WhatsAppManager";

	public static void launchWhatsAppWithText(Context context, String text) {
		if (context != null) {
			if (!TextUtils.isEmpty(text)) {
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.setPackage(Constants.WHATSAPP_APP_PACKAGE);

				intent.putExtra(Intent.EXTRA_TEXT, text);
				context.startActivity(Intent
						.createChooser(intent, "Share with"));
			} else {
				Logger.warn(TAG, "Values in WhatsApp intent are null");
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}
	}

	public static boolean isWhatsAppInstalled(Context context) {
		PackageManager pm = context.getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(Constants.WHATSAPP_APP_PACKAGE,
					PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}
}
