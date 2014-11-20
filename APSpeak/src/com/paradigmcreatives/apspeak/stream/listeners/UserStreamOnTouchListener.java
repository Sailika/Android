package com.paradigmcreatives.apspeak.stream.listeners;

import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;

/**
 * Listener to detect UserStream onTouch event
 * 
 * @author Dileep | neuv
 * 
 */
public class UserStreamOnTouchListener implements OnTouchListener {

	private GestureDetector gestureDetector;
	private Fragment fragment;

	public UserStreamOnTouchListener(GestureDetector gd, Fragment fragment) {
		super();
		this.gestureDetector = gd;
		this.fragment = fragment;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (fragment instanceof GlobalStreamsFragment) {
		//		((GlobalStreamsFragment) fragment).removeLongPressView();
			}

			break;
		case MotionEvent.ACTION_MOVE:
			break;

		}
		return gestureDetector.onTouchEvent(event);
	}
}
