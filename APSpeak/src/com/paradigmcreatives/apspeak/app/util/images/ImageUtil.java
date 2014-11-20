package com.paradigmcreatives.apspeak.app.util.images;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.layers.Layer;
import com.paradigmcreatives.apspeak.logging.Logger;

public class ImageUtil {

    private static final String TAG = "ImageUtil";

    private static final String[] COLOR_CODES = { "#1a9fe0", "#f1a40a", "#59aa1d", "#e51400" };

    private static final int CIRCULAR_IMAGE_OUTLINE_DP = 2;
    private static final int CONTACT_IMAGE_LETTER_SIZE = 4;

    private static Random randomGenerator = new Random();

    /**
     * Calculates the inSampleSize of the image for the max height width
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
	// Raw height and width of image
	int inSampleSize = 1;

	if (options != null) {
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    if (height > reqHeight || width > reqWidth) {

		// Calculate ratios of height and width to requested height and width
		final int heightRatio = Math.round((float) height / (float) reqHeight);
		final int widthRatio = Math.round((float) width / (float) reqWidth);

		// Choose the larger ratio as inSampleSize value, this will guarantee
		// a final image with both dimensions lesser than or equal to the
		// requested height and width. Saves memory
		inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
	    }
	} else {
	    Logger.warn(TAG, "Bitmap options is null while calculating in sample size");
	}

	// inSampleSize = (inSampleSize % 2) == 0 ? inSampleSize + 2 : inSampleSize + 3;
	return inSampleSize;
    }

    /**
     * This method gives the portrait image correction matrix keeping in consideration its exif orientation of the
     * original
     * 
     * @param imageOptions
     *            : The original image options
     * @param imagePath
     *            : The original image path
     * @return : The portrait image correction matrix
     */
    public static Matrix getPortraitOrientedImageCorrectionMatrix(BitmapFactory.Options imageOptions, String imagePath) {
	Matrix result = null;
	if (imageOptions != null) {
	    try {
		ExifInterface exif = new ExifInterface(imagePath);
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

		Matrix matrix = new Matrix();
		float rotation = 0;
		boolean addMatrix = true;

		switch (orientation) {
		case ExifInterface.ORIENTATION_NORMAL:
		    addMatrix = false;
		    break;

		case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
		    matrix.setScale(-1, 1);
		    matrix.postTranslate(imageOptions.outWidth, 0);
		    break;

		case ExifInterface.ORIENTATION_ROTATE_180:
		    rotation = 180.0f;
		    matrix.postRotate(rotation);
		    break;

		case ExifInterface.ORIENTATION_FLIP_VERTICAL:
		    matrix.setScale(1, -1);
		    matrix.postTranslate(0, imageOptions.outHeight);
		    break;

		case ExifInterface.ORIENTATION_TRANSPOSE:
		    matrix.setScale(1, -1);
		    matrix.postTranslate(0, imageOptions.outHeight);
		    rotation = 90.0f;
		    matrix.postRotate(rotation);
		    break;

		case ExifInterface.ORIENTATION_ROTATE_90:
		    rotation = 90.0f;
		    matrix.postRotate(rotation);
		    break;

		case ExifInterface.ORIENTATION_TRANSVERSE:
		    matrix.setScale(1, -1);
		    matrix.postTranslate(0, imageOptions.outHeight);
		    rotation = 270.0f;
		    matrix.postRotate(rotation);
		    break;

		case ExifInterface.ORIENTATION_ROTATE_270:
		    rotation = 270.0f;
		    matrix.postRotate(rotation);
		    break;

		default:
		    addMatrix = false;
		    break;
		}

		if (addMatrix) {
		    result = matrix;
		} else {
		    result = null;
		}
	    } catch (IOException e) {
		Logger.logStackTrace(e);
	    } catch (IllegalArgumentException iae) {
		Logger.logStackTrace(iae);
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }
	} else {
	    Logger.warn(TAG, "Image is null");
	}

	return result;
    }

