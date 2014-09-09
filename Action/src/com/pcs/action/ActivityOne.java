package com.pcs.action;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class ActivityOne extends Activity{

	protected static final int LENGTH_LONG = 100;
	private EditText first_edt;
	private Button submit_one;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);


		first_edt =(EditText)findViewById(R.id.first);		


		submit_one = (Button)findViewById(R.id.submit_one);
		submit_one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				Intent return_one = new Intent();
				return_one.putExtra("term_one", first_edt.getText().toString());
				setResult(Activity.RESULT_OK,return_one);
				finish();
			}

		});

	}
}
