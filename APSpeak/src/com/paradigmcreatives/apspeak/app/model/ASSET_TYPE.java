package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * Enum represents stream asset type
 * 
 * @author Dileep | neuv
 * 
 */
public enum ASSET_TYPE implements Parcelable {
	DOODLE, GREETING, EMOJI, RAGE_FACE, IMAGE;

	public int describeContents() {
		return 0;
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(ordinal());
	}

	public static final Creator<ASSET_TYPE> CREATOR = new Creator<ASSET_TYPE>() {

		@Override
		public ASSET_TYPE createFromParcel(Parcel in) {
			return ASSET_TYPE.values()[in.readInt()];
		}

		public ASSET_TYPE[] newArray(int size) {
			return new ASSET_TYPE[size];
		};
	};
};
