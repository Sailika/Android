package com.paradigmcreatives.apspeak.app.util.server.assets.beans;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.logging.Logger;

public class AssetBean implements Parcelable {
	private static final String TAG = "AssetBean";
	private String id;
	private String url;
	private String thumbnailURL;
	private String title;
	private ArrayList<String> category;
	private int autoID;

	public ArrayList<String> getCategories() {
		return category;
	}

	public void setCategory(ArrayList<String> category) {
		this.category = category;
	}

	/**
     * 
     */
	public AssetBean() {
		super();
	}

	/**
	 * @param id
	 * @param url
	 * @param thumbnailURL
	 * @param title
	 */
	public AssetBean(String id, String url, String thumbnailURL, String title) {
		this.id = id;
		this.url = url;
		this.thumbnailURL = thumbnailURL;
		this.title = title;
		// this.category = category;
	}

	/**
	 * @param id
	 * @param url
	 * @param thumbnailURL
	 * @param title
	 */
	public AssetBean(String id, String url, String thumbnailURL, String title, int autoID) {
		this.id = id;
		this.url = url;
		this.thumbnailURL = thumbnailURL;
		this.title = title;
		this.autoID = autoID;
		// this.category = category;
	}

	/**
	 * Checks if the constraint string is a part of the asset
	 * 
	 * @param constraint
	 * @return
	 */
	public boolean matchString(String constraint) {
		boolean result = false;
		if (!TextUtils.isEmpty(title)) {
			if (!TextUtils.isEmpty(constraint)) {
				if (title.toLowerCase().contains(constraint.toLowerCase())) {
					result = true;
				}
			}
		}

		return result;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the thumbnailURL
	 */
	public String getThumbnailURL() {
		return thumbnailURL;
	}

	/**
	 * @param thumbnailURL the thumbnailURL to set
	 */
	public void setThumbnailURL(String thumbnailURL) {
		this.thumbnailURL = thumbnailURL;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the autoID
	 */
	public int getAutoID() {
		return autoID;
	}

	/**
	 * @param autoID the autoID to set
	 */
	public void setAutoID(int autoID) {
		this.autoID = autoID;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof AssetBean) {
			return ((AssetBean) o).id.equals(this.id);
		}
		return false;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(url);
		dest.writeString(thumbnailURL);
		dest.writeString(title);
		dest.writeStringList(category);
		dest.writeInt(autoID);
	}

	public AssetBean(Parcel in) {
		this.id = in.readString();
		this.url = in.readString();
		this.thumbnailURL = in.readString();
		this.title = in.readString();
		this.category = new ArrayList<String>();
		in.readStringList(this.category);
		this.autoID = in.readInt();
	}

	public static final Parcelable.Creator<AssetBean> CREATOR = new Parcelable.Creator<AssetBean>() {
		public AssetBean createFromParcel(Parcel in) {
			return new AssetBean(in);
		}

		public AssetBean[] newArray(int size) {
			return new AssetBean[size];
		}
	};

	public static AssetBean getAssetBeanFromData(Intent data) {
		if (data != null) {
			Bundle bundle = data.getExtras();
			if (bundle != null) {
				final AssetBean assetBean = bundle.getParcelable(Constants.ASSET_PARCELABLE);
				if (assetBean != null) {
					return assetBean;
				} else {
					Logger.warn(TAG, "Could not initiate the loading of large image. Asset Bean is NULL");
				}
			} else {
				Logger.warn(TAG, "Could not initiate the loading of large image. Bundle is NULL");
			}
		} else {
			Logger.warn(TAG, "Could not initiate the loading of large image. Intent passed in NULL");
		}

		return null;
	}
}