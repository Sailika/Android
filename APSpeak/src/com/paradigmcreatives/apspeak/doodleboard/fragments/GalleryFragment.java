package com.paradigmcreatives.apspeak.doodleboard.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;

public class GalleryFragment extends Fragment {

	private ImageSelectionFragmentActivity activity;
	public int SELECT_PICTURE = 1;
	public int SELECT_PICTURE_KITKAT_AND_ABOVE = 2;

	public GalleryFragment(ImageSelectionFragmentActivity activity) {
		this.activity = activity;
	}

	public GalleryFragment() {
		super();
	}

	// private GalleryImageAdapter mGalleryImageAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.gallery_frame, container, false);
		/*
		 * GridView gridview = (GridView) view.findViewById(R.id.gridview);
		 * mGalleryImageAdapter = new GalleryImageAdapter(activity,
		 * getFilePaths()); gridview.setAdapter(mGalleryImageAdapter);
		 * gridview.setOnItemClickListener(new OnItemClickListener() {
		 * 
		 * @Override public void onItemClick(AdapterView<?> parent, View view,
		 * int position, long id) {
		 * activity.notifyImagePath(getFilePaths().get(position), true); } });
		 */

		if (Build.VERSION.SDK_INT < 19) {
			Intent galleryIntent = new Intent();
			galleryIntent.setType("image/*");
			galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(galleryIntent, "Select Picture"),
					SELECT_PICTURE);
		} else {
			Intent galleryIntent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(galleryIntent,
					SELECT_PICTURE_KITKAT_AND_ABOVE);
		}
		if (activity != null) {
			activity.stopProgress();
		}
		return view;

	}

	public ArrayList<String> getFilePaths() {

		Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.ImageColumns.DATA };
		Cursor c = null;
		SortedSet<String> dirList = new TreeSet<String>();
		ArrayList<String> resultList = new ArrayList<String>();

		String[] directories = null;
		if (u != null) {
			c = activity.getContentResolver().query(u, projection, null, null,
					null);
		}

		if ((c != null) && (c.moveToFirst())) {
			do {
				String tempDir = c.getString(0);
				tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
				try {
					dirList.add(tempDir);
				} catch (Exception e) {

				}
			} while (c.moveToNext());
			directories = new String[dirList.size()];
			dirList.toArray(directories);

		}

		for (int i = 0; i < dirList.size(); i++) {
			File imageDir = new File(directories[i]);
			File[] imageList = imageDir.listFiles();
			if (imageList == null)
				continue;
			for (File imagePath : imageList) {
				try {

					if (imagePath.isDirectory()) {
						imageList = imagePath.listFiles();

					}
					if (imagePath.getName().contains(".jpg")
							|| imagePath.getName().contains(".JPG")
							|| imagePath.getName().contains(".jpeg")
							|| imagePath.getName().contains(".JPEG")
							|| imagePath.getName().contains(".png")
							|| imagePath.getName().contains(".PNG")
							|| imagePath.getName().contains(".gif")
							|| imagePath.getName().contains(".GIF")
							|| imagePath.getName().contains(".bmp")
							|| imagePath.getName().contains(".BMP")) {

						String path = imagePath.getAbsolutePath();

						resultList.add(path);

					}
				}
				// }
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return resultList;

	}

}
