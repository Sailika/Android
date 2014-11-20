package com.paradigmcreatives.apspeak.discovery.handlers;

import java.util.Collection;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;

public class UserNetworkHandler extends Handler {

	private UserNetworkFragment fragment = null;
	private static final int STARTING = 0;
	private static final int SUCCESS = 1;
	private static final int ERROR = 2;

	private ProgressDialog progressDialog;
	private UserNetwork network;
	private int offset;

	public UserNetworkHandler(UserNetworkFragment fragment, boolean showDialog) {
		this.fragment = fragment;
		if(showDialog){
						progressDialog = new ProgressDialog(fragment.getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
						progressDialog.setTitle("Searching..");
						progressDialog.setMessage("Please wait...");
						progressDialog.setCancelable(false);
		}
	}

	public UserNetworkHandler(UserNetworkFragment fragment, boolean showDialog,
			UserNetwork network, int offset) {
		this(fragment, showDialog);
		this.network = network;
		this.offset = offset;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
		if (msg != null) {
			switch (msg.what) {
			case STARTING:
				if (progressDialog != null) {
					progressDialog.show();
				}
				break;
			case SUCCESS:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if (fragment != null && msg.obj instanceof HashMap<?, ?>) {
					if (this.network == UserNetwork.FACEBOOK_FRIENDS) {
						fragment.refreshBatchedListView(
								(HashMap<UserNetwork, Collection<Friend>>) msg.obj,
								this.network, this.offset);
					} else {
						fragment.refreshListView((HashMap<UserNetwork, Collection<Friend>>) msg.obj);
					}
				}
				break;
			case ERROR:
				if (progressDialog != null) {
					progressDialog.dismiss();
					;
				}
				fragment.showError((String) msg.obj);
				break;
			}
		}
	}

	public void onSuccess(HashMap<UserNetwork, Collection<Friend>> friends) {
		Message msg = new Message();
		msg.what = SUCCESS;
		msg.obj = friends;
		sendMessage(msg);
	}

	public void onError(String error) {
		Message msg = new Message();
		msg.what = ERROR;
		msg.obj = error;
		sendMessage(msg);
	}

	public void onStarting() {
		sendEmptyMessage(STARTING);
	}

}
