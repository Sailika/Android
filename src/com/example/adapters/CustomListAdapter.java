package com.example.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.model.FridgeStatusItems;
import com.example.whirlpool.R;

public class CustomListAdapter extends BaseAdapter {
	private List<FridgeStatusItems> fridge_status_list;
	private Context mContext;
	private LayoutInflater inflator;

	public CustomListAdapter(Context activity,
			List<FridgeStatusItems> fridge_status_list) {
		mContext = activity;
		this.fridge_status_list = fridge_status_list;
	}

	@Override
	public int getCount() {
		return fridge_status_list.size();
	}

	@Override
	public Object getItem(int position) {
		return fridge_status_list.get(position);
	}

	@Override
	public long getItemId(int position) {
        return fridge_status_list.indexOf(getItem(position));	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		   if (convertView == null) {
	            LayoutInflater mInflater = (LayoutInflater) mContext
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            convertView = mInflater.inflate(R.layout.fridge_status_list_view, null);
	        }

	        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.staus_icon);
	        TextView txtTitle = (TextView) convertView.findViewById(R.id.heading);
	        TextView txtState = (TextView) convertView.findViewById(R.id.state);

	        FridgeStatusItems row_pos = fridge_status_list.get(position);
	        txtState.setText(row_pos.getState());
	        txtTitle.setText(row_pos.getHeading());
	        imgIcon.setBackgroundResource(R.drawable.singleprogressbar);

	        return convertView;
}
}
