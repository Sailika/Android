package com.paradigmcreatives.apspeak.doodleboard.layers;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Convenience methods for layers
 * 
 * @author robin
 * 
 */
public class LayerUtil {

    private static final String TAG = "LayerUtil";

    /**
     * Get the coordinates for placing the given bitmap at the center of the DoodleView. In case of any error its return
     * (0, 0) as the placement coordinates
     * 
     * @param bmp
     * @param doodleWidth
     *            Width of the doodle view
     * @param doodleHeight
     *            Height of the doodle view
     * @return
     */
    public static PointF getCoordinatesForCenter(Bitmap bmp, int doodleWidth, int doodleHeight) {
	PointF coord = new PointF(0, 0);

	if (bmp != null) {
	    int width = bmp.getWidth();
	    int height = bmp.getHeight();

	    if (doodleWidth > 0 && doodleHeight > 0 && width > 0 && height > 0) {
		coord.x = Math.abs(doodleWidth - width) / 2;
		coord.y = Math.abs(doodleHeight - height) / 2;
	    }
	} else {
	    Logger.warn(TAG, "Null bitmap passed to get its center placement coordinates");
	}

	return coord;

    }

    public static int intIDforUUID(DoodleView doodleView, String layerID) {
	int i = 0;

	if (doodleView != null) {
	    List<Layer> layers = doodleView.getBitmapLayers();

	    if (layers != null && !layers.isEmpty()) {
		for (Layer layer : layers) {
		    if (TextUtils.equals(layer.getID(), layerID)) {
			break;
		    }
		    i++;
		}
	    }
	    if (i < layers.size()) {
		return i;
	    }
	}

	return -1;
    }

}
