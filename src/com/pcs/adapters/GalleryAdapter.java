package com.pcs.adapters;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.pcs.imagegallery.R;

public class GalleryAdapter extends ArrayAdapter<String> {
	private Context mContext;
	private String[] urlArray;
	private LayoutInflater inflator;
	public ArrayList<Bitmap> imgs= new ArrayList<Bitmap>();

	public GalleryAdapter(Context context, int textViewResourceId, String[] urlArray) {
		super(context, textViewResourceId, urlArray);

		this.mContext = context;
		this.urlArray = urlArray;
		inflator = LayoutInflater.from(mContext);

	}

	@Override
	public int getCount() {
		return urlArray.length;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflator.inflate(R.layout.imagelist, null);

			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		}

		viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.imageURL = urlArray[position];
		new Download().execute(viewHolder);
		
		return convertView;
	}

	private static class ViewHolder {

		private ImageView imageView;
		private String imageURL;
		private Bitmap bitmap;
	}

	private class Download extends AsyncTask<ViewHolder, Void, ViewHolder> {

		@Override
		protected ViewHolder doInBackground(ViewHolder... params) {
			// TODO Auto-generated method stub
			// load image directly
			ViewHolder viewHolder = params[0];
			try {
				URL imageURL = new URL(viewHolder.imageURL);
				viewHolder.bitmap = BitmapFactory.decodeStream(imageURL
						.openStream());
				Log.e("success", "Downloading Image Completed");
			} catch (IOException e) {
				// TODO: handle exception
				Log.e("error", "Downloading Image Failed");
				viewHolder.bitmap = null;
			}

			return viewHolder;
		}

		@Override
		protected void onPostExecute(ViewHolder result) {
			// TODO Auto-generated method stub
			if (result.bitmap == null) {
				result.imageView.setImageResource(R.drawable.test);
				
			} else {
				result.imageView.setImageBitmap(result.bitmap);
				imgs.add(result.bitmap);
			}
		}
	}

}
