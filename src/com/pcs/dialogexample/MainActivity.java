package com.pcs.dialogexample;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity{
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		Button setBtn = (Button)findViewById(R.id.set_btn);
		

		
		setBtn.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
				dialog.setTitle(R.string.dialog_title);
				dialog.setMessage(R.string.dialog_msg);
				dialog.setPositiveButton(R.string.ok_btn, new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						ProgressDialog loading = new ProgressDialog(MainActivity.this);
						loading.setMessage(getResources().getString(R.string.loading_dialog));
						loading.show();
						Intent intent = new Intent(MainActivity.this,FormActivity.class);
						startActivity(intent);
						
					}
				});
		dialog.setNegativeButton(R.string.cancel_btn, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		 dialog.show();
		
			}
		});		

	}
	}
	
