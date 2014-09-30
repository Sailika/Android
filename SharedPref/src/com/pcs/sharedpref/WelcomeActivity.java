package com.pcs.sharedpref;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WelcomeActivity extends Activity{

	protected TextView welcomeMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		welcomeMsg = (TextView)findViewById(R.id.msg);
		//get shared Prefernce token value from intent
		String message= getIntent().getStringExtra(Constants.Extras.uname).toString();
		//Display on Screen
		welcomeMsg.setText("Welcome,  " +message);
		
	}
}
