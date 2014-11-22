package com.paradigmcreatives.apspeak.registration;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.app.model.FacebookProfile;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;

public class LoginActivity extends FragmentActivity {

	private HashMap<String, Fragment> fragments = new HashMap<String, Fragment>();
	// private FacebookConnectFragment facebookConnectFragment;
	private FacebookConnectAnimationFragment facebookConnectFragment;
	private FacebookProfile facebookProfile = null;
	// Variables to handle fragment display when activity restore happens
	// private boolean mWaitForResult = false; // represents whether
	// onActivityResult get called or not
	private boolean mIsPostResumed = false; // represents whether activity's
											// onPostResume get called or not
	private boolean mFragmentRequested = false; // represents whether fragment
												// to show is requested or not
	private String mTagToUse;
	private boolean mResetBackStack;
	private boolean mAddToBackStack;

	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

		initFragments();
	}

	private void initFragments() {
		// Add the fragments to the stack
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();

		// Add Login With Facebook fragment
		// facebookConnectFragment = new FacebookConnectFragment();
		facebookConnectFragment = new FacebookConnectAnimationFragment();
		// Add User Handle Selection fragment
		// Add Friends Network Fragment
		// UserNetworkFragment friendsNetworkFragment = new
		// UserNetworkFragment(UserNetwork.FRIENDS);
		// Bundle bundlenewuser = new Bundle();
		// bundlenewuser.putString(Constants.NEW_USER_KEY, Constants.NEW_USER);
		// friendsNetworkFragment.setArguments(bundlenewuser);

		/*
		 * If user profile created/exists with Whatsay server then launch
		 * Friends Network screen else launch Connect via Facebook screen
		 */
		if (AppPropertiesUtil.isUserProfileComplete(this)) {
			// fragmentTransaction.add(android.R.id.content,
			// friendsNetworkFragment,
			// Constants.FRIENDS_NETWORK_FRAGMENT_TAG);
			launchAppNewHomeActivity();
		} else {
			fragmentTransaction.add(android.R.id.content,
					facebookConnectFragment,
					Constants.FACEBOOK_CONNECT_FRAGMENT_TAG);
			fragmentTransaction.commit();
		}

		fragments.put(Constants.FACEBOOK_CONNECT_FRAGMENT_TAG,
				facebookConnectFragment);
		// fragments.put(Constants.FRIENDS_NETWORK_FRAGMENT_TAG,
		// friendsNetworkFragment);
	}

	/**
	 * Shows the fragment with the given tag
	 * 
	 * @param tag
	 * @return <code>true</code> if the fragment exists and is shown else
	 *         returns <code>false</code>
	 */
	public boolean showFragment(String tag, boolean addToBackStack,
			boolean resetBackStack) {
		/*
		 * Known issue is that, sometimes show fragment request comes even
		 * before activity state is restored. We need to handle this particular
		 * case through variables such as mFragmentRequested, mWaitForResult. To
		 * handle a known issue of activity state being lost. Please refer to:
		 * http
		 * ://www.androiddesignpatterns.com/2013/08/fragment-transaction-commit
		 * -state-loss.html
		 * http://stackoverflow.com/questions/16265733/failure-delivering
		 * -result-onactivityforresult http://stackoverflow.
		 * com/questions/7469082
		 * /getting-exception-illegalstateexception-can-not-
		 * perform-this-action-after-onsa
		 */
		mFragmentRequested = true;
		mResetBackStack = resetBackStack;
		if (/* !mWaitForResult */mIsPostResumed) {
			if (resetBackStack) {
				resetStack();
			}
			// As this is not after onActivityResult, we are free to show
			// requested fragment
			FragmentManager fragmentManager = getSupportFragmentManager();
			Fragment fragment = fragments.get(tag);
			if (fragment != null) {
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction
						.replace(android.R.id.content, fragment, tag);
				if (addToBackStack) {
					fragmentTransaction.addToBackStack(null);
				}
				fragmentTransaction.commit();
				return true;
			} else {
				return false;
			}
		} else {
			// onActivityResult has got called but yet activity state is not
			// restored, hence save the values and show
			// the fragment onPostResume
			mTagToUse = tag;
			mAddToBackStack = addToBackStack;
			return true;
		}
	}

	/**
	 * Pops all the fragments from the stack and reset it to the very beginning
	 */
	public void resetStack() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		FragmentManager manager = getSupportFragmentManager();
		// mWaitForResult = true;
		if (facebookConnectFragment != null
				&& manager
						.findFragmentByTag(Constants.FACEBOOK_CONNECT_FRAGMENT_TAG) != null) {
			// TODO Verify the request code that its coming from FB only then
			// call the following line
			facebookConnectFragment.onActivityResult(requestCode, resultCode,
					data);
		}

	}

	public FacebookProfile getFacebookProfile() {
		return facebookProfile;
	}

	public void setFacebookProfile(FacebookProfile facebookProfile) {
		this.facebookProfile = facebookProfile;
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getApplicationContext(), Constants.FACEBOOK_APPID);
	}
	
	@Override
	protected void onPostResume() {
		super.onPostResume();
		mIsPostResumed = true;
		/*
		 * Show the fragment if the request is raised before activity restore
		 * happened
		 */
		if (/* mWaitForResult && */mFragmentRequested) {
			if (mResetBackStack) {
				resetStack();
				mResetBackStack = false;
			}
			// Show fragment now as activity state is restored by this point
			FragmentManager fragmentManager = getSupportFragmentManager();
			Fragment fragment = fragments.get(mTagToUse);
			if (fragment != null) {
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(android.R.id.content, fragment,
						mTagToUse);
				if (mAddToBackStack) {
					fragmentTransaction.addToBackStack(null);
				}
				fragmentTransaction.commit();
			}
			// Reset the values
			mFragmentRequested = false;
			// mWaitForResult = false;
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		/*
		 * As the activity is going away, reset the flag mIsPostResumed to
		 * false, so that any pending tasks in meantime will be handled once
		 * activity is resumed back
		 */
		mIsPostResumed = false;
	}

	/**
	 * Launches AppNewHomeActivity that contains palce holders for Cues
	 * Fragment, Profile Fragment and MyFeed Fragment
	 */
	public void launchAppNewHomeActivity() {
		Intent intent = new Intent(LoginActivity.this, AppNewHomeActivity.class);
		startActivity(intent);
		finish();
	}

	/*
	 * @Override public void onBackPressed() { if(mCurrentFragmentTag != null &&
	 * mCurrentFragmentTag.equals(Constants.FACEBOOK_CONNECT_FRAGMENT_TAG) &&
	 * facebookConnectFragment != null){
	 * if(facebookConnectFragment.handleBackPressed()){ // do nothing }else{
	 * super.onBackPressed(); } }else{ super.onBackPressed(); } }
	 */
}
