package com.paradigmcreatives.apspeak.registration.adapters;
/*package com.paradigmcreatives.apspeak.registration.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;

public class PromotionalScreenAdapter extends PagerAdapter {

    private FacebookConnectFragment fragment = null;

    public PromotionalScreenAdapter(FacebookConnectFragment fragment) {
	this.fragment = fragment;
    }

    private static final int TOTAL_ITEMS = 3;
    private int[] pics = { R.drawable.welcomescreen_1, R.drawable.welcomescreen_2, R.drawable.welcomescreen_3 };
    //private int[] indicators = { R.drawable.whatsay_launcher_icon, R.drawable.whatsay_launcher_icon, R.drawable.whatsay_launcher_icon };

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return TOTAL_ITEMS;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
	return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(View collection, int position) {
	// ImageView view = new ImageView(fragment.getActivity());
	// view.setImageResource(pics[position]);
	//
	// ((ViewPager) collection).addView(view, 0);
	//
	// return view;

	ImageView imgDisplay;
	//ImageView indicator;

	LayoutInflater inflater = (LayoutInflater) fragment.getActivity().getSystemService(
		Context.LAYOUT_INFLATER_SERVICE);

	View viewLayout = inflater.inflate(R.layout.viewpageritem, (ViewGroup) collection, false);

	imgDisplay = (ImageView) viewLayout.findViewById(R.id.welcomescreen_image);
	//indicator = (ImageView) viewLayout.findViewById(R.id.welcomescreen_indicator);

	imgDisplay.setImageResource(pics[position]);
	//indicator.setImageResource(indicators[position]);

	((ViewPager) collection).addView(viewLayout);

	return viewLayout;

    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
	((ViewPager) collection).removeView((LinearLayout) view);
    }

    @Override
    public void finishUpdate(View arg0) {
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public Parcelable saveState() {
	return null;
    }

    @Override
    public void startUpdate(View arg0) {
    }

}
*/