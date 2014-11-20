package com.paradigmcreatives.apspeak.registration;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paradigmcreatives.apspeak.app.model.FacebookProfile;
import com.paradigmcreatives.apspeak.app.util.facebook.OpenGraphRequestUtil.OpenGraphRequestCallback;
import com.paradigmcreatives.apspeak.registration.handlers.SigninHandler;
import com.paradigmcreatives.apspeak.registration.tasks.RegistrationTasksHelper;
import com.paradigmcreatives.apspeak.registration.tasks.SigninThread;

public abstract class FragmentImplOpenGraphRequest extends Fragment implements OpenGraphRequestCallback {

    private ProgressDialog progressDialog = null;
    private SigninThread signinThread = null;
    private SigninHandler handler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	handler = new SigninHandler(this);
	signinThread = new SigninThread(getActivity(), handler);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	super.onCreateView(inflater, container, savedInstanceState);

	progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
	//progressDialog
	//	.setMessage("Give us a moment we're connecting to Facebook...");
	progressDialog
		.setMessage("Please wait for a moment...");
	progressDialog.setCancelable(false);
	return container;
    }

    @Override
    public void onBasicFacebookProfileFetchComplete(FacebookProfile profile) {
	if (getActivity() instanceof LoginActivity) {
	    LoginActivity activity = (LoginActivity) getActivity();
	    activity.setFacebookProfile(profile);

	    RegistrationTasksHelper helper = new RegistrationTasksHelper(getActivity().getApplicationContext());
	    // 1 - Try to persist all the available FB data in the DB. System
	    // can take care if the data is not
	    // persisted for some reason
	    helper.storeFacebookProfileData(profile);

	    // 2 - Start the signin thread
	    Thread t = new Thread(signinThread);
	    t.start();

	}

    }

    @Override
    public void moveToFragment(String tag, boolean resetBackStack, boolean addToBackStack) {
	if (!TextUtils.isEmpty(tag) && getActivity() != null && getActivity() instanceof LoginActivity) {
	    LoginActivity activity = (LoginActivity) getActivity();
	    /*
	    if (resetBackStack) {
		activity.resetStack();
	    }
	    */
	    activity.showFragment(tag, addToBackStack, resetBackStack);
	}
    }

 

    @Override
    public ProgressDialog getProgressDialog() {
	return progressDialog;
    }

}
