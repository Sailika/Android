package com.pcs.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.pcs.model.User;

public class MyDataBase extends SQLiteOpenHelper{
	public Cursor cursor;


	public MyDataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	public MyDataBase(Context context){
		super(context,Contrast.DATABASE_NAME, null, Contrast.DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Contrast.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " +Contrast.TABLE_NAME);
		onCreate(db);
	}
  //This ?Method opens the database and inserts values into database
	public User insert(User users){
		SQLiteDatabase sqldb= this.getWritableDatabase();
		ContentValues values = new ContentValues();
		//Get values from Model Class and store them in ContentValues class 
		values.put(Contrast.NAME, users.getName());
		values.put(Contrast.PHONE, users.getPhone());
		values.put(Contrast.EMAIL, users.getEmail());
		sqldb.insert(Contrast.TABLE_NAME, null, values);
		//Return Model Class(User) object
		return users;
	}
	
	//Returns cursor object that contains the data from query
	public Cursor getContacts(){
		
		SQLiteDatabase sqldb =this.getReadableDatabase();
	 cursor=sqldb.query(Contrast.TABLE_NAME,new String[]{Contrast.ID,Contrast.NAME,Contrast.PHONE,Contrast.EMAIL},null, null, null, null, null);
	
	return cursor;
	}
	//Method to CLose Databse

	public void closeDB() {
		SQLiteDatabase db = this.getReadableDatabase();
		if (db != null && db.isOpen())
			db.close();
	}
	
	

	public static class Contrast{

		// Database Version
		public static final int DATABASE_VERSION = 2;

		// Database Name
		public static final String DATABASE_NAME = "contactsManager";

		public final static String TABLE_NAME = "contacts";

		public final static String NAME ="Name";
		public final static String PHONE ="Phone";
		public final static String EMAIL ="Email";
		public final static String ID ="_id";

		private static final String CREATE_TABLE = "CREATE TABLE "
				+ TABLE_NAME + "(" +ID +" INTEGER," + NAME +" TEXT," +PHONE+ " INTEGER," +EMAIL +" TEXT);";


	}

}
