package com.paradigmcreatives.apspeak.feed.fragments;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.announcements.handlers.GetAnnouncementContentHandler;
import com.paradigmcreatives.apspeak.announcements.tasks.GetAnnouncementContentThread;
import com.paradigmcreatives.apspeak.app.model.MyFeedBean;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.feed.adapters.MyFeedAdapter;
import com.paradigmcreatives.apspeak.feed.handlers.MyFeedHandler;
import com.paradigmcreatives.apspeak.feed.listeners.MyFeedListClickListener;
import com.paradigmcreatives.apspeak.feed.tasks.MyFeedThread;
import com.paradigmcreatives.apspeak.home.AppNewHomeActivity;
import com.paradigmcreatives.apspeak.logging.Logger;

public class MyFeedFragment extends Fragment {

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
	
	private final String SAVE_FEED = "feed";
	private final String SAVE_USERID = "userid";
	private final String SAVE_OFFSET = "offset";
	private final String SAVE_COUNT = "count";

	/**
	 * Default constructor
	 */
	public MyFeedFragment() {
		super();
	}

	public MyFeedFragment(String userID, String offset, String count) {
		super();
		this.userID = userID;
		this.offset = offset;
		this.count = count;
	}

	public MyFeedFragment(String userID, String offset, String count,
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

		View view = inflater.inflate(R.layout.my_feed, container, false);

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

		if (myFeedBeanList != null && myFeedBeanList.size() > 0) {
			// update the adapter
			setAdapter(myFeedBeanList);
		} else {
			// Show spinner
			if (progressBar != null
					&& (progressBar.getVisibility() == View.INVISIBLE || progressBar
							.getVisibility() == View.GONE)) {
				progressBar.setVisibility(View.VISIBLE);
			}

			fetchMyFeedFromServer();
		}

		if (!TextUtils.isEmpty(mAnnouncementId)) {
			showAnnouncementContentDialog(constructAnnouncementContentView());
			fetchAnnouncementContentFromServer(mAnnouncementId);
		}
		return view;
	}// end onCreateView

