package com.paradigmcreatives.apspeak.stream.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.stream.handlers.GetAssetLikesHandler;
import com.paradigmcreatives.apspeak.stream.tasks.helpers.GetAssetLikesHelper;

import android.content.Context;
import android.text.TextUtils;

/**
 * Thread that initiates fetching Asset's Likes details
 * 
 * @author Dileep | neuv
 *
 */
public class GetAssetLikesTask extends Thread{

    private Context context;
    private GetAssetLikesHandler handler;
    private String assetId;
    
    public GetAssetLikesTask(final Context context, GetAssetLikesHandler handler, String assetId){
	super();
	this.context = context;
	this.handler = handler;
	this.assetId = assetId;
    }
    
    @Override
    public void run() {
         super.run();
         if(context != null && !TextUtils.isEmpty(assetId)){
             GetAssetLikesHelper helper = new GetAssetLikesHelper(context, assetId, 0, 25);
             HashMap<ASSOCIATION_TYPE, ArrayList<Friend>> associations = helper.execute();
             if(handler != null){
                 if(associations != null && associations.size() > 0 && associations.containsKey(ASSOCIATION_TYPE.LOVE)){
                     handler.didFetchComplete(associations.get(ASSOCIATION_TYPE.LOVE));
                 }else{
            	 	handler.failed(-1, -1, "No associations found");
                 }
             }
         }
    }
}
