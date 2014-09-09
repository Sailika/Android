package com.pcs.parcelabledataactivity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.pcs.constants.Constants;
import com.pcs.model.AndroidVersions;

public class RecieverDataActivity extends Activity{
	private TextView print_edt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reciever);
		print_edt = (TextView)findViewById(R.id.print);
		AndroidVersions android_versions = getIntent().getParcelableExtra(Constants.IntentExtras.ANDROID);
		if(android_versions!=null)
		{
			print_edt.setText(android_versions.toString());
		
		}
		
	}
	

}
