package com.pcs.apspeak;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.pcs.adapter.CustomGridAdapter;
import com.pcs.model.FeedBackItems;

public class SingleViewActivity extends FragmentActivity implements OnClickListener {
	private ImageButton likeBtn;

	private List<FeedBackItems> mItems; // GridView items list

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_bar);
		likeBtn = (ImageButton) findViewById(R.id.like_btn);
	
		likeBtn.setOnClickListener(new OnClickListener() {
        
			@Override
			public void onClick(View v) {
				Boolean clicked = new Boolean(false);
				likeBtn.setTag(clicked); // wasn't clicked

				 if( ((Boolean)likeBtn.getTag())==false ){
		              likeBtn.setImageResource(R.drawable.like_button);
		              likeBtn.setTag(new Boolean(true));
				
				 }
				 else {
						likeBtn.setImageResource(R.drawable.inlike);

				}
			}
		});
		ActionBar actionBar = getActionBar();
		// Enabling Up / Back navigation
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Get intent data
		Intent intent = getIntent();

		// Selected image id
		int position = intent.getExtras().getInt("id");
		CustomGridAdapter imageAdapter = new CustomGridAdapter(
				getApplicationContext(), mItems);

		ImageView imageView = (ImageView) findViewById(R.id.expanded_image);
		TypedArray images = getResources().obtainTypedArray(R.array.drawables);

		imageView.setImageResource(images.getResourceId(position, -1));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		// Locate MenuItem with ShareActionProvider
		MenuItem item = menu.findItem(R.id.action_share);

		// Return true to display menu
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.action_share) {
			final View addView = getLayoutInflater().inflate(
					R.layout.custom_dialog, null);

			new AlertDialog.Builder(this)
					.setTitle("Share Via")
					.setView(addView)
					.show();
					

			// set values for custom dialog components - image button
			ImageButton whatsapp = (ImageButton) addView.findViewById(R.id.whatsapp);
			ImageButton facebook = (ImageButton) addView.findViewById(R.id.facebook);
			ImageButton twitter = (ImageButton) addView.findViewById(R.id.twitter);
			ImageButton mail = (ImageButton) addView.findViewById(R.id.mail);

			// if decline button is clicked, close the custom dialog
			 whatsapp.setOnClickListener(this);
			 facebook.setOnClickListener(this);

			 twitter.setOnClickListener(this);

			 mail.setOnClickListener(this);


		}
		return super.onOptionsItemSelected(item);
	}
	 @Override
	 public void onClick(View v) {
	 switch (v.getId()) {
	 case R.id.mail:
		 Intent mail_intent = new Intent(Intent.ACTION_SEND);
			mail_intent.setType("plain/Text");
			mail_intent.putExtra(Intent.EXTRA_EMAIL, "");
			mail_intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
			mail_intent.putExtra(Intent.EXTRA_TEXT, "mail body");
			startActivity(Intent.createChooser(mail_intent,"Select"));
	
	
	 break;
	 case R.id.twitter:
		 Intent twitter_intent = new Intent(SingleViewActivity.this,Twitter.class);
		
			startActivity(twitter_intent);
	
	
	 break;
	 case R.id.facebook:
		 Intent fb_intent = new Intent(SingleViewActivity.this,FaceBook.class);
		
			startActivity(fb_intent);
	
	
	 break;
	 case R.id.whatsapp:
		 Intent whatsapp_intent = new Intent(Intent.ACTION_SEND);
		 whatsapp_intent.setType("plain/Text");
		 whatsapp_intent.putExtra(Intent.EXTRA_EMAIL, "");
		 whatsapp_intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
		 whatsapp_intent.putExtra(Intent.EXTRA_TEXT, "mail body");
			startActivity(Intent.createChooser(whatsapp_intent,"Select"));
	
	
	 break;
	
	 default:
	 break;
	 }
	 }

	
}
