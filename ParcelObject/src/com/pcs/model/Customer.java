package com.pcs.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Customer implements Parcelable{

	private String Name;
	private String Phone;
	private String Lnet;
	private String Pnet;

	
	public Customer()
	{
	}
	
	
	public Customer(Parcel source)
	{
		setName(source.readString());
		setPhone(source.readString());
		setLnet(source.readString());
		setPnet(source.readString());
	}
	
	
	
	
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getLnet() {
		return Lnet;
	}
	public void setLnet(String lnet) {
		Lnet = lnet;
	}
	public String getPnet() {
		return Pnet;
	}
	public void setPnet(String pnet) {
		Pnet = pnet;
	}


	@Override
	public int describeContents() {

		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(getName());
		dest.writeString(getPhone());
		dest.writeString(getLnet());
		dest.writeString(getPnet());
	}
	
	public static final Creator<Customer> CREATOR = new Creator<Customer>() {

		@Override
		public Customer createFromParcel(Parcel source) {
			
			return new Customer(source);
		}

		@Override
		public Customer[] newArray(int size) {
			
			return new Customer[size];
		}
		
	};
	
	public String toString() {
		return getName()+"\n\n"+getPhone()+"\n\n"+getLnet()+"\n\n"+getPnet()+"\n\nThank You , \n\n Your Number Has Been Ported";
	};
}
