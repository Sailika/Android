package com.pcs.swipedelete;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.pcs.adapters.Adapter;
import com.pcs.model.User;

public class MainActivity extends Activity {
	private ListView list;
	private ArrayList<User> userlist;
	private Adapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		list = (ListView) findViewById(R.id.list);
		userlist = new ArrayList<User>();

		User users = new User();
		users.setName("Skype");
		users.setMsgContent("Skype LoginSucessfull");
		userlist.add(users);

		users = new User();
		users.setName("Gmail");
		users.setMsgContent("Welcome to Gmail!");
		userlist.add(users);

		users = new User();
		users.setName("Whatsappp");
		users.setMsgContent("Start Using Whatsapp");
		userlist.add(users);
		
		users = new User();
		users.setName("John");
		users.setMsgContent("Hello,How are You??");
		userlist.add(users);
		
		users = new User();
		users.setName("BaseCamp");
		users.setMsgContent("Daily recap for thursday,Nov 21");
		userlist.add(users);
		
		users = new User();
		users.setName("Hike");
		users.setMsgContent("Get Free Recharge,Start USing Hike");
		userlist.add(users);

		adapter = new Adapter(MainActivity.this, userlist);
		list.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
