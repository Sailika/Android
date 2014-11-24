package com.paradigmcreatives.apspeak.app.invite.fragments;

import java.util.ArrayList;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.invite.listeners.InviteButtonsClickListeners;
import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.feed.adapters.MyFeedAdapter;

public class InviteFriendsFragment extends Fragment {
	
	private ArrayList<MyFeedBean> myFeedBeanList;
	private ProgressBar progressBar;
	private String userID = null;
	private String offset;
	private String count;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView listView;
	private MyFeedAdapter adapter;

	private String mAnnouncementId;
	private String mAnnouncementMessage;

	private Dialog mAnnouncementDialog;
	private LinearLayout mAnnouncementLayout;
	private ProgressBar mAnnouncementProgressBar;
	private WebView mAnnouncementWebView;
	private Button contactsBtn, emailBtn, twitterBtn, facebookBtn, whatsappBtn;
	
	private final String SAVE_FEED = "feed";
	private final String SAVE_USERID = "userid";
	private final String SAVE_OFFSET = "offset";
	private final String SAVE_COUNT = "count";

	/**
	 * Default constructor
	 */
	public InviteFriendsFragment() {
		super();
	}

	public InviteFriendsFragment(String userID, String offset, String count) {
		super();
		this.userID = userID;
		this.offset = offset;
		this.count = count;
	}

	public InviteFriendsFragment(String userID, String offset, String count,
			String announcementId, String annoucementMessage) {
		this(userID, offset, count);
		this.mAnnouncementId = announcementId;
		this.mAnnouncementMessage = annoucementMessage;
	}

	/**
	 * Called when the Fragment is first created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.my_feed_new, container, false);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(SAVE_USERID)) {
				userID = savedInstanceState.getString(SAVE_USERID);
			}
			if (savedInstanceState.containsKey(SAVE_OFFSET)) {
				offset = savedInstanceState.getString(SAVE_OFFSET);
			}
			if (savedInstanceState.containsKey(SAVE_COUNT)) {
				count = savedInstanceState.getString(SAVE_COUNT);
			}
			if (savedInstanceState.containsKey(SAVE_FEED)) {
				myFeedBeanList = savedInstanceState
						.getParcelableArrayList(SAVE_FEED);
			}
		}

		initUI(view);

		// If no user ID is supplied then show the profile of the user who is
		// signed in
		if (TextUtils.isEmpty(userID)) {
			userID = AppPropertiesUtil.getUserID(getActivity());

		} else {
			if (TextUtils.equals(userID,
					AppPropertiesUtil.getUserID(getActivity()))) {

			}
		}

		

			
		
		return view;
	}// end onCreateView

	

	

	private void initUI(View rootView) {

		contactsBtn = (Button) rootView.findViewById(R.id.invite_contacts);
		emailBtn = (Button) rootView.findViewById(R.id.invite_email);
		facebookBtn = (Button) rootView.findViewById(R.id.invite_facebook);
		twitterBtn = (Button) rootView.findViewById(R.id.invite_twitter);
		whatsappBtn = (Button) rootView.findViewById(R.id.invite_whatsap);
		InviteButtonsClickListeners inviteBtnListener = new InviteButtonsClickListeners(this.getActivity());
		contactsBtn.setOnClickListener(inviteBtnListener);
		emailBtn.setOnClickListener(inviteBtnListener);
		facebookBtn.setOnClickListener(inviteBtnListener);
		twitterBtn.setOnClickListener(inviteBtnListener);
		whatsappBtn.setOnClickListener(inviteBtnListener);
		
		
	}

	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			if (!TextUtils.isEmpty(userID)) {
				outState.putString(SAVE_USERID, userID);
			}
			if (!TextUtils.isEmpty(offset)) {
				outState.putString(SAVE_OFFSET, offset);
			}
			if (!TextUtils.isEmpty(count)) {
				outState.putString(SAVE_COUNT, count);
			}
			if (myFeedBeanList != null) {
				outState.putParcelableArrayList(SAVE_FEED, myFeedBeanList);
			}
		}
		super.onSaveInstanceState(outState);
	}

	


}
