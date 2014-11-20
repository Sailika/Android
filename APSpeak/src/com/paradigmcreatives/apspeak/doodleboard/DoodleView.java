package com.paradigmcreatives.apspeak.doodleboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.support.v4.view.GestureDetectorCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.database.SettingsDBUtil;
import com.paradigmcreatives.apspeak.app.util.CircularList;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.doodleboard.layers.Layer;
import com.paradigmcreatives.apspeak.doodleboard.layers.LayerControl;
import com.paradigmcreatives.apspeak.doodleboard.layers.LayerUtil;
import com.paradigmcreatives.apspeak.doodleboard.layers.TextLayer;
import com.paradigmcreatives.apspeak.doodleboard.layers.Layer.LayerType;
import com.paradigmcreatives.apspeak.doodleboard.layers.LayerControl.Gravity;
import com.paradigmcreatives.apspeak.doodleboard.layers.listeners.DeleteLayerListener;
import com.paradigmcreatives.apspeak.doodleboard.listeners.DoodleViewGestureListener;
import com.paradigmcreatives.apspeak.doodleboard.listeners.DoodleViewScaleListener;
import com.paradigmcreatives.apspeak.doodleboard.model.TextProperties;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * This class is used as helper class for viewing a doodle
 * 
 * @author
 * 
 */
