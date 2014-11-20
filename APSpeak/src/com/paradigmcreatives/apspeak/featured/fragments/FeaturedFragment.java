package com.paradigmcreatives.apspeak.featured.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.globalstream.listeners.GlobalStreamsAdapter;
import com.paradigmcreatives.apspeak.globalstream.tasks.FetchGlobalStreamsTask;
import com.paradigmcreatives.apspeak.stream.handlers.GetStreamHandler;
import com.paradigmcreatives.apspeak.stream.listeners.ListOnScrollListenerImpl;
import com.paradigmcreatives.apspeak.stream.listeners.NextBatchFetchListener;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;

public class FeaturedFragment extends Fragment implements
		NextBatchFetchListener {

	private final String SAVED_FEATURE_LIST = "featuredList";

	private PullToRefreshGridView mPullToRefreshGridView;
	private GridView mGridView;
	private ProgressBar mProgressBar;
	private TextView mErrorMessageView;

	private GlobalStreamsAdapter mAdapter;
	private ArrayList<StreamAsset> mFeaturedArrayList;

	private ListOnScrollListenerImpl mScrollListener;

	public FeaturedFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.featured_layout, container, false);

		if (savedInstanceState != null) {
			mFeaturedArrayList = savedInstanceState
					.getParcelableArrayList(SAVED_FEATURE_LIST);
		}

		initUI(view);
		setAdapter();
		fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, false);
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			if (mFeaturedArrayList != null) {
				outState.putParcelableArrayList(SAVED_FEATURE_LIST,
						mFeaturedArrayList);
			}
		}
		super.onSaveInstanceState(outState);
	}

	private void initUI(View view) {
		if (view != null) {
			mPullToRefreshGridView = (PullToRefreshGridView) view
					.findViewById(R.id.featured_stream_grid_view);
			mPullToRefreshGridView.setScrollingWhileRefreshingEnabled(true);
			mPullToRefreshGridView
					.setMode(mPullToRefreshGridView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START
							: Mode.BOTH);
			mGridView = mPullToRefreshGridView.getRefreshableView();
			// mGridView = (GridView)
			// view.findViewById(R.id.featured_stream_grid_view);
			mProgressBar = (ProgressBar) view
					.findViewById(R.id.featured_stream_progressBar);
			mErrorMessageView = (TextView) view
					.findViewById(R.id.featured_stream_errorMessage);

			// Set a listener to be invoked when the list should be refreshed.
			mPullToRefreshGridView
					.setOnRefreshListener(new OnRefreshListener2<GridView>() {

						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// Toast.makeText(getActivity(), "Pull Down!",
							// Toast.LENGTH_SHORT).show();
							// new GetDataTask().execute();
							// Fetch list from server
							fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, true);
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// Toast.makeText(getActivity(), "Pull Up!",
							// Toast.LENGTH_SHORT).show();
							// new GetDataTask().execute();
							// Fetch next batch from server
							if (mGridView != null) {
								BaseAdapter adapter = (BaseAdapter) mGridView
										.getAdapter();
								if (adapter != null) {
									int count = adapter.getCount();
									fetchNextBatch(count,
											Constants.BATCH_FETCHLIMIT, true);
								}
							}
						}

					});
			setAdapter();
			fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, false);
		}
	}

	public void setAdapter() {
		if (isAdded() && mGridView != null && mFeaturedArrayList != null) {
			mAdapter = new GlobalStreamsAdapter(this, mFeaturedArrayList);
			mScrollListener = new ListOnScrollListenerImpl(this, false);
			mGridView.setAdapter(mAdapter);
			mGridView.setOnScrollListener(mScrollListener);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void fetchNextBatch(int startIndex, int limit,
			boolean isPullToRefresh) {
		if (startIndex == 0) {
			if (!isPullToRefresh) {
				if (mProgressBar != null
						&& mProgressBar.getVisibility() != View.VISIBLE) {
					mProgressBar.setVisibility(View.VISIBLE);
				}
				if (mErrorMessageView != null
						&& mErrorMessageView.getVisibility() != View.INVISIBLE) {
					mErrorMessageView.setVisibility(View.INVISIBLE);
				}
			}
		} else {
			if (mProgressBar != null
					&& mProgressBar.getVisibility() == View.VISIBLE) {
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			if (mErrorMessageView != null
					&& mErrorMessageView.getVisibility() == View.VISIBLE) {
				mErrorMessageView.setVisibility(View.INVISIBLE);
			}
		}
		String userIdToFetchStream = null;
		if (TextUtils.isEmpty(userIdToFetchStream)) {
			// Fetch stream for the current user
			userIdToFetchStream = AppPropertiesUtil.getUserID(getActivity());
		}
		fetchBatchedStreamFromServer(userIdToFetchStream, startIndex, limit);
	}

	private void fetchBatchedStreamFromServer(String userIdToFetchStream,
			int startIndex, int limit) {
		if (Util.isOnline(getActivity())) {
			GetStreamHandler handler = new GetStreamHandler(this);
			FetchGlobalStreamsTask task = new FetchGlobalStreamsTask(
					getActivity(), handler, userIdToFetchStream, null, null,
					STREAM_TYPE.FEATURED_STREAM, startIndex, limit);
			task.start();
		} else {
			if(mPullToRefreshGridView != null){
				mPullToRefreshGridView.onRefreshComplete();
			}
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network_pull_refresh),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Sets User's batched stream of assets
	 * 
	 * @param streamAssets
	 */
	public void setBatchedStream(ArrayList<StreamAsset> streamAssets,
			int startIndex, int limit, String type) {

		if (isAdded()) {
			if (mScrollListener != null) {
				mScrollListener.setShouldLoadMoreFlag(true);
			}
			STREAM_TYPE streamType = Util.convertToStreamType(type);
			if (mProgressBar != null
					&& mProgressBar.getVisibility() == View.VISIBLE) {
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			if (mErrorMessageView != null
					&& mErrorMessageView.getVisibility() == View.VISIBLE) {
				mErrorMessageView.setVisibility(View.INVISIBLE);
			}
			if (streamAssets != null && streamAssets.size() > 0) {
				if (startIndex == 0) {
					mFeaturedArrayList = streamAssets;
					setAdapter();
				} else {
					updateAdapter(streamAssets, streamType);
				}
			}
			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshGridView.onRefreshComplete();
		}
	}

	/**
	 * Updates grid view adapter by appending next batch assets
	 * 
	 * @param streamAssets
	 */
	public synchronized void updateAdapter(ArrayList<StreamAsset> streamAssets,
			STREAM_TYPE streamType) {
		if (mGridView != null && streamAssets != null
				&& streamAssets.size() > 0 && isAdded()) {
			if (mFeaturedArrayList != null) {
				mFeaturedArrayList.addAll(streamAssets);
			} else {
				mFeaturedArrayList = streamAssets;
			}
			if (mAdapter != null) {
				mAdapter.notifyDataSetChanged();
				//mAdapter.appendNextBatchAssets(streamAssets);
			}else{
				setAdapter();
			}
		}
	}

	/**
	 * Sets error message on screen by removing ongoing spinner
	 * 
	 * @param message
	 */
	public void setErrorMessage(String message) {
		if (isAdded()) {
			// Call onRefreshComplete when the list has been refreshed.
			mPullToRefreshGridView.onRefreshComplete();
			if (mScrollListener != null) {
				mScrollListener.setShouldLoadMoreFlag(true);
			}
			if (mAdapter != null && mAdapter.getCount() > 0) {
				// This case will occur when we are fetching user's personal
				// stream with batched assets fetch
				return;
			}
			if (mProgressBar != null
					&& mProgressBar.getVisibility() == View.VISIBLE) {
				mProgressBar.setVisibility(View.INVISIBLE);
			}
			if (mErrorMessageView != null
					&& mErrorMessageView.getVisibility() != View.VISIBLE) {
				if (!TextUtils.isEmpty(message)) {
					mErrorMessageView.setText(message);
				} else {
					mErrorMessageView
							.setText(R.string.stream_load_errormessage);
				}
				mErrorMessageView.setVisibility(View.VISIBLE);
			}
		}
	}

}
