package com.pcs.model;

import java.io.Serializable;


public class Customer implements Serializable{
	private static final long serialVersionUID = 1L;
	private String Name;
	private String Phone;
	private String Lnet;
	private String Pnet;

	
	
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


	
	public String toString() {
		return getName()+"\n\n"+getPhone()+"\n\n"+getLnet()+"\n\n"+getPnet()+"\n\nThank You , \n\n Your Number Has Been Ported";
	};
}
