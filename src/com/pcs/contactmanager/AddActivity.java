package com.pcs.contactmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pcs.constants.Constants;

public class AddActivity extends Activity {
	
	private EditText nameEdt;
	private EditText phoneEdt;
	private EditText emailEdt;
	private Button contactBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_add);
		
		nameEdt = (EditText)findViewById(R.id.name_edt);
		phoneEdt = (EditText)findViewById(R.id.phone_edt);
		emailEdt = (EditText)findViewById(R.id.email_edt);
		
		contactBtn = (Button) findViewById(R.id.add_btn);
		contactBtn.setOnClickListener(new OnClickListener() {
			
			
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddActivity.this, ContactsActivity.class);
				intent.putExtra(Constants.IntentExtras.NAME, nameEdt.getText().toString());
				intent.putExtra(Constants.IntentExtras.PHONE, phoneEdt.getText().toString());
				intent.putExtra(Constants.IntentExtras.EMAIL, emailEdt.getText().toString());
				setResult(Activity.RESULT_OK,intent);
				finish();
			}
		});
		
	}

}