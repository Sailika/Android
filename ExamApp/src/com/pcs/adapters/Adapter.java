package com.pcs.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pcs.examapp.R;
import com.pcs.model.WeatherData;
public class Adapter extends BaseAdapter{
	
	private ArrayList<WeatherData> weather;
	public Context context;
	public LayoutInflater inflater;

	public Adapter() {
		// TODO Auto-generated constructor stub
	}

	public Adapter(Context context,ArrayList<WeatherData> weather){
		this.context=context;
		this.weather= weather;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return weather.size();
	}

	@Override
	public Object getItem(int position) {
		return weather.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		//Inflate the View
		convertView = inflater.inflate(R.layout.list_view, null);
		
		//Get the TextView from inflated XML
		TextView city = (TextView)convertView.findViewById(R.id.city);
		TextView temp = (TextView)convertView.findViewById(R.id.temp);
		TextView temp_min = (TextView)convertView.findViewById(R.id.temp_min);
		TextView temp_max = (TextView)convertView.findViewById(R.id.temp_max);
		TextView humidity = (TextView)convertView.findViewById(R.id.humidity);

		//Setting Text to the TextView's
		city.setText(weather.get(position).getCity());
		temp.setText(weather.get(position).getTemp());
		temp_max.setText(weather.get(position).getMax_temp());
		temp_min.setText(weather.get(position).getMin_temp());
		humidity.setText(weather.get(position).getHumidity());


		return convertView;
	}

}
