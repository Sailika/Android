package com.paradigmcreatives.apspeak.assets.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.assets.handlers.WhatsayAssetDownloadHandler;

public class WhatsayAssetDownloadThread extends Thread{
    
    private Context mContext;
    private String mAssetDownloadURL;
    private String mAssetDownloadPath;
    private WhatsayAssetDownloadHandler mHandler;
    
    public WhatsayAssetDownloadThread(final Context context, String assetDownloadURL, WhatsayAssetDownloadHandler handler){
	super();
	this.mContext = context;
	this.mAssetDownloadURL = assetDownloadURL;
	this.mHandler = handler;
    }
    
    @Override
    public void run() {
	boolean isSuccess = downloadAsset(mContext, mAssetDownloadURL);
        if(mHandler != null){
            mHandler.sendAssetDownloadStatus(mAssetDownloadURL, mAssetDownloadPath, isSuccess);
        }
    }

    /**
     * Downloads the asset(if not exist) from given url and store it in app's private files directory with file name
     * parsed from given asset download url
     * 
     * @param context
     * @param assetDownloadURL
     * @return
     */
    public boolean downloadAsset(Context context, String url) {
	boolean isAvailable = false;
	if (context != null && !TextUtils.isEmpty(url)) {
	    String assetFileName = url.substring(url.lastIndexOf('/') + 1);
	    if (!TextUtils.isEmpty(assetFileName)) {
		File file = new File(context.getFilesDir(), assetFileName);
		mAssetDownloadPath = file.getAbsolutePath();
		if (!file.exists()) {
		    // asset not exists, so download now
		    OutputStream os = null;
		    try {
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			// conn.setConnectTimeout(30000);
			// conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			os = new FileOutputStream(file);
			copyStream(is, os);
			os.close();
			isAvailable = true;
		    } catch (Exception ex) {
			ex.printStackTrace();
			isAvailable = false;
		    } finally {
			try {
			    if (os != null) {
				os.close();
			    }
			} catch (IOException ie) {

			}
		    }
		} else {
		    isAvailable = true;
		}
	    }
	}
	return isAvailable;
    }

    /**
     * Copies stream from InputStream to OutputStream
     * 
     * @param is
     * @param os
     */
    public static void copyStream(InputStream is, OutputStream os) throws Exception {
	final int buffer_size = 1024;
	byte[] bytes = new byte[buffer_size];
	for (;;) {
	    int count = is.read(bytes, 0, buffer_size);
	    if (count == -1)
		break;
	    os.write(bytes, 0, count);
	}
    }

}
