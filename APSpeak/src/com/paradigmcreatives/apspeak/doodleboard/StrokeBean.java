package com.paradigmcreatives.apspeak.doodleboard;

/**
 * Brush stroke bean
 * @author robin
 *
 */
public class StrokeBean {
    
    /**
     * Size of the brush
     */
    private int size;
    
    /**
     * 6-character alpha-numeric hex of the color
     */
    private String color;

    /**
     * @param size
     * @param color
     */
    public StrokeBean(int size, String color) {
	this.size = size;
	this.color = color;
    }

    /**
     * @param size
     */
    public StrokeBean(int size) {
	this.size = size;
    }

    /**
     * @param color
     */
    public StrokeBean(String color) {
	this.color = color;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color;
    }
    
    

}
