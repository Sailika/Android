package com.paradigmcreatives.apspeak.app.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera.CameraInfo;
import android.text.TextUtils;
import android.util.Log;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.database.SettingsDBUtil;
import com.paradigmcreatives.apspeak.app.database.settingsdb.SettingsDAO;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Utility class for maintaining the application preferences
 * 
 * @author robin
 * 
 */
public class AppPropertiesUtil {

	private static final String TAG = "AppPropertiesUtil";

	/**
	 * Initializes the application preferences
	 * 
	 * 
	 * @param context
	 *            Context of the application
	 * @return <code>true</code> if initialization is successful, else returns
	 *         <code>false</code>
	 * 
	 */
	public static boolean init(Context context) {
		if (context == null) {
			return false;
		}

		boolean initialized = true;

		SettingsDAO settingsDAO = new SettingsDAO(context);
		SettingsDBUtil settingsDBUtil = new SettingsDBUtil(context);
		// update the DB

		// Check for the existence of the required keys, put them if they don't
		// exist
		if (settingsDBUtil != null && settingsDAO != null) {
		} else {
			Logger.warn(TAG, "Settings db reference objects are null");
		}

		if (Constants.DEBUG) {
			Log.v(TAG, "Initialization of app preferences - " + initialized);
		}

		return initialized;
	}

