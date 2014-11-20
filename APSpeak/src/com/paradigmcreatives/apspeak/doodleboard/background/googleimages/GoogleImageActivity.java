package com.paradigmcreatives.apspeak.doodleboard.background.googleimages;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.GridView;

import com.facebook.AppEventsLogger;
import com.facebook.Settings;
import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.google.image.GoogleImageSearchHelper;
import com.paradigmcreatives.apspeak.app.util.google.image.beans.ImageResultsBean;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners.GoogleImageItemClickListenerImpl;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners.GoogleImageOnScrollListenerImpl;
import com.paradigmcreatives.apspeak.doodleboard.background.googleimages.listeners.GoogleImagesEditorListener;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Activity to launch url in webview and provide a snap shot
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class GoogleImageActivity extends Activity {

    private static final String TAG = "GoogleImageActivity";

    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private static final String STATE_PAUSE_ON_SCROLL = "STATE_PAUSE_ON_SCROLL";
    private static final String STATE_PAUSE_ON_FLING = "STATE_PAUSE_ON_FLING";
    private boolean pauseOnScroll = false;
    private boolean pauseOnFling = true;

    // private ImageButton backButton;
    // private ImageButton refreshButton;
    // private ImageButton forwardButton;
    private EditText searchBar;
    private GridView gridView;
    private GoogleImageSearchHelper searchHelper;

    private boolean isLoadingMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.background_google_images);

	options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.ARGB_8888).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
		.showImageForEmptyUri(R.drawable.removed).showImageOnFail(R.drawable.removed)
		.showStubImage(R.drawable.sandtime).build();

	searchBar = (EditText) findViewById(R.id.search_bar);
	Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
	searchBar.setTypeface(myTypeface);
//	searchBar.setOnEditorActionListener(new GoogleImagesEditorListener(this));

	gridView = (GridView) findViewById(R.id.grid_view);
//	gridView.setOnItemClickListener(new GoogleImageItemClickListenerImpl(this));

    }

    public void updateAdapter(ArrayList<ImageResultsBean> results) {
	GoogleImageAdapter adapter = (GoogleImageAdapter) gridView.getAdapter();
	if (adapter != null) {
	    adapter.addImageResults(results);
	} else {
//	    adapter = new GoogleImageAdapter(this, results);
	    gridView.setAdapter(adapter);
	}

	adapter.notifyDataSetChanged();
    }

    public void resetAdapter() {
	GoogleImageAdapter adapter = (GoogleImageAdapter) gridView.getAdapter();
//	adapter = new GoogleImageAdapter(this, new ArrayList<ImageResultsBean>());
	gridView.setAdapter(adapter);

	adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
	setResult(RESULT_CANCELED);
	finish();
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
	pauseOnScroll = savedInstanceState.getBoolean(STATE_PAUSE_ON_SCROLL, false);
	pauseOnFling = savedInstanceState.getBoolean(STATE_PAUSE_ON_FLING, true);
    }

    @Override
    public void onResume() {
	super.onResume();
	AppEventsLogger.activateApp(getApplicationContext(), Constants.FACEBOOK_APPID);
	applyScrollListener();
    }

    private void applyScrollListener() {
//	gridView.setOnScrollListener(new GoogleImageOnScrollListenerImpl(this, imageLoader, pauseOnScroll, pauseOnFling));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	outState.putBoolean(STATE_PAUSE_ON_SCROLL, pauseOnScroll);
	outState.putBoolean(STATE_PAUSE_ON_FLING, pauseOnFling);
    }

    /**
     * Saves bitmap in whatsay folder as tempImage.jpeg The image will be deleted after submitting the expression to
     * stream
     * 
     * @param context
     * @param bitmap
     * @return
     */
    public boolean saveTheGoogleImage(Bitmap bitmap) {
	boolean saved = false;
	try {
	    if (bitmap != null) {
		if (DeviceInfoUtil.mediaWritable()) {
		    String appRoot = AppPropertiesUtil.getAppDirectory(this);
		    String tempFolder = getResources().getString(R.string.temp_folder);
		    File tempFile = new File(appRoot, tempFolder);
		    if (!tempFile.exists()) {
			tempFile.mkdir();
		    }

		    String tempFolderPath = tempFile.getAbsolutePath();
		    File imageFile = new File(tempFolderPath, getResources().getString(R.string.temp_google_image_snap));
		    FileOutputStream out = new FileOutputStream(imageFile);

		    saved = bitmap.compress(CompressFormat.PNG, Constants.COMPRESSION_QUALITY_HIGH, out);
		} else {
		    Logger.warn(TAG, "Can't save bitmap to file. No memory card");
		}

	    } else {
		Logger.warn(TAG, "Either context is null or the supplied bitmap is null. Conext - " + " Bitmap - "
			+ bitmap);
	    }
	} catch (Exception e) {
	    Logger.warn(TAG, e.getMessage());
	}
	return saved;
    }

    /**
     * Returns the current search text
     * 
     * @return
     */
    public String getSearchText() {
	String result = null;
	if (searchBar != null) {
	    result = searchBar.getText().toString();
	} else {
	    Logger.warn(TAG, "Search bar is not initialized");
	}

	return result;
    }

    /**
     * @return the searchHelper
     */
    public GoogleImageSearchHelper getSearchHelper() {
	return searchHelper;
    }

    /**
     * @param searchHelper
     *            the searchHelper to set
     */
    public void setSearchHelper(GoogleImageSearchHelper searchHelper) {
	this.searchHelper = searchHelper;
    }

    /**
     * @return the options
     */
    public DisplayImageOptions getDisplayImageOptions() {
	return options;
    }

    /**
     * @param options
     *            the options to set
     */
    public void setDisplayImageOptions(DisplayImageOptions options) {
	this.options = options;
    }

    /**
     * @return the imageLoader
     */
    public ImageLoader getImageLoader() {
	return imageLoader;
    }

    /**
     * @param imageLoader
     *            the imageLoader to set
     */
    public void setImageLoader(ImageLoader imageLoader) {
	this.imageLoader = imageLoader;
    }

    /**
     * @return the isLoadingMore
     */
    public boolean isLoadingMore() {
	return isLoadingMore;
    }

    /**
     * @param isLoadingMore
     *            the isLoadingMore to set
     */
    public void setLoadingMore(boolean isLoadingMore) {
	this.isLoadingMore = isLoadingMore;
    }

}
