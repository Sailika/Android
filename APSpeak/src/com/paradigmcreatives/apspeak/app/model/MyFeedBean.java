package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyFeedBean implements Parcelable {

	private String mUserId;
	private String mName;
	private String mProfilePicture;
	private String mType;
	private String mMessage;
	private String mTs;
	private String mId;
	private String mIconUrl;
	private String mSnapUrl;
	private String mAssetId;
	private String mValue;
	private String mMyFeedAsJSON;

	// Default constructor
	public MyFeedBean() {
		super();
	}

	public String getUserId() {
		return mUserId;
	}

	public void setUserId(String userId) {
		this.mUserId = userId;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getProfilePicture() {
		return mProfilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.mProfilePicture = profilePicture;
	}

	public String getType() {
		return mType;
	}

	public void setType(String type) {
		this.mType = type;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		this.mMessage = message;
	}

	public String getTs() {
		return mTs;
	}

	public void setTs(String ts) {
		this.mTs = ts;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getMyFeedAsJSON() {
		return mMyFeedAsJSON;
	}

	public void setMyFeedAsJSON(String myFeedAsJSON) {
		this.mMyFeedAsJSON = myFeedAsJSON;
	}

	public String getValue() {
		return mValue;
	}

	public void setValue(String value) {
		this.mValue = value;
	}

	public String getAssetId() {
		return mAssetId;
	}

	public void setAssetId(String assetId) {
		this.mAssetId = assetId;
	}

	public String getSnapUrl() {
		return mSnapUrl;
	}

	public void setSnapUrl(String snapUrl) {
		this.mSnapUrl = snapUrl;
	}

	public String getIconUrl() {
		return mIconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.mIconUrl = iconUrl;
	}

	public MyFeedBean(Parcel in) {
		super();
		readFromParcel(in);
	}

	@Override
	public int describeContents() {

		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(mUserId);
		out.writeString(mName);
		out.writeString(mProfilePicture);
		out.writeString(mType);
		out.writeString(mMessage);
		out.writeString(mTs);
		out.writeString(mId);
		out.writeString(mIconUrl);
		out.writeString(mSnapUrl);
		out.writeString(mAssetId);
		out.writeString(mValue);
		out.writeString(mMyFeedAsJSON);

	}

	/**
	 * Reads from parcel and initializes member variables
	 * 
	 * @param in
	 */
	private void readFromParcel(Parcel in) {
		mUserId = in.readString();
		mName = in.readString();
		mProfilePicture = in.readString();
		mType = in.readString();
		mMessage = in.readString();
		mTs = in.readString();
		mId = in.readString();
		mIconUrl = in.readString();
		mSnapUrl = in.readString();
		mAssetId = in.readString();
		mValue = in.readString();
		mMyFeedAsJSON = in.readString();

	}

	public static final Parcelable.Creator<MyFeedBean> CREATOR = new Parcelable.Creator<MyFeedBean>() {

		public MyFeedBean createFromParcel(Parcel in) {
			return new MyFeedBean(in);
		};

		public MyFeedBean[] newArray(int size) {
			return new MyFeedBean[size];
		};
	};

	@Override
	public String toString() {

		return "";
	}

}
