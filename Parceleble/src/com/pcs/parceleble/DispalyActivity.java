package com.pcs.parceleble;

import com.pcs.helper.Constants;
import com.pcs.model.Employee;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class DispalyActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		
		
		TextView display = (TextView)findViewById(R.id.display);
		
		Employee emp = getIntent().getParcelableExtra(Constants.Extras.DETAILS);
		
		if(emp!=null){
			//If received Data is not null set it to TextView
		display.setText(emp.toString());
		}else {
			Toast.makeText(this, getResources().getString(R.string.msg), Toast.LENGTH_LONG).show();
		}
	}

}
