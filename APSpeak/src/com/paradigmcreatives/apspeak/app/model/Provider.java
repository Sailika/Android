package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * The provider bean
 * 
 * @author robin
 * 
 */
public class Provider implements Parcelable {

    public enum PROVIDER_TYPE {
	PHONE, EMAIL
    }

    private PROVIDER_TYPE type;
    private String providerValue1;
    private String providerValue2;
    private String providerValue3;

    public Provider() {
	super();
    }

    /**
     * Copies the provider
     * 
     * @param provider
     */
    public Provider(Provider provider) {
	super();
	this.type = provider.type;
	this.providerValue1 = provider.providerValue1;
	this.providerValue2 = provider.providerValue2;
	this.providerValue3 = provider.providerValue3;
    }

    /**
     * @param type
     * @param providerValue1
     * @param providerValue2
     * @param providerValue3
     */
    public Provider(PROVIDER_TYPE type, String providerValue1, String providerValue2, String providerValue3) {
	super();
	this.type = type;
	this.providerValue1 = providerValue1;
	this.providerValue2 = providerValue2;
	this.providerValue3 = providerValue3;
    }

    /**
     * @param type
     * @param providerValue1
     */
    public Provider(PROVIDER_TYPE type, String providerValue1) {
	super();
	this.type = type;
	this.providerValue1 = providerValue1;
    }

    /**
     * @return the type
     */
    public PROVIDER_TYPE getType() {
	return type;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(PROVIDER_TYPE type) {
	this.type = type;
    }

    /**
     * @return the first provider value
     */
    public String getProviderValue1() {
	return providerValue1;
    }

    /**
     * @param providerValue1
     *            the first provider value to be set
     */
    public void setProviderValue1(String providerValue1) {
	this.providerValue1 = providerValue1;
    }

    /**
     * @return the second provider value
     */
    public String getProviderValue2() {
	return providerValue2;
    }

    /**
     * @param providerValue2
     *            the second provider value to be set
     */
    public void setProviderValue2(String providerValue2) {
	this.providerValue2 = providerValue2;
    }

    /**
     * @return the third provider value
     */
    public String getProviderValue3() {
	return providerValue3;
    }

    /**
     * @param providerValue3
     *            the third provider value to be set
     */
    public void setProviderValue3(String providerValue3) {
	this.providerValue3 = providerValue3;
    }

    /**
     * Gets the provider string
     * 
     * @return
     */
    public String getProviderString() {
	if (!TextUtils.isEmpty(providerValue2)) {
	    return "+" + providerValue2 + providerValue1;
	} else {
	    return providerValue1;
	}
    }

    /**
     * Overrides default equal method.
     */
    public boolean equals(Object object) {
	boolean result = false;
	if (this != null && object != null && object instanceof Provider) {
	    Provider obj = (Provider) object;

	    result = (obj.type.equals(this.type));
	    result = result && TextUtils.equals(obj.providerValue1, this.providerValue1);
	    result = result && TextUtils.equals(obj.providerValue2, this.providerValue2);
	    result = result && TextUtils.equals(obj.providerValue3, this.providerValue3);
	}
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	return (providerValue1 == null) ? 0 : providerValue1.length() + type.ordinal();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "Provider [type=" + type + ", providerValue1=" + providerValue1 + ", providerValue2=" + providerValue2
		+ ", providerValue3=" + providerValue3 + "]";
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	out.writeString(type.name());
	out.writeString(providerValue1);
	out.writeString(providerValue2);
	out.writeString(providerValue3);
    }

    public Provider(Parcel in) {
	readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
	String typeString = in.readString();
	type = (TextUtils.isEmpty(typeString)) ? null : PROVIDER_TYPE.valueOf(typeString);
	providerValue1 = in.readString();
	providerValue2 = in.readString();
	providerValue3 = in.readString();
    }

    public static final Parcelable.Creator<Provider> CREATOR = new Parcelable.Creator<Provider>() {

	public Provider createFromParcel(Parcel in) {
	    return new Provider(in);
	}

	public Provider[] newArray(int size) {
	    return new Provider[size];
	}

    };

}
