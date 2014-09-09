package com.pcs.toastmessageactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ButtonsActivity extends Activity {
	private Button uname_btn;
	private Button pwd_btn;
	private Button email_btn;
	public static final int REQUEST_CODE_A = 1;
	public static final int REQUEST_CODE_B = 2;
	public static final int REQUEST_CODE_C = 3;
	public static final int REQUEST_CODE_D = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.button);
		uname_btn = (Button) findViewById(R.id.unameBtn);
		pwd_btn = (Button) findViewById(R.id.pwdBtn);
		email_btn = (Button) findViewById(R.id.emailBtn);

		uname_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ButtonsActivity.this,
						UsernameToastActivity.class);
				startActivityForResult(intent, REQUEST_CODE_B);

			}
		});
		pwd_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ButtonsActivity.this,
						PasswordToastActivity.class);
				startActivityForResult(intent, REQUEST_CODE_C);
			}
		});
		email_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ButtonsActivity.this,
						EmailToastActivity.class);
				startActivityForResult(intent, REQUEST_CODE_D);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if(requestCode==REQUEST_CODE_B)
			{
				String value = data.getExtras().getString(
						Constants.ConstantsExtra.USERNAME);
				Toast.makeText(ButtonsActivity.this,"Android Developer=" +" "+ value, Toast.LENGTH_LONG)
				.show();
			}

			if(requestCode==REQUEST_CODE_C)
			{
				String value = data.getExtras().getString(
						Constants.ConstantsExtra.PASSWORD);
				Toast.makeText(ButtonsActivity.this, "iOS Developer=" +" "+ value, Toast.LENGTH_LONG)
				.show();
			}
			if(requestCode==REQUEST_CODE_D)
			{
				String value = data.getExtras().getString(
						Constants.ConstantsExtra.EMAIL);
				Toast.makeText(ButtonsActivity.this,"Web Developer=" +" "+ value, Toast.LENGTH_LONG)
				.show();
			}
		}
	}
}
