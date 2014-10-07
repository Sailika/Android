package com.pcs.parceleble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.pcs.model.Employee;

public class MainActivity extends Activity {
	
	public EditText name_edt;
	public EditText id_edt;
	public EditText desg_edt;
	public Button submit_btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		name_edt =(EditText)findViewById(R.id.name_edt);
		id_edt =(EditText)findViewById(R.id.id_edt);
		desg_edt =(EditText)findViewById(R.id.desg_edt);
		submit_btn =(Button)findViewById(R.id.submit);
		
		submit_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, DispalyActivity
						.class);
				
				//creating a new object for employee and setting data to Employee
				Employee emp = new Employee();
				emp.setName(name_edt.getText().toString());
				emp.setId(id_edt.getText().toString());
				emp.setDesg(desg_edt.getText().toString());
				
				//Put Pareceleble Extra
				intent.putExtra(com.pcs.helper.Constants.Extras.DETAILS, emp);
				
				//Start the Activity
				startActivity(intent);
			}
		});
		
	}

	
}
