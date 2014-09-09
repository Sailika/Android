package com.pcs.logindemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;


public class LoginDemoActivity extends Activity{
	
	public static final String TAG = "Android";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logindemo);
		
		EditText uname_edt = (EditText) findViewById(R.id.uname);
		EditText pwd_edt = (EditText) findViewById(R.id.pwd);
		
		String username = uname_edt.getText().toString();
		String pwd = pwd_edt.getText().toString();
		
		Log.i(TAG,"Username: "+username);
		Log.i(TAG,"Password: "+pwd);
	}	

}
