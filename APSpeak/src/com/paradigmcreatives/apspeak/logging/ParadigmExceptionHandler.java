package com.paradigmcreatives.apspeak.logging;

import java.io.File;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.StatFs;

/**
 * Handles all the un-handled exception occurring anywhere in the application. It also sends a report to the server.
 * After the report is sent, it restarts the application.<br>
 * <br>
 * 
 * <b>Usage</b><br>
 * In your main activity's <code>onCreate</code> method write<br>
 * <code>
 *         intent = PendingIntent.getActivity(getApplication().getBaseContext(), 0, new Intent(getIntent()), getIntent().getFlags());
 * </code> <br>
 * <br>
 * Pass the <code>intent</code> above to the constructor of <code>ParadigmExceptionHandler</code> along with the
 * <code>Context</code> of the application.<br>
 * <br>
 * Finally set the <code>Thread.setDefaultUncaughtExceptionHandler(ParadigmExceptionHandler instance)</code> with the
 * instance of <code>ParadigmExceptionHandler</code> created earlier.
 * 
 * @author
 * 
 */

public class ParadigmExceptionHandler implements Thread.UncaughtExceptionHandler {

    private Context mContext;
    private final String TAG = "ParadigmExceptionHandler";
    private String logDirectoryPath = null;

    private String versionName = "", packageName = "", filePath = "", phoneModel = "", androidVersion = "", board = "",
	    brand = "", device = "", display = "", fingerPrint = "", host = "";

    private String id = "", model = "", product = "", tags = "", time = "", type = "", user = "",
	    availableInternalMemory = "", totalInternalMemory = "";

    private String cause = "";

    public ParadigmExceptionHandler(Context context, String logDirectoryPath) {
	super();
	this.mContext = context;
	this.logDirectoryPath = logDirectoryPath;
    }

