package com.paradigmcreatives.apspeak.app.util;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import com.paradigmcreatives.apspeak.logging.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class DeviceInfoUtil {
    private static final String TAG = "DeviceInfoUtil";

    Context mContext;
    String versionName, packageName, filePath, phoneModel, androidVersion, board, brand, device, display, fingerPrint,
	    host, id, model, product, tags, time, type, user, totalInternalMemory, availableInternalMemory,
	    screenResolution;

    public DeviceInfoUtil(Context mContext) {
	this.mContext = mContext;
    }

    public static String getLocalIPAddress() {
	try {
	    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
		NetworkInterface intf = en.nextElement();
		for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
		    InetAddress inetAddress = enumIpAddr.nextElement();
		    if (!inetAddress.isLoopbackAddress()) {
			return inetAddress.getHostAddress().toString();
		    }
		}
	    }
	} catch (SocketException ex) {
	    Logger.warn(TAG, ex.getLocalizedMessage());
	}
	return "";
    }

    /**
     * Get IP address from first non-localhost interface
     * 
     * @param ipv4
     *            true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    public static String getIPAddress(boolean useIPv4) {
	try {
	    List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
	    for (NetworkInterface intf : interfaces) {
		List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
		for (InetAddress addr : addrs) {
		    if (!addr.isLoopbackAddress()) {
			String sAddr = addr.getHostAddress().toUpperCase();
			boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
			if (useIPv4) {
			    if (isIPv4)
				return sAddr;
			} else {
			    if (!isIPv4) {
				int delim = sAddr.indexOf('%'); // drop ip6 port suffix
				return delim < 0 ? sAddr : sAddr.substring(0, delim);
			    }
			}
		    }
		}
	    }
	} catch (Exception ex) {
	} // for now eat exceptions
	return "";
    }

    public ArrayList<String> collectDeviceInfo() {
	ArrayList<String> deviceInfoList = new ArrayList<String>();
	PackageManager pm = null;

	if (mContext != null) {
	    pm = mContext.getPackageManager();
	}

	try {
	    if (pm != null) {
		PackageInfo pi;
		pi = pm.getPackageInfo(mContext.getPackageName(), 0);

		// Version
		versionName = "versionName" + " : " + pi.versionName;

		// Package name
		packageName = "packageName" + " : " + pi.packageName;
	    }

	    if (mContext != null) {
		// Files dir for storing the stack traces
		filePath = "filePath" + " : " + mContext.getFilesDir().getAbsolutePath();
	    }

	    // Device model
	    phoneModel = "phoneModel  : " + android.os.Build.MODEL;

	    // Android version
	    androidVersion = "androidVersion : " + android.os.Build.VERSION.RELEASE;

	    board = "board : " + android.os.Build.BOARD;
	    brand = "brand : " + android.os.Build.BRAND;

	    // CPU_ABI = android.os.Build.;
	    device = "device : " + android.os.Build.DEVICE;
	    display = "display : " + android.os.Build.DISPLAY;
	    fingerPrint = "fingerPrint : " + android.os.Build.FINGERPRINT;
	    host = "host : " + android.os.Build.HOST;
	    id = "id : " + android.os.Build.ID;

	    // Manufacturer = android.os.Build.;
	    model = "model : " + android.os.Build.MODEL;
	    product = "product : " + android.os.Build.PRODUCT;
	    tags = "tags : " + android.os.Build.TAGS;
	    time = "time : " + android.os.Build.TIME;
	    type = "type : " + android.os.Build.TYPE;
	    user = "user : " + android.os.Build.USER;

	    // Internal Memory
	    totalInternalMemory = "totalInternalMemory : " + getTotalInternalMemorySize() + "";
	    availableInternalMemory = "availableInternalMemory : " + getAvailableInternalMemorySize() + "";

	    if (mContext != null && mContext instanceof Activity) {
		// Screen Resolution
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		    screenResolution = "screenResolution : " + dm.heightPixels + " x " + dm.widthPixels;
		} else {
		    screenResolution = "screenResolution : " + dm.widthPixels + " x " + dm.heightPixels;
		}
	    }

	    deviceInfoList.add(versionName);
	    deviceInfoList.add(packageName);
	    deviceInfoList.add(filePath);
	    deviceInfoList.add(phoneModel);
	    deviceInfoList.add(androidVersion);
	    deviceInfoList.add(board);
	    deviceInfoList.add(brand);
	    deviceInfoList.add(device);
	    deviceInfoList.add(display);
	    deviceInfoList.add(fingerPrint);
	    deviceInfoList.add(host);
	    deviceInfoList.add(id);
	    deviceInfoList.add(model);
	    deviceInfoList.add(product);
	    deviceInfoList.add(tags);
	    deviceInfoList.add(time);
	    deviceInfoList.add(type);
	    deviceInfoList.add(user);
	    deviceInfoList.add(totalInternalMemory);
	    deviceInfoList.add(availableInternalMemory);
	    deviceInfoList.add(screenResolution);

	} catch (NameNotFoundException e) {
	    Log.e(TAG, e + "");
	} catch (Exception e) {
	    Log.e(TAG, e + "");
	}

	return deviceInfoList;
    }

    /**
     * Gets the total internal memory
     * 
     * @return
     */
    public long getTotalInternalMemorySize() {
	File path = Environment.getDataDirectory();
	StatFs stat = new StatFs(path.getPath());
	long blockSize = stat.getBlockSize();
	long totalBlocks = stat.getBlockCount();
	return totalBlocks * blockSize;
    }

    /**
     * Gets the available internal memory
     * 
     * @return
     */
    public long getAvailableInternalMemorySize() {
	File path = Environment.getDataDirectory();
	StatFs stat = new StatFs(path.getPath());
	long blockSize = stat.getBlockSize();
	long availableBlocks = stat.getAvailableBlocks();
	return availableBlocks * blockSize;
    }

    public static boolean externalMemoryAvailable() {
	return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long getAvailableExternalMemorySize() {
	if (externalMemoryAvailable()) {
	    File path = Environment.getExternalStorageDirectory();
	    StatFs stat = new StatFs(path.getPath());
	    long blockSize = stat.getBlockSize();
	    long availableBlocks = stat.getAvailableBlocks();
	    return (availableBlocks * blockSize);
	} else {
	    return 0L;
	}
    }

    public static long getTotalExternalMemorySize() {
	if (externalMemoryAvailable()) {
	    File path = Environment.getExternalStorageDirectory();
	    StatFs stat = new StatFs(path.getPath());
	    long blockSize = stat.getBlockSize();
	    long totalBlocks = stat.getBlockCount();
	    return (totalBlocks * blockSize);
	} else {
	    return 0L;
	}
    }

    /**
     * Check whether external media is writable or not.
     * 
     * @return <code>true</code> if external media is available and have some free memory to write, else
     *         <code>false</code>
     */
    public static boolean mediaWritable() {
	boolean writable = false;
	double megAvailable = 0;

	String state = Environment.getExternalStorageState();

	// If media is mounted and read-only then verify whether space is
	// available or not
	if (state.equals(Environment.MEDIA_MOUNTED) && !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {

	    StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	    long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getBlockCount();

	    megAvailable = bytesAvailable / 1048576;

	    if (megAvailable > 0) {
		writable = true;
	    }

	}

	return writable;
    }

    /**
     * Check if media is readable or not
     * 
     * @return <code>true</code> if external media is readable, otherwise <code>false</code>
     */
    public static boolean mediaReadable() {
	String state = Environment.getExternalStorageState();
	if (state.equals(Environment.MEDIA_MOUNTED)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Returns the screen dimension of the device
     * 
     * @param context
     * @return <code>Point</code> having width and height of the screen if the method runs successfully,
     *         <code>null</code> otherwise
     */
    public static Point getScreenSize(Context context) {
	int widthPixels = -1;
	int heightPixels = -1;
	if (context != null) {
	    WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    Display d = w.getDefaultDisplay();
	    DisplayMetrics metrics = new DisplayMetrics();
	    d.getMetrics(metrics);
	    // since SDK_INT = 1;
	    widthPixels = metrics.widthPixels;
	    heightPixels = metrics.heightPixels;
	    // includes window decorations (status-bar/menu-bar)
	    if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
		try {
		    widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
		    heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
		} catch (Exception e) {
		    Logger.warn(TAG, "Could not get the dimensions which includes window decorations");
		}
	    // includes window decorations (status-bar/menu-bar)
	    if (Build.VERSION.SDK_INT >= 17)
		try {
		    Point realSize = new Point();
		    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
		    widthPixels = realSize.x;
		    heightPixels = realSize.y;
		} catch (Exception e) {
		    Logger.warn(TAG, "Could not get the dimensions which includes windows decoration");
		}
	} else {
	    Logger.warn(TAG, "Null activity passed while trying to get the screen size");
	}

	if (widthPixels == -1 || heightPixels == -1) {
	    return null;
	} else {
	    return new Point(widthPixels, heightPixels);
	}
    }

    public static boolean memoryLessThanTwoMB() {

	return getAvailableExternalMemorySize() < (2 * 1048576);

    }

    /**
     * Get device screen size normalized to density
     * 
     * @param context
     * @param normalizeToDensity
     * @return
     */

    public static Point getScreenSize(Context context, boolean normalizedToDensity) {
	int widthPixels = -1;
	int heightPixels = -1;
	DisplayMetrics metrics = null;
	if (context != null) {
	    WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    Display d = w.getDefaultDisplay();
	    metrics = new DisplayMetrics();
	    d.getMetrics(metrics);
	    // since SDK_INT = 1;
	    widthPixels = metrics.widthPixels;
	    heightPixels = metrics.heightPixels;
	    // includes window decorations (status-bar/menu-bar)
	    if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17)
		try {
		    widthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(d);
		    heightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(d);
		} catch (Exception e) {
		    Logger.warn(TAG, "Could not get the dimensions which includes window decorations");
		}
	    // includes window decorations (status-bar/menu-bar)
	    if (Build.VERSION.SDK_INT >= 17)
		try {
		    Point realSize = new Point();
		    Display.class.getMethod("getRealSize", Point.class).invoke(d, realSize);
		    widthPixels = realSize.x;
		    heightPixels = realSize.y;
		} catch (Exception e) {
		    Logger.warn(TAG, "Could not get the dimensions which includes windows decoration");
		}
	} else {
	    Logger.warn(TAG, "Null activity passed while trying to get the screen size");
	}

	if (widthPixels == -1 || heightPixels == -1) {
	    return null;
	} else {
	    if (normalizedToDensity) {
		return new Point((int) (widthPixels * metrics.density), (int) (heightPixels * metrics.density));
	    } else {
		return new Point(widthPixels, heightPixels);
	    }
	}
    }

    public static int getDensity(Context context) {
	int densityDPI = DisplayMetrics.DENSITY_DEFAULT;
	DisplayMetrics metrics = null;
	if (context != null) {
	    WindowManager w = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	    Display d = w.getDefaultDisplay();
	    metrics = new DisplayMetrics();
	    d.getMetrics(metrics);
	    densityDPI = metrics.densityDpi;

	} else {
	    Logger.warn(TAG, "Null activity passed while trying to get the screen density");
	}
	return densityDPI;

    }

}