package com.paradigmcreatives.apspeak.app.invite.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.contact.ContactsListAdapter;
import com.paradigmcreatives.apspeak.app.contact.model.Contact;
import com.paradigmcreatives.apspeak.app.contact.model.ContactObject;
import com.paradigmcreatives.apspeak.app.invite.listeners.InviteContactsOnTextChangeListenerImpl;
import com.paradigmcreatives.apspeak.app.util.ContactsUtil;
import com.paradigmcreatives.apspeak.logging.Logger;

public class InviteContactsActivity extends Activity implements OnClickListener{
	 private static final String TAG = "InviteContactsActivity";

	    private ListView inviteContactList;
	    private EditText searchText;
	    private ArrayList<ContactObject> contactObjects;
	    private ContactsListAdapter contactsAdapter;
	    private ProgressBar spinner;
	    private ArrayList<Contact> contactList;
	    private TextView headerTitle;
	    private ImageView back;

	    /*
	     * (non-Javadoc)
	     * 
	     * @see android.app.Activity#onCreate(android.os.Bundle)
	     */
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.invite_contact_layout);
		init();
		
		contactsAdapter = null;
		contactObjects = null;

		inviteContactList.setFilterText("");
		searchText.addTextChangedListener(new InviteContactsOnTextChangeListenerImpl(this, inviteContactList));
		spinner = (ProgressBar) findViewById(R.id.contact_progress_spinner);
		hideProgressBar();

		try {
		    ContactsUtil contactsUtil = new ContactsUtil(this);
		    contactList = contactsUtil.getPhoneNumberContactStringList();
		    if(contactList != null && contactList.size()>0){
		    	inviteContactList.setAdapter(new ContactsListAdapter(this, contactList));
		    }
		    else{
		    	Logger.info("contacts list empty");
		    }
		} catch (IllegalThreadStateException itse) {
		    Logger.warn(TAG, itse.getLocalizedMessage());
		} catch (Exception e) {
		    Logger.warn(TAG, e.getLocalizedMessage());
		}
	    }



	    private void init() {
			// TODO Auto-generated method stub
	    	headerTitle = (TextView) findViewById(R.id.globel_header_text);
			inviteContactList = (ListView) findViewById(R.id.invite_contact_list);
			searchText = (EditText) findViewById(R.id.search_text);
			back = (ImageView) findViewById(R.id.home_back);
			back.setOnClickListener(this);
			headerTitle.setText(getResources().getString(R.string.invite_friends_heading));
		}



		@Override
	    protected void onDestroy() {
		
		super.onDestroy();
	    }

	    public void setContactList(ArrayList<ContactObject> contactObjects) {
		if (contactObjects != null) {
		    this.contactObjects = contactObjects;
		}
	    }



/*	    *//**
	     * Invites all the selected contacts
	     * 
	     * @param message
	     *//*
	    public void inviteSelectedContacts(String message) {
		if (contactsAdapter != null) {
		    ArrayList<ContactObject> selectedContacts = contactsAdapter.getSelectedContacts();
		    JSONArray contactIds = new JSONArray();
		    if (selectedContacts != null && selectedContacts.size() > 0) {
			for (ContactObject contactObject : selectedContacts) {
			    Provider provider = contactObject.getProvider();
			    if (provider != null) {
				String number = provider.getProviderString();
				contactIds.put(number);
			    } else {
				Logger.warn(TAG, "Provider is null");
			    }
			}
			try {
			    InviteContactsTask task = new InviteContactsTask(this);
			    task.setInviteesList(contactIds);
			    task.start();
			} catch (IllegalThreadStateException itse) {
			    Logger.warn(TAG, itse.getLocalizedMessage());
			} catch (Exception e) {
			    Logger.warn(TAG, e.getLocalizedMessage());
			}
		    }
		} else {
		    Logger.warn(TAG, "Contact adapter is null");
		}

		InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		finish();
	    }*/

	    public void showProgressBar() {
		if (spinner != null) {
		    inviteContactList.setTextFilterEnabled(false);
		    spinner.setVisibility(View.VISIBLE);
		} else {
		    Logger.warn(TAG, "Spinner is null");
		}
	    }

	    public void hideProgressBar() {
		if (spinner != null) {
		    inviteContactList.setTextFilterEnabled(true);
		    spinner.setVisibility(View.GONE);
		} else {
		    Logger.warn(TAG, "Spinner is null");
		}
	    }



		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.home_back:
				finish();
				break;

			default:
				break;
			}
		}

	    /**
	     * Launches the invitation permission dialog
	     *//*
	    public void launchSMSInvitePermissionDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.invite_sms_permission_dialog, null);
		final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
		TextView dialogTitle = (TextView) view.findViewById(R.id.dialog_title);
		final EditText inviteSmsBody = (EditText) view.findViewById(R.id.invite_sms_body);
		TextView inviteSmsText = (TextView) view.findViewById(R.id.invite_sms_dialog_text);
		TextView chargesText = (TextView) view.findViewById(R.id.charges);
		Typeface myTypeface = Typeface.createFromAsset(getAssets(), "Roboto-Light.ttf");
		Typeface myTypefaceBold = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
		dialogTitle.setTypeface(myTypefaceBold);
		Button yesButton = (Button) view.findViewById(R.id.yes_button);
		yesButton.setTypeface(myTypeface);
		chargesText.setTypeface(myTypeface);
		inviteSmsText.setTypeface(myTypeface);

		yesButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
			dialog.dismiss();
			inviteSelectedContacts(inviteSmsBody.getText().toString());
		    }
		});
		Button noButton = (Button) view.findViewById(R.id.no_button);
		noButton.setTypeface(myTypeface);
		noButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
			dialog.dismiss();
			InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			finish();
		    }
		});
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.show();
	    }*/
}
