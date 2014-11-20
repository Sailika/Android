package com.paradigmcreatives.apspeak.doodleboard;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.paradigmcreatives.apspeak.R;

public class GalleryImageAdapter extends BaseAdapter {

	private ImageSelectionFragmentActivity activity;

	private ArrayList<String> listofImagePaths;

	public GalleryImageAdapter(ImageSelectionFragmentActivity activity,
			ArrayList<String> listofImagePaths) {
		this.activity = activity;
		this.listofImagePaths = listofImagePaths;
	}

	public void add(String path) {
		listofImagePaths.add(path);
	}

	@Override
	public int getCount() {
		return listofImagePaths.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listofImagePaths.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = activity.getLayoutInflater().inflate(
					R.layout.gallery_grid_item, parent, false);
		} else {
			view = convertView;
		}
		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
		Bitmap bm = decodeSampledBitmapFromUri(listofImagePaths.get(position),
				100, 100);

		imageView.setImageBitmap(bm);
		return view;
	}

	public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
			int reqHeight) {

		Bitmap bm = null;
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		bm = BitmapFactory.decodeFile(path, options);

		return bm;
	}

	public int calculateInSampleSize(

	BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}

		return inSampleSize;
	}

}