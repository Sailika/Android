package com.pcs.implicit;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ImplicitActivity extends Activity implements OnClickListener{

	private Button Contacts;
	private Button Dialer;
	private Button Mail;
	private Button Chrome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity);
		
		Contacts=(Button)findViewById(R.id.contacts);
		Dialer=(Button)findViewById(R.id.dialer);
		Mail=(Button)findViewById(R.id.mail);
		Chrome=(Button)findViewById(R.id.chrome);
		
		Contacts.setOnClickListener(this);
		Dialer.setOnClickListener(this);
		Mail.setOnClickListener(this);
		Chrome.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent();
		switch(v.getId()){
		
		case R.id.contacts :
	       intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("Content://Contacts/People/"));
			startActivity(Intent.createChooser(intent, getResources().getString(R.string.dialog)));
		    break;
		    
		    
		case R.id.dialer :
			 intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:8297397174"));
			startActivity(Intent.createChooser(intent, getResources().getString(R.string.dialog)));
		    break;
		    
		    
		case R.id.mail :
			 intent = new Intent(Intent.ACTION_SENDTO);
			intent.setData(Uri.parse("tel:sailika.07@gmail.com"));
			startActivity(Intent.createChooser(intent, getResources().getString(R.string.dialog)));
		    break;
		    
		    
		case R.id.chrome :
			intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("http://www.google.com"));
			startActivity(Intent.createChooser(intent, getResources().getString(R.string.dialog)));
		    break;   
		    
		default :
			  break;
			  
		}
		
		
		
	}
}
