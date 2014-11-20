package com.paradigmcreatives.apspeak.doodleboard.background.map;


public class GoogleMapsActivity //extends MapActivity implements DoodlyDooLocationListener {
{
//    private static final String TAG = "GoogleMapsActivity";
//
//    private static final int LOCATION_COUNTDOWN_TIMER_WAIT = 30 * 1000;
//
//    private DoodlyDooLocationManager mLocationManager;
//    private MapView mMapView;
//    private GeoPoint mCurrentPosition;
//    private GeoPoint mPreviousPosition;
//    private ImageButton mCapture;
//    private ImageButton mCancel;
//    private boolean initLocationVars = true;
//
//    private LogoutBroadcastReceiver logoutBroadcastReceiver;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//	super.onCreate(savedInstanceState);
//	setContentView(R.layout.map_layout);
//	logoutBroadcastReceiver = new LogoutBroadcastReceiver(this);
//	registerReceiver(logoutBroadcastReceiver, new IntentFilter(Constants.LOGOUT_BROADCAST_ACTION));
//
//	TextView headerTitle = (TextView) findViewById(R.id.map_title_textview);
//	Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
//	headerTitle.setTypeface(myTypeface);
//	mCapture = (ImageButton) findViewById(R.id.map_capture_icon);
//	mCancel = (ImageButton) findViewById(R.id.map_cancel_icon);
//
//	// Instead of waiting for the location to init the MapView, initializing the var here itself
//	mMapView = (MapView) findViewById(R.id.mapview);
//
//	// mCapture.setVisibility(View.INVISIBLE);
//	mCapture.setOnClickListener(new MapCaptureListener());
//	mCancel.setOnClickListener(new MapCancelListener());
//
//	initLocationManager();
//    }
//    
//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Rest of the code should come here
//        EasyTracker.getInstance(this).activityStart(this);
//    }
//    
//    @Override
//    protected void onStop() {
//        super.onStop();
//        // Rest of the code should come here
//        EasyTracker.getInstance(this).activityStop(this);
//    }
//
//    /**
//     * Initializes LocationManager
//     */
//    private void initLocationManager() {
//	Log.v(TAG, "init location manager");
//	// Get the location manager
//	mLocationManager = new DoodlyDooLocationManager(this);
//	Location location = mLocationManager.getLastKnownLocation();
//
//	if (location == null) {
//	    Log.v(TAG, "Get location from google");
//	    location = (new MyLocationOverlay(this, mMapView)).getLastFix();
//	}
//
//	// Initialize the location fields
//	if (location != null) {
//	    Log.v(TAG, "Got location : " + location.toString());
//	    receivedLocation(location);
//	} else {
//	    // Location details not available
//	    Util.displayToast(this, getResources().getString(R.string.wait_location_search));
//	}
//    }
//
//    private void receivedLocation(Location location) {
//	double latitude = location.getLatitude();
//	double longitude = location.getLongitude();
//	if (initLocationVars) {
//	    mPreviousPosition = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
//	    mCurrentPosition = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
//	    initLocationVars = false;
//	} else {
//	    if (mPreviousPosition != null && mCurrentPosition != null) {
//		mPreviousPosition = new GeoPoint(mCurrentPosition.getLatitudeE6(), mCurrentPosition.getLongitudeE6());
//		mCurrentPosition = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
//	    }
//	}
//	updateMapFragment();
//    }
//
//    @Override
//    protected boolean isRouteDisplayed() {
//	return false;
//    }
//
//    @Override
//    protected void onResume() {
//	Util.cancelNotification(this, false);
//	super.onResume();
//	Settings.publishInstallAsync(getApplicationContext(), Constants.FACEBOOK_APPID);
//	if (mLocationManager != null) {
//	    mLocationManager.setListener(this);
//	    mLocationManager.requestLocationUpdates(false);
//	    try {
//		DoodlyDooLocationCountdownTimer locTimer = new DoodlyDooLocationCountdownTimer(this,
//			LOCATION_COUNTDOWN_TIMER_WAIT, LOCATION_COUNTDOWN_TIMER_WAIT);
//		locTimer.start();
//	    } catch (IllegalThreadStateException itse) {
//		Logger.warn(TAG, itse.getLocalizedMessage());
//		itse.printStackTrace();
//	    } catch (Exception e) {
//		Logger.warn(TAG, "Unknown exception : " + e.getLocalizedMessage());
//		e.printStackTrace();
//	    }
//	}
//    }
//
//    @Override
//    protected void onPause() {
//	super.onPause();
//	if (mLocationManager != null) {
//	    mLocationManager.stopRequestingLocationUpdates();
//	}
//    }
//
//    @Override
//    protected void onDestroy() {
//	unregisterReceiver(logoutBroadcastReceiver);
//
//	super.onDestroy();
//    }
//
//    /**
//     * Initializes map fragment(if not exists) and adds current location's marker on it
//     */
//    private void updateMapFragment() {
//	if (mCurrentPosition != null) {
//	    if (mMapView == null) {
//		Logger.info("MAP_LOCATION", "updateMapFragment inside if mMapView is null");
//		mMapView = (MapView) findViewById(R.id.mapview);
//	    }
//
//	    mMapView.setBuiltInZoomControls(true);
//	    if (mCapture != null) {
//		mCapture.setVisibility(View.VISIBLE);
//	    }
//	    addCurrentLocationMarker();
//	}
//    }
//
//    /**
//     * Add current location marker to the map
//     */
//    private void addCurrentLocationMarker() {
//	if (mMapView != null) {
//	    List<Overlay> mapOverlays = mMapView.getOverlays();
//	    Drawable drawable = this.getResources().getDrawable(R.drawable.map_location_place);
//	    MapItemizedOverlay itemizedOverlay = new MapItemizedOverlay(drawable, this);
//	    OverlayItem overlayItem = new OverlayItem(mCurrentPosition, "Current Position", "Lat : "
//		    + mCurrentPosition.getLatitudeE6() + ", Lon : " + mCurrentPosition.getLongitudeE6());
//
//	    itemizedOverlay.addOverlay(overlayItem);
//	    mapOverlays.clear();
//	    mapOverlays.add(itemizedOverlay);
//
//	    MapController mc = mMapView.getController();
//	    mc.setCenter(mCurrentPosition);
//	    mc.setZoom(16);
//	}
//    }
//
//    /**
//     * Captures the current map view and creates a bitmap out of it
//     * 
//     * @return Bitmap, returns the current map view in the form of Bitmap
//     */
//    private Bitmap getMapImage() {
//	Bitmap resultBmp = null;
//	if (mMapView != null) {
//	    Logger.info(TAG, "getMapImage");
//	    /* Capture drawing cache as bitmap */
//	    mMapView.setDrawingCacheEnabled(true);
//	    Bitmap mapImage = Bitmap.createBitmap(mMapView.getDrawingCache());
//	    mMapView.setDrawingCacheEnabled(false);
//
//	    if (mapImage != null) {
//		SettingsDBUtil settingsDBUtil = new SettingsDBUtil(this);
//		int compressionQuality = Constants.DEFAULT_COMPRESSION_QUALITY;
//		compressionQuality = Util.convertStringToInt(
//			settingsDBUtil.getValue(Constants.KEY_IMAGE_COMPRESSION_QUALITY),
//			Constants.DEFAULT_COMPRESSION_QUALITY);
//		resultBmp = ImageUtil.getCompressedOpaqueImage(mapImage, compressionQuality);
//	    } else {
//		Logger.warn(TAG, "Map image is null");
//	    }
//	} else {
//	    Util.displayToast(GoogleMapsActivity.this, "Map view not loaded yet");
//	}
//	return resultBmp;
//    }
//
//    /**
//     * Listener class to handle Map Capture button click
//     * 
//     * @author Dileep | Paradigm Creatives
//     * 
//     */
//    private class MapCaptureListener implements OnClickListener {
//	@Override
//	public void onClick(View v) {
//	    Bitmap bmp = getMapImage();
//	    if (bmp != null) {
//		boolean isBitmapCreated = saveMapBitmap(bmp);
//		if (isBitmapCreated) {
//		    setResult(RESULT_OK);
//		    finish();
//		} else {
//		    Util.displayToast(GoogleMapsActivity.this, "Failed to save map snap shot");
//		}
//	    }
//	}
//    }
//
//    /**
//     * Listener class to handle Map Cancel button click
//     * 
//     * @author Dileep | Paradigm Creatives
//     * 
//     */
//    private class MapCancelListener implements OnClickListener {
//	@Override
//	public void onClick(View v) {
//	    // Nothing to perform, simply close the GoogleMapsActivity
//	    finish();
//	}
//    }
//
//    /**
//     * Saves the captured bitmap to the app directory
//     * 
//     * @param mapImage
//     *            bitmap of the captured mapview
//     * @return boolean, specifies whether bitmap got created or not
//     */
//    private boolean saveMapBitmap(Bitmap mapImage) {
//	boolean result = false;
//	if (DeviceInfoUtil.mediaWritable()) {
//	    String appRoot = AppPropertiesUtil.getAppDirectory(this);
//	    String tempFolder = getResources().getString(R.string.temp_folder);
//	    File tempFile = new File(appRoot, tempFolder);
//	    if (!tempFile.exists()) {
//		tempFile.mkdir();
//	    }
//
//	    String tempFolderPath = tempFile.getAbsolutePath();
//	    result = Util.saveImageToExternalMemory(tempFolderPath,
//		    this.getResources().getString(R.string.temp_map_bitmap), mapImage);
//	} else {
//	    Util.displayToast(this, this.getResources().getString(R.string.no_memory_card));
//	}
//
//	return result;
//    }
//
//    /**
//     * Compares two GeoPoint location values and returns true if both are same, otherwise returns false Also returns
//     * false if atleast one of the passed GeoPoint location object is null
//     * 
//     * @param currentLocation
//     *            GeoPoint object of the current location
//     * @param previousLocation
//     *            GeoPoint object of the previous location
//     * @return boolean, returns true if both current location GeoPoint and previous location GeoPoint is same
//     */
//    public boolean compareLocations(GeoPoint currentLocation, GeoPoint previousLocation) {
//	boolean isSame = false;
//	if (currentLocation != null && previousLocation != null) {
//	    if ((currentLocation.getLatitudeE6() == previousLocation.getLatitudeE6())
//		    || (currentLocation.getLongitudeE6() == previousLocation.getLongitudeE6())) {
//		isSame = true;
//	    }
//	}
//	return isSame;
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see com.neuv.doodlydoo.app.util.location.DoodlyDooLocationListener#onLocationReceived(android.location
//     * .Location)
//     */
//    @Override
//    public void onLocationReceived(Location location) {
//	receivedLocation(location);
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see com.neuv.doodlydoo.app.util.location.DoodlyDooLocationListener#onFailedToReceiveLocation(boolean)
//     */
//    @Override
//    public void onFailedToReceiveLocation(boolean isListeningToLocationUpdates) {
//	if (mLocationManager != null) {
//	    if (isListeningToLocationUpdates) {
//		mLocationManager.stopRequestingLocationUpdates();
//		// Util.displayToast(this, getString(R.string.location_retrieval_failure));
//		if (mMapView == null) {
//		    mMapView = (MapView) findViewById(R.id.mapview);
//		}
//	    }
//	}
//    }
//
//    /**
//     * Gracefully handles the unsuccessful wait for location
//     */
//    public void onLocationTimerFinished() {
//	if (mLocationManager != null) {
//	    mLocationManager.stopRequestingLocationUpdates();
//	    // Util.displayToast(this, getString(R.string.location_retrieval_failure));
//	    if (mMapView == null) {
//		mMapView = (MapView) findViewById(R.id.mapview);
//	    }
//	}
//    }
}
