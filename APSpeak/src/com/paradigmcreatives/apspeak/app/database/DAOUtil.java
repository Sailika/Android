package com.paradigmcreatives.apspeak.app.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Util class to help database interactions
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class DAOUtil {

    private static final String TAG = "DAOUtil";

    /**
     * Closes the the supplied cursor and the SQLite DB object
     * 
     * @param cursor
     * @param sqlDB
     */
    public void closeDBObjects(Cursor cursor, SQLiteDatabase sqlDB) {
	try {
	    if (cursor != null) {
		cursor.close();
	    }
	    if (sqlDB != null) {
		sqlDB.close();
	    }
	} catch (SQLException sqle) {
	    Logger.warn(TAG, "SQL error while closing DB connection. : " + sqle.getLocalizedMessage());
	} catch (Exception e) {
	    Logger.warn(TAG, "Unknown error while closing DB connection.");
	    Logger.logStackTrace(e);
	}

    }

    /**
     * Gets a readable db from the db table handler
     * 
     * @param dbHandler
     *            : The db table handler
     * @return : A readable db instance
     */
    public SQLiteDatabase getReadableDatabase(SQLiteOpenHelper dbHandler) {
	SQLiteDatabase sqlDB = null;
	try {
	    sqlDB = dbHandler.getReadableDatabase();
	} catch (SQLException sqle) {
	    Logger.warn(TAG, "Error connecting to database. : " + sqle.getLocalizedMessage());
	} catch (Exception e) {
	    Logger.warn(TAG, "Error connecting to database.");
	    Logger.logStackTrace(e);
	}

	return sqlDB;
    }

    /**
     * Gets a writable db from the db table handler
     * 
     * @param dbHandler
     *            : The db table handler
     * @return : A writable db instance
     */
    public SQLiteDatabase getWritableDatabase(SQLiteOpenHelper dbHandler) {
	SQLiteDatabase sqlDB = null;
	try {
	    sqlDB = dbHandler.getWritableDatabase();
	} catch (SQLException sqle) {
	    Logger.warn(TAG, "Error connecting to database. : " + sqle.getLocalizedMessage());
	} catch (Exception e) {
	    Logger.warn(TAG, "Error connecting to database.");
	    Logger.logStackTrace(e);
	}

	return sqlDB;
    }

    /**
     * Retrieves doodle records from the given cursor object.
     * 
     * @param cursor
     *            cursor with appropriate condition.
     * 
     * @return The list of DoodleInfo objects
     */
    /*
    public List<DoodleInfo> processDoodlesInfo(Cursor cursor) {
	// Initialize empty list
	ArrayList<DoodleInfo> doodleList = new ArrayList<DoodleInfo>();
	DoodleInfo doodleInfo = null;

	if (!cursor.moveToFirst()) {
	    return doodleList;
	}

	try {
	    do {
		doodleInfo = extractDoodleInfoFromCursor(cursor);
		if (doodleInfo != null) {
		    doodleList.add(doodleInfo);
		}
	    } while (cursor.moveToNext());
	} catch (Exception e) {
	    Logger.warn(TAG, "Unknown error while retreiving records.");
	    Logger.logStackTrace(e);
	}
	return doodleList;
    }
    */

    /**
     * Processes the cursor to builds a DoodleInfo object out of it. If the cursor is invalid then it returns
     * <code>null</code>
     * 
     * @param cursor
     * @return
     */
    /*
    public DoodleInfo extractDoodleInfoFromCursor(Cursor cursor) throws Exception {
	if (cursor != null && !cursor.isClosed()) {
	    DoodleInfo doodleInfo = null;

	    // Getting the path of the saved doodle file
	    String doodlePath = cursor.getString(cursor.getColumnIndex(DoodleTable.PATH));
	    if (new File(doodlePath).exists()) {
		doodleInfo = new DoodleInfo();

		doodleInfo.setPath(doodlePath);

		// Get the doodle uuid
		doodleInfo.setTransactionId(cursor.getString(cursor.getColumnIndex(DoodleTable.TRANSACTION_ID)));

		// Getting the state of doodle download
		String doodleStatus = cursor.getString(cursor.getColumnIndex(DoodleTable.STATUS));
		if (doodleStatus.equalsIgnoreCase(DoodleStatus.SENDING.name())) {
		    doodleInfo.setStatus(DoodleStatus.SENDING);
		} else if (doodleStatus.equalsIgnoreCase(DoodleStatus.RECEIVED.name())) {
		    doodleInfo.setStatus(DoodleStatus.RECEIVED);
		} else if (doodleStatus.equalsIgnoreCase(DoodleStatus.SENT.name())) {
		    doodleInfo.setStatus(DoodleStatus.SENT);
		} else if (doodleStatus.equalsIgnoreCase(DoodleStatus.FAILED.name())) {
		    doodleInfo.setStatus(DoodleStatus.FAILED);
		} else if (doodleStatus.equalsIgnoreCase(DoodleStatus.DELIVERED.name())) {
		    doodleInfo.setStatus(DoodleStatus.DELIVERED);
		} else if (doodleStatus.equalsIgnoreCase(DoodleStatus.VIEWED.name())) {
		    doodleInfo.setStatus(DoodleStatus.VIEWED);
		}

		// Get its timestamp
		doodleInfo.setTimeStamp(cursor.getLong(cursor.getColumnIndex(DoodleTable.TIMESTAMP)));

		// Get its read flag
		if (cursor.getString(cursor.getColumnIndex(DoodleTable.READ_FLAG)).equals("1")) {
		    doodleInfo.setReadFlag(true);
		} else {
		    doodleInfo.setReadFlag(false);
		}

		// Get the doodle path
		doodleInfo.setPath(cursor.getString(cursor.getColumnIndex(DoodleTable.PATH)));

		// Get the thumbnail
		byte[] thumbnailBlob = cursor.getBlob(cursor.getColumnIndex(DoodleTable.THUMBNAIL));
		doodleInfo.setThumbnail(getThumbnailBitmap(thumbnailBlob));

		// Get doodle id
		doodleInfo.setDoodleId(cursor.getString(cursor.getColumnIndex(DoodleTable.DOODLE_ID)));

		// Get its love flag
		if (cursor.getString(cursor.getColumnIndex(DoodleTable.LOVE_FLAG)).equals("1")) {
		    doodleInfo.setLoveFlag(true);
		} else {
		    doodleInfo.setLoveFlag(false);
		}
		// Get its love count
		doodleInfo.setLoveCount(cursor.getLong(cursor.getColumnIndex(DoodleTable.LOVE_COUNT)));

		// Get its share count
		doodleInfo.setShareCount(cursor.getLong(cursor.getColumnIndex(DoodleTable.SHARE_COUNT)));

		// Get its view count
		doodleInfo.setViewCount(cursor.getLong(cursor.getColumnIndex(DoodleTable.VIEW_COUNT)));
	    } else {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
		    Logger.info(TAG, "The doodle doesn't exist sd card");
		} else {
		    Logger.info(TAG, "The sd card has been removed from device");
		}
	    }
	    return doodleInfo;
	} else {
	    Logger.warn(TAG, "Cursor invalid or closed");
	    return null;
	}

    }
    */

    /**
     * Takes the <code>byte</code> array of an image and converts it into a <code>Bitmap</code>
     * 
     * @param thumbnailBlob
     * @return
     */
    public Bitmap getThumbnailBitmap(byte[] thumbnailBlob) {
	Bitmap thumbnail = null;
	if (thumbnailBlob != null) {
	    thumbnail = BitmapFactory.decodeByteArray(thumbnailBlob, 0, thumbnailBlob.length);
	    if (thumbnail == null) {
		Logger.warn(TAG, "thumbnail is null");
	    }
	} else {
	    Logger.warn(TAG, "thumbnail blob is null in db");
	}

	return thumbnail;
    }

    /**
     * Checks if the database has the entry for the uuid
     * 
     * @param uuid
     * @param db
     * @param table
     * @param columnName
     * @return
     */
    public boolean rowWithIdExists(String uuid, SQLiteDatabase db, String table, String columnName) {
	boolean result = false;
	Cursor cursor = null;
	try {
	    String checkQuery = "SELECT COUNT(*) FROM " + table + " WHERE " + columnName + "=?";
	    cursor = db.rawQuery(checkQuery, new String[] { uuid });
	    if (cursor != null && cursor.moveToFirst()) {
		if (cursor.getInt(0) > 0) {
		    result = true;
		}
	    }
	} catch (SQLException sqle) {
	    Logger.warn(TAG, "SQL error while check query : " + sqle.getLocalizedMessage());
	} catch (Exception e) {
	    Logger.warn(TAG, "Unknown error while check query : " + e.getLocalizedMessage());
	    Logger.logStackTrace(e);
	} finally {
	    closeDBObjects(cursor, null);
	}
	return result;
    }
}
