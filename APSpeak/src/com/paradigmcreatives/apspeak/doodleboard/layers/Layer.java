package com.paradigmcreatives.apspeak.doodleboard.layers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.doodleboard.DoodleView;

public class Layer {

	public enum LayerType {
		RAGE_FACE, TEXT, EMOJI, GOOGLE_IMAGES, GREETINGS, MAP, CAMERA, GALLERY, VIA_INTENT, FRAME, STICKER
	};

	private Bitmap bitmap;
	public float x;
	public float y;
	private int width = -1;
	private int height = -1;
	private int origWidth = -1;
	private int origHeight = -1;
	private Matrix matrix;
	float scale = 1.0f;
	private float angle = 0.0f;
	private LayerType type;
	private String thumbnailURL;
	private Matrix maskMatrix;
	private List<LayerControl> controls;
	private String id;
	private DoodleView doodleView;

	/**
	 * @param bitmap
	 * @param x
	 * @param y
	 */
	public Layer(DoodleView doodleView, Bitmap bitmap, float x, float y, LayerType type) {
		this(doodleView, bitmap, x, y, type, null);
	}

	public Layer(DoodleView doodleView, Bitmap bitmap, float x, float y, LayerType type, String thumbnailURL) {
		this.doodleView = doodleView;
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.type = type;
		this.thumbnailURL = thumbnailURL;
		this.id = UUID.randomUUID().toString();
		init();
	}

	private void init() {
		if (bitmap != null) {
			width = bitmap.getWidth();
			height = bitmap.getHeight();
			origWidth = width;
			origHeight = height;
		}
		matrix = new Matrix();
		matrix.postTranslate(this.x, this.y);
		maskMatrix = new Matrix();

		// Initialize the list of controls
		controls = new ArrayList<LayerControl>();
	}

	/**
	 * @return the bitmap
	 */
	public Bitmap getBitmap() {
		return bitmap;
	}

	/**
	 * @param bitmap the bitmap to set
	 */
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public synchronized void setX(float x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public synchronized void setY(float y) {
		this.y = y;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public synchronized void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public synchronized void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the matrix
	 */
	public Matrix getMatrix() {
		return matrix;
	}

	/**
	 * @param matrix the matrix to set
	 */
	public synchronized void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public synchronized void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * @return the origWidth
	 */
	public int getOrigWidth() {
		return origWidth;
	}

	/**
	 * @param origWidth the origWidth to set
	 */
	public synchronized void setOrigWidth(int origWidth) {
		this.origWidth = origWidth;
	}

	/**
	 * @return the origHeight
	 */
	public int getOrigHeight() {
		return origHeight;
	}

	/**
	 * @param origHeight the origHeight to set
	 */
	public synchronized void setOrigHeight(int origHeight) {
		this.origHeight = origHeight;
	}

	/**
	 * @return the type
	 */
	public LayerType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public synchronized void setType(LayerType type) {
		this.type = type;
	}

	/**
	 * @return the thumbnailURL
	 */
	public String getThumbnailURL() {
		return thumbnailURL;
	}

	/**
	 * @param thumbnailURL the thumbnailURL to set
	 */
	public synchronized void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	/**
	 * Parses the data to get the thumbnail URL
	 * 
	 * @param data
	 * @return
	 */
	public static String getThumbnailURLFromData(Intent data) {
		// TODO Get the asset utils
		// final AssetBean assetBean = AssetBean.getAssetBeanFromData(data);
		// if (assetBean != null) {
		// return assetBean.getThumbnailURL();
		// } else {
		// Logger.warn(TAG, "Could not initiate the loading of large image. Bundle is NULL");
		// }
		return null;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public Matrix getMaskMatrix() {
		return maskMatrix;
	}

	public void setMaskMatrix(Matrix maskMatrix) {
		this.maskMatrix = maskMatrix;
	}

	public boolean contains(PointF point) {
		PointF[] transformedVertices = getTransformedVeritices(this);

		return rayCasting(point, transformedVertices, 4);
	}

	private boolean rayCasting(PointF point, PointF[] points, int n) {
		int i, j, nvert = n;
		boolean lies = false;

		for (i = 0, j = nvert - 1; i < nvert; j = i++) {
			if (((points[i].y > point.y) != (points[j].y > point.y))
					&& (point.x < (points[j].x - points[i].x) * (point.y - points[i].y) / (points[j].y - points[i].y)
							+ points[i].x))
				lies = !lies;
		}

		return lies;
	}

	private PointF[] getTransformedVeritices(Layer layer) {
		PointF[] vertices = new PointF[4];
		float[][] corners = new float[4][2];
		Matrix maskMatrix = layer.getMaskMatrix();

		// Upper left corner
		corners[0][0] = layer.x;
		corners[0][1] = layer.y;

		// Upper right corner
		corners[1][0] = layer.x + layer.getWidth();
		corners[1][1] = layer.y;

		// Lower right corner
		corners[2][0] = layer.x + layer.getWidth();
		corners[2][1] = layer.y + layer.getHeight();

		// Lower left corner
		corners[3][0] = layer.x;
		corners[3][1] = layer.y + layer.getHeight();

		// Transform
		maskMatrix.mapPoints(corners[0]);
		maskMatrix.mapPoints(corners[1]);
		maskMatrix.mapPoints(corners[2]);
		maskMatrix.mapPoints(corners[3]);

		// Construct the return result
		vertices[0] = new PointF(corners[0][0], corners[0][1]);
		vertices[1] = new PointF(corners[1][0], corners[1][1]);
		vertices[2] = new PointF(corners[2][0], corners[2][1]);
		vertices[3] = new PointF(corners[3][0], corners[3][1]);

		return vertices;
	}

	/**
	 * Returns the affine transformed starting points of the layer
	 * 
	 * @return
	 */
	public PointF getTransformedXY() {
		float[] points = { this.x, this.y };
		maskMatrix.mapPoints(points);
		return new PointF(points[0], points[1]);
	}

	/**
	 * Returns the X-Y value based on the gravity. Default is the top-left side
	 * 
	 * @param gravityX
	 * @param gravityY
	 * @return
	 */
	public PointF getXY(LayerControl.Gravity gravityX, LayerControl.Gravity gravityY) {
		float x = this.x;
		float y = this.y;
		switch (gravityX) {
		case RIGHT:
			x = this.x + this.width;
			break;
		case CENTER:
			x = this.x + this.width / 2;
			break;
		case LEFT:
		default:
			x = this.x;
		}

		switch (gravityY) {
		case BOTTOM:
			y = this.y + this.height;
			break;
		case CENTER:
			y = this.y + this.height / 2;
			break;
		case TOP:
		default:
			y = this.y;
		}

		float[] points = { x, y };
		maskMatrix.mapPoints(points);

		return new PointF(points[0], points[1]);
	}

	public List<LayerControl> getControls() {
		return controls;
	}

	/**
	 * Add the given control to the layer
	 * 
	 * @param control
	 */
	public void addControl(LayerControl control) {
		if (control != null && controls != null) {
			controls.add(control);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Layer) {
			if (TextUtils.equals(id, ((Layer) o).getID())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		this.id = id;
	}

	public DoodleView getDoodleView() {
		return doodleView;
	}
}// end of class