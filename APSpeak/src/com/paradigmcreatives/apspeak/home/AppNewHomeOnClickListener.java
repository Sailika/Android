package com.paradigmcreatives.apspeak.home;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Listener class to handle onClicks from AppNewHomeActivity
 * 
 * @author Dileep | neuv
 * 
 */
public class AppNewHomeOnClickListener implements OnClickListener {

	private Activity mActivity;

	public AppNewHomeOnClickListener(Activity activity) {
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
