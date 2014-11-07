package com.example.fragments.fridge;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adapters.CustomListAdapter;
import com.example.model.FridgeStatusItems;
import com.example.whirlpool.R;

public class FridgeStatus extends Fragment {

	private ListView fridgeStatusList;
	private LinearLayout listLayout;

	private List<FridgeStatusItems> fridge_status_list;
	private CustomListAdapter mAdapter;
	private String[] headings;
	private String[] state;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fridge_status, container, false);

		return view;
	}

	 @Override
	 public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	 super.onActivityCreated(savedInstanceState);
	 listLayout = (LinearLayout) view.findViewById(R.id.listLayout);
	 fridgeStatusList = (ListView) view.findViewById(
	 R.id.fridge_list);
	 if (fridgeStatusList != null) {
	
	 headings = getResources().getStringArray(R.array.headings);
	 state = getResources().getStringArray(R.array.states);
	
	 fridge_status_list = new ArrayList<FridgeStatusItems>();
	
	 for (int i = 0; i < headings.length; i++) {
	 FridgeStatusItems items = new FridgeStatusItems(headings[i],state[i]);
	 fridge_status_list.add(items);
	 }
	 mAdapter = new CustomListAdapter(getActivity(), fridge_status_list);
	 // setListAdapter(mAdapter);
	 fridgeStatusList.setAdapter(mAdapter);
	 } else {
	 Toast.makeText(getActivity(), "Not Loaded", Toast.LENGTH_SHORT)
	 .show();
	 }
	
	 }

}
