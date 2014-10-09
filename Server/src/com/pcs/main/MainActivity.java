package com.pcs.main;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pcs.contentprovider.ContentProviderHelper;
import com.pcs.databases.DataBase;
import com.pcs.databases.DataBase.Contrast;
import com.pcs.model.User;

public class MainActivity extends Activity {

	public EditText id;
	public EditText name;
	public EditText phone;
	public EditText email;
	public EditText address;
	public Button Add;
	public Button Show;

	public User user;
	public DataBase mydb;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Add = (Button) findViewById(R.id.add_btn);
		Show = (Button) findViewById(R.id.show_btn);
		id = (EditText) findViewById(R.id.id_edt);

		name = (EditText) findViewById(R.id.name_edt);
		phone = (EditText) findViewById(R.id.phone_edt);
		email = (EditText) findViewById(R.id.email_edt);
		address = (EditText) findViewById(R.id.address_edt);
		Add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ContentValues values = new ContentValues();
				values.put(Contrast.ID, id.getText().toString());
				values.put(Contrast.UNAME, name.getText().toString());
				values.put(Contrast.EMAIL, email.getText().toString());
				values.put(Contrast.ADDRESS, address.getText().toString());
				values.put(Contrast.PHONE, phone.getText().toString());

				Uri uri = getContentResolver().insert(
						ContentProviderHelper.CONTENT_URI, values);

				Toast.makeText(getBaseContext(), uri.toString(),
						Toast.LENGTH_LONG).show();

			}
		});

		Show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(MainActivity.this,
						ShowActivity.class);
				startActivity(intent);
			}
		});

	}

}
