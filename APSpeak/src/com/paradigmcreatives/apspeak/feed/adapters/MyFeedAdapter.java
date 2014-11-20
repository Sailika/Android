package com.paradigmcreatives.apspeak.feed.adapters;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.feed.util.MyFeedUtil;

public class MyFeedAdapter extends BaseAdapter {

	private Fragment fragment;
	private ArrayList<MyFeedBean> myFeedList;
	private DisplayImageOptions options;
	private Typeface mRobotoBold;
	private Typeface mRobotoRegular;

	public MyFeedAdapter(Fragment fragment, ArrayList<MyFeedBean> myFeedList) {
		super();
		this.fragment = fragment;
		this.myFeedList = myFeedList;
		this.options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();
		this.mRobotoBold = Typeface.createFromAsset(this.fragment.getActivity()
				.getAssets(), "Roboto-Bold.ttf");
		this.mRobotoRegular = Typeface.createFromAsset(this.fragment
				.getActivity().getAssets(), "Roboto-Regular.ttf");
	}

	@Override
	public int getCount() {
		if (myFeedList != null) {
			return myFeedList.size();
		} else {
			return 0;
		}
	}

	@Override
	public MyFeedBean getItem(int position) {
		if (myFeedList != null) {
			return myFeedList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null && fragment != null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(fragment.getActivity()).inflate(
					R.layout.my_feed_list_item, null);

			viewHolder.feedOwnerPic = (ImageView) convertView
					.findViewById(R.id.my_feed_owner_pic);
			viewHolder.feedMessage = (TextView) convertView
					.findViewById(R.id.my_feed_text);
			viewHolder.feedTime = (TextView) convertView
					.findViewById(R.id.time_text);
			viewHolder.assetImage = (ImageView) convertView
					.findViewById(R.id.my_feed_asset);
			viewHolder.assetImage.setVisibility(View.GONE);

			convertView.setFocusable(false);
			convertView.setClickable(false);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (viewHolder != null && fragment != null) {

			MyFeedUtil feedUtil = new MyFeedUtil(fragment, options);
			MyFeedBean myFeedBean = getItem(position);

			String type = myFeedBean.getType();
			if (!TextUtils.isEmpty(type)) {
				if (type.equals(Constants.ANNOUNCEMENT)) {
					viewHolder.feedOwnerPic
							.setImageResource(R.drawable.announcement);
					viewHolder.assetImage.setVisibility(View.INVISIBLE);
				} else if (type.equals(Constants.EMOTE)) {
					viewHolder.feedOwnerPic
							.setImageResource(R.drawable.like_selected);
					viewHolder.assetImage.setVisibility(View.VISIBLE);
					feedUtil.setMyFeedPic(myFeedBean, viewHolder.assetImage,
							myFeedBean.getSnapUrl(), false);
				} else if (type.equals(Constants.EXPRESSION)) {
					viewHolder.assetImage.setVisibility(View.VISIBLE);
					String iconUrl = myFeedBean.getIconUrl();
					String snapUrl = myFeedBean.getSnapUrl();
					if (TextUtils.isEmpty(iconUrl)) {
						iconUrl = snapUrl;
						snapUrl = null;
					}
					feedUtil.setMyFeedPic(myFeedBean, viewHolder.feedOwnerPic,
							iconUrl, false);
					feedUtil.setMyFeedPic(myFeedBean, viewHolder.assetImage,
							snapUrl, false);
				} else {
					viewHolder.assetImage.setVisibility(View.INVISIBLE);
					feedUtil.setMyFeedPic(myFeedBean, viewHolder.feedOwnerPic,
							myFeedBean.getProfilePicture(), true);
				}
			} else {
				viewHolder.assetImage.setVisibility(View.INVISIBLE);
				feedUtil.setMyFeedPic(myFeedBean, viewHolder.feedOwnerPic,
						myFeedBean.getProfilePicture(), true);
			}
			feedUtil.setMyFeedCreateDate(myFeedBean, viewHolder.feedTime);

			viewHolder.feedMessage.setText(myFeedBean.getMessage());

		}
		return convertView;
	}

	/**
	 * Adapter's view holder
	 * 
	 * @author Dileep | neuv
	 * 
	 */
	private class ViewHolder {

		private ImageView feedOwnerPic;
		private TextView feedMessage;
		private TextView feedTime;
		private ImageView assetImage;

	}

}
