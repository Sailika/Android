package com.pcs.whatsapp;
import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class WelcomeActivity extends Activity{
	
	private EditText WelcomeNoteTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {        	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcomea_ctivity);
	
	
WelcomeNoteTxt =  (EditText)findViewById(R.id.welcome);
String username = getIntent().getStringExtra(Constants.LoginExtras.USER_NAME);
WelcomeNoteTxt.setText(getResources().getString(R.string.wel)+" " +username);
}
}