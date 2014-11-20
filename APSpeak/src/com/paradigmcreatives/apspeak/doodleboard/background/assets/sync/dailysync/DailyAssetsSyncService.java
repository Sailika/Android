package com.paradigmcreatives.apspeak.doodleboard.background.assets.sync.dailysync;

import android.app.IntentService;
import android.content.Intent;

import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.server.assets.AssetDownloader.AssetType;
import com.paradigmcreatives.apspeak.doodleboard.background.assets.sync.SyncAssetThread;
import com.paradigmcreatives.apspeak.doodleboard.background.assets.sync.SyncTSHelper;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Service to perform the assets sync in the background. The intent passed here is coming from a
 * <code>WakefulBroadcastReceiver</code> which ensures that the CPU is awake until and unless the service is not
 * finished
 * 
 * @author robin
 * 
 */
public class DailyAssetsSyncService extends IntentService {
    private static final String TAG = "DailyAssetsSyncService";

    public DailyAssetsSyncService() {
	super(TAG);

    }

    @Override
    protected void onHandleIntent(Intent arg0) {
	Logger.info(TAG, "Initiating sync of assets via DailySync");
	initiateSyncOfAllAssets();
    }

    /**
     * Iterates over all the <code>AssetType</code> and initiates the sync for all the assets
     */
    private void initiateSyncOfAllAssets() {
	SyncAssetThread syncAssetThread = null;
	Thread thread = null;
	try {
	    if (Util.isOnline(this)) {
		for (AssetType assetType : AssetType.values()) {
		    // Wait to get the instance of sync asset thread
		    if (SyncTSHelper.shouldSync(this, assetType)) {
			while ((syncAssetThread = SyncAssetThread.getInstance(this, assetType)) == null) {
			    Thread.sleep(1000);
			}
			thread = new Thread(syncAssetThread);
			thread.start();
		    } else {
			Logger.info(TAG, "No need to sync " + assetType + " as it was synced recently");
		    }
		}
	    }
	} catch (InterruptedException e) {
	    Logger.warn(TAG, "Thread sleep interrupted");
	} catch (Exception e) {
	    Logger.warn(TAG, "Something wrong happened while perform daily asset sync" + e.getMessage());
	}
    }

}
