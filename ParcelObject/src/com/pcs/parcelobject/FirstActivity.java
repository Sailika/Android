package com.pcs.parcelobject;

import com.example.parcelobject.R;
import com.pcs.helper.Constants;
import com.pcs.model.Customer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class FirstActivity extends Activity {
	
	private EditText name_edt;
	private EditText phone_edt;
	private EditText lnet_edt;
	private EditText pnet_edt;
	private Button submitBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first);
		
		name_edt = (EditText) findViewById(R.id.name_edt);
		phone_edt = (EditText) findViewById(R.id.phn_edt);
		lnet_edt = (EditText) findViewById(R.id.lastnet_edt);
		pnet_edt = (EditText) findViewById(R.id.prenet_edt);
		submitBtn = (Button) findViewById(R.id.submit_btn);
		
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstActivity.this, SecondActivity.class);
				Customer cust = new Customer();
				cust.setName(name_edt.getText().toString());
				cust.setPhone(phone_edt.getText().toString());
				cust.setLnet(lnet_edt.getText().toString());
				cust.setPnet(pnet_edt.getText().toString());
				intent.putExtra(Constants.Extras.DETAILS, cust);
				startActivity(intent);
			}
		});
	}

}
