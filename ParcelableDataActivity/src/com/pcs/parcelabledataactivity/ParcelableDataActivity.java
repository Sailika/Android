package com.pcs.parcelabledataactivity;

import com.pcs.constants.Constants;
import com.pcs.model.AndroidVersions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ParcelableDataActivity extends Activity{
	private Button AndroidBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				setContentView(R.layout.parcelable);
				AndroidBtn = (Button)findViewById(R.id.btn);
				AndroidBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ParcelableDataActivity.this,RecieverDataActivity.class);
						AndroidVersions android_versions = new AndroidVersions();
						android_versions.setFirstversion(getResources().getString(R.string.first));
						android_versions.setSecondversion(getResources().getString(R.string.second));
						android_versions.setThirdversion(getResources().getString(R.string.third));
						android_versions.setFourthversion(getResources().getString(R.string.fourth));
						android_versions.setFifthversion(getResources().getString(R.string.fifth));
						android_versions.setSixthversion(getResources().getString(R.string.sixth));
						android_versions.setSeventhversion(getResources().getString(R.string.seventh));
						android_versions.setEigthversion(getResources().getString(R.string.eigth));
						android_versions.setNinthversion(getResources().getString(R.string.ninth));
						android_versions.setTenthversion(getResources().getString(R.string.tenth));
						android_versions.setEleventhversion(getResources().getString(R.string.eleventh));
						intent.putExtra(Constants.IntentExtras.ANDROID, android_versions);
						startActivity(intent);
					}
				});
				
	}

}
