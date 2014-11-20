package com.paradigmcreatives.apspeak.doodleboard;
/*package com.paradigmcreatives.apspeak.doodleboard;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.GoogleImageActivity;
import com.paradigmcreatives.apspeak.doodleboard.listeners.CameraImageListener;
import com.paradigmcreatives.apspeak.doodleboard.listeners.CameraOptionsClickListener;

*//**
 * Activity to launch Camera, with which user can capture current moment
 * 
 * @author Dileep | neuv
 * 
 *//*
public class DoodlyDooCameraActivity extends Activity implements
		CameraImageListener {

	private static final int SELECT_PICTURE = 1;
	private static final int SELECT_PICTURE_KITKAT_AND_ABOVE = 2;
	private static final int SUBMIT_PICTURE = 3;
	private static final int SEARCH_IMAGE = 4;

	private FrameLayout mCameraPreviewLayout;
	private LinearLayout mCameraOptionsLayout;
	private LinearLayout mCameraOptionsTopLayout;
	private LinearLayout mCameraOptionsBottomLayout;
	private ImageView mCameraFlash;
	private ImageView mFlipCamera;
	private ImageView mCameraGrid;
	private ImageView mGallery;
	private ImageView mCapture;
	private ImageView mExplore;
	private ImageView mSticker;

	// private Camera mCamera;
	private CameraPreview mCameraPreview;
	private int squareCameraWidth = 0;
	private int squareCameraHeight = 0;
	private boolean mIsFrontCameraInUse;

	// Comment details
	private boolean isForCommenting = false;
	private String commentOnAssetId = null;
	private LinearLayout cameraOverlayLayout;
	private Dialog cameraOverlayDialog;

	// private boolean mIsFlashOn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera_layout_v2);

		// Check whether camera is launched to comment on an asset
		if (getIntent().hasExtra(Constants.IS_COMMENT)) {
			isForCommenting = getIntent().getBooleanExtra(Constants.IS_COMMENT,
					false);
			if (isForCommenting
					&& getIntent().hasExtra(Constants.COMMENT_ON_ASSET_ID)) {
				commentOnAssetId = getIntent().getStringExtra(
						Constants.COMMENT_ON_ASSET_ID);
			}
		}

		initUI();

		cameraOverlayDialog = new Dialog(this,
				android.R.style.Theme_Translucent_NoTitleBar);
		cameraOverlayDialog
				.setContentView(R.layout.camera_layout_help_overlay_v2);
		cameraOverlayLayout = (LinearLayout) cameraOverlayDialog
				.findViewById(R.id.camerahelplayout);
		if (!AppPropertiesUtil.getCameraHelpOverlayStatus(this)) {
			showOverLay();
			AppPropertiesUtil.setCameraHelpOverlayStatus(this, true);
		} else {
			cameraOverlayLayout.setVisibility(View.GONE);
		}

		// create an instance of camera
		// mCamera = getCameraInstance();
		// Create our Preview view and set it as the content of our activity.
		mCameraPreview = new DoodlyDooCameraPreview(this, mCamera, 
		mIsFrontCameraInUse, this);
		if (mCameraPreviewLayout != null) {
			mCameraPreviewLayout.addView(mCameraPreview);
		}
		if (mSticker != null) {
			mCameraPreviewLayout.addView(mSticker);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		 * if (mCamera != null) { mCamera.release(); mCamera = null; }
		 
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		 * if (mCamera != null) { mCamera.release(); mCamera = null; }
		 
	}

	private void initUI() {
		mCameraPreviewLayout = (FrameLayout) findViewById(R.id.camera_preview_layout);
		mCameraOptionsLayout = (LinearLayout) findViewById(R.id.options);
		mCameraOptionsTopLayout = (LinearLayout) findViewById(R.id.options_top);
		mCameraOptionsBottomLayout = (LinearLayout) findViewById(R.id.options_bottom);

		mCapture = (ImageView) findViewById(R.id.camera_capture);
		mFlipCamera = (ImageView) findViewById(R.id.flip_camera);
		mGallery = (ImageView) findViewById(R.id.gallery_launch);
		mExplore = (ImageView) findViewById(R.id.explore_launch);
		mCameraFlash = (ImageView) findViewById(R.id.camera_flash_toggle);
		mCameraFlash.setTag(R.drawable.flash_disabled_icon);
		mCameraFlash.setImageResource(R.drawable.flash_disabled_icon);
		mCameraGrid = (ImageView) findViewById(R.id.camera_grid);
		mCameraGrid.setTag(R.drawable.grid_disabled);
		mCameraGrid.setImageResource(R.drawable.grid_disabled);

		mSticker = new ImageView(this);
		mSticker.setImageResource(R.drawable.sticker4);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(200,
				200, Gravity.CENTER | Gravity.RIGHT);
		mSticker.setLayoutParams(params);
		mSticker.setVisibility(View.INVISIBLE);

		adjustLayoutToShowCameraInSquare();

		CameraOptionsClickListener listener = new CameraOptionsClickListener(
				this);
		mCapture.setOnClickListener(listener);
		mGallery.setOnClickListener(listener);
		mExplore.setOnClickListener(listener);
		mCameraGrid.setOnClickListener(listener);
		mCameraFlash.setOnClickListener(listener);

		if (!hasFrontCamera()) {
			mFlipCamera.setVisibility(View.INVISIBLE);
		} else {
			mFlipCamera.setOnClickListener(listener);
		}

		mIsFrontCameraInUse = false;
		// mIsFlashOn = false;
	}

	*//**
	 * Adjusts entire layout so that live camera will be shown in Square shape
	 *//*
	private void adjustLayoutToShowCameraInSquare() {
		Display display = this.getWindowManager().getDefaultDisplay();
		if (display != null) {
			Point size = new Point();
			display.getSize(size);

			// Screen width and height
			int screenWidth = size.x;
			int screenHeight = size.y;

			// Calculate square Camera width, height and layout width, height
			int cameraOptionsLayoutWidth = 0;
			int cameraOptionsLayoutHeight = 0;
			if (screenWidth < screenHeight) {
				squareCameraWidth = screenWidth;
				squareCameraHeight = squareCameraWidth;
			} else {
				squareCameraHeight = screenHeight;
				squareCameraWidth = squareCameraHeight;
			}
			cameraOptionsLayoutWidth = squareCameraWidth;
			cameraOptionsLayoutHeight = screenHeight - squareCameraHeight;

			// Allocate 30% of layoutHeight to top layout height and remaining
			// 70% to bottom layout height
			// 1. Assign width, height and gravity for camera options layout
			FrameLayout.LayoutParams optionsLayoutParams = new FrameLayout.LayoutParams(
					cameraOptionsLayoutWidth, cameraOptionsLayoutHeight);
			optionsLayoutParams.gravity = Gravity.BOTTOM;
			if (mCameraOptionsLayout != null) {
				mCameraOptionsLayout.setLayoutParams(optionsLayoutParams);
			}
			// 2. Assign width and height to camera options top layout
			LinearLayout.LayoutParams topOptionsLayoutParams = new LinearLayout.LayoutParams(
					cameraOptionsLayoutWidth,
					(int) (0.3 * cameraOptionsLayoutHeight));
			if (mCameraOptionsTopLayout != null) {
				mCameraOptionsTopLayout.setLayoutParams(topOptionsLayoutParams);
			}
			// 3. Assign width and height to camera options bottom layout
			LinearLayout.LayoutParams bottomOptionsLayoutParams = new LinearLayout.LayoutParams(
					cameraOptionsLayoutWidth,
					(int) (0.7 * cameraOptionsLayoutHeight));
			if (mCameraOptionsBottomLayout != null) {
				mCameraOptionsBottomLayout
						.setLayoutParams(bottomOptionsLayoutParams);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == SELECT_PICTURE
					|| requestCode == SELECT_PICTURE_KITKAT_AND_ABOVE) {
				// Picture selected from Gallery
				if (data != null) {
					Uri selectedImageUri = data.getData();
					showImageFilterScreen(selectedImageUri);

					
					 * String selectedImagePath = getPath(selectedImageUri);
					 * notifyImagePath(selectedImagePath, true);
					 

				}
			} else if (requestCode == SEARCH_IMAGE) {
				String appRoot = AppPropertiesUtil.getAppDirectory(this);
				String tempFolder = getResources().getString(
						R.string.temp_folder);
				File tempFile = new File(appRoot, tempFolder);
				if (tempFile != null && tempFile.exists()) {
					String tempFolderPath = tempFile.getAbsolutePath();
					File imageFile = new File(tempFolderPath, getResources()
							.getString(R.string.temp_google_image_snap));
					if (imageFile != null && imageFile.exists()) {
						notifyImagePath(imageFile.getAbsolutePath(), true);
					}
				}
			} else if (requestCode == SUBMIT_PICTURE) {
				finish();
			}
		}
	}

	*//**
	 * Returns available cameras count of the device
	 * 
	 * @return
	 *//*
	private int getNumberOfCameras() {
		return Camera.getNumberOfCameras();
	}

	*//**
	 * Returns information of the given Camera Id
	 * 
	 * @param cameraId
	 * @param cameraInfo
	 *//*
	private void getCameraInfo(int cameraId, Camera.CameraInfo cameraInfo) {
		Camera.getCameraInfo(cameraId, cameraInfo);
	}

	*//**
	 * Takes picture from the current live camera
	 *//*
	public void takePicture() {
		if (mCameraPreview != null) {
			mCameraPreview.takePicture();
		}
	}

	*//**
	 * Flips camera from FRONT to BACK and vice-versa
	 *//*
	public void flipCamera() {
		if (mCameraPreview != null) {
			// Flip the camera
			mCameraPreview.flipCamera(mIsFrontCameraInUse);

			// After the flip, if we are seeing FRONT camera, then disable flash
			// by setting to OFF
			if (mIsFrontCameraInUse) {
				if (mCameraFlash != null) {
					Camera camera = mCameraPreview.getOpenedCameraInstance();
					Camera.Parameters parameters = camera.getParameters();
					if (parameters != null) {
						List<String> flashModes = parameters
								.getSupportedFlashModes();
						int currentTag = ((Integer) mCameraFlash.getTag())
								.intValue();
						switch (currentTag) {
						case R.drawable.flash_auto_icon:
						case R.drawable.flash_selected_icon:
							mCameraFlash.setTag(R.drawable.flash_disabled_icon);
							mCameraFlash
									.setImageResource(R.drawable.flash_disabled_icon);
							break;
						}
					}
				}
			}
		}
	}// end of function flipCamera()

	*//**
	 * Changes the camera flash mode from ON to OFF and vice-versa
	 *//*
	public void changeCameraFlashMode() {
		if (mCameraPreview != null && mCameraFlash != null) {
			Camera camera = mCameraPreview.getOpenedCameraInstance();
			Camera.Parameters parameters = camera.getParameters();
			if (parameters != null) {
				List<String> flashModes = parameters.getSupportedFlashModes();
				int currentTag = ((Integer) mCameraFlash.getTag()).intValue();
				switch (currentTag) {
				case R.drawable.flash_disabled_icon:
					// Change to flash AUTO
					if (flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
						parameters
								.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
						camera.setParameters(parameters);
						mCameraFlash.setTag(R.drawable.flash_auto_icon);
						mCameraFlash
								.setImageResource(R.drawable.flash_auto_icon);
					}
					break;

				case R.drawable.flash_auto_icon:
					// Change to flash ON
					if (flashModes.contains(Camera.Parameters.FLASH_MODE_ON)) {
						parameters
								.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
						camera.setParameters(parameters);
						mCameraFlash.setTag(R.drawable.flash_selected_icon);
						mCameraFlash
								.setImageResource(R.drawable.flash_selected_icon);
					}
					break;

				case R.drawable.flash_selected_icon:
					// Change to flash OFF
					if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
						parameters
								.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
						camera.setParameters(parameters);
						mCameraFlash.setTag(R.drawable.flash_disabled_icon);
						mCameraFlash
								.setImageResource(R.drawable.flash_disabled_icon);
					}
					break;

				default:
					// Change to flash AUTO
					if (flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
						parameters
								.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
						camera.setParameters(parameters);
						mCameraFlash.setTag(R.drawable.flash_auto_icon);
						mCameraFlash
								.setImageResource(R.drawable.flash_auto_icon);
					}
					break;
				}
			}
		}
	}

	*//**
	 * Shows/hides stickers on live camera
	 *//*
	public void showStickers() {
		if (mCameraGrid != null && mSticker != null) {
			int currentTag = ((Integer) mCameraGrid.getTag()).intValue();
			switch (currentTag) {
			case R.drawable.grid_disabled:
				// Show stickers
				mSticker.setVisibility(View.VISIBLE);
				mCameraGrid.setTag(R.drawable.grid_selected);
				mCameraGrid.setImageResource(R.drawable.grid_selected);
				break;

			case R.drawable.grid_selected:
				// Hide stickers
				mSticker.setVisibility(View.INVISIBLE);
				mCameraGrid.setTag(R.drawable.grid_disabled);
				mCameraGrid.setImageResource(R.drawable.grid_disabled);
				break;

			default:
				break;
			}
		}
	}

	*//**
	 * Sets the flag that represents whether FRONT camera is in use or not
	 * 
	 * @param isFrontCameraInUse
	 *//*
	public void setIsFrontCameraInUse(boolean isFrontCameraInUse) {
		mIsFrontCameraInUse = isFrontCameraInUse;
		// Flash can't be used while front camera is in use
		if (mCameraFlash != null) {
			if (mIsFrontCameraInUse) {
				// Disable flash clicks
				mCameraFlash.setClickable(false);
			} else {
				// Enable flash clicks
				mCameraFlash.setClickable(true);
			}

		}
	}

	*//**
	 * Updates camera instance
	 * 
	 * @param camera
	 *//*
	
	 * public void updateCameraInstance(Camera camera) { mCamera = camera; }
	 

	public void saveVisiblePortion(String imagePath) {

		if (!TextUtils.isEmpty(imagePath)) {
			Paint paint = new Paint();
			paint.setFilterBitmap(true);
			File mediaStorageDir = new File(
					Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
					"MyCameraApp");
			File mediaFile2 = new File(mediaStorageDir.getPath()
					+ File.separator + "IMG_temp" +  timeStamp + ".jpg");
			File mediaFile = new File(imagePath);

			Bitmap bitmapOrg = BitmapFactory.decodeFile(mediaFile
					.getAbsolutePath());
			// Bitmap bitmapOrg =
			// BitmapFactory.decodeResource(getResources(),R.drawable.sticker4);

			// Rotate the image
			Matrix mat = new Matrix();
			mat.postRotate(90);
			Bitmap bMapRotate = Bitmap.createBitmap(bitmapOrg, 0, 0,
					bitmapOrg.getWidth(), bitmapOrg.getHeight(), mat, true);

			// Crop the image
			
			 * int targetWidth = 200; int targetHeight = 200;
			 * 
			 * 
			 * Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
			 * targetHeight,Bitmap.Config.ARGB_8888);
			 * 
			 * RectF rectf = new RectF(0, 0, 100, 100);
			 * 
			 * Canvas canvas = new Canvas(targetBitmap); Path path = new Path();
			 * 
			 * path.addRect(rectf, Path.Direction.CW); canvas.clipPath(path);
			 * 
			 * //canvas.drawBitmap( bitmapOrg, new Rect(0, 0,
			 * bitmapOrg.getWidth(), bitmapOrg.getHeight()), // new Rect(0, 0,
			 * targetWidth, targetHeight), paint);
			 * 
			 * canvas.drawBitmap( bMapRotate, new Rect(0, 0,
			 * bMapRotate.getWidth(), bMapRotate.getHeight()), new Rect(0, 0,
			 * targetWidth, targetHeight), paint);
			 * 
			 * 
			 * Matrix matrix = new Matrix(); matrix.postScale(2f, 2f); Bitmap
			 * resizedBitmap = Bitmap.createBitmap(targetBitmap, 0, 0, 100, 100,
			 * matrix, true); //mSticker.setImageBitmap(resizedBitmap);
			 
			mSticker.setImageBitmap(cropBitmap(bMapRotate));
			 convert Bitmap to resource 
			// BitmapDrawable bd = new BitmapDrawable(resizedBitmap);

			
			 * // copy cropped image to a specific file //b is the Bitmap
			 * 
			 * //calculate how many bytes our image consists of. int bytes =
			 * resizedBitmap.getByteCount(); //or we can calculate bytes this
			 * way. Use a different value than 4 if you don't use 32bit images.
			 * //int bytes = b.getWidth()*b.getHeight()*4;
			 * 
			 * ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new
			 * buffer resizedBitmap.copyPixelsToBuffer(buffer); //Move the byte
			 * data to the buffer
			 * 
			 * byte[] array = buffer.array(); //Get the underlying array
			 * containing the data. try { FileOutputStream fos = new
			 * FileOutputStream(mediaFile2); fos.write(array); fos.close(); }
			 * catch (FileNotFoundException e) { //Log.d(TAG, "File not found: "
			 * + e.getMessage()); } catch (IOException e) { //Log.d(TAG,
			 * "Error accessing file: " + e.getMessage()); } catch (Exception e)
			 * { // TODO: handle exception }
			 
		}
	}

	@Override
	public void notifyImagePath(String imagePath, boolean isFromGallery) {
		if (mCameraPreview != null) {
			mCameraPreview.setIsSafeToClick(true);
		}
		if (!TextUtils.isEmpty(imagePath)) {
			// saveVisiblePortion(capturedImagePath);
			// String imagePath = pictureFile.getAbsolutePath();
			// if (imagePath != null) {
			Intent intent = new Intent(getBaseContext(),
					CanvasFragmentActivity.class);
			intent.putExtra(Constants.CANVAS_INTENT_SOURCE_KEY,
					Constants.CANVAS_INTENT_SOURCE_CAMERA);
			intent.putExtra(Constants.CANVAS_INTENT_BITMAP_PATH, imagePath);
			intent.putExtra(Constants.ROTATE_BITMAP, true);
			if (mIsFrontCameraInUse) {
				intent.putExtra(Constants.ISFRONTCAMERA, true);
			}
			if (isFromGallery) {
				intent.putExtra(Constants.ISFROMGALLERY, isFromGallery);
				intent.putExtra(Constants.ROTATE_BITMAP, false);
			}
			intent.putExtra(Constants.IS_COMMENT, isForCommenting);
			intent.putExtra(Constants.COMMENT_ON_ASSET_ID, commentOnAssetId);
			// startActivity(intent);
			startActivityForResult(intent, SUBMIT_PICTURE);
		}
	}

	public Bitmap cropBitmap(Bitmap img) {
		// Merge two images together.
		int width = img.getWidth();
		int height = img.getHeight();
		
		 * Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(),
		 * Bitmap.Config.ARGB_8888); Canvas combineImg = new Canvas(bm);
		 * combineImg.drawBitmap(img, 0f, 0f, null);
		 * combineImg.drawBitmap(templateImage, 0f, 0f, null);
		 

		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);

		for (int i = 0; i < 200; i++) {
			for (int j = 0; j < 200; j++) {
				int px = img.getPixel(i, j);
				finalBm.setPixel(i, j, px);
			}
		}

		
		 * // Get the coordinates for the middle of combineImg. int hMid =
		 * bm.getHeight() / 2; int wMid = bm.getWidth() / 2; int hfMid =
		 * finalBm.getHeight() / 2; int wfMid = finalBm.getWidth() / 2;
		 * 
		 * int y2 = hfMid; int x2 = wfMid;
		 * 
		 * for (int y = hMid; y >= 0; y--) { boolean template = false; // Check
		 * Upper-left section of combineImg. for (int x = wMid; x >= 0; x--) {
		 * if (x2 < 0) { break; }
		 * 
		 * int px = bm.getPixel(x, y); if (Color.red(px) == 234 &&
		 * Color.green(px) == 157 && Color.blue(px) == 33) { template = true;
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else if (template) {
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else {
		 * finalBm.setPixel(x2, y2, px); } x2--; } // Check upper-right section
		 * of combineImage. x2 = wfMid; template = false; for (int x = wMid; x <
		 * bm.getWidth(); x++) { if (x2 >= finalBm.getWidth()) { break; }
		 * 
		 * int px = bm.getPixel(x, y); if (Color.red(px) == 234 &&
		 * Color.green(px) == 157 && Color.blue(px) == 33) { template = true;
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else if (template) {
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else {
		 * finalBm.setPixel(x2, y2, px); } x2++; }
		 * 
		 * // Once we reach the top-most part on the template line, set pixel
		 * value transparent // from that point on. int px = bm.getPixel(wMid,
		 * y); if (Color.red(px) == 234 && Color.green(px) == 157 &&
		 * Color.blue(px) == 33) { for (int y3 = y2; y3 >= 0; y3--) { for (int
		 * x3 = 0; x3 < finalBm.getWidth(); x3++) { finalBm.setPixel(x3, y3,
		 * Color.TRANSPARENT); } } break; }
		 * 
		 * x2 = wfMid; y2--; }
		 * 
		 * x2 = wfMid; y2 = hfMid; for (int y = hMid; y <= bm.getHeight(); y++)
		 * { boolean template = false; // Check bottom-left section of
		 * combineImage. for (int x = wMid; x >= 0; x--) { if (x2 < 0) { break;
		 * }
		 * 
		 * int px = bm.getPixel(x, y); if (Color.red(px) == 234 &&
		 * Color.green(px) == 157 && Color.blue(px) == 33) { template = true;
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else if (template) {
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else {
		 * finalBm.setPixel(x2, y2, px); } x2--; }
		 * 
		 * // Check bottom-right section of combineImage. x2 = wfMid; template =
		 * false; for (int x = wMid; x < bm.getWidth(); x++) { if (x2 >=
		 * finalBm.getWidth()) { break; }
		 * 
		 * int px = bm.getPixel(x, y); if (Color.red(px) == 234 &&
		 * Color.green(px) == 157 && Color.blue(px) == 33) { template = true;
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else if (template) {
		 * finalBm.setPixel(x2, y2, Color.TRANSPARENT); } else {
		 * finalBm.setPixel(x2, y2, px); } x2++; }
		 * 
		 * // Once we reach the bottom-most part on the template line, set pixel
		 * value transparent // from that point on. int px = bm.getPixel(wMid,
		 * y); if (Color.red(px) == 234 && Color.green(px) == 157 &&
		 * Color.blue(px) == 33) { for (int y3 = y2; y3 < finalBm.getHeight();
		 * y3++) { for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
		 * finalBm.setPixel(x3, y3, Color.TRANSPARENT); } } break; }
		 * 
		 * x2 = wfMid; y2++; }
		 

		// Get rid of images that we finished with to save memory.
		img.recycle();
		return finalBm;
	}

	*//**
	 * Launches Android Gallery to select an image and customize it with doodle
	 * draw, stickers, frames, etc.
	 *//*
	public void launchGallery() {
		if (Build.VERSION.SDK_INT < 19) {
			Intent galleryIntent = new Intent();
			galleryIntent.setType("image/*");
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(galleryIntent, "Select Picture"),
					SELECT_PICTURE);
		} else {
			Intent galleryIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent,
					SELECT_PICTURE_KITKAT_AND_ABOVE);
		}
	}

	*//**
	 * Launches an activity to search an image from Google and customize it with
	 * doodle draw, stickers, frames, etc.
	 *//*
	public void launchGoogleSearch() {
		Intent googleSearch = new Intent(DoodlyDooCameraActivity.this,
				GoogleImageActivity.class);
		startActivityForResult(googleSearch, SEARCH_IMAGE);
	}

	*//**
	 * helper to retrieve the path of an image URI
	 *//*
	public String getPath(Uri uri) {
		// just some safety built in
		if (uri == null) {
			return null;
		}
		// try to retrieve the image from the media store first
		// this will only work for images selected from gallery
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = this.getContentResolver().query(uri, projection, null,
				null, null);
		if (cursor != null && cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndex(MediaStore.Images.Media.DATA);
			String path = cursor.getString(column_index);
			cursor.close();
			return path;
		}
		// this is our fallback here
		return uri.getPath();
	}

	*//**
	 * Returns boolean that specifies whether front camera exists or not
	 *//*
	public boolean hasFrontCamera() {
		boolean hasFrontCamera = false;
		try {
			PackageManager pm = getApplicationContext().getPackageManager();
			hasFrontCamera = pm
					.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
		} catch (Exception e) {

		}
		return hasFrontCamera;
	}

	private void showOverLay() {
		cameraOverlayLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				cameraOverlayDialog.dismiss();
			}
		});
		cameraOverlayDialog.show();
	}

	private void showImageFilterScreen(Uri imageUri) {
		Intent intent = new Intent(this, ImageSelectionFragmentActivity.class);
		intent.putExtra(Constants.LAUNCH_FILTERS_FRAGMENT, imageUri.toString());
		startActivity(intent);
	}
}
*/