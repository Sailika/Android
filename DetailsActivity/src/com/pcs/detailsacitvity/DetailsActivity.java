package com.pcs.detailsacitvity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.pcs.detailsactivity.R;

public class DetailsActivity extends Activity implements OnClickListener{
	private Button loginBtn;
	private Button phnBtn;
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		loginBtn=(Button)findViewById(R.id.login_btn);
		phnBtn=(Button)findViewById(R.id.phone_btn);
		
		
		loginBtn.setOnClickListener(this);
		phnBtn.setOnClickListener(this);
		
		
	}


	@Override
	public void onClick(View v) {
		
		switch(v.getId())
		{
			case R.id.login_btn :
			
			Intent lgn_intent = new Intent(DetailsActivity.this,LoginActivity.class);
			startActivity(lgn_intent);
			break; 
			
			case R.id.phone_btn :
				
				Intent phn_intent = new Intent(DetailsActivity.this,PhoneActivity.class);
				startActivity(phn_intent);
				break; 
			
			
		}
	
		
		
	}

}
