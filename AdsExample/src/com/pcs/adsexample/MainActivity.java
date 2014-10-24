package com.pcs.adsexample;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView ad = (AdView) findViewById(R.id.adView);
        ad.loadAd(new AdRequest());
       
       
    }
   
}

