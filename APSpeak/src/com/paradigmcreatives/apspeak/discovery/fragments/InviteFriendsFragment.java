package com.paradigmcreatives.apspeak.discovery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.discovery.listeners.InviteFriendsClickListener;
import com.paradigmcreatives.apspeak.registration.FragmentImplOpenGraphRequest;

public class InviteFriendsFragment extends FragmentImplOpenGraphRequest {

	private UiLifecycleHelper uiHelper;
	private Session session = null;
	private boolean isFBFreshInvite = false;
	private Button fbInviteButton = null;
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state, final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    	//super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.invite_friends_fragment, container, false);
		initView(view);
		return view;

	}

	private void initView(View view) {
		if (view != null) {
			fbInviteButton = (Button) view.findViewById(R.id.fbinvitefriends);
			InviteFriendsClickListener listener = new InviteFriendsClickListener(this);
			fbInviteButton.setOnClickListener(listener);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		Session session = Session.getActiveSession();
		if (session != null) {
			onSessionStateChange(session, session.getState(), null);
		}
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		this.session = session;
		if (state == SessionState.CREATED) {
			isFBFreshInvite = true;
		} else if (state.isOpened()) {
			if (isFBFreshInvite) {
				isFBFreshInvite = false;
				fbInviteButton.performClick();
			}
		} else if (state.isClosed()) {
			// Do nothing
		}
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public void sendLodingTime() {
		// TODO Auto-generated method stub
	}

	@Override
	public void showError(String error, int errorCode) {
		// TODO Auto-generated method stub
	}
}
