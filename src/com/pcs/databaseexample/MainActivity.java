package com.pcs.databaseexample;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.pcs.databases.MyDataBase;
import com.pcs.databases.MyDataBase.Contrast;
import com.pcs.model.User;

public class MainActivity extends Activity {
	public MyDataBase database ;	//Crete database object

	public User users;	//Create object to model class(User)
	protected EditText nameEdt;
	protected EditText phoneEdt;
	protected EditText emailEdt;
	public ListView list;
	protected String name;
	protected String phone;
	protected String email; 
	public ArrayList<User> userlist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		userlist = new ArrayList<User>(); //Initialize ArrayList
		list = (ListView)findViewById(R.id.display);
		nameEdt =(EditText)findViewById(R.id.name);
		phoneEdt =(EditText)findViewById(R.id.phone);
		emailEdt =(EditText)findViewById(R.id.email);

		openDb(MainActivity.this); //Call openDatabase method

		database= new MyDataBase(MainActivity.this);



		Button insert = (Button)findViewById(R.id.insert);
		Button displayBtn = (Button)findViewById(R.id.display_btn);
		insert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//get text from user using EditText
				name = nameEdt.getText().toString();
				phone= phoneEdt.getText().toString();
				email= emailEdt.getText().toString();
				//crete new user object to insert the data
				users =new User();
				users.setName(name);
				users.setPhone(phone);
				users.setEmail(email);
               // Insert to database
				database.insert(users);
			}
		});
		displayBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Get cursor object from getContact() Method and initialize it to new Cursor cursor_display
				Cursor cursor_display= database.getContacts();

				SimpleCursorAdapter adpter= new SimpleCursorAdapter(MainActivity.this, 
									R.layout.view, cursor_display, new String[]{Contrast.NAME, Contrast.PHONE}
									,new int[]{R.id.txt,R.id.txt_one});

				list.setAdapter(adpter);

			}
		});
		//Close Database
		database.closeDB(); 
	}

	public SQLiteDatabase openDb(Context context) throws SQLException{

		MyDataBase database= new MyDataBase(context);
		SQLiteDatabase sqldb= database.getWritableDatabase();
		return sqldb;
	}

}
