package com.paradigmcreatives.apspeak.stream.adapters;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.stream.fragments.AssetDetailsWithCommentsFragment;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamClickListener;
import com.paradigmcreatives.apspeak.stream.util.StreamAssetUtil;

/**
 * Class used as adapter to hold AssetDetails with Comments stream
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetDetailsWithCommentsAdapter extends BaseAdapter {

	private AssetDetailsWithCommentsFragment fragment;
	private ArrayList<StreamAsset> streamAssets;
	private DisplayImageOptions options;
	private Typeface mRobotoBold;
	private Typeface mRobotoRegular;

	public AssetDetailsWithCommentsAdapter(
			AssetDetailsWithCommentsFragment fragment,
			ArrayList<StreamAsset> assets) {
		super();
		this.fragment = fragment;
		this.streamAssets = assets;
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
		if (streamAssets != null) {
			return streamAssets.size();
		} else {
			return 0;
		}
	}

	@Override
	public StreamAsset getItem(int position) {
		if (streamAssets != null) {
			return streamAssets.get(position);
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
		if (/* convertView == null && */fragment != null) {
			viewHolder = new ViewHolder();
			if (position == 0) {
				convertView = fragment.initFirstChild(convertView, viewHolder);
			} else {
				convertView = LayoutInflater
						.from(fragment.getActivity())
						.inflate(R.layout.assetdetails_otherchilds_layout, null);

				viewHolder.assetOwnerPic = (ImageView) convertView
						.findViewById(R.id.asset_owner_pic);
				viewHolder.assetOwnerName = (TextView) convertView
						.findViewById(R.id.asset_owner_name);
				viewHolder.assetCreatedTimestamp = (TextView) convertView
						.findViewById(R.id.asset_created_date);
				viewHolder.assetImage = (ImageView) convertView
						.findViewById(R.id.asset_image);
				viewHolder.assetImage.getLayoutParams().height = viewHolder.assetImage
						.getLayoutParams().width;
				viewHolder.progreswheel = (ProgressWheel) convertView
						.findViewById(R.id.progressBarwheelone);
				viewHolder.assetLoves = (TextView) convertView
						.findViewById(R.id.asset_loves);
				viewHolder.assetComments = (TextView) convertView
						.findViewById(R.id.asset_comments);
				viewHolder.optionsImage = (ImageView) convertView
						.findViewById(R.id.asset_options);
				viewHolder.assetOwnerName.setTypeface(mRobotoBold);
				viewHolder.assetCreatedTimestamp.setTypeface(mRobotoRegular);

			}
			convertView.setFocusable(false);
			convertView.setClickable(false);
			convertView.setTag(viewHolder);

		}/*
		 * else { viewHolder = (ViewHolder) convertView.getTag(); }
		 */

		if (viewHolder != null && fragment != null && position > 0) {
			if (viewHolder.progreswheel != null) {
				viewHolder.progreswheel.setVisibility(View.GONE);
			}
			StreamAssetUtil assetUtil = new StreamAssetUtil(fragment, options);
			StreamAsset asset = getItem(position);
			assetUtil.setAssetOwnerPic(asset, viewHolder.assetOwnerPic);
			assetUtil.setOwnerName(asset, viewHolder.assetOwnerName);
			assetUtil.setAssetCreateDate(asset,
					viewHolder.assetCreatedTimestamp);
			assetUtil.setAssetLoves(asset, viewHolder.assetLoves);
			assetUtil.setAssetComments(asset, viewHolder.assetComments);
			assetUtil.setAssetImage(asset, viewHolder.assetImage,
					viewHolder.progreswheel);

			UserStreamClickListener listener = new UserStreamClickListener(
					fragment, asset);
			viewHolder.assetOwnerPic.setOnClickListener(listener);
			viewHolder.assetOwnerName.setOnClickListener(listener);

		}
		return convertView;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return areAllItemsEnabled();
	}

	/**
	 * Refreshes the particular item
	 * 
	 * @param position
	 */
	public synchronized void refreshListItemAtPosition(int position,
			ListView listView) {
		if (listView != null && position >= listView.getFirstVisiblePosition()
				&& position <= listView.getLastVisiblePosition()
				&& fragment != null) {
			View listItem = listView.getChildAt(position
					- listView.getFirstVisiblePosition());
			ImageView likeImage = (ImageView) listItem
					.findViewById(R.id.asset_action_love);
			TextView lovesCountText = (TextView) listItem
					.findViewById(R.id.asset_loves);
			TextView commentsCountText = (TextView) listItem
					.findViewById(R.id.asset_comments);

			lovesCountText.setTypeface(mRobotoRegular);
			commentsCountText.setTypeface(mRobotoRegular);

			StreamAssetUtil assetUtil = new StreamAssetUtil(fragment, options);
			StreamAsset asset = getItem(position);
			assetUtil.setAssetLikeIcon(asset, likeImage);
			assetUtil.setAssetLoves(asset, lovesCountText);
			assetUtil.setAssetComments(asset, commentsCountText);
		}
	}

	/**
	 * Refreshes the particular item from grid view
	 * 
	 * @param position
	 */
	public synchronized void refreshGridItemAtPosition(int position,
			GridView gridView) {
		if (gridView != null && position >= gridView.getFirstVisiblePosition()
				&& position <= gridView.getLastVisiblePosition()
				&& fragment != null) {
			View listItem = gridView.getChildAt(position
					- gridView.getFirstVisiblePosition());
			ImageView likeImage = (ImageView) listItem
					.findViewById(R.id.asset_action_love);
			TextView lovesCountText = (TextView) listItem
					.findViewById(R.id.asset_loves);
			TextView commentsCountText = (TextView) listItem
					.findViewById(R.id.asset_comments);

			lovesCountText.setTypeface(mRobotoRegular);
			commentsCountText.setTypeface(mRobotoRegular);

			StreamAssetUtil assetUtil = new StreamAssetUtil(fragment, options);
			StreamAsset asset = getItem(position);
			assetUtil.setAssetLikeIcon(asset, likeImage);
			assetUtil.setAssetLoves(asset, lovesCountText);
			assetUtil.setAssetComments(asset, commentsCountText);
		}
	}

	/**
	 * @return the listOfAssets
	 */
	public ArrayList<StreamAsset> getListOfAssets() {
		return streamAssets;
	}

	/**
	 * @param listOfAssets
	 *            the listOfAssets to set
	 */
	public void setListOfAssets(ArrayList<StreamAsset> listOfAssets) {
		this.streamAssets = listOfAssets;
	}

	/**
	 * Updates asset relationship with icon/details/count/etc
	 */
	public void updateAssetRelationship(String assetId,
			ASSOCIATION_TYPE relationship, View view) {
		if (!TextUtils.isEmpty(assetId) && relationship != null
				&& streamAssets != null && streamAssets.size() > 0
				&& view != null) {
			if (relationship == ASSOCIATION_TYPE.LOVE) {
				((ImageView) view).setImageResource(R.drawable.like_selected);
			}
		}
	}

	/**
	 * Appends next batch of assets to existing list
	 * 
	 * @param nextBatchAssets
	 */
	public void appendNextBatchAssets(ArrayList<StreamAsset> nextBatchAssets) {
		if (nextBatchAssets != null && nextBatchAssets.size() > 0) {
			this.streamAssets.addAll(nextBatchAssets);
			notifyDataSetChanged();
		}
	}

	/**
	 * Updates adapter by adding new list of assets to the existing list of
	 * assets
	 * 
	 * @param assets
	 */
	public void updateAdapter(ArrayList<StreamAsset> assets) {
		if (assets != null && assets.size() > 0) {
			this.streamAssets.addAll(assets);
			this.notifyDataSetChanged();
		}
	}
	
	
	/**
	 * Adapter's view holder
	 * 
	 * @author Dileep | neuv
	 * 
	 */
	public class ViewHolder {
		public ImageView assetOwnerPic;
		public TextView assetOwnerName;
		public TextView assetOwnerGroupName;
		public TextView assetCreatedTimestamp;
		public ImageView assetImage;
		public ImageView optionsImage;
		public ImageView assetLoveAnimationImage;
		public TextView assetLoves;
		public TextView assetComments;
		public RelativeLayout assetLovedUsersLayout;
		public GridView assetLovedUsersGrid;
		public TextView assetLovedUsersSeeAll;
		public ProgressWheel progreswheel;
		public TextView shareTxt;

		
		
	}

}
