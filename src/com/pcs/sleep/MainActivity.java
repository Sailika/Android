package com.pcs.sleep;
 
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
        
        private Button Download;
        
        private TextView Msg;
        
        private ProgressDialog prgDialog;
      
        public static final int progress_bar = 0;
        static String data_total ;
        
      
 
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            
            Msg=(TextView)findViewById(R.id.msg);
            
            Download = (Button) findViewById(R.id.download);
            
            Download.setOnClickListener(new View.OnClickListener() {
               
                public void onClick(View v) {
                    
                  
                   
                        Toast.makeText(getApplicationContext(), "You CLicked on Download!", Toast.LENGTH_LONG).show();
                        
                        new DownloadfromInternet().execute();
                    
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
 
       
     public   class DownloadfromInternet extends AsyncTask<String, Integer, String> {
 
            // Show Progress bar before downloading 
    	 @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Shows Progress Bar Dialog and then calls doInBackground method
                showDialog(progress_bar);
                
            }
 
           
            protected String doInBackground(String... d_url) {
                String count;
                try {
                    URL url = new URL("https://graph.facebook.com/19292868552");
                    URLConnection conection = url.openConnection();
                    conection.connect();
                   
                    
                    InputStream input = new BufferedInputStream(url.openStream(),10*1024);
                    InputStreamReader isr = new InputStreamReader(input);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder sb= new StringBuilder();
                  
                 
                 
                    while ((count = br.readLine()) != null) {
                       
                        sb.append(count+"\t");
                      
                       publishProgress(50);
                       
                    }
                   data_total = sb.toString();
                   Toast.makeText(getApplicationContext(), data_total, Toast.LENGTH_LONG).show();
                   
                } catch (Exception e) {
                    Log.e("Error: ", e.getMessage());
                }
                return data_total;
                
            }
 
            
            protected void onProgressUpdate(Integer... progress) {
                super.onProgressUpdate(progress);
                prgDialog.setProgress(progress[0]);
            }
 
            
            protected void onPostExecute(String result) {
                // Dismiss the dialog after the  file was downloaded
            	String id=null,username=null,website=null,link=null;
            	
                dismissDialog(progress_bar);
               
                Toast.makeText(getApplicationContext(), "Download complete", Toast.LENGTH_LONG).show();
               try{
            	   
            	   //Getting result to JSON object
            	   JSONObject obj = new JSONObject(result);
            	   if((obj).has("id")){
            		id = obj.getString("id");   
            	   }
            	   if((obj).has("username")){
            		   username= obj.getString("username");
            	   }
            	   if((obj).has("website")){
            		   website=obj.getString("website");
            	   }
            	   if((obj).has("link")){
            		   link=obj.getString("link");
            	   }
            	   //Setting data from url to TextView
            	  Msg.setText("ID: "+id+"\n"+"UserName: "+username+"\n"+"Website: "+website+"\n"+"Link: "+link);
            	   }
            	  catch (Exception e) {
            		  e.printStackTrace();
            	  }
				
       
            }
     }
}
 
     