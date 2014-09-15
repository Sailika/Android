package com.pcs.contactmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.pcs.adapters.CustomAdapter;
import com.pcs.constants.Constants;
import com.pcs.model.Contacts;

public class ContactsActivity extends Activity {

	private Button addBtn;
	private static final int REQ_A = 107;
	private ArrayList<Contacts> contactslist;
	private ListView ls;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts);

		addBtn = (Button) findViewById(R.id.add_btn);
		contactslist =   new ArrayList<Contacts>();
		ls = (ListView) findViewById(R.id.listview);


		CustomAdapter adapter = new CustomAdapter(ContactsActivity.this, contactslist);
		ls.setAdapter(adapter);
		ls.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String call = "tel:"+contactslist.get(position).getPhone();
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setData(Uri.parse(call));
				startActivity(intent);

			}
		});

		addBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ContactsActivity.this , AddActivity.class);
				startActivityForResult(intent, REQ_A);

			}

		});







	}

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
			contactslist.add(contact);

			CustomAdapter adapter = new CustomAdapter(ContactsActivity.this,
					contactslist);
			ls.setAdapter(adapter);

		}

	}



}