package com.paradigmcreatives.apspeak.cues.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.cues.adapters.CuesNewAdapter;
import com.paradigmcreatives.apspeak.cues.handlers.GetCuesHandler;
import com.paradigmcreatives.apspeak.cues.tasks.GetCuesThread;
import com.paradigmcreatives.apspeak.globalstream.AppNewChildActivity;

/**
 * Fragment that displays Whatsay cues
 * 
 * @author Dileep | neuv
 * 
 */
public class CuesFragment extends Fragment {

	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private ProgressBar mProgressBar;
	private TextView mErrorMessage;
	private CuesNewAdapter mCuesNewAdapter;
	private HashMap<Integer, ArrayList<Campaigns>> mCues;

	private final String CUES_ARRAY = "cues_array";

	/**
	 * Default constructor
	 */
	public CuesFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.cues_layout_v1, container,
				false);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(CUES_ARRAY)) {
				try {
					mCues = (HashMap<Integer, ArrayList<Campaigns>>) savedInstanceState
							.getSerializable(CUES_ARRAY);
				} catch (Exception e) {

				}
			}
		}

		initUI(rootView);
		if (mCues != null && mCues.size() > 0) {
			setAdapter();
		} else {
			if (mProgressBar != null
					&& mProgressBar.getVisibility() != View.VISIBLE) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			fetchCuesFromServer();
		}
		return rootView;
	}

	private void initUI(View rootView) {
		if (rootView != null) {
			mPullToRefreshListView = (PullToRefreshListView) rootView
					.findViewById(R.id.cues_list_view);
			mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
			mListView = mPullToRefreshListView.getRefreshableView();
			mPullToRefreshListView
					.setOnRefreshListener(new OnRefreshListener<ListView>() {
						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {
							fetchCuesFromServer();
						}
					});
			mProgressBar = (ProgressBar) rootView
					.findViewById(R.id.cues_progressBar);
			mErrorMessage = (TextView) rootView
					.findViewById(R.id.cues_errorMessage);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			if (mCues != null) {
				outState.putSerializable(CUES_ARRAY, mCues);
			}
		}
		super.onSaveInstanceState(outState);
	}

	/**
	 * Sets adapter to Cues GridView
	 */
	public void setAdapter() {
		if (isAdded() && mListView != null && mCues != null) {
			mCuesNewAdapter = new CuesNewAdapter(this, mCues);
			mListView.setAdapter(mCuesNewAdapter);
			mCuesNewAdapter.notifyDataSetChanged();
		}
	}

	public void setCues(ArrayList<Campaigns> cues) {
		manipulateAndSetCuesHash(cues);
		if (mProgressBar != null
				&& mProgressBar.getVisibility() == View.VISIBLE) {
			mProgressBar.setVisibility(View.GONE);
		}
		if (mCues == null || mCues.size() < 1) {
			if (mErrorMessage != null
					&& mErrorMessage.getVisibility() != View.VISIBLE) {
				mErrorMessage.setText(R.string.cues_load_errormessage);
				mErrorMessage.setVisibility(View.VISIBLE);
			}
		}else{
			if(mErrorMessage != null && mErrorMessage.getVisibility() == View.VISIBLE){
				mErrorMessage.setVisibility(View.INVISIBLE);
			}
		}
		mPullToRefreshListView.onRefreshComplete();
	}

	/**
	 * Fetches cues from server
	 */
	public void fetchCuesFromServer() {
		if (Util.isOnline(getActivity())) {
			GetCuesHandler handler = new GetCuesHandler(this);
			GetCuesThread thread = new GetCuesThread(getActivity(), handler);
			thread.start();
		} else {
			if(mPullToRefreshListView != null){
				mPullToRefreshListView.onRefreshComplete();
			}
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network_pull_refresh),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Launches Cues Stream
	 * 
	 * @param cue
	 */
	public void launchCueStream(Campaigns cue) {
		if (cue != null) {
			Intent intent = new Intent(getActivity(), AppNewChildActivity.class);
			intent.putExtra(Constants.LAUNCH_GLOBALSTREAM_SCREEN, true);
			intent.putExtra(Constants.CUE_OBJECT, cue);
			startActivity(intent);
		}
	}

	public void setCuesFetchError() {
		mPullToRefreshListView.onRefreshComplete();
		if (mProgressBar != null
				&& mProgressBar.getVisibility() == View.VISIBLE) {
			mProgressBar.setVisibility(View.GONE);
		}
		if (mErrorMessage != null
				&& mErrorMessage.getVisibility() != View.VISIBLE) {
			mErrorMessage.setText(R.string.cues_load_errormessage);
			mErrorMessage.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Parses the passed array of cues and creates an HashMap with integer and
	 * ArrayList as Key-Value pairs, so that the same hashmap will be used while
	 * populating cues on the screen.
	 * 
	 * @param cues
	 */
	private void manipulateAndSetCuesHash(ArrayList<Campaigns> cues) {
		/*
		 * The logic here is as follows: --> Lets have the array of cues with
		 * respective widths as: Cue1(width 1 or null), Cue2(width 1 or null),
		 * Cue3(width 2), Cue4(width1 or null) and Cue5(width1 or null)
		 * 
		 * --> Then the cues are loaded on the screen as follows: First row will
		 * show Cue1 and Cue2, Second row will show Cue3, Thrid row will show
		 * Cue4 and Cue5
		 * 
		 * --> In summary: A Cue with width 2 will be shown as single item in a
		 * row, otherwise, we will be showing 2 Cues in each row
		 */
		if (cues != null && !cues.isEmpty()) {
			int position = 0;
			mCues = new HashMap<Integer, ArrayList<Campaigns>>();
			ArrayList<Campaigns> tempArray = new ArrayList<Campaigns>();
			boolean moveToNextRow = false;

			Iterator<Campaigns> iterator = cues.iterator();
			Campaigns item;
			while (iterator.hasNext()) {
				item = iterator.next();
				if (item.getWidth() == 2) {
					moveToNextRow = true;
				}

				if (moveToNextRow) {
					tempArray.add(item);
					mCues.put(position++, tempArray);
					tempArray = new ArrayList<Campaigns>();
					moveToNextRow = false;
				} else {
					tempArray.add(item);
					moveToNextRow = true;
				}
			}

			// Add the cue in case if its left pending
			if (moveToNextRow) {
				mCues.put(position++, tempArray);
			}
		}
	}
}
