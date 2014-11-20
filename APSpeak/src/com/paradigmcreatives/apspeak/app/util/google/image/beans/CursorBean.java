package com.paradigmcreatives.apspeak.app.util.google.image.beans;

import java.util.ArrayList;

/**
 * Bean for the cursor item returned in the Google Image search
 * 
 * @author robin
 * 
 */
public class CursorBean {
    /**
     * Total number of result for the query
     */
    private int resultCount;

    /**
     * Total number of pages
     */
    private ArrayList<PagesBean> pages;

    /**
     * Current page index
     */
    private int currentPageIndex;

    /**
     * The URL for getting more result
     */
    private String moreResultsUrl;

    /**
     * Time taken to perform the result
     */
    private float serachResultTime;

    /**
     * @param resultCount
     * @param pages
     * @param currentPageIndex
     * @param moreResultsUrl
     * @param serachResultTime
     */
    public CursorBean(int resultCount, ArrayList<PagesBean> pages, int currentPageIndex, String moreResultsUrl,
	    float serachResultTime) {
	this.resultCount = resultCount;
	this.pages = pages;
	this.currentPageIndex = currentPageIndex;
	this.moreResultsUrl = moreResultsUrl;
	this.serachResultTime = serachResultTime;
    }

    public CursorBean() {
	super();
    }

    /**
     * @return the resultCount
     */
    public int getResultCount() {
	return resultCount;
    }

    /**
     * @param resultCount
     *            the resultCount to set
     */
    public void setResultCount(int resultCount) {
	this.resultCount = resultCount;
    }

    /**
     * @return the pages
     */
    public ArrayList<PagesBean> getPages() {
	return pages;
    }

    /**
     * @param pages
     *            the pages to set
     */
    public void setPages(ArrayList<PagesBean> pages) {
	this.pages = pages;
    }

    /**
     * @return the currentPageIndex
     */
    public int getCurrentPageIndex() {
	return currentPageIndex;
    }

    /**
     * @param currentPageIndex
     *            the currentPageIndex to set
     */
    public void setCurrentPageIndex(int currentPageIndex) {
	this.currentPageIndex = currentPageIndex;
    }

    /**
     * @return the moreResultsUrl
     */
    public String getMoreResultsUrl() {
	return moreResultsUrl;
    }

    /**
     * @param moreResultsUrl
     *            the moreResultsUrl to set
     */
    public void setMoreResultsUrl(String moreResultsUrl) {
	this.moreResultsUrl = moreResultsUrl;
    }

    /**
     * @return the serachResultTime
     */
    public float getSerachResultTime() {
	return serachResultTime;
    }

    /**
     * @param serachResultTime
     *            the serachResultTime to set
     */
    public void setSerachResultTime(float serachResultTime) {
	this.serachResultTime = serachResultTime;
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
	result = prime * result + currentPageIndex;
	result = prime * result + ((moreResultsUrl == null) ? 0 : moreResultsUrl.hashCode());
	result = prime * result + ((pages == null) ? 0 : pages.hashCode());
	result = prime * result + resultCount;
	result = prime * result + Float.floatToIntBits(serachResultTime);
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
	CursorBean other = (CursorBean) obj;
	if (currentPageIndex != other.currentPageIndex)
	    return false;
	if (moreResultsUrl == null) {
	    if (other.moreResultsUrl != null)
		return false;
	} else if (!moreResultsUrl.equals(other.moreResultsUrl))
	    return false;
	if (pages == null) {
	    if (other.pages != null)
		return false;
	} else if (!pages.equals(other.pages))
	    return false;
	if (resultCount != other.resultCount)
	    return false;
	if (Float.floatToIntBits(serachResultTime) != Float.floatToIntBits(other.serachResultTime))
	    return false;
	return true;
    }

}
