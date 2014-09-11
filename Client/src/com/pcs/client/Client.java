package com.pcs.client;

import com.pcs.helper.Constants;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Client extends Activity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_activity);
		
		Button text =(Button)findViewById(R.id.text_btn);
		Button image = (Button)findViewById(R.id.image_btn);
		Button web =(Button)findViewById(R.id.web_btn);
		
		text.setOnClickListener(this);
		image.setOnClickListener(this);
		web.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		

		switch (v.getId()) {
		case R.id.image_btn:
			
			Intent imageIntent=new Intent(Constants.IntentExtras.IMAGE);
			imageIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			startActivity(imageIntent);
            break;
			
		case R.id.text_btn:
			Intent textIntent=new Intent(Constants.IntentExtras.TEXT);
			textIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			startActivity(textIntent);
			break;

		case R.id.web_btn:
			Intent webpageIntent=new Intent(Constants.IntentExtras.WEBPAGE);
			webpageIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			startActivity(webpageIntent);
			break;

		default:
			break;
		}
		
	}

	

}
