package com.paradigmcreatives.apspeak.app.contact;

import java.util.ArrayList;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.contact.model.Contact;
import com.paradigmcreatives.apspeak.app.contact.model.ContactObject;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Adapter for the Contact list
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class ContactsListAdapter extends BaseAdapter {

	private static final String TAG = "ContactsListAdapter";

	private Activity activity;
	private ArrayList<Contact> listOfContacts;

	public ContactsListAdapter(Activity activity, ArrayList<Contact> contacts) {
		super();
		this.activity = activity;
		this.listOfContacts = contacts;

	}

	@Override
	public int getCount() {
		return listOfContacts.size();
	}

	@Override
	public Contact getItem(int position) {

		return listOfContacts.get(position);

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.contact_list_item, null);

			viewHolder.contactImage = (ImageView) convertView
					.findViewById(R.id.contact_image);
			viewHolder.contactNameText = (TextView) convertView
					.findViewById(R.id.contact_name);
			convertView.setFocusable(false);
			convertView.setClickable(false);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (viewHolder != null) {
			setContactImage(position, viewHolder.contactImage);
			setContactName(position, viewHolder.contactNameText);
		}

		return convertView;
	}

	private class ViewHolder {
		private ImageView contactImage;
		private TextView contactNameText;
	}

	private void setContactName(int position, TextView contactNameText) {
		if (contactNameText == null) {
			return;
		}
		Contact contactObject = getItem(position);
		if (contactObject != null) {

			contactNameText.setText(contactObject.getContactName());
		} else {
			Logger.info(TAG, "Contact object is null");
		}
	}

	private void setContactImage(int position, ImageView contactImage) {
		if (contactImage == null) {
			return;
		}
		Contact contactObject = getItem(position);
		if (contactObject != null) {

			contactImage.setImageResource(R.drawable.ic_launcher);

		} else {
			Logger.info(TAG, "Contact object is null");
		}
	}

}
