package com.paradigmcreatives.apspeak.app.contact.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.model.Provider;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Contact bean for discovery list
 * 
 * @author Soumya Behera | Paradigm Creatives
 * 
 */
public class ContactObject implements Parcelable, Comparable<ContactObject> {

    private static final String TAG = "ContactObject";

    private String contactName;
    private Bitmap contactImage;
    private Provider provider;
    private String contactId;
    private boolean isSelected;
    private boolean isDoodlydooContact;

    public ContactObject(String contactName, Bitmap contactImage, Provider provider, String contactId,
	    boolean isSelected, boolean isDoodlydooContact) {
	super();
	this.contactName = contactName;
	this.contactImage = contactImage;
	this.provider = provider;
	this.contactId = contactId;
	this.isSelected = isSelected;
	this.isDoodlydooContact = isDoodlydooContact;
    }

    public ContactObject(ContactObject contactObject) {
	super();
	if (contactObject != null) {
	    this.contactId = (TextUtils.isEmpty(contactObject.contactId)) ? null : new String(contactObject.contactId);
	    this.contactName = (TextUtils.isEmpty(contactObject.contactName)) ? null : new String(
		    contactObject.contactName);
	    this.provider = (contactObject.provider == null) ? null : new Provider(contactObject.provider);
	    this.isSelected = contactObject.isSelected;
	    this.isDoodlydooContact = contactObject.isDoodlydooContact;
	    this.contactImage = contactObject.contactImage;
	}
    }

    /**
     * Checks if the constraint string is a part of the contact
     * 
     * @param constraint
     * @return
     */
    public boolean matchString(String constraint) {
	boolean result = false;
	if (!TextUtils.isEmpty(contactName) && provider != null) {
	    if (!TextUtils.isEmpty(constraint)) {
		if (contactName.toLowerCase().contains(constraint.toLowerCase())
			|| provider.getProviderString().toLowerCase().contains(constraint.toLowerCase())) {
		    result = true;
		}
	    }
	}

	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
	boolean result = false;

	if (o != null && o instanceof ContactObject) {
	    ContactObject obj = (ContactObject) o;
	    // result = TextUtils.equals(this.contactName, obj.contactName);
	    result = /* result && */TextUtils.equals(this.contactId, obj.contactId);
	    // if (this.contactImage != null && obj.contactImage != null) {
	    // result = result && this.contactImage.equals(obj.contactImage);
	    // } else {
	    // result = result && (this.contactImage == null && obj.contactImage == null);
	    // }
	    if (this.provider != null && obj.provider != null) {
		result = result && this.provider.equals(obj.provider);
	    } else {
		result = result && (this.provider == null && obj.provider == null);
	    }
	} else {
	    Logger.warn(TAG, "Invalid object");
	}

	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	int contactNameLength = (TextUtils.isEmpty(contactName)) ? 0 : contactName.length();
	int contactIdLength = (TextUtils.isEmpty(contactId)) ? 0 : contactId.length();
	int contactImageCode = (contactImage == null) ? 0 : contactImage.hashCode();
	int providerCode = (provider == null) ? 0 : provider.hashCode();
	return (contactImageCode + contactNameLength + contactIdLength + providerCode);
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
	return contactName;
    }

    /**
     * @param contactName
     *            the contactName to set
     */
    public void setContactName(String contactName) {
	this.contactName = contactName;
    }

    /**
     * @return the contactImage
     */
    public Bitmap getContactImage() {
	return contactImage;
    }

    /**
     * @param contactImage
     *            the contactImage to set
     */
    public void setContactImage(Bitmap contactImage) {
	this.contactImage = contactImage;
    }

    /**
     * @return the provider
     */
    public Provider getProvider() {
	return provider;
    }

    /**
     * @param provider
     *            the provider to set
     */
    public void setProvider(Provider provider) {
	this.provider = provider;
    }

    /**
     * @return the contactId
     */
    public String getContactId() {
	return contactId;
    }

    /**
     * @param contactId
     *            the contactId to set
     */
    public void setContactId(String contactId) {
	this.contactId = contactId;
    }

    /**
     * @return the isSelected
     */
    public boolean isSelected() {
	return isSelected;
    }

    /**
     * @param isSelected
     *            the isSelected to set
     */
    public void setSelected(boolean isSelected) {
	this.isSelected = isSelected;
    }

    /**
     * @return the isDoodlydooContact
     */
    public boolean isDoodlydooContact() {
	return isDoodlydooContact;
    }

    /**
     * @param isDoodlydooContact
     *            the isDoodlydooContact to set
     */
    public void setDoodlydooContact(boolean isDoodlydooContact) {
	this.isDoodlydooContact = isDoodlydooContact;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ContactObject [contactName=" + contactName + ", contactImage=" + contactImage + ", provider="
		+ provider + ", contactId=" + contactId + ", isSelected=" + isSelected + ", isDoodlydooContact="
		+ isDoodlydooContact + "]";
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	out.writeString(contactId);
	out.writeValue(provider);
    }

    public ContactObject(Parcel in) {
	super();
	readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
	contactId = in.readString();
	provider = (Provider) in.readValue(Provider.class.getClassLoader());
    }

    public static final Parcelable.Creator<ContactObject> CREATOR = new Parcelable.Creator<ContactObject>() {

	public ContactObject createFromParcel(Parcel in) {
	    return new ContactObject(in);
	}

	public ContactObject[] newArray(int size) {
	    return new ContactObject[size];
	}
    };

    @Override
    public int compareTo(ContactObject another) {
	int order = 0;
	if (another != null && another instanceof ContactObject) {
	    if (!TextUtils.equals(another.contactName, contactName)) {
		if (contactName != null && another.contactName != null) {
		    order = contactName.compareToIgnoreCase(another.contactName);
		} else {
		    if (contactName != null) {
			order = 1;
		    } else if (another.contactName != null) {
			order = -1;
		    } else {
			order = 0;
		    }
		}
	    } else {
		if (another.provider != null && this.provider != null) {
		    if (another.provider.getProviderString() != null && provider.getProviderString() != null) {
			order = provider.getProviderString().compareToIgnoreCase(another.provider.getProviderString());
		    } else {
			if (provider.getProviderString() != null) {
			    order = 1;
			} else if (another.provider.getProviderString() != null) {
			    order = -1;
			} else {
			    order = 0;
			}
		    }
		} else {
		    if (provider != null) {
			order = 1;
		    } else if (another.provider != null) {
			order = -1;
		    } else {
			order = 0;
		    }
		}
	    }
	} else {
	    order = 1;
	}
	return order;
    }
}
