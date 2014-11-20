package com.paradigmcreatives.apspeak.app.util;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.paradigmcreatives.apspeak.app.model.Provider;
import com.paradigmcreatives.apspeak.app.model.Provider.PROVIDER_TYPE;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Util class to deal with <code>Provider</code> objects
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class ProvidersUtil {

    private static final String TAG = "ProvidersUtil";

    private static final String PHONE = "PHONE";
    private static final String EMAIL = "EMAIL";

    private Context context;

    public ProvidersUtil(Context context) {
	super();
	this.context = context;
    }

    /**
     * Generates a json array out of the provider list
     * 
     * @param providersList
     * @param emailKey
     * @param countryCodeKey
     * @param numberKey
     * @param providerTypeKey
     * @return
     */
    public synchronized JSONArray getProvidersJsonArray(List<Provider> providersList, String emailKey,
	    String countryCodeKey, String numberKey, String providerTypeKey) {
	JSONArray jArray = null;
	if (context != null) {
	    try {
		if (providersList != null && providersList.size() > 0) {
		    jArray = new JSONArray();
		    for (int i = 0; i < providersList.size(); i++) {
			Provider provider = providersList.get(i);
			if (provider != null) {
			    JSONObject providerJSON = new JSONObject();

			    if (providersList.get(i).getType().equals(PROVIDER_TYPE.EMAIL)) {
				providerJSON.put(providerTypeKey, EMAIL);
				providerJSON.put(emailKey, provider.getProviderValue1());
			    } else if (providersList.get(i).getType().equals(PROVIDER_TYPE.PHONE)) {
				providerJSON.put(providerTypeKey, PHONE);
				providerJSON.put(countryCodeKey, provider.getProviderValue2());
				providerJSON.put(numberKey, provider.getProviderValue1());
			    }

			    jArray.put(providerJSON);
			} else {
			    Logger.warn(TAG, "Provider is null at index : " + i);
			}
		    }
		} else {
		    Logger.warn(TAG, "Email list in getprovidersJsonArray() is null");
		}
	    } catch (JSONException e) {
		Logger.logStackTrace(e);
	    } catch (ArrayIndexOutOfBoundsException e) {
		Logger.logStackTrace(e);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }
	} else {
	    Logger.warn(TAG, "Context in getprovidersJsonArray() is null");
	}
	return jArray;
    }

    public synchronized JSONObject getProviderJsonObject(Provider provider, String providerTypeKey, String emailKey,
	    String countryCodeKey, String numberKey) {

	JSONObject providerJSON = new JSONObject();
	if (context != null) {
	    try {
		if (provider.getType().equals(PROVIDER_TYPE.EMAIL)) {
		    providerJSON.put(providerTypeKey, EMAIL);
		    providerJSON.put(emailKey, provider.getProviderValue1());
		} else if (provider.getType().equals(PROVIDER_TYPE.PHONE)) {
		    providerJSON.put(providerTypeKey, PHONE);
		    providerJSON.put(countryCodeKey, provider.getProviderValue2());
		    providerJSON.put(numberKey, provider.getProviderValue1());
		}
	    } catch (JSONException e) {
		Logger.logStackTrace(e);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }
	} else {
	    Logger.warn(TAG, "Context in getprovidersJsonArray() is null");
	}
	return providerJSON;
    }
}
