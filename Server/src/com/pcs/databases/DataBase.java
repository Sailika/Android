package com.pcs.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper{
	public SQLiteDatabase sqldb;
	

	public DataBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	

	public DataBase(Context context) {
		super(context, Contrast.DATABASE_NAME, null, Contrast.DATABASE_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(Contrast.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Contrast.DROP_TABLE);
		onCreate(db);
	}
	

	public static class Contrast{
		
		public static final String DATABASE_NAME ="PersonData.db";
		public static final String PROVIDER_NAME ="PersonProvider";
		public static final String TABLE_NAME ="Data";
		public static final int DATABASE_VERSION =2;
		public static final String ID ="_id";
		public static final String UNAME ="username";
		public static final String PHONE ="phone";
		public static final String EMAIL ="email";
		public static final String ADDRESS ="address";
		public static final String CREATE_TABLE ="CREATE TABLE "+TABLE_NAME+"("+ID +" INTEGER NOT NULL,"+UNAME 
				+" TEXT NOT NULL,"+EMAIL+" TEXT NOT NULL,"+ADDRESS+" TEXT NOT NULL,"+PHONE+" INTEGER NOT NULL);";
		public static final String DROP_TABLE= "DROP TABLE IF EXISTS "+TABLE_NAME;
	
	}
}

