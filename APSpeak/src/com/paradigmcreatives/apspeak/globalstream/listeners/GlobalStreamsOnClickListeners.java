package com.paradigmcreatives.apspeak.globalstream.listeners;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.globalstream.fragments.GlobalStreamsFragment;

public class GlobalStreamsOnClickListeners implements OnClickListener {

	private Fragment mFragment;

	public GlobalStreamsOnClickListeners(Fragment fragment) {
		super();
		this.mFragment = fragment;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.global_streams_cuedetails_layout:
		case R.id.global_stream_make_post:
			if (mFragment != null && mFragment instanceof GlobalStreamsFragment) {
				((GlobalStreamsFragment) mFragment)
						.launchImageSelectionActivity();
			}
			break;
			//APSpeak
	/*	case R.id.grid_list_switch_layout:
		case R.id.grid_list_switch_icon:
			if(mFragment != null && mFragment instanceof GlobalStreamsFragment){
				((GlobalStreamsFragment) mFragment).showListViewOrGridView();
			}
			break;*/

		case R.id.college_button_layout:
		case R.id.allcolleges_button_layout:
		//case R.id.friends_button_layout:
			if (mFragment != null && mFragment instanceof GlobalStreamsFragment) {
				((GlobalStreamsFragment) mFragment).handleStreamTypeSelected(v
						.getId());
			}
			break;

		default:
			break;
		}
	}
}