    /**
     * Gives the aspect fit rect for the bitmap image
     * 
     * @param picWidth
     *            : the image height
     * @param picHeight
     *            : The image height
     * @param width
     *            : The width to fit into
     * @param height
     *            : The height to fit into
     * 
     * @return : The aspect fit rect for the image
     */
    public static RectF getPictureRectFWithAspectFit(int picWidth, int picHeight, float width, float height) {
	RectF picRect = new RectF();
	float screenWidth = width;
	float screenHeight = height;

	float picRatio = ((float) picWidth) / picHeight;
	float widthRatio = ((float) picWidth) / screenWidth;
	float heightRatio = ((float) picHeight) / screenHeight;

	float left = 0, top = 0, right, bottom;
	if ((widthRatio <= 1.0f) && (heightRatio <= 1.0f)) {
	    left = (screenWidth - picWidth) * 0.5f;
	    top = (screenHeight - picHeight) * 0.5f;
	} else if (widthRatio >= heightRatio) {
	    float newPicWidth = (float) screenWidth;
	    float newPicHeight = newPicWidth / picRatio;
	    left = 0.0f;
	    top = (screenHeight - newPicHeight) * 0.5f;
	} else {
	    // float newPicHeight = (float) screenHeight;
	    // float newPicWidth = newPicHeight * picRatio;
	    left = 0.0f;// (screenWidth - newPicWidth) * 0.5f;
	    top = 0.0f;
	}

	right = screenWidth - left;
	bottom = screenHeight - top;
	picRect.set(left, top, right, bottom);

	return picRect;
    }

    public static Rect getPictureRectWithAspectFit(int picWidth, int picHeight, float width, float height) {
	Rect picRect = new Rect();
	float screenWidth = width;
	float screenHeight = height;

	float picRatio = ((float) picWidth) / picHeight;
	float widthRatio = ((float) picWidth) / screenWidth;
	float heightRatio = ((float) picHeight) / screenHeight;

	float left = 0, top = 0, right, bottom;
	if ((widthRatio <= 1.0f) && (heightRatio <= 1.0f)) {
	    left = (screenWidth - picWidth) * 0.5f;
	    top = (screenHeight - picHeight) * 0.5f;
	} else if (widthRatio >= heightRatio) {
	    float newPicWidth = (float) screenWidth;
	    float newPicHeight = newPicWidth / picRatio;
	    left = 0.0f;
	    top = (screenHeight - newPicHeight) * 0.5f;
	} else {
	    float newPicHeight = (float) screenHeight;
	    float newPicWidth = newPicHeight * picRatio;
	    left = (screenWidth - newPicWidth) * 0.5f;
	    top = 0.0f;
	}

	right = screenWidth - left;
	bottom = screenHeight - top;
	picRect.set((int) left, (int) top, (int) right, (int) bottom);

	return picRect;
    }

    /**
     * Gives the aspect fit rect for the bitmap image
     * 
     * @param picWidth
     *            : the image height
     * @param picHeight
     *            : The image height
     * @param bounds
     *            : The bounding rect for the image
     * @return : The aspect fit rect for the image
     */
    public static RectF getPictureRectFWithAspectFit(int picWidth, int picHeight, RectF bounds) {
	RectF picRect = new RectF();

	RectF scaledRect = getPictureRectFWithAspectFit(picWidth, picHeight, bounds.width(), bounds.height());

	float left = scaledRect.left + bounds.left;
	float top = scaledRect.top + bounds.top;
	float right = bounds.right - scaledRect.left;
	float bottom = bounds.bottom - scaledRect.top;

	picRect.set(left, top, right, bottom);
	return picRect;
    }

