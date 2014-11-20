package com.paradigmcreatives.apspeak.registration.tasks;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.RegisterBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.registration.handlers.ProfileBuildupHandler;
import com.paradigmcreatives.apspeak.registration.tasks.helpers.AddUserToGroupHelper;
import com.paradigmcreatives.apspeak.registration.tasks.helpers.RegistrationHelper;

public class ProfileBuildupThread implements Runnable {

    private Fragment fragment = null;
    private ProfileBuildupHandler handler = null;

    public ProfileBuildupThread(Fragment fragment, ProfileBuildupHandler handler) {
	this.fragment = fragment;
	this.handler = handler;
    }

    @Override
    public void run() {
	if (fragment != null && handler != null) {
	    handler.onStart();
	    RegistrationHelper helper = new RegistrationHelper(fragment);
	    RegisterBean registerBean = helper.execute();
	    if (registerBean == null) {
		// Something went wrong in the request. Handle it
		handler.onError("Something went wrong", -1);
	    } else if (registerBean.isSuccess() && TextUtils.isEmpty(registerBean.getUserID())) {
		// Add user to a group
		AddUserToGroupHelper addToGroupHelper = new AddUserToGroupHelper(fragment.getActivity(),
			AppPropertiesUtil.getGroupId(fragment.getActivity()), AppPropertiesUtil.getUserID(fragment
				.getActivity()));
		if (addToGroupHelper.execute()) {
		    // Signin happened. Handle it
		    handler.onSuccess("");
		} else {
		    // Something went wrong in the request. Handle it
		    handler.onError("Something went wrong", -1);
		}
	    } else if (registerBean.isSuccess() && !TextUtils.isEmpty(registerBean.getUserID())) {
		// Add user to a group
		AddUserToGroupHelper addToGroupHelper = new AddUserToGroupHelper(fragment.getActivity(),
			AppPropertiesUtil.getGroupId(fragment.getActivity()), AppPropertiesUtil.getUserID(fragment
				.getActivity()));
		if (addToGroupHelper.execute()) {
		    AppPropertiesUtil.setUserAddedToGroup(fragment.getActivity(), true);
		    // Signup happened. Handle it
		    handler.onSuccess(registerBean.getUserID());
		} else {
		    // Something went wrong in the request. Handle it
		    handler.onError("Something went wrong", Constants.USER_ADDITON_TO_GROUP_FAILED_ERROR);
		}
	    }
	}
    }

}
