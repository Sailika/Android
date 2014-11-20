package com.paradigmcreatives.apspeak.discovery.adapters;

import java.util.ArrayList;

import android.text.TextUtils;
import android.widget.Filter;

import com.paradigmcreatives.apspeak.app.model.Friend;

public class FriendsFilter extends Filter {

    private UserNetworkAdapter adapter;

    public FriendsFilter(UserNetworkAdapter adapter) {
	super();
	this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
	if (adapter != null) {
	    FilterResults filteredResults = new FilterResults();
	    ArrayList<Friend> originalList = new ArrayList<Friend>();
	    originalList.addAll(adapter.getListOfFriends());

	    if (originalList != null && originalList.size() > 0) {
		ArrayList<Friend> filteredFriends = null;
		if (constraint != null && !TextUtils.isEmpty(constraint.toString())) {
		    filteredFriends = new ArrayList<Friend>();
		    ArrayList< Friend> tempList=new ArrayList<Friend>(originalList);
		    for (int i = 0; i < originalList.size(); i++) {
			Friend friend = originalList.get(i);
			if ((friend.getName().toLowerCase()).startsWith(constraint.toString().toLowerCase())) {
			    filteredFriends.add(friend);
			    tempList.remove(friend);
			}
		    }
		    for(int i=0;i<tempList.size();i++){
		    	Friend friend = tempList.get(i);
		    	if (friend.matchString(constraint.toString())) {
		    		filteredFriends.add(friend);
		    	}
		    }
		}else{
		    // When the constraint is null, the original data must be restored
		    filteredFriends = new ArrayList<Friend>(originalList);
		}
		filteredResults.count = filteredFriends.size();
		filteredResults.values = filteredFriends;
	    }

	    return filteredResults;
	} else {
	    return null;
	}
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
	if (adapter != null && results != null) {
	    ArrayList<Friend> filteredResults = (ArrayList<Friend>) results.values;
	    adapter.setFilteredFriends(filteredResults);
	    adapter.notifyDataSetChanged();
	}
    }
}