    public void uncaughtException(Thread thread, Throwable ex) {

	System.out.println("Crashed " + ex.getStackTrace());
	// Getting the cause of exception and its stack trace
	if (ex.getMessage() != null) {
	    cause = ex.getMessage() + "\n";
	}

	if (ex.getCause() != null) {
	    cause += ex.toString() + "\n";
	}

	StackTraceElement[] ele = ex.getStackTrace();
	if (ele != null) {
	    for (StackTraceElement el : ele) {
		cause += "   File: " + el.getFileName() + "   Class:" + el.getClassName() + "   Method:"
			+ el.getMethodName() + "   Line:" + el.getLineNumber() + "\n";
	    }
	}

	// Getting the data of phone
	ArrayList<NameValuePair> data = collectLocalInformation(mContext);

	// Adding the cause to data
	BasicNameValuePair bnvp = new BasicNameValuePair("exception", cause);
	data.add(bnvp);

	printLocalInformation();
	Logger.fatal("Crash Happened :: " + cause);

	// Put the logs on S3
	if (mContext != null) {
	    LogUploader logUploader = new LogUploader(mContext, logDirectoryPath);
	    logUploader.uploadLogsToS3();
	} else {
	    Logger.warn(TAG, "App Context is null, cannot upload logs");
	}

	// Give 2 seconds wait for crash log to upload and then kill the application
	try {
	    Thread.sleep(2000);
	} catch (InterruptedException e) {
	    //Do nothing
	} finally {
	    android.os.Process.killProcess(android.os.Process.myPid());
	}

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

    /*
     * private void restartApp() { if(mContext != null && intent != null) { AlarmManager mgr = (AlarmManager)
     * mContext.getSystemService(Context.ALARM_SERVICE); mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
     * intent); System.exit(2); } }
     */

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
     * Collects phone and application specific data and creates an <code>ArrayList</code> in the form required by the
     * <code>WebServiceWrapper</code>. This data is sent to the server.
     * 
     * @param context
     *            Application's context
     * @return
     */
    public ArrayList<NameValuePair> collectLocalInformation(Context context) {
	ArrayList<NameValuePair> data = new ArrayList<NameValuePair>();

	PackageManager pm = context.getPackageManager();
	try {
	    // Version
	    PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
	    versionName = pi.versionName;
	    // Package name
	    packageName = pi.packageName;
	    // Files dir for storing the stack traces
	    filePath = context.getFilesDir().getAbsolutePath();
	    // Device model
	    phoneModel = android.os.Build.MODEL;
	    // Android version
	    androidVersion = android.os.Build.VERSION.RELEASE;

	    board = android.os.Build.BOARD;
	    brand = android.os.Build.BRAND;
	    // CPU_ABI = android.os.Build.;
	    device = android.os.Build.DEVICE;
	    display = android.os.Build.DISPLAY;
	    fingerPrint = android.os.Build.FINGERPRINT;
	    host = android.os.Build.HOST;
	    id = android.os.Build.ID;
	    // Manufacturer = android.os.Build.;
	    model = android.os.Build.MODEL;
	    product = android.os.Build.PRODUCT;
	    tags = android.os.Build.TAGS;
	    time = android.os.Build.TIME + "";
	    type = android.os.Build.TYPE;
	    user = android.os.Build.USER;

	    // Internal Memory
	    totalInternalMemory = getTotalInternalMemorySize() + "";
	    availableInternalMemory = getAvailableInternalMemorySize() + "";

	} catch (NameNotFoundException e) {
	    e.printStackTrace();
	    Logger.logStackTrace(e);
	    System.out.println("ParadigmExceptionHandler.RecoltInformations()" + e);
	} catch (Exception e) {
	    e.printStackTrace();
	    Logger.logStackTrace(e);
	}

	// Adding the values
	BasicNameValuePair bnvp = new BasicNameValuePair("versionName", versionName);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("versionName", versionName);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("packageName", packageName);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("phoneModel", phoneModel);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("brand", brand);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("board", board);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("device", device);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("display", display);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("fingerPrint", fingerPrint);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("host", host);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("product", product);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("model", model);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("id", id);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("user", user);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("type", type);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("tags", tags);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("totalInternalMemory", totalInternalMemory);
	data.add(bnvp);

	bnvp = new BasicNameValuePair("availableInternalMemory", availableInternalMemory);
	data.add(bnvp);

	return data;
    }

    public void printLocalInformation() {
	String localDeviceInformation = "";

	// System.out.println("Version: " + versionName);
	localDeviceInformation = localDeviceInformation + "Version: " + versionName + "\n";
	// System.out.println("PKG: " + packageName);
	localDeviceInformation = localDeviceInformation + "PKG: " + packageName + "\n";
	// System.out.println("File: " + filePath);
	localDeviceInformation = localDeviceInformation + "File: " + filePath + "\n";
	// System.out.println("Model: " + phoneModel);
	localDeviceInformation = localDeviceInformation + "Model: " + phoneModel + "\n";

	// System.out.println("Android Version: " + androidVersion);
	localDeviceInformation = localDeviceInformation + "Android Version: " + androidVersion + "\n";
	// System.out.println("Board: " + board);
	localDeviceInformation = localDeviceInformation + "Board: " + board + "\n";
	// System.out.println("Brand: " + brand);
	localDeviceInformation = localDeviceInformation + "Brand: " + brand + "\n";
	// System.out.println("Device: " + device);
	localDeviceInformation = localDeviceInformation + "Device: " + device + "\n";

	// System.out.println("Display: " + display);
	localDeviceInformation = localDeviceInformation + "Display: " + display + "\n";
	// System.out.println("Fngr: " + fingerPrint);
	localDeviceInformation = localDeviceInformation + "Fngr: " + fingerPrint + "\n";
	// System.out.println("Host: " + host);
	localDeviceInformation = localDeviceInformation + "Host: " + host + "\n";
	// System.out.println("ID: " + id);
	localDeviceInformation = localDeviceInformation + "ID: " + id + "\n";

	// System.out.println("Model: " + model);
	localDeviceInformation = localDeviceInformation + "Model: " + model + "\n";
	// System.out.println("Product: " + product);
	localDeviceInformation = localDeviceInformation + "Product: " + product + "\n";
	// System.out.println("Tags: " + tags);
	localDeviceInformation = localDeviceInformation + "Tags: " + tags + "\n";
	// System.out.println("Time: " + time);
	localDeviceInformation = localDeviceInformation + "Time: " + time + "\n";

	// System.out.println("User: " + user);
	localDeviceInformation = localDeviceInformation + "User: " + user + "\n";
	// System.out.println("Avail Mem: " + availableInternalMemory);
	localDeviceInformation = localDeviceInformation + "Avail Mem: " + availableInternalMemory + "\n";
	// System.out.println("Total Mem: " + totalInternalMemory);
	localDeviceInformation = localDeviceInformation + "Total Mem: " + totalInternalMemory + "\n";

	Logger.info("Device Information::\n" + localDeviceInformation);
    }
}