package com.pcs.action;



import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import android.widget.Toast;

public class FirstActivity extends Activity implements OnClickListener {

	public static final int RequestCode_one = 101;
	public static final int RequestCode_two = 102;
	public static final int RequestCode_three = 103;
	private static final int LENGTH_LONG = 100;
	private Button Screen_one;
	private Button Screen_two;
	private Button Screen_three;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		Screen_one = (Button)findViewById(R.id.one);
		Screen_two = (Button)findViewById(R.id.two);
		Screen_three = (Button)findViewById(R.id.three); 
		
		
	

		Screen_one.setOnClickListener(this); 
		Screen_two.setOnClickListener(this);
		Screen_three.setOnClickListener(this);
	}
			@Override
			public void onClick(View v) {
			
				switch(v.getId()){
				
				case R.id.one :
					Intent intent_one= new Intent(FirstActivity.this,ActivityOne.class);
					startActivityForResult(intent_one, RequestCode_one);
					break;
					
				case R.id.two : 
					
					Intent intent_two = new Intent(FirstActivity.this,ActivityTwo.class);
					
					startActivityForResult(intent_two, RequestCode_two);
					break;
					
				case R.id.three :
					Intent intent_three = new Intent(FirstActivity.this,ActivityThree.class);
					startActivityForResult(intent_three, RequestCode_three);
					break;
					default:
						break;
				}
				
			}
		
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		
		if(requestCode== RequestCode_one)
		{
		String term = data.getStringExtra("term_one");
			Toast.makeText(this,"You entered the term"+" "+term, LENGTH_LONG).show();
			
		}
		
		else if(requestCode== RequestCode_two)
		{
			 
			String term = data.getStringExtra("term_two");
			Toast.makeText(this,"You entered the term"+" "+term, LENGTH_LONG).show();
			
		}
		
		   else if(requestCode== RequestCode_three)
		{
			   String term =data.getStringExtra("term_three");
			Toast.makeText(this,"You entered the term"+" "+term, LENGTH_LONG).show();
			
		}
}
	
	

	}


