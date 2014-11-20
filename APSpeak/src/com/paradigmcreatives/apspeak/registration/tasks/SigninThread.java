package com.paradigmcreatives.apspeak.registration.tasks;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.SigninBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.handlers.SigninHandler;
import com.paradigmcreatives.apspeak.registration.tasks.helpers.AddUserToGroupHelper;
import com.paradigmcreatives.apspeak.registration.tasks.helpers.SigninHelper;

public class SigninThread implements Runnable {

	private static final String TAG = "SigninThread";
	private Context context = null;
	private SigninHandler handler = null;

	public SigninThread(Context context, SigninHandler handler) {
		this.context = context;
		this.handler = handler;
	}

	@Override
	public void run() {

		if (context != null) {

			SigninHelper signinHelper = new SigninHelper(context);
			SigninBean signinBean = signinHelper.execute();

			if (signinBean != null && TextUtils.isEmpty(signinBean.getUserID())) {
				// As User is not registered, request for Profile creation with
				// Whatsay server
				handler.requestProfileCreation();
			} else if (signinBean != null
					&& !TextUtils.isEmpty(signinBean.getUserID())) {
				AddUserToGroupHelper addUserToGroupHelper = new AddUserToGroupHelper(
						context, AppPropertiesUtil.getGroupId(context),
						AppPropertiesUtil.getUserID(context));
				if (addUserToGroupHelper.execute()) {
					AppPropertiesUtil.setUserAddedToGroup(context, true);
					// Take the user to setup network if he/she already exists
					AppPropertiesUtil.setAppLaunchedBeforeToTrue(context
							.getApplicationContext());
					handler.launchNewHomeActivity();
					// handler.showFriendsNetwork();
				} else {
					// 2.3 - Some error occurred. Inform the user
					handler.showError(Constants.USER_ADDITON_TO_GROUP_FAILED_ERROR);
				}
			} else {
				// 2.3 - Some error occurred. Inform the user
				handler.showError(-1);
			}

		} else {
			Logger.warn(TAG, "Empty context supplied while trying to signin");
		}

	}

}
