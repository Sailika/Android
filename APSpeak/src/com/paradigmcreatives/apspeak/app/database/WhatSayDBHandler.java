package com.paradigmcreatives.apspeak.app.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paradigmcreatives.apspeak.app.database.expressionsdb.ExpressionsSubmitQueueTable;
import com.paradigmcreatives.apspeak.app.database.settingsdb.SettingsTable;
import com.paradigmcreatives.apspeak.logging.Logger;

public class WhatSayDBHandler extends SQLiteOpenHelper {

    private static final String TAG = "WhatSayDBHandler";

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "WhatsayDB";

    private Context context;

    public WhatSayDBHandler(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	try {
	    db.execSQL(SettingsTable.getCreateQuery());
	    db.execSQL(ExpressionsSubmitQueueTable.getCreateQuery());
	} catch (SQLException e) {
	    Logger.fatal(TAG, "SQL Exception has occurred while creating the tables : " + e.getLocalizedMessage());
	} catch (Exception e) {
	    Logger.fatal(TAG, "Unknown Exception occured while creating tables : " + e.getLocalizedMessage());
	}
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);

	// upgrade the existing tables if any
	try {
		if(newVersion >= 2){
			if(oldVersion == 1){
				db.execSQL(ExpressionsSubmitQueueTable.getCreateQuery());
			}
		}
	} catch (SQLException sqle) {
	    Logger.warn(TAG, "Error upgrading database. Database not upgraded." + sqle.getLocalizedMessage());
	} catch (Exception e) {
	    Logger.warn(TAG, "Unknown error in upgrading database. Database not upgraded." + e.getLocalizedMessage());
	}
    }

    /**
     * Deletes entire application database.
     * 
     * @param context
     *            calling context.
     * 
     * @return true id database is deleted, else false.
     */
    public boolean deleteDB(Context context) {
	return (context == null) ? false : context.deleteDatabase(DATABASE_NAME);
    }// end of deleteDB()

}
