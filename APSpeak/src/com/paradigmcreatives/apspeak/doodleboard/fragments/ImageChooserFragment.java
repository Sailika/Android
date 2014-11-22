package com.paradigmcreatives.apspeak.doodleboard.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.doodleboard.listeners.ImageSelectionOnClickListener;
import com.paradigmcreatives.apspeak.textstyles.TypeFontAssets;

public class ImageChooserFragment extends Fragment {
	private Fragment fragment;
	private LinearLayout mCameraLayout;
	private LinearLayout mGalleryLayout;
	private LinearLayout mWriteLayout;
	private TextView mCameraText;
	private TextView mGalleryText;
	private TextView mBackgroundText;
	private TextView mPostTxt;
	private FrameLayout childFrame;
	private ImageButton textIcon_camera,textIcon_gallery,imageIcon;
	private ImageSelectionFragmentActivity activity;
	private String cueID;

	public ImageChooserFragment() {
		super();
	}

	public ImageChooserFragment(ImageSelectionFragmentActivity activity,
			String cueID) {
		this.activity = activity;
		this.cueID = cueID;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.image_selection_layout,
				container, false);
		TextView headerText = (TextView)view.findViewById(R.id.globel_header_text);
	    headerText.setText(getResources().getString(R.string.create_own_idea));
		mCameraLayout = (LinearLayout) view
				.findViewById(R.id.camera_button_layout);

		mGalleryLayout = (LinearLayout) view
				.findViewById(R.id.gallery_button_layout);
		mWriteLayout = (LinearLayout) view
				.findViewById(R.id.write_button_layout);
		
		/***
		 * Change Style Here
		 */
		mCameraText = (TextView) view.findViewById(R.id.camera_text);
		mGalleryText = (TextView) view.findViewById(R.id.gallery_text);
		mBackgroundText = (TextView) view.findViewById(R.id.write_text);
		mPostTxt = (TextView)view.findViewById(R.id.posts);
		
		TypeFontAssets fontAssets = new TypeFontAssets(getActivity());
		
		mCameraText.setTypeface(fontAssets.regularFont);
		mGalleryText.setTypeface(fontAssets.regularFont);
		mBackgroundText.setTypeface(fontAssets.regularFont);
		mPostTxt.setTypeface(fontAssets.regularFont);
		
		
		
		
		childFrame = (FrameLayout) view.findViewById(R.id.child_frame);
		
		textIcon_camera=(ImageButton)view.findViewById(R.id.img_selection_text_icon1);
		textIcon_gallery=(ImageButton)view.findViewById(R.id.img_selection_text_icon2);
		imageIcon=(ImageButton)view.findViewById(R.id.img_selection_camera_icon1);

		/*
		 * mMainChildFrameLayout = (FrameLayout) findViewById(R.id.child_frame);
		 * mCameraFragmentLayout = (FrameLayout)
		 * findViewById(R.id.camera_frame);
		 */
		/*
		 * progressLayout = (RelativeLayout) findViewById(R.id.loadingPanel);
		 */
		ImageSelectionOnClickListener listener = new ImageSelectionOnClickListener(
				this);
		mCameraLayout.setOnClickListener(listener);
		mGalleryLayout.setOnClickListener(listener);
		mWriteLayout.setOnClickListener(listener);
		showCameraFragment();

		return view;

	}

	@Override
	public void onResume() {
		super.onResume();
		showCameraFragment();
	}

	/**
	 * Show the camera fragment and start the camera
	 */
	public void showCameraFragment() {
		mCameraText.setTextColor(getResources().getColor(R.color.white));
		mCameraLayout.setBackgroundColor(getResources().getColor(
				R.color.apspeak_green));
		mGalleryText.setTextColor(getResources()
				.getColor(R.color.apspeak_green));
		mGalleryLayout.setBackgroundColor(getResources()
				.getColor(R.color.white));
		mBackgroundText
				.setTextColor(getResources().getColor(R.color.apspeak_green));
		mWriteLayout.setBackgroundColor(getResources().getColor(
				R.color.white));
		textIcon_camera.setVisibility(View.VISIBLE);
		imageIcon.setVisibility(View.VISIBLE);
		textIcon_gallery.setVisibility(View.GONE);
		fragment = new CameraFragment(activity);
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		if (fragment != null && transaction != null) {
			transaction.replace(R.id.child_frame, fragment,
					ImageSelectionFragmentActivity.CAMERA_TAG);
			transaction
					.addToBackStack(ImageSelectionFragmentActivity.CAMERA_TAG);
			transaction.commit();
		}
	}

	public void showGallery() {
		mGalleryText.setTextColor(getResources().getColor(R.color.white));
		mGalleryLayout.setBackgroundColor(getResources().getColor(
				R.color.apspeak_green));
		mCameraText.setTextColor(getResources().getColor(R.color.tab_color));
		mCameraLayout.setBackgroundColor(getResources().getColor(
				R.color.white));
		mBackgroundText
				.setTextColor(getResources().getColor(R.color.tab_color));
		mWriteLayout.setBackgroundColor(getResources().getColor(
				R.color.white));
		
		textIcon_camera.setVisibility(View.GONE);
		imageIcon.setVisibility(View.GONE);
		textIcon_gallery.setVisibility(View.VISIBLE);

		fragment = new GalleryFragment(activity);
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		if (fragment != null && transaction != null) {
			transaction.replace(R.id.child_frame, fragment,
					ImageSelectionFragmentActivity.GALLERY_TAG);
			transaction
					.addToBackStack(ImageSelectionFragmentActivity.GALLERY_TAG);
			transaction.commit();

		}

	}

	public void showBackgroundFragment() {
		mBackgroundText.setTextColor(getResources().getColor(R.color.white));
		mWriteLayout.setBackgroundColor(getResources().getColor(
				R.color.apspeak_green));
		mCameraText.setTextColor(getResources().getColor(R.color.tab_color));
		mCameraLayout.setBackgroundColor(getResources().getColor(
				R.color.white));
		mGalleryText.setTextColor(getResources().getColor(R.color.tab_color));
		mGalleryLayout.setBackgroundColor(getResources().getColor(
				R.color.white));
		textIcon_camera.setVisibility(View.GONE);
		imageIcon.setVisibility(View.GONE);
		textIcon_gallery.setVisibility(View.VISIBLE);
		fragment = new BackgroundFragment(activity, cueID);
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();
		if (fragment != null && transaction != null) {
			transaction.replace(R.id.child_frame, fragment,
					ImageSelectionFragmentActivity.BACKGROUND_TAG);
			transaction
					.addToBackStack(ImageSelectionFragmentActivity.BACKGROUND_TAG);
			transaction.commit();

		}
	}

}
