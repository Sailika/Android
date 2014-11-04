package com.pcs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pcs.model.User;
import com.pcs.swipedelete.MainActivity;
import com.pcs.swipedelete.R;

public class Adapter extends BaseAdapter {
	private final int INVALID = -1;
	protected int DELETE_POS = -1;
	private Context ucontext;
	// private Animation fade_in;
	// private Animation fade_out;
	private ArrayList<User> users;
	private LayoutInflater inflator;

	public Adapter(MainActivity activity, ArrayList<User> userlist) {
		ucontext = activity;
		users = userlist;
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
		// fade_out = AnimationUtils.loadAnimation(ucontext, R.anim.fade_out);
		// fade_in = AnimationUtils.loadAnimation(ucontext, R.anim.fade_in);

		if (contentView == null) {
			contentView = inflator.inflate(R.layout.item_view, null);

			holder = new ViewHolder();
			holder.name = (TextView) contentView.findViewById(R.id.name);
			holder.content = (TextView) contentView.findViewById(R.id.content);
			holder.undo = (Button) contentView.findViewById(R.id.undo);
			holder.deleted = (Button) contentView.findViewById(R.id.deleted);

			contentView.setTag(holder);
		} else {
			holder = (ViewHolder) contentView.getTag();
		}

		holder.name.setText(users.get(position).getName());
		holder.content.setText(users.get(position).getMsgContent());
		if (DELETE_POS == position) {
			
			
			holder.undo.setVisibility(View.VISIBLE);
			holder.deleted.setVisibility(View.VISIBLE);

		} else

			holder.undo.setVisibility(View.GONE);
		holder.deleted.setVisibility(View.GONE);

		holder.deleted.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteItem(position);
			}
		});

		return contentView;
	}

	public class ViewHolder {

		public TextView content;
		public TextView name;
		public Button undo;
		public Button deleted;

	}

	public void onSwipeItem(boolean isRight, int position) {
		if (isRight == false) {
			DELETE_POS = position;
		} else if (DELETE_POS == position) {
			DELETE_POS = INVALID;
		}
		notifyDataSetChanged();
		
	}

	public void deleteItem(int pos) {

		users.remove(pos);
		DELETE_POS = INVALID;
		notifyDataSetChanged();
	}
}