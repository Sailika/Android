package com.example.whirlpool;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.example.fragments.fridge.FridgeContainer;
import com.example.fragments.fridgeoroven.FridgeOrOvenContainer;
import com.example.fragments.homedata.HomeData;
import com.example.fragments.oven.OvenContainer;

public class MainActivity extends FragmentActivity implements OnClickListener{
	private VerticalButton fridgeBtn;
	private VerticalButton ovenBtn;
	private VerticalButton fastSettingsBtn;
	private VerticalButton settingsBtn;
	private LinearLayout homeMenuContentAreaLayout,homeContentAreaLayout;
	private com.example.whirlpool.SlidingDrawerFragment slideDrawer;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;

	private Fragment fragments;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


		setContentView(R.layout.custom_drawer);

		fastSettingsBtn = (VerticalButton) findViewById(R.id.home_fast_settings_menu_btn);
		settingsBtn = (VerticalButton) findViewById(R.id.home_settings_menu_btn);
		ovenBtn = (VerticalButton) findViewById(R.id.home_oven_menu_btn);
		fridgeBtn = (VerticalButton) findViewById(R.id.home_fridge_menu_btn);


		fridgeBtn.setOnClickListener(this);
		ovenBtn.setOnClickListener(this);
		settingsBtn.setOnClickListener(this);
		fastSettingsBtn.setOnClickListener(this);

		homeMenuContentAreaLayout = (LinearLayout) findViewById(R.id.content);
		homeContentAreaLayout = (LinearLayout) findViewById(R.id.home_data_container_layout);

		slideDrawer = (com.example.whirlpool.SlidingDrawerFragment) findViewById(R.id.slider);


		gestureDetector = new GestureDetector(this, new MyGestureDetector());
		gestureListener = new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		};

		fridgeBtn.setOnTouchListener(gestureListener);
		ovenBtn.setOnTouchListener(gestureListener);
		settingsBtn.setOnTouchListener(gestureListener);
		fastSettingsBtn.setOnTouchListener(gestureListener);

		slideDrawer
		.setTrackHandle((View) findViewById(R.id.home_fast_settings_menu_btn));
		slideDrawer
		.setTrackHandle((View) findViewById(R.id.home_fridge_menu_btn));
		slideDrawer
		.setTrackHandle((View) findViewById(R.id.home_oven_menu_btn));
		slideDrawer
		.setTrackHandle((View) findViewById(R.id.home_settings_menu_btn));

		Fragment homeDataFragment = new HomeData();
		FragmentManager homeDataFragmentManager = getSupportFragmentManager();

		homeDataFragmentManager
		.beginTransaction()
		.replace(R.id.home_data_area_container,
				homeDataFragment).commit();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {


		case R.id.home_fridge_menu_btn:

			visibilityofHomeData();

			fragments = new FridgeContainer();

			slideDrawerOpened();

			fridgeButtonSelected();

			break;


		case R.id.home_oven_menu_btn:

			visibilityofHomeData();

			fragments = new OvenContainer();

			slideDrawerOpened();

			ovenButtonSelected();

			break;

		case R.id.home_settings_menu_btn:

			visibilityofHomeData();

			fragments = new FridgeOrOvenContainer();

			slideDrawerOpened();

			settingsButtonSelected();

			break;


		case R.id.home_fast_settings_menu_btn:

			visibilityofHomeData();

			fragments = new FridgeOrOvenContainer();

			slideDrawerOpened();

			fastSettingsButtonSelected();

			break;

		} getSupportFragmentManager().beginTransaction()
		.replace(R.id.home_menu_container, fragments).commit();
	}


	private void visibilityofHomeData() {
		
		homeContentAreaLayout.setVisibility(View.GONE);

	}


	private void slideDrawerOpened() 
	{

		if (!slideDrawer.isOpened()) 
			slideDrawer.animateOpen();
	}


	@SuppressWarnings("deprecation")
	private void settingsButtonSelected() 
	{

		settingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_remove));
		fridgeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		ovenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		fastSettingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));		
	}


	@SuppressWarnings("deprecation")
	private void fridgeButtonSelected() 
	{
		//		homeMenuContentAreaLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_remove));
		fridgeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_remove));
		ovenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		settingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		fastSettingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
	}

	@SuppressWarnings("deprecation")
	private void ovenButtonSelected() 
	{

		ovenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_remove));
		fridgeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		settingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		fastSettingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
	}


	@SuppressWarnings("deprecation")
	private void fastSettingsButtonSelected() 
	{

		fastSettingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_remove));
		fridgeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		settingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
		ovenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
	}



	class MyGestureDetector extends SimpleOnGestureListener {
		@SuppressWarnings("deprecation")
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					slideDrawer.animateOpen();
					fridgeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_remove));
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

					slideDrawer.animateClose();
					homeContentAreaLayout.setVisibility(View.VISIBLE);
					fridgeBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
					settingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
					ovenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
					fastSettingsBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
					return true;
				}
			} catch (Exception e) {
				// nothing
			}

			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}
	}



}