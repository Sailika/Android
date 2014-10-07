package com.pcs.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable{
	
	String name,id,desg;
	Context context;
	
	public Employee(Parcel source){
    setName(source.readString());
    setId(source.readString());
    setDesg(source.readString());
	
	}

	public Employee() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDesg() {
		return desg;
	}

	public void setDesg(String desg) {
		this.desg = desg;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
     dest.writeString(getName());
     dest.writeString(getId());
     dest.writeString(getDesg());
	}
	public static final Creator<Employee> CREATOR = new Creator<Employee>() {

		@Override
		public Employee createFromParcel(Parcel source) {
			
			return new Employee(source);
		}

		@Override
		public Employee[] newArray(int size) {
			
			return new Employee[size];
		}
		
	};
	public String toString() {
		return getName()+"\n\n"+getId()+"\n\n"+getDesg()+" \n\n Your Data Has Been Parceled";
	};

}
