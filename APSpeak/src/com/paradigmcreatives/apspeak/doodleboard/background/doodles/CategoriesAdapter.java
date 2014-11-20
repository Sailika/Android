package com.paradigmcreatives.apspeak.doodleboard.background.doodles;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

//import com.neuv.doodlydoo.R;

public class CategoriesAdapter extends BaseAdapter {

    private ArrayList<String> categories;
    private Context context;

    public CategoriesAdapter(Context context, ArrayList<String> categories) {
	this.categories = categories;
	this.context = context;
    }

    @Override
    public int getCount() {
	if (categories != null) {
	    return categories.size();
	}

	return 0;
    }

    @Override
    public Object getItem(int pos) {
	if (categories != null && categories.size() > pos) {
	    return categories.get(pos);
	}

	return null;
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	if (convertView == null) {
	    // convertView = LayoutInflater.from(context).inflate(R.layout.background_doodle_list_item, null);
	}
	return null;
    }

}
