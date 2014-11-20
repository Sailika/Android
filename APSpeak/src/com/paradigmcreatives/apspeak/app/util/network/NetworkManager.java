package com.paradigmcreatives.apspeak.app.util.network;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import com.paradigmcreatives.apspeak.logging.Logger;

import android.util.Log;

/**
 * Network Manager class is a utility which registers list of background web services which can be aborted at a later
 * stage if a network change is encountered.
 * 
 * @author Vishal | Paradigm Creatives
 */
public class NetworkManager {
    private ArrayList<Object> listOfServices = new ArrayList<Object>();

    private static String TAG = "NetworkManager";
    private static NetworkManager networkManager;

    /**
     * Creates a <code>NetworkManager</code> instance.
     * 
     * @return <code>NetworkManager</code> instance
     */
    public static NetworkManager getInstance() {
	if (networkManager == null) {
	    networkManager = new NetworkManager();
	    Logger.info(TAG, "Network Manager initialized.");
	}

	return networkManager;
    }

    /**
     * Aborts all the services registered to it when there is a network change.
     */
    public void abortAll() {
	if (!listOfServices.isEmpty()) {
	    try {
		for (Object webObject : listOfServices) {
		    abort(webObject);
		}
	    } catch (Exception e) {
		Logger.fatal(e.getMessage());
	    }
	} else {
	    Log.e(TAG, "Nothing to abort!!!");
	}
    }// end of abortAll()

    /**
     * Registers a web request in abortable list.
     * 
     * @param webServiceObject
     * @return boolean <code>true if web service is registered,
     * else <code>false</code>.
     */
    public boolean register(Object webServiceObject) {
	boolean registered = false;
	if ((webServiceObject instanceof HttpPost) || (webServiceObject instanceof HttpPut)
		|| (webServiceObject instanceof HttpGet) || (webServiceObject instanceof HttpDelete)
		|| (webServiceObject instanceof HttpURLConnection)) {
	    listOfServices.add(webServiceObject);
	    registered = true;
	} else {
	    Log.e(TAG, "Only HttpPost , HttpPut, HttpDelete, HttpURLConnection and HttpGet is accepted. "
		    + "Given object is " + webServiceObject.getClass());
	}

	return registered;
    }// end of register()

    /**
     * UnRegisters a web service from abortable list of services.
     * 
     * @param webServiceObject
     *            A web service.
     * @return <code>true</code> if list is Unregistered, else <code>false</code>.
     */
    public boolean unRegister(Object webServiceObject) {
	boolean deRegistered = false;

	if (listOfServices.contains(webServiceObject)) {
	    listOfServices.remove(webServiceObject);
	    deRegistered = true;
	    Log.i(TAG, "Deregistered successfully !!! ");
	} else {
	    Log.e(TAG, "Not in list, register first !!! ");
	}

	return deRegistered;
    }// end of deRegister()

    /**
     * Attempts to abort a service.
     * 
     * @param webObject
     *            a web service object to be aborted.
     */
    public void abort(Object webObject) {
	try {
	    if (webObject instanceof HttpPost) {
		((HttpPost) webObject).abort();
		unRegister(webObject);
	    } else if (webObject instanceof HttpPut) {
		((HttpPut) webObject).abort();
		unRegister(webObject);
	    } else if (webObject instanceof HttpGet) {
		((HttpGet) webObject).abort();
		unRegister(webObject);
	    } else if (webObject instanceof HttpDelete) {
		((HttpDelete) webObject).abort();
		unRegister(webObject);
	    } else if (webObject instanceof HttpURLConnection) {
		System.out.println("DOODLEWORLD: disconnecting urlconnection in NetworkManager");
		((HttpURLConnection) webObject).disconnect();
	    } else {
		Log.e(TAG, "Given Web Service Object is not abortable");
	    }
	} catch (Exception e) {
	    Logger.logStackTrace(e);
	}
    }// end of abort()

    /**
     * Attempts to abort a list of services.
     * 
     * @param abortableTaskList
     *            <code>Collection</code> of abortable services.
     */
    public void abortList(ArrayList<Object> abortableTaskList) {
	System.out.println("DOODLEWORLD: abortList");
	if (abortableTaskList == null) {
	    return;
	}
	for (Object abortObject : abortableTaskList) {
	    if (listOfServices.contains(abortObject)) {
		System.out.println("DOODLEWORLD: connection object matched in NetworkManager:" + abortObject.hashCode());
		abort(abortObject);
	    } else {
		Log.e(TAG, "Given Web Service Object is not in list... please first register");
	    }
	}
    }// end of abortList()

    /**
     * Gets the list of services registered with <code>NetworkManager</code>.
     * 
     * @return number of registered services.
     */
    public int getRegisteredCount() {
	if (listOfServices != null) {
	    return listOfServices.size();
	}

	return 0;
    }// end of getRegisteredCount()

}// end of class