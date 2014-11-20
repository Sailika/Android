package com.paradigmcreatives.apspeak.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
public class HelpActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		WebView helpWebView = (WebView) findViewById(R.id.webview);

		helpWebView.loadUrl("https://whatsayapp.com/help-android.html");

	}

	@Override
	protected void onStart() {
		super.onStart();
		// Rest of the code should come here

	}

	@Override
	protected void onStop() {
		super.onStop();
		// Rest of the code should come here

	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getApplicationContext(), Constants.FACEBOOK_APPID);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

}
