package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean that represents Whatsay Group
 * 
 * @author Dileep | neuv
 * 
 */
public class GroupBean implements Parcelable {

    private String mGroupId;
    private String mGroupName;
    private String mGroupIconURL;
    private String mCreatedTimestamp;

    public GroupBean() {
	super();
    }

    public void setGroupId(String groupId) {
	this.mGroupId = groupId;
    }

    public String getGroupId() {
	return this.mGroupId;
    }

    public void setGroupName(String groupName) {
	this.mGroupName = groupName;
    }

    public String getGroupName() {
	return this.mGroupName;
    }

    public void setGroupIconURL(String groupIconURL) {
	this.mGroupIconURL = groupIconURL;
    }

    public String getGroupIconURL() {
	return this.mGroupIconURL;
    }

    public void setGroupCreatedTimestamp(String createdTimestamp) {
	this.mCreatedTimestamp = createdTimestamp;
    }

    public String getGroupCreatedTimestamp() {
	return this.mCreatedTimestamp;
    }

    public GroupBean(Parcel in) {
	super();
	readFromParcel(in);
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int arg1) {
	out.writeString(mGroupId);
	out.writeString(mGroupName);
	out.writeString(mGroupIconURL);
	out.writeString(mCreatedTimestamp);
    }

    private void readFromParcel(Parcel in) {
	mGroupId = in.readString();
	mGroupName = in.readString();
	mGroupIconURL = in.readString();
	mCreatedTimestamp = in.readString();
    }

    public static final Parcelable.Creator<GroupBean> CREATOR = new Parcelable.Creator<GroupBean>() {

	public GroupBean createFromParcel(Parcel in) {
	    return new GroupBean(in);
	};

	public GroupBean[] newArray(int size) {
	    return new GroupBean[size];
	};
    };

    @Override
    public String toString() {
	return "GroupBean [GroupId=" + mGroupId + ", GroupName=" + mGroupName + ", GroupIconURL=" + mGroupIconURL
		+ ", GroupCreatedTimestamp=" + mCreatedTimestamp + "]";
    }
}
