package com.paradigmcreatives.apspeak.registration.tasks.parsers;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;

/**
 * Parses given json to Group Bean object(s)
 * 
 * @author Dileep | neuv
 * 
 */
public class GroupBeanParser {

    public GroupBeanParser() {
	super();
    }

    /**
     * Parses given JSONArray and returns array list of GroupBean instances
     * 
     * @param jsonArray
     * @return
     */
    public ArrayList<GroupBean> parseGroupBeanJSONArray(JSONArray jsonArray) {
	ArrayList<GroupBean> groupsList = null;
	if (jsonArray != null && jsonArray.length() > 0) {
	    groupsList = new ArrayList<GroupBean>();
	    for (int i = 0; i < jsonArray.length(); i++) {
		try {
		    GroupBean groupBean = parseGroupBeanJSON(jsonArray.getJSONObject(i));
		    if (groupBean != null) {
			groupsList.add(groupBean);
		    }
		} catch (Exception e) {

		}
	    }
	}
	return groupsList;
    }

    /**
     * Parses given JSONObject and returns GroupBean instance
     * 
     * @param jsonObject
     * @return
     */
    public GroupBean parseGroupBeanJSON(JSONObject jsonObject) {
	GroupBean groupBean = null;
	if (jsonObject != null) {
	    try {
		groupBean = new GroupBean();
		// group id
		if (jsonObject.has(JSONConstants.ID) && !jsonObject.isNull(JSONConstants.ID)) {
		    groupBean.setGroupId(jsonObject.getString(JSONConstants.ID));
		}
		// group name
		if (jsonObject.has(JSONConstants.NAME) && !jsonObject.isNull(JSONConstants.NAME)) {
		    groupBean.setGroupName(jsonObject.getString(JSONConstants.NAME));
		}
		// group icon URL
		if (jsonObject.has(JSONConstants.ICON) && !jsonObject.isNull(JSONConstants.ICON)) {
		    groupBean.setGroupIconURL(jsonObject.getString(JSONConstants.ICON));
		}
		// group created timestamp
		if (jsonObject.has(JSONConstants.CREATED_AT) && !jsonObject.isNull(JSONConstants.CREATED_AT)) {
		    groupBean.setGroupCreatedTimestamp(jsonObject.getString(JSONConstants.CREATED_AT));
		}
	    } catch (Exception e) {

	    }
	}
	return groupBean;
    }
}
