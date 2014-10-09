package com.pcs.contentprovider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.pcs.databases.DataBase;
import com.pcs.databases.DataBase.Contrast;

public class ContentProviderHelper extends ContentProvider{
	public DataBase mydb;
	public Context context;
	public SQLiteDatabase sqldb;
	
	   private static HashMap<String, String> STUDENTS_PROJECTION_MAP;
	public static final String  PROVIDER_NAME="com.pcs.contentprovider.ContentProviderHelper";
	public static final String URL = "content://" + PROVIDER_NAME + "/personinfo";
	   public final static Uri CONTENT_URI = Uri.parse(URL);
	//   private static HashMap<String, String> STUDENTS_PROJECTION_MAP;
	   
	   public static final int PERSON = 1;
	 public static final int PERSON_ID = 2;

	  public static final UriMatcher uriMatcher;
	   static{
	      uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	      uriMatcher.addURI(PROVIDER_NAME, "personinfo", PERSON);
	      uriMatcher.addURI(PROVIDER_NAME, "personinfo/#", PERSON);
	   }


	@Override
	public boolean onCreate() {
		context = getContext();
		mydb= new DataBase(context);
		sqldb= mydb.getWritableDatabase();
		
	      return (mydb == null)? false:true;	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		 SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
	      qb.setTables(DataBase.Contrast.TABLE_NAME);
	      
	      switch (uriMatcher.match(uri)) {
	      case PERSON:
	         qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
	         break;
	     
	      default:
	         throw new IllegalArgumentException("Unknown URI " + uri);
	      }
	      if (sortOrder == null || sortOrder == ""){
	       
	         sortOrder =DataBase.Contrast.UNAME;
	      }
	      Cursor c = qb.query(sqldb,projection,	selection, selectionArgs, 
	                          null, null, sortOrder);
	      /** 
	       * register to watch a content URI for changes
	       */
	      c.setNotificationUri(getContext().getContentResolver(), uri);

	      return c;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)){
	     
	      case PERSON:
	         return "vnd.android.cursor.dir/personinfo";
	     
	      case PERSON_ID:
	          return "vnd.android.cursor.item/persons";	   
	      default:
	         throw new IllegalArgumentException("Unsupported URI: " + uri);
	      }
	   }
	

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		long rowID= sqldb.insert(Contrast.TABLE_NAME, null, values);
		if (rowID > 0)
	      {
	         Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
	         getContext().getContentResolver().notifyChange(_uri, null);
	         return _uri;
	      }
	      throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		 int count = 0;

	      switch (uriMatcher.match(uri)){
	      case PERSON:
	         count = sqldb.delete(DataBase.Contrast.TABLE_NAME, selection, selectionArgs);
	         break;
	      case PERSON_ID:
	         String id = uri.getPathSegments().get(1);
	         count = sqldb.delete( DataBase.Contrast.TABLE_NAME,  DataBase.Contrast.ID  +  " = " + id + 
	                (!TextUtils.isEmpty(selection) ? " AND (" + 
	                selection + ')' : ""), selectionArgs);
	         break;
	      default: 
	         throw new IllegalArgumentException("Unknown URI " + uri);
	      }
	      
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		 int count = 0;
	      
	      switch (uriMatcher.match(uri)){
	      case PERSON:
	         count = sqldb.update(DataBase.Contrast.TABLE_NAME, values, 
	                 selection, selectionArgs);
	         break;
	      case PERSON_ID:
	         count = sqldb.update(DataBase.Contrast.TABLE_NAME, values, DataBase.Contrast.ID + 
	                 " = " + uri.getPathSegments().get(1) + 
	                 (!TextUtils.isEmpty(selection) ? " AND (" +
	                 selection + ')' : ""), selectionArgs);
	         break;
	      default: 
	         throw new IllegalArgumentException("Unknown URI " + uri );
	      }
	      getContext().getContentResolver().notifyChange(uri, null);
	      return count;
	}

}
