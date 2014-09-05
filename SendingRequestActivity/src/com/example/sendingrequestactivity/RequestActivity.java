package com.example.sendingrequestactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RequestActivity extends Activity{
	private EditText email_edt;
	private Button btn_signin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.request);
		email_edt = (EditText)findViewById(R.id.edt_gmail);
		btn_signin = (Button)findViewById(R.id.signin_btn);
		Toast.makeText(RequestActivity.this, getResources().getString(R.string.signin_message), Toast.LENGTH_LONG).show();
		btn_signin.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RequestActivity.this, DisplayActivity.class);
				intent.putExtra(Constants.ConstantsUser.user_name, email_edt.getText().toString());
				startActivity(intent);
				
			}
		});
	}

}
