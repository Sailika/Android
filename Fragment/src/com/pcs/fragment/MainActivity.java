package com.pcs.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends FragmentActivity {
	private Button Home;
	private Button Contacts;
	private Button Whatsapp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		 
	}
	
	
/**
 * changing the fragment depending on the button clicked
 * @param view
 */
	public void selectFragment(View view){
		Fragment frag=null;
		 Home = (Button) findViewById(R.id.home);
	      Contacts = (Button) findViewById(R.id.contacts);
	      Whatsapp = (Button) findViewById(R.id.messenger);
	    
		if(view == Home)
		{
			frag = new FragmentOne();
		}
		if(view ==Contacts){
			frag = new FragmentTwo();
			
		}
		if(view== Whatsapp){
			frag= new FragmentThree();
		}

	
		android.app.FragmentManager fm =getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.place, frag);
		ft.commit();
	}



	
}