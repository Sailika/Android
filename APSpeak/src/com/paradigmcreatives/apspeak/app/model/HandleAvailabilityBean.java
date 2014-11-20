package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HandleAvailabilityBean implements Parcelable {
    private boolean availability = false;

    /**
     * @param availability
     */
    public HandleAvailabilityBean(boolean availability) {
	this.setAvailability(availability);
    }

    public HandleAvailabilityBean(Parcel in) {
	availability = (in.readByte() == 1) ? true : false;
    }

    /**
     * @return the availability
     */
    public boolean isAvailable() {
	return availability;
    }

    /**
     * @param availability
     *            the availability to set
     */
    public void setAvailability(boolean availability) {
	this.availability = availability;
    }

    @Override
    public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeByte((byte) (availability ? 1 : 0));
    }

    public static final Parcelable.Creator<HandleAvailabilityBean> CREATOR = new Parcelable.Creator<HandleAvailabilityBean>() {
	public HandleAvailabilityBean createFromParcel(Parcel in) {
	    return new HandleAvailabilityBean(in);
	}

	public HandleAvailabilityBean[] newArray(int size) {
	    return new HandleAvailabilityBean[size];
	}
    };

}
