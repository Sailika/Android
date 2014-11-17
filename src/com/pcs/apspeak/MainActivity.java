package com.pcs.apspeak;

import com.pcs.adapter.TabListener;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity{
	// Declare Tab Variable
	ActionBar.Tab Tab1, Tab2, Tab3;
	Fragment feedback = new FeedBack();
	Fragment ideas = new Ideas();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
 
      

		ActionBar actionBar = getActionBar();
		  // Enabling Up / Back navigation
        actionBar.setDisplayHomeAsUpEnabled(true);

		// Hide Actionbar Icon
		actionBar.setDisplayShowHomeEnabled(true);

		// Hide Actionbar Title
		actionBar.setDisplayShowTitleEnabled(true);

		// Create Actionbar Tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set Tab Icon and Titles
		Tab1 = actionBar.newTab().setText("Ideas");
		Tab2 = actionBar.newTab().setText("FeedBack");

		// Set Tab Listeners
		Tab1.setTabListener(new TabListener(ideas));
		Tab2.setTabListener(new TabListener(feedback));

		// Add tabs to actionbar
		actionBar.addTab(Tab1);
		actionBar.addTab(Tab2);
	}


}