    /**
     * Gets the opaque image from the bitmap
     * 
     * @param image
     * @param compressionQuality
     * @return
     */
    public static Bitmap getCompressedOpaqueImage(Bitmap image, int compressionQuality) {
	Bitmap result = null;
	if (image != null) {
	    try {
		ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
		boolean compressSuccess = image.compress(Bitmap.CompressFormat.JPEG, compressionQuality, bmpStream);
		byte[] bmpPicByteArray = bmpStream.toByteArray();
		if (bmpPicByteArray != null && compressSuccess) {
		    result = BitmapFactory.decodeByteArray(bmpPicByteArray, 0, bmpPicByteArray.length);
		    Logger.info(TAG, "successfully make image opaque");
		} else {
		    Logger.warn(TAG, "Failed to make image opaque");
		    result = image;
		}
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown exception while getting opaque image : " + e.getLocalizedMessage());
		result = image;
	    }
	} else {
	    Logger.warn(TAG, "Image is null");
	}

	return result;
    }

    public static Bitmap getRoundedCornerAndClippedBitmap(Bitmap bitmap) {
	if (bitmap == null) {
	    return null;
	}

	int width = bitmap.getWidth();
	int height = bitmap.getHeight();
	Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
	Canvas canvas = new Canvas(output);

	final Paint paint = new Paint();
	final Rect rect = new Rect(0, 0, width, height);
	final RectF rectF = new RectF(rect);
	final float roundPx = 5;

	paint.setAntiAlias(true);
	canvas.drawARGB(0, 0, 0, 0);
	paint.setColor(0xff121212);
	canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	canvas.drawBitmap(bitmap, rect, rect, paint);

	return output;
    }

    /**
     * Watermark Doodly Doo logo on the bottom right corner of the supplied original <code>Bitmap</code>. The resultant
     * <code>Bitmap</code> should be recycled after its usage is over. This method does not take care of recycling of
     * the bitmaps.
     * 
     * @param original
     * @return
     */
    public static Bitmap watermarkDoodlyDoo(Context context, Bitmap original) {
	Bitmap result = null;
	Bitmap watermarkImageBitmap = BitmapFactory.decodeResource(context.getResources(),
		R.drawable.whatsay_launcher_icon);
	if (original != null && context != null && watermarkImageBitmap != null) {
	    result = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(result);
	    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    canvas.drawBitmap(original, 0, 0, paint);
	    paint.setAlpha(Constants.WATERMARK_ALPHA);

	    // Placing the watermark on the bottom right corner of the image
	    canvas.drawBitmap(watermarkImageBitmap, original.getWidth() - watermarkImageBitmap.getWidth(),
		    original.getHeight() - watermarkImageBitmap.getHeight(), paint);
	}

	return result;

    }

    public static Bitmap getCircularBitmapResizeTo(Context context, Bitmap originalBitmap, int width, int height) {
	if (context == null || originalBitmap == null) {
	    return null;
	}

	Bitmap bitmap = Bitmap.createScaledBitmap(originalBitmap, getPxFromDip(context, width),
		getPxFromDip(context, height), true);
	int strokeWidth = getPxFromDip(context, CIRCULAR_IMAGE_OUTLINE_DP);

	int bitmapWidth = bitmap.getWidth();
	int bitmapHeight = bitmap.getHeight();
	Bitmap output = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
	Canvas canvas = new Canvas(output);

	final Paint paint = new Paint();
	final Rect rect = new Rect(0, 0, bitmapWidth, bitmapHeight);
	paint.setAntiAlias(true);
	canvas.drawARGB(0, 0, 0, 0);
	paint.setColor(Color.WHITE);

	canvas.drawCircle(bitmapWidth / 2, bitmapHeight / 2, bitmapWidth / 2 - strokeWidth / 2, paint);

	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	canvas.drawBitmap(bitmap, rect, rect, paint);

	paint.setColor(Color.LTGRAY);
	paint.setStyle(Style.STROKE);
	paint.setStrokeWidth(strokeWidth);
	paint.setXfermode(null);
	canvas.drawCircle(bitmapWidth / 2, bitmapHeight / 2, bitmapWidth / 2 - strokeWidth / 2, paint);

	// Recycling xxxx bitmaps
	if (!bitmap.equals(originalBitmap)) {
	    bitmap.recycle();
	}

	return output;
    }