	/**
	 * Gets the current app version
	 * 
	 * @param context
	 * @return
	 */
	public static int getCurrentAppVersion(Context context) {
		int version = 0;
		if (context == null) {
			return version;
		}
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			version = pInfo.versionCode;
		} catch (NameNotFoundException nnfe) {
			Logger.warn(TAG, nnfe.getLocalizedMessage());
		} catch (Exception e) {
			Logger.warn(TAG, e.getLocalizedMessage());
		}
		return version;
	}

	/**
	 * Gets the persisted app version
	 * 
	 * @param context
	 * @return
	 */
	public static int getPersistedAppVersion(Context context) {
		int version = 0;
		if (context == null) {
			return version;
		}
		SharedPreferences appPref = context.getSharedPreferences(
				Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		version = appPref.getInt(Constants.KEY_APP_VERSION, 0);
		return version;
	}

	/**
	 * Created the application directory on the SD card if it does not exists
	 * already
	 * 
	 * @param context
	 *            Application's context
	 * @return <code>true</code> if directory created successfully or it already
	 *         exists, otherwise <code>false</code>
	 */
	public static boolean initAppDirectory(Context context) {
		if (context == null) {
			return false;
		}

		String sdCardPath = Util.getSdCardPath();
		if (sdCardPath != null && DeviceInfoUtil.mediaWritable()) {
			File appDir = new File(Util.getSdCardPath()
					+ context.getResources().getString(R.string.app_name));
			boolean temp = appDir.exists();
			temp = appDir.mkdirs();
			return appDir.exists() || appDir.mkdirs();
		} else {
			return false;
		}
	}

	/**
	 * Create the profile picture and save it to memory
	 * 
	 * @param context
	 *            Application's context
	 * @return <code>true</code> if profile picture successfully created or
	 *         exists
	 */
	public static boolean initProfilePic(Context context) {
		if (context == null) {
			return false;
		}

		String appDirectory = getAppDirectory(context);
		if (appDirectory != null && DeviceInfoUtil.mediaWritable()) {
			File profilePicFile = new File(appDirectory
					+ context.getResources().getString(
							R.string.profile_pic_folder), context
					.getResources().getString(
							R.string.profile_picture_image_name));
			if (!profilePicFile.exists()) {
				File profilePicFolder = new File(appDirectory
						+ context.getResources().getString(
								R.string.profile_pic_folder));
				if (!profilePicFolder.exists()) {
					if (!profilePicFolder.mkdirs()) {
						return false;
					}
				}
				Bitmap profilePic = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.yay);
				return ImageUtil.saveProfileImage(context, profilePic);
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * Returns the application directory
	 * 
	 * @param context
	 * @return Application directory, <code>null</code> if not available
	 */
	public static String getAppDirectory(Context context) {
		if (context == null) {
			return null;
		}

		String sdCardPath = Util.getSdCardPath();
		if (sdCardPath != null) {
			return sdCardPath
					+ context.getResources().getString(R.string.app_name)
					+ File.separator;
		} else {
			return null;
		}

	}

	/**
	 * This method returns the gcmID present in the shared preferences
	 * 
	 * @param context
	 *            : Context
	 * @return gcmID: GCM ID saved in the shared preferences, which can be null
	 */
	public static String getGCMID(Context context) {

		SharedPreferences appPref = context.getSharedPreferences(
				Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

		String gcmID = appPref.getString(Constants.KEY_GCM_ID, null);

		return gcmID;

	}

	/**
	 * This method returns the userID present in the shared preferences
	 * 
	 * @param context
	 *            : Context
	 * @return userID: user ID saved in the shared preferences, which can be
	 *         null
	 */
	public static String getUserID(Context context) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

			String userId = appPref.getString(Constants.KEY_USER_ID, null);
			return userId;
		} else {
			return null;
		}

	}

	/**
	 * Checks whether user profile is complete or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isUserProfileComplete(Context context) {
		boolean isComplete = false;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			isComplete = appPref.getBoolean(Constants.IS_USER_PROFILE_COMPLETE,
					false);
		}
		return isComplete;
	}

	/**
	 * Checks whether facebook profile captured or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFacebookProfileCaptured(Context context) {
		boolean isCaptured = false;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			isCaptured = appPref.getBoolean(
					Constants.IS_FACEBOOK_PROFILE_CAPTURED, false);
		}
		return isCaptured;
	}

	/**
	 * Sets the user profile completion status in Shared Preferences
	 * 
	 * @param context
	 * @param isComplete
	 */
	public static void setUserProfileComplete(Context context,
			boolean isComplete) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putBoolean(Constants.IS_USER_PROFILE_COMPLETE,
						isComplete);
				editor.commit();
			}
		}
	}

	/**
	 * Sets the facebook profile captured status in Shared Preferences
	 * 
	 * @param context
	 * @param isComplete
	 */
	public static void setFacebookProfileCapture(Context context,
			boolean isCaptured) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putBoolean(Constants.IS_FACEBOOK_PROFILE_CAPTURED,
						isCaptured);
				editor.commit();
			}
		}
	}

	/**
	 * Gets user's unique handle
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserUniqueHandle(Context context) {
		String uniqueHandle = null;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			uniqueHandle = appPref.getString(
					Constants.KEY_USER_PROFILE_UNIQUEHANDLE, null);
		}
		return uniqueHandle;
	}

	/**
	 * Sets the selected user's unique handle in Shared Preferences
	 * 
	 * @param context
	 * @param uniqueHandle
	 */
	public static void setSelectedUniqueHandle(Context context,
			String uniqueHandle) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putString(Constants.KEY_USER_PROFILE_UNIQUEHANDLE,
						uniqueHandle);
				editor.commit();
			}
		}
	}

	/**
	 * Sets the doodly doo userId in Shared Preferences
	 * 
	 * @param context
	 * @param userId
	 */
	public static void setUserID(Context context, String userID) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putString(Constants.KEY_USER_ID, userID);
				editor.commit();
			}
		}
	}

	/**
	 * Sets the doodly doo server's Session Id
	 * 
	 * @param context
	 * @param sessionId
	 */
	public static void setSessionID(Context context, String sessionID) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putString(Constants.KEY_SESSION_ID, sessionID);
				editor.commit();
			}
		}
	}

	/**
	 * Gets Server's session id
	 * 
	 * @param context
	 * @return
	 */
	public static String getSessionId(Context context) {
		String sessionId = null;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			sessionId = appPref.getString(Constants.KEY_SESSION_ID, null);
		}
		return sessionId;
	}

	/**
	 * Sets the user's group id
	 * 
	 * @param context
	 * @param groupId
	 */
	public static void setGroupID(Context context, String groupID) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putString(Constants.KEY_GROUP_ID, groupID);
				editor.commit();
			}
		}
	}

	/**
	 * Gets user's group id
	 * 
	 * @param context
	 * @return
	 */
	public static String getGroupId(Context context) {
		String groupId = null;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			groupId = appPref.getString(Constants.KEY_GROUP_ID, null);
		}
		return groupId;
	}

	/**
	 * Sets the user added to group flag
	 * 
	 * @param context
	 * @param groupId
	 */
	public static void setUserAddedToGroup(Context context, boolean flag) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putBoolean(Constants.KEY_USER_ADDED_TO_GROUP_FLAG, flag);
				editor.commit();
			}
		}
	}

	public static void setCameraFlashMode(Context context, int value) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putInt(Constants.KEY_CAMERA_FLASH_MODE, value);
				editor.commit();
			}
		}
	}

	public static int getCameraFlashMode(Context context) {
		int value = Constants.FLASH_MODE_AUTOMATIC;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			value = appPref.getInt(Constants.KEY_CAMERA_FLASH_MODE, value);
		}
		return value;
	}

	public static void setCameraFacing(Context context, int value) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putInt(Constants.KEY_CAMERA_FACING, value);
				editor.commit();
			}
		}
	}

	public static int getCameraFacing(Context context) {
		int value = CameraInfo.CAMERA_FACING_BACK;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			value = appPref.getInt(Constants.KEY_CAMERA_FACING, value);
		}
		return value;
	}

	/**
	 * Gets user added to group flag
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getUserAddedToGroupFlag(Context context) {
		boolean isUserAddedToGroup = false;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			isUserAddedToGroup = appPref.getBoolean(
					Constants.KEY_USER_ADDED_TO_GROUP_FLAG, false);
		}
		return isUserAddedToGroup;
	}

	/**
	 * Sets the user's name to shared preferences
	 * 
	 * @param context
	 * @param groupId
	 */
	public static void setUserName(Context context, String name) {
		if (context != null && !TextUtils.isEmpty(name)) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putString(Constants.KEY_USER_PROFILE_NAME, name);
				editor.commit();
			}
		}
	}

	/**
	 * Gets user's name
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserName(Context context) {
		String name = null;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			name = appPref.getString(Constants.KEY_USER_PROFILE_NAME, null);
		}
		return name;
	}

	/**
	 * Sets the user's group name to shared preferences
	 * 
	 * @param context
	 * @param groupName
	 */
	public static void setUserGroupName(Context context, String name) {
		if (context != null && !TextUtils.isEmpty(name)) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putString(Constants.KEY_GROUP_NAME, name);
				editor.commit();
			}
		}
	}

	/**
	 * Gets user's group name
	 * 
	 * @param context
	 * @return
	 */
	public static String getUserGroupName(Context context) {
		String name = null;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			name = appPref.getString(Constants.KEY_GROUP_NAME, null);
		}
		return name;
	}

	/**
	 * Sets the flag to decide whether to show/hide Featured Stream
	 * 
	 * @param context
	 * @param featuredFlag
	 */
	public static void setFeaturedFlag(Context context, boolean flag) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putBoolean(Constants.KEY_FEATURED_FLAG, flag);
				editor.commit();
			}
		}
	}

	/**
	 * Gets Featured enable/disable flag value
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getFeaturedFlag(Context context) {
		boolean flag = false;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			flag = appPref.getBoolean(Constants.KEY_FEATURED_FLAG, false);
		}
		return flag;
	}

	/**
	 * Sets the notifications count that needs to be displayed on My Feed icon
	 * 
	 * @param context
	 * @param notificationsCount
	 */
	public static void setNotificationsCount(Context context,
			int notificationsCount) {
		if (context != null && notificationsCount >= 0) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putInt(Constants.KEY_NOTIFICATIONS_COUNT,
						notificationsCount);
				editor.commit();
			}
		}
	}

	/**
	 * Gets Notifications Count value
	 * 
	 * @param context
	 * @return
	 */
	public static int getNotificationsCount(Context context) {
		int notificationsCount = 0;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			notificationsCount = appPref.getInt(
					Constants.KEY_NOTIFICATIONS_COUNT, 0);
		}
		return notificationsCount;
	}

	/**
	 * Sets the notification id that is lastly assigned/used to latest
	 * Notification.
	 * 
	 * @param context
	 * @param notificationsCount
	 */
	public static synchronized void setLastUsedNotificationId(Context context,
			int notificationId) {
		if (context != null && notificationId >= 2) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putInt(Constants.KEY_NOTIFICATION_ID, notificationId);
				editor.commit();
			}
		}
	}

	/**
	 * Gets lastly assigned/used notification id. Caller has to increment the
	 * returned value, assign it to the new/latest notification and store the
	 * incremented value in shared preferences by calling the function
	 * setLastUsedNotificationId()
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized int getLastUsedNotificationId(Context context) {
		/*
		 * Notification id 1 is assigned for WHATSAY_ASSET_LOVED. Hence if there
		 * is no key present in shared preferences then return the value 1, so
		 * that caller will increment, assign the value to new/latest
		 * notification and stores the incremented value in shared preferences
		 * by calling the function setLastUsedNotificationId()
		 */
		int notificationId = 1;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			notificationId = appPref.getInt(Constants.KEY_NOTIFICATION_ID, 1);
		}
		return notificationId;
	}

	public static boolean setAppLaunchedBeforeToTrue(Context context) {
		SharedPreferences appPref = context.getSharedPreferences(
				Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = appPref.edit();
		editor.putBoolean(Constants.APP_IS_LAUNCHED, true);
		return editor.commit();
	}

	public static Boolean isWelcomeScreenShownBefore(Context context) {

		SharedPreferences appPref = context.getSharedPreferences(
				Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

		boolean isAppLaunchedBefore = appPref.getBoolean(
				Constants.IS_WELCOME_SCREEN_DISPLAYED, false);

		return isAppLaunchedBefore;

	}

	public static boolean setWelcomeScreenShownBeforeToTrue(Context context) {
		SharedPreferences appPref = context.getSharedPreferences(
				Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = appPref.edit();
		editor.putBoolean(Constants.IS_WELCOME_SCREEN_DISPLAYED, true);
		return editor.commit();
	}

	/**
	 * Sets the user's unique handle creation status in Shared Preferences
	 * 
	 * @param context
	 * @param isCreated
	 */
	public static void setUniqueHandleCreatedStatus(Context context,
			boolean isCreated) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putBoolean(Constants.IS_UNIQUE_HANDLE_CREATED, isCreated);
				editor.commit();
			}
		}
	}

	/**
	 * Sets the selected user's unique handle password in Shared Preferences
	 * 
	 * @param context
	 * @param uniqueHandle
	 */
	public static void setSelectedUniqueHandlePassword(Context context,
			String password) {
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = appPref.edit();
			if (editor != null) {
				editor.putString(Constants.KEY_USER_PROFILE_PASSWORD, password);
				editor.commit();
			}
		}
	}

	/**
	 * Returns whether asset details help overlay has been shown or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getTextHelpOverlayStatus(Context context) {
		boolean isShown = false;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			isShown = appPref.getBoolean(Constants.KEY_TEXT_HELPOVERLAY, false);
		}
		return isShown;
	}

	/**
	 * Sets the flag that represents whether asset details help overlay has been
	 * shown or not
	 * 
	 * @param context
	 * @param isShown
	 * @return
	 */
	public static boolean setTextHelpOverlayStatus(Context context,
			boolean isShown) {
		SharedPreferences appPref = context.getSharedPreferences(
				Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = appPref.edit();
		editor.putBoolean(Constants.KEY_TEXT_HELPOVERLAY, isShown);
		return editor.commit();
	}

	/**
	 * Returns whether asset details help overlay has been shown or not
	 * 
	 * @param context
	 * @return
	 */
	public static boolean getAssetDetailsHelpOverlayStatus(Context context) {
		boolean isShown = false;
		if (context != null) {
			SharedPreferences appPref = context.getSharedPreferences(
					Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
			isShown = appPref.getBoolean(
					Constants.KEY_ASSETDETAILS_HELPOVERLAY, false);
		}
		return isShown;
	}

	/**
	 * Sets the flag that represents whether asset details help overlay has been
	 * shown or not
	 * 
	 * @param context
	 * @param isShown
	 * @return
	 */
	public static boolean setAssetDetailsHelpOverlayStatus(Context context,
			boolean isShown) {
		SharedPreferences appPref = context.getSharedPreferences(
				Constants.KEY_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = appPref.edit();
		editor.putBoolean(Constants.KEY_ASSETDETAILS_HELPOVERLAY, isShown);
		return editor.commit();
	}

}
