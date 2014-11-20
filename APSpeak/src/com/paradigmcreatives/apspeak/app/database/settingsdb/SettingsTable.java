package com.paradigmcreatives.apspeak.app.database.settingsdb;

/**
 * The Database handler for settings
 * 
 * @author Vineela Gadhiraju | Paradigm Creatives
 * 
 */
public class SettingsTable {
    public static final String SETTINGS_INFO_TABLE = "settings_info";
    public static final String KEY = "key";
    public static final String VALUE = "value";

    public static String getCreateQuery() {
	return "CREATE TABLE IF NOT EXISTS " + SETTINGS_INFO_TABLE + "(" + KEY + " TEXT PRIMARY KEY, " + VALUE
		+ " TEXT);";
    }
}
