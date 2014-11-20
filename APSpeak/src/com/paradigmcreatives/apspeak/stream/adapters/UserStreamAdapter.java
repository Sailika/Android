package com.paradigmcreatives.apspeak.stream.adapters;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.GestureDetector;
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
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamClickListener;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamGestureListener;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamOnTouchListener;
import com.paradigmcreatives.apspeak.stream.util.StreamAssetUtil;

/**
 * Class used as adapter to hold StreamAsset objects
 * 
 * @author Dileep | neuv
 * 
 */
public class UserStreamAdapter extends BaseAdapter {

	private Fragment fragment;
	private ArrayList<StreamAsset> streamAssets;
	private DisplayImageOptions options;
	private boolean mIsGridviewInUse = true;
	private Typeface mRobotoBold;
	private Typeface mRobotoRegular;
	private boolean showProfilePicAndName;

	public UserStreamAdapter(Fragment fragment, ArrayList<StreamAsset> assets) {
		super();
		this.fragment = fragment;
		this.streamAssets = assets;
		this.options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.displayer(new FadeInBitmapDisplayer(250)).build();
		this.mRobotoBold = Typeface.createFromAsset(this.fragment.getActivity().getAssets(), "Roboto-Bold.ttf");
		this.mRobotoRegular = Typeface.createFromAsset(this.fragment.getActivity().getAssets(), "Roboto-Regular.ttf");
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
		if (convertView == null && fragment != null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.stream_item_layout, null);

			//viewHolder.assetLayout = (RelativeLayout) convertView.findViewById(R.id.stream_doodle_asset);
			viewHolder.assetOwnerPic = (ImageView) convertView.findViewById(R.id.asset_owner_pic);
			viewHolder.assetOwnerName = (TextView) convertView.findViewById(R.id.asset_owner_name);
			viewHolder.assetCreatedTimestamp = (TextView) convertView.findViewById(R.id.asset_created_date);
			viewHolder.assetImage = (ImageView) convertView.findViewById(R.id.asset_image);
			viewHolder.assetLoveAnimationImage = (ImageView) convertView.findViewById(R.id.asset_love_animation_image);
			viewHolder.assetLoves = (TextView) convertView.findViewById(R.id.asset_loves);
			viewHolder.assetComments = (TextView) convertView.findViewById(R.id.asset_comments);
			viewHolder.assetLoveAction = (ImageView) convertView.findViewById(R.id.asset_action_love);
			viewHolder.assetOptions = (ImageView) convertView.findViewById(R.id.asset_options);
			viewHolder.progreswheel = (ProgressWheel) convertView.findViewById(R.id.progressBarwheel);
			
			viewHolder.assetOwnerName.setTypeface(mRobotoBold);
			viewHolder.assetCreatedTimestamp.setTypeface(mRobotoRegular);
			viewHolder.assetLoves.setTypeface(mRobotoRegular);
			viewHolder.assetComments.setTypeface(mRobotoRegular);
			
			// Hide both Asset's owner picture and name if the current view is GridView 
			if(mIsGridviewInUse){
			    RelativeLayout assetsHeaderLayout = (RelativeLayout) convertView.findViewById(R.id.asset_header_layout);
			    assetsHeaderLayout.setVisibility(View.GONE);
			}
			
			// Hide both Asset Owner Pic and Name if the asset in stream is being shown in Profile screen
			if(!showProfilePicAndName){
			    viewHolder.assetOwnerPic.setVisibility(View.GONE);
			    viewHolder.assetOwnerName.setVisibility(View.GONE);
			    viewHolder.assetCreatedTimestamp.setVisibility(View.GONE);
			}else{
			    viewHolder.assetOwnerPic.setVisibility(View.VISIBLE);
			    viewHolder.assetOwnerName.setVisibility(View.VISIBLE);
			    viewHolder.assetCreatedTimestamp.setVisibility(View.VISIBLE);
			}
			
			convertView.setFocusable(false);
			convertView.setClickable(false);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (viewHolder != null && fragment != null) {
			viewHolder.progreswheel.setVisibility(View.GONE);
			StreamAssetUtil assetUtil = new StreamAssetUtil(fragment, options);
			StreamAsset asset = getItem(position);
			if(viewHolder.assetOwnerPic.getVisibility() == View.VISIBLE){
			    assetUtil.setAssetOwnerPic(asset, viewHolder.assetOwnerPic);
			}
			if(viewHolder.assetOwnerName.getVisibility() == View.VISIBLE){
			    assetUtil.setOwnerName(asset, viewHolder.assetOwnerName);
			}
			assetUtil.setAssetCreateDate(asset, viewHolder.assetCreatedTimestamp);
			assetUtil.setAssetImage(asset, viewHolder.assetImage, viewHolder.progreswheel);
			assetUtil.setAssetLikeIcon(asset, viewHolder.assetLoveAction);
			assetUtil.setAssetLoves(asset, viewHolder.assetLoves);
			assetUtil.setAssetComments(asset, viewHolder.assetComments);
			UserStreamClickListener listener = new UserStreamClickListener(fragment, asset);
			listener.setAssetAnimationIconHolder(viewHolder.assetLoveAnimationImage);
			viewHolder.assetOwnerPic.setOnClickListener(listener);
			viewHolder.assetOwnerName.setOnClickListener(listener);
			viewHolder.assetLoveAction.setOnClickListener(listener);
			viewHolder.assetOptions.setOnClickListener(listener);
			
			UserStreamGestureListener gestureListener = new UserStreamGestureListener(fragment, asset);
			gestureListener.setAssetAnimationIconHolder(viewHolder.assetLoveAnimationImage);
			GestureDetector gd = new GestureDetector(fragment.getActivity().getApplicationContext(), gestureListener);
			UserStreamOnTouchListener touchListener = new UserStreamOnTouchListener(gd,fragment);
			viewHolder.assetImage.setOnTouchListener(touchListener);
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
	public synchronized void refreshListItemAtPosition(int position, ListView listView) {
		if (listView != null && position >= listView.getFirstVisiblePosition()
				&& position <= listView.getLastVisiblePosition() && fragment != null) {
			View listItem = listView.getChildAt(position - listView.getFirstVisiblePosition());
			ImageView likeImage = (ImageView) listItem.findViewById(R.id.asset_action_love);
			TextView lovesCountText = (TextView) listItem.findViewById(R.id.asset_loves);
			TextView commentsCountText = (TextView) listItem.findViewById(R.id.asset_comments);
			
			lovesCountText.setTypeface(mRobotoRegular);
			commentsCountText.setTypeface(mRobotoRegular);
			
			StreamAssetUtil assetUtil = new StreamAssetUtil(fragment, options);
			StreamAsset asset = getItem(position);
			assetUtil.setAssetLikeIcon(asset, likeImage);
			assetUtil.setAssetLoves(asset, lovesCountText);
		}
	}

	/**
	 * Refreshes the particular item from grid view
	 * 
	 * @param position
	 */
	public synchronized void refreshGridItemAtPosition(int position, GridView gridView) {
		if (gridView != null && position >= gridView.getFirstVisiblePosition()
				&& position <= gridView.getLastVisiblePosition() && fragment != null) {
			View listItem = gridView.getChildAt(position - gridView.getFirstVisiblePosition());
			ImageView likeImage = (ImageView) listItem.findViewById(R.id.asset_action_love);
			TextView lovesCountText = (TextView) listItem.findViewById(R.id.asset_loves);
			TextView commentsCountText = (TextView) listItem.findViewById(R.id.asset_comments);
			
			lovesCountText.setTypeface(mRobotoRegular);
			commentsCountText.setTypeface(mRobotoRegular);
			
			StreamAssetUtil assetUtil = new StreamAssetUtil(fragment, options);
			StreamAsset asset = getItem(position);
			assetUtil.setAssetLikeIcon(asset, likeImage);
			assetUtil.setAssetLoves(asset, lovesCountText);
			assetUtil.setAssetReposts(asset, commentsCountText);
		}
	}

	/**
	 * @return the listOfAssets
	 */
	public ArrayList<StreamAsset> getListOfAssets() {
		return streamAssets;
	}

	/**
	 * @param listOfAssets the listOfAssets to set
	 */
	public void setListOfAssets(ArrayList<StreamAsset> listOfAssets) {
		this.streamAssets = listOfAssets;
	}

	/**
	 * Updates asset relationship with icon/details/count/etc
	 */
	public void updateAssetRelationship(String assetId, ASSOCIATION_TYPE relationship, View view) {
		if (!TextUtils.isEmpty(assetId) && relationship != null && streamAssets != null && streamAssets.size() > 0
				&& view != null) {
			if (relationship == ASSOCIATION_TYPE.LOVE) {
				((ImageView) view).setImageResource(R.drawable.like_selected);
			}
		}
	}

	/**
	 * Adapter's view holder
	 * 
	 * @author Dileep | neuv
	 * 
	 */
	private class ViewHolder {
		//private RelativeLayout assetLayout;
		private ImageView assetOwnerPic;
		private TextView assetOwnerName;
		private TextView assetCreatedTimestamp;
		private ImageView assetImage;
		private ImageView assetLoveAnimationImage;
		private TextView assetLoves;
		private TextView assetComments;
		private ImageView assetLoveAction;
		private ImageView assetOptions;
		private ProgressWheel progreswheel=null;
	}

	/**
	 * Sets the flag that represents whether grid view is in use or not
	 * @param gridViewUsageFlag
	 */
	public void setIsGridViewInUseFlag(boolean gridViewUsageFlag){
	    mIsGridviewInUse = gridViewUsageFlag;
	}

    /**
     * Appends next batch of assets to existing list
     * 
     * @param nextBatchAssets
     */
    public void appendNextBatchAssets(ArrayList<StreamAsset> nextBatchAssets) {
	if (nextBatchAssets != null && nextBatchAssets.size() > 0) {
	    //this.streamAssets.addAll(nextBatchAssets);
	    notifyDataSetChanged();
	}
    }

    
    public void showProfilePicAndName(boolean isShow){
	this.showProfilePicAndName = isShow;
    }
    
    /**
     * Clears all items from the adapter
     */
    public void clearAll(){
	if(streamAssets != null){
	    streamAssets.clear();
	    notifyDataSetChanged();
	}
    }
}

