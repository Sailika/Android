package com.paradigmcreatives.apspeak.app.util;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.contact.model.Contact;
import com.paradigmcreatives.apspeak.logging.Logger;

public class ContactsUtil {
	private static final String TAG = "ContactsUtil";

	private Context context;

	public ContactsUtil(Context context) {
		this.context = context;
	}
    /**
     * Gets the contact image for the contact id provided
     * 
     * @param contactId
     * @return
     */
    @SuppressLint("InlinedApi")
    public synchronized Bitmap getImageOfContact(long contactId) {
	Bitmap contactPhoto = null;

	if (context != null) {
	    Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	    if (contactUri != null) {
		Uri thumbUri = null;
		if (Build.VERSION.SDK_INT >= 11) {
		    ContentResolver cr = context.getContentResolver();
		    Cursor cursor = null;
		    try {
			cursor = cr.query(contactUri, new String[] { Contacts.PHOTO_THUMBNAIL_URI }, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
			    do {
				// getting photo id
				String thumbnailUriString = cursor.getString(cursor
					.getColumnIndex(Contacts.PHOTO_THUMBNAIL_URI));
				thumbUri = (TextUtils.isEmpty(thumbnailUriString)) ? null : Uri
					.parse(thumbnailUriString);
			    } while (cursor.moveToNext());
			}// else do nothing
		    } catch (IllegalArgumentException iae) {
			Logger.logStackTrace(iae);
		    } catch (CursorIndexOutOfBoundsException e) {
			Logger.logStackTrace(e);
		    } catch (Exception e) {
			Logger.logStackTrace(e);
		    } finally {
			try {
			    if (cursor != null) {
				cursor.close();
			    }
			} catch (Exception e) {
			    Logger.logStackTrace(e);
			}
		    }
		} else {
		    thumbUri = Uri.withAppendedPath(contactUri, Photo.CONTENT_DIRECTORY);
		}
		if (thumbUri != null) {
		    AssetFileDescriptor afd = null;
		    try {
			afd = context.getContentResolver().openAssetFileDescriptor(thumbUri, "r");
			/*
			 * Gets a file descriptor from the asset file descriptor. This object can be used across
			 * processes.
			 */
			FileDescriptor fileDescriptor = afd.getFileDescriptor();
			// Decode the photo file and return the result as a Bitmap
			// If the file descriptor is valid
			if (fileDescriptor != null) {
			    // Decodes the bitmap
			    contactPhoto = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, null);
			}
			// If the file isn't found
		    } catch (FileNotFoundException e) {
			Logger.warn(TAG, "Thumbnail file not found : " + e.getLocalizedMessage());
		    } catch (Exception e) {
			Logger.logStackTrace(e);
		    } finally {
			if (afd != null) {
			    try {
				afd.close();
			    } catch (IOException e) {
				Logger.logStackTrace(e);
			    } catch (Exception e) {
				Logger.logStackTrace(e);
			    }
			}
		    }
		} else {
		    Logger.warn(TAG, "Thumbnail uri is null");
		}
	    } else {
		Logger.warn(TAG, "Contact uri is null");
	    }
	} else {
	    Logger.warn(TAG, "context is null");
	}
	return contactPhoto;
    }
    /**
     * Gets the contact name from the contact id provided
     * 
     * @param contactId
     * @return
     */
    public synchronized String getContactName(long contactId) {
	String name = null;
	Cursor cursor = null;

	// Concatenate the phone number with the prefix
	if (contactId >= 0) {
	    try {
		if (context != null) {
		    // Initialize the variables
		    ContentResolver cr = context.getContentResolver();
		    Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);

		    cursor = cr.query(uri, new String[] { Contacts.DISPLAY_NAME }, null, null, null);
		    if (cursor != null && cursor.moveToFirst()) {
			do {
			    name = cursor.getString(cursor.getColumnIndex(Contacts.DISPLAY_NAME));
			    if (!TextUtils.isEmpty(name)) {
				break;
			    }
			} while (cursor.moveToNext());
		    }
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    } finally {
		attemptToCloseCursor(cursor);
	    }
	} else {
	   
	}
	return name;
    }


	/**
	 * Provides the list of all contacts. Just the strings saved in device
	 * 
	 * @return
	 */
	public synchronized ArrayList<Contact> getPhoneNumberContactStringList() {
		if (context == null) {
			return null;
		}

		ArrayList<Contact> contactStrings = null;
		String[] projection = { Phone.DISPLAY_NAME, Phone.NUMBER };

		// Query the Contacts DB to get the selected number
		Cursor cursor = null;

		try {
			cursor = context.getContentResolver().query(Phone.CONTENT_URI,
					projection, null, null,
					Phone.DISPLAY_NAME + " COLLATE NOCASE");
			if (cursor != null && cursor.moveToFirst()) {
				do {
					String phoneNumberString = cursor.getString(cursor
							.getColumnIndex(Phone.NUMBER));
					String contactNameString = cursor.getString(cursor
							.getColumnIndex(Phone.DISPLAY_NAME));
					if (!TextUtils.isEmpty(phoneNumberString)) {
						if (contactStrings == null) {
							contactStrings = new ArrayList<Contact>();
						}
						contactStrings.add(new Contact(phoneNumberString, contactNameString));
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Logger.logStackTrace(e);
		} finally {
			try {
				if (cursor != null) {
					cursor.close();
				}
			} catch (Exception e) {
				Logger.logStackTrace(e);
			}
		}

		return contactStrings;
	}
	 /**
     * Closes the given cursor
     * 
     * @param cursor
     */
    private void attemptToCloseCursor(Cursor cursor) {
	try {
	    if (cursor != null) {
		cursor.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
