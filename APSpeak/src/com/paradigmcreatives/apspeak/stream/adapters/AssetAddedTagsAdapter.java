package com.paradigmcreatives.apspeak.stream.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.logging.Logger;

/**
 * Class used as adapter to hold Asset's tag added by users
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetAddedTagsAdapter extends BaseAdapter {

	private final String TAG = "AssetAddedTagsAdapter";

	private Activity activity;
	private ArrayList<String> tags;

	public AssetAddedTagsAdapter(Activity activity, ArrayList<String> tags) {
		super();
		this.activity = activity;
		this.tags = tags;
	}

	@Override
	public int getCount() {
		if (tags != null) {
			return tags.size();
		} else {
			return 0;
		}
	}

	@Override
	public String getItem(int position) {
		if (tags != null) {
			return tags.get(position);
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.stream_itemdetails_tag_griditem, null);

			viewHolder.tagName = (TextView) convertView.findViewById(R.id.added_tag_name);
			convertView.setFocusable(false);
			convertView.setClickable(false);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (viewHolder != null) {
			String tag = getItem(position);
			setTagName(tag, viewHolder.tagName);
		}
		return convertView;
	}

	/**
	 * Sets asset's tag name
	 * 
	 * @param tagName
	 * @param ownerTextView
	 */
	public void setTagName(String tagName, TextView tagTextView) {
		if (tagTextView == null) {
			return;
		}
		String defaultName = "#TAG";
		if (activity != null) {
			defaultName = activity.getString(R.string.asset_hash_tag);
		}
		if (!TextUtils.isEmpty(tagName)) {
			tagTextView.setText(tagName);
			tagTextView.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf"));
		} else {
			Logger.info(TAG, "Asset added tag is null");
			tagTextView.setText(defaultName);
			tagTextView.setTypeface(Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf"));
		}
	}

	/**
	 * @return the listOfTags
	 */
	public ArrayList<String> getListOfTags() {
		return tags;
	}

	/**
	 * @param listOfTags
	 *            the listOfTags to set
	 */
	public void setListOfTags(ArrayList<String> listOfTags) {
		this.tags = listOfTags;
	}

	/**
	 * Adapter's view holder
	 * 
	 * @author Dileep | neuv
	 * 
	 */
	private class ViewHolder {
		private TextView tagName;
	}

}
