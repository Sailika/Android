package com.paradigmcreatives.apspeak.autosend;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.app.database.expressionsdb.ExpressionsSubmitQueueDAO;
import com.paradigmcreatives.apspeak.app.model.ExpressionSubmitQueueBean;
import com.paradigmcreatives.apspeak.app.model.SUBMISSION_STATUS;
import com.paradigmcreatives.apspeak.app.model.SubmitResultBean;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.handlers.SaveAndSubmitAssetHandler;
import com.paradigmcreatives.apspeak.doodleboard.listeners.SaveAndSubmitListener;
import com.paradigmcreatives.apspeak.doodleboard.tasks.SaveAndSubmitAssetTask;
import com.paradigmcreatives.apspeak.doodleboard.tasks.SaveAndSubmitAssetTask.TYPE;

/**
 * AutoSendManager, used to submit queued expressions to Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class AutoSendManager implements SaveAndSubmitListener {

	public static final int TRIES_COUNT = 1;
	private static AutoSendManager mManager;
	private Context mContext;
	private boolean mIsRunning;
	private int mSubmittingExpressionId;
	private int mTrailsCount;
	private boolean mIsInterrupted;
	private int mPriorityExpressionId;
	private boolean mShowSubmitDialog;
	private ProgressDialog mSubmitDialog;

	private AutoSendManager(Context context) {
		super();
		this.mContext = context;
		this.mIsRunning = false;
		this.mTrailsCount = 0;
		this.mSubmittingExpressionId = -1;
		this.mIsInterrupted = false;
		this.mPriorityExpressionId = -1;
		this.mShowSubmitDialog = false;
	}

	public static AutoSendManager getInstance(Context context) {
		if (mManager == null && context != null) {
			mManager = new AutoSendManager(context);
		}
		return mManager;
	}

	public void startSending() {
		if (mIsRunning) {
			// Nothing to do
		} else {
			mIsRunning = true;
			triggerSend(mSubmittingExpressionId);
		}
	}

	private void triggerSend(int expressionId) {
		if (mContext != null && Util.isOnline(mContext)) {
			ExpressionsSubmitQueueDAO dao = new ExpressionsSubmitQueueDAO(
					mContext);
			if (dao != null) {
				if (mSubmittingExpressionId == -1) {
					mSubmittingExpressionId = dao
							.getOldestExpressionId(SUBMISSION_STATUS.PENDING);
				}
				if (mSubmittingExpressionId != -1) {
					ExpressionSubmitQueueBean expression = dao
							.getExpressionFromDB(mSubmittingExpressionId);
					if (expression != null) {
						SaveAndSubmitAssetHandler handler = new SaveAndSubmitAssetHandler(
								this);
						SaveAndSubmitAssetTask task = new SaveAndSubmitAssetTask(
								mContext, handler, expression, TYPE.SUBMIT);
						Thread t = new Thread(task);
						t.start();
					}
				} else {
					// As there are no more expressions in submission queue with
					// PENDING status in db, hence reset the flag
					mIsRunning = false;
					/*
					 * // Broadcast Expressions submission complete/success
					 * Intent autoSendComplete = new Intent();
					 * autoSendComplete.setAction
					 * (Constants.AUTOSEND_STATUS_BRAODCAST_ACTION);
					 * mContext.sendBroadcast(autoSendComplete);
					 */
				}
			}
		} else {
			mIsRunning = false;
			mSubmittingExpressionId = -1;
			mTrailsCount = 0;
		}
	}

	@Override
	public void onSaved(String path) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveError(String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSaveStart() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSubmit(SubmitResultBean bean) {
		mTrailsCount = 0;
		// Dismiss submit dialog
		if (mSubmitDialog != null && mSubmitDialog.isShowing()) {
			mSubmitDialog.dismiss();
			if(mShowSubmitDialog){
				Toast.makeText(mContext, "Expression posted successfully", Toast.LENGTH_SHORT).show();
			}
		}
		mShowSubmitDialog = false;
		// Now, delete the record from database and trigger next submission
		if (mContext != null && mSubmittingExpressionId != -1) {
			ExpressionsSubmitQueueDAO dao = new ExpressionsSubmitQueueDAO(
					mContext);
			if (dao != null) {
				dao.deleteExpressionFromDB(mSubmittingExpressionId);
				// Broadcast AutoSend status, so that, queue layout in fragment
				// will be refreshed
				Intent autoSendSuccess = new Intent();
				autoSendSuccess
						.setAction(Constants.AUTOSEND_STATUS_BRAODCAST_ACTION);
				mContext.sendBroadcast(autoSendSuccess);
				// Reset and trigger next expression for auto send
				if (mIsInterrupted) {
					mSubmittingExpressionId = mPriorityExpressionId;
					mIsInterrupted = false;
					mShowSubmitDialog = true;
					mPriorityExpressionId = -1;
				} else {
					mSubmittingExpressionId = -1;
				}
				triggerSend(mSubmittingExpressionId);
			}
		}
	}

	@Override
	public void onSubmitError(String error, int errorCode) {
		/*
		 * Check whether we have tried the same expression(failed) submission
		 * for defined number of times. If yes, delete the record from DB else
		 * try until the trails count reaches defined number
		 */
		mTrailsCount++;
		if (mTrailsCount < TRIES_COUNT) {
			triggerSend(mSubmittingExpressionId);
		} else {
			// Reset Submit Dialog
			if (mSubmitDialog != null && mSubmitDialog.isShowing()) {
				mSubmitDialog.dismiss();
				if(mShowSubmitDialog){
					if(errorCode == HttpStatus.SC_FORBIDDEN){
						if(!TextUtils.isEmpty(error)){
							Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(mContext, error, Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(mContext, "Failed to post expression", Toast.LENGTH_SHORT).show();
					}
				}
			}
			mShowSubmitDialog = false;
			// Now, update the record in database with FAILED status
			if (mContext != null && mSubmittingExpressionId != -1) {
				ExpressionsSubmitQueueDAO dao = new ExpressionsSubmitQueueDAO(
						mContext);
				if (dao != null) {
					// dao.deleteExpressionFromDB(mSubmittingExpressionId);
					dao.updateExpressionSubmissionStatus(
							mSubmittingExpressionId, SUBMISSION_STATUS.FAILED);
					// Broadcast AutoSend status, so that, queue layout in
					// fragment will be refreshed
					Intent autoSendSuccess = new Intent();
					autoSendSuccess
							.setAction(Constants.AUTOSEND_STATUS_BRAODCAST_ACTION);
					mContext.sendBroadcast(autoSendSuccess);
					mTrailsCount = 0;
					// Reset and trigger next expression for auto send
					if (mIsInterrupted) {
						mSubmittingExpressionId = mPriorityExpressionId;
						mIsInterrupted = false;
						mShowSubmitDialog = true;
						mPriorityExpressionId = -1;
					} else {
						mSubmittingExpressionId = -1;
					}
					triggerSend(mSubmittingExpressionId);
				}
			}
		}
	}

	@Override
	public void onSubmitStart() {
		if (mSubmitDialog != null) {
			mSubmitDialog.setMessage("Almost done!");
			if(mShowSubmitDialog){
				mSubmitDialog.show();
			}
		}
	}

	/**
	 * Interrupts and submits the expression with passed id to server
	 * 
	 * @param expressionId
	 */
	public void interruptAndStartSending(int expressionId) {
		if (expressionId != -1) {
			if (mIsRunning) {
				mIsInterrupted = true;
				mPriorityExpressionId = expressionId;
			} else {
				mSubmittingExpressionId = expressionId;
				mShowSubmitDialog = true;
				startSending();
			}
		}
	}
	
	
	public void initializeSubmitDialog(ProgressDialog dialog){
		this.mSubmitDialog = dialog;
	}
}
