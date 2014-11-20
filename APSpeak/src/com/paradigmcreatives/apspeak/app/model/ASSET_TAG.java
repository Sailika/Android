package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * Enum represents stream asset tag
 * 
 * @author Dileep | neuv
 * 
 */
public enum ASSET_TAG implements Parcelable {
	SPORTS, CRICKET, FOOD, MOVIES, ENTERTAINMENT;

	public int describeContents() {
		return 0;
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(ordinal());
	}

	public static final Creator<ASSET_TAG> CREATOR = new Creator<ASSET_TAG>() {

		@Override
		public ASSET_TAG createFromParcel(Parcel in) {
			return ASSET_TAG.values()[in.readInt()];
		}

		public ASSET_TAG[] newArray(int size) {
			return new ASSET_TAG[size];
		};
	};
};
