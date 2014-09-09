package com.pcs.loginformactivity;

import com.pcs.constants.LoginExtras;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends Activity{
	
	private TextView welcomenote;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		welcomenote = (TextView )findViewById(R.id.txt_display);
		
		String message = getIntent().getStringExtra(LoginExtras.LoginFormExtras.user_name);
		
		welcomenote.setText(getResources().getString(R.string.welcome_display)+" "+message); 
		
		

	}
}
