package com.pcs.toastmessageactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class EmailToastActivity extends Activity{
	private EditText edtEmail;
	private Button btn_Done;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email);
		edtEmail = (EditText)findViewById(R.id.email_edt);
		btn_Done = (Button)findViewById(R.id.mail_btn);
		btn_Done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(Constants.ConstantsExtra.EMAIL,edtEmail.getText().toString());
				setResult(Activity.RESULT_OK, intent);
				finish();
				
			}
		});
		
	}

}
