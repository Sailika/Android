package com.paradigmcreatives.apspeak.doodleboard.handlers;

import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.app.model.SubmitResultBean;
import com.paradigmcreatives.apspeak.doodleboard.listeners.SaveAndSubmitListener;

/**
 * Handle the save and submit
 * 
 * @author robin
 * 
 */
public class SaveAndSubmitAssetHandler extends Handler implements SaveAndSubmitListener {

	private static final int SAVE_START = 0;
	private static final int SAVED = 1;
	private static final int SAVE_ERROR = 2;
	private static final int SUBMIT_START = 3;
	private static final int SUBMIT = 4;
	private static final int SUBMIT_ERROR = 5;

	private SaveAndSubmitListener listener = null;

	public SaveAndSubmitAssetHandler(SaveAndSubmitListener listener) {
		this.listener = listener;
	}

	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg != null && listener != null) {
			switch (msg.what) {
			case SAVE_START:
				listener.onSaveStart();
				break;
			case SAVED:
				listener.onSaved((String) msg.obj);
				break;
			case SAVE_ERROR:
				listener.onSaveError((String) msg.obj);
				break;
			case SUBMIT_START:
				listener.onSubmitStart();
				break;
			case SUBMIT:
				listener.onSubmit((SubmitResultBean) msg.obj);
				break;
			case SUBMIT_ERROR:
				listener.onSubmitError((String) msg.obj, msg.arg1);
				break;
			}
		}
	}

	@Override
	public void onSaveStart() {
		sendEmptyMessage(SAVE_START);
	}

	@Override
	public void onSubmitStart() {
		sendEmptyMessage(SUBMIT_START);

	}

	@Override
	public void onSaved(String path) {
		Message msg = new Message();
		msg.what = SAVED;
		msg.obj = path;
		sendMessage(msg);
	}

	@Override
	public void onSaveError(String error) {
		Message msg = new Message();
		msg.what = SAVE_ERROR;
		msg.obj = error;
		sendMessage(msg);
	}

	@Override
	public void onSubmit(SubmitResultBean bean) {
		Message msg = new Message();
		msg.what = SUBMIT;
		msg.obj = bean;
		sendMessage(msg);
	}

	@Override
	public void onSubmitError(String error, int errorCode) {
		Message msg = new Message();
		msg.what = SUBMIT_ERROR;
		msg.obj = error;
		msg.arg1 = errorCode;
		sendMessage(msg);
	}
}
