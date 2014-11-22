package com.paradigmcreatives.apspeak.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;

public class PrivacyPolicyActivity extends Fragment {
	private ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.privacy_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		WebView webView = (WebView) view.findViewById(R.id.webview);
		progressBar.bringToFront();
		webView.loadUrl("http://www.whatsayapp.com/privacy.html");
		webView.setWebViewClient(new WebViewClient() {

			public void onPageFinished(WebView view, String url) {
				if (progressBar != null
						&& (progressBar.getVisibility() == View.VISIBLE)) {
					progressBar.setVisibility(View.GONE);
				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(getActivity(), Constants.FACEBOOK_APPID);
	}

}
