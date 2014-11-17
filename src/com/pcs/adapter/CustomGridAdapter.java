package com.pcs.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pcs.apspeak.R;
import com.pcs.model.FeedBackItems;

@SuppressLint("InflateParams")
public class CustomGridAdapter extends BaseAdapter {
	private List<FeedBackItems> mItems;
	private Context mContext;

	public CustomGridAdapter(Context context, List<FeedBackItems> items) {
		mContext = context;
		this.mItems = items;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}

	@Override
	public Object getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			// inflate the GridView item layout
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.feeb_back_list_item_view,
					parent, false);
			// initialize the view holder
			viewHolder = new ViewHolder();
			viewHolder.layout = (RelativeLayout) convertView
					.findViewById(R.id.list_layout);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.list_image);;
			viewHolder.likeIcon = (ImageView) convertView
					.findViewById(R.id.like_button);
			convertView.setTag(viewHolder);
		} else {
			// recycle the already inflated view
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// update the item view
		FeedBackItems item = mItems.get(position);
		viewHolder.image.setBackgroundResource(item.getImage());

		
		viewHolder.likeIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Boolean clicked = new Boolean(false);
				viewHolder.likeIcon.setTag(clicked); // wasn't clicked

				 if( ((Boolean)viewHolder.likeIcon.getTag())==false ){
		              viewHolder.likeIcon.setImageResource(R.drawable.like_button);
		              viewHolder.likeIcon.setTag(new Boolean(true));
				
				 }
				 else {
						viewHolder.likeIcon.setImageResource(R.drawable.inlike);

				}
			}
		});

		return convertView;
	}

	/**
	 * The view holder design pattern prevents using findViewById() repeatedly
	 * in the getView() method of the adapter.
	 */
	public static class ViewHolder {
		public ImageView likeIcon;
		public ImageView image;
		public RelativeLayout layout;
	}

}
