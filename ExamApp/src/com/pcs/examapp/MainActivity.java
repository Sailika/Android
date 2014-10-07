package com.pcs.examapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity {
	protected EditText username_edt;
	protected EditText password_edt;
	protected Button login_btn;
	public String uname;
	public String pwd ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		login_btn =(Button)findViewById(R.id.login_btn);
		username_edt = (EditText)findViewById(R.id.username_edt);
		password_edt = (EditText)findViewById(R.id.password_edt);
		uname = username_edt.getText().toString();
		pwd = password_edt.getText().toString();
		login_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Checking if the editFields are null
				if(uname!= null && pwd!=null){
					Intent intent_login = new Intent(MainActivity.this,CityActivity.class);
					startActivity(intent_login);


				}
				else{

					//Display Message if null
					Toast.makeText(MainActivity.this, getResources().getString(R.string.login_msg), Toast.LENGTH_LONG).show();
				}

			}
		});

	}





}
