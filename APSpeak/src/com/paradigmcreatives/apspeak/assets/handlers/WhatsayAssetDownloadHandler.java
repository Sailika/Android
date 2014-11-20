package com.paradigmcreatives.apspeak.assets.handlers;

import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;

public class WhatsayAssetDownloadHandler extends Handler{

    private static final int SUCCESS = 1;
    private static final int FAILURE = 2;
    private static final String DOWNLOAD_URL = "downloadURL";
    private static final String DOWNLOAD_PATH = "donwloadPath";
    
    private Fragment mFragment;
    
    public WhatsayAssetDownloadHandler(final Fragment fragment){
	super();
	this.mFragment = fragment;
    }
    
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(msg != null && mFragment != null){
            String assetDownloadURL = null;
            String assetDownloadPath = null;
            Bundle data = msg.getData();
            if(data != null){
        	if(data.containsKey(DOWNLOAD_URL)){
        	    assetDownloadURL = data.getString(DOWNLOAD_URL);
        	}
        	if(data.containsKey(DOWNLOAD_PATH)){
        	    assetDownloadPath = data.getString(DOWNLOAD_PATH);
        	}
            }
            switch (msg.what) {
	    case SUCCESS:
		if(mFragment instanceof GlobalStreamsFragment){
		    ((GlobalStreamsFragment)mFragment).assetDownloadStatus(assetDownloadURL, assetDownloadPath, true);
		}
		break;
	    case FAILURE:
		if(mFragment instanceof GlobalStreamsFragment){
		    ((GlobalStreamsFragment)mFragment).assetDownloadStatus(assetDownloadURL, assetDownloadPath, false);
		}
		break;
	    default:
		break;
	    }
        }
    }
    
    public void sendAssetDownloadStatus(String assetDownloadURL, String assetDownloadPath, boolean isSuccess){
	Message msg = new Message();
	if(isSuccess){
	    msg.what = SUCCESS;
	}else{
	    msg.what = FAILURE;
	}
	Bundle data = new Bundle();
	data.putString(DOWNLOAD_URL, assetDownloadURL);
	data.putString(DOWNLOAD_PATH, assetDownloadPath);
	msg.setData(data);
	sendMessage(msg);
    }
}
