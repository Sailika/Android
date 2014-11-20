package com.paradigmcreatives.apspeak.stream.adapters;



import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.images.ImageUtil;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Class used as adapter to hold Asset Liked user picture and name
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetLikedPeopleAdapter extends BaseAdapter {

	private final String TAG = "AssetLikedPeopleAdapter";

	private Activity activity;
	private ArrayList<Friend> lovedUsers;
	private DisplayImageOptions options;

	public AssetLikedPeopleAdapter(Activity activity, ArrayList<Friend> lovedUsers) {
		super();
		this.activity = activity;
		this.lovedUsers = lovedUsers;
		this.options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
				.displayer(new FadeInBitmapDisplayer(250)).build();
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this.activity));
	}

	@Override
	public int getCount() {
		if (lovedUsers != null) {
			return lovedUsers.size();
		} else {
			return 0;
		}
	}

	@Override
	public Friend getItem(int position) {
		if (lovedUsers != null) {
			return lovedUsers.get(position);
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
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.stream_itemdetails_people_griditem, null);
			viewHolder.assetOwnerPic = (ImageView) convertView.findViewById(R.id.loved_user_pic);
			viewHolder.assetOwnerName = (TextView) convertView.findViewById(R.id.loved_user_name);
			convertView.setFocusable(false);
			convertView.setClickable(false);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (viewHolder != null) {
			Friend lovedUser = getItem(position);
			setAssetOwnerPic(lovedUser.getProfilePicURL(), viewHolder.assetOwnerPic);
			setOwnerName(lovedUser.getName(), viewHolder.assetOwnerName);
		}
		return convertView;
	}

	/**
	 * Sets asset's owner picture
	 * 
	 * @param profilePicUrl
	 * @param ownerImage
	 */
	public void setAssetOwnerPic(String profilePicUrl, ImageView ownerImage) {
		if (ownerImage == null) {
			return;
		}
		ownerImage.setImageResource(R.drawable.userpic);
		if (!TextUtils.isEmpty(profilePicUrl)) {
			ImageLoader.getInstance().displayImage(profilePicUrl, ownerImage, options, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				    try{
					loadedImage = ImageUtil.getCircularBitmapResizeTo(activity, loadedImage,
						Constants.BUBBLE_IMAGE_SIZE, Constants.HEADER_IMAGE_SIZE);
					((ImageView) view).setImageBitmap(loadedImage);
				    }catch(Exception e){
					
				    }
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub
				}
			});
		} else {
			Logger.info(TAG, "Asset is null");
		}
	}

	/**
	 * Sets asset's owner name
	 * 
	 * @param userName
	 * @param ownerTextView
	 */
	public void setOwnerName(String userName, TextView ownerTextView) {
		if (ownerTextView == null) {
			return;
		}
		String defaultName = "Whatsay User";
		if (activity != null) {
			defaultName = activity.getString(R.string.whatsay_user);
		}
		if (!TextUtils.isEmpty(userName)) {
			ownerTextView.setText(userName);
			ownerTextView.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf"));
		} else {
			Logger.info(TAG, "Asset Owner name is null");
			ownerTextView.setText(defaultName);
			ownerTextView.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf"));
		}
	}

	/**
	 * @return the listOfLovedUsers
	 */
	public ArrayList<Friend> getListOfLovedUsers() {
		return lovedUsers;
	}

	/**
	 * @param listOfLovedUsers
	 *            the listOfLovedUsers to set
	 */
	public void setListOfUsers(ArrayList<Friend> listOfLovedUsers) {
		this.lovedUsers = listOfLovedUsers;
	}

	/**
	 * Adapter's view holder
	 * 
	 * @author Dileep | neuv
	 * 
	 */
	private class ViewHolder {
		private ImageView assetOwnerPic;
		private TextView assetOwnerName;
	}

}
