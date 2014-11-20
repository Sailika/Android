package com.paradigmcreatives.apspeak.registration;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Patterns;

import com.paradigmcreatives.apspeak.app.model.Provider;
import com.paradigmcreatives.apspeak.app.model.Provider.PROVIDER_TYPE;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.ProvidersUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class contains util methods for registration
 * 
 * @author Vineela | Paradigm Creatives
 * 
 */
public class RegistrationUtil {

    private static final String TAG = "RegistrationUtil";

    private static final String PLATFORM = "platform";
    private static final String ACCOUNTS = "accounts";
    private static final String PUSH_ID = "push_id";
    private static final String USER_ID = "user_id";
    private static final String EMAIL_VALUE_KEY = "email";
    private static final String NUMBER_VALUE_KEY = "phone_number";
    private static final String COUNTRY_CODE_KEY = "country_code";
    private static final String PROVIDER_TYPE_KEY = "provider";

    private Context context;

    public RegistrationUtil(Context context) {
	super();
	this.context = context;
    }

    /**
     * Gets all the accounts logged in the device
     * 
     * @param context
     * @return
     */
    public synchronized List<Provider> getLoggedGmailIdsInDevice() {
	List<Provider> mEmailsList = null;
	new ArrayList<Provider>();
	if (context != null) {
	    mEmailsList = new ArrayList<Provider>();
	    try {
		if (mEmailsList != null) {
		    mEmailsList.clear();
		}

		Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
		Account[] accounts = AccountManager.get(context).getAccounts();
		for (Account account : accounts) {
		    if (emailPattern.matcher(account.name).matches()) {
			if (account.type.contains("android.mail") || account.type.contains("android.windowslive")
				|| account.type.contains("com.google")) {
			    String possibleEmail = account.name;
			    mEmailsList.add(new Provider(PROVIDER_TYPE.EMAIL, possibleEmail));

			}
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
		Logger.logStackTrace(e);
	    }
	} else {
	    Logger.warn(TAG, "Context in getRegisteredEmailIds() is null");
	}
	return mEmailsList;
    }

    /**
     * This method is to check if any logged in google accounts present in the device
     * 
     * @return
     */
    public synchronized boolean checkIfGoogleAccountISPresent() {
	boolean status = false;
	Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
	Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
	for (Account account : accounts) {
	    if (emailPattern.matcher(account.name).matches()) {
		status = true;
	    }
	}
	return status;
    }

    /**
     * Gets receiver json for registration
     * 
     * @param providersList
     * @param userId
     * @return
     */
    public synchronized JSONObject getRecieverJsonForRegistration(List<Provider> providersList, String userId) {
	JSONObject jObject = null;
	if (context != null) {
	    try {
		if (providersList != null && providersList.size() > 0) {
		    JSONArray jArray = (new ProvidersUtil(context)).getProvidersJsonArray(providersList,
			    EMAIL_VALUE_KEY, COUNTRY_CODE_KEY, NUMBER_VALUE_KEY, PROVIDER_TYPE_KEY);
		    if (jArray != null) {
			jObject = new JSONObject();
			String gcmId = AppPropertiesUtil.getGCMID(context);
			if (TextUtils.isEmpty(gcmId)) {
			    Logger.info(TAG, "Registering without GCM id");
			    gcmId = "NULL";
			}
			jObject.put(ACCOUNTS, jArray);
			jObject.put(PLATFORM, Constants.PLATFORM_VALUE);
			jObject.put(PUSH_ID, gcmId);
			if (!TextUtils.isEmpty(userId)) {
			    jObject.put(USER_ID, userId);
			}
		    } else {
			Logger.warn(TAG, "json array from provider list is null");
		    }
		} else {
		    Logger.warn(TAG, "Email list in getprovidersJsonArray() is null");
		}
	    } catch (JSONException e) {
		Logger.logStackTrace(e);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }
	} else {
	    Logger.warn(TAG, "Context in getprovidersJsonArray() is null");
	}
	return jObject;
    }

    /**
     * This method is used to show a dialog of verification status if registration is success, a method to open inbox
     * will be called
     * 
     * @param registrationActivity
     * @param isVerified
     */
    /*
    public static void showVerificationStatusDialog(final RegistrationActivity registrationActivity,
	    final boolean isVerified) {
	Util.logFlurryEvent(FlurryConstants.VERIFICATION_STATUS_DIALOG_SHOWN, FlurryConstants.EVENT_HANDLER, TAG);
	if (registrationActivity == null) {
	    Logger.warn(TAG, "Registration activity in show verification dialog is null");
	    return;
	}
	View verificationStatusView = (View) LayoutInflater.from(registrationActivity).inflate(
		R.layout.registration_status_dialog, null);
	final Dialog dialog = new Dialog(registrationActivity, android.R.style.Theme_Translucent_NoTitleBar);
	Button optionButton = (Button) verificationStatusView.findViewById(R.id.option_button);
	TextView verificationStatusText = (TextView) verificationStatusView.findViewById(R.id.registration_status);
	TextView verificationStatusTitle = (TextView) verificationStatusView
		.findViewById(R.id.registration_status_dialog_title);
	Typeface myTypefaceBold = Typeface.createFromAsset(registrationActivity.getAssets(), "Roboto-Bold.ttf");
	Typeface myTypeface = Typeface.createFromAsset(registrationActivity.getAssets(), "Roboto-Regular.ttf");

	verificationStatusTitle.setTypeface(myTypefaceBold);
	verificationStatusText.setTypeface(myTypeface);

	optionButton.setOnClickListener(new View.OnClickListener() {

	    public void onClick(View v) {
		dialog.dismiss();
		if (isVerified) {
		    Util.logFlurryEvent(FlurryConstants.CONTINUE_BUTTON_VERIFICATION_STATUS_DIALOG_CLICKED,
			    FlurryConstants.EVENT_HANDLER, TAG);
		    Intent intent = new Intent(registrationActivity, AssetsActivity.class);
		    intent.putExtra(Constants.ASSET_TYPE, AssetType.DOODLEWORLD.name());
		    registrationActivity.setResult(Activity.RESULT_OK);
		    registrationActivity.finish();
		} else {
		    Util.logFlurryEvent(FlurryConstants.RETRY_BUTTON_VERIFICATION_STATUS_DIALOG_CLICKED,
			    FlurryConstants.EVENT_HANDLER, TAG);
		}
	    }
	});

	if (isVerified) {
	    Util.logFlurryEvent(FlurryConstants.VERIFICATION_SUCCEES, FlurryConstants.EVENT_HANDLER, TAG);
	    verificationStatusText.setText(registrationActivity.getString(R.string.registration_success));
	    verificationStatusTitle.setText(registrationActivity.getString(R.string.yay));
	    optionButton.setText(registrationActivity.getString(R.string.continue_for));
	} else {
	    Logger.info(TAG, "Verification Failed");
	    Util.logFlurryEvent(FlurryConstants.VERIFICATION_FAILED, FlurryConstants.EVENT_HANDLER, TAG);
	    verificationStatusText.setText(registrationActivity.getString(R.string.verification_failed));

	    verificationStatusTitle.setText(registrationActivity.getString(R.string.oops_verification_failed));
	    optionButton.setText(registrationActivity.getResources().getString(R.string.ok));
	}
	dialog.setContentView(verificationStatusView);
	dialog.show();
    }
    */

    public String getDeviceMacId() {
	String macAddress = null;
	if (context != null) {
	    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	    WifiInfo wInfo = wifiManager.getConnectionInfo();
	    macAddress = wInfo.getMacAddress();
	} else {
	    Logger.warn(TAG, "context is null");
	}
	return macAddress;

    }

    /**
     * Generates device unique id with the combination of TelephonyManager deviceId and Settings.Secure ANDROID_ID Note
     * the point that, getDeviceId() API of TelephonyManager returns different values when SIM card is inserted and when
     * SIM card is NOT inserted.
     * 
     * @param context
     * @return String, generated device unique id, null otherwise
     */
    public static String generateDeviceUniqueId(Context context) {
	String deviceId = null;
	if (context != null) {
	    try {
		String tmDevice, androidId;
		final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		tmDevice = "" + tm.getDeviceId();
		androidId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

		UUID deviceUUID = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32));
		deviceId = deviceUUID.toString();
	    } catch (Exception e) {
		deviceId = null;
	    }
	}
	return deviceId;
    }

    /**
     * Returns the unique id given by Settings.Secure ANDROID_ID
     * 
     * @param context
     * @return String, device unique id, null otherwise
     */
    public static String getDeviceUniqueId(Context context) {
	String deviceId = null;
	if (context != null) {
	    try {
		deviceId = "" + Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
	    } catch (Exception e) {
		deviceId = null;
	    }
	}
	return deviceId;
    }
}