	/**
	 * Sets adapter of MyFeedBean objects to the list view
	 * 
	 * @param myFeedBeanList
	 */
	public synchronized void setAdapter(ArrayList<MyFeedBean> myFeedBeanList) {
		if ((listView != null) && myFeedBeanList != null
				&& myFeedBeanList.size() > 0 && isAdded()) {
			adapter = new MyFeedAdapter(this, myFeedBeanList);

			if (listView != null) {
				listView.setAdapter(adapter);
				// listView.setOnScrollListener(mScrollListener);
			}

			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Gets called when my feed is failed in server
	 * 
	 * @param offset
	 * @param count
	 */
	public void getMyFeedFailed(String offset, String count) {
		/*
		 * Handle the failure by changing respective relationship
		 * icon/count/details,etc
		 */
		// sending love functionality failure info to Google Analytics.
		/*
		 * TODO: Uncomment for Google Analytics
		 * GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
		 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
		 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
		 * GoogleAnalyticsConstants.STREAM_LOVE_FAILURE_BUTTON);
		 */
	}

	private void initUI(View rootView) {

		progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
		mPullToRefreshListView = (PullToRefreshListView) rootView
				.findViewById(R.id.lv_my_feed);
		mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
		mPullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						fetchMyFeedFromServer();
					}
				});
		listView = mPullToRefreshListView.getRefreshableView();
		listView.setOnItemClickListener(new MyFeedListClickListener(
				getActivity(), this));
	}

	public void reloadFeed() {
		/*
			if (myFeedBeanList != null && myFeedBeanList.size() > 0) {
				// Don't show spinner
				if (progressBar != null) {
					progressBar.setVisibility(View.INVISIBLE);
				}
			} else {
				// Show spinner
				if (progressBar != null
						&& (progressBar.getVisibility() == View.INVISIBLE || progressBar
								.getVisibility() == View.GONE)) {
					progressBar.setVisibility(View.VISIBLE);
				}
			}
			fetchMyFeedFromServer();
			*/
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

	/**
	 * Gets called to update my feed details
	 * 
	 * @param myFeedBeanList
	 */
	public void setMyFeedFetchStatus(ArrayList<MyFeedBean> myFeedBeanList) {
		// If my feed details are fetched then update UI
		if (isAdded()) {
			if (myFeedBeanList != null) {
				this.myFeedBeanList = myFeedBeanList;

				if (listView != null && myFeedBeanList.size() != 0) {
					adapter = new MyFeedAdapter(this, myFeedBeanList);

					if (listView != null) {
						listView.setAdapter(adapter);

					}

					adapter.notifyDataSetChanged();
				}

			} else {
				// do nothing
			}
			if (progressBar != null) {
				progressBar.setVisibility(View.GONE);
			}
			mPullToRefreshListView.onRefreshComplete();
			if(getActivity() instanceof AppNewHomeActivity){
				// Reset notifications count value
				AppPropertiesUtil.setNotificationsCount(getActivity(), 0);
				// Update UI too
				((AppNewHomeActivity)getActivity()).showHideNotificationsCount();
			}
		}
	}

	public void setMyFeedFetchError() {
		mPullToRefreshListView.onRefreshComplete();
	}

	/**
	 * Fetches Announcement's content from server and shows the same on a
	 * webview
	 * 
	 * @param announcementId
	 */
	public void fetchAnnouncementContentFromServer(String announcementId) {
		mAnnouncementId = announcementId;
		GetAnnouncementContentHandler handler = new GetAnnouncementContentHandler(
				this);
		GetAnnouncementContentThread thread = new GetAnnouncementContentThread(
				handler, mAnnouncementId);
		thread.start();
	}

	public void setAnnouncementContentSuccess(String announcementId,
			String announcementContent) {
		if (!TextUtils.isEmpty(announcementId)
				&& !TextUtils.isEmpty(mAnnouncementId)
				&& announcementId.equals(mAnnouncementId)) {
			if (!TextUtils.isEmpty(announcementContent)) {
				showAnnouncement(announcementContent);
			} else {
				setAnnouncementContentFailed(announcementId);
			}
		}
	}

	public void setAnnouncementContentFailed(String announcementId) {
		if (!TextUtils.isEmpty(announcementId)
				&& !TextUtils.isEmpty(mAnnouncementId)
				&& announcementId.equals(mAnnouncementId)) {
			showAnnouncement(mAnnouncementMessage);
		}
	}

	private void showAnnouncement(String announcementContent) {
		final String mimeType = "text/html";
		final String encoding = "UTF-8";
		if (mAnnouncementWebView != null
				&& !TextUtils.isEmpty(announcementContent)) {
			if (mAnnouncementDialog != null && mAnnouncementDialog.isShowing()) {
				if (mAnnouncementProgressBar != null) {
					mAnnouncementProgressBar.setVisibility(View.INVISIBLE);
				}
				mAnnouncementWebView.loadDataWithBaseURL("",
						announcementContent, mimeType, encoding, "");
			}
		} else {
			removeAnnouncementContentDialog();
		}
	}

	/**
	 * Removes the announcement content dialog
	 */
	public void removeAnnouncementContentDialog() {
		if (mAnnouncementDialog != null && mAnnouncementDialog.isShowing()) {
			mAnnouncementDialog.dismiss();
		}
	}

	/**
	 * Add announcement content dialog to the screen
	 * 
	 * @param countryListView
	 */
	public void showAnnouncementContentDialog(View announcementContentView) {
		if (announcementContentView != null) {
			mAnnouncementDialog = new Dialog(getActivity(),
					android.R.style.Theme_Light_NoTitleBar_Fullscreen);
			mAnnouncementDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			mAnnouncementDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mAnnouncementDialog.setContentView(announcementContentView);
			mAnnouncementDialog
					.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
							// do nothing
						}
					});
			/*
			 * WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			 * lp.copyFrom(mAnnouncementDialog.getWindow().getAttributes());
			 * lp.width = WindowManager.LayoutParams.MATCH_PARENT; lp.height =
			 * WindowManager.LayoutParams.MATCH_PARENT;
			 */

			mAnnouncementDialog.show();
			// mAnnouncementDialog.getWindow().setAttributes(lp);
		}
	}

	/**
	 * Inflates the Announcement Content WebView
	 */
	public View constructAnnouncementContentView() {
		View announcementView = null;
		if (getActivity() != null) {
			announcementView = (View) LayoutInflater.from(getActivity())
					.inflate(R.layout.announcement_layout, null);
			mAnnouncementLayout = (LinearLayout) announcementView
					.findViewById(R.id.announcement_layout);
			mAnnouncementLayout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					removeAnnouncementContentDialog();
				}
			});
			mAnnouncementProgressBar = (ProgressBar) announcementView
					.findViewById(R.id.announcement_progressBar);
			mAnnouncementWebView = (WebView) announcementView
					.findViewById(R.id.announcement_webview);
		}
		return announcementView;
	}

	public void fetchMyFeedFromServer() {
		// Start my feed thread
		if (Util.isOnline(getActivity())) {
			if (!TextUtils.isEmpty(userID)) {
				MyFeedHandler handler = new MyFeedHandler(this);
				MyFeedThread task = new MyFeedThread(getActivity()
						.getApplicationContext(), userID, offset, count,
						handler);
				Thread t = new Thread(task);
				t.start();
			}
		} else {
			if(mPullToRefreshListView != null){
				mPullToRefreshListView.onRefreshComplete();
			}
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network_pull_refresh),
					Toast.LENGTH_SHORT).show();
		}
	}

}
