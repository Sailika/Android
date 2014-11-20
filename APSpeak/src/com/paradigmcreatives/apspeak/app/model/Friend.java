package com.paradigmcreatives.apspeak.app.model;

import java.util.Comparator;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Friend, to represent a doodlydoo friend connected via social network channel(s)
 * 
 * @author Dileep | neuv
 * 
 */
public class Friend extends User implements Comparable<Friend>, Comparator<Friend>, Parcelable {

    private FRIEND_TYPE friendType;
    private String fb_id;
    private boolean followStatus;

    public enum FRIEND_TYPE {
	CONTACT, FACEBOOK, TWITTER, EMAIL
    };

    /**
     * Default constructor
     */
    public Friend() {
	super();
	this.friendType = FRIEND_TYPE.FACEBOOK;
    }

    public Friend(Parcel in) {
	super(in);
	int friendTypeOrdinal = in.readInt();
	friendType = FRIEND_TYPE.values()[friendTypeOrdinal];
	followStatus = (in.readByte() == 1) ? true : false;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    public String getLocation() {
	return this.location;
    }

    public void setFriendType(FRIEND_TYPE type) {
	this.friendType = type;
    }

    public FRIEND_TYPE getFriendType() {
	return this.friendType;
    }

    public void setFollowStatus(boolean status) {
	this.followStatus = status;
    }

    public boolean getFollowStatus() {
	return this.followStatus;
    }

    public void setFacebookId(String id) {
	this.fb_id = id;
    }

    public String getFacebookId() {
	return this.fb_id;
    }

    /**
     * Checks if the constraint string is a part of the contact
     * 
     * @param constraint
     * @return
     */
    public boolean matchString(String constraint) {
	boolean result = false;
	if (!TextUtils.isEmpty(name)) {
	    if (!TextUtils.isEmpty(constraint)) {
		if (name.toLowerCase().contains(constraint.toLowerCase())) {
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
	if (o == null) {
	    return false;
	}

	if (o instanceof Friend) {
	    if (!TextUtils.isEmpty(this.userId) && !TextUtils.isEmpty(((Friend) o).getUserId())) {
		if (TextUtils.equals(this.userId, ((Friend) o).getUserId())) {
		    return true;
		} else {
		    return false;
		}
	    } else if (!TextUtils.isEmpty(this.fb_id) && !TextUtils.isEmpty(((Friend) o).getFacebookId())) {
		if (TextUtils.equals(this.fb_id, ((Friend) o).getFacebookId())) {
		    return true;
		} else {
		    return false;
		}
	    } else {
		return false;
	    }
	} else {
	    return false;
	}
    }

    @Override
    public int compare(Friend lhs, Friend rhs) {
	if (lhs == null && rhs == null) {
	    return 0;
	}

	if (lhs == null && rhs != null) {
	    return -1;
	}

	if (lhs != null && rhs == null) {
	    return 1;
	}

	if (lhs.equals(rhs)) {
	    return 0;
	}

	String lName = lhs.getName();
	String rName = rhs.getName();

	if (TextUtils.isEmpty(lName) || TextUtils.isEmpty(rName)) {
	    lName = lhs.getUniqueHandle();
	    rName = rhs.getUniqueHandle();
	}

	if (!TextUtils.isEmpty(lName) && !TextUtils.isEmpty(rName)) {
	    return lName.compareTo(rName);
	}

	return 0;
    }

    @Override
    public int compareTo(Friend another) {
	return compare(this, another);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.neuv.doodlydoo.app.model.User#writeToParcel(android.os.Parcel, int)
     */
    @Override
    public void writeToParcel(Parcel out, int flags) {
	super.writeToParcel(out, flags);
	out.writeInt(friendType.ordinal());
	out.writeByte((byte) (followStatus ? 1 : 0));
    }

    public static final Parcelable.Creator<Friend> CREATOR = new Parcelable.Creator<Friend>() {
	public Friend createFromParcel(Parcel in) {
	    return new Friend(in);
	}

	public Friend[] newArray(int size) {
	    return new Friend[size];
	}
    };
}
