package com.paradigmcreatives.apspeak.share.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.ShareAppInfo;

public class ShareWhatsayImageAdapter extends BaseAdapter{
    private Context mContext;
    private List<ShareAppInfo> mList = null;
    
    public ShareWhatsayImageAdapter(Context context, List<ShareAppInfo> appsList){
	this.mContext = context;
	this.mList = appsList;
	
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return  ((null != mList) ? mList.size() : 0);
    }

    @Override
    public ShareAppInfo getItem(int position) {
	// TODO Auto-generated method stub
	return  ((null != mList) ? mList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	// TODO Auto-generated method stub
	 View view = convertView;
	        if (null == view) {
	            LayoutInflater layoutInflater = (LayoutInflater) mContext
	                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = layoutInflater.inflate(R.layout.share_whatsay_image, null);
	        }
	 
	        ShareAppInfo data = mList.get(position);
	        if (null != data) {
	            TextView appName = (TextView) view.findViewById(R.id.app_label);
	            ImageView iconview = (ImageView) view.findViewById(R.id.app_icon);
	 
	            appName.setText(data.getName());
	            iconview.setImageDrawable(data.getIcon()); 
	        }
	
	return view;
    }

}
