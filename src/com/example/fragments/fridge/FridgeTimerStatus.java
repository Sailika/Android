package com.example.fragments.fridge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.whirlpool.R;

public class FridgeTimerStatus extends Fragment {
	private TextView timer_text;
	private Button timer_dispaly;
	private View view;
	private int i;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 view=inflater.inflate(R.layout.fridge_timer_status, container,false);
		return view;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		
		timer_text=(TextView) view.findViewById(R.id.fridge_timer_state);
		timer_dispaly=(Button)view.findViewById(R.id.timer_triangle_button);
		
		if(timer_text!=null){
			timer_text.setText("Timer,9 minutes left");
			timer_dispaly.setVisibility(View.VISIBLE);
			timer_dispaly.setText("+9");
		}
		else{
			timer_text.setText("No Timer is ON");
		}
		
	}
	
}
