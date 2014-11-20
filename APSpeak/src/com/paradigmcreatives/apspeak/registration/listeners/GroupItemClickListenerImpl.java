package com.paradigmcreatives.apspeak.registration.listeners;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.discovery.fragments.SettingsFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;

/**
 * Listener for group list items.
 * 
 * @author Dileep | neuv
 */
public class GroupItemClickListenerImpl implements OnItemClickListener {

    private Fragment mFragment;

    public GroupItemClickListenerImpl(final Fragment fragment) {
	super();
	this.mFragment = fragment;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long arg3) {
	if (mFragment != null) {
	    GroupBean group = (GroupBean) parent.getItemAtPosition(position);
	    if (mFragment instanceof FacebookConnectFragment) {
		((FacebookConnectFragment) mFragment).setGroup(group);
	    } else if (mFragment instanceof FacebookConnectAnimationFragment) {
		((FacebookConnectAnimationFragment) mFragment).setGroup(group);
	    }else if (mFragment instanceof SettingsFragment) {
		((SettingsFragment) mFragment).setGroup(group);
	    }
	}
    }
}