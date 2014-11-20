package com.paradigmcreatives.apspeak.app.model;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Asset that represents Stream object of the user
 * 
 * @author Dileep | neuv
 * 
 */
public class StreamAsset implements Parcelable {

    private String mAssetId;
    private ASSET_TAG[] mAssetTags;
    private String mAssetThumbnailURL;
    private String mAssetSnapURL;
    private Bitmap mAssetSnapBitmap;
    private String mAssetDownloadURL;
    private String mAssetShareURL;
    private String mAssetDescription;
    private User mAssetOwner;
    /*
     * Represented and stored as String. This will be converted to long only while displaying on UI
     */
    private String mCreatedTimestamp;
    private ASSET_TYPE mAssetType;
    private HashMap<ASSOCIATION_TYPE, Integer> mAssociations;
    private int mReposts;
    private int mComments;
    // private int mLikes;
    private boolean mIsLoved;
    private boolean mIsReposted;
    private boolean mIsCommented;
    private String mAssetAsJSON;

    // Default constructor
    public StreamAsset() {
	super();
    }

    public void setAssetId(String assetId) {
	this.mAssetId = assetId;
    }

    public String getAssetId() {
	return this.mAssetId;
    }

    public void setAssetTagsArray(ASSET_TAG[] tags) {
	this.mAssetTags = tags;
    }

    public ASSET_TAG[] getAssetTagsArray() {
	return this.mAssetTags;
    }

    public void setAssetSnapURL(String snapURL) {
	this.mAssetSnapURL = snapURL;
    }

    public String getAssetSnapURL() {
	return this.mAssetSnapURL;
    }

    public void setAssetThumbnailURL(String thumbnailURL) {
	this.mAssetThumbnailURL = thumbnailURL;
    }

    public String getAssetThumbnailURL() {
	return this.mAssetThumbnailURL;
    }

    public void setAssetSnapBitmap(Bitmap bmp) {
	this.mAssetSnapBitmap = bmp;
    }

    public Bitmap getAssetSnapBitmap() {
	return this.mAssetSnapBitmap;
    }

    public void setAssetDownloadURL(String downloadURL) {
	this.mAssetDownloadURL = downloadURL;
    }

    public String getAssetDownloadURL() {
	return this.mAssetDownloadURL;
    }

    public void setAssetShareURL(String shareURL) {
	this.mAssetShareURL = shareURL;
    }

    public String getAssetShareURL() {
	return this.mAssetShareURL;
    }

    public void setAssetDescription(String description) {
	this.mAssetDescription = description;
    }

    public String getAssetDescription() {
	return this.mAssetDescription;
    }

    public void setAssetOwner(User owner) {
	this.mAssetOwner = owner;
    }

    public User getAssetOwner() {
	return this.mAssetOwner;
    }

    public void setAssetCreatedTimestamp(String timestamp) {
	this.mCreatedTimestamp = timestamp;
    }

    public String getAssetCreatedTimestamp() {
	return this.mCreatedTimestamp;
    }

    public void setAssetType(ASSET_TYPE type) {
	this.mAssetType = type;
    }

    public ASSET_TYPE getAssetType() {
	return this.mAssetType;
    }

    public void setAssetAssociations(HashMap<ASSOCIATION_TYPE, Integer> associations) {
	this.mAssociations = associations;
    }

    public HashMap<ASSOCIATION_TYPE, Integer> getAssetAssociations() {
	return this.mAssociations;
    }

    public void setAssetRepostsCount(int repostsCount) {
	this.mReposts = repostsCount;
    }

    public int getAssetRepostsCount() {
	return (this.mReposts > 0) ? this.mReposts : 0;
    }

    public void setAssetCommentsCount(int commentsCount) {
	this.mComments = commentsCount;
    }

    public int getAssetCommentsCount() {
	return (this.mComments > 0) ? this.mComments : 0;
    }

    /*
     * public void setAssetLikesCount(int likesCount) { this.mLikes = likesCount; }
     * 
     * public int getAssetLikesCount() { return (this.mLikes > 0) ? this.mLikes : 0; }
     */

    public void setAssetIsLoved(boolean isLoved) {
	this.mIsLoved = isLoved;
    }

    public boolean getAssetIsLoved() {
	return this.mIsLoved;
    }

