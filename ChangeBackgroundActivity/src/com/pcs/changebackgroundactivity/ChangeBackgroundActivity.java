package com.pcs.changebackgroundactivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ChangeBackgroundActivity extends Activity implements OnClickListener{
	private Button sachinBtn;
	private Button sehwagBtn;
	private Button gangulyBtn;
	private Button dravidBtn;
	private Button rohitBtn;
	private Button dhawanBtn;
	private Button kohliBtn;
	private Button dhoniBtn;
	private Button bhuviBtn;
	private Button jadejaBtn;
	private View main_view;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.background);
		sachinBtn = (Button)findViewById(R.id.sachin);
		sehwagBtn = (Button)findViewById(R.id.sehwag);
		dravidBtn = (Button)findViewById(R.id.dravid);
		gangulyBtn= (Button)findViewById(R.id.ganguly);
		dhawanBtn = (Button)findViewById(R.id.dhawan);
		rohitBtn =  (Button)findViewById(R.id.rohit);
		kohliBtn = (Button)findViewById(R.id.kohli);
		bhuviBtn =(Button)findViewById(R.id.bhuvi);
		dhoniBtn = (Button)findViewById(R.id.dhoni);
		jadejaBtn = (Button)findViewById(R.id.jadeja);
		main_view = (View)findViewById(R.id.main);
		sachinBtn.setOnClickListener(this);
		sehwagBtn.setOnClickListener(this);
		gangulyBtn.setOnClickListener(this);
		dravidBtn.setOnClickListener(this);
		rohitBtn.setOnClickListener(this);
		dhawanBtn.setOnClickListener(this);
		kohliBtn.setOnClickListener(this);
		dhoniBtn.setOnClickListener(this);
		bhuviBtn.setOnClickListener(this);
		jadejaBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		v.getId();
		switch(v.getId())
		{
		case R.id.sachin:
			main_view.setBackgroundResource(R.drawable.sachin);
			sachinBtn.setTextColor(Color.BLUE);
			break;
		case R.id.sehwag:
			main_view.setBackgroundResource(R.drawable.sehwag);
			sehwagBtn.setTextColor(Color.CYAN);
			break;
		case R.id.ganguly:
			main_view.setBackgroundResource(R.drawable.ganguli);
			gangulyBtn.setTextColor(Color.DKGRAY);
			break;
		case R.id.dravid:
			main_view.setBackgroundResource(R.drawable.dravid);
			dravidBtn.setTextColor(Color.GRAY);
			break;
		case R.id.kohli:
			main_view.setBackgroundResource(R.drawable.kohli);
			kohliBtn.setTextColor(Color.MAGENTA);
			break;
		case R.id.rohit:
			main_view.setBackgroundResource(R.drawable.rohit);
			rohitBtn.setTextColor(Color.RED);
			break;
		case R.id.dhawan:
			main_view.setBackgroundResource(R.drawable.dhawan);
			dhawanBtn.setTextColor(Color.YELLOW);
			break;
		case R.id.dhoni:
			main_view.setBackgroundResource(R.drawable.dhoni);
			dhoniBtn.setTextColor(Color.WHITE);
			break;
		case R.id.jadeja:
			main_view.setBackgroundResource(R.drawable.jadeja);
			jadejaBtn.setTextColor(Color.LTGRAY);
			break;
		case R.id.bhuvi:
			main_view.setBackgroundResource(R.drawable.bhuvi);
			bhuviBtn.setTextColor(Color.TRANSPARENT);
			break;
		default:
			break;
		}
	}

}

