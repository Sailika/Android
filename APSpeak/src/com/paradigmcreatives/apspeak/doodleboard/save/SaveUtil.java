package com.paradigmcreatives.apspeak.doodleboard.save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.DeviceInfoUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.DoodleViewProperties;
import com.paradigmcreatives.apspeak.doodleboard.StrokeBean;
import com.paradigmcreatives.apspeak.doodleboard.layers.Layer;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Util class for saving files to SD card
 * 
 * @author Soumya Behera
 * 
 */
public class SaveUtil {

	private static final String TAG = "SaveUtil";

	/**
	 * This method creates the required files and/or folders for the doodle
	 * 
	 * @param context
	 * @return : The path of the doodle created
	 */
	public static String createEmptyDoodleFolder(Context context) {
		String doodlePath = null;
		try {
			if (context == null) {
				Logger.warn(TAG, "Context is null");
				return doodlePath;
			}
			SharedPreferences prefs = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);
			String appDirectory = AppPropertiesUtil.getAppDirectory(context);
			if ((appDirectory != null) && (DeviceInfoUtil.mediaWritable())) {
				File doodlesRootFolder = new File(appDirectory, context.getResources()
						.getString(R.string.whatsay_folder));
				if (!doodlesRootFolder.exists()) {
					doodlesRootFolder.mkdir();
				}
				int count = prefs.getInt(Constants.DOODLE_SAVE_COUNT, 0);
				String doodleFolderPath = doodlesRootFolder.getAbsolutePath() + "/"
						+ context.getResources().getString(R.string.doodle_file_prefix);
				File doodleFolder = new File(doodleFolderPath + count);

				while (doodleFolder.exists()) {
					++count;
					doodleFolder = new File(doodleFolderPath + count);
				}
				boolean fileCreated = doodleFolder.mkdir();
				doodlePath = doodleFolder.getAbsolutePath();
				if ((doodleFolder != null) && (!TextUtils.isEmpty(doodlePath)) && fileCreated) {
					SharedPreferences.Editor prefsEditor = prefs.edit();
					prefsEditor.putInt(Constants.DOODLE_SAVE_COUNT, count + 1);
					prefsEditor.commit();
					new File(doodlePath, context.getResources().getString(R.string.doodle_points_file_name));
					new File(doodlePath, context.getResources().getString(R.string.doodle_settings_file_name));
					new File(doodlePath, context.getResources().getString(R.string.doodle_thumbnail_file_name));
				}
			}
		} catch (Exception e) {
			Logger.warn(TAG, e.getLocalizedMessage());
			Logger.logStackTrace(e);
		}
		return doodlePath;
	}

	/**
	 * Creates empty doodle folder in temp location
	 * 
	 * @param context
	 */
	public static String createEmptyDoodleInTempFolder(Context context) {
		String doodlePath = null;
		if (context != null) {
			if (DeviceInfoUtil.mediaWritable()) {
				String appRoot = AppPropertiesUtil.getAppDirectory(context);
				String tempFolder = context.getResources().getString(R.string.temp_folder);
				File tempFile = new File(appRoot, tempFolder);
				if (!tempFile.exists()) {
					tempFile.mkdir();
				}

				String tempFolderPath = tempFile.getAbsolutePath();
				File doodleFolder = new File(tempFolderPath, context.getResources().getString(R.string.whatsay_folder));
				if (!doodleFolder.exists()) {
					doodleFolder.mkdir();
				}

				doodlePath = doodleFolder.getAbsolutePath();
			} else {
				Logger.warn(TAG, "Can't create empty doodle temp folder. No memory card");
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}
		return doodlePath;
	}

	/**
	 * Creates empty folder with asset_id inside Doodly Doo/doodle/ directory to download a doodle. Any downloaded
	 * doodle folder will be saved with asset_id name. For example: Doodly Doo/doodle/<asset_id>/
	 * 
	 * @param context
	 */
	public static String createEmptyDoodleAssetFolder(Context context, String assetId) {
		String doodlePath = null;
		if (context != null && !TextUtils.isEmpty(assetId)) {
			if (DeviceInfoUtil.mediaWritable()) {
				String appRoot = AppPropertiesUtil.getAppDirectory(context);
				String doodleFolderPath = context.getResources().getString(R.string.whatsay_folder);
				File doodleFile = new File(appRoot, doodleFolderPath);
				if (!doodleFile.exists()) {
					doodleFile.mkdirs();
				}

				String assetFolderPath = doodleFile.getAbsolutePath();
				File doodleFolder = new File(assetFolderPath, assetId + "/");
				if (!doodleFolder.exists()) {
					doodleFolder.mkdirs();
				}

				doodlePath = doodleFolder.getAbsolutePath();
			} else {
				Logger.warn(TAG, "Can't create doodle folder with assetId name. No memory card");
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}
		return doodlePath;
	}

	/**
	 * Creates an empty download folder
	 * 
	 * @param context
	 * @return : The path of the new doodle folder
	 */
	public static String createEmptyDownloadDoodleFolder(Context context) {
		String downloadDoodlePath = null;
		try {
			if (context == null) {
				Logger.warn(TAG, "Context is null");
				return downloadDoodlePath;
			}
			String appDirectory = AppPropertiesUtil.getAppDirectory(context);
			if (appDirectory != null && DeviceInfoUtil.mediaWritable()) {
				File file = null;
				String doodleFolder = context.getResources().getString(R.string.whatsay_folder);
				String doodleDownloads = context.getResources().getString(R.string.doodle_downloads);
				String doodleFilePrefix = context.getResources().getString(R.string.doodle_file_prefix);
				file = new File(appDirectory, doodleFolder);
				if (!file.exists()) {
					file.mkdir();
				}

				String doodleRootFolderPath = appDirectory + "/" + doodleFolder;
				file = new File(doodleRootFolderPath, doodleDownloads);
				if (!file.exists()) {
					file.mkdir();
				}

				String doodleDownloadFolderPath = file.getAbsolutePath();
				String mPath = doodleDownloadFolderPath + "/" + doodleFilePrefix;
				SharedPreferences prefs = context.getSharedPreferences(Constants.APP_NAME, Context.MODE_PRIVATE);

				int count = prefs.getInt(Constants.DOODLE_RECEIVE_COUNT, 0);
				file = new File(mPath + count);
				while (file.exists()) {
					++count;
					file = new File(mPath + count);
				}
				boolean fileCreated = file.mkdir();
				downloadDoodlePath = file.getAbsolutePath();
				if ((file != null) && (!TextUtils.isEmpty(downloadDoodlePath)) && fileCreated) {
					SharedPreferences.Editor prefsEditor = prefs.edit();
					prefsEditor.putInt(Constants.DOODLE_RECEIVE_COUNT, count + 1);
					prefsEditor.commit();
				}
			}
		} catch (Exception e) {
			Logger.warn(TAG, e.getLocalizedMessage());
			Logger.logStackTrace(e);
		}
		return downloadDoodlePath;
	}

	/**
	 * Checks whether asset_id folder already exists or not
	 * 
	 * @param context
	 * @param asset_id
	 * @return
	 */
	public static boolean isAssetFolderExists(Context context, String asset_id){
		boolean isExists = false;
		if(context != null && !TextUtils.isEmpty(asset_id)){
			String appRoot = AppPropertiesUtil.getAppDirectory(context);
			String doodleFolderPath = context.getResources().getString(R.string.whatsay_folder);
			File doodleFolder = new File(appRoot, doodleFolderPath);
			if(doodleFolder.exists()){
				String assetFolderPath = doodleFolder.getAbsolutePath();
				File assetFolder = new File(assetFolderPath, asset_id + "/");
				if(assetFolder.exists()){
					isExists = true;
				}
			}
		}
		return isExists;
	}
	
	/**
	 * This method is used to call methods to store all details of doodle in the SD card
	 * 
	 * @param context
	 *            : Context to be used
	 * @param systemUniqueKey
	 *            : The system generated unique key
	 * @param doodleViewProperties
	 *            : Doodle view properties
	 * @param doodlePoints
	 *            : array list of doodle points
	 * @param thumbnail
	 *            : Thumbnail of the doodle
	 * @param receiverContactId
	 *            : The receiver's contact id
	 * @param doodlePath
	 *            : Path in which doodle files needs to be created
	 * 
	 * @return The path of the doodle saved
	 */
	public String saveDoodleToTempLocation(final Context context, String systemUniqueKey,
			DoodleViewProperties doodleViewProperties, ArrayList<Object> doodlePoints, Bitmap thumbnail,
			String doodlePath) {
		String insertedDoodlePath = null;
		if (context != null) {
			if (TextUtils.isEmpty(doodlePath)) {
				doodlePath = createEmptyDoodleInTempFolder(context);
			} else {
				if (Util.cleanDoodleFolder(doodlePath)) {
					new File(doodlePath, context.getString(R.string.doodle_points_file_name));
					new File(doodlePath, context.getString(R.string.doodle_settings_file_name));
					new File(doodlePath, context.getString(R.string.doodle_thumbnail_file_name));
				}
			}

			if (doodlePath != null) {
				if (saveDoodlePoints(context, doodlePath, doodlePoints)
						&& saveDoodleSettings(context, doodlePath, doodleViewProperties)
						&& saveDoodleThumbnail(context, doodlePath, thumbnail)) {
					// If there is a background or there exists layers
					if (doodleViewProperties.getBackgroundBitmap() != null
							|| (doodleViewProperties.getBitmapLayers() != null && doodleViewProperties
									.getBitmapLayers().size() > 0)) {
						saveDoodleBackgroundImage(context, doodlePath, doodleViewProperties);
					}
					insertedDoodlePath = doodlePath;
				} else {
					Logger.warn(TAG, "doodle is not saved properly");
				}
			} else {
				Logger.warn(TAG, "Could not create doodle root folder");
			}
		} else {
			Logger.warn(TAG, "Context is null");
		}

		return insertedDoodlePath;
	}

	/**
	 * This method is used to save doodle points in a file
	 * 
	 * @param context
	 *            : Context to be used
	 * @param doodlePath
	 *            : The doodle root folder path
	 * @param doodlePointsList
	 *            : Array list of doodle points
	 * 
	 * @return : boolean value
	 */
	public boolean saveDoodlePoints(final Context context, String doodlePath, ArrayList<Object> doodlePointsList) {
		boolean result = false;
		File doodlePointsFile;

		if (context == null) {
			Logger.warn(TAG, "Context is null");
			return result;
		}

		BufferedWriter doodlePointsWriter = null;
		if (DeviceInfoUtil.mediaWritable()) {
			File file = new File(doodlePath);
			try {
				doodlePointsFile = new File(file, context.getResources().getString(R.string.doodle_points_file_name));

				FileWriter doodlePointsFileWriter = new FileWriter(doodlePointsFile);

				if (doodlePointsFileWriter != null) {
					doodlePointsWriter = new BufferedWriter(doodlePointsFileWriter);

					if (doodlePointsList != null) {
						Iterator<Object> iterator = doodlePointsList.iterator();
						Object object = null;
						PointF point = null;
						StrokeBean stroke = null;
						String lineBreak = null;

						// Save doodle points
						while (iterator.hasNext()) {
							object = iterator.next();

							if (object instanceof PointF) { // Save the point
								point = (PointF) object;
								float i = point.x;
								float j = point.y;
								String s = String.format("p %s %s", i, j);
								doodlePointsWriter.write(s);
								doodlePointsWriter.newLine();
							} else if (object instanceof StrokeBean) {// save color
								stroke = (StrokeBean) object;
								if (!TextUtils.isEmpty(stroke.getColor())) {
									doodlePointsWriter.write("c " + stroke.getColor());
									doodlePointsWriter.newLine();
								}

								if (stroke.getSize() > 0) {// save stroke width
									doodlePointsWriter.write("w " + stroke.getSize() + "");
									doodlePointsWriter.newLine();
								}
							} else if (object instanceof String) {// write LINE_BREAK
								lineBreak = (String) object;
								if (lineBreak.equals(DoodleView.LINE_BREAK)) {
									doodlePointsWriter.write(DoodleView.LINE_BREAK);
									doodlePointsWriter.newLine();
								}
							}
						}
						result = true;
					}
				}
			} catch (IOException e) {
				Logger.logStackTrace(e);
			} catch (Exception e) {
				Logger.logStackTrace(e);
				e.printStackTrace();
			} finally {
				try {
					doodlePointsWriter.close();
				} catch (IOException e) {
					Logger.warn(TAG, "Error while saving" + e);
				} catch (Exception e) {
					Logger.warn(TAG, "Error while saving" + e);
				}
			}
		} else {
			Util.displayToast(context, context.getResources().getString(R.string.no_memory_card));
		}

		return result;
	}

	/**
	 * This method is used to save doodle screen properties in a file
	 * 
	 * @param context
	 *            , : Context to be used
	 * @param doodlePath
	 *            : The doodle root folder path
	 * @param doodleViewProperties
	 *            :Doodle view properties
	 * 
	 * @return: boolean value
	 */
	public boolean saveDoodleSettings(final Context context, String doodlePath,
			DoodleViewProperties doodleViewProperties) {
		boolean result = false;
		File doodleSettingsFile;

		if (context == null) {
			Logger.warn(TAG, "Context is null");
			return result;
		}

		BufferedWriter doodleSettingsWriter = null;
		if (DeviceInfoUtil.mediaWritable()) {
			File file = new File(doodlePath);
			try {
				doodleSettingsFile = new File(file, context.getResources()
						.getString(R.string.doodle_settings_file_name));

				FileWriter doodleSettingsFileWriter = new FileWriter(doodleSettingsFile);

				if (doodleSettingsFileWriter != null) {
					Logger.info(TAG, "Doodle save doodle properties : " + doodleViewProperties.toString());
					doodleSettingsWriter = new BufferedWriter(doodleSettingsFileWriter);

					// Save the DoodleView properties
					if (doodleViewProperties != null) {
						int width = doodleViewProperties.getScreenWidth();
						int height = doodleViewProperties.getScreenHeight();
						if (width > 0 && height > 0) {
							doodleSettingsWriter.write("s " + width + " " + height);
							doodleSettingsWriter.newLine();
						}
						String colorString = Util.getHexStringForColor(doodleViewProperties.getBackgroundColor());
						if (colorString == null) {
							colorString = "FFFFFF";
						}
						doodleSettingsWriter.write("c " + colorString);
						doodleSettingsWriter.newLine();
						result = true;
					}
				}
			} catch (IOException e) {
				Logger.logStackTrace(e);
			} catch (Exception e) {
				Logger.logStackTrace(e);
			} finally {
				try {
					doodleSettingsWriter.close();
				} catch (IOException e) {
					Logger.warn(TAG, "Error while saving" + e);
				} catch (Exception e) {
					Logger.warn(TAG, "Error while saving" + e);
				}
			}
		} else {
			Util.displayToast(context, context.getResources().getString(R.string.no_memory_card));
		}

		return result;
	}

	/**
	 * This method is used to save doodle thumbnail in a file
	 * 
	 * @param context
	 *            : Context to be used
	 * @param doodlePath
	 *            : The doodle root folder path
	 * @param thumbnail
	 *            : Thumbnail
	 * 
	 * @return : boolean value
	 */
	public boolean saveDoodleThumbnail(final Context context, String doodlePath, Bitmap thumbnail) {
		boolean result = false;
		if (context == null) {
			Logger.warn(TAG, "Context is null");
			return result;
		}

		if (DeviceInfoUtil.mediaWritable()) {
			result = Util.saveImageToExternalMemory(doodlePath,
					context.getResources().getString(R.string.doodle_thumbnail_file_name), thumbnail);
			// Save the thumbnail to a temporary location
			result = result && Util.saveBitampToTempLocation(context, thumbnail);
		} else {
			Util.displayToast(context, context.getResources().getString(R.string.no_memory_card));
		}

		return result;
	}

	/**
	 * This method is used to save doodle background image in a file
	 * 
	 * @param context
	 *            : Context to be used
	 * @param doodlePath
	 *            : The doodle root folder path
	 * @param backgroundImage
	 *            : The background image
	 * @return :<code>true</code> in case of success
	 */
	public boolean saveDoodleBackgroundImage(final Context context, String doodlePath,
			DoodleViewProperties doodleViewProperties) {
		boolean result = false;
		if (context == null || doodleViewProperties == null) {
			Logger.warn(TAG, "Context is null");
			return result;
		}

		Bitmap backgroundImage = doodleViewProperties.getBackgroundBitmap();
		ArrayList<Layer> bitmapLayers = doodleViewProperties.getBitmapLayers();

		if (DeviceInfoUtil.mediaWritable()) {
			backgroundImage = ImageUtil.putLayersOverBitmap(backgroundImage, bitmapLayers,
					doodleViewProperties.getScreenWidth(), doodleViewProperties.getScreenHeight());
			result = Util.saveImageToExternalMemory(doodlePath,
					context.getResources().getString(R.string.doodle_background_file_name), backgroundImage);
		} else {
			Util.displayToast(context, context.getResources().getString(R.string.no_memory_card));
		}

		return result;
	}

}
