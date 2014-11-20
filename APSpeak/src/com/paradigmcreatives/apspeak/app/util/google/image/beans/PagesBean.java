package com.paradigmcreatives.apspeak.app.util.google.image.beans;

/**
 * Bean for the pages stored in the cursor
 * 
 * @author robin
 * 
 */
public class PagesBean {
    /**
     * Start index to be used in the query
     */
    private int start;

    /**
     * Label for the start index
     */
    private int label;

    /**
     * @param start
     * @param label
     */
    public PagesBean(int start, int label) {
	this.start = start;
	this.label = label;
    }

    public PagesBean() {
	super();
    }

    /**
     * @return the start
     */
    public int getStart() {
	return start;
    }

    /**
     * @param start
     *            the start to set
     */
    public void setStart(int start) {
	this.start = start;
    }

    /**
     * @return the label
     */
    public int getLabel() {
	return label;
    }

    /**
     * @param label
     *            the label to set
     */
    public void setLabel(int label) {
	this.label = label;
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
	result = prime * result + label;
	result = prime * result + start;
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
	PagesBean other = (PagesBean) obj;
	if (label != other.label)
	    return false;
	if (start != other.start)
	    return false;
	return true;
    }

}
