package com.pcs.action;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ActivityThree extends Activity{
	private EditText third_edt;
	private Button submit_three;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		
		
    
		third_edt =(EditText)findViewById(R.id.third);
		
		
		submit_three = (Button)findViewById(R.id.submit_three);
		submit_three.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				  
				Intent return_third = new Intent();
				return_third.putExtra("term_three", third_edt.getText().toString());
				setResult(Activity.RESULT_OK,return_third);
				finish();
			}
		});
		
	}
	
	

}

