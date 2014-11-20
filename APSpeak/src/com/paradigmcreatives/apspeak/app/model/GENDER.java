package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * Enum represents user's gender
 * 
 * @author Dileep | neuv
 * 
 */
public enum GENDER implements Parcelable {
	MALE, FEMALE, OTHERS;

	public int describeContents() {
		return 0;
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(ordinal());
	}

	public static final Creator<GENDER> CREATOR = new Creator<GENDER>() {

		@Override
		public GENDER createFromParcel(Parcel in) {
			return GENDER.values()[in.readInt()];
		}

		public GENDER[] newArray(int size) {
			return new GENDER[size];
		};
	};
};

