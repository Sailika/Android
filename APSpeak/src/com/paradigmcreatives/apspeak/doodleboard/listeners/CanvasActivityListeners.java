package com.paradigmcreatives.apspeak.doodleboard.listeners;

import org.apache.http.HttpStatus;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.app.model.SubmitResultBean;
import com.paradigmcreatives.apspeak.app.util.dialogs.WhatsayDialogsUtil;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImagePreviewFragment;

public class CanvasActivityListeners implements SaveAndSubmitListener {

	private Activity activity;
	private Fragment fragment;
	private ProgressDialog saveAndSubmitDialog = null;

	@SuppressLint("InlinedApi")
	public CanvasActivityListeners(Activity activity, Fragment fragment) {
		this.activity = activity;
		this.fragment = fragment;
		if (fragment != null) {
			saveAndSubmitDialog = new ProgressDialog(activity,
					ProgressDialog.THEME_HOLO_LIGHT);
			saveAndSubmitDialog.setTitle("Just a moment");
		}
	}

	@Override
	public void onSaveStart() {
		if (saveAndSubmitDialog != null) {
			saveAndSubmitDialog.setMessage("Putting your creation together!");
			saveAndSubmitDialog.setCancelable(false);
			saveAndSubmitDialog.show();
		}
	}

	@Override
	public void onSaved(String path) {
		if (saveAndSubmitDialog != null) {
			saveAndSubmitDialog.dismiss();
		}
		activity.setResult(Activity.RESULT_OK);
		activity.finish();
	}

	@Override
	public void onSaveError(String error) {
		if (saveAndSubmitDialog != null) {
			saveAndSubmitDialog.dismiss();
		}

		if (!TextUtils.isEmpty(error) && fragment != null) {
			Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
		}
		activity.setResult(Activity.RESULT_OK);
		activity.finish();
	}

	@Override
	public void onSubmitStart() {
		if (saveAndSubmitDialog != null) {
			saveAndSubmitDialog.setMessage("Almost done!");
		}
	}

	@Override
	public void onSubmit(SubmitResultBean bean) {
		if (saveAndSubmitDialog != null) {
			saveAndSubmitDialog.dismiss();
		}

		if (fragment != null && bean != null) {

			activity.setResult(Activity.RESULT_OK);
			activity.finish();
		}
	}

	@Override
	public void onSubmitError(String error, int errorCode) {
		if (saveAndSubmitDialog != null) {
			saveAndSubmitDialog.dismiss();
		}

		if (errorCode != HttpStatus.SC_FORBIDDEN) {
			showFailDialog();
		}
		if (!TextUtils.isEmpty(error) && fragment != null) {
			Toast.makeText(activity, error, Toast.LENGTH_SHORT).show();
		}
	}

	private void showFailDialog() {
		if (fragment instanceof ImagePreviewFragment) {
			Dialog dialog = WhatsayDialogsUtil.submitFailedDialog(activity,
					(ImagePreviewFragment) fragment);
			if (dialog != null) {
				dialog.show();
			}
		}
	}
}// end of class