package com.paradigmcreatives.apspeak.app.util.server.assets.parsers;

import java.util.ArrayList;

import org.json.JSONArray;

/**
 * Interface to be implemented by all the Doodly Doo asset parsers
 * 
 * @author robin
 * 
 */
public interface AssetsParser {

    /**
     * This method parses the JSON object based on the supplied asset type
     * 
     * @param object
     * @return
     */
    public ArrayList<?> parse(JSONArray object);

}
