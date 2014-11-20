package com.paradigmcreatives.apspeak.registration.listeners;

import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.paradigmcreatives.apspeak.discovery.fragments.SettingsFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectAnimationFragment;
import com.paradigmcreatives.apspeak.registration.fragments.FacebookConnectFragment;

/**
 * Listener for DONE action on search in groups list.
 * 
 * @author Dileep | neuv
 */
public class GroupsSearchActionListenerImpl implements OnEditorActionListener {

    private static final String TAG = "GroupsSearchActionListenerImpl";

    private Fragment fragment;

    public GroupsSearchActionListenerImpl(final Fragment fragment) {
	super();
	this.fragment = fragment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.TextView.OnEditorActionListener#onEditorAction(android.widget.TextView, int,
     * android.view.KeyEvent)
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	if (fragment != null) {
	    if (actionId == EditorInfo.IME_ACTION_DONE) {
		if (fragment instanceof FacebookConnectFragment) {
		    ((FacebookConnectFragment) fragment).removeGroupsListView();
		} else if (fragment instanceof FacebookConnectAnimationFragment) {
		    ((FacebookConnectAnimationFragment) fragment).removeGroupsListView();
		} else if (fragment instanceof SettingsFragment) {
		    ((SettingsFragment) fragment).removeGroupsListView();
		}
	    }// else do nothing
	}
	return false;
    }
}