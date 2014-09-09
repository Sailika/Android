package com.pcs.gallery;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
    private static final int LENGTH_LONG = 0;
	private Button Whatsapp;
    private Button Line;
    private Button Hike;
    private Button Facebook;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	Whatsapp = (Button)findViewById(R.id.what_btn);
	Line = (Button)findViewById(R.id.line_btn);
	Hike = (Button)findViewById(R.id.hike_btn);
	Facebook = (Button)findViewById(R.id.fb_btn);
	
	Whatsapp.setOnClickListener(this);
	Line.setOnClickListener(this);
	Hike.setOnClickListener(this);
	Facebook.setOnClickListener(this);
	
	}
	
	@Override
	public void onClick(View v) {
	  v.getId();
	  View background = findViewById(R.id.background);
	   
	  
	  switch(v.getId()){
	  
	  case R.id.what_btn :
		  background.setBackgroundResource(R.drawable.backwhats);
		  Toast.makeText(MainActivity.this,getResources().getString(R.string.msgw), LENGTH_LONG).show();
		  break;
		  
		  
	  case R.id.hike_btn :
		  background.setBackgroundResource(R.drawable.backhike);
		  Toast.makeText(MainActivity.this,getResources().getString(R.string.msgh), LENGTH_LONG).show();
		  break;  
		  
	  case R.id.line_btn :
		  background.setBackgroundResource(R.drawable.backline);
		  Toast.makeText(MainActivity.this,getResources().getString(R.string.msgl), LENGTH_LONG).show();
		  break;	  
		  
	  case R.id.fb_btn :
		  background.setBackgroundResource(R.drawable.backfb);
		  Toast.makeText(MainActivity.this,getResources().getString(R.string.msgf), LENGTH_LONG).show();
		  break;  
		  
		  default:
			  break;
		  
	  }
		
	}


	
}
