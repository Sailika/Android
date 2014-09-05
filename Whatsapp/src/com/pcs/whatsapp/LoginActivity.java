package com.pcs.whatsapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	public static final String TAG = "Android";

	private  Button LoginBtn;
	private EditText UserNameedt;
	private EditText PassWordedt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {        	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		 LoginBtn = (Button)findViewById(R.id.login_id);
		 UserNameedt = (EditText)findViewById(R.id.username);
		 PassWordedt = (EditText)findViewById(R.id.password);



         LoginBtn.setOnClickListener(new OnClickListener()
         {
        	 public void onClick(View v)
        	 {
        		 Intent intent = new Intent(LoginActivity.this,WelcomeActivity.class);
        		 intent.putExtra(Constants.LoginExtras.USER_NAME,UserNameedt.getText().toString());
        		 startActivity(intent);
        	 }
         });
			
			
	}



}
