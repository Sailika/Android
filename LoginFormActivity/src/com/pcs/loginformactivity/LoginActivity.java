package com.pcs.loginformactivity;

import com.pcs.constants.LoginExtras;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	public static final String TAG = LoginActivity.class.getSimpleName();

	private Button signup;
	private Button signin;
	private EditText uname_edt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		uname_edt = (EditText) findViewById(R.id.username_edt);
		signup = (Button) findViewById(R.id.btn_signup);
		signin = (Button) findViewById(R.id.btn_signin);

		signup.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(LoginActivity.this,DisplayActivity.class);
				
				intent.putExtra(LoginExtras.LoginFormExtras.user_name, uname_edt.getText().toString());
				
				startActivity(intent);

			}
		});
		signin.setOnClickListener(new OnClickListener() {  

			public void onClick(View v) {

				Intent intent = new Intent(LoginActivity.this,SigninActivity.class);
				startActivity(intent);

			}
		});
	}

}
