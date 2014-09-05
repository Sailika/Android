package com.example.sendingrequestactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayActivity extends Activity{
	private TextView welcomenote;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		String message = getIntent().getStringExtra(Constants.ConstantsUser.user_name);
		welcomenote = (TextView)findViewById(R.id.display);
		welcomenote.setText(getResources().getString(R.string.welcome)+" "+message); 
		Toast.makeText(DisplayActivity.this, getResources().getString(R.string.signup_message), Toast.LENGTH_LONG).show();
	}

}
