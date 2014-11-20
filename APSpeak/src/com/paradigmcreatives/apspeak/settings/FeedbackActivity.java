package com.paradigmcreatives.apspeak.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;

public class FeedbackActivity extends Activity {
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_layout);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		WebView webView = (WebView) findViewById(R.id.webview);
		progressBar.bringToFront();
		webView.loadUrl("https://docs.google.com/a/neuv.in/forms/d/1JXc8nrF7oLUS3ILrsERVLTSWE1uR0gbJu596JQ9Ri4I/viewform");
		webView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				if (progressBar != null
						&& (progressBar.getVisibility() == View.VISIBLE)){
					progressBar.setVisibility(View.GONE);
				}
			}
		});
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
