package com.pcs.adapter;

import com.pcs.databases.DataBase;
import com.pcs.main.R;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class Adapter extends CursorAdapter{
private LayoutInflater cursorinflator;
	public Adapter(Context context, Cursor c) {
		super(context, c);
		cursorinflator =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

	return cursorinflator.inflate(R.layout.cursordisplay, parent,false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView id=(TextView)view.findViewById(R.id.id);
		TextView name=(TextView)view.findViewById(R.id.name);
		TextView email=(TextView)view.findViewById(R.id.email);
		TextView address=(TextView)view.findViewById(R.id.address);
		TextView phone=(TextView)view.findViewById(R.id.phone);
		
			
		id.setText(cursor.getString(cursor.getColumnIndex(DataBase.Contrast.ID)));
		  name.setText(cursor.getString(cursor
					.getColumnIndex(DataBase.Contrast.UNAME)));
		  email.setText(cursor.getString(cursor
					.getColumnIndex(DataBase.Contrast.EMAIL)));
		  address.setText(cursor.getString(cursor
					.getColumnIndex(DataBase.Contrast.ADDRESS)));
		  phone.setText(cursor.getString(cursor
					.getColumnIndex(DataBase.Contrast.PHONE)));
	
		}
	}