    /**
     * Converts the given DIP to PX
     * 
     * @param context
     * @param px
     * @return
     */
    public static int getPxFromDip(Context context, int px) {
	int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) px, context.getResources()
		.getDisplayMetrics());
	return value;
    }

    /**
     * Saves the profile image provided
     * 
     * @param context
     *            : The context to be used
     * @param profileImage
     *            : The profile image to be saved
     * @return
     */
    public static boolean saveProfileImage(Context context, Bitmap profileImage) {
	if (context == null) {
	    Logger.warn(TAG, "Context is null");
	    return false;
	}
	String appDirectory = AppPropertiesUtil.getAppDirectory(context);
	if (appDirectory != null && DeviceInfoUtil.mediaWritable()) {
	    boolean result = false;
	    try {
		File profilePicFile = new File(appDirectory + context.getString(R.string.profile_pic_folder),
			context.getString(R.string.profile_picture_image_name));
		Bitmap profilePic = ImageUtil.getCircularBitmapResizeTo(context, profileImage,
			Constants.HEADER_IMAGE_SIZE, Constants.HEADER_IMAGE_SIZE);
		if (profilePic != null) {
		    FileOutputStream out = new FileOutputStream(profilePicFile);
		    profilePic.compress(Bitmap.CompressFormat.PNG, Constants.COMPRESSION_QUALITY_HIGH, out);
		    result = true;
		} else {
		    return false;
		}
	    } catch (FileNotFoundException fnfe) {
		Logger.warn(TAG, fnfe.getLocalizedMessage());
	    } catch (Exception e) {
		Logger.logStackTrace(e);
	    }
	    return result;
	} else {
	    Logger.warn(TAG, "Memory not writeable");
	    return false;
	}
    }

    /**
     * Generates Bitmap from the given string's initial letter
     * 
     * @param width
     * @param height
     * @param string
     * @return
     */
    public static Bitmap generateBitmapFromInitialLetter(int width, int height, String string) {
	Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(result);

	Paint bgPaint = new Paint();
	int color = generateColorCode(false);
	bgPaint.setColor(color);
	bgPaint.setStrokeWidth(2);
	bgPaint.setStyle(Style.FILL);
	bgPaint.setAntiAlias(true);
	canvas.drawPaint(bgPaint);

	Paint textPaint = new Paint();
	textPaint.setColor(Color.WHITE);
	textPaint.setTextSize(height - 10);
	textPaint.setStrokeWidth(CONTACT_IMAGE_LETTER_SIZE);
	textPaint.setTextAlign(Align.CENTER);
	textPaint.setAntiAlias(true);
	float xPos = canvas.getWidth() * 0.5f;
	float yPos = ((canvas.getHeight() * 0.5f) - ((textPaint.descent() + textPaint.ascent()) * 0.5f));
	String displayString = Util.getInitialsFromString(string);
	canvas.drawText(displayString, xPos, yPos, textPaint);
	return result;
    }

    /**
     * Generates a colored image
     * 
     * @param width
     * @param height
     * @param random
     * @return
     */
    public static Bitmap generateColoredImage(int width, int height, boolean random, boolean roundedCorners) {
	Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(result);

	int color = generateColorCode(random);
	if (roundedCorners) {
	    Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    xferPaint.setColor(color);

	    // We're just reusing xferPaint to paint a normal looking rounded box, the 20.f
	    // is the amount we're rounding by.
	    canvas.drawRoundRect(new RectF(0, 0, width, height), width / 20.0f, width / 20.0f, xferPaint);

	    // Now we apply the 'magic sauce' to the paint
	    xferPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
	} else {
	    Rect rt = new Rect(0, 0, width, height);
	    Paint fillPaint = new Paint();
	    fillPaint.setColor(color);
	    fillPaint.setStyle(Style.FILL);
	    canvas.drawRect(rt, fillPaint);
	}

	return result;
    }

    /**
     * Gives a image with a transparent center
     * 
     * @param size
     * @param radius
     * @return
     */
    public static Bitmap getImageWithCircularWindow(int size, int diameter) {
	Bitmap result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(result);

	Rect rt = new Rect(0, 0, size, size);
	Paint fillPaint = new Paint();
	fillPaint.setColor(Color.WHITE);
	fillPaint.setStyle(Style.FILL);
	canvas.drawRect(rt, fillPaint);

	Paint transparentPaint = new Paint();
	transparentPaint.setColor(Color.TRANSPARENT);
	transparentPaint.setStrokeWidth(0);
	transparentPaint.setAntiAlias(true);
	transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
	canvas.drawCircle(size / 2.0f, size / 2.0f, diameter / 2.0f, transparentPaint);

	return result;
    }

    /**
     * Generates the color code randomly
     * 
     * @param random
     *            : flag to tell when to generate random colors
     * 
     * @return
     */
    public static int generateColorCode(boolean random) {
	if (random) {
	    String[] letters = new String[15];
	    letters = "0123456789ABCDEF".split("");
	    String code = "#";
	    int index = 0;
	    while (code.length() <= 6) {
		if (TextUtils.equals(code, "#FFFFF")) {
		    index = randomGenerator.nextInt(15);
		} else {
		    index = randomGenerator.nextInt(16);
		}
		code += letters[index];
	    }
	    Log.v(TAG, "Color code : " + code);
	    return Util.returnColorValue(code);
	} else {
	    return getStaticColor();
	}
    }

    /**
     * Returns a random color from the static list
     * 
     * @return
     */
    public static int getStaticColor() {
	int index = randomGenerator.nextInt(4);

	return Util.returnColorValue(COLOR_CODES[index]);
    }

    /**
     * Resizes the given bitmap
     * 
     * @param bitmap
     * @param dst
     * @return
     */
    public static Bitmap resizeBitmap(Bitmap bitmap, RectF dst) {
	Bitmap result = null;
	try {
	    if (bitmap != null) {
		result = Bitmap.createScaledBitmap(bitmap, (int) Math.abs(dst.right - dst.left),
			(int) Math.abs(dst.bottom - dst.top), true);
	    }
	} catch (Exception e) {
	    return bitmap;
	}
	return result;
    }

    /**
     * Returns byte array of the supplied bitmap
     * 
     * @param bmp
     * @return
     */
    public static byte[] getBytesForBitmap(Bitmap bmp) {
	if (bmp == null) {
	    return null;
	}

	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	return stream.toByteArray();

    }

    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {

	// Stack Blur v1.0 from
	// http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
	//
	// Java Author: Mario Klingemann <mario at quasimondo.com>
	// http://incubator.quasimondo.com
	// created Feburary 29, 2004
	// Android port : Yahel Bouaziz <yahel at kayenko.com>
	// http://www.kayenko.com
	// ported april 5th, 2012

	// This is a compromise between Gaussian Blur and Box blur
	// It creates much better looking blurs than Box Blur, but is
	// 7x faster than my Gaussian Blur implementation.
	//
	// I called it Stack Blur because this describes best how this
	// filter works internally: it creates a kind of moving stack
	// of colors whilst scanning through the image. Thereby it
	// just has to add one new block of color to the right side
	// of the stack and remove the leftmost color. The remaining
	// colors on the topmost layer of the stack are either added on
	// or reduced by one, depending on if they are on the right or
	// on the left side of the stack.
	//
	// If you are using this algorithm in your code please add
	// the following line:
	//
	// Stack Blur Algorithm by Mario Klingemann <mario@quasimondo.com>

	Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

	if (radius < 1) {
	    return (null);
	}

	int w = bitmap.getWidth();
	int h = bitmap.getHeight();

	int[] pix = new int[w * h];
	Log.e("pix", w + " " + h + " " + pix.length);
	bitmap.getPixels(pix, 0, w, 0, 0, w, h);

	int wm = w - 1;
	int hm = h - 1;
	int wh = w * h;
	int div = radius + radius + 1;

	int r[] = new int[wh];
	int g[] = new int[wh];
	int b[] = new int[wh];
	int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
	int vmin[] = new int[Math.max(w, h)];

	int divsum = (div + 1) >> 1;
	divsum *= divsum;
	int dv[] = new int[256 * divsum];
	for (i = 0; i < 256 * divsum; i++) {
	    dv[i] = (i / divsum);
	}

	yw = yi = 0;

	int[][] stack = new int[div][3];
	int stackpointer;
	int stackstart;
	int[] sir;
	int rbs;
	int r1 = radius + 1;
	int routsum, goutsum, boutsum;
	int rinsum, ginsum, binsum;

	for (y = 0; y < h; y++) {
	    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
	    for (i = -radius; i <= radius; i++) {
		p = pix[yi + Math.min(wm, Math.max(i, 0))];
		sir = stack[i + radius];
		sir[0] = (p & 0xff0000) >> 16;
		sir[1] = (p & 0x00ff00) >> 8;
		sir[2] = (p & 0x0000ff);
		rbs = r1 - Math.abs(i);
		rsum += sir[0] * rbs;
		gsum += sir[1] * rbs;
		bsum += sir[2] * rbs;
		if (i > 0) {
		    rinsum += sir[0];
		    ginsum += sir[1];
		    binsum += sir[2];
		} else {
		    routsum += sir[0];
		    goutsum += sir[1];
		    boutsum += sir[2];
		}
	    }
	    stackpointer = radius;

	    for (x = 0; x < w; x++) {

		r[yi] = dv[rsum];
		g[yi] = dv[gsum];
		b[yi] = dv[bsum];

		rsum -= routsum;
		gsum -= goutsum;
		bsum -= boutsum;

		stackstart = stackpointer - radius + div;
		sir = stack[stackstart % div];

		routsum -= sir[0];
		goutsum -= sir[1];
		boutsum -= sir[2];

		if (y == 0) {
		    vmin[x] = Math.min(x + radius + 1, wm);
		}
		p = pix[yw + vmin[x]];

		sir[0] = (p & 0xff0000) >> 16;
		sir[1] = (p & 0x00ff00) >> 8;
		sir[2] = (p & 0x0000ff);

		rinsum += sir[0];
		ginsum += sir[1];
		binsum += sir[2];

		rsum += rinsum;
		gsum += ginsum;
		bsum += binsum;

		stackpointer = (stackpointer + 1) % div;
		sir = stack[(stackpointer) % div];

		routsum += sir[0];
		goutsum += sir[1];
		boutsum += sir[2];

		rinsum -= sir[0];
		ginsum -= sir[1];
		binsum -= sir[2];

		yi++;
	    }
	    yw += w;
	}
	for (x = 0; x < w; x++) {
	    rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
	    yp = -radius * w;
	    for (i = -radius; i <= radius; i++) {
		yi = Math.max(0, yp) + x;

		sir = stack[i + radius];

		sir[0] = r[yi];
		sir[1] = g[yi];
		sir[2] = b[yi];

		rbs = r1 - Math.abs(i);

		rsum += r[yi] * rbs;
		gsum += g[yi] * rbs;
		bsum += b[yi] * rbs;

		if (i > 0) {
		    rinsum += sir[0];
		    ginsum += sir[1];
		    binsum += sir[2];
		} else {
		    routsum += sir[0];
		    goutsum += sir[1];
		    boutsum += sir[2];
		}

		if (i < hm) {
		    yp += w;
		}
	    }
	    yi = x;
	    stackpointer = radius;
	    for (y = 0; y < h; y++) {
		// Preserve alpha channel: ( 0xff000000 & pix[yi] )
		pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

		rsum -= routsum;
		gsum -= goutsum;
		bsum -= boutsum;

		stackstart = stackpointer - radius + div;
		sir = stack[stackstart % div];

		routsum -= sir[0];
		goutsum -= sir[1];
		boutsum -= sir[2];

		if (x == 0) {
		    vmin[y] = Math.min(y + r1, hm) * w;
		}
		p = x + vmin[y];

		sir[0] = r[p];
		sir[1] = g[p];
		sir[2] = b[p];

		rinsum += sir[0];
		ginsum += sir[1];
		binsum += sir[2];

		rsum += rinsum;
		gsum += ginsum;
		bsum += binsum;

		stackpointer = (stackpointer + 1) % div;
		sir = stack[stackpointer];

		routsum += sir[0];
		goutsum += sir[1];
		boutsum += sir[2];

		rinsum -= sir[0];
		ginsum -= sir[1];
		binsum -= sir[2];

		yi += w;
	    }
	}

	Log.e("pix", w + " " + h + " " + pix.length);
	bitmap.setPixels(pix, 0, w, 0, 0, w, h);

	if (bitmap != sentBitmap) {
	    sentBitmap.recycle();
	}

	return (bitmap);
    }

    /**
     * Get the radius of the profile picture bubble normalized to the screen size
     * 
     * @param context
     * @return
     */
    public static int getBubbleRadius(Context context) {
	Point screenSize = DeviceInfoUtil.getScreenSize(context, true);
	int divFactor = 1;
	int densityDPI = DeviceInfoUtil.getDensity(context);
	switch (densityDPI) {
	case DisplayMetrics.DENSITY_DEFAULT:
	    divFactor = 8;
	    break;
	case DisplayMetrics.DENSITY_HIGH:
	    divFactor = 8;
	    break;
	case DisplayMetrics.DENSITY_LOW:
	    divFactor = 8;
	    break;
	case DisplayMetrics.DENSITY_XHIGH:
	    divFactor = 12;
	    break;
	case DisplayMetrics.DENSITY_XXHIGH:
	    divFactor = 22;
	    break;
	}
	int radius = (int) (screenSize.x / divFactor);
	return radius;

    }

    /**
     * Spl. calculation for user profile pic
     * 
     * @param context
     * @return
     */
    public static int getProfilePicBubbleRadius(Context context) {
	Point screenSize = DeviceInfoUtil.getScreenSize(context, true);
	int divFactor = 1;
	int densityDPI = DeviceInfoUtil.getDensity(context);
	switch (densityDPI) {
	case DisplayMetrics.DENSITY_DEFAULT:
	    divFactor = 12;
	    break;
	case DisplayMetrics.DENSITY_HIGH:
	    divFactor = 12;
	    break;
	case DisplayMetrics.DENSITY_LOW:
	    divFactor = 12;
	    break;
	case DisplayMetrics.DENSITY_XHIGH:
	    divFactor = 18;
	    break;
	case DisplayMetrics.DENSITY_XXHIGH:
	    divFactor = 33;
	    break;
	}
	int radius = (int) (screenSize.x / divFactor);

	return radius;
    }

    public static Bitmap putLayersOverBitmap(Bitmap bitmap, ArrayList<Layer> bitmapLayers, int width, int height) {
	Bitmap result = null;
	result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	Canvas canvas = new Canvas(result);
	canvas.drawColor(0xFFFFFFFF);

	if (bitmap != null) {
	    int bkWidth = bitmap.getWidth();
	    int bkHeight = bitmap.getHeight();
	    int posX = (width - bkWidth) / 2;
	    int posY = (height - bkHeight) / 2;
	    canvas.drawBitmap(bitmap, posX, posY, null);
	}

	if (bitmapLayers != null) {
	    Iterator<Layer> iterator = bitmapLayers.iterator();
	    Layer tempLayer;
	    while (iterator.hasNext()) {
		tempLayer = iterator.next();
		if (tempLayer.getBitmap() != null) {
		    canvas.drawBitmap(tempLayer.getBitmap(), tempLayer.getMatrix(), null);
		}
	    }

	}

	return result;
    }

    /**
     * Decodes image and scales it to reduce memory consumption
     * @param f
     * @return
     */
    public static Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }    
}
