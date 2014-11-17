package com.pcs.apspeak;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.IInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pcs.adapter.CustomGridAdapter;
import com.pcs.model.FeedBackItems;

public class Ideas extends Fragment {
	private GridView itemsList;
	private LinearLayout homeFragmnet;
	private LinearLayout gridLayout;

//	private ImageView expandedImage;
	private LinearLayout expandedLayout;

	private TypedArray images; // Array for image id's

	private List<FeedBackItems> mItems; // GridView items list
	private CustomGridAdapter mAdapter; // GridView adapter

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View feedback = inflater.inflate(R.layout.ideas_fragment, container,
				false);
		return feedback;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		super.onViewCreated(view, savedInstanceState);

		// initialize the items list
		mItems = new ArrayList<FeedBackItems>();
		Resources resources = getResources();

		images = resources.obtainTypedArray(R.array.drawables);
		for (int i = 0; i < images.length(); i++) {
			mItems.add(new FeedBackItems(images.getResourceId(i, -1), 212));

		}
		// initialize the adapter
		mAdapter = new CustomGridAdapter(getActivity(), mItems);
		// initialize the GridView
		itemsList = (GridView) view.findViewById(R.id.grid);
		// initialize layout values
		 gridLayout=(LinearLayout)view.findViewById(R.id.grid_layout);
		 expandedLayout=(LinearLayout)view.findViewById(R.id.image_click);

		itemsList.setAdapter(mAdapter);
		itemsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			   // Send intent to SingleViewActivity

//				FragmentActivity singleView = new SingleViewActivity();
//				android.app.FragmentManager fm =getFragmentManager();
//				FragmentTransaction ft = fm.beginTransaction();
//				ft.replace(R.id.fragment_container, singleView);
//				ft.commit();
	              Intent intent = 
	              new Intent(getActivity(), SingleViewActivity.class);
	              // Pass image index
	              intent.putExtra("id", position);
	              startActivity(intent);
			}
			
		});

		
		

	}

}
