package com.pcs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcs.model.User;
import com.pcs.swipedelete.MainActivity;
import com.pcs.swipedelete.R;


public class Adapter extends BaseAdapter {

	private Context ucontext;
	private ArrayList<User> users;
	private LayoutInflater inflator;

	public Adapter(MainActivity activity, ArrayList<User> userlist)
	{
		ucontext = activity;
		users=userlist;
		inflator = LayoutInflater.from(ucontext);
	}
	@Override
	public int getCount() {

		return users.size();
	}

	@Override
	public Object getItem(int position) {

		return users.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View contentView, ViewGroup parent) {
		ViewHolder holder;
		if(contentView ==null){
			contentView =inflator.inflate(R.layout.item_view,null);

			holder= new ViewHolder();
			holder.name=(TextView)contentView.findViewById(R.id.name);
			holder.content=(TextView)contentView.findViewById(R.id.content);
			contentView.setTag(holder);
		}
		else{
			holder=(ViewHolder)contentView.getTag();
		}


		holder.name.setText(users.get(position).getName());
		holder.content.setText(users.get(position).getMsgContent());
		
		
		return contentView;
	}

	public class ViewHolder{

		public TextView content;
		public TextView name;

	}

}