package com.paradigmcreatives.apspeak.doodleboard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImageChooserFragment;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImageFilterFragment;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImagePreviewFragment;
import com.paradigmcreatives.apspeak.doodleboard.listeners.CameraImageListener;
import com.paradigmcreatives.apspeak.doodleboard.tasks.GetResizedBitmapFromPathTask;

public class ImageSelectionFragmentActivity extends FragmentActivity implements
		CameraImageListener {
	private Fragment fragment;
	public static final String CAMERA_TAG = "CAMERA_TAG";
	public static final String FILTERS_TAG = "FILTERS_TAG";
	public static final String GALLERY_TAG = "GALLERY_TAG";
	public static final String BACKGROUND_TAG = "BACKGROUND_TAG";
	public static final String CANVAS_TAG = "CANVAS_TAG";
	public static final String PREVIEW_TAG = "PREVIEW_TAG";

	private final static String IMAGE_CHOOSER_FRAGMENT_TAG = "imageChooserFragment";
	private ImageChooserFragment imageChooserFragment;
	private String cueId;
	private ProgressBar progress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getIntent().hasExtra(Constants.CUE_ID)) {
			cueId = getIntent().getStringExtra(Constants.CUE_ID);
		}
		setContentView(R.layout.image_chooser);		
		showImageChooserFragment();
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getApplicationContext(), Constants.FACEBOOK_APPID);
	}
	
	/**
	 * Show the image chooser fragment so that user can select an image either
	 * from camera, default gallery or from the backgrounds
	 */
	public void showImageChooserFragment() {
		if (imageChooserFragment == null) {
			imageChooserFragment = new ImageChooserFragment(this, cueId);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			if (transaction != null) {
				transaction.replace(R.id.root, imageChooserFragment,
						IMAGE_CHOOSER_FRAGMENT_TAG);
				transaction.addToBackStack(IMAGE_CHOOSER_FRAGMENT_TAG);
				transaction.commit();
			}
		}
	}

	/**
	 * Show the image filter fragment with the given bitmap
	 * 
	 * @param selectedBitmap
	 */
	public void showImageFiltersFragment(Bitmap selectedBitmap) {
		if (selectedBitmap != null) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			fragment = new ImageFilterFragment(this, selectedBitmap);
			if (fragment != null && transaction != null) {
				transaction.replace(R.id.root, fragment, FILTERS_TAG);
				transaction.addToBackStack(FILTERS_TAG);
				transaction.commit();

			}

		}

	}

	public void showCanvasFragment(Bitmap selectedBitmap) {
		if (selectedBitmap != null) {
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			fragment = new ImagePreviewFragment(selectedBitmap, cueId);
			if (fragment != null && transaction != null) {
				transaction.replace(R.id.root, fragment, CANVAS_TAG);
				transaction.addToBackStack(CANVAS_TAG);
				transaction.commit();
			}

		}
	}

	@Override
	public void notifyImagePath(String imagePath, boolean isFromGallery) {
		int screenWidth;
		int screenHeight;
		Display display = this.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
		if (!TextUtils.isEmpty(imagePath)) {
			GetResizedBitmapFromPathTask bitmapTask = new GetResizedBitmapFromPathTask(
					this, imagePath, screenWidth, screenHeight, isFromGallery);
			bitmapTask.execute(imagePath);
	}
	}

	@Override
	public void notifyImageBitmap(Bitmap bitmap) {
		int screenWidth;
		int screenHeight;
		Display display = this.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
		if (bitmap != null) {
			GetResizedBitmapFromPathTask bitmapTask = new GetResizedBitmapFromPathTask(
					this, bitmap, screenWidth, screenHeight, true);
			bitmapTask.execute();
		}
	}

	public void processBitmapAndMoveToFilters(byte[] data, boolean isFrontFacing) {
		int screenWidth;
		int screenHeight;
		Display display = this.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		screenHeight = size.y;
		if (data != null) {
			GetResizedBitmapFromPathTask bitmapTask = new GetResizedBitmapFromPathTask(
					this, data, isFrontFacing, screenWidth, screenHeight, false);
			bitmapTask.execute("");
		}
	}

	/**
	 * helper to retrieve the path of an image URI
	 */
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (data != null) {
				Uri selectedImageUri = data.getData();
				String selectedImagePath = getPath(selectedImageUri);
				notifyImagePath(selectedImagePath, true);

			}

		}
	}

	public void stopProgress() {

		if (progress != null) {
			progress.setVisibility(View.GONE);
		}

	}

	public void showProgress() {

		if (progress != null) {
			progress.setVisibility(View.VISIBLE);
			progress.bringToFront();
		}

	}

	@Override
	public void onBackPressed() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		String fragmentTag = fragmentManager.getBackStackEntryAt(
				fragmentManager.getBackStackEntryCount() - 1).getName();
		if (!TextUtils.isEmpty(fragmentTag)) {
			if (fragmentTag.equals(CAMERA_TAG)
					|| fragmentTag.equals(GALLERY_TAG)
					|| fragmentTag.equals(BACKGROUND_TAG)
					|| fragmentTag.equals(IMAGE_CHOOSER_FRAGMENT_TAG)) {
				finish();
			}
			if (fragmentTag.equals(CANVAS_TAG)) {
				removeBackStack();
			}
			if (fragmentTag.equals(FILTERS_TAG)) {
				removeBackStack();
			}
		} else {
			super.onBackPressed();
		}

	}

	private void removeBackStack() {
		FragmentManager fm = getSupportFragmentManager();
		if (fm.getBackStackEntryCount() > 0) {
			fm.popBackStack();

		}
	}

}
