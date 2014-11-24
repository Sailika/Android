package com.paradigmcreatives.apspeak.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;

/**
 * This class is used to show help screen
 * 
 * @author Vineela
 * 
 */
public class HelpActivity extends Fragment {

	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);

		view =  inflater.inflate(R.layout.help_layout, null);
		
		WebView helpWebView = (WebView)view.findViewById(R.id.webview);

		helpWebView.loadUrl("https://whatsayapp.com/help-android.html");
		
		return view;
		
	}

	@Override
	public void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getActivity(), Constants.FACEBOOK_APPID);
	}

}
