package com.paradigmcreatives.apspeak.doodleboard.tasks;

import android.content.Context;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.database.expressionsdb.ExpressionsSubmitQueueDAO;
import com.paradigmcreatives.apspeak.app.model.ExpressionSubmitQueueBean;
import com.paradigmcreatives.apspeak.app.model.SubmitResultBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.doodleboard.DoodleView;
import com.paradigmcreatives.apspeak.doodleboard.handlers.SaveAndSubmitAssetHandler;
import com.paradigmcreatives.apspeak.doodleboard.send.AssetSubmitHelper;
import com.paradigmcreatives.apspeak.logging.Logger;

public class SaveAndSubmitAssetTask implements Runnable {

	private static final String TAG = "SaveAndSubmitAssetTask";
	private Context context;
	private SaveAndSubmitAssetHandler handler = null;
	private DoodleView doodleView;
	private String cueId;
	private String mFilePath;
	private TYPE mTaskType;
	private ExpressionSubmitQueueBean mExpression;

	public enum TYPE {
		SAVE, SUBMIT, SAVE_AND_SUBMIT
	};

	/**
	 * @param context
	 * @param doodleView
	 */
	public SaveAndSubmitAssetTask(Context context,
			SaveAndSubmitAssetHandler handler, DoodleView doodleView, String cueId,
			TYPE type) {
		this.context = context;
		this.handler = handler;
		this.doodleView = doodleView;
		this.cueId = cueId;
		this.mTaskType = type;
	}

	public SaveAndSubmitAssetTask(Context context,
			SaveAndSubmitAssetHandler handler,
			ExpressionSubmitQueueBean expression, TYPE type) {
		this.context = context;
		this.handler = handler;
		this.mExpression = expression;
		this.mTaskType = type;
	}

	@Override
	public void run() {
		if (mTaskType != null) {
			mFilePath = null;
			handler.onSaveStart();
			if (mTaskType == TYPE.SAVE) {
				mFilePath = addToQueue();
				handler.onSaved("");
			} else {
				if (mTaskType == TYPE.SAVE_AND_SUBMIT) {
					mFilePath = addToQueue();
					if(mExpression != null){
						mExpression.setFilePath(mFilePath);
					}
				}
				handler.onSubmitStart();
				AssetSubmitHelper helper = new AssetSubmitHelper(context,
						mExpression);

				SubmitResultBean res = helper.submitDoodle();
				if (res != null) {
					if (res.isSuccess()) {
						handler.onSubmit(res);
					} else {
						handler.onSubmitError(res.getError(),
								res.getErrorCode());
					}
				} else {
					handler.onSubmitError("Ooops!! Empty Response", -1);
				}
				Logger.info(TAG, "Submitted: " + res);
			}
		} else {
			Logger.warn(TAG, "Empty doodle view");
			;
		}

	}

	/**
	 * Adds expression submission to Queue
	 */
	public String addToQueue() {
		String filePath = null;
		if (context != null && doodleView != null) {
			String groupId = AppPropertiesUtil.getGroupId(context);
			String userId = AppPropertiesUtil.getUserID(context);
			filePath = AssetSubmitHelper.saveBitmapToAppPrivateFolder(context,
					doodleView.getDoodleBitmap());
			if (!TextUtils.isEmpty(filePath)) {
				ExpressionsSubmitQueueDAO dao = new ExpressionsSubmitQueueDAO(
						context);
				if (dao != null) {
					ExpressionSubmitQueueBean expression = new ExpressionSubmitQueueBean();
					expression.setGroupId(groupId);
					expression.setUserId(userId);
					expression.setCueId(cueId);
					expression.setType(Constants.IMAGE);
					expression.setFilePath(filePath);
					dao.insertExpressionToDB(expression);
				}
			}
		}
		return filePath;
	}

}
