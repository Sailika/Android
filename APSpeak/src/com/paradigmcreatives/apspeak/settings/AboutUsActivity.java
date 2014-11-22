package com.paradigmcreatives.apspeak.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.textstyles.TypeFontAssets;

public class AboutUsActivity extends Fragment {

	private TextView aboutUsTxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.about_us_layout, null);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		aboutUsTxt = (TextView) view.findViewById(R.id.about_us_text);

		TypeFontAssets fontAssets = new TypeFontAssets(getActivity());

		aboutUsTxt.setTypeface(fontAssets.lightFont);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AppEventsLogger.activateApp(getActivity(), Constants.FACEBOOK_APPID);
	}

}
