package com.paradigmcreatives.apspeak.app.util.google.image;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.paradigmcreatives.apspeak.app.util.constants.ServerConstants;
import com.paradigmcreatives.apspeak.app.util.network.NetworkManager;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class contains convenience methods for executing google search queries
 * 
 * @author robin
 * 
 */
public class Query {

    /**
     * Google image search base URL
     */
    private static final String GOOGLE_IMAGE_SEARCH_URL = "https://ajax.googleapis.com/ajax/services/search/images";

    /**
     * Version of the google search URL to be used
     */
    private static final String VERSION = "1.0";

    /**
     * Minimum permissible value of 'start' param
     */
    private static final int MIN_START = 0;

    /**
     * Maximum permissible value of 'start' param
     */
    private static final int MAX_START = 56;

    /**
     * Minimum permissible value of results per search
     */
    private static final int MIN_RSZ = 1;

    /**
     * Maximum permissible value of results per page
     */
    private static final int MAX_RSZ = 8;

    /**
     * TAG for Android log
     */
    private static final String TAG = "Query";

    /**
     * URL of the application web-site
     */
    private static final String SITE_URL = "www.doodlydoo.mobi";

    /**
     * Search string field
     */
    private String searchString;

    /**
     * Value of 'start' param
     */
    private int start = 0;

    /**
     * Value of result per page param
     */
    private int rsz = 8;

    /**
     * IP address of the device requesting the search
     */
    private String userIP;

    /**
     * @param searchString
     * @param start
     * @param rsz
     */
    public Query(String searchString, int start, int rsz, String userIP) {
	this.searchString = searchString;
	this.start = start;
	this.rsz = rsz;
	this.userIP = userIP;
    }

    public JSONObject execute() {
	JSONObject result = null;

	// Validate the IP address
	IPAddressValidator ipav = new IPAddressValidator();
	if (!ipav.validate(userIP)) {
	    // If its not a valid IP then hard-code a dummy IP
	    userIP = "192.168.1.1";
	}

	if (!TextUtils.isEmpty(searchString) && start >= MIN_START && start <= MAX_START && rsz >= MIN_RSZ
		&& rsz <= MAX_RSZ) {
	    InputStreamReader ir = null;
	    BufferedReader reader = null;
	    URLConnection connection = null;
	    try {
		URL url = new URL(GOOGLE_IMAGE_SEARCH_URL + "?v=" + VERSION + "&q="
			+ URLEncoder.encode(searchString, "ISO-8859-1") + "&start=" + start + "&rsz=" + rsz + "&userip"
			+ userIP);
		connection = url.openConnection();
		NetworkManager.getInstance().register(connection);
		connection.setConnectTimeout(ServerConstants.CONNECTION_TIMEOUT);
		connection.addRequestProperty("Referer", SITE_URL);

		String line;
		StringBuilder builder = new StringBuilder();
		ir = new InputStreamReader(connection.getInputStream());
		reader = new BufferedReader(ir);
		while ((line = reader.readLine()) != null) {
		    builder.append(line);
		}

		if (builder.length() > 0) {
		    result = new JSONObject(builder.toString());
		}
	    } catch (MalformedURLException e) {
		Log.w(TAG, "Wrong URL used - " + e.getLocalizedMessage());
		e.printStackTrace();
	    } catch (IOException e) {
		Log.w(TAG, "IO Exception while opening the connection - " + e.getLocalizedMessage());
		e.printStackTrace();
	    } catch (JSONException e) {
		Log.w(TAG, "Error while parsing the result from google " + e.getLocalizedMessage());
		e.printStackTrace();
	    } catch (Exception e) {
		Log.w(TAG, "Something bad happened " + e.getLocalizedMessage());
		e.printStackTrace();
	    } finally {
		try {
		    if (ir != null) {
			ir.close();
		    }
		    if (reader != null) {
			reader.close();
		    }
		    if (connection != null) {
			NetworkManager.getInstance().unRegister(connection);
		    }
		} catch (IOException ioe) {
		    Logger.warn(TAG, ioe.getLocalizedMessage());
		} catch (Exception e) {
		    Logger.warn(TAG, e.getLocalizedMessage());
		}
	    }
	}

	return result;

    }
}