public class DoodleView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = "DoodleView";

	private static final int POLLING_INTERVAL = 100;

	private ArrayList<Object> doodle;
	private PointF lastTouched;
	private PointF touched;
	private SurfaceHolder surfaceHolder;
	private Bitmap pictureBitmap;
	private Canvas pictureCanvas;
	private boolean isSurfaceInitialized = false;
	private DoodleViewBounds doodleViewBounds;
	private boolean isSurfacePaused = false;
	private DoodleViewGestureListener gestureListener;
	private GestureDetectorCompat gestureDetector;
	private ScaleGestureDetector mScaleDetector;
	// private RotationGestureDetector mRotationDetector;

	/*
	 * Declarations for layers.
	 * 
	 * Since these members would be used very often therefore to save the
	 * repetitive memory allocation they are being declared globally
	 */
	private ArrayList<Layer> bitmapLayers;
	private Paint mask;
	private int selectedLayerID = -1;
	private int previouslySelectedLayerID = -1;
	private Iterator<Layer> layersIterator;
	private Layer tempLayer;
	private Layer tempLayerForRotation;
	private int previousAction = -1;
	private CircularList<Integer> actions;
	private int tempLayerSelecedID = -1;
	private int layerToResize = -1;
	private FrameLayout doodleViewHolder;
	private volatile boolean rotateLayer = false;

	/**
	 * Number of previous touch actions to be stored in the circular list
	 */
	private static final int MAX_TRAILING_ACTIONS = 5;

	/**
	 * Properties of the canvas
	 */
	private DoodleViewProperties doodleViewProperties;

	/**
	 * String representing line break in a doodle
	 */
	public static final String LINE_BREAK = "LINE_BREAK";

	/**
	 * Paint object for drawing the dots
	 */
	private Paint brush;

	/**
	 * Width of the canvas
	 */
	private int width;

	/**
	 * Height of the canvas
	 */
	private int height;

	/**
	 * The background image
	 */
	private Bitmap backgroundImage;

	/**
	 * Thread for playing the doodle
	 */
	private DoodlePlay playDoodle;

	/**
	 * To resize the doodle while handling EDIT feature
	 */
	private DoodleResize resizeDoodle;

	/**
	 * Play states of the doodle play
	 * 
	 * @author robin
	 * 
	 */
	public enum PlayState {
		PLAYING, PAUSED, STOPPED, DRAW, KILLING, KILLED, REPLAY, BLOCKED, LAYERS
	}

	private Context context;

	// Current play state of the doodle view
	private volatile PlayState playState;

	// Previous Play state of the doodle view
	private volatile PlayState previousPlayState;

	/**
	 * Manages different brushes and colors on the Canvas UI as well as while
	 * drawing the doodle
	 */
	private PaintOptions paintOptions;

	private boolean isEditInitializationPending;

	private boolean istouch = true;

	public DoodleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		width = 0;
		height = 0;
		isEditInitializationPending = false;
		doodleViewBounds = null;
		doodleViewProperties = new DoodleViewProperties(width, height);
		doodle = new ArrayList<Object>();
		bitmapLayers = new ArrayList<Layer>();
		actions = new CircularList<Integer>(MAX_TRAILING_ACTIONS);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		gestureListener = new DoodleViewGestureListener(context, this);
		gestureDetector = new GestureDetectorCompat(context, gestureListener);
		mScaleDetector = new ScaleGestureDetector(context,
				new DoodleViewScaleListener(this));
		// mRotationDetector = new RotationGestureDetector(new
		// DoodleViewRotationListener(this));
		initBrush();
		initBackground();
		initLayers();
		setFocusable(true);
		setPlayState(PlayState.DRAW);
	}

	/**
	 * Initializes the foreground brush
	 */
	private void initBrush() {
		brush = new Paint();
		paintOptions = new PaintOptions(this);
		brush.setStrokeWidth(getStrokeSize());
		brush.setColor(getColor());
		brush.setStyle(Paint.Style.STROKE);
		brush.setStrokeJoin(Paint.Join.ROUND);
		brush.setStrokeCap(Paint.Cap.ROUND);
		brush.setAntiAlias(true);
	}

	private void resetBrush() {
		if (brush == null) {
			brush = new Paint();
		}

		brush.setStrokeWidth(Constants.DEFAULT_BRUSH_SIZE);
		brush.setColor(Util.returnColorValue(Constants.DEFAULT_BRUSH_COLOR));
	}

	/**
	 * Initializes the background brush
	 */
	private void initBackground() {
		backgroundImage = null;
		doodleViewProperties.setBackgroundColor(Color
				.parseColor(Constants.DEFAULT_BACKGROUND_COLOR));
	}

	/**
	 * Initializes the layers on the doodle view
	 */
	private void initLayers() {
		// Initialization
		doodleViewProperties.setBitmapLayers(bitmapLayers);

		// Init Mask
		mask = new Paint();
		mask.setColor(Color.parseColor(Constants.LAYER_SELECTION_MASK
				.get(LayerType.EMOJI)));
		mask.setAlpha(Constants.LAYER_SELECTION_ALPHA);
		mask.setAntiAlias(true);
	}

	/**
	 * 
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param type
	 * @param thumbnailURL
	 * @return
	 */
	public boolean addLayer(Bitmap bitmap, int x, int y, LayerType type,
			String thumbnailURL, TextProperties textProperties) {
		if (bitmap != null && x >= 0 && y >= 0) {
			Layer layer = new Layer(this, bitmap, x, y, type, thumbnailURL);
			if (type == LayerType.TEXT) {
				layer = new TextLayer(this, bitmap, x, y, thumbnailURL,
						textProperties);
			} else {
				layer = new Layer(this, bitmap, x, y, type, thumbnailURL);
			}

			/**
			 * Adding the cross button to the layer
			 */
			ImageButton cross = new ImageButton(context);
			cross.setBackgroundResource(R.drawable.delete_layer);
			cross.setOnClickListener(new DeleteLayerListener(this, context,
					layer.getID()));
			layer.addControl(new LayerControl(cross,
					LayerControl.Gravity.RIGHT, LayerControl.Gravity.TOP));

			bitmapLayers.add(layer);
			doodleViewProperties.setBitmapLayers(bitmapLayers);

			// Make the layer selected. Re-setting the touched point to null
			selectedLayerID = bitmapLayers.indexOf(layer);
			layerToResize = selectedLayerID;
			previouslySelectedLayerID = selectedLayerID;
			touched = null;

			setPlayState(PlayState.LAYERS);

			checkForRotationLayout();
			redrawSurface(doodleViewProperties);
			return true;
		}
		return false;
	}

	/**
	 * Adds a layer to the surface at the center location and refreshes
	 * 
	 * @param layer
	 * @return
	 */
	public boolean addLayer(Bitmap bitmap, LayerType type) {
		return addLayer(bitmap, type, null, null);
	}

	public boolean addTextLayer(Bitmap bitmap, TextProperties textProperties) {
		return addLayer(bitmap, LayerType.TEXT, null, textProperties);
	};

	public boolean addLayer(Layer layer) {
		if (layer.getBitmap() != null) {

			/**
			 * Adding the cross button to the layer
			 */
			ImageButton cross = new ImageButton(context);
			cross.setBackgroundResource(R.drawable.delete_layer);
			cross.setOnClickListener(new DeleteLayerListener(this, context,
					layer.getID()));
			layer.addControl(new LayerControl(cross,
					LayerControl.Gravity.RIGHT, LayerControl.Gravity.TOP));

			bitmapLayers.add(layer);
			doodleViewProperties.setBitmapLayers(bitmapLayers);

			// Make the layer selected. Re-setting the touched point to null
			selectedLayerID = bitmapLayers.indexOf(layer);
			layerToResize = selectedLayerID;
			previouslySelectedLayerID = selectedLayerID;
			touched = null;

			setPlayState(PlayState.LAYERS);
			checkForRotationLayout();
			redrawSurface(doodleViewProperties);

			return true;
		}
		return false;
	}

	/**
	 * Adds a layer to the surface and refreshes.
	 * 
	 * @param bitmap
	 * @param type
	 * @param thumbnailURL
	 *            The thumbnail URL of the image to be loaded. Mainly used in
	 *            the Asset Store and Image Search
	 * @return
	 */
	public boolean addLayer(Bitmap bitmap, LayerType type, String thumbnailURL,
			TextProperties textProperties) {
		// Get the coordinates for center of the screen
		if (context instanceof Activity) {
			PointF coord = LayerUtil.getCoordinatesForCenter(bitmap, width,
					height);
			return addLayer(bitmap, (int) coord.x, (int) coord.y, type,
					thumbnailURL, textProperties);

		} else {
			Logger.warn(TAG, "Context is not of Activity type");
		}

		return addLayer(bitmap, 0, 0, type, thumbnailURL, textProperties);

	}

	public void removeLayerWithID(int layerID) {
		if (bitmapLayers != null && layerID >= 0
				&& bitmapLayers.size() >= layerID) {
			removeControls(bitmapLayers.get(layerID));
			bitmapLayers.remove(layerID);
			selectedLayerID = -1;

			// If the removed layer was the last one then switch to draw mode
			if (bitmapLayers.isEmpty()) {
				resetPlayState();
				stopLayerSelectionAndRedraw();
			}

			redrawSurface(doodleViewProperties);
		}
	}

	/**
	 * Get the layer with the given thumbnailURL and replaces its bitmap with
	 * the given new bitmap along with updating other state values of the layer
	 * 
	 * @param thumbnailURL
	 * @param newBitmap
	 */
	public void replaceBitmapOfLayer(String thumbnailURL, Bitmap newBitmap) {
		if (!TextUtils.isEmpty(thumbnailURL) && newBitmap != null) {
			// Get the layer with the given thumbnail URL
			Iterator<Layer> iterator = bitmapLayers.iterator();
			Layer layer = null;
			while (iterator.hasNext()) {
				layer = iterator.next();
				if (thumbnailURL.equals(layer.getThumbnailURL())) {
					// Replace the bitmap
					layer.setBitmap(newBitmap);
					layer.setOrigHeight(newBitmap.getHeight());
					layer.setOrigWidth(newBitmap.getWidth());

					// Resize the layer to suit the current dimension
					float scaleFactor = (float) layer.getWidth()
							/ (float) layer.getOrigWidth();

					layer.setScale(scaleFactor);
					PointF transformedXY = layer.getTransformedXY();
					layer.getMatrix().postScale(scaleFactor, scaleFactor,
							transformedXY.x, transformedXY.y);

					if (doodleViewProperties != null) {
						redrawSurface(doodleViewProperties);
					}
				}
			}

		} else {
			Logger.warn(
					TAG,
					"Empty thumbnail url sent to update the bitmap or bitmap itself is null. Not doing it.");
		}

	}

	/**
	 * Get the layer with the given thumbnailURL and replaces its bitmap with
	 * the given new bitmap along with updating other state values of the layer
	 * 
	 * @param thumbnailURL
	 * @param newBitmap
	 */
	public void replaceBitmapOfLayerViaID(String layerID, Bitmap newBitmap) {
		if (!TextUtils.isEmpty(layerID) && newBitmap != null) {
			// Get the layer with the given thumbnail URL
			Iterator<Layer> iterator = bitmapLayers.iterator();
			Layer layer = null;
			while (iterator.hasNext()) {
				layer = iterator.next();
				if (layerID.equals(layer.getID())) {
					// Recycle the original bitmap
					Bitmap bmp = layer.getBitmap();
					bmp.recycle();

					// Replace the bitmap
					layer.setBitmap(newBitmap);
					layer.setOrigHeight(newBitmap.getHeight());
					layer.setOrigWidth(newBitmap.getWidth());

					// Reset the dimensions for the layer
					layer.setHeight(newBitmap.getHeight());
					layer.setWidth(newBitmap.getWidth());

					// Resize the layer to suit the current dimension
					float scaleFactor = (float) layer.getWidth()
							/ (float) layer.getOrigWidth();

					layer.setScale(scaleFactor);
					PointF transformedXY = layer.getTransformedXY();
					layer.getMatrix().postScale(scaleFactor, scaleFactor,
							transformedXY.x, transformedXY.y);

					if (doodleViewProperties != null) {
						redrawSurface(doodleViewProperties);
					}
				}
			}

		} else {
			Logger.warn(TAG, "Empty bitmap sent to be replaced");
		}

	}

	public void resetToDraw() {
		resetPlayState();
		initBrush();
		initBackground();
		if (width > 0 && height > 0) {
			doodleViewProperties.setScreenHeight(height);
			doodleViewProperties.setScreenWidth(width);
		} else {
			Logger.warn(TAG, "width x height : " + width + "x" + height);
		}
		RectF boundRect = new RectF(0, 0,
				doodleViewProperties.getScreenWidth(),
				doodleViewProperties.getScreenHeight());
		doodleViewBounds = new DoodleViewBounds(boundRect, 1.0f);

		Log.v(TAG, "doodle properties : " + doodleViewProperties.toString()
				+ ", doodle bounds : " + doodleViewBounds.toString());
		// Reset the surface
		clear();
	}

	/**
	 * Resets DoodleView to edit doodle
	 */
	public void resetToEdit() {
		resetPlayState();
		initBrush();
		// Add brush color and size to model
		if (doodle != null) {
			String hexColor = Util.getHexStringForColor(Util
					.returnColorValue(Constants.DEFAULT_BRUSH_COLOR));
			doodle.add(new StrokeBean(hexColor));
			doodle.add(new StrokeBean(Constants.DEFAULT_BRUSH_SIZE));
		}
	}

	/**
	 * This method is used to reset the play state
	 */
	public void resetPlayState() {
		killDoodlePlay();
		// Update various states and values
		setPlayState(PlayState.DRAW);
	}

	public void stopLayerSelectionAndRedraw() {
		touched = null;
		lastTouched = null;
		selectedLayerID = -1;
		// TODO Hide rotation controls
		// ((CanvasActivity) context).hideRotationControls();
		redrawSurface(doodleViewProperties);

	}

	public void stopDoodle() {
		if (playDoodle != null) {
			playDoodle.stopDoodle();
		}
		if (resizeDoodle != null) {
			resizeDoodle.stopDoodle();
		}
	}

	public void pauseDoodle() {
		if (playDoodle != null) {
			playDoodle.pauseDoodle();
		}
		if (resizeDoodle != null) {
			resizeDoodle.pauseDoodle();
		}
	}

	public void resumeDoodle() {
		if (!isSurfaceInitialized && isSurfacePaused) {
			isSurfacePaused = false;
		} else {
			if (playDoodle != null) {
				playDoodle.resumeDoodle();
			}
		}
	}

	/**
	 * Calculates the scaling bounds of the doodle in the current device.
	 */
	protected void calculateDoodleBounds() {
		doodleViewBounds = null;
		if (doodleViewProperties != null) {
			int doodleWidth = doodleViewProperties.getScreenWidth();
			int doodleHeight = doodleViewProperties.getScreenHeight();
			Logger.info(TAG, "doodle width : " + doodleWidth
					+ " doodle height : " + doodleHeight);
			if ((width > 0) && (height > 0) && (doodleWidth > 0)
					&& (doodleHeight > 0)) {
				if ((width == doodleViewProperties.getScreenWidth())
						&& (height == doodleViewProperties.getScreenHeight())) {
					RectF boundRect = new RectF(0, 0, width, height);
					doodleViewBounds = new DoodleViewBounds(boundRect, 1.0f);
				} else {
					float screenRatio = (float) width / (float) height;
					float doodleRatio = (float) doodleWidth
							/ (float) doodleHeight;
					float scaleFactor;

					if (doodleRatio < screenRatio) {
						scaleFactor = (float) height / (float) doodleHeight;
					} else {
						scaleFactor = (float) width / (float) doodleWidth;
					}

					float scaledWidth = scaleFactor * doodleWidth;
					float scaledHeight = scaleFactor * doodleHeight;

					float left = (width - scaledWidth) / 2;
					float top = (height - scaledHeight) / 2;
					float right = width - left;
					float bottom = height - top;

					RectF boundRect = new RectF(left, top, right, bottom);
					doodleViewBounds = new DoodleViewBounds(boundRect,
							scaleFactor);
				}
			}
		}

		if (doodleViewBounds == null) {
			Logger.warn(TAG, "Doodle bounds calculated at wrong time");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder
	 * , int, int, int)
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int surWidth,
			int surHeight) {
		if (isEditInitializationPending) {
			return;
		}
		width = surWidth;
		height = surHeight;

		if (pictureBitmap == null) {
			try {
				pictureBitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_8888);
			} catch (IllegalArgumentException iae) {
				Logger.fatal(TAG,
						"Illegal Arguments while creating the surface - " + iae);
			} catch (Exception e) {
				Logger.fatal(TAG,
						"Unknown exception while creating the surface - " + e);
			}
		}

		if (pictureBitmap != null) {
			pictureCanvas = new Canvas(pictureBitmap);

			// Initialize the properties if its not initialized already
			if (doodleViewProperties == null) {
				doodleViewProperties = new DoodleViewProperties(width, height,
						Color.parseColor(Constants.DEFAULT_BACKGROUND_COLOR),
						backgroundImage, bitmapLayers);
			} else {
				if (playState == PlayState.DRAW
						|| playState == PlayState.LAYERS) {
					doodleViewProperties.setScreenHeight(height);
					doodleViewProperties.setScreenWidth(width);
				}
			}
			calculateDoodleBounds();

			if (playState != PlayState.BLOCKED && playState != PlayState.PAUSED) {
				// Clear the surface
				clearSurface();
				// Since surface has changed, therefore re-draw the doodle again
				// with the properties
				redrawSurface(doodleViewProperties);
			}
		}

		// Since surface has changed therefore update the canvas
		updateCanvas();
		synchronized (holder) {
			isSurfaceInitialized = true;
		}

		synchronized (holder) {
			if (!isSurfacePaused && playState == PlayState.PAUSED) {
				setPlayState(PlayState.PLAYING);
			}
		}
	}

	/**
	 * Launches the paint options
	 */
	protected void launchPaintOptions() {
		if (context != null && paintOptions != null) {
			paintOptions.brushSizeAndColorDialog(context).show();
			resetPlayState();
			stopLayerSelectionAndRedraw();
		} else {
			Logger.warn(TAG, "Context or paint options is null");
		}
	}

	/**
	 * As the name sez ;-)
	 * 
	 * @param doodleViewProperties
	 */
	public void redrawSurface(DoodleViewProperties doodleViewProperties) {
		redrawSurface(doodleViewProperties, false);
	}

	private void redrawSurface(DoodleViewProperties doodleViewProperties,
			boolean selectAllLayers) {
		resetBrush();
		// 1 - Set the background color
		if (pictureCanvas != null) {
			try {
				pictureCanvas.drawColor(doodleViewProperties
						.getBackgroundColor());
			} catch (IllegalArgumentException e) {
				Logger.warn(TAG, e + " ");
			} catch (Exception e) {
				Logger.warn(TAG, e + " ");
			}
		}

		// 2 - Set the background image
		if (doodleViewProperties.getBackgroundBitmap() != null) {
			drawBackgroundImage(doodleViewProperties.getBackgroundBitmap());
		}

		// 3 - Draw all the layers
		drawLayers(selectAllLayers);

		// 4 - Draw the doodle
		drawAllPoint();

	}

	/**
	 * Draw all the layers on the screen
	 * 
	 * @param showAllSelected
	 *            : If its <code>true</code> then all the layers are shown in
	 *            selected mode
	 */
	public void drawLayers(boolean showAllSelected) {
		if (pictureCanvas != null) {
			layersIterator = bitmapLayers.iterator();
			while (layersIterator.hasNext()) {
				tempLayer = layersIterator.next();
				if (tempLayer.getBitmap() != null) {
					// pictureCanvas.drawBitmap(tempLayer.getBitmap(),
					// tempLayer.x, tempLayer.y, brush);
					removeControls(tempLayer);
					pictureCanvas.drawBitmap(tempLayer.getBitmap(),
							tempLayer.getMatrix(), brush);
					pictureCanvas.save();
					pictureCanvas.setMatrix(tempLayer.getMaskMatrix());
					if (!tempLayer.getType().equals(LayerType.FRAME)) {
						// If a layer is selected then draw a mask on top of it,
						// other than a FRAME
						mask.setColor(Color
								.parseColor(Constants.LAYER_SELECTION_MASK
										.get(tempLayer.getType())));
						mask.setAlpha(Constants.LAYER_SELECTION_ALPHA);
						if (selectedLayerID >= 0
								&& tempLayer.equals(bitmapLayers
										.get(selectedLayerID))) {
							pictureCanvas.drawRect(getRectForLayer(tempLayer),
									mask);
							drawControls(tempLayer);
						} else if (showAllSelected) { // Show all the layers in
														// selected mode.
							pictureCanvas.drawRect(getRectForLayer(tempLayer),
									mask);
							drawControls(tempLayer);
						}
						pictureCanvas.restore();
					}
				}
			}
		}

	}

	/**
	 * Remove all the controls available in the given layer
	 * 
	 * @param layer
	 */
	private void removeControls(Layer layer) {
		if (layer != null) {
			List<LayerControl> controls = layer.getControls();
			if (controls != null && !controls.isEmpty()) {
				for (final LayerControl control : controls) {

					// Remove rotation control
					if (doodleViewHolder != null
							&& context instanceof ImageSelectionFragmentActivity) {
						((ImageSelectionFragmentActivity) context)
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// If this view is already attached to
										// the parent then
										// remove it first and then add it
										// again with the updated coordinates
										doodleViewHolder
												.removeViewInLayout(control
														.getView());
										doodleViewHolder.invalidate();
									}

								});
					}
				}
			}

		}
	}

	private void drawControls(Layer layer) {
		if (layer != null) {
			List<LayerControl> controls = layer.getControls();
			if (controls != null && !controls.isEmpty()) {
				for (final LayerControl control : controls) {
					final PointF loc = layer.getXY(control.getGravityX(),
							control.getGravityY());
					final int height = control.getView().getBackground()
							.getMinimumHeight();
					final int width = control.getView().getBackground()
							.getMinimumWidth();

					// TODO draw controls update ui
					if (doodleViewHolder != null
							&& context instanceof ImageSelectionFragmentActivity) {
						((ImageSelectionFragmentActivity) context)
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
												LayoutParams.WRAP_CONTENT,
												LayoutParams.WRAP_CONTENT);
										params.setMargins((int) loc.x
												- (width / 2), (int) loc.y
												- (height / 2), 0, 0);

										// If this view is already attached to
										// the parent then remove it first and
										// then add it
										// again with the updated coordinates
										doodleViewHolder
												.removeViewInLayout(control
														.getView());
										doodleViewHolder.addView(
												control.getView(), params);
										// ((CanvasActivity) context)
										// .bringCanvasLayoutToFront();

									}

								});
					}
				}
			}

		}
	}

	/**
	 * Select all the layers and redraw the surface
	 */
	public boolean selectAllLayers() {
		if (bitmapLayers != null && !bitmapLayers.isEmpty()) {
			redrawSurface(doodleViewProperties, true);
			return true;
		}
		return false;
	}

	/**
	 * Returns a bounding rectangle for the given layer
	 * 
	 * @param layer
	 * @return
	 */
	private RectF getRectForLayer(Layer layer) {
		RectF rect = null;
		if (layer != null && layer.getBitmap() != null) {
			rect = new RectF(layer.x, layer.y, layer.x + layer.getWidth(),
					layer.y + layer.getHeight());
		}

		return rect;
	}

	/**
	 * Draws the background image into the canvas
	 * 
	 * @param backgroundBitmap
	 *            : the background image
	 */
	protected void drawBackgroundImage(Bitmap backgroundBitmap) {
		if (pictureCanvas != null) {
			try {
				int picScaledWidth = (int) (backgroundBitmap.getWidth() * doodleViewBounds
						.getScaleFactor());
				int picScaledHeight = (int) (backgroundBitmap.getHeight() * doodleViewBounds
						.getScaleFactor());
				RectF screenRect = ImageUtil.getPictureRectFWithAspectFit(
						picScaledWidth, picScaledHeight,
						doodleViewBounds.getBoundRect());

				pictureCanvas.drawBitmap(backgroundBitmap, null, screenRect,
						null);

				// Update the doodle view property with the resized bitmap
				doodleViewProperties.setBackgroundBitmap(ImageUtil
						.resizeBitmap(backgroundBitmap, screenRect));

				// Commented this to stop flickering of the image
				// updateCanvas();
			} catch (IllegalArgumentException e) {
				Logger.warn(TAG, e.getLocalizedMessage());
			} catch (Exception e) {
				Logger.warn(TAG, e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Draw all the points in the doodle model
	 */
	private void drawAllPoint() {
		if (doodle != null) {
			Iterator<Object> iterator = doodle.iterator();
			PointF touched = null;
			PointF lastTouched = null;
			StrokeBean stroke = null;
			Object object = null;
			while (iterator.hasNext()) {
				object = iterator.next();
				if (object instanceof StrokeBean) { // Item has stroke data
					stroke = (StrokeBean) object;

					// If the stroke has color data change the brush color
					if (stroke.getColor() != null) {
						brush.setColor(Color.parseColor("#" + stroke.getColor()));
					}

					// If the stroke has size data change the brush size
					if (stroke.getSize() > 0) {
						brush.setStrokeWidth(stroke.getSize());
					}
				} else if (object instanceof PointF) { // Item has point data
					touched = (PointF) object;
					// Draw the point
					doDraw(touched, lastTouched);

					// Update last touched
					lastTouched = touched;
				} else if (object instanceof String) { // Item has LINE_BREAK
					// Its a line break. Update last touched so that a point is
					// drawn
					lastTouched = null;
				}
			}
			updateCanvas();
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		ViewParent parent = getParent();
		if (parent != null && parent instanceof FrameLayout) {
			doodleViewHolder = (FrameLayout) parent;
		}

	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		synchronized (holder) {
			if (playState == PlayState.PAUSED) {
				isSurfacePaused = true;
			}
			isSurfaceInitialized = false;
		}
	}

	/**
	 * Checks if canvas is empty or not
	 * 
	 * @return <code>true</code> if the canvas is empty, else <code>false</code>
	 */
	public boolean isCanvasEmpty() {
		boolean isEmpty = true;
		// Since the first two entries of doodle array are reserved for brush
		// color and size, there if number of entries
		// are less than or equal to two, it means doodle is empty

		if (backgroundImage != null) {
			return false;
		}

		if (bitmapLayers != null && bitmapLayers.size() > 0) {
			return false;
		}

		isEmpty = isDoodleEmpty();
		return isEmpty;
	}

	public boolean isDoodleEmpty() {
		boolean isEmpty = true;
		if (doodle != null) {
			Iterator<Object> iterator = doodle.iterator();
			while (iterator.hasNext()) {
				if (iterator.next() instanceof PointF) {
					isEmpty = false;
					break;
				}
			}

		}
		return isEmpty;
	}

	/**
	 * Standard override to get touch events.DoodlePlay
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// If Play state is layers then handle the layer events
		if (playState == PlayState.LAYERS) {
			// mScaleDetector.onTouchEvent(event);
			// mRotationDetector.onTouchEvent(event);
			handleLayerTouches(event);
			return gestureDetector.onTouchEvent(event);
		}
		if (istouch && context != null) {
			istouch = false;
			/*
			 * GoogleAnalytics.sendEventTrackingInfoToGA((Activity)context,
			 * GoogleAnalyticsConstants.CANVAS_SCREEN_CAT_NAME,
			 * GoogleAnalyticsConstants
			 * .EVENT_ACTION_IMAGE,GoogleAnalyticsConstants.CANVAS_DOODLE_VIEW);
			 */
		}

		// Fire the Doodle View touched callback if the context is of Canvas
		// Activity
		if (playState == PlayState.DRAW) {
			if (event.getAction() == MotionEvent.ACTION_DOWN
					|| event.getAction() == MotionEvent.ACTION_MOVE) {
				didStartDraw();

				brush.setColor(getColor());
				brush.setStrokeWidth(getStrokeSize());

				float x = event.getX();
				float y = event.getY();
				touched = new PointF(x, y);
				doodle.add(touched);

				if (touched != null && lastTouched != null) {
					doDraw(touched, lastTouched);
					updateCanvas();
					// redrawSurface(doodleViewProperties);
				}

				// Update last touched point
				lastTouched = new PointF(x, y);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				// Check for POINT and draw it
				if ((touched.x == lastTouched.x)
						&& (touched.y == lastTouched.y)) {
					// Passing null as lastTouched will trigger to draw POINT on
					// canvas
					doDraw(touched, null);
					updateCanvas();
				}

				// if the motion event is up give the break for drawing
				lastTouched = null;
				// ///////////
				// TODO : checking dots issue
				touched = null;
				// float x = event.getX();
				// float y = event.getY();
				// touched = new PointF(x, y);
				// if (touched != null) {
				// doDraw(touched, null);
				// doodle.add(touched);
				// }
				// //////////////
				doodle.add(LINE_BREAK);
				didEndDraw();
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			if (playState != null && playState != PlayState.BLOCKED) {
				if (playState != PlayState.REPLAY
						&& playState != PlayState.PLAYING
						&& playState != PlayState.PAUSED) {
					toggleOptionsAfterPlay();
				} else {
					if (shouldPauseResume()) {
						toggleDoodlePauseResume();
					}
				}
			}
			setRotateLayer(false);
		}
		return true;
	}

	private void handleLayerTouches(MotionEvent event) {
		if ((event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE)
				&& event.getPointerCount() == 1) {
			if (touched == null) {
				touched = new PointF(event.getX(), event.getY());
			}
			lastTouched = new PointF(touched.x, touched.y);
			touched = new PointF(event.getX(), event.getY());

			// Get the new selection only if all the layers are unlocked or if
			// its a fresh action down
			if (selectedLayerID == -1
					|| event.getActionMasked() == MotionEvent.ACTION_DOWN) {
				previouslySelectedLayerID = selectedLayerID;
				selectedLayerID = getIntersectingBitmapID(bitmapLayers, touched);
				layerToResize = (selectedLayerID > -1) ? selectedLayerID
						: previouslySelectedLayerID;
				lastTouched = new PointF(touched.x, touched.y);
			}
			if (selectedLayerID >= 0) { // Move a layer only if one is selected
										// and
				// there is only single touch on the screen
				moveLayer(bitmapLayers.get(selectedLayerID));
			}

		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			// If DOWN happened in the previous limited set of actions then
			// select the layer
			if (actions.contains(MotionEvent.ACTION_DOWN)) {
				// If its a layer that has been tapped on then keep it selected
				// else unselect
				tempLayerSelecedID = getIntersectingBitmapID(bitmapLayers,
						touched);
				if (tempLayerSelecedID == previouslySelectedLayerID
						&& previouslySelectedLayerID > -1) {
					selectedLayerID = -1;
				} else {
					previouslySelectedLayerID = selectedLayerID;
					selectedLayerID = tempLayerSelecedID;
				}
			} else if (previousAction == MotionEvent.ACTION_MOVE) {
				previouslySelectedLayerID = selectedLayerID;
				selectedLayerID = -1;
			}

			touched = null;
			lastTouched = null;
			mScaleDetector = new ScaleGestureDetector(context,
					new DoodleViewScaleListener(this));
		}
		checkForRotationLayout();
		redrawSurface(doodleViewProperties);
		previousAction = event.getActionMasked();
		actions.add(event.getActionMasked());
	}

	private void checkForRotationLayout() {
		// TODO Check for rotation layout impl
		// ((CanvasActivity) context).runOnUiThread(new Runnable() {
		// public void run() {
		// if (selectedLayerID >= 0) {
		// // ((CanvasActivity) context).showRotationControls();
		// } else {
		// // ((CanvasActivity) context).hideRotationControls();
		// }
		// }
		// });
		//
	}

	private void moveLayer(Layer layer) {
		// If a FRAME is added as a layer, it is not movable
		if (layer.getType().equals(LayerType.FRAME)) {
			return;
		}

		// Perform movement only if pointer up didn't happen in the previous
		// actions buffer
		if (!actions.contains(MotionEvent.ACTION_POINTER_UP)) {
			float dx = touched.x - lastTouched.x;
			float dy = touched.y - lastTouched.y;

			// layer.x += dx;
			// layer.y += dy;

			layer.getMatrix().postTranslate(dx, dy);
			layer.getMaskMatrix().postTranslate(dx, dy);
		}
	}

	public void handleRotationOfLayer(float rotateBy) {
		if (layerToResize >= 0 && layerToResize < bitmapLayers.size()) {
			// Select the layer for visual cue
			previouslySelectedLayerID = selectedLayerID;
			selectedLayerID = layerToResize;

			// Get the layer
			tempLayerForRotation = bitmapLayers.get(selectedLayerID);

			// Get the centre point of the layer
			// float centreX = tempLayerForRotation.getX() + (float)
			// (tempLayerForRotation.getWidth() / 2.0f);
			// float centreY = tempLayerForRotation.getY() + (float)
			// (tempLayerForRotation.getHeight() / 2.0f);
			PointF pivot = tempLayerForRotation.getXY(Gravity.CENTER,
					Gravity.CENTER);

			// Rotate
			tempLayerForRotation.getMatrix().postRotate(rotateBy, pivot.x,
					pivot.y);
			tempLayerForRotation.getMaskMatrix().postRotate(rotateBy, pivot.x,
					pivot.y);

			tempLayerForRotation.setAngle(rotateBy);
		}
		redrawSurface(doodleViewProperties);
	}

	public void handleScalingOfLayer(float scaleFactor, PointF focus) {
		if (layerToResize >= 0 && layerToResize < bitmapLayers.size()) {
			// Select the layer for visual cue
			previouslySelectedLayerID = selectedLayerID;
			selectedLayerID = layerToResize;
			// Get the layer
			tempLayer = bitmapLayers.get(selectedLayerID);

			// Restrict the scaling if it goes beyond the permissible limit
			// Permissible limit depends on the size of the image v/s size of
			// the screen
			float newScaleFactor = tempLayer.getScale() * scaleFactor;
			int newHeight = (int) (tempLayer.getOrigHeight() * newScaleFactor);
			int newWidth = (int) (tempLayer.getOrigWidth() * newScaleFactor);

			if (height > 0 && width > 0) { // If there is a valid screen width
											// and height then apply formula
											// based on
				// them
				float widthRatio = (float) newWidth / (float) width;
				float heightRatio = (float) newHeight / (float) height;
				if (widthRatio > Constants.MAX_SCALE
						|| widthRatio < Constants.MIN_SCALE
						|| heightRatio > Constants.MAX_SCALE
						|| heightRatio < Constants.MIN_SCALE) {
					return;
				}
			} else if (newScaleFactor >= Constants.MAX_SCALE
					|| newScaleFactor <= Constants.MIN_SCALE) { // otherwise,
				// use an
				// alternate
				// way
				return;
			}

			tempLayer.setScale(newScaleFactor);
			tempLayer.getMatrix().postScale(scaleFactor, scaleFactor, focus.x,
					focus.y);
			tempLayer.getMaskMatrix().postScale(scaleFactor, scaleFactor,
					focus.x, focus.y);

			// if (tempLayer.getScale() >= 1) {
			// tempLayer.x = tempLayer.x + (tempLayer.x - focus.x) *
			// (scaleFactor - 1);
			// tempLayer.y = tempLayer.y + (tempLayer.y - focus.y) *
			// (scaleFactor - 1);
			// } else {
			// tempLayer.x = tempLayer.x - (tempLayer.x - focus.x) * (1 -
			// scaleFactor);
			// tempLayer.y = tempLayer.y - (tempLayer.y - focus.y) * (1 -
			// scaleFactor);
			// }
			//
			// tempLayer.setHeight(newHeight);
			// tempLayer.setWidth(newWidth);
		}

	}

	/**
	 * Returns the index of the bitmap on which the given point lies. If there
	 * is more than one <code>Bitmap</code> then the highest ID is returned
	 * (assuming the highest ID would be having the max Z-order position)
	 * 
	 * @param layers
	 * @param x
	 * @param y
	 * @return
	 */
	private int getIntersectingBitmapID(ArrayList<Layer> layers, PointF point) {
		int id = -1;
		Iterator<Layer> iterator = layers.iterator();
		Layer layer = null;

		while (iterator.hasNext()) {
			layer = iterator.next();
			if (layer != null && layer.getBitmap() != null) {
				if (layer.contains(point)) {
					id = layers.indexOf(layer);
				}
			}
		}

		return id;
	}

	private void didEndDraw() {
		showCanvasOptionsButton();
		showSendButton();
	}

	/**
	 * This method actually draws the doodle on the canvas
	 * 
	 * @param touched
	 *            :Touched Point
	 * @param lastTouched
	 *            :Last Touched Point
	 */
	protected void doDraw(PointF touched, PointF lastTouched) {
		float lastTouchedX = 0;
		float lastTouchedY = 0;
		float touchedX = 0;
		float touchedY = 0;
		try {
			if (pictureCanvas == null) {
				if (pictureBitmap == null) {
					pictureBitmap = Bitmap.createBitmap(width, height,
							Bitmap.Config.ARGB_8888);
				}
				pictureCanvas = new Canvas(pictureBitmap);
			}

			if (lastTouched != null && touched != null) {
				// If the doodle is supposed to be played then check the
				// properties and scale the drawing to fit the
				// screen size
				if (doodleViewProperties != null) {
					if (doodleViewProperties.getScreenWidth() != 0
							&& doodleViewProperties.getScreenHeight() != 0) {
						RectF bounds = doodleViewBounds.getBoundRect();
						lastTouchedX = bounds.left
								+ lastTouched.x
								* (bounds.width() / (float) doodleViewProperties
										.getScreenWidth());
						lastTouchedY = bounds.top
								+ lastTouched.y
								* (bounds.height() / (float) doodleViewProperties
										.getScreenHeight());
						touchedX = bounds.left
								+ touched.x
								* (bounds.width() / (float) doodleViewProperties
										.getScreenWidth());
						touchedY = bounds.top
								+ touched.y
								* (bounds.height() / (float) doodleViewProperties
										.getScreenHeight());
					} // else do nothing
				} else {
					lastTouchedX = lastTouched.x;
					lastTouchedY = lastTouched.y;
					touchedX = touched.x;
					touchedY = touched.y;
				}
				pictureCanvas.drawLine(lastTouchedX, lastTouchedY, touchedX,
						touchedY, brush);
			} else if (touched != null) { // Just one point is touched, draw a
											// point
				// TODO : checking dots issue
				pictureCanvas.drawPoint(touched.x, touched.y, brush);
			}
		} catch (IllegalArgumentException iae) {
			Logger.warn(TAG, "Could not draw. Illegal Argument received - "
					+ iae);
		} catch (Exception e) {
			Logger.warn(TAG, "Could not draw. Exception received - " + e);
		}
	}

	/**
	 * This method updates the canvas synchronously
	 */
	protected void updateCanvas() {
		Canvas c = null;
		try {
			if (surfaceHolder == null) {
				return;
			}
			c = surfaceHolder.lockCanvas(null);
			synchronized (surfaceHolder) {
				if (c != null) {
					c.drawColor(doodleViewProperties.getBackgroundColor());
					c.drawBitmap(pictureBitmap, 0, 0, brush);
				}
			}
		} catch (Exception e) {
			Logger.warn(TAG, "Exception occurred while updating the canvas - "
					+ e);
		} finally {
			if (c != null) {
				surfaceHolder.unlockCanvasAndPost(c);
			}
		}
	}

	/**
	 * This method returns the current screenshot as a bitmap
	 * 
	 * @return Bitmap for the current screen
	 */
	public Bitmap getDoodleBitmap() {
		Bitmap doodleImage = null;
		try {
			doodleImage = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas c = new Canvas(doodleImage);
			if (c != null) {
				c.drawColor(Color.WHITE);
				c.drawBitmap(pictureBitmap, 0, 0, brush);
				c.save();
			}
		} catch (IllegalArgumentException e) {
			Logger.warn(TAG, "Can't create the doodle bitmap - " + e);
		} catch (Exception e) {
			Logger.warn(TAG, "Can't create the doodle bitmap - " + e);
		}

		return doodleImage;
	}

	/**
	 * This method clears the canvas
	 */
	protected void clear() {
		clearModel();
		clearSurface();
	}

	protected void clearSurface() {
		if (pictureCanvas != null) {
			// Clear the screen
			pictureCanvas.drawColor(doodleViewProperties.getBackgroundColor(),
					Mode.CLEAR);
			updateCanvas();
		}
	}

	private void killDoodlePlay() {
		// Kill the playing doodle
		if ((playDoodle != null || resizeDoodle != null)
				&& playState == PlayState.PLAYING) {
			if (playDoodle != null) {
				playDoodle.kill();
			}

			if (resizeDoodle != null) {
				resizeDoodle.kill();
			}

			// Wait for the kill
			while (playState != PlayState.KILLED) {
				try {
					Thread.sleep(POLLING_INTERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Undo the last few steps
	 */
	protected void undo() {
		if (!isCanvasLocked() && (doodle != null)) {
			boolean isLineBreak = false;
			int index = doodle.size();
			boolean startRemoving = false;
			try {
				do {
					if (doodle.size() > 0) {
						Object object = doodle.get(index - 1);
						if (object instanceof PointF) {
							if (startRemoving) {
								doodle.remove(index - 1);
							}
						} else if (object instanceof String) { // Item has
																// LINE_BREAK
							// Its a line break. Update last touched so that a
							// point is drawn
							String objectString = (String) object;
							if (objectString.equals(DoodleView.LINE_BREAK)) {
								if (startRemoving) {
									isLineBreak = true;
								} else {
									startRemoving = true;
									doodle.remove(index - 1);
								}
							}
						} else if (object instanceof StrokeBean) {
							Logger.warn(TAG, "stroke bean during undo");
						}

						index--;
					}
				} while ((index > 0) && !isLineBreak);

				redrawSurface(doodleViewProperties);
			} catch (IndexOutOfBoundsException iobe) {
				Logger.warn(TAG, iobe.getLocalizedMessage());
			} catch (Exception e) {
				Logger.warn(
						TAG,
						"Unknown error during removing doodle point : "
								+ e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Clears the background image and canvas
	 */
	protected void clearBackground() {
		clearBackgroundImage();
		doodleViewProperties.setBackgroundColor(Color
				.parseColor(Constants.DEFAULT_BACKGROUND_COLOR));
		redrawSurface(doodleViewProperties);
	}

	/**
	 * Clears the drawn doodle
	 */
	protected void clearDoodle() {
		// Clear the doodle points model
		clearDoodlePoints();
		redrawSurface(doodleViewProperties);
	}

	/**
	 * Clears the doodle points from the array
	 */
	private void clearDoodlePoints() {
		// Clear the doodle points model
		if (doodle != null) {
			doodle.clear();

			// Update the brush color model
			String hexColor = Integer.toHexString(getColor());
			hexColor = hexColor.substring(2, hexColor.length());
			doodle.add(new StrokeBean(hexColor));

			// Update the brush size model
			doodle.add(new StrokeBean(getStrokeSize()));
		}
	}

	public void clearAddOns() {
		if (bitmapLayers != null && !bitmapLayers.isEmpty()) {
			Iterator<Layer> iterator = bitmapLayers.iterator();
			while (iterator.hasNext()) {
				tempLayer = iterator.next();
				if (tempLayer.getBitmap() != null) {
					tempLayer.getBitmap().recycle();
				}
			}

			// Empty the bitmap layers
			bitmapLayers.clear();
			bitmapLayers.trimToSize();
			redrawSurface(doodleViewProperties);
			resetPlayState();
			stopLayerSelectionAndRedraw();
		}
	}

	/**
	 * Clears the complete model
	 */
	private void clearModel() {
		// Clear the doodle view background image, doodle points, add-ons and
		// color model
		clearDoodlePoints();
		clearBackgroundImage();
		clearAddOns();
		doodleViewProperties.setBackgroundColor(Color
				.parseColor(Constants.DEFAULT_BACKGROUND_COLOR));
	}

	/**
	 * Removes the background image
	 */
	private void clearBackgroundImage() {
		if (backgroundImage != null) {
			backgroundImage.recycle();
		}
		backgroundImage = null;
		doodleViewProperties.setBackgroundBitmap(null);
	}

	/**
	 * This method helps in playing the doodle
	 * 
	 * @param doodle
	 *            : Doodle data
	 */
	public void play(ArrayList<Object> doodle,
			DoodleViewProperties doodleViewProperties) {
		// If a doodle is already being played then display warning and return
		if (playState == PlayState.REPLAY || playState == PlayState.PLAYING) {
			if (context != null) {
				Util.displayToast(context,
						context.getString(R.string.wait_playing));
			} // else do nothing
			return;
		}
		SettingsDBUtil settingsDBUtil = new SettingsDBUtil(context);
		int doodlePlaySpeed = Util.convertStringToInt(
				settingsDBUtil.getValue(Constants.KEY_DOODLE_PLAY_SPEED),
				Constants.DEFAULT_DOODLE_SPEED);
		playDoodle = new DoodlePlay(this, doodle, doodleViewProperties,
				doodlePlaySpeed);
		try {
			playDoodle.start();
		} catch (IllegalThreadStateException itse) {
			Logger.warn(TAG, itse.getLocalizedMessage());
			itse.printStackTrace();
		} catch (Exception e) {
			Logger.warn(TAG, "Unknown exception : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method helps in resizing and drawing the doodle
	 * 
	 * @param doodle
	 *            : Doodle data
	 */
	public void resize(ArrayList<Object> doodle,
			DoodleViewProperties doodleViewProperties, DoodleViewBounds bounds) {
		// If a doodle is already being played then display warning and return
		if (playState == PlayState.REPLAY || playState == PlayState.PLAYING) {
			if (context != null) {
				Util.displayToast(context,
						context.getString(R.string.wait_playing));
			} // else do nothing
			return;
		}
		resizeDoodle = new DoodleResize(this, doodle, doodleViewProperties,
				bounds, context);
		try {
			resizeDoodle.performResize();
		} catch (IllegalThreadStateException itse) {
			Logger.warn(TAG, itse.getLocalizedMessage());
			itse.printStackTrace();
		} catch (Exception e) {
			Logger.warn(TAG, "Unknown exception : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/**
	 * This method helps in playing the existing
	 * 
	 * @param allowDrawAfterPlay
	 *            : boolean value to check if the doodle is newly drawn or not
	 */
	public void play(boolean allowDrawAfterPlay) {
		// If a doodle is already being played then display warning and return
		if (playState == PlayState.REPLAY || playState == PlayState.PLAYING) {
			if (context != null) {
				Util.displayToast(context,
						context.getString(R.string.wait_playing));
			} // else do nothing
			return;
		}

		setPlayState(PlayState.REPLAY);
		SettingsDBUtil settingsDBUtil = new SettingsDBUtil(context);
		int doodlePlaySpeed = Util.convertStringToInt(
				settingsDBUtil.getValue(Constants.KEY_DOODLE_PLAY_SPEED),
				Constants.DEFAULT_DOODLE_SPEED);
		resetBrush();
		initBackground();
		playDoodle = new DoodlePlay(this, doodle, doodleViewProperties,
				doodlePlaySpeed, allowDrawAfterPlay);
		try {
			playDoodle.start();
		} catch (IllegalThreadStateException itse) {
			Logger.warn(TAG, itse.getLocalizedMessage());
			itse.printStackTrace();
		} catch (Exception e) {
			Logger.warn(TAG, "Unknown exception : " + e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	// Halts the start of play thread until the surface gets created
	protected void waitForSurfaceInit() {
		// Value of isSurfaceCreated is changed in the overridden method -
		// surfaceCreated()
		while (!isSurfaceInitialized) {
			try {
				Thread.sleep(POLLING_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected Paint getBrush() {
		return brush;
	}

	protected void setBrush(Paint brush) {
		this.brush = brush;
	}

	protected float getScaleFactor() {
		float scale = 1.0f;
		if (doodleViewBounds != null) {
			scale = doodleViewBounds.getScaleFactor();
		}

		return scale;
	}

	/**
	 * Gets the current color
	 * 
	 * @return
	 */
	public int getColor() {
		if (paintOptions != null) {
			return paintOptions.getColor();
		} else {
			return Util.returnColorValue(Constants.DEFAULT_BRUSH_COLOR);
		}
	}

	/**
	 * This method sets the color of the paint brush to the specified color and
	 * updates the model too
	 * 
	 * @param color
	 *            :The color value
	 */
	public void setColor(int color) {
		// Change the color only if its a new color
		if (this.paintOptions != null) {
			if (getColor() != color) {
				paintOptions.setColor(color);

				// Updating the model
				if (doodle != null) {
					String hexColor = Util.getHexStringForColor(color);
					doodle.add(new StrokeBean(hexColor));
				}
			}
		}
	}

	/**
	 * Gets the current stroke size
	 * 
	 * @return
	 */
	public int getStrokeSize() {
		if (paintOptions != null) {
			return paintOptions.getSize();
		} else {
			return Constants.DEFAULT_BRUSH_SIZE;
		}
	}

	/**
	 * This method sets the size of the paint brush to the specified size and
	 * updates the model too
	 * 
	 * @param size
	 *            :The size of the brush
	 */
	public void setStrokeSize(int size) {
		// Change the size only if its a new size
		if (this.paintOptions != null) {
			if (getStrokeSize() != size) {
				paintOptions.setSize(size);

				// Updating the model
				if (doodle != null) {
					doodle.add(new StrokeBean(size));
				}
			}
		}
	}

	/**
	 * Adds the bitmap image to the doodle
	 * 
	 * @param bmp
	 *            : the bitmap image (should not be null)
	 */
	public void setBackgroundBitmap(Bitmap bmp) {
		if (bmp != null) {
			clearBackgroundImage();
			backgroundImage = bmp;
			doodleViewProperties.setBackgroundBitmap(bmp);
			clearSurface();
			redrawSurface(doodleViewProperties);
		} else {
			Logger.warn(TAG, "Background image is null");
		}
	}

	/**
	 * Tells whether to block UI interaction in doodle view
	 * 
	 * @return
	 */
	public boolean isCanvasLocked() {
		boolean result = false;
		if ((playState == PlayState.PLAYING) || (playState == PlayState.REPLAY)
				|| (playState == PlayState.BLOCKED)) {
			result = true;
		}

		return result;
	}

	/**
	 * @return the doodle
	 */
	public ArrayList<Object> getDoodle() {
		return doodle;
	}

	protected void setDoodle(ArrayList<Object> doodle) {
		this.doodle = doodle;
	}

	public DoodleViewProperties getDoodleViewProperties() {
		return doodleViewProperties;
	}

	protected void setDoodleViewProperties(
			DoodleViewProperties doodleViewProperties) {
		this.doodleViewProperties = doodleViewProperties;
	}

	public PlayState getPlayState() {
		return playState;
	}

	public void setPlayState(PlayState playState) {
		setPreviousPlayState(this.playState);
		this.playState = playState;
	}

	/**
	 * @return the previousPlayState
	 */
	public PlayState getPreviousPlayState() {
		return previousPlayState;
	}

	/**
	 * @param previousPlayState
	 *            the previousPlayState to set
	 */
	public void setPreviousPlayState(PlayState previousPlayState) {
		this.previousPlayState = previousPlayState;
	}

	/**
	 * Sets the play state of the doodle view to one the state before the
	 * current state
	 */
	public void restorePreviousPlayState() {
		if (previousPlayState != null) {
			setPlayState(previousPlayState);
		}
	}

	protected Bitmap getBackgroundImage() {
		return backgroundImage;
	}

	protected void setBackgroundImage(Bitmap backgroundImage) {
		if (this.backgroundImage != null) {
			this.backgroundImage.recycle();
		}
		if (backgroundImage != null) {
			this.backgroundImage = backgroundImage;
		} else {
			this.backgroundImage = null;
		}
	}

	/**
	 * @return the doodleViewBounds
	 */
	public DoodleViewBounds getDoodleViewBounds() {
		return doodleViewBounds;
	}

	/**
	 * Marks unused memory for GC
	 */
	public void deallocate() {
		if (pictureBitmap != null) {
			pictureBitmap.recycle();
		}
	}

	private boolean shouldPauseResume() {
		// TODO Update
		// if (context != null && context instanceof CanvasActivity) {
		// CanvasActivity canvas = (CanvasActivity) context;
		// CanvasState canvasState = canvas.getCanvasState();
		// return (canvasState == CanvasState.PLAY_RECEIVED_DOODLE);
		// }

		return false;
	}

	private void showCanvasOptionsButton() {
		// TODO Update
		// if (context != null && context instanceof CanvasActivity) {
		// ((CanvasActivity) context).showCanvasOptionsButton();
		// }
	}

	private void showCanvasOptionsAfterPlay() {
		// TODO Update
		// if (context != null && context instanceof CanvasActivity) {
		// (new Handler()).postDelayed(new Runnable() {
		//
		// @Override
		// public void run() {
		// ((CanvasActivity) context).animationForOptionsAfterPlay();
		// }
		// }, CanvasActivity.DELAY_AFTER_PLAY_OPTIONS);
		// }
	}

	private void toggleOptionsAfterPlay() {
		// TODO Update
		// if (context != null && context instanceof CanvasActivity) {
		// (new Handler()).post(new Runnable() {
		//
		// @Override
		// public void run() {
		// ((CanvasActivity) context).toggleAfterPlayOptions();
		// }
		// });
		// }
	}

	private void toggleDoodlePauseResume() {
		if (playState == PlayState.PAUSED) {
			resumeDoodle();
		} else if (playState == PlayState.PLAYING
				|| playState == PlayState.REPLAY) {
			pauseDoodle();
		}
	}

	private void showSendButton() {
		// TODO Update
		// if (context != null && context instanceof CanvasActivity) {
		// ((CanvasActivity) context).showSendLayout();
		// }
	}

	private void didStartDraw() {
		// TODO Update
		// if (context != null && context instanceof CanvasActivity) {
		// CanvasActivity canvas = (CanvasActivity) context;
		// canvas.hideAllExtraLayouts();
		// }
	}

	public void onReplayStop() {
		// TODO Update
		// ((CanvasActivity) context).runOnUiThread(new Runnable() {
		// public void run() {
		// try {
		// showCanvasOptionsButton();
		// } catch (final Exception ex) {
		// Logger.logStackTrace(ex);
		// }
		// }
		// });

	}

	public void onPlayStop() {
		// TODO Update
		// ((CanvasActivity) context).runOnUiThread(new Runnable() {
		// public void run() {
		// try {
		// showCanvasOptionsAfterPlay();
		// } catch (final Exception ex) {
		// Logger.logStackTrace(ex);
		// }
		// }
		// });

	}

	public void onResizeStop() {
		// TODO Update
		// ((CanvasActivity) context).runOnUiThread(new Runnable() {
		// public void run() {
		// try {
		// resetToEdit();
		// } catch (final Exception ex) {
		// Logger.logStackTrace(ex);
		// }
		// }
		// });

	}

	/**
	 * @return the selectedLayerID
	 */
	public int getSelectedLayerID() {
		return selectedLayerID;
	}

	/**
	 * Sets the width of the doodle view
	 * 
	 * @param newWidth
	 */
	public void setDoodleViewWidth(int newWidth) {
		this.width = newWidth;
	}

	/**
	 * Sets the height of the doodle view
	 * 
	 * @param newHeight
	 */
	public void setDoodleViewHeight(int newHeight) {
		this.height = newHeight;
	}

	/**
	 * Sets the flag that tells whether edit initialization is pending or not
	 * 
	 * @param flag
	 */
	public void setEditInitializationPendingFlag(boolean flag) {
		this.isEditInitializationPending = flag;
	}

	public ArrayList<Layer> getBitmapLayers() {
		return bitmapLayers;
	}

	synchronized public boolean isRotateLayer() {
		return rotateLayer;
	}

	synchronized public void setRotateLayer(boolean rotateLayer) {
		this.rotateLayer = rotateLayer;
	}

	/**
	 * Removes layer from the doodle view, if there exists only one layer of
	 * type not equals to LayerType.TEXT
	 */
	public void removeOneAndOnlyExistingNonTEXTTypeLayer() {
		if (bitmapLayers != null && bitmapLayers.size() > 0) {
			int nonTextTypeLayersCount = 0;
			int layerId = -1;
			for (int i = 0; i < bitmapLayers.size(); i++) {
				Layer layer = bitmapLayers.get(i);
				if (layer != null && layer.getType() != LayerType.TEXT) {
					nonTextTypeLayersCount++;
					layerId = bitmapLayers.indexOf(layer);
				}
			}
			if (nonTextTypeLayersCount == 1 && layerId != -1) {
				removeLayerWithID(layerId);
			}
		}
	}

}