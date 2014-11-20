package com.paradigmcreatives.apspeak.app.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.paradigmcreatives.apspeak.app.database.settingsdb.SettingsTable;
import com.paradigmcreatives.apspeak.logging.Logger;

public class SettingsDBUtil {
    /**
     * Database access object for querying the settings values
     * 
     * @author Vineela Gadhiraju| Paradigm Creatives
     * 
     */
    private static final String TAG = "SettingsDBUtil";

    private SQLiteDatabase sqlDB;
    private WhatSayDBHandler dbHandler;
    private DAOUtil daoUtil;

    public SettingsDBUtil(Context context) {
	dbHandler = new WhatSayDBHandler(context);
	daoUtil = new DAOUtil();
    }

    /**
     * Gets the settings value for a given key
     * 
     * @param key
     *            value of key
     * @return value
     */
    public String getValue(String key) {
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
    sqlDB = daoUtil.getWritableDatabase(dbHandler);
	String value = null;
	if (sqlDB != null) {
	    Cursor cursor = null;
	    try {
		String query = "SELECT " + SettingsTable.VALUE + " FROM " + SettingsTable.SETTINGS_INFO_TABLE
			+ " WHERE " + SettingsTable.KEY + " = ?";
		cursor = sqlDB.rawQuery(query, new String[] { key });
		if ((cursor == null) || (cursor.getCount() <= 0)) {
		    value = null;
		} else {
		    if (cursor.moveToFirst()) {
			value = cursor.getString(0);
		    } else {
			Logger.warn(TAG, "settings querry has empty result");
		    }
		}
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while retreiving records.");
		Logger.logStackTrace(sqle);
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while retreving records.");
		Logger.logStackTrace(e);
	    } finally {
		daoUtil.closeDBObjects(cursor, sqlDB);
	    }
	}

	return value;
    }

    /**
     * Used to find if a given key is present or not
     * 
     * @param key
     *            value of key
     * @return boolean value
     */
    public boolean isKeyPresent(String key) {
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
    sqlDB = daoUtil.getWritableDatabase(dbHandler);
	boolean result = false;
	if (sqlDB != null) {
	    Cursor cursor = null;
	    try {
		String query = "SELECT " + SettingsTable.KEY + " FROM " + SettingsTable.SETTINGS_INFO_TABLE + " WHERE "
			+ SettingsTable.KEY + " = ? ";
		cursor = sqlDB.rawQuery(query, new String[] { key });
		if ((cursor == null) || (cursor.getCount() <= 0)) {
		    result = false;
		} else {
		    if (cursor.moveToFirst()) {
			result = true;
		    } else {
			result = false;
			Logger.warn(TAG, "settings querry has empty result");
		    }
		}
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while retreiving records.");
		Logger.logStackTrace(sqle);
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while retreving records.");
		Logger.logStackTrace(e);
	    } finally {
		daoUtil.closeDBObjects(cursor, sqlDB);
	    }
	}

	return result;
    }
}
