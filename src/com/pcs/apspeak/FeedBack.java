package com.pcs.apspeak;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FeedBack extends Fragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View ideas =inflater.inflate(R.layout.feedback_fragment, container, false);
		return ideas;
		
	}

}
