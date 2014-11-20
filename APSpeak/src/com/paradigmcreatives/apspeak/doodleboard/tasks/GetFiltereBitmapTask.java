package com.paradigmcreatives.apspeak.doodleboard.tasks;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.android.gpuimage.GPUImage;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.fragments.ImageFilterFragment;

public class GetFiltereBitmapTask extends AsyncTask<String, Void, Bitmap> {

	private Bitmap bitmap;
	private ImageFilterFragment fragment;
	private ProgressDialog progressBar;
	private GPUImage gpuImage;

	public GetFiltereBitmapTask(ImageFilterFragment fragment, GPUImage gpuImage) {

		this.fragment = fragment;
		this.gpuImage = gpuImage;

	}

	@Override
	protected Bitmap doInBackground(String... arg0) {
		bitmap = gpuImage.getBitmapWithFilterApplied();
		return bitmap;

	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (progressBar != null) {
			progressBar.dismiss();
		}
		fragment.setFilteredBitmap(bitmap);
	}

	@Override
	protected void onPreExecute() {
		progressBar = new ProgressDialog(fragment.getActivity(),
				R.style.MyTheme);
		progressBar.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
		if (progressBar != null) {
			progressBar.show();
		}
	}
}