package com.paradigmcreatives.apspeak.app.database.settingsdb;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.paradigmcreatives.apspeak.app.database.DAOUtil;
import com.paradigmcreatives.apspeak.app.database.WhatSayDBHandler;
import com.paradigmcreatives.apspeak.logging.Logger;

public class SettingsDAO {
    /**
     * Settings db access object
     * 
     * @author Vineela Gadhiraju | Paradigm Creatives
     * 
     */
    private static final String TAG = "SettingsDAO";

    private SQLiteDatabase sqlDB;
    private WhatSayDBHandler dbHandler;
    private DAOUtil daoUtil;

    public SettingsDAO(Context context) {
	dbHandler = new WhatSayDBHandler(context);
	daoUtil = new DAOUtil();
    }

    /**
     * Updates a settings record.
     * 
     * @param key
     *            value of key.
     * @param value
     *            value.
     * @return no of rows affected in case update is successful, else 0.
     */
    public synchronized int updateSettingsInfo(String key, String value) {
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
    sqlDB = daoUtil.getWritableDatabase(dbHandler);

	int rowsUpdated = -1;
	if (sqlDB != null) {
	    try {
		ContentValues cv = new ContentValues();
		cv.put(SettingsTable.KEY, key);
		cv.put(SettingsTable.VALUE, value);
		rowsUpdated = sqlDB.update(SettingsTable.SETTINGS_INFO_TABLE, cv, SettingsTable.KEY + " = ?",
			new String[] { key });
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while updating records. : " + sqle.getLocalizedMessage());
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while updating records. : " + e.getLocalizedMessage());
		Logger.logStackTrace(e);
	    } finally {
		daoUtil.closeDBObjects(null, sqlDB);
	    }
	}

	return rowsUpdated;
    }// end of updateDoodle()

    /**
     * Inserts a settings record.
     * 
     * @param key
     *            value of key.
     * @param value
     *            value.
     * @return row id inserted if successful, else -1
     */
    public synchronized long insertSettingsInfo(String key, String value) {
	long rowID = -1;

	ContentValues cv = new ContentValues();
	cv.put(SettingsTable.KEY, key);
	cv.put(SettingsTable.VALUE, value);

	rowID = insertSettingsInfo(cv);

	Logger.info(TAG, "Row ID : " + rowID);
	return rowID;
    }

    /**
     * Inserts a settings record.
     * 
     * @param contentValues
     *            : values of the records.
     */
    private synchronized long insertSettingsInfo(ContentValues contentValues) {
	long rowID = -1;
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
	sqlDB = daoUtil.getWritableDatabase(dbHandler);

	if (sqlDB != null) {
	    try {
		rowID = sqlDB.insert(SettingsTable.SETTINGS_INFO_TABLE, null, contentValues);
	    } catch (SQLiteConstraintException sce) {
		Logger.warn(TAG, "Constraint exception : " + sce.getLocalizedMessage());
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while inserting records. : " + sqle.getLocalizedMessage());
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while inserting records. : " + e.getLocalizedMessage());
		Logger.logStackTrace(e);
	    } finally {
		daoUtil.closeDBObjects(null, sqlDB);
	    }
	}

	return rowID;
    }// end of insertNotificationInfo()

    /**
     * Inserts the data if not present or else replaces the value
     * 
     * @param key
     * @param value
     * @return
     */
    public synchronized boolean insertOrReplace(String key, String value) {
	boolean success = false;
	long insertID = -1;
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
	sqlDB = daoUtil.getWritableDatabase(dbHandler);

	if (sqlDB != null) {
	    try {
		ContentValues cv = new ContentValues();
		cv.put(SettingsTable.KEY, key);
		cv.put(SettingsTable.VALUE, value);
		insertID = sqlDB.insertWithOnConflict(SettingsTable.SETTINGS_INFO_TABLE, null, cv,
			SQLiteDatabase.CONFLICT_REPLACE);
	    } catch (SQLiteConstraintException sce) {
		Logger.warn(TAG, "Constraint exception : " + sce.getLocalizedMessage());
		sce.printStackTrace();
		insertID = -1;
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while upserting records. : " + sqle.getLocalizedMessage());
		sqle.printStackTrace();
		insertID = -1;
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while upserting records. : " + e.getLocalizedMessage());
		Logger.logStackTrace(e);
		insertID = -1;
	    } finally {
		daoUtil.closeDBObjects(null, sqlDB);
	    }
	}

	if (insertID > 0) {
	    success = true;
	}
	return success;
    }

    /**
     * Inserts the data if not present or else replaces the value
     * 
     * @param database
     * @param key
     * @param value
     * @return
     */
    public synchronized boolean insertOrReplace(SQLiteDatabase database, String key, String value) {
	boolean success = false;
	long insertID = -1;

	if (database != null) {
	    try {
		ContentValues cv = new ContentValues();
		cv.put(SettingsTable.KEY, key);
		cv.put(SettingsTable.VALUE, value);
		insertID = database.insertWithOnConflict(SettingsTable.SETTINGS_INFO_TABLE, null, cv,
			SQLiteDatabase.CONFLICT_REPLACE);
	    } catch (SQLiteConstraintException sce) {
		Logger.warn(TAG, "Constraint exception : " + sce.getLocalizedMessage());
		insertID = -1;
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while upserting records. : " + sqle.getLocalizedMessage());
		insertID = -1;
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while upserting records. : " + e.getLocalizedMessage());
		Logger.logStackTrace(e);
		insertID = -1;
	    } finally {
		daoUtil.closeDBObjects(null, null);
	    }
	}

	if (insertID > 0) {
	    success = true;
	}
	return success;
    }

    /**
     * Updates any counter present atomically
     * 
     * @param key
     * @param increment
     * @return
     */
    public synchronized boolean updateCounter(String key, boolean increment) {
	boolean success = false;
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
	sqlDB = daoUtil.getWritableDatabase(dbHandler);

	if (sqlDB != null) {
	    try {
		String sql = null;
		if (increment) {
		    sql = "UPDATE " + SettingsTable.SETTINGS_INFO_TABLE + " SET " + SettingsTable.VALUE + " = "
			    + SettingsTable.VALUE + " + ? WHERE " + SettingsTable.KEY + " = ?";
		} else {
		    sql = "UPDATE " + SettingsTable.SETTINGS_INFO_TABLE + " SET " + SettingsTable.VALUE + " = "
			    + SettingsTable.VALUE + " - ? WHERE " + SettingsTable.KEY + " = ?";
		}
		sqlDB.execSQL(sql, new Object[] { "1", key });
		success = true;
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while incrementing count : " + sqle.getLocalizedMessage());
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while incrementing count : " + e.getLocalizedMessage());
		Logger.logStackTrace(e);
	    } finally {
		daoUtil.closeDBObjects(null, sqlDB);
	    }
	}
	return success;
    }

    /**
     * Deletes a settings from records.
     * 
     * @param doodleUUID
     *            : The unique key of the doodle
     * 
     * @return row id inserted if successful, else -1
     */
    public synchronized int deleteSettingsInfo(String key) {
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
    sqlDB = daoUtil.getWritableDatabase(dbHandler);

	int rowsDeleted = -1;
	if (sqlDB != null) {
	    try {
		String whereClause = SettingsTable.KEY + " = ?";
		String[] whereArguments = new String[] { key };

		rowsDeleted = sqlDB.delete(SettingsTable.SETTINGS_INFO_TABLE, whereClause, whereArguments);
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while deleting records. : " + sqle.getLocalizedMessage());
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while deleting records. : " + e.getLocalizedMessage());
		Logger.logStackTrace(e);
	    } finally {
		daoUtil.closeDBObjects(null, sqlDB);
	    }
	}

	return rowsDeleted;
    }// end of deleteDoodleNotificationInfo()

    /**
     * This method deletes all the settings from the database.
     * 
     * @return : Upon success deleted settings count will be returned and on failure -1 will be returned.
     */
    public synchronized int deleteAllSettingsInfos() {
	//sqlDB = daoUtil.getReadableDatabase(dbHandler);
    sqlDB = daoUtil.getWritableDatabase(dbHandler);

	int deleteStatus = -1;
	if (sqlDB != null) {
	    try {
		deleteStatus = sqlDB.delete(SettingsTable.SETTINGS_INFO_TABLE, "1", null);
	    } catch (SQLException sqle) {
		Logger.warn(TAG, "SQL error while deleting records. : " + sqle.getLocalizedMessage());
	    } catch (Exception e) {
		Logger.warn(TAG, "Unknown error while deleting records. : " + e.getLocalizedMessage());
		Logger.logStackTrace(e);
	    } finally {
		daoUtil.closeDBObjects(null, sqlDB);
	    }
	}
	return deleteStatus;
    }// end of deleteAllNotificationInfos()
}
