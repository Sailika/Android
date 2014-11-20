package com.paradigmcreatives.apspeak.registration.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;

/**
 * This class is used as adapter class to display group name and flag in list
 * 
 * @author Dileep | neuv
 * 
 */
public class GroupsAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<GroupBean> data;

    public GroupsAdapter(Activity context, ArrayList<GroupBean> data) {
	this.context = context;
	this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	Holder holder;
	if (convertView == null) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    convertView = inflater.inflate(R.layout.spinner_layout, parent, false);
	    holder = new Holder();
	    holder.groupName = (TextView) convertView.findViewById(R.id.group_name);
	    convertView.setTag(holder);
	} else {
	    holder = (Holder) convertView.getTag();
	}
	GroupBean item = data.get(position);
	if (item != null) {
	    holder.groupName.setText(item.getGroupName());
	}

	return convertView;
    }

    @Override
    public int getCount() {
	return data.size();
    }

    @Override
    public GroupBean getItem(int position) {
	return data.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    class Holder {
	public TextView groupName;
    }

}