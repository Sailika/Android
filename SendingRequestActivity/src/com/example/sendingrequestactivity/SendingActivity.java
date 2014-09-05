package com.example.sendingrequestactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SendingActivity extends Activity{
	private EditText email_edt;
	private Button btn_signup;
	private Button btn_signin;
	public static final int REQUEST_CODE_S = 101;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sending);
		email_edt = (EditText)findViewById(R.id.edt_gmail);
		btn_signup = (Button)findViewById(R.id.signup_btn);
		btn_signin = (Button)findViewById(R.id.signin_btn);
		btn_signup.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SendingActivity.this, DisplayActivity.class);
				intent.putExtra(Constants.ConstantsUser.user_name, email_edt.getText().toString());
				startActivity(intent);
				
			}
		});
		btn_signin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SendingActivity.this, RequestActivity.class);
				startActivity(intent);
				
			}
		});
		
	}

}
