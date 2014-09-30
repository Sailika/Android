package com.pcs.sharedpref;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public SharedPreferences pref;
	protected EditText username;
	protected EditText email;
	protected Button question;
	protected Button login;
	protected String VALUE = "value";
	protected String MESSAGE ;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		username =(EditText)findViewById(R.id.username);
		login=(Button)findViewById(R.id.login);
		question= (Button)findViewById(R.id.question);
		email =(EditText)findViewById(R.id.email);
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				MESSAGE = username.getText().toString();
				pref = getPreferences(MODE_PRIVATE);

				SharedPreferences.Editor editor = pref.edit();
				editor.putString(VALUE,MESSAGE );
				editor.commit();
				Toast.makeText(MainActivity.this,getResources().getString(R.string.login_msg),Toast.LENGTH_LONG).show();
				
			}
		});


		question.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String name= pref.getString(VALUE,"no Value");
				Toast.makeText(MainActivity.this,getResources().getString(R.string.ques_msg),
						Toast.LENGTH_LONG).show();
				if(name!= null ){

				Intent intent = new Intent(MainActivity.this,WelcomeActivity.class);
				intent.putExtra(Constants.Extras.uname, name);
				startActivity(intent);
				}else{
					Toast.makeText(MainActivity.this,getResources().getString(R.string.toast),
							Toast.LENGTH_LONG).show();
				}
				

		}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
