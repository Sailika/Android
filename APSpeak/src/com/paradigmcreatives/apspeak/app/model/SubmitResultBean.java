package com.paradigmcreatives.apspeak.app.model;

public class SubmitResultBean {

	private String assetID = null;
	private String error = null;
	private int errorStatusCode = -1;
	private boolean success = false;

	/**
	 * @param assetID
	 * @param error
	 * @param success
	 */
	public SubmitResultBean(String assetID, String error, int code,
			boolean success) {
		this.assetID = assetID;
		this.error = error;
		this.errorStatusCode = code;
		this.success = success;
	}

	/**
	 * @return the assetID
	 */
	public String getAssetID() {
		return assetID;
	}

	/**
	 * @param assetID
	 *            the assetID to set
	 */
	public void setAssetID(String assetID) {
		this.assetID = assetID;
	}

	/**
	 * @return the error
	 */
	public String getError() {
		return error;
	}

	/**
	 * @param error
	 *            the error to set
	 */
	public void setError(String error) {
		this.error = error;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorStatusCode;
	}

	/**
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(int errorCode) {
		this.errorStatusCode = errorCode;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	@Override
	public String toString() {
		return "Asset Id =" + getAssetID() + ",  Error =" + getError()
				+ ", Error Code" + getErrorCode() + ", Success=" + isSuccess();
	}

}
