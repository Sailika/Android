package com.pcs.main;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.pcs.adapter.Adapter;
import com.pcs.contentprovider.ContentProviderHelper;
import com.pcs.databases.DataBase;

public class ShowActivity extends Activity {

	public Button Show;
	public ListView list;
	public Adapter cursorAdapter;
	public Cursor cursor_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show);
		Show = (Button) findViewById(R.id.show_btn);

		list = (ListView) findViewById(R.id.list);

		Show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/**
				 * String action = getIntent().getAction();
				 * 
				 * if((Constants.Extras.CLIENT).equals(action)){
				 **/
				ContentResolver resolver = getContentResolver();

				// String
				// url="content://com.pcs.contentprovider.ContentProviderHelper/people";
				// Uri people = Uri.parse(url);

				 cursor_main = resolver.query(
						ContentProviderHelper.CONTENT_URI,
						new String[] { 
	                            DataBase.Contrast.ID,DataBase.Contrast.UNAME,
							    DataBase.Contrast.EMAIL,DataBase.Contrast.ADDRESS,
							    DataBase.Contrast.PHONE	
							    },
							    null, null,null);

				if(cursor_main!=null){
					
						SimpleCursorAdapter adapter = new SimpleCursorAdapter(ShowActivity.this,R.layout.cursordisplay,
								cursor_main, new String[]{DataBase.Contrast.ID,DataBase.Contrast.UNAME,
									DataBase.Contrast.EMAIL,DataBase.Contrast.ADDRESS,DataBase.Contrast.PHONE},
									new int[]{R.id.id,R.id.name,R.id.email,R.id.address,R.id.phone});
							list.setAdapter(adapter);		
								
				}
				
			
				     
			}
		});

	}

}
