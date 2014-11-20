package com.paradigmcreatives.apspeak.user;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paradigmcreatives.apspeak.R;

public class UserTagsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.user_tags, container, false);
	initializAllViews(view);
	return view;

    }

    private void initializAllViews(View view) {
    }
}
