package com.pcs.model;

import android.os.Parcel;
import android.os.Parcelable;


public class AndroidVersions implements Parcelable{
	private String firstversion;
	private String secondversion;
	private String thirdversion;
	private String fourthversion;
	private String fifthversion;
	private String sixthversion;
	private String seventhversion;
	private String eigthversion;
	private String ninthversion;
	private String tenthversion;
	private String eleventhversion;
	

	public AndroidVersions(Parcel source) {
		setFirstversion(source.readString());
		setSecondversion(source.readString());
		setThirdversion(source.readString());
		setFourthversion(source.readString());
		setFifthversion(source.readString());
		setSixthversion(source.readString());
		setSeventhversion(source.readString());
		setEigthversion(source.readString());
		setNinthversion(source.readString());
		setTenthversion(source.readString());
		setEleventhversion(source.readString());
	}

	public AndroidVersions() {
		// TODO Auto-generated constructor stub
	}

	public String getFirstversion() {
		return firstversion;
	}

	public void setFirstversion(String firstversion) {
		this.firstversion = firstversion;
	}

	public String getSecondversion() {
		return secondversion;
	}

	public void setSecondversion(String secondversion) {
		this.secondversion = secondversion;
	}

	public String getThirdversion() {
		return thirdversion;
	}

	public void setThirdversion(String thirdversion) {
		this.thirdversion = thirdversion;
	}

	public String getFourthversion() {
		return fourthversion;
	}

	public void setFourthversion(String fourthversion) {
		this.fourthversion = fourthversion;
	}

	public String getFifthversion() {
		return fifthversion;
	}

	public void setFifthversion(String fifthversion) {
		this.fifthversion = fifthversion;
	}

	public String getSixthversion() {
		return sixthversion;
	}

	public void setSixthversion(String sixthversion) {
		this.sixthversion = sixthversion;
	}

	public String getSeventhversion() {
		return seventhversion;
	}

	public void setSeventhversion(String seventhversion) {
		this.seventhversion = seventhversion;
	}

	public String getEigthversion() {
		return eigthversion;
	}

	public void setEigthversion(String eigthversion) {
		this.eigthversion = eigthversion;
	}

	public String getNinthversion() {
		return ninthversion;
	}

	public void setNinthversion(String ninthversion) {
		this.ninthversion = ninthversion;
	}

	public String getTenthversion() {
		return tenthversion;
	}

	public void setTenthversion(String tenthversion) {
		this.tenthversion = tenthversion;
	}

	public String getEleventhversion() {
		return eleventhversion;
	}

	public void setEleventhversion(String eleventhversion) {
		this.eleventhversion = eleventhversion;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getFirstversion());
		dest.writeString(getSecondversion());
		dest.writeString(getThirdversion());
		dest.writeString(getFourthversion());
		dest.writeString(getFifthversion());
		dest.writeString(getSixthversion());
		dest.writeString(getSeventhversion());
		dest.writeString(getEigthversion());
		dest.writeString(getNinthversion());
		dest.writeString(getTenthversion());
		dest.writeString(getEleventhversion());
		
	}
	public static final Creator<AndroidVersions> CREATOR = new Creator<AndroidVersions>() {

		@Override
		public AndroidVersions createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new AndroidVersions(source);
		}

		@Override
		public AndroidVersions[] newArray(int size) {
			// TODO Auto-generated method stub
			return new AndroidVersions[size];
		}
	};
	public String toString(){
		return getFirstversion()+"\n"+ getSecondversion()+"\n"+getThirdversion()+"\n"+getFourthversion()+"\n"+getFifthversion()+"\n"+getSixthversion()+"\n"+getSeventhversion()+"\n"+getEigthversion()+"\n"+getNinthversion()+"\n"+getTenthversion()+"\n"+getEleventhversion();
	}
	}
