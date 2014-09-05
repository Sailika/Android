package com.pcs.toastmessageactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PasswordToastActivity extends Activity{
	private EditText edtPwd;
	private Button btn_Done;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password);
		edtPwd = (EditText)findViewById(R.id.pwd_edt);
		btn_Done = (Button)findViewById(R.id.pass_btn);
		btn_Done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(Constants.ConstantsExtra.PASSWORD,edtPwd.getText().toString());
				setResult(Activity.RESULT_OK, intent);
				finish();
				
			}
		});
		
	}

}
