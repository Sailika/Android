package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * Enum represents stream asset's association type
 * 
 * @author Dileep | neuv
 * 
 */
public enum ASSOCIATION_TYPE implements Parcelable {
	LOVE, HATE, TAGGED_BY;

	public int describeContents() {
		return 0;
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(ordinal());
	}

	public static final Creator<ASSOCIATION_TYPE> CREATOR = new Creator<ASSOCIATION_TYPE>() {

		@Override
		public ASSOCIATION_TYPE createFromParcel(Parcel in) {
			return ASSOCIATION_TYPE.values()[in.readInt()];
		}

		public ASSOCIATION_TYPE[] newArray(int size) {
			return new ASSOCIATION_TYPE[size];
		};
	};
};
