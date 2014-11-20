package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * Enum represents Expressions Submission Status
 * 
 * @author Dileep | neuv
 * 
 */
public enum SUBMISSION_STATUS implements Parcelable {
	PENDING, FAILED;

	public int describeContents() {
		return 0;
	};

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(ordinal());
	}

	public static final Creator<SUBMISSION_STATUS> CREATOR = new Creator<SUBMISSION_STATUS>() {

		@Override
		public SUBMISSION_STATUS createFromParcel(Parcel in) {
			return SUBMISSION_STATUS.values()[in.readInt()];
		}

		public SUBMISSION_STATUS[] newArray(int size) {
			return new SUBMISSION_STATUS[size];
		};
	};
};
