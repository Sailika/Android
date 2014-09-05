package com.pcs.avocado;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pcs.constants.RegisterExtras;

public class SigninActivity extends Activity{
	
	private Button signin;
	private EditText uname_edt;
	private EditText pwd_edt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signin);
		
		uname_edt = (EditText) findViewById(R.id.username_edt);
		pwd_edt = (EditText) findViewById(R.id.password_edt);
		signin = (Button) findViewById(R.id.btn_signin);
		
		signin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				Intent intent = new Intent(SigninActivity.this,WelcomeActivity.class);
							
				intent.putExtra(RegisterExtras.AvocadoExtras.user_name, uname_edt.getText().toString());
				
				startActivity(intent);
							
			}
		});
	}

}