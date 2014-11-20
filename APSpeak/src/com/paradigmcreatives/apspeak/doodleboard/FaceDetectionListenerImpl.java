package com.paradigmcreatives.apspeak.doodleboard;

import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.util.Log;

public class FaceDetectionListenerImpl implements FaceDetectionListener {

	@Override
	public void onFaceDetection(Face[] faces, Camera camera) {
		if (faces.length > 0){
            Log.d("FaceDetection", "face detected: "+ faces.length +
                    " Face 1 Location X: " + faces[0].rect.centerX() +
                    "Y: " + faces[0].rect.centerY() );
        }

	}

}
