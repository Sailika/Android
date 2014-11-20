package com.paradigmcreatives.apspeak;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.widget.ImageView;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.dialogs.WhatsayDialogsUtil;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.gcm.GCMUtilClient;
import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.LoginActivity;

/**
 * Initial check for user registration while splashing the screen. If yes,
 * navigate to Stream, otherwise shows register via Facebook screen.
 * 
 * @author Dileep | neuv
 */
public class SplashScreenActivity extends Activity {

	private static final String TAG = "SplashScreenActivity";
	private Handler splashScreenHandler = null;
	private Runnable inboxScreenRunnable = null;

	// private ImageView imgView;
	// private AnimationDrawable frameAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hiding title bar from the current window.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// Setting the default layout
		setContentView(R.layout.splash);

		NetworkManager.getInstance();

		if (TextUtils.isEmpty(AppPropertiesUtil.getGCMID(this))) {
			GCMUtilClient.register(this);
		}

		// Initialize the components
		/*
		 * imgView = (ImageView) findViewById(R.id.animationImage);
		 * imgView.setVisibility(ImageView.VISIBLE);
		 * imgView.setBackgroundResource(R.drawable.frame_animation);
		 */
		// frameAnimation = (AnimationDrawable) imgView.getBackground();
		// toggleAnimation();
		boolean initialized = initAppComponents();

		if (initialized) {
			if (!AppPropertiesUtil.initProfilePic(this)) {
				Logger.warn(TAG, "Couldn't save profile pic");
			}
			// Launch next meaningful activity from here
			requestLaunchNextMeaningfulActivity();
		} else {
			WhatsayDialogsUtil.exitDialog(this, getString(R.string.exit_title))
					.show();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onBackPressed() {
		if ((splashScreenHandler != null) && (inboxScreenRunnable != null)) {
		if ((splashScreenHandler != null) && (inboxScreenRunnable != null))
		{
			splashScreenHandler.removeCallbacks(inboxScreenRunnable);
		}
		super.onBackPressed();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getApplicationContext(),
				Constants.FACEBOOK_APPID);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Initializes all the background processes and the APIs which requires the
	 * application context Components getting initialized
	 * <ul>
	 * <li>Logger API</li>
	 * <li>Exception Handler</li>
	 * <li>Pending Sync Feature</li>
	 * <li>Application Preferences</li>
	 * <li>Periodic pending doodle flushing</li>
	 * </ul>
	 * 
	 * @return <code>true</code> if initialization is successful else returns
	 *         <code>false</code>
	 */
	private boolean initAppComponents() {
		boolean initialized = true;
		Context context = getApplicationContext();

		// Initializing the logger, app properties
		initialized = AppPropertiesUtil.initAppDirectory(context)
				&& Logger.init(context) && AppPropertiesUtil.init(context);

		// Initializing the Exception Handler and setting it as the default for
		// all the uncaught exceptions
		String logDirectoryPath = AppPropertiesUtil.getAppDirectory(context)
				+ getResources().getString(R.string.log_folder);

		// ParadigmExceptionHandler paradigmException = new
		// ParadigmExceptionHandler(this, logDirectoryPath);
		// Thread.setDefaultUncaughtExceptionHandler(paradigmException);

		return initialized;
	}

	// private void toggleAnimation() {
	// if (frameAnimation.isRunning()) {
	// frameAnimation.stop();
	// } else {
	// frameAnimation.start();
	// }
	// }

	/**
	 * Decides which activity needs to be launched depends on user's phone
	 * number registration and profile creation statuses
	 */
	private void requestLaunchNextMeaningfulActivity() {

		Intent nextActivityIntent = null;

		// Successful initialization of all the components done!
		// Check if the user already registered via Facebook or not
		// Also check whether user profile created or not
		/**
		 * IF Profile Created THEN launch Home Screen with Navigation Drawer
		 * ELSE launch Profile Creation Screen
		 * 
		 */
		if (AppPropertiesUtil.isUserProfileComplete(this)) {
			// Profile already created, so, launch Default App Home screen
			// nextActivityIntent = new Intent(this, AppHomeActivity.class);
			nextActivityIntent = new Intent(this, AppNewHomeActivity.class);
		} else {
			// Profile not created, so, launch Connect Via Facebook screen
			nextActivityIntent = new Intent(this, LoginActivity.class);
		}

		delayedNextActivity(nextActivityIntent,
				Constants.SPLASH_SCREEN_DURATION);
	}

	/**
	 * Waits for the given time before starting the next activity.
	 * 
	 * @param nextActivityIntent
	 *            Intent for next activity.
	 * @param delayBy
	 *            Time is ms by which the start of next activity should be
	 *            delayed.
	 */
	private void delayedNextActivity(final Intent nextActivityIntent,
			long delayBy) {
		// Keep the splash screen for few seconds and then move to the next
		// activity by killing the existing activity
		splashScreenHandler = new Handler();
		inboxScreenRunnable = new Runnable() {
			@Override
			public void run() {
				// toggleAnimation();
				finish();
				try {
					SplashScreenActivity.this.startActivity(nextActivityIntent);
				} catch (ActivityNotFoundException anfe) {
					Logger.warn(
							TAG,
							"Activity not found : "
									+ anfe.getLocalizedMessage());
				} catch (Exception e) {
					Logger.warn(TAG,
							"Unknown Exception : " + e.getLocalizedMessage());
				}
			}
		};
		splashScreenHandler.postDelayed(inboxScreenRunnable,
				Constants.SPLASH_SCREEN_DURATION);
	}

}