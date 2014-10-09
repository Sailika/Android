package com.pcs.examapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pcs.model.WeatherData;

public class CityActivity extends Activity{

	public static String data_total ;
	public ListView list;
	public ArrayList<WeatherData> weather;

	public EditText city_edt;
	public TextView display_txt;
	public Button get_data;
	public ProgressDialog prgDialog;
	public static final int progress_bar = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_activity);

		city_edt = (EditText)findViewById(R.id.city_edt);
		list = (ListView)findViewById(R.id.display_list);
		get_data = (Button)findViewById(R.id.getdata);
		get_data.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Call for AsyncTask
				new Download(CityActivity.this,list).execute();
			}
		});

	}

	// Show Dialog Box with Progress bar
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar:
			prgDialog = new ProgressDialog(this);
			prgDialog.setMessage("Downloading. Please wait...");
			prgDialog.setIndeterminate(false);
			prgDialog.setMax(100);
			prgDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			prgDialog.setCancelable(false);
			prgDialog.show();
			return prgDialog;
		default:
			return null;
		}
	}

	public class Download extends AsyncTask<String, Integer, String>{
		Context mcontext;
		ListView list;
		public Download(CityActivity cityActivity, ListView list) {
			mcontext = cityActivity;
			this.list=list;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar); //Calls CreateDialog method
		}


		@Override
		protected String doInBackground(String... params) {


			try {

				//Give the URL
				URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city_edt.getText().toString()+"&mode=json");
				
				//Open Connection
				URLConnection connection = url.openConnection();


				InputStream input = new BufferedInputStream(url.openStream(),10*1024);
				InputStreamReader isr = new InputStreamReader(input);
				BufferedReader br = new BufferedReader(isr);
				StringBuilder sb= new StringBuilder();



				String count;
				while ((count = br.readLine()) != null) {

					sb.append(count);

					publishProgress(50);

				}
				data_total = sb.toString();
				Toast.makeText(getApplicationContext(), data_total, Toast.LENGTH_LONG).show();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			return data_total;

		}
		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			//Setting the progress in progress
			prgDialog.setProgress(progress[0]);

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			dismissDialog(progress_bar);

			Toast.makeText(getApplicationContext(), "Download complete", Toast.LENGTH_LONG).show();

			try{
				//Initilaizing the ArrayList and Adapter
				weather = new ArrayList<WeatherData>();
				com.pcs.adapters.Adapter adapter = new com.pcs.adapters.Adapter(CityActivity.this, weather);
				
				//Create new Weather data object
				WeatherData data = new WeatherData();

				//Getting result to JSON object
				JSONObject obj = new JSONObject(result);



				if((obj).has("name")){
					data.setCity("City :           "+obj.getString("name"));
				}

				JSONObject object = obj.getJSONObject("main");
				if((object).has("temp")){

					data.setTemp("Temp   :         "+object.getString("temp"));

				}
				if((object).has("humidity")){

					data.setHumidity("humidity :   "+object.getString("humidity"));
				}
				if((object).has("temp_min")){

					data.setMin_temp("Temp-min :   "+object.getString("temp_min"));
				}
				if((object).has("temp_max")){

					data.setMax_temp("Temp-Max :   "+object.getString("temp_max"));
				}
				weather.add(data);
				list.setAdapter(adapter);				

			}
			catch (Exception e) {
				e.printStackTrace();
			}



		}
	}
}
