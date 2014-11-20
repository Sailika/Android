package com.paradigmcreatives.apspeak.app.util.google.image;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONObject;

import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.CursorBean;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.PagesBean;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.SearchResultsBean;
import com.paradigmcreatives.apspeak.logging.Logger;

public class GoogleImageSearchHelper {

    private static final String TAG = "GoogleImageSearchHelper";

    /**
     * Search string
     */
    private String searchString;

    /*
     * Google parameter to be sent in the request. Determines the index of the page to get the results from
     */
    private int currentStart = 0;

    /**
     * Results per search
     */
    private int rsz = 8;

    /**
     * IP address of the device
     */
    private String userIP;

    /**
     * Is the class initialized or not
     */
    private boolean initialized = false;

    /**
     * Constructor
     * 
     * @param searchString
     */
    public GoogleImageSearchHelper(String searchString) {
	this.searchString = searchString;
    }

    /**
     * The instance should be initialized first before making ant calls. At the moment initialization only contains
     * getting of the IP address of the device
     */
    public void init() {
	userIP = DeviceInfoUtil.getIPAddress(true);
	if (!TextUtils.isEmpty(userIP)) {
	    initialized = true;
	}
    }

    /**
     * Performs the image search by executing the google search query. Subsequent calls to this method would return next
     * set of results
     * 
     * @return
     */
    public synchronized SearchResultsBean next() {
	SearchResultsBean result = null;

	if (initialized) {
	    // 1 - Perform the image search query
	    Query query = new Query(searchString, currentStart, rsz, userIP);
	    JSONObject jsonResult = query.execute();

	    ResultParser rp = new ResultParser(jsonResult);
	    result = rp.parse();

	    updateStartIndex(result, true);
	}

	return result;
    }

    public synchronized SearchResultsBean previous() {
	SearchResultsBean result = null;

	if (initialized) {
	    // 1 - Perform the image search query
	    int startIndex = 0;
	    if (currentStart - 2 * rsz > 0) {
		startIndex = currentStart - 2 * rsz;
	    }
	    if (startIndex == currentStart - rsz) {
		Logger.warn(TAG, "No query required");
		return result;
	    }
	    Query query = new Query(searchString, startIndex, rsz, userIP);
	    JSONObject jsonResult = query.execute();

	    ResultParser rp = new ResultParser(jsonResult);
	    result = rp.parse();

	    updateStartIndex(result, false);
	}

	return result;
    }

    private void updateStartIndex(SearchResultsBean searchResultBean, boolean didMoveToNext) {
	if (searchResultBean != null) {
	    CursorBean cursorBean = searchResultBean.getCursor();
	    if (cursorBean != null) {
		ArrayList<PagesBean> pages = cursorBean.getPages();
		if (pages != null) {
		    if (didMoveToNext) {
			currentStart = getNextStart(pages);
		    } else {
			currentStart = getPreviousStart(pages);
		    }
		}
	    }
	}
    }

    private int getNextStart(ArrayList<PagesBean> pages) {
	int nextStart = currentStart;
	boolean pickNextStart = false;
	Iterator<PagesBean> iterator = pages.iterator();
	PagesBean item;
	while (iterator.hasNext()) {
	    item = iterator.next();

	    if (pickNextStart) {
		nextStart = item.getStart();
		break;
	    }

	    if (item.getStart() == currentStart) {
		pickNextStart = true;
	    }
	}
	Logger.info(TAG, "start index : " + nextStart);

	// If control has reached here it means there are no more results available
	return nextStart;
    }

    private int getPreviousStart(ArrayList<PagesBean> pages) {
	int prevStart = currentStart;
	Iterator<PagesBean> iterator = pages.iterator();
	PagesBean item;
	while (iterator.hasNext()) {
	    item = iterator.next();
	    if (item.getStart() != currentStart) {
		prevStart = item.getStart();
	    } else {
		break;
	    }
	}
	Logger.info(TAG, "start index : " + prevStart);

	// If control has reached here it means there are no more results available
	return prevStart;
    }

}
