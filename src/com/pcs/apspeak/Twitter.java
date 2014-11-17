package com.pcs.apspeak;

import java.util.List;

import org.brickred.socialauth.Photo;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Twitter extends Activity {
	// SocialAuth Component
	SocialAuthAdapter adapter;
	Profile profileMap;
	List<Photo> photosList;

	// Android Components
	Button update;
	EditText edit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.twitter);

		// Share Button
		Button share = (Button) findViewById(R.id.sharebutton);

		// Add it to Library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add provider
		adapter.addProvider(Provider.FACEBOOK, R.drawable.fb);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);

		// Providers require setting user call Back url
		adapter.addCallBack(Provider.TWITTER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");
		adapter.addCallBack(Provider.YAMMER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		// Enable Provider
		adapter.enable(share);

	}

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		@Override
		public void onComplete(Bundle values) {

			Log.d("ShareButton", "Authentication Successful");

			// Get name of provider after authentication
			final String providerName = values
					.getString(SocialAuthAdapter.PROVIDER);
			Toast.makeText(Twitter.this, providerName + " connected",
					Toast.LENGTH_LONG).show();

			update = (Button) findViewById(R.id.update);
			edit = (EditText) findViewById(R.id.editTxt);

			update.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					adapter.updateStatus(edit.getText().toString(),
							new MessageListener(), false);
				}
			});

		}

		@Override
		public void onError(SocialAuthError error) {
			Log.d("ShareButton", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			Log.d("ShareButton", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			Log.d("Share-Button", "Dialog Closed by pressing Back Key");
		}

	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {
		@Override
		public void onExecute(String provider, Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(Twitter.this, "Message posted on " + provider,
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(Twitter.this,
						"Message not posted on " + provider, Toast.LENGTH_LONG)
						.show();
		}

		@Override
		public void onError(SocialAuthError e) {

		}
	}

}
