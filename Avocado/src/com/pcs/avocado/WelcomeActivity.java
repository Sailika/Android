package com.pcs.avocado;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.pcs.constants.RegisterExtras;

public class WelcomeActivity extends Activity{

	private TextView welcomenote;

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		welcomenote =  (TextView)findViewById(R.id.wel_txt);
		
		String message = getIntent().getStringExtra(RegisterExtras.AvocadoExtras.user_name);

		welcomenote.setText(getResources().getString(R.string.welcome)+ " "+ message); 



	}
}