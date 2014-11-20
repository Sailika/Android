package com.paradigmcreatives.apspeak.stream.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.assets.handlers.AssetDeleteHandler;
import com.paradigmcreatives.apspeak.assets.handlers.InappropriateFlagHandler;
import com.paradigmcreatives.apspeak.assets.tasks.AssetDeleteThread;
import com.paradigmcreatives.apspeak.assets.tasks.AssetInappropriateThread;
import com.paradigmcreatives.apspeak.assets.tasks.UserInappropriateThread;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.globalstream.tasks.FetchGlobalStreamsTask;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.stream.adapters.UserStreamAdapter;
import com.paradigmcreatives.apspeak.stream.handlers.GetStreamHandler;
import com.paradigmcreatives.apspeak.stream.listeners.ListOnScrollListenerImpl;
import com.paradigmcreatives.apspeak.stream.listeners.NextBatchFetchListener;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;

/**
 * Fragment that appears as the "content_frame", shows User's main stream
 * 
 * @author Dileep | neuv
 */
public class UserStreamFragment extends Fragment implements
		NextBatchFetchListener {

	private static final String TAG = "UserStreamFragment";

	private ProgressBar progressBar;
	private TextView errorMessageView;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView listView;
	private GridView gridView;
	private UserStreamAdapter adapter;
	private ListOnScrollListenerImpl mScrollListener;
	private ArrayList<StreamAsset> streamAssets;
	private String userId;
	private boolean isFromProfileFragment;
	private DisplayImageOptions options;

	private final String STREAM_ASSETS = "stream_assets";

	private boolean mIsGridviewInUse = true;

	/**
	 * Default constructor
	 */
	public UserStreamFragment() {
		super();
	}

	/**
	 * Constructor
	 */
	public UserStreamFragment(String userId, boolean isFromProfileFragment) {
		super();
		this.userId = userId;
		this.isFromProfileFragment = isFromProfileFragment;
	}

	/**
	 * Constructor to assign fragment's stream of assets
	 */
	public UserStreamFragment(ArrayList<StreamAsset> assets) {
		super();
		this.streamAssets = assets;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.userstream_layout_v1,
				container, false);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(STREAM_ASSETS)) {
				streamAssets = savedInstanceState
						.getParcelableArrayList(STREAM_ASSETS);
			}
		}

		// Initialize UI components
		initUI(rootView);

		if (streamAssets != null && streamAssets.size() > 0) {
			// update the adapter
			setAdapter(streamAssets);
		} else {
			// Show spinner
			if (progressBar != null
					&& (progressBar.getVisibility() == View.INVISIBLE || progressBar
							.getVisibility() == View.GONE)) {
				progressBar.setVisibility(View.VISIBLE);
			}
			// fetch user's stream
			fetchUserStream();
		}

		this.options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();
		return rootView;
	}

	/**
	 * Initializes view components
	 * 
	 * @param rootView
	 */
	private void initUI(View rootView) {
		if (rootView != null) {
			mPullToRefreshListView = (PullToRefreshListView) rootView
					.findViewById(R.id.stream_list_view);
			mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
			mPullToRefreshListView
					.setOnRefreshListener(new OnRefreshListener<ListView>() {
						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {
							fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, true);
						}
					});
			listView = mPullToRefreshListView.getRefreshableView();
			gridView = (GridView) rootView.findViewById(R.id.grid_view);
			progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
			errorMessageView = (TextView) rootView
					.findViewById(R.id.errorMessage);

			// By default, we will be showing ListView
			gridView.setVisibility(View.GONE);
			mIsGridviewInUse = false;
			if (adapter != null) {
				adapter.setIsGridViewInUseFlag(mIsGridviewInUse);
				if (isFromProfileFragment) {
					adapter.showProfilePicAndName(false);
				}
			}

			ImageView createStream = (ImageView) rootView
					.findViewById(R.id.createStream);
			if (isFromProfileFragment) {
				createStream.setVisibility(View.GONE);
			} else {
				createStream.setVisibility(View.VISIBLE);
				createStream.setOnClickListener(new View.OnClickListener() {
					public void onClick(View view) {
						GoogleAnalyticsHelper
								.sendEventToGA(
										getActivity(),
										GoogleAnalyticsConstants.USER_STREAM_SCREEN,
										GoogleAnalyticsConstants.ACTION_BUTTON,
										GoogleAnalyticsConstants.STREAM_PLUS_CANVAS_BUTTON);
						// Launch canvas
						launchCanvas();
					}
				});
			}

			GoogleAnalyticsHelper.sendScreenViewToGA(getActivity(),
					GoogleAnalyticsConstants.USER_STREAM_SCREEN);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			if (streamAssets != null) {
				outState.putParcelableArrayList(STREAM_ASSETS, streamAssets);
			}
		}
		super.onSaveInstanceState(outState);
	}

	/**
	 * Launches the canvas activity
	 */
	private void launchCanvas() {
		if (getActivity() != null) {
			Intent intent = new Intent(getActivity(),
					ImageSelectionFragmentActivity.class);
			// starting CanvasActivity
			try {
				getActivity().startActivity(intent);
			} catch (ActivityNotFoundException anfe) {
				Logger.warn(TAG,
						"Activity not found : " + anfe.getLocalizedMessage());
			} catch (Exception e) {
				Logger.warn(TAG,
						"Unknown Exception : " + e.getLocalizedMessage());
			}
		} else {
			Logger.warn(TAG, "Inbox is null");
		}
	}

	/**
	 * Fetches User's stream from DB and triggers fetching latest stream from
	 * Server
	 */
	private void fetchUserStream() {
		String userIdToFetchStream = userId;
		if (TextUtils.isEmpty(userIdToFetchStream)) {
			// Fetch stream for the current user
			userIdToFetchStream = AppPropertiesUtil.getUserID(getActivity());
		}
		// Batched fetching enabled to both general stream and user profile's
		// personal stream
		fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, false);
	}

	/**
	 * Fetches User's stream of given limit, starting from a given index from
	 * server
	 */
	private void fetchBatchedUserStreamFromServer(String userIdToFetchStream,
			int startIndex, int limit) {
		if (Util.isOnline(getActivity())) {
			if (!TextUtils.isEmpty(userIdToFetchStream)) {
				GetStreamHandler handler = new GetStreamHandler(this);
				FetchGlobalStreamsTask task = new FetchGlobalStreamsTask(
						getActivity(), handler, userIdToFetchStream, null,
						null, STREAM_TYPE.PERSONAL_STREAM, startIndex,
						Constants.BATCH_FETCHLIMIT);
				task.start();
			}
		} else {
			if(mPullToRefreshListView != null){
				mPullToRefreshListView.onRefreshComplete();
			}
			if(mScrollListener != null){
				mScrollListener.setShouldLoadMoreFlag(true);
			}
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network_pull_refresh),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Sets User's stream of assets
	 * 
	 * @param streamAssets
	 */
	public void setUserStream(ArrayList<StreamAsset> streamAssets) {

		if (isAdded()) {
			this.streamAssets = streamAssets;
			if (progressBar != null
					&& progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if (errorMessageView != null
					&& errorMessageView.getVisibility() == View.VISIBLE) {
				errorMessageView.setVisibility(View.INVISIBLE);
			}
			if (streamAssets != null && streamAssets.size() > 0) {
				setAdapter(streamAssets);
			} else {
				Toast.makeText(getActivity(), "No stream to display",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * Sets User's batched stream of assets
	 * 
	 * @param streamAssets
	 */
	public void setBatchedUserStream(ArrayList<StreamAsset> streamAssets,
			int startIndex, int limit) {

		if (isAdded()) {
			if (progressBar != null
					&& progressBar.getVisibility() == View.VISIBLE) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if (errorMessageView != null
					&& errorMessageView.getVisibility() == View.VISIBLE) {
				errorMessageView.setVisibility(View.INVISIBLE);
			}
			if (streamAssets != null && streamAssets.size() > 0) {
				if (startIndex == 0) {
					this.streamAssets = streamAssets;
					setAdapter(this.streamAssets);
				} else {
					if (this.streamAssets != null
							&& this.streamAssets.size() > 0) {
						this.streamAssets.addAll(streamAssets);
						if (adapter != null) {
							adapter.notifyDataSetChanged();
						}
					} else {
						this.streamAssets = streamAssets;
						setAdapter(this.streamAssets);
					}
					// updateAdapter(this.streamAssets);
				}
			}
			mPullToRefreshListView.onRefreshComplete();
		}
	}

	/**
	 * Sets error message on screen by removing ongoing spinner
	 * 
	 * @param message
	 */
	public void setErrorMessage(String message) {
		if (isAdded()) {
			mPullToRefreshListView.onRefreshComplete();
			if (adapter != null && adapter.getCount() > 0) {
				// This case will occur when we are fetching user's personal
				// stream with batched assets fetch
				return;
			}
			if (progressBar != null
					&& (progressBar.getVisibility() == View.VISIBLE || progressBar
							.getVisibility() == View.GONE)) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if (errorMessageView != null) {
				if (!TextUtils.isEmpty(message)) {
					errorMessageView.setText(message);
				} else {
					errorMessageView.setText(R.string.stream_load_errormessage);
				}
				errorMessageView.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * Sets adapter of StreamAsset objects to the grid view
	 * 
	 * @param streamAssets
	 */
	public synchronized void setAdapter(ArrayList<StreamAsset> streamAssets) {
		if ((listView != null || gridView != null) && streamAssets != null
				&& streamAssets.size() > 0 && isAdded()) {
			adapter = new UserStreamAdapter(this, streamAssets);
			adapter.setIsGridViewInUseFlag(mIsGridviewInUse);
			if (isFromProfileFragment) {
				adapter.showProfilePicAndName(false);
			}
			mScrollListener = new ListOnScrollListenerImpl(this,
					isFromProfileFragment);
			if (listView != null) {
				listView.setAdapter(adapter);
				listView.setOnScrollListener(mScrollListener);
			}
			if (gridView != null) {
				gridView.setAdapter(adapter);
				gridView.setOnScrollListener(mScrollListener);
			}
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Updates grid view adapter by appending next batch assets
	 * 
	 * @param streamAssets
	 */
	public synchronized void updateAdapter(ArrayList<StreamAsset> streamAssets) {
		if (mScrollListener != null) {
			mScrollListener.setShouldLoadMoreFlag(true);
		}
		if ((listView != null || gridView != null) && streamAssets != null
				&& streamAssets.size() > 0 && isAdded()) {
			if (adapter != null) {
				adapter.appendNextBatchAssets(streamAssets);
			}
		}
	}

	/**
	 * 
	 * Gets called when asset relationship(LOVE, etc) is updated with server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRelationshipUpdated(String assetId,
			ASSOCIATION_TYPE relationship) {
		/*
		 * Change the LIKE icon and total likes count for the asset in Stream
		 */
		if ((listView != null || gridView != null) && streamAssets != null
				&& !TextUtils.isEmpty(assetId) && isAdded()) {
			for (int i = 0; i < streamAssets.size(); i++) {
				if (streamAssets.get(i).getAssetId().equals(assetId)) {
					int likesCount = 0;
					HashMap<ASSOCIATION_TYPE, Integer> associations = streamAssets
							.get(i).getAssetAssociations();
					if (associations != null) {
						if (associations.containsKey(ASSOCIATION_TYPE.LOVE)) {
							likesCount = associations
									.get(ASSOCIATION_TYPE.LOVE);
						}
					} else {
						associations = new HashMap<ASSOCIATION_TYPE, Integer>();
					}
					// Increment the likes/LOVE count by 1
					likesCount = likesCount + 1;
					// Set new likes/LOVE count to asset associations
					associations.put(ASSOCIATION_TYPE.LOVE, likesCount);
					streamAssets.get(i).setAssetAssociations(associations);
					streamAssets.get(i).setAssetIsLoved(true);
					if (adapter != null) {
						if (listView != null) {
							// adapter.refreshListItemAtPosition(i, listView);
							adapter.notifyDataSetChanged();
						}
						if (gridView != null) {
							adapter.refreshGridItemAtPosition(i, gridView);
						}

					}
					break;
				}
			}
		}
	}

	/**
	 * Gets called when asset relationship(LOVE, etc) update is failed in server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRelationshipUpdateFailed(String assetId,
			ASSOCIATION_TYPE relationship) {
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

	/**
	 * Gets called when asset relationship(LOVE, etc) is removed from server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRelationshipRemoveSuccess(String assetId,
			ASSOCIATION_TYPE relationship) {
		/*
		 * Change the LIKE icon and total likes count for the asset in Stream
		 */
		if (isAdded() && (listView != null || gridView != null)
				&& streamAssets != null && !TextUtils.isEmpty(assetId)) {
			for (int i = 0; i < streamAssets.size(); i++) {
				if (streamAssets.get(i).getAssetId().equals(assetId)) {
					int likesCount = 0;
					HashMap<ASSOCIATION_TYPE, Integer> associations = streamAssets
							.get(i).getAssetAssociations();
					if (associations != null) {
						if (associations.containsKey(ASSOCIATION_TYPE.LOVE)) {
							likesCount = associations
									.get(ASSOCIATION_TYPE.LOVE);
						}
					} else {
						associations = new HashMap<ASSOCIATION_TYPE, Integer>();
					}
					// Decrement the likes/LOVE count by 1
					likesCount = likesCount - 1;
					// Set new likes/LOVE count to asset associations
					associations.put(ASSOCIATION_TYPE.LOVE,
							(likesCount < 0) ? 0 : likesCount);
					streamAssets.get(i).setAssetAssociations(associations);
					streamAssets.get(i).setAssetIsLoved(false);
					if (adapter != null) {
						if (listView != null) {
							// adapter.refreshListItemAtPosition(i, listView);
							adapter.notifyDataSetChanged();
						}
						if (gridView != null) {
							adapter.refreshGridItemAtPosition(i, gridView);
						}

					}
					break;
				}
			}
		}
	}

	/**
	 * Gets called when asset relationship(LOVE, etc) remove is failed in server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRelationshipRemoveFailed(String assetId,
			ASSOCIATION_TYPE relationship) {
		/*
		 * Handle the failure by changing respective relationship
		 * icon/count/details,etc
		 */
	}

	/**
	 * Gets called when asset repost is success with server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRepostSuccess(String assetId) {
		/*
		 * TODO: Uncomment for Google Analytics
		 * GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
		 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
		 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
		 * GoogleAnalyticsConstants.STREAM_REPOST_SUCCESS_BUTTON);
		 */
		/*
		 * Handle the success by increasing reposts count and display on asset
		 */
		if ((listView != null || gridView != null) && streamAssets != null
				&& !TextUtils.isEmpty(assetId) && isAdded()) {
			for (int i = 0; i < streamAssets.size(); i++) {
				if (streamAssets.get(i).getAssetId().equals(assetId)) {
					int repostsCount = 0;
					// Increment the reposts count by 1
					repostsCount = streamAssets.get(i).getAssetRepostsCount() + 1;
					// Set new reposts count to asset associations
					streamAssets.get(i).setAssetRepostsCount(repostsCount);
					streamAssets.get(i).setAssetIsReposted(true);
					if (adapter != null) {
						if (listView != null) {
							adapter.refreshListItemAtPosition(i, listView);
						}
						if (gridView != null) {
							adapter.refreshGridItemAtPosition(i, gridView);
						}

					}
					break;
				}
			}
		}
	}

	public void assetDeleteSuccess(String assetId) {
		if ((listView != null || gridView != null) && streamAssets != null
				&& !TextUtils.isEmpty(assetId) && isAdded()) {
			for (int i = 0; i < streamAssets.size(); i++) {
				if (streamAssets.get(i).getAssetId().equals(assetId)) {
					streamAssets.remove(i);
					adapter.notifyDataSetChanged();
					if (progressBar != null
							&& (progressBar.getVisibility() == View.VISIBLE)) {
						progressBar.setVisibility(View.GONE);
					}
					Toast.makeText(this.getActivity().getApplicationContext(),
							getResources().getString(R.string.delete_success),
							Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public void assetDeleteFailed(String assetId) {
		if (progressBar != null
				&& (progressBar.getVisibility() == View.VISIBLE)) {
			progressBar.setVisibility(View.GONE);
		}
		Toast.makeText(this.getActivity().getApplicationContext(),
				getResources().getString(R.string.delete_failure),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Gets called when asset repost is failed in server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRepostFailed(String assetId) {
		/*
		 * TODO: Uncomment for Google Analytics
		 * GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
		 * GoogleAnalyticsConstants.STREAM_SCREEN_CAT_NAME,
		 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
		 * GoogleAnalyticsConstants.STREAM_REPOST_FAILURE_BUTTON);
		 */
		/*
		 * /* Handle the failure
		 */
	}

	/**
	 * Shows stream content in ListView by hiding current GridView
	 */
	public void showListView() {
		if (listView != null) {
			listView.setVisibility(View.VISIBLE);
		}
		if (gridView != null) {
			gridView.setVisibility(View.GONE);
		}
		mIsGridviewInUse = false;
		if (adapter != null) {
			adapter.setIsGridViewInUseFlag(mIsGridviewInUse);
		}
	}

	/**
	 * Shows stream content in GridView by hiding current ListView
	 */
	public void showGridView() {
		if (gridView != null) {
			gridView.setVisibility(View.VISIBLE);
		}
		if (listView != null) {
			listView.setVisibility(View.GONE);
		}
		mIsGridviewInUse = true;
		if (adapter != null) {
			adapter.setIsGridViewInUseFlag(mIsGridviewInUse);
		}
	}

	public void fetchNextBatch(int startIndex, int limit,
			boolean isPullToRefresh) {
		String userIdToFetchStream = userId;
		if (TextUtils.isEmpty(userIdToFetchStream)) {
			// Fetch stream for the current user
			userIdToFetchStream = AppPropertiesUtil.getUserID(getActivity());
		}
		fetchBatchedUserStreamFromServer(userIdToFetchStream, startIndex, limit);
	}

	public void showUserOptionsDialog(final String assetId) {

		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View optionsDialog = inflater.inflate(
				R.layout.user_asset_options_dialog, null);
		final Dialog dialog = new Dialog(this.getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		TextView deleteTextView = (TextView) optionsDialog
				.findViewById(R.id.delete_image);
		TextView userFlag = (TextView) optionsDialog
				.findViewById(R.id.user_inappropriate);
		TextView assetFlag = (TextView) optionsDialog
				.findViewById(R.id.flag_inappropriate);
		final String uId = AppPropertiesUtil.getUserID(this.getActivity());
		if (!TextUtils.isEmpty(uId) && !TextUtils.isEmpty(userId)) {
			if (userId.equals(uId)) {
				deleteTextView.setVisibility(View.VISIBLE);
				userFlag.setVisibility(View.GONE);
				assetFlag.setVisibility(View.GONE);
			} else {
				deleteTextView.setVisibility(View.GONE);
				userFlag.setVisibility(View.VISIBLE);
				assetFlag.setVisibility(View.VISIBLE);
			}
		}
		assetFlag.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if(Util.isOnline(getActivity())){
					if (!TextUtils.isEmpty(assetId)) {
						if (progressBar != null
								&& (progressBar.getVisibility() == View.INVISIBLE)
								&& (progressBar.getVisibility() == View.GONE)) {
							progressBar.setVisibility(View.VISIBLE);
						}
						AssetInappropriateThread thread = new AssetInappropriateThread(
								UserStreamFragment.this.getActivity(), assetId,
								userId, new InappropriateFlagHandler(
										UserStreamFragment.this));
						thread.start();
					}
				}else{
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_network),
							Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});

		userFlag.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if(Util.isOnline(getActivity())){
					if (!TextUtils.isEmpty(assetId)) {
						if (progressBar != null
								&& (progressBar.getVisibility() == View.INVISIBLE)
								&& (progressBar.getVisibility() == View.GONE)) {
							progressBar.setVisibility(View.VISIBLE);
						}
						UserInappropriateThread thread = new UserInappropriateThread(
								UserStreamFragment.this.getActivity(), uId, userId,
								new InappropriateFlagHandler(
										UserStreamFragment.this));
						thread.start();
					}
				}else{
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_network),
							Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});
		deleteTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if(Util.isOnline(getActivity())){
					if (!TextUtils.isEmpty(assetId)) {
						if (progressBar != null
								&& (progressBar.getVisibility() == View.INVISIBLE || progressBar
										.getVisibility() == View.GONE)) {
							progressBar.setVisibility(View.VISIBLE);
						}
						AssetDeleteHandler handler = new AssetDeleteHandler(
								UserStreamFragment.this);
						AssetDeleteThread thread = new AssetDeleteThread(
								UserStreamFragment.this.getActivity(), assetId,
								handler);
						thread.start();
					}
				}else{
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_network),
							Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();

			}
		});
		dialog.setContentView(optionsDialog);
		dialog.show();
	}

	public void inappropriateSuccess() {
		if (progressBar != null
				&& (progressBar.getVisibility() == View.VISIBLE)) {
			progressBar.setVisibility(View.GONE);
		}
		Toast.makeText(this.getActivity().getApplicationContext(),
				getResources().getString(R.string.server_call_success),
				Toast.LENGTH_LONG).show();
	}

	public void inappropriateFailure() {
		if (progressBar != null
				&& (progressBar.getVisibility() == View.VISIBLE)) {
			progressBar.setVisibility(View.GONE);
		}
		Toast.makeText(this.getActivity().getApplicationContext(),
				getResources().getString(R.string.server_failure),
				Toast.LENGTH_LONG).show();
	}

	public void refreshStream() {
		fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, true);
	}

}
