package com.pcs.toastmessageactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UsernameToastActivity extends Activity{
	private EditText edtUname;
	private Button btn_Done;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.username);
		edtUname = (EditText)findViewById(R.id.uname_edt);
		btn_Done = (Button)findViewById(R.id.name_btn);
		btn_Done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(Constants.ConstantsExtra.USERNAME,edtUname.getText().toString());
				setResult(Activity.RESULT_OK, intent);
				finish();

			}
		});

	}

}
