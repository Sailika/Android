package com.paradigmcreatives.apspeak.app.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.ASSET_TAG;
import com.paradigmcreatives.apspeak.app.model.ASSET_TYPE;
import com.paradigmcreatives.apspeak.app.model.GENDER;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;

/**
 * This class contains all the utilities which are required for this project
 * 
 * @author Dileep | neuv
 * 
 */
public class Util {

	private static final String TAG = "Util";

	private static Toast toast;

	/**
	 * This method is used to get the path of the SD card
	 * 
	 * @return: Path to the SD card
	 */
	public static String getSdCardPath() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getPath()
					+ File.separator;
		} else {
			return null;
		}
	}

	/**
	 * This method returns the color int value for the given color String
	 * 
	 * @param colorCode
	 *            :The color string
	 * @return :color value in int
	 */
	public static int returnColorValue(String colorCode) {
		int color = 0xffffff;
		try {
			color = Color.parseColor(colorCode);
		} catch (IllegalArgumentException iae) {
			Logger.warn(
					TAG,
					"Error while parsing color string : "
							+ iae.getLocalizedMessage());
		} catch (Exception e) {
			Logger.warn(
					TAG,
					"Unknown error while parsing color string : "
							+ e.getLocalizedMessage());
		}
		return color;
	}

	/**
	 * Gets the initial from the string
	 * 
	 * @param originalString
	 * @return
	 */
	public static String getInitialsFromString(String originalString) {
		String initial = null;
		if (TextUtils.isEmpty(originalString)) {
			initial = "?";
		} else {
			String initialLetter = null;
			try {
				initialLetter = originalString.substring(0, 1);
			} catch (IndexOutOfBoundsException iobe) {
				initialLetter = null;
				Logger.warn(
						TAG,
						"Error while getting initials : "
								+ iobe.getLocalizedMessage());
			} catch (Exception e) {
				initialLetter = null;
				Logger.warn(
						TAG,
						"Error while getting initials : "
								+ e.getLocalizedMessage());
			}
			if (TextUtils.isEmpty(initialLetter)) {
				initial = "?";
			} else {
				Pattern p = Pattern.compile("[a-zA-Z]");
				if (p.matcher(initialLetter).find()) {
					initial = initialLetter;
				} else {
					initial = "?";
				}
			}
		}

		if (TextUtils.isEmpty(initial)) {
			initial = "?";
		}
		return initial;
	}

	/**
	 * This method converts the response from the server to a user
	 * understandable format
	 * 
	 * @param is
	 *            : Response from the server
	 * @return: String in a user understandable format
	 */
	public static String convertingInputToString(InputStream is) {
		StringBuffer buffer = new StringBuffer();
		byte[] b = new byte[4096];
		try {
			for (int n; (n = is.read(b)) != -1;) {
				buffer.append(new String(b, 0, n));
			}
			String str = buffer.toString();
			Log.v(TAG, "result string : " + str);
			return str;
		} catch (IOException e) {
			Logger.logStackTrace(e);
		} catch (IndexOutOfBoundsException ioobe) {
			Logger.logStackTrace(ioobe);
		} catch (Exception e) {
			Logger.logStackTrace(e);
		}
		return null;

	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			java.net.URL url = new java.net.URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Calculate time difference between post date and current date in
	 * years,weeks,days ,hours,minutes...
	 * 
	 * 
	 * @param time
	 *            is posted date.
	 * @return
	 */
	public static String getdate(long time) {
		String result = null;
		long starttime = time * 1000;
		long presettime = System.currentTimeMillis();

		try {

			// in milliseconds
			long diff = presettime - starttime;
			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			long weeks = diffDays / 7;
			long diffyears = diffDays / 365;

			if (diffyears == 1) {
				result = diffyears + " year ago";
			} else if (diffyears > 1) {

				result = diffyears + " years ago";
			} else if (weeks == 1) {
				result = weeks + " week ago";
			} else if (weeks > 1) {
				result = weeks + " weeks ago";
			} else if (diffDays == 1) {

				result = diffDays + " day ago";

			} else if (diffDays > 1) {

				result = diffDays + " days ago";

			} else if (diffHours == 1) {

				result = diffHours + " hour ago";

			} else if (diffHours > 1) {

				result = diffHours + " hours ago";

			} else if (diffMinutes == 1) {

				result = diffMinutes + " minute ago";
			} else if (diffMinutes > 1) {

				result = diffMinutes + " minutes ago";
			} else if (diffSeconds >= 1) {
				result = "just now";
			}

		} catch (Exception e) {
			e.printStackTrace();
			result = "NA";
		}

		return result;
	}

	/**
	 * Converts the given type in string to GENDER
	 * 
	 * @param type
	 * @return
	 */
	public static GENDER convertToGENDER(String type) {
		if (type != null) {
			if (type.equalsIgnoreCase(GENDER.MALE.name())) {
				return GENDER.MALE;
			} else if (type.equalsIgnoreCase(GENDER.FEMALE.name())) {
				return GENDER.FEMALE;
			} else {
				return GENDER.OTHERS;
			}
		}
		return null;
	}

	/**
	 * Converts the given type in string to ASSERT_TYPE
	 * 
	 * @param type
	 * @return
	 */
	public static ASSET_TYPE convertToAssetType(String type) {
		if (type != null) {
			if (type.equals(ASSET_TYPE.DOODLE.name())) {
				return ASSET_TYPE.DOODLE;
			} else if (type.equals(ASSET_TYPE.EMOJI.name())) {
				return ASSET_TYPE.EMOJI;
			} else if (type.equals(ASSET_TYPE.GREETING.name())) {
				return ASSET_TYPE.GREETING;
			} else if (type.equals(ASSET_TYPE.RAGE_FACE.name())) {
				return ASSET_TYPE.RAGE_FACE;
			} else if (type.equals(ASSET_TYPE.IMAGE.name())) {
				return ASSET_TYPE.IMAGE;
			}
		}
		return null;
	}

	/**
	 * Converts the given tag in string to ASSERT_TAG
	 * 
	 * @param tag
	 * @return
	 */
	public static ASSET_TAG convertToAssetTag(String tag) {
		if (tag != null) {
			if (tag.equals(ASSET_TAG.SPORTS.name())) {
				return ASSET_TAG.SPORTS;
			} else if (tag.equals(ASSET_TAG.MOVIES.name())) {
				return ASSET_TAG.MOVIES;
			} else if (tag.equals(ASSET_TAG.CRICKET.name())) {
				return ASSET_TAG.CRICKET;
			} else if (tag.equals(ASSET_TAG.FOOD.name())) {
				return ASSET_TAG.FOOD;
			} else if (tag.equals(ASSET_TAG.ENTERTAINMENT.name())) {
				return ASSET_TAG.ENTERTAINMENT;
			}
		}
		return null;
	}

	/**
	 * Converts the given stream type in string to STREAM_TYPE
	 * 
	 * @param streamType
	 * @return
	 */
	public static STREAM_TYPE convertToStreamType(String streamType) {
		if (streamType != null) {
			if (streamType.equals(STREAM_TYPE.MAIN_STREAM.name())) {
				return STREAM_TYPE.MAIN_STREAM;
			} else if (streamType.equals(STREAM_TYPE.PERSONAL_STREAM.name())) {
				return STREAM_TYPE.PERSONAL_STREAM;
			} else if (streamType.equals(STREAM_TYPE.COMMENTS_STREAM.name())) {
				return STREAM_TYPE.COMMENTS_STREAM;
			} else if (streamType.equals(STREAM_TYPE.COLLEGE.name())) {
				return STREAM_TYPE.COLLEGE;
			} else if (streamType.equals(STREAM_TYPE.ALLCOLLEGES.name())) {
				return STREAM_TYPE.ALLCOLLEGES;
			} else if (streamType.equals(STREAM_TYPE.FRIENDS.name())) {
				return STREAM_TYPE.FRIENDS;
			}
		}
		return null;
	}

	/**
	 * This method compresses the doodle
	 * 
	 * @param srcFolder
	 *            : The path of the doodle before compression
	 * @param destZipFile
	 *            : The path of the doodle before compression
	 */
	public static void zipFolder(String srcFolder, String destZipFile) {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		try {
			fileWriter = new FileOutputStream(destZipFile);
			zip = new ZipOutputStream(fileWriter);
			addFolderToZip("", srcFolder, zip);
			zip.flush();
			zip.close();
		} catch (FileNotFoundException e) {
			Logger.logStackTrace(e);
		} catch (IOException e) {
			Logger.logStackTrace(e);
		} catch (Exception e) {
			Logger.logStackTrace(e);
		}
	}

	/**
	 * This method adds all the required contents of a doodle in to a single
	 * folder
	 * 
	 * @param path
	 *            : Path of the doodle data
	 * @param srcFile
	 *            : Name of the file
	 * @param zip
	 *            :Instance of a Compression class
	 */
	private static void addFileToZip(String path, String srcFile,
			ZipOutputStream zip) {
		File folder = new File(srcFile);
		if (folder.isDirectory()) {
			addFolderToZip(path, srcFile, zip);
		} else {
			byte[] buf = new byte[1024];
			int len;
			FileInputStream in;
			try {
				in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + File.separator
						+ folder.getName()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
				if (in != null) {
					in.close();
				}
			} catch (FileNotFoundException e) {
				Logger.logStackTrace(e);
			} catch (IOException e) {
				Logger.logStackTrace(e);
			} catch (Exception e) {
				Logger.logStackTrace(e);
			}
		}
	}

	/**
	 * This method compresses the entire doodle folder
	 * 
	 * @param path
	 *            : Path of the folder
	 * @param srcFolder
	 *            : Name of the folder
	 * @param zip
	 *            :Instance of a Compression class
	 */
	private static void addFolderToZip(String path, String srcFolder,
			ZipOutputStream zip) {
		File folder = new File(srcFolder);

		if (folder.exists()) {
			if (folder.isDirectory() && (folder.list() != null)) {
				for (String fileName : folder.list()) {
					if (path != null && fileName != null) {
						if (path.equals("")) {
							addFileToZip(folder.getName(), srcFolder
									+ File.separator + fileName, zip);
						} else {
							addFileToZip(
									path + File.separator + folder.getName(),
									srcFolder + File.separator + fileName, zip);
						}
					}
				}
			} else {
				Logger.warn(TAG, "Folder is not a directory with children");
			}
		} else {
			Logger.warn(TAG, "Folder doesn't exist to add to zip");
		}
	}

	/**
	 * Cleans the doodle folder if it exists
	 * 
	 * @param doodleFolderPath
	 * @return <code>true</code> if successful cleaning
	 */
	public static boolean cleanDoodleFolder(String doodleFolderPath) {
		boolean success = true;
		File doodleFile = new File(doodleFolderPath);
		if (doodleFile.exists()) {
			if (doodleFile.isDirectory()) {
				String[] children = doodleFile.list();
				for (int i = 0; i < children.length; i++) {
					success = success
							&& deleteFile(new File(doodleFile, children[i]));
				}
			} else {
				success = false;
			}
		} else {
			success = false;
		}

		return success;
	}

	/**
	 * This method recursively deletes the contents of the folder.
	 * 
	 * @param file
	 *            : The file to delete
	 * @return: Status after deletion
	 */
	public static boolean deleteFile(File file) {
		if (file != null) {
			if (file.isDirectory()) {
				String[] children = file.list();
				for (int i = 0; i < children.length; i++) {
					boolean success = deleteFile(new File(file, children[i]));
					if (!success) {
						return false;
					}
				}
			}
			return file.delete();
		}
		return false;
	}

	/**
	 * This method displays the toast message to the user with the given message
	 * 
	 * @param message
	 *            :Message to be displayed in a toast
	 */
	public static void displayToast(Context context, String message) {
		if (context != null) {
			toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.show();
		} else {
			Logger.warn(TAG, "Context in displayToast()is null");
		}
	}

	/**
	 * Converts the color from int to hex code
	 * 
	 * @param color
	 *            : the int color code
	 * @return : the hex coded color
	 */
	public static String getHexStringForColor(int color) {
		try {
			String hexColor = Integer.toHexString(color);
			hexColor = hexColor.substring(2, hexColor.length());
			return hexColor;
		} catch (StringIndexOutOfBoundsException e) {
			Logger.logStackTrace(e);
		}
		return null;

	}

	/**
	 * Compresses and saves the provided image into external memory. By default
	 * it uses JPEG compression
	 * 
	 * @param root
	 *            : The root directory
	 * @param filename
	 *            : The filename of the image
	 * @param image
	 *            : The bitmap image
	 * @return <code>true</code> on success
	 */
	public static boolean saveImageToExternalMemory(String root,
			String filename, Bitmap image) {
		boolean result = saveImageToExternalMemory(root, filename, image,
				Bitmap.CompressFormat.JPEG);
		return result;
	}

	/**
	 * Compresses and saves the provided image into external memory
	 * 
	 * @param root
	 *            : The root directory
	 * @param filename
	 *            : The filename of the image
	 * @param image
	 *            : The bitmap image
	 * @param format
	 *            : The format in which the image should be compressed
	 * 
	 * @return <code>true</code> on success
	 */
	public static boolean saveImageToExternalMemory(String root,
			String filename, Bitmap image, CompressFormat format) {
		boolean result = false;
		File imageFile;
		BufferedWriter imageWriter = null;
		File file = new File(root);
		try {
			imageFile = new File(file, filename);
			FileWriter imageFileWriter = new FileWriter(imageFile);
			if (imageFileWriter != null) {
				imageWriter = new BufferedWriter(imageFileWriter);
				// Save the image
				if (image != null) {
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					image.compress(format, Constants.COMPRESSION_QUALITY_HIGH,
							stream);
					byte[] byteArray = stream.toByteArray();
					byte[] encodedString = Base64.encode(byteArray,
							Base64.DEFAULT);
					String imageData = new String(encodedString);
					imageWriter.write(imageData);
				}
				result = true;
			}
		} catch (IOException e) {
			Logger.warn(
					TAG,
					"Error while compressing image during save : "
							+ e.getLocalizedMessage());
			Logger.logStackTrace(e);
		} catch (Exception e) {
			Logger.warn(
					TAG,
					"Unknown error while compressing image during save : "
							+ e.getLocalizedMessage());
			Logger.logStackTrace(e);
		} finally {
			try {
				imageWriter.close();
			} catch (IOException e) {
				Logger.warn(TAG, "Error while saving image" + e);
			} catch (Exception e) {
				Logger.warn(TAG, "Error while saving image" + e);
			}
		}

		return result;
	}

	public static boolean saveBitampToTempLocation(Context context,
			Bitmap bitmap) {
		boolean saved = false;
		try {
			if (context != null && bitmap != null) {
				if (DeviceInfoUtil.mediaWritable()) {
					String appRoot = AppPropertiesUtil.getAppDirectory(context);
					String tempFolder = context.getResources().getString(
							R.string.temp_folder);
					File tempFile = new File(appRoot, tempFolder);
					if (!tempFile.exists()) {
						tempFile.mkdir();
					}

					String tempFolderPath = tempFile.getAbsolutePath();
					File imageFile = new File(tempFolderPath, context
							.getResources().getString(
									R.string.temp_thumbnail_bitmap));
					FileOutputStream out = new FileOutputStream(imageFile);

					// TODO get the water-marked bitmap and save it
					Bitmap watermarkedBitmap = ImageUtil.watermarkDoodlyDoo(
							context, bitmap);
					if (watermarkedBitmap != null) {
						saved = watermarkedBitmap.compress(CompressFormat.PNG,
								Constants.COMPRESSION_QUALITY_HIGH, out);
						watermarkedBitmap.recycle();
					} else {
						saved = bitmap.compress(CompressFormat.PNG,
								Constants.COMPRESSION_QUALITY_HIGH, out);
					}
				} else {
					Logger.warn(TAG,
							"Can't save bitmap to file. No memory card");
				}

			} else {
				Logger.warn(TAG,
						"Either context is null or the supplied bitmap is null. Conext - "
								+ context + " Bitmap - " + bitmap);
			}
		} catch (Exception e) {
			Logger.warn(TAG, e.getMessage());
		}
		return saved;
	}

	/**
	 * This method checks whether the user is connected to a network or not
	 * 
	 * @param mContext
	 * @return :true, if the user is connected to a network ,else returns false
	 */
	public static boolean isOnline(Context context) {
		if (context == null) {
			return false;
		}

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnectedOrConnecting();
		} else {
			return false;
		}
	}

	/**
	 * This method is used to convert String value to Integer value
	 * 
	 * @param value
	 *            String value
	 * @param default_value
	 *            if string value is null default value is used
	 * @return Integer value
	 */
	public static int convertStringToInt(String value, int default_value) {
		int result = default_value;

		if (!TextUtils.isEmpty(value)) {
			try {
				result = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				Logger.logStackTrace(e);
				result = default_value;
			} catch (Exception e) {
				e.printStackTrace();
				result = default_value;
			}
		}
		return result;
	}

	/**
	 * This method extracts the base 64 string of the doodle thumbnail from the
	 * file thumbnail.txt file
	 * 
	 * @param root
	 *            : The root folder
	 * @param fileName
	 *            : The name of the file containing the string
	 * @return : The string content of the file
	 */
	public static String getStringFromFile(String root, String fileName) {
		if (root != null) {
			File rootFolder = new File(root);
			File file = new File(rootFolder, fileName);
			String fileData = null;
			FileReader fileReader = null;
			StringBuilder stringBuilder = new StringBuilder();
			try {
				if (file.exists()) {
					fileReader = new FileReader(file.getAbsolutePath());
					if (fileReader != null) {
						BufferedReader bufferReader = new BufferedReader(
								fileReader);
						if (bufferReader != null) {
							while ((fileData = bufferReader.readLine()) != null) {
								stringBuilder.append(fileData);
							}
							bufferReader.close();
						}
					}
				}
			} catch (FileNotFoundException e) {
				Logger.logStackTrace(e);
			} catch (IOException e) {
				Logger.logStackTrace(e);
			} catch (Exception e) {
				Logger.logStackTrace(e);
			} finally {
				try {
					if (fileReader != null)
						fileReader.close();
				} catch (IOException e) {
					Logger.logStackTrace(e);
				} catch (Exception e) {
					Logger.logStackTrace(e);
				}
			}
			if (stringBuilder.toString().length() > 0) {
				return stringBuilder.toString();
			} else {
				return null;
			}
		}
		return null;
	}

	/**
	 * Decompresses the image from the encoded text file
	 * 
	 * @param root
	 *            : The root folder path
	 * @param filename
	 *            : The file name storing the compressed image data
	 * @return : The bitmap image
	 */
	public static Bitmap decompressImage(String root, String filename) {
		Bitmap image = null;
		if (root != null) {
			byte imageContent[] = null;
			try {
				String doodleThumbnailData = getStringFromFile(root, filename);
				if (doodleThumbnailData != null) {
					imageContent = Base64.decode(doodleThumbnailData,
							Base64.DEFAULT);
				} else {
					Logger.warn(TAG + "File is not available");
				}
			} catch (IllegalArgumentException e) {
				Logger.logStackTrace(e);
			} catch (Exception e) {
				Logger.logStackTrace(e);
			}
			if (imageContent != null) {
				image = BitmapFactory.decodeByteArray(imageContent, 0,
						imageContent.length);
			}
		}

		return image;
	}

	public static Bitmap getBitmap(String url, Context context) {
		String filename = String.valueOf(url.hashCode());
		String tempFileName = AppPropertiesUtil.getAppDirectory(context) + "/"
				+ filename;
		File f = new File(tempFileName);
		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}

	}

	// decodes image and scales it to reduce memory consumption
	private static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	/**
	 * This method helps in composing email
	 * 
	 * @param smsBody
	 *            : Content of the message
	 * @param targetNumber
	 * @return: An instance of email intent
	 */
	public static Intent getPreFormattedEmailIntent(Context context) {
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		if (context == null) {
			return null;
		}

		emailIntent.setType("plain/text");

		emailIntent.putExtra(
				android.content.Intent.EXTRA_EMAIL,
				new String[] { context.getResources().getString(
						R.string.app_email) });

		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, context
				.getResources().getString(R.string.email_subject));

		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, context
				.getResources().getString(R.string.email_body));
		return emailIntent;

	}

}
