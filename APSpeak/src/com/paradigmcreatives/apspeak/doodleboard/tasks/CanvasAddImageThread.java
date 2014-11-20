package com.paradigmcreatives.apspeak.doodleboard.tasks;


/**
 * Helper to perform image addition in background
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class CanvasAddImageThread extends Thread {

    private static final String TAG = "CanvasAddImageThread";

//    private CanvasActivity canvasActivity;
//    private int requestCode;
//    private int resultCode;
//    private Intent data;
//    private CanvasAddImageHandler handler;
//
//    public CanvasAddImageThread(CanvasActivity canvasActivity, int requestCode, int resultCode, Intent data) {
//	super();
//	this.canvasActivity = canvasActivity;
//	this.requestCode = requestCode;
//	this.resultCode = resultCode;
//	if (data != null) {
//	    this.data = new Intent(data);
//	} else {
//	    this.data = null;
//	}
//	handler = new CanvasAddImageHandler(canvasActivity, data);
//    }
//
//    private void willStartTask() {
//	if (handler != null) {
//	    handler.willStartTask();
//	} else {
//	    Logger.warn(TAG, "Handler is null");
//	}
//    }
//
//    private void didEndTask() {
//	if (handler != null) {
//	    handler.didEndTask();
//	} else {
//	    Logger.warn(TAG, "Handler is null");
//	}
//    }
//
//    @Override
//    public void run() {
//	willStartTask();
//	performImageAddition();
//	didEndTask();
//	super.run();
//    }
//
//    /**
//     * Does the image addition to canvas activity doodleview
//     */
//    private void performImageAddition() {
//	if (canvasActivity != null) {
//	    switch (requestCode) {
//	    case PickCaptureImageUtil.PICK_IMAGE_FROM_GALLERY:
//		if (resultCode == Activity.RESULT_OK) {
//		    PickCaptureImageUtil mImageUtility = canvasActivity.getImageUtility();
//		    DoodleView doodleView = canvasActivity.getDoodleView();
//		    if ((mImageUtility != null) && (doodleView != null)) {
//			DisplayMetrics displaymetrics = new DisplayMetrics();
//			canvasActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//			int screenHeight = displaymetrics.heightPixels;
//			int screenWidth = displaymetrics.widthPixels;
//			Bitmap bmp = mImageUtility.processSelectedGalleryImage(data, screenWidth, screenHeight, true);
//			if (bmp != null) {
//			    // doodleView.setBackgroundBitmap(bmp);
//			    doodleView.addLayer(ImageUtil.scaleBitmapToFitScreen(bmp, canvasActivity.getDoodleView(), 0.75f),
//				    LayerType.GALLERY);
//			} else {
//			    Logger.warn(TAG, "Failed to fetch image");
//			}
//		    }
//		}
//		break;
//
//	    case PickCaptureImageUtil.CAPTURE_IMAGE_FROM_CAMERA:
//		if (resultCode == Activity.RESULT_OK) {
//		    PickCaptureImageUtil mImageUtility = canvasActivity.getImageUtility();
//		    DoodleView doodleView = canvasActivity.getDoodleView();
//		    if ((mImageUtility != null) && (doodleView != null)) {
//			DisplayMetrics displaymetrics = new DisplayMetrics();
//			canvasActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//			int screenHeight = displaymetrics.heightPixels;
//			int screenWidth = displaymetrics.widthPixels;
//			Bitmap bmp = mImageUtility.processCapturedImage(screenWidth, screenHeight, true);
//			if (bmp != null) {
//			    // doodleView.setBackgroundBitmap(bmp);
//			    doodleView.addLayer(ImageUtil.scaleBitmapToFitScreen(bmp, canvasActivity.getDoodleView(), 0.75f),
//				    LayerType.CAMERA);
//			}
//		    }
//		}
//		break;
//
//	    case Constants.MAP_CAPTURE_CODE:
//		if (resultCode == Activity.RESULT_OK) {
//		    addMapSnapToBackground();
//		}
//		break;
//
//	    case Constants.RAGE_FACE_REQUEST_CODE:
//		if (resultCode == Activity.RESULT_OK) {
//		    addRageFaceSnapToBackground();
//		}
//		break;
//
//	    case Constants.GREETING_REQUEST_CODE:
//		if (resultCode == Activity.RESULT_OK) {
//		    addGreetingsSnapToBackground();
//		}
//		break;
//
//	    case Constants.EMOJI_REQUEST_CODE:
//		if (resultCode == Activity.RESULT_OK) {
//		    addEmojiSnapToBackground();
//		}
//		break;
//
//	    case Constants.BACKGROUND_GOOGLE_IMAGE_REQUEST_CODE:
//		if (resultCode == Activity.RESULT_OK) {
//		    addGoogleImageSnapToBackground();
//		}
//		break;
//
//	    /*
//	     * case Constants.BACKGROUND_TEXT_REQUEST_CODE: if (resultCode == Activity.RESULT_OK) {
//	     * addTextSnapToBackground(); } break;
//	     */
//	    case Constants.SEND_ACTION_IMAGE_CODE:
//		PickCaptureImageUtil mImageUtility = canvasActivity.getImageUtility();
//		DoodleView doodleView = canvasActivity.getDoodleView();
//		if ((mImageUtility != null) && (doodleView != null)) {
//		    DisplayMetrics displaymetrics = new DisplayMetrics();
//		    canvasActivity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		    int screenHeight = displaymetrics.heightPixels;
//		    int screenWidth = displaymetrics.widthPixels;
//		    Parcelable imageParcel = data.getParcelableExtra(Intent.EXTRA_STREAM);
//		    if (imageParcel != null && imageParcel instanceof Uri) {
//			Bitmap bmp = mImageUtility.processSelectedGalleryImage((Uri) imageParcel, screenWidth,
//				screenHeight, true);
//			if (bmp != null) {
//			    // doodleView.setBackgroundBitmap(bmp);
//			    doodleView.addLayer(ImageUtil.scaleBitmapToFitScreen(bmp, doodleView, 0.75f), LayerType.VIA_INTENT);
//			} else {
//			    Logger.warn(TAG, "Failed to fetch image");
//			}
//		    } else {
//			Logger.warn(TAG, "Unknown format of image sent");
//		    }
//		}
//		break;
//
//	    default:
//		break;
//	    }
//	} else {
//	    Logger.warn(TAG, "Canvas activity is null");
//	}
//    }
//
//    /**
//     * Adds the snap of the greeting to the background
//     */
//    private void addGreetingsSnapToBackground() {
//	if (canvasActivity != null) {
//	    String tempFolderPath = AppPropertiesUtil.getAppDirectory(canvasActivity) + "/"
//		    + canvasActivity.getString(R.string.temp_folder);
//	    String mapBitmapFilePath = canvasActivity.getString(R.string.temp_assets_snap);
//	    Bitmap bmp = Util.decompressImage(tempFolderPath, mapBitmapFilePath);
//	    if (bmp != null) {
//		DoodleView doodleView = canvasActivity.getDoodleView();
//		if (doodleView != null) {
//		    // doodleView.setBackgroundBitmap(bmp);
//		    bmp = ImageUtil.scaleBitmapToFitScreen(bmp, canvasActivity.getDoodleView(), 1);
//		    bmp = ImageUtil.maskBitampForLowQuality(doodleView, bmp);
//		    doodleView.addLayer(bmp, LayerType.GREETINGS, Layer.getThumbnailURLFromData(data));
//		}
//	    }
//	    Util.deleteFile(new File(tempFolderPath, mapBitmapFilePath));
//	} else {
//	    Logger.warn(TAG, "Canvas activity is null");
//	}
//    }
//
//    /**
//     * Adds the snap of the emoji to the background
//     */
//    private void addEmojiSnapToBackground() {
//	if (canvasActivity != null) {
//	    String tempFolderPath = AppPropertiesUtil.getAppDirectory(canvasActivity) + "/"
//		    + canvasActivity.getString(R.string.temp_folder);
//	    String mapBitmapFilePath = canvasActivity.getString(R.string.temp_assets_snap);
//	    Bitmap bmp = Util.decompressImage(tempFolderPath, mapBitmapFilePath);
//	    if (bmp != null) {
//		DoodleView doodleView = canvasActivity.getDoodleView();
//		if (doodleView != null) {
//		    // Adding emoji as a layer instead of background
//		    // doodleView.addLayer(bmp, 0, 0, LayerType.EMOJI);
//		    bmp = ImageUtil.maskBitampForLowQuality(doodleView, bmp);
//		    doodleView.addLayer(bmp, LayerType.EMOJI, Layer.getThumbnailURLFromData(data));
//		    // doodleView.setBackgroundBitmap(bmp);
//		}
//	    }
//	    Util.deleteFile(new File(tempFolderPath, mapBitmapFilePath));
//	} else {
//	    Logger.warn(TAG, "Canvas activity is null");
//	}
//    }
//
//    /**
//     * Adds the snap of the rage face to the background
//     */
//    private void addRageFaceSnapToBackground() {
//	if (canvasActivity != null) {
//	    String tempFolderPath = AppPropertiesUtil.getAppDirectory(canvasActivity) + "/"
//		    + canvasActivity.getString(R.string.temp_folder);
//	    String mapBitmapFilePath = canvasActivity.getString(R.string.temp_assets_snap);
//	    Bitmap bmp = Util.decompressImage(tempFolderPath, mapBitmapFilePath);
//	    if (bmp != null) {
//		DoodleView doodleView = canvasActivity.getDoodleView();
//		if (doodleView != null) {
//		    // doodleView.setBackgroundBitmap(bmp);
//		    // Adding Rage Face as layer instead of background
//		    bmp = ImageUtil.maskBitampForLowQuality(doodleView, bmp);
//		    doodleView.addLayer(bmp, LayerType.RAGE_FACE, Layer.getThumbnailURLFromData(data));
//		}
//	    }
//	    Util.deleteFile(new File(tempFolderPath, mapBitmapFilePath));
//	} else {
//	    Logger.warn(TAG, "Canvas activity is null");
//	}
//    }
//
//    /**
//     * Adds the snap of the google image to the background
//     */
//    private void addGoogleImageSnapToBackground() {
//	if (canvasActivity != null) {
//	    String tempFolderPath = AppPropertiesUtil.getAppDirectory(canvasActivity) + "/"
//		    + canvasActivity.getString(R.string.temp_folder);
//	    String mapBitmapFilePath = canvasActivity.getString(R.string.temp_google_image_snap);
//	    Bitmap bmp = Util.decompressImage(tempFolderPath, mapBitmapFilePath);
//	    if (bmp != null) {
//		DoodleView doodleView = canvasActivity.getDoodleView();
//		if (doodleView != null) {
//		    // Adding google image as layer instead of background
//		    bmp = ImageUtil.maskBitampForLowQuality(doodleView, bmp);
//		    doodleView.addLayer(bmp, LayerType.GOOGLE_IMAGES, Layer.getThumbnailURLFromData(data));
//		    // doodleView.setBackgroundBitmap(bmp);
//		}
//	    }
//	    Util.deleteFile(new File(tempFolderPath, mapBitmapFilePath));
//	} else {
//	    Logger.warn(TAG, "Canvas activity is null");
//	}
//    }
//
//    /**
//     * Adds the snap of the map view to the background
//     */
//    private void addMapSnapToBackground() {
//	if (canvasActivity != null) {
//	    String tempFolderPath = AppPropertiesUtil.getAppDirectory(canvasActivity) + "/"
//		    + canvasActivity.getString(R.string.temp_folder);
//	    String mapBitmapFilePath = canvasActivity.getString(R.string.temp_map_bitmap);
//	    Bitmap bmp = Util.decompressImage(tempFolderPath, mapBitmapFilePath);
//	    if (bmp != null) {
//		DoodleView doodleView = canvasActivity.getDoodleView();
//		if (doodleView != null) {
//		    // doodleView.setBackgroundBitmap(bmp);
//		    doodleView.addLayer(ImageUtil.scaleBitmapForScreen(bmp, canvasActivity.getDoodleView(), 0.75f),
//			    LayerType.MAP);
//		}
//	    }
//	    Util.deleteFile(new File(tempFolderPath, mapBitmapFilePath));
//	} else {
//	    Logger.warn(TAG, "Canvas activity is null");
//	}
//    }

}
