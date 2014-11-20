package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean that represents Whatsay CUE
 * 
 * @author Dileep | neuv
 * 
 */
public class Campaigns implements Parcelable {

    private String mCueId;
    private String mCueMessage;
    private String mBackgroundColor;
    private String mForegroundColor;
    private String mBackgroundURL;
    private String mBackgroundURLWide;
    private String mIconURL;
    private int mWidth;

    public Campaigns() {
	super();
    }

    public void setCueId(String cueId) {
	this.mCueId = cueId;
    }

    public String getCueId() {
	return this.mCueId;
    }

    public void setCueMessage(String cueMessage) {
	this.mCueMessage = cueMessage;
    }

    public String getCueMessage() {
	return this.mCueMessage;
    }

    public void setBackgroundColor(String backgroundColor) {
	this.mBackgroundColor = backgroundColor;
    }

    public String getBackgroundColor() {
	return this.mBackgroundColor;
    }

    public void setForegroundColor(String foregroundColor) {
	this.mForegroundColor = foregroundColor;
    }

    public String getForegroundColor() {
	return this.mForegroundColor;
    }

    public void setBackgroundURL(String backgroundURL) {
	this.mBackgroundURL = backgroundURL;
    }

    public String getBackgroundURL() {
	return this.mBackgroundURL;
    }

    public void setBackgroundURLWide(String backgroundURLWide) {
	this.mBackgroundURLWide = backgroundURLWide;
    }

    public String getBackgroundURLWide() {
	return this.mBackgroundURLWide;
    }

    public void setIconURL(String iconURL) {
	this.mIconURL = iconURL;
    }

    public String getIconURL() {
	return this.mIconURL;
    }

    public void setWidth(int width) {
	this.mWidth = width;
    }

    public int getWidth() {
	return this.mWidth;
    }

    public Campaigns(Parcel in) {
	super();
	readFromParcel(in);
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	out.writeString(mCueId);
	out.writeString(mCueMessage);
	out.writeString(mBackgroundColor);
	out.writeString(mForegroundColor);
	out.writeString(mBackgroundURL);
	out.writeString(mBackgroundURLWide);
	out.writeString(mIconURL);
	out.writeInt(mWidth);
    }

    private void readFromParcel(Parcel in) {
	mCueId = in.readString();
	mCueMessage = in.readString();
	mBackgroundColor = in.readString();
	mForegroundColor = in.readString();
	mBackgroundURL = in.readString();
	mBackgroundURLWide = in.readString();
	mIconURL = in.readString();
	mWidth = in.readInt();
    }

    public static final Parcelable.Creator<Campaigns> CREATOR = new Parcelable.Creator<Campaigns>() {

	public Campaigns createFromParcel(Parcel in) {
	    return new Campaigns(in);
	};

	public Campaigns[] newArray(int size) {
	    return new Campaigns[size];
	};
    };

    @Override
    public String toString() {
	return "CueBean [CueId=" + mCueId + ", CueMessage=" + mCueMessage + ", BackgroundColor=" + mBackgroundColor
		+ ", ForegroundColor=" + mForegroundColor + ", BackgroundURL=" + mBackgroundURL + ", BackgroundURLWide="
		+ mBackgroundURLWide + ", IconURl=" + mIconURL + ", Width=" + mWidth + "]";
    }
}
