package com.paradigmcreatives.apspeak.app.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Data structure template for circular list. It is a partial implementation to support the need of DoodleView only
 * 
 * @author robin
 * 
 * @param <T>
 */
public class CircularList<T> {
    private int size;
    private List<T> circularList;
    private int pointer = -1;
    private boolean listFilledTillMaxSize = false;

    public CircularList(int length) {
	this.size = length;
	circularList = new ArrayList<T>(size);
    }

    public void add(T obj) {
	if (pointer < (size - 1)) {
	    pointer++;
	} else {
	    pointer = 0;
	}
	if (pointer < size && !listFilledTillMaxSize) {
	    circularList.add(pointer, obj);
	    if(pointer == (size - 1)) {
		listFilledTillMaxSize = true;
	    }
	} else {
	    circularList.set(pointer, obj);
	}

    }

    public boolean contains(T obj) {
	return circularList.contains(obj);
    }

}
