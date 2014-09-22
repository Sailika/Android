package com.pcs.contactmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.pcs.adapters.CustomAdapter;
import com.pcs.constants.Constants;
import com.pcs.model.Contacts;

public class ContactsActivity extends Activity {

	private Button addBtn;
	private static final int REQ_A = 107;
	private ArrayList<Contacts> contactslist;
	private CustomAdapter adapter;
	private ListView ls;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);

		addBtn = (Button) findViewById(R.id.add_btn);
		ls = (ListView) findViewById(R.id.listview);
		
		/**Creating new Arraylist to hold contact details **/
		
		contactslist =   new ArrayList<Contacts>();
		
		

		 adapter = new CustomAdapter(ContactsActivity.this, contactslist);
		ls.setAdapter(adapter);
		

		
		/**Setting OnCLick Listener for Add ContactButton **/
		addBtn.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ContactsActivity.this , AddActivity.class);
				startActivityForResult(intent, REQ_A);

			}

		});
	
		

	}
	
	
	

/* Receiving data from CreateContact Activity and setting the data to list View**/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);


		if (data != null){

			Contacts contact = new Contacts();
			String name = data.getStringExtra(Constants.IntentExtras.NAME);
			String phone = data.getStringExtra(Constants.IntentExtras.PHONE);
			String email = data.getStringExtra(Constants.IntentExtras.EMAIL);

			contact.setName(name);
			contact.setEmail(email);
			contact.setPhone(phone);
			
			adapter.addUser(contact);

			ls.setAdapter(adapter);
			ls.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Toast.makeText(ContactsActivity.this, "hell", Toast.LENGTH_LONG).show();
				}
			});
			

		}

	}

	

}