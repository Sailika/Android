package com.paradigmcreatives.apspeak.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.textstyles.TypeFontAssets;

public class AboutUsActivity extends Activity {
	
	private TextView aboutUsTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about_us_layout);
		
		aboutUsTxt = (TextView)findViewById(R.id.about_us_text);
		
		TypeFontAssets fontAssets = new TypeFontAssets(getApplicationContext());
		
		aboutUsTxt.setTypeface(fontAssets.lightFont);
		

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
		// TODO Auto-generated method stub
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
