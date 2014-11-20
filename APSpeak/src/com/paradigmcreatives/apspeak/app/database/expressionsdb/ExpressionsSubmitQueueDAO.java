package com.paradigmcreatives.apspeak.app.database.expressionsdb;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.database.DAOUtil;
import com.paradigmcreatives.apspeak.app.database.WhatSayDBHandler;
import com.paradigmcreatives.apspeak.app.model.ExpressionSubmitQueueBean;
import com.paradigmcreatives.apspeak.app.model.SUBMISSION_STATUS;

/**
 * Database Access Object to access ExpressionsSubmitQueueTable
 * 
 * @author Dileep | neuv
 * 
 */
public class ExpressionsSubmitQueueDAO {

	private SQLiteDatabase mSQLDb;
	private WhatSayDBHandler mDBHandler;
	private DAOUtil mDAOUtil;

	public ExpressionsSubmitQueueDAO(Context context) {
		mDBHandler = new WhatSayDBHandler(context);
		mDAOUtil = new DAOUtil();
	}

	/**
	 * Inserts passed ExpressionsSubmitQueueBean to ExpressionsSubmitQueueTable
	 * 
	 * @param expression
	 * @return
	 */
	public synchronized long insertExpressionToDB(
			ExpressionSubmitQueueBean expression) {
		long rowId = -1;
		if (expression != null && mDBHandler != null && mDAOUtil != null) {
			try {
				mSQLDb = mDAOUtil.getWritableDatabase(mDBHandler);
				if (mSQLDb != null) {
					ContentValues cv = new ContentValues();
					cv.put(ExpressionsSubmitQueueTable.GROUP_ID,
							expression.getGroupId());
					cv.put(ExpressionsSubmitQueueTable.USER_ID,
							expression.getUserId());
					cv.put(ExpressionsSubmitQueueTable.CUE_ID,
							expression.getCueId());
					cv.put(ExpressionsSubmitQueueTable.ROOT_ASSET_ID,
							expression.getRootAssetId());
					cv.put(ExpressionsSubmitQueueTable.DESCRIPTION,
							expression.getDescription());
					cv.put(ExpressionsSubmitQueueTable.TYPE,
							expression.getType());
					cv.put(ExpressionsSubmitQueueTable.FILE_PATH,
							expression.getFilePath());
					String submissionStatus = (expression.getSubmissionStatus() != null) ? expression
							.getSubmissionStatus().name()
							: SUBMISSION_STATUS.PENDING.name();
					cv.put(ExpressionsSubmitQueueTable.SUBMISSION_STATUS,
							submissionStatus);

					rowId = mSQLDb
							.insertWithOnConflict(
									ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE,
									null, cv, SQLiteDatabase.CONFLICT_IGNORE);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return rowId;
	}

	/**
	 * Checks whether an expression is available in the table or not based on
	 * passed id
	 * 
	 * @param id
	 * @return
	 */
	public synchronized boolean isExpressionAvailable(int id) {
		boolean isAvailable = false;
		if (id != -1 && mDBHandler != null && mDAOUtil != null) {
			try {
				mSQLDb = mDAOUtil.getWritableDatabase(mDBHandler);
				if (mSQLDb != null) {
					Cursor cursor = mSQLDb
							.query(ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE,
									null, ExpressionsSubmitQueueTable.ID
											+ " =?",
									new String[] { String.valueOf(id) }, null,
									null, null);
					if (cursor != null && cursor.moveToFirst()) {
						isAvailable = true;
					}
				}
			} catch (Exception e) {

			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return isAvailable;
	}

	/**
	 * Gets expression from DB for the given id
	 * 
	 * @param id
	 * @return
	 */
	public synchronized ExpressionSubmitQueueBean getExpressionFromDB(int id) {
		ExpressionSubmitQueueBean expression = null;
		if (id != -1 && mDBHandler != null && mDAOUtil != null) {
			try {
				mSQLDb = mDAOUtil.getWritableDatabase(mDBHandler);
				if (mSQLDb != null) {
					Cursor cursor = mSQLDb
							.query(ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE,
									null, ExpressionsSubmitQueueTable.ID
											+ " =?",
									new String[] { String.valueOf(id) }, null,
									null, null);
					ArrayList<ExpressionSubmitQueueBean> expressions = getExpressionsFromCursor(cursor);
					if (expressions != null && expressions.size() > 0) {
						expression = expressions.get(0);
					}
				}
			} catch (Exception e) {

			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return expression;
	}

	/**
	 * Gets expressions from DB for the given groupId, userId, cueId and
	 * status(optional)
	 * 
	 * @param groupId
	 * @param userId
	 * @param cueId
	 * @return
	 */
	public synchronized ArrayList<ExpressionSubmitQueueBean> getExpressionsFromDB(
			String groupId, String userId, String cueId,
			SUBMISSION_STATUS status) {
		ArrayList<ExpressionSubmitQueueBean> expressions = null;
		if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(userId)
				&& !TextUtils.isEmpty(cueId) && mDBHandler != null
				&& mDAOUtil != null) {
			try {
				mSQLDb = mDAOUtil.getWritableDatabase(mDBHandler);
				if (mSQLDb != null) {
					String selection = ExpressionsSubmitQueueTable.GROUP_ID
							+ " =? AND " + ExpressionsSubmitQueueTable.USER_ID
							+ " =? AND " + ExpressionsSubmitQueueTable.CUE_ID
							+ " =?";
					String[] selectionArgs = new String[] { groupId, userId,
							cueId };
					if (status != null) {
						selection += " AND "
								+ ExpressionsSubmitQueueTable.SUBMISSION_STATUS
								+ " =?";
						selectionArgs = new String[] { groupId, userId, cueId,
								status.name() };
					}
					Cursor cursor = mSQLDb
							.query(ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE,
									null, selection, selectionArgs, null, null,
									null);
					expressions = getExpressionsFromCursor(cursor);
				}
			} catch (Exception e) {

			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return expressions;
	}

	/**
	 * Parses given Cursor and returns an array list of
	 * ExpressionSubmitQueueBean
	 * 
	 * @param cursor
	 * @return
	 * @throws SQLException
	 */
	private ArrayList<ExpressionSubmitQueueBean> getExpressionsFromCursor(
			Cursor cursor) throws SQLException {
		ArrayList<ExpressionSubmitQueueBean> expressions = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				expressions = new ArrayList<ExpressionSubmitQueueBean>();
				do {
					ExpressionSubmitQueueBean expression = new ExpressionSubmitQueueBean();
					expression.setID(cursor.getInt(cursor
							.getColumnIndex(ExpressionsSubmitQueueTable.ID)));
					expression
							.setGroupId(cursor.getString(cursor
									.getColumnIndex(ExpressionsSubmitQueueTable.GROUP_ID)));
					expression
							.setUserId(cursor.getString(cursor
									.getColumnIndex(ExpressionsSubmitQueueTable.USER_ID)));
					expression
							.setCueId(cursor.getString(cursor
									.getColumnIndex(ExpressionsSubmitQueueTable.CUE_ID)));
					expression
							.setRootAssetId(cursor.getString(cursor
									.getColumnIndex(ExpressionsSubmitQueueTable.ROOT_ASSET_ID)));
					expression
							.setDescription(cursor.getString(cursor
									.getColumnIndex(ExpressionsSubmitQueueTable.DESCRIPTION)));
					expression.setType(cursor.getString(cursor
							.getColumnIndex(ExpressionsSubmitQueueTable.TYPE)));
					expression
							.setFilePath(cursor.getString(cursor
									.getColumnIndex(ExpressionsSubmitQueueTable.FILE_PATH)));
					String status = cursor.getString(cursor.getColumnIndex(ExpressionsSubmitQueueTable.SUBMISSION_STATUS));
					if(SUBMISSION_STATUS.FAILED.name().equals(status)){
						expression.setSubmissionStatus(SUBMISSION_STATUS.FAILED);
					}else if(SUBMISSION_STATUS.PENDING.name().equals(status)){
						expression.setSubmissionStatus(SUBMISSION_STATUS.PENDING);
					}

					expressions.add(expression);
				} while (cursor.moveToNext());
			}
		}
		return expressions;
	}

	/**
	 * Gets expressions count from DB for the given groupId, userId, cueId and
	 * status(optional)
	 * 
	 * @param id
	 * @return
	 */
	public synchronized int getExpressionsCountFromDB(String groupId,
			String userId, String cueId, SUBMISSION_STATUS status) {
		int expressionsCount = 0;
		if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(userId)
				&& !TextUtils.isEmpty(cueId) && mDBHandler != null
				&& mDAOUtil != null) {
			try {
				mSQLDb = mDAOUtil.getWritableDatabase(mDBHandler);
				if (mSQLDb != null) {
					String query = "select count("
							+ ExpressionsSubmitQueueTable.ID
							+ ") from "
							+ ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE
							+ " where " + ExpressionsSubmitQueueTable.GROUP_ID
							+ "='" + groupId + "' and "
							+ ExpressionsSubmitQueueTable.USER_ID + "='"
							+ userId + "' and "
							+ ExpressionsSubmitQueueTable.CUE_ID + "='" + cueId
							+ "'";
					if (status != null) {
						query += " and "
								+ ExpressionsSubmitQueueTable.SUBMISSION_STATUS
								+ "='" + status.name() + "'";
					}
					Cursor cursor = mSQLDb.rawQuery(query, null);
					if (cursor != null && cursor.moveToFirst()) {
						expressionsCount = cursor.getInt(0);
					}
				}
			} catch (Exception e) {

			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return expressionsCount;
	}

	/**
	 * Gets last/latest expression id from the entire records of the table
	 * 
	 * @return
	 */
	public int getLatestExpressionId(SUBMISSION_STATUS status) {
		int lastId = -1;
		if (mDBHandler != null && mDAOUtil != null && status != null) {
			try {
				mSQLDb = mDAOUtil.getReadableDatabase(mDBHandler);
				if (mSQLDb != null) {
					String query = "select * from "
							+ ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE
							+ " where "
							+ ExpressionsSubmitQueueTable.ID
							+ " = "
							+ "(select max("
							+ ExpressionsSubmitQueueTable.ID
							+ ") from "
							+ ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE
							+ ")" + " and "
							+ ExpressionsSubmitQueueTable.SUBMISSION_STATUS
							+ " = '" + status.name() + "'";
					Cursor cursor = mSQLDb.rawQuery(query, null);
					if (cursor != null && cursor.moveToFirst()) {
						lastId = cursor.getInt(0);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return lastId;
	}

	/**
	 * Gets oldest expression id from the entire records of the table
	 * 
	 * @return
	 */
	public int getOldestExpressionId(SUBMISSION_STATUS status) {
		int lastId = -1;
		if (mDBHandler != null && mDAOUtil != null && status != null) {
			try {
				mSQLDb = mDAOUtil.getReadableDatabase(mDBHandler);
				if (mSQLDb != null) {
					String query = "select * from "
							+ ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE
							+ " e1 "
							+ " where "
							+ ExpressionsSubmitQueueTable.ID
							+ " = "
							+ "(select min("
							+ ExpressionsSubmitQueueTable.ID
							+ ") from "
							+ ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE + " e2 "
							+ " where e1." + ExpressionsSubmitQueueTable.SUBMISSION_STATUS + " = " + "e2." + ExpressionsSubmitQueueTable.SUBMISSION_STATUS
							+ ")" + " and "
							+ ExpressionsSubmitQueueTable.SUBMISSION_STATUS
							+ " = '" + status.name() + "'"
							+ " group by " + ExpressionsSubmitQueueTable.SUBMISSION_STATUS;
					Cursor cursor = mSQLDb.rawQuery(query, null);
					//Cursor cursor = mSQLDb.query(ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE, null, selection, selectionArgs, groupBy, having, orderBy)
					if (cursor != null && cursor.moveToFirst()) {
						lastId = cursor.getInt(0);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return lastId;
	}

	/**
	 * Updates the expression submission status of the given expression id
	 * @param id
	 * @param status
	 */
	public synchronized void updateExpressionSubmissionStatus(int id,
			SUBMISSION_STATUS status) {
		if (mDAOUtil != null && mDBHandler != null && id != -1
				&& status != null) {
			try {
				mSQLDb = mDAOUtil.getReadableDatabase(mDBHandler);
				if (mSQLDb != null) {
					ContentValues cv = new ContentValues();
					cv.put(ExpressionsSubmitQueueTable.SUBMISSION_STATUS,
							status.name());
					mSQLDb.update(
							ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE,
							cv, ExpressionsSubmitQueueTable.ID + " =?",
							new String[] { String.valueOf(id) });
				}
			} catch (Exception e) {

			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
	}

	/**
	 * Deletes an expression from the DB
	 * 
	 * @param id
	 * @return
	 */
	public synchronized boolean deleteExpressionFromDB(int id) {
		boolean isDeleted = false;
		if (id != -1 && mDBHandler != null && mDAOUtil != null) {
			try {
				mSQLDb = mDAOUtil.getWritableDatabase(mDBHandler);
				int value = mSQLDb
						.delete(ExpressionsSubmitQueueTable.EXPRESSIONS_SUBMITQUEUE_TABLE,
								ExpressionsSubmitQueueTable.ID + " =?",
								new String[] { String.valueOf(id) });
				if (value > 0) {
					isDeleted = true;
				}
			} catch (Exception e) {

			} finally {
				mDAOUtil.closeDBObjects(null, mSQLDb);
			}
		}
		return isDeleted;
	}
}
