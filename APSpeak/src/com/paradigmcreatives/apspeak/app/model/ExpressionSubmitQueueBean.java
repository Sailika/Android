package com.paradigmcreatives.apspeak.app.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean that contains details of an Expression, which is used while
 * adding/retrieving Expressions Submitting Queue
 * 
 * @author Dileep | neuv
 * 
 */
public class ExpressionSubmitQueueBean implements Parcelable {

	private int mId;
	private String mGroupId;
	private String mUserId;
	private String mCueId;
	private String mRootAssetId; // Contains value only if the submitting
									// expression is a Comment
	private String mDescription;
	private String mType;
	private String mFilePath;
	private SUBMISSION_STATUS mSubmissionStatus;

	// Default constructor
	public ExpressionSubmitQueueBean() {
		super();
	}

	public void setID(int id) {
		this.mId = id;
	}

	public int getID() {
		return this.mId;
	}

	public void setGroupId(String groupId) {
		this.mGroupId = groupId;
	}

	public String getGroupId() {
		return this.mGroupId;
	}

	public void setUserId(String userId) {
		this.mUserId = userId;
	}

	public String getUserId() {
		return this.mUserId;
	}

	public void setCueId(String cueId) {
		this.mCueId = cueId;
	}

	public String getCueId() {
		return this.mCueId;
	}

	public void setRootAssetId(String rootAssetId) {
		this.mRootAssetId = rootAssetId;
	}

	public String getRootAssetId() {
		return this.mRootAssetId;
	}

	public void setDescription(String description) {
		this.mDescription = description;
	}

	public String getDescription() {
		return this.mDescription;
	}

	public void setType(String type) {
		this.mType = type;
	}

	public String getType() {
		return this.mType;
	}

	public void setFilePath(String filePath) {
		this.mFilePath = filePath;
	}

	public String getFilePath() {
		return this.mFilePath;
	}

	public void setSubmissionStatus(SUBMISSION_STATUS status) {
		this.mSubmissionStatus = status;
	}

	public SUBMISSION_STATUS getSubmissionStatus() {
		return this.mSubmissionStatus;
	}

	public ExpressionSubmitQueueBean(Parcel in) {
		super();
		readFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(mId);
		out.writeString(mGroupId);
		out.writeString(mUserId);
		out.writeString(mCueId);
		out.writeString(mRootAssetId);
		out.writeString(mDescription);
		out.writeString(mType);
		out.writeString(mFilePath);
		out.writeParcelable(mSubmissionStatus, flags);
	}

	/**
	 * Reads from parcel and initializes member variables
	 * 
	 * @param in
	 */
	private void readFromParcel(Parcel in) {
		mId = in.readInt();
		mGroupId = in.readString();
		mUserId = in.readString();
		mCueId = in.readString();
		mRootAssetId = in.readString();
		mDescription = in.readString();
		mType = in.readString();
		mFilePath = in.readString();
		mSubmissionStatus = (SUBMISSION_STATUS) in.readParcelable(getClass()
				.getClassLoader());
	}

	public static final Parcelable.Creator<ExpressionSubmitQueueBean> CREATOR = new Parcelable.Creator<ExpressionSubmitQueueBean>() {

		public ExpressionSubmitQueueBean createFromParcel(Parcel in) {
			return new ExpressionSubmitQueueBean(in);
		};

		public ExpressionSubmitQueueBean[] newArray(int size) {
			return new ExpressionSubmitQueueBean[size];
		};
	};

	@Override
	public String toString() {
		return "ExpressionSubmitQueueBean [Id=" + mId + ", GroupId=" + mGroupId
				+ ", UserId=" + mUserId + ", CueId=" + mCueId
				+ ", RootAssetId=" + mRootAssetId + ", Descripiton="
				+ mDescription + ", Type=" + mType + ", FilePath=" + mFilePath
				+ ", SubmissionStatus="
				+ ((mSubmissionStatus != null) ? mSubmissionStatus.name() : "")
				+ "]";
	}
}
