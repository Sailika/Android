package com.paradigmcreatives.apspeak.app.util.google.image.beans;

import java.util.ArrayList;

/**
 * Bean returning the search result of google image search
 * 
 * @author robin
 * 
 */
public class SearchResultsBean {

    private CursorBean cursor;
    private ArrayList<ImageResultsBean> imageResults;
    private int responseStatus;
    private String responseDetails;

    /**
     * @param cursors
     * @param imageResults
     * @param responseStatus
     * @param responseDetails
     */
    public SearchResultsBean(CursorBean cursor, ArrayList<ImageResultsBean> imageResults, int responseStatus,
	    String responseDetails) {
	super();
	this.cursor = cursor;
	this.imageResults = imageResults;
	this.responseStatus = responseStatus;
	this.responseDetails = responseDetails;
    }

    public SearchResultsBean() {
	super();
    }

    /**
     * @return the cursor
     */
    public CursorBean getCursor() {
	return cursor;
    }

    /**
     * @param cursor
     *            the cursor to set
     */
    public void setCursor(CursorBean cursor) {
	this.cursor = cursor;
    }

    /**
     * @return the imageResults
     */
    public ArrayList<ImageResultsBean> getImageResults() {
	return imageResults;
    }

    /**
     * @param imageResults
     *            the imageResults to set
     */
    public void setImageResults(ArrayList<ImageResultsBean> imageResults) {
	this.imageResults = imageResults;
    }

    /**
     * @return the responseStatus
     */
    public int getResponseStatus() {
	return responseStatus;
    }

    /**
     * @param responseStatus
     *            the responseStatus to set
     */
    public void setResponseStatus(int responseStatus) {
	this.responseStatus = responseStatus;
    }

    /**
     * @return the responseDetails
     */
    public String getResponseDetails() {
	return responseDetails;
    }

    /**
     * @param responseDetails
     *            the responseDetails to set
     */
    public void setResponseDetails(String responseDetails) {
	this.responseDetails = responseDetails;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((cursor == null) ? 0 : cursor.hashCode());
	result = prime * result + ((imageResults == null) ? 0 : imageResults.hashCode());
	result = prime * result + ((responseDetails == null) ? 0 : responseDetails.hashCode());
	result = prime * result + responseStatus;
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	SearchResultsBean other = (SearchResultsBean) obj;
	if (cursor == null) {
	    if (other.cursor != null)
		return false;
	} else if (!cursor.equals(other.cursor))
	    return false;
	if (imageResults == null) {
	    if (other.imageResults != null)
		return false;
	} else if (!imageResults.equals(other.imageResults))
	    return false;
	if (responseDetails == null) {
	    if (other.responseDetails != null)
		return false;
	} else if (!responseDetails.equals(other.responseDetails))
	    return false;
	if (responseStatus != other.responseStatus)
	    return false;
	return true;
    }

}
