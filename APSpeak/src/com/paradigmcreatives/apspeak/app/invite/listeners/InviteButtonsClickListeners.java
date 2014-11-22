package com.paradigmcreatives.apspeak.app.invite.listeners;

import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

public class InviteButtonsClickListeners implements OnClickListener{

	private Activity mActivity;

	public InviteButtonsClickListeners(Activity activity) {
		super();
		this.mActivity = activity;
	}

	@Override
	public void onClick(View view) {
		if (mActivity != null) {
			if (mActivity instanceof AppNewHomeActivity) {
				((AppNewHomeActivity) mActivity).handleItemSelected(view
						.getId());
			}
		}
	}

}