    public void setAssetIsReposted(boolean isReposted) {
	this.mIsReposted = isReposted;
    }

    public boolean getAssetIsReposted() {
	return this.mIsReposted;
    }

    public void setAssetIsCommented(boolean isCommented) {
	this.mIsCommented = isCommented;
    }

    public boolean getAssetIsCommented() {
	return this.mIsCommented;
    }

    public void setAssetAsJSON(String assetAsJSON) {
	this.mAssetAsJSON = assetAsJSON;
    }

    public String getAssetAsJSON() {
	return this.mAssetAsJSON;
    }

    public StreamAsset(Parcel in) {
	super();
	readFromParcel(in);
    }

    @Override
    public int describeContents() {
	return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
	out.writeString(mAssetId);
	out.writeParcelableArray(mAssetTags, flags);
	out.writeString(mAssetThumbnailURL);
	out.writeString(mAssetSnapURL);
	out.writeParcelable(mAssetSnapBitmap, flags);
	out.writeString(mAssetDownloadURL);
	out.writeString(mAssetShareURL);
	out.writeString(mAssetDescription);
	out.writeParcelable(mAssetOwner, flags);
	out.writeString(mCreatedTimestamp);
	out.writeParcelable(mAssetType, flags);
	out.writeSerializable(mAssociations);
	out.writeInt(mReposts);
	out.writeInt(mComments);
	// out.writeInt(mLikes);
	out.writeInt((mIsLoved) ? 1 : 0);
	out.writeInt((mIsReposted) ? 1 : 0);
	out.writeInt((mIsCommented) ? 1 : 0);
	out.writeString(mAssetAsJSON);
    }

    /**
     * Reads from parcel and initializes member variables
     * 
     * @param in
     */
    private void readFromParcel(Parcel in) {
	mAssetId = in.readString();
	mAssetTags = (ASSET_TAG[]) in.readParcelableArray(getClass().getClassLoader());
	mAssetThumbnailURL = in.readString();
	mAssetSnapURL = in.readString();
	mAssetSnapBitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
	mAssetDownloadURL = in.readString();
	mAssetShareURL = in.readString();
	mAssetDescription = in.readString();
	mAssetOwner = (User) in.readParcelable(getClass().getClassLoader());
	mCreatedTimestamp = in.readString();
	mAssetType = (ASSET_TYPE) in.readParcelable(getClass().getClassLoader());
	mAssociations = (HashMap<ASSOCIATION_TYPE, Integer>) in.readSerializable();
	mReposts = in.readInt();
	mComments = in.readInt();
	// mLikes = in.readInt();
	mIsLoved = (in.readInt() == 1) ? true : false;
	mIsReposted = (in.readInt() == 1) ? true : false;
	mIsCommented = (in.readInt() == 1) ? true : false;
	mAssetAsJSON = in.readString();
    }

    public static final Parcelable.Creator<StreamAsset> CREATOR = new Parcelable.Creator<StreamAsset>() {

	public StreamAsset createFromParcel(Parcel in) {
	    return new StreamAsset(in);
	};

	public StreamAsset[] newArray(int size) {
	    return new StreamAsset[size];
	};
    };

    @Override
    public String toString() {
	String assetOwner = (mAssetOwner != null) ? mAssetOwner.toString() : "";
	int likesCount = (mAssociations != null && mAssociations.containsKey(ASSOCIATION_TYPE.LOVE)) ? mAssociations
		.get(ASSOCIATION_TYPE.LOVE) : 0;
	return "StreamAsset [AssetId=" + mAssetId + ", AssetOwner=" + assetOwner + ", AssetThumbnailURL="
		+ mAssetThumbnailURL + ", AssetSnapURL=" + mAssetSnapURL + ", AssetDownloadURL=" + mAssetDownloadURL
		+ ", AssetShareURL=" + mAssetShareURL + ", AssetDescription=" + mAssetDescription + ", AssetType="
		+ mAssetType.name() + ", Asset Reposts=" + mReposts + ", Asset Comments=" + mComments
		+ ", Asset Likes=" + likesCount + ", Asset IsLoved=" + mIsLoved + ", Asset IsReposted=" + mIsReposted
		+ ", Asset IsCommented=" + mIsCommented + "]";
    }
}
