package com.example.fragments.fridge;

import com.example.whirlpool.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FridgeWarningStatus extends Fragment {
	private TextView warningText;
	private Button warningButton;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view= inflater.inflate(R.layout.fridge_warning_status, container);
		return view;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
		warningButton=(Button)view.findViewById(R.id.warnings_triangle_button);
		warningText=(TextView)view.findViewById(R.id.fridge_warnings_state);
		
		if(warningText!=null){
			warningButton.setVisibility(View.VISIBLE);
			warningButton.setText("+2");
			warningText.setText("You have 2 warning Message");
		}
		else{
			warningButton.setVisibility(View.GONE);
			warningText.setText("No Warnings");
		}
	}

}
