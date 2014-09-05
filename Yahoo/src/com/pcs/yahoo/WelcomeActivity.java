package com.pcs.yahoo;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yahoo.R;

public class WelcomeActivity extends Activity{

	private TextView WelcomeNoteTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {        	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);


		WelcomeNoteTxt =  (TextView)findViewById(R.id.wel_txt);
		String username = getIntent().getStringExtra(Constants.LoginExtras.USER_NAME);
		WelcomeNoteTxt.setText(getResources().getString(R.string.wel)+" " +username);
	}
}