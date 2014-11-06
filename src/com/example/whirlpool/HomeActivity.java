package com.example.whirlpool;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.contants.FridgeOrOvenContainer;
import com.example.fragments.fridgeoroven.FridgeOrOvenContainerFragment;

public class HomeActivity extends FragmentActivity {
	private VerticalButton fridgeBtn;
	private VerticalButton ovenBtn;
	private VerticalButton fastSettingsBtn;
	private VerticalButton settingsBtn;
	private LinearLayout homeMenuContentAreaLayout;
	private com.example.whirlpool.SlidingDrawer slideDrawer;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;

	private GestureDetector gestureDetector;
	private View.OnTouchListener gestureListener;

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_drawer);
		fastSettingsBtn = (VerticalButton) findViewById(R.id.home_fast_settings_menu_btn);
		settingsBtn = (VerticalButton) findViewById(R.id.home_settings_menu_btn);
		ovenBtn = (VerticalButton) findViewById(R.id.home_oven_menu_btn);
		fridgeBtn = (VerticalButton) findViewById(R.id.home_fridge_menu_btn);
		homeMenuContentAreaLayout = (LinearLayout) findViewById(R.id.content);
		homeMenuContentAreaLayout.setBackgroundColor(Color.GREEN);
		slideDrawer = (com.example.whirlpool.SlidingDrawer) findViewById(R.id.slider);

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
		Fragment fridgeOrOvenContainerFragment = new FridgeOrOvenContainerFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.home_menu_container,
						fridgeOrOvenContainerFragment).commit();
	}

	public void settingsClick(View v) {
		if (!slideDrawer.isOpened()) {
			slideDrawer.animateOpen();
		}
	settingsBtn.setBackground(getResources().getDrawable(R.drawable.removeborder));
	fridgeBtn.setBackground(getResources().getDrawable(R.drawable.button));
	ovenBtn.setBackground(getResources().getDrawable(R.drawable.button));
	fastSettingsBtn.setBackground(getResources().getDrawable(R.drawable.button));
	 settingsBtn.setBackgroundColor(getResources().getColor(R.color.light_gray));

		homeMenuContentAreaLayout.setBackgroundColor(Color.BLUE);
	}

	public void fridgeClick(View v) {
		if (!slideDrawer.isOpened()) {
			slideDrawer.animateOpen();
		}
		Fragment fridgeOrOvenContainerFragment = new FridgeOrOvenContainerFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		Bundle bundle = new Bundle();
		bundle.putString(FridgeOrOvenContainer.FRIDGE_OR_OVEN,
				FridgeOrOvenContainer.FRIDGE);
		fridgeOrOvenContainerFragment.setArguments(bundle);
		fragmentManager
				.beginTransaction()
				.replace(R.id.home_menu_container,
						fridgeOrOvenContainerFragment).commit();

		homeMenuContentAreaLayout.setBackgroundColor(Color.MAGENTA);
		fridgeBtn.setBackground(getResources().getDrawable(R.drawable.removeborder));
		settingsBtn.setBackground(getResources().getDrawable(R.drawable.button));
		ovenBtn.setBackground(getResources().getDrawable(R.drawable.button));
		fastSettingsBtn.setBackground(getResources().getDrawable(R.drawable.button));
		fridgeBtn.setBackgroundColor(getResources().getColor(R.color.light_gray));
	}

	public void ovenClick(View v) {
		if (!slideDrawer.isOpened()) {
			slideDrawer.animateOpen();
		}
		Fragment fridgeOrOvenContainerFragment = new FridgeOrOvenContainerFragment();
		FragmentManager fragmentManager = getSupportFragmentManager();
		Bundle bundle = new Bundle();
		bundle.putString(FridgeOrOvenContainer.FRIDGE_OR_OVEN,
				FridgeOrOvenContainer.OVEN);
		fridgeOrOvenContainerFragment.setArguments(bundle);
		fragmentManager
				.beginTransaction()
				.replace(R.id.home_menu_container,
						fridgeOrOvenContainerFragment).commit();

		homeMenuContentAreaLayout.setBackgroundColor(Color.GRAY);
		ovenBtn.setBackground(getResources().getDrawable(R.drawable.removeborder));
		ovenBtn.setBackgroundColor(getResources().getColor(R.color.light_gray));
		fridgeBtn.setBackground(getResources().getDrawable(R.drawable.button));
		settingsBtn.setBackground(getResources().getDrawable(R.drawable.button));
		fastSettingsBtn.setBackground(getResources().getDrawable(R.drawable.button));
	}

	public void fastSettingsClick(View v) {
		if (!slideDrawer.isOpened()) {
			slideDrawer.animateOpen();
		}

		homeMenuContentAreaLayout.setBackgroundColor(Color.RED);
		fastSettingsBtn.setBackground(getResources().getDrawable(R.drawable.removeborder));
		fridgeBtn.setBackground(getResources().getDrawable(R.drawable.button));
		ovenBtn.setBackground(getResources().getDrawable(R.drawable.button));
		settingsBtn.setBackground(getResources().getDrawable(R.drawable.button));
		fastSettingsBtn.setBackgroundColor(getResources().getColor(R.color.light_gray));
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					slideDrawer.animateOpen();
					return true;
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
					slideDrawer.animateClose();
					fastSettingsBtn.setBackground(getResources().getDrawable(R.drawable.button));
					fridgeBtn.setBackground(getResources().getDrawable(R.drawable.borderless));
					ovenBtn.setBackground(getResources().getDrawable(R.drawable.button));
					settingsBtn.setBackground(getResources().getDrawable(R.drawable.borderless));
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
