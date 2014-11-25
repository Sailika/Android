package com.paradigmcreatives.apspeak.navdrawer.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;

public class NavDrawerAdapter extends BaseAdapter{

	private Context context;

    private ArrayList<NavDrawerItem> navDrawerItems;
    public NavDrawerAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){

        this.context = context;

        this.navDrawerItems = navDrawerItems;

    }

    @Override

    public int getCount() {

        return navDrawerItems.size();

    }

    @Override

    public Object getItem(int position) {   

        return navDrawerItems.get(position);

    }

    @Override

    public long getItemId(int position) {

        return position;

    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater mInflater = (LayoutInflater)

                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.nav_drawer_list_item, null);

        }

       

       // ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);


       // imgIcon.setImageResource(navDrawerItems.get(position).getIcon());    

        txtTitle.setText(navDrawerItems.get(position).getTitle());  
        Typeface myTypeface = Typeface.createFromAsset(context.getAssets(),
				"Roboto-Light.ttf");
		
		txtTitle.setTypeface(myTypeface);



        
        return convertView;

    }

}
