package com.pcs.clientapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ContentResolver contentResolver;
	private Uri people;
	private Cursor mCursor;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list);
		contentResolver = getContentResolver();
		people = Uri
				.parse("content://com.pcs.contentprovider.ContentProviderHelper/personinfo");
		getContentResolver().registerContentObserver(people, false, mObserver);
		Button displayBtn = (Button) findViewById(R.id.show_btn);
		Button deleteBtn = (Button) findViewById(R.id.delete_btn);
		retreive(listView);
		displayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				retreive(listView);
			}
		});
		deleteBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				delete();
			}
		});
	}

	private ContentObserver mObserver = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			retreive(listView);
		}

		public void onChange(boolean selfChange, Uri uri) {
			retreive(listView);
		};
	};

	@SuppressWarnings("deprecation")
	private void retreive(final ListView listView) {
		mCursor = contentResolver.query(people, null, null, null, null);
		if (mCursor != null) {
			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					getBaseContext(),
					R.layout.cursordisplay,
					mCursor,
					new String[] { "_id", "username", "email", "address", "phone" },
					new int[] { R.id.id, R.id.name, R.id.email, R.id.address,
							R.id.phone, });
			listView.setAdapter(adapter);
		} else {
			Toast.makeText(getBaseContext(),
					getResources().getString(R.string.no_contacts),
					Toast.LENGTH_LONG).show();
		}
	}

	private void delete() {
		int count = contentResolver.delete(people, null, null);
		if (count > 0) {
			Toast.makeText(
					getBaseContext(),
					count + " "
							+ getResources().getString(R.string.delete_toast),
					Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getBaseContext(),
					getResources().getString(R.string.not_deleted),
					Toast.LENGTH_LONG).show();
		}
	}
}
