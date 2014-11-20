package com.paradigmcreatives.apspeak.app.util.server.assets.beans;

/**
 * Bean for storing the data of a category
 * @author robin
 *
 */
public class CategoryBean {

    private String name;
    private int cardsCount;

    /**
     * 
     */
    public CategoryBean() {
	super();
    }

    /**
     * @param name
     * @param cardsCount
     */
    public CategoryBean(String name, int cardsCount) {
	this.name = name;
	this.cardsCount = cardsCount;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the cardsCount
     */
    public int getCardsCount() {
	return cardsCount;
    }

    /**
     * @param cardsCount
     *            the cardsCount to set
     */
    public void setCardsCount(int cardsCount) {
	this.cardsCount = cardsCount;
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
	result = prime * result + cardsCount;
	result = prime * result + ((name == null) ? 0 : name.hashCode());
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
	CategoryBean other = (CategoryBean) obj;
	if (cardsCount != other.cardsCount)
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	return true;
    }

}
