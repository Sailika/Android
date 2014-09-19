package com.pcs.dialogexample;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class FormActivity extends Activity {


 private ProgressDialog progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form);
		 Button dateBtn = (Button)findViewById(R.id.date_btn);
		 Button timeBtn = (Button)findViewById(R.id.time_btn);
		 final EditText dateEdt = (EditText) findViewById(R.id.date_edt);
		
		 
		 dateBtn.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Calendar calendar = Calendar.getInstance();
				int year = calendar.get(Calendar.YEAR);
				final int month = calendar.get(Calendar.MONTH);
				int day = calendar.get(Calendar.DAY_OF_MONTH);
			
				
 				Dialog date_dialog = new DatePickerDialog(FormActivity.this, new OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
                        dateEdt.setText(year +"/" +monthOfYear +"/" +dayOfMonth );  	
                       
                        
					}
				}, year, month, day);
 				date_dialog.show();
			}
		});
		 
		 timeBtn.setOnClickListener(new android.view.View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Calendar calendar = Calendar.getInstance();
				final int hour = calendar.get(Calendar.HOUR);
					final int minutelocal = calendar.get(Calendar.MINUTE);
					
					final EditText timeEdt = (EditText) findViewById(R.id.time_edt);
					Dialog time_dialog = new TimePickerDialog(FormActivity.this, new OnTimeSetListener() {
						
						@Override
						public void onTimeSet(TimePicker view, final int hourOfDay, int minute) {
							
							timeEdt.setText(hourOfDay + ":" +minute);
							
						}
					}, hour, minutelocal, true);
	 				
	 			time_dialog.show();
				}
			}); 
		 
		 Button submitBtn = (Button)findViewById(R.id.submit_btn);
         submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		progress =new ProgressDialog(FormActivity.this);
		progress.setMessage("Sending");
		progress.setMax(100);
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.show();
		final Thread t = new Thread(){

			   @Override
			   public void run(){
			  /** Progress bar is updated until the thread runs **/

			      int jumpTime = 0;
			      while(jumpTime < 100){
			         try {
			            sleep(200);
			            jumpTime += 1;
			            progress.setProgress(jumpTime);
			           
			            	
			         }
			         catch (InterruptedException e) {
			           e.printStackTrace();
			         }

			      }
			   }
			   };
			   t.start();
		 
 				
			} 
		});
	}

}
