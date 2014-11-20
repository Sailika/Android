package com.paradigmcreatives.apspeak.app.util.google.image.beans;

/**
 * Bean for the image result array. As of now only limited parameters are being captured. There are many more parameters
 * that can be captured
 * 
 * @author robin
 * 
 */
public class ImageResultsBean {
    private int thumbnailWidth;
    private int thumbnailHeight;
    private String thumbnailURL;
    private int width;
    private int height;
    private String url;
    private String title;

    /**
     * @param thumbnailWidth
     * @param thumbnailHeight
     * @param thumbnailURL
     * @param width
     * @param height
     * @param url
     * @param title
     * @param timeTaken
     */
    public ImageResultsBean(int thumbnailWidth, int thumbnailHeight, String thumbnailURL, int width, int height,
	    String url, String title) {
	this.thumbnailWidth = thumbnailWidth;
	this.thumbnailHeight = thumbnailHeight;
	this.thumbnailURL = thumbnailURL;
	this.width = width;
	this.height = height;
	this.url = url;
	this.title = title;
    }
    
    public ImageResultsBean() {
	super();
    }

    /**
     * @return the thumbnailWidth
     */
    public int getThumbnailWidth() {
	return thumbnailWidth;
    }

    /**
     * @param thumbnailWidth
     *            the thumbnailWidth to set
     */
    public void setThumbnailWidth(int thumbnailWidth) {
	this.thumbnailWidth = thumbnailWidth;
    }

    /**
     * @return the thumbnailHeight
     */
    public int getThumbnailHeight() {
	return thumbnailHeight;
    }

    /**
     * @param thumbnailHeight
     *            the thumbnailHeight to set
     */
    public void setThumbnailHeight(int thumbnailHeight) {
	this.thumbnailHeight = thumbnailHeight;
    }

    /**
     * @return the thumbnailURL
     */
    public String getThumbnailURL() {
	return thumbnailURL;
    }

    /**
     * @param thumbnailURL
     *            the thumbnailURL to set
     */
    public void setThumbnailURL(String thumbnailURL) {
	this.thumbnailURL = thumbnailURL;
    }

    /**
     * @return the width
     */
    public int getWidth() {
	return width;
    }

    /**
     * @param width
     *            the width to set
     */
    public void setWidth(int width) {
	this.width = width;
    }

    /**
     * @return the height
     */
    public int getHeight() {
	return height;
    }

    /**
     * @param height
     *            the height to set
     */
    public void setHeight(int height) {
	this.height = height;
    }

    /**
     * @return the url
     */
    public String getURL() {
	return url;
    }

    /**
     * @param url
     *            the url to set
     */
    public void setURL(String url) {
	this.url = url;
    }

    /**
     * @return the title
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(String title) {
	this.title = title;
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
	result = prime * result + height;
	result = prime * result + thumbnailHeight;
	result = prime * result + ((thumbnailURL == null) ? 0 : thumbnailURL.hashCode());
	result = prime * result + thumbnailWidth;
	result = prime * result + ((title == null) ? 0 : title.hashCode());
	result = prime * result + ((url == null) ? 0 : url.hashCode());
	result = prime * result + width;
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
	ImageResultsBean other = (ImageResultsBean) obj;
	if (height != other.height)
	    return false;
	if (thumbnailHeight != other.thumbnailHeight)
	    return false;
	if (thumbnailURL == null) {
	    if (other.thumbnailURL != null)
		return false;
	} else if (!thumbnailURL.equals(other.thumbnailURL))
	    return false;
	if (thumbnailWidth != other.thumbnailWidth)
	    return false;
	if (title == null) {
	    if (other.title != null)
		return false;
	} else if (!title.equals(other.title))
	    return false;
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.equals(other.url))
	    return false;
	if (width != other.width)
	    return false;
	return true;
    }

}
