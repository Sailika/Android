package com.pcs.action;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

public class ActivityTwo extends Activity{
	private EditText second_edt;
	private Button submit_two;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
	
		second_edt =(EditText)findViewById(R.id.second);

		
		
		submit_two = (Button)findViewById(R.id.submit_two);
		submit_two.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent return_two = new Intent();
				return_two.putExtra("term_two", second_edt.getText().toString());
				setResult(Activity.RESULT_OK,return_two);
				
				finish();
			}
		});
		
	
	
	}

}