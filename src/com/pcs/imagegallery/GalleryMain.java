package com.pcs.imagegallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.pcs.adapters.GalleryAdapter;

public class GalleryMain extends Activity {
	private ListView list;
	private GalleryAdapter adapter;
	private ImageView img;
	private Button btnDownload;

	private String[] urlArray = new String[] {
			"http://www.buynetbookcomputer.com/android-netbook-images/android-netbook-big.jpg",
			"http://www.sharepointhoster.com/wp-content/uploads/2011/03/android_apps.jpeg",
		"http://www.androidbegin.com/wp-content/uploads/2013/07/HD-Logo.gif",	
	"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRMJHwZzfuBMa5NtXcucLWQZZzcn2caift9WG7M20cAX9_vTZx",
	"http://www.9ori.com/store/media/images/8ab579a656.jpg",
	"http://api.androidhive.info/images/sample.jpg"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		img = (ImageView) findViewById(R.id.img);
		btnDownload = (Button) findViewById(R.id.btn);

		btnDownload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				list = (ListView) findViewById(R.id.list);

				adapter = new GalleryAdapter(GalleryMain.this,
						R.layout.imagelist, urlArray);

				list.setAdapter(adapter);
				list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						Toast.makeText(getApplicationContext(),
								"Position: " + position + "",
								Toast.LENGTH_SHORT).show();
						img.setImageBitmap(adapter.imgs.get(position));
					}
				});

			}
		});

	}
	@Override
	   public boolean onCreateOptionsMenu(Menu menu) {
	      // Inflate the menu; this adds items to the action bar if it is present.
	      getMenuInflater().inflate(R.menu.main, menu);
	      return true;
	   }

	   public boolean onOptionsItemSelected(MenuItem item) 
	   { 
	   super.onOptionsItemSelected(item); 
	      switch(item.getItemId()) 
	      { 
	      case R.id.zoomInOut:
	         ImageView image = (ImageView)findViewById(R.id.img);
	         Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom);
	         image.startAnimation(animation);
	            return true;
	      case R.id.rotate360:
	        ImageView image1 = (ImageView)findViewById(R.id.img);
	        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
	        image1.startAnimation(animation1);
	            return true;
	      case R.id.fadeInOut:
	        ImageView image2 = (ImageView)findViewById(R.id.img);
	        Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
	        image2.startAnimation(animation2);
	           return true;


	      }
	      return false;
	   }
}
