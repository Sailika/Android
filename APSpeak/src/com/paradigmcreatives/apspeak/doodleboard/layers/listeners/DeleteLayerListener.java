package com.paradigmcreatives.apspeak.doodleboard.layers.listeners;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView.PlayState;
import com.paradigmcreatives.apspeak.doodleboard.layers.LayerUtil;

public class DeleteLayerListener implements OnClickListener {

    private DoodleView doodleView;
    private Context context;
    private String layerID;

    /**
     * @param doodleView
     * @param context
     */
    public DeleteLayerListener(DoodleView doodleView, Context context, String layerID) {
	this.doodleView = doodleView;
	this.context = context;
	this.layerID = layerID;
    }

    @Override
    public void onClick(View v) {
	if (context != null && doodleView != null) {
	    int selectedLayerID = LayerUtil.intIDforUUID(doodleView, layerID);
	    if (selectedLayerID >= 0) {
	    	doodleView.removeLayerWithID(selectedLayerID);
	    	doodleView.setPlayState(PlayState.LAYERS);
//		LayerDialogUtil.launchBackgroundChangeDialog(doodleView, context, selectedLayerID)
//			.show();
	    }
	}

    }
}
