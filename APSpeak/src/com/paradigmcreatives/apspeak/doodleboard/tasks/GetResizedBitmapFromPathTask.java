package com.paradigmcreatives.apspeak.doodleboard.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;

public class GetResizedBitmapFromPathTask extends
		AsyncTask<String, Void, Bitmap> {
	private static final int SOURCE_CAMERA = 1;
	private Bitmap bitmap;

	private String imagePath;
	private boolean isFrontFacing;
	private Activity activity;
	private int screenWidth;
	private BitmapFactory.Options options;
	private boolean isFromGallery;
	private int screenHeight;
	private int source;
	byte[] data;

	public GetResizedBitmapFromPathTask(Activity activity, String imagePath,
			int screenWidth, int screenHeight, boolean isFromGallery) {
		this.isFromGallery = isFromGallery;
		this.imagePath = imagePath;
		this.activity = activity;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

	}

	public GetResizedBitmapFromPathTask(Activity activity, byte[] data,
			boolean isFrontFacing, int screenWidth, int screenHeight,
			boolean isFromGallery) {
		source = SOURCE_CAMERA;
		this.isFrontFacing = isFrontFacing;
		this.isFromGallery = isFromGallery;
		this.activity = activity;
		this.data = data;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

	}

	public GetResizedBitmapFromPathTask(Activity activity, Bitmap bitmap,
			int screenWidth, int screenHeight, boolean isFromGallery) {
		this.isFromGallery = isFromGallery;
		this.activity = activity;
		this.bitmap = bitmap;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		options = new BitmapFactory.Options();
		Bitmap bmp;
		if (source == SOURCE_CAMERA) {
			/*
			 * 1) Captured image data is saved to private temporary file. 2)
			 * Applies techniques to reduce the size of the file so that huge
			 * memory is not consumed while using the bitmap. 3) Removes created
			 * private temporary file.
			 */
			String savedPath = saveCameraDataToPrivateTempFile(data);
			if(!TextUtils.isEmpty(savedPath)){
				bitmap = decodeSampledBitmapFromResource(savedPath, screenWidth,
						screenHeight);
				deleteSavedFile(savedPath);
			}
			//bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			
			// create a matrix for the manipulation
			Matrix matrix = new Matrix();
			if (isFrontFacing) {
				matrix.postRotate(-90);
			} else {
				matrix.postRotate(90);
			}
			bmp = bitmap;
			/*
			// Taking only the square part of the bitmap from valid position
			int startX = 0;
			if (isFrontFacing) {
			startX = bitmap.getWidth() - bitmap.getHeight();
			}
			if (bitmap.getWidth() >= bitmap.getHeight()) {
			bmp = Bitmap.createBitmap(bitmap, startX, 0, bitmap.getHeight(), bitmap.getHeight());
			} else {
			bmp = Bitmap.createBitmap(bitmap, startX, 0, bitmap.getWidth(), bitmap.getWidth());
			}
			*/
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			if (bitmap != bmp) {
				bmp.recycle();
				bmp = null;
			}
			/*
			String filePath = saveBitmapToPrivateTempFile(bitmap);
			if(!TextUtils.isEmpty(filePath)){
				imagePath = filePath;
			}
			*/
			/*
			bitmap.recycle();
			bitmap = null;
			bitmap = bmp;
			*/
		} else if (!TextUtils.isEmpty(imagePath)) {
			bitmap = decodeSampledBitmapFromResource(imagePath, screenWidth,
					screenHeight);
		} else {
			bmp = getResizedBitmap(bitmap, screenWidth, screenWidth);
			bitmap.recycle();
			bitmap = null;
			bitmap = bmp;
		}
		if (bitmap != null) {
			bmp = null;
			if (!TextUtils.isEmpty(imagePath)) {
				bmp = convertRectangleImageToSquaredImage(bitmap);
				Bitmap bitmap = getResizedBitmap(bmp, screenWidth, screenWidth);
				bmp.recycle();
				bmp = null;
				return bitmap;
			} else {
				int startX = 0;
				if (bitmap.getWidth() >= bitmap.getHeight()) {
					bmp = Bitmap.createBitmap(bitmap, startX, 0,
							bitmap.getHeight(), bitmap.getHeight());
				} else {
					bmp = Bitmap.createBitmap(bitmap, startX, 0,
							bitmap.getWidth(), bitmap.getWidth());
				}

				Bitmap bitmap = getResizedBitmap(bmp, screenWidth, screenWidth);
				bmp.recycle();
				bmp = null;
				return bitmap;
			}
		}

		return bitmap;

	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (activity != null) {
			if (activity instanceof ImageSelectionFragmentActivity) {
				((ImageSelectionFragmentActivity) activity)
						.showImageFiltersFragment(bitmap);
			}

		}

	}

	/**
	 * Resizes the bitmap according to the given newHeight and newWidth
	 * 
	 * @param bm
	 * @param newHeight
	 * @param newWidth
	 * @return
	 */
	public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	private Bitmap decodeSampledBitmapFromResource(String imagePath,
			int reqWidth, int reqHeight) {
		// First decode with inJustDecodeBounds=true to check dimensions
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(imagePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		Bitmap bmp = BitmapFactory.decodeFile(imagePath, options);

		return bmp;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	private Bitmap convertRectangleImageToSquaredImage(Bitmap bmp) {
		if (bmp != null) {
			Bitmap sourceBitmap = bmp;
			int sourceBitmapWidth = sourceBitmap.getWidth();
			int sourceBitmapHeight = sourceBitmap.getHeight();
			Bitmap resultBitmap = null;
			if (sourceBitmapHeight > sourceBitmapWidth) {
				resultBitmap = Bitmap.createBitmap(sourceBitmap.getHeight(),
						sourceBitmap.getHeight(), Bitmap.Config.ARGB_8888);
			} else if (sourceBitmapWidth > sourceBitmapHeight) {
				resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
						sourceBitmap.getWidth(), Bitmap.Config.ARGB_8888);
			} else {
				// Its a squared image
				resultBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(),
						sourceBitmap.getWidth(), Bitmap.Config.ARGB_8888);
			}

			if (resultBitmap != null) {
				Canvas c = new Canvas(resultBitmap);
				c.drawColor(Color.BLACK);

				Rect sourceRect = new Rect(0, 0, sourceBitmap.getWidth(),
						sourceBitmap.getHeight());
				Rect destinationRect = null;
				if (sourceBitmapHeight > sourceBitmapWidth) {
					destinationRect = new Rect(
							(resultBitmap.getWidth() - sourceBitmap.getWidth()) / 2,
							0, (resultBitmap.getWidth() + sourceBitmap
									.getWidth()) / 2, sourceBitmap.getHeight());
				} else if (sourceBitmapWidth > sourceBitmapHeight) {
					destinationRect = new Rect(0,
							(resultBitmap.getHeight() - sourceBitmap
									.getHeight()) / 2, sourceBitmap.getWidth(),
							(resultBitmap.getHeight() + sourceBitmap
									.getHeight()) / 2);
				} else {
					// Its a squared image
					destinationRect = new Rect(0, 0, sourceBitmap.getWidth(),
							sourceBitmap.getHeight());
				}
				c.drawBitmap(sourceBitmap, sourceRect, destinationRect, null);

				return resultBitmap;
			} else {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Saves passed captured camera data to a file "CamperaCapture_temp.jpg" in application's private files folder
	 * @param data
	 * @return
	 */
	private String saveCameraDataToPrivateTempFile(byte[] data){
		String filePath = null;
		if(activity != null && data != null && data.length > 0){
			FileOutputStream fos = null;
			try{
				File file = new File(activity.getFilesDir(), "CameraCapture_temp.jpg");
				if(file != null){
					filePath = file.getAbsolutePath();
					fos = new FileOutputStream(file);
					fos.write(data);
				}
			}catch(Exception e){
				filePath = null;
			}finally{
				try{
					if(fos != null){
						fos.close();
					}
				}catch(IOException ioe){
					
				}
			}
		}
		return filePath;
	}
	
	/**
	 * Deletes file if it exists at the passed file location
	 * @param filePath
	 * @return
	 */
	private boolean deleteSavedFile(String filePath){
		boolean isFileDeleted = false;
		if(!TextUtils.isEmpty(filePath)){
			File file = new File(filePath);
			if(file != null && file.exists()){
				isFileDeleted = file.delete();
			}
		}
		return isFileDeleted;
	}
	
	/**
	 * Saves the passed bitmap to private files directory
	 * 
	 * @param bitmap
	 * @return
	 */
	private String saveBitmapToPrivateTempFile(Bitmap bitmap) {
		String savedPath = null;
		if (bitmap != null) {
			File file = new File(activity.getFilesDir(),
					"CameraCapture_temp.jpg");
			FileOutputStream out = null;
			if (file != null) {
				try {
					out = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (out != null) {
							out.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return savedPath;
	}
}// end of class