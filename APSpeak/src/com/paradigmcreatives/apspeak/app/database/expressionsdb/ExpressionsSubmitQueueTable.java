package com.paradigmcreatives.apspeak.app.database.expressionsdb;

/**
 * Database Table that represents Queued Expressions, which are to be submitted
 * to Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class ExpressionsSubmitQueueTable {

	public static final String EXPRESSIONS_SUBMITQUEUE_TABLE = "expressionsSubmitQueueTable";
	public static final String ID = "id";
	public static final String GROUP_ID = "groupId";
	public static final String USER_ID = "userId";
	public static final String CUE_ID = "cueId";
	public static final String ROOT_ASSET_ID = "rootAssetId";
	public static final String DESCRIPTION = "description";
	public static final String TYPE = "type";
	public static final String FILE_PATH = "filePath";
	public static final String SUBMISSION_STATUS = "status";

	public static String getCreateQuery() {
		return "CREATE TABLE IF NOT EXISTS " + EXPRESSIONS_SUBMITQUEUE_TABLE
				+ "(" + ID + " INTEGER PRIMARY KEY, " + GROUP_ID + " TEXT, "
				+ USER_ID + " TEXT, " + CUE_ID + " TEXT, " + ROOT_ASSET_ID
				+ " TEXT, " + DESCRIPTION + " TEXT, " + TYPE + " TEXT, "
				+ FILE_PATH + " TEXT, " + SUBMISSION_STATUS + " TEXT);";
	}
}
