package com.paradigmcreatives.apspeak.app.model;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * User, represents doodly doo user with details such as handle, userId, name,
 * profilePicURL, profilePicBitmap, etc
 * 
 * @author Dileep | neuv
 * 
 */
public class User implements Parcelable {

	protected String uniqueHandle;
	protected String userId;
	protected ArrayList<String> userGroupNames;
	protected String name;
	protected GENDER gender;
	protected String location;
	protected String profilePicURL;
	protected Bitmap profilePicBitmap;
	protected String coverImageURL;
	protected Bitmap coverImageBitmap;
	protected int posts = 0;
	protected int reposts = 0;
	protected int tagsCreated = 0;
	protected int tagsUsed = 0;
	protected int following = 0;
	protected int followers = 0;
	protected List<Friend> friends = null;

	/**
	 * Default constructor
	 */
	public User() {
		super();
		friends = new ArrayList<Friend>();
	}

	public void setUniqueHandle(String uniqueHandle) {
		this.uniqueHandle = uniqueHandle;
	}

	public String getUniqueHandle() {
		return this.uniqueHandle;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return this.userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setGender(GENDER gender) {
		this.gender = gender;
	}

	public GENDER getGender() {
		return this.gender;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return this.location;
	}

	public void setProfilePicURL(String profilePicURL) {
		this.profilePicURL = profilePicURL;
	}

	public String getProfilePicURL() {
		return this.profilePicURL;
	}

	public void setProfilePicBitmap(Bitmap bmp) {
		this.profilePicBitmap = bmp;
	}

	public Bitmap getProfilePicBitmap() {
		return this.profilePicBitmap;
	}

	public void setCoverImageURL(String coverImageURL) {
		this.coverImageURL = coverImageURL;
	}

	public String getCoverImageURL() {
		return this.coverImageURL;
	}

	public void setCoverImageBitmap(Bitmap bmp) {
		this.coverImageBitmap = bmp;
	}

	public Bitmap getCoverImageBitmap() {
		return this.coverImageBitmap;
	}

	public User(Parcel in) {
		super();
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(uniqueHandle);
		out.writeString(userId);
		out.writeString(name);
		out.writeSerializable(userGroupNames);
		out.writeParcelable(gender, flags);
		out.writeString(location);
		out.writeString(profilePicURL);
		out.writeParcelable(profilePicBitmap, flags);
		out.writeString(coverImageURL);
		out.writeParcelable(coverImageBitmap, flags);
		out.writeInt(posts);
		out.writeInt(reposts);
		out.writeInt(tagsCreated);
		out.writeInt(tagsUsed);
		out.writeInt(following);
		out.writeInt(followers);
		out.writeTypedList(friends);
	}

	private void readFromParcel(Parcel in) {
		uniqueHandle = in.readString();
		userId = in.readString();
		name = in.readString();
		userGroupNames = (ArrayList<String>)in.readSerializable();
		gender = (GENDER) in.readParcelable(getClass().getClassLoader());
		location = in.readString();
		profilePicURL = in.readString();
		profilePicBitmap = (Bitmap) in.readParcelable(getClass()
				.getClassLoader());
		coverImageURL = in.readString();
		coverImageBitmap = (Bitmap) in.readParcelable(getClass()
				.getClassLoader());
		posts = in.readInt();
		reposts = in.readInt();
		tagsCreated = in.readInt();
		tagsUsed = in.readInt();
		following = in.readInt();
		followers = in.readInt();

		// Using default class loader
		in.readList(friends, null);
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

		public User createFromParcel(Parcel in) {
			return new User(in);
		};

		public User[] newArray(int size) {
			return new User[size];
		};
	};

	@Override
	public String toString() {
		return "User [Handle=" + uniqueHandle + ", UserId=" + userId
				+ ", Name=" + name + ", Location=" + location
				+ ", ProfilePicURL=" + profilePicURL + ", CoverImageURL="
				+ coverImageURL + ", Posts=" + posts + ", Reposts=" + reposts
				+ ", Tags Created=" + tagsCreated + ", Tags Used=" + tagsUsed
				+ ", Followers=" + followers + ", Following=" + following
				+ ", Friends=" + friends.toArray() + "]";
	}

	public int getPosts() {
		return posts;
	}

	public void setPosts(int posts) {
		this.posts = posts;
	}

	public int getReposts() {
		return reposts;
	}

	public void setReposts(int reposts) {
		this.reposts = reposts;
	}

	public int getTagsCreated() {
		return tagsCreated;
	}

	public void setTagsCreated(int tagsCreated) {
		this.tagsCreated = tagsCreated;
	}

	public int getTagsUsed() {
		return tagsUsed;
	}

	public void setTagsUsed(int tagsUsed) {
		this.tagsUsed = tagsUsed;
	}

	public int getFollowing() {
		return following;
	}

	public void setFollowing(int following) {
		this.following = following;
	}

	public int getFollowers() {
		return followers;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

	public void addFriends(List<Friend> friends) {
		this.friends.addAll(friends);
	}

	public ArrayList<String> getUserGroupNames() {
		return userGroupNames;
	}

	public void setUserGroupName(ArrayList<String> userGroupNames) {
		this.userGroupNames = userGroupNames;
	}
}
