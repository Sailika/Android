package com.paradigmcreatives.apspeak.globalstream.fragments;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.apache.http.HttpStatus;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.database.expressionsdb.ExpressionsSubmitQueueDAO;
import com.paradigmcreatives.apspeak.app.model.Campaigns;
import com.paradigmcreatives.apspeak.app.model.ExpressionSubmitQueueBean;
import com.paradigmcreatives.apspeak.app.model.SUBMISSION_STATUS;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.customcontrols.FullWidthImageView;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.assets.handlers.AssetDeleteHandler;
import com.paradigmcreatives.apspeak.assets.handlers.InappropriateFlagHandler;
import com.paradigmcreatives.apspeak.assets.handlers.WhatsayAssetDownloadHandler;
import com.paradigmcreatives.apspeak.assets.tasks.AssetDeleteThread;
import com.paradigmcreatives.apspeak.assets.tasks.AssetInappropriateThread;
import com.paradigmcreatives.apspeak.assets.tasks.UserInappropriateThread;
import com.paradigmcreatives.apspeak.assets.tasks.WhatsayAssetDownloadThread;
import com.paradigmcreatives.apspeak.autosend.AutoSendManager;
import com.paradigmcreatives.apspeak.doodleboard.ImageSelectionFragmentActivity;
import com.paradigmcreatives.apspeak.feedback.FeedBack;
import com.paradigmcreatives.apspeak.feedback.FeedBackResponse;
import com.paradigmcreatives.apspeak.feedback.FeedBackType;
import com.paradigmcreatives.apspeak.feedback.StreamRequest;
import com.paradigmcreatives.apspeak.globalstream.AppNewChildActivity;
import com.paradigmcreatives.apspeak.globalstream.adapters.QueuedExpressionsAdapter;
import com.paradigmcreatives.apspeak.globalstream.listeners.GlobalStreamsAdapter;
import com.paradigmcreatives.apspeak.globalstream.listeners.GlobalStreamsOnClickListeners;
import com.paradigmcreatives.apspeak.globalstream.tasks.FetchGlobalStreamsTask;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.network.ResponseParser;
import com.paradigmcreatives.apspeak.network.RestClient;
import com.paradigmcreatives.apspeak.stream.adapters.UserStreamAdapter;
import com.paradigmcreatives.apspeak.stream.handlers.GetStreamHandler;
import com.paradigmcreatives.apspeak.stream.listeners.ListOnScrollListenerImpl;
import com.paradigmcreatives.apspeak.stream.listeners.NextBatchFetchListener;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;

/**
 * Fragment used to show different streams based on the cue passed
 * 
 * @author Dileep | neuv
 * 
 */
public class GlobalStreamsFragment extends Fragment implements
		NextBatchFetchListener, OnClickListener {

	public static final String TAG = GlobalStreamsFragment.class
			.getSimpleName();

	private View rootView;
	private FrameLayout mCueDetailsView;
	private static FullWidthImageView mCueDetailsBackgroundWideImage;
	private Button mCreatePost;
	// private LinearLayout mListGridSwitchLayout;
	// private ImageView mListGridSwitchIcon;
	private LinearLayout mQueueLayout;
	private ImageView mQueueIcon;
	private TextView mQueueMessage;
	private TextView mCollege;
	private TextView mAllColleges;
	private TextView mFriends;
	private LinearLayout mCollegeLayout;
	private LinearLayout mAllCollegesLayout;
	// private LinearLayout mFriendsLayout;
	private ProgressBar mProgressBar;
	private TextView mErrorMessageView;
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private UserStreamAdapter mListAdapter;
	private PullToRefreshGridView mPullToRefreshGridView;
	private GridView mGridView;
	private GlobalStreamsAdapter mGridAdapter;
	private Dialog mQueuedExpressionsDialog;
	private ListView mQueuedExpressionsListView;
	private QueuedExpressionsAdapter mQueuedExpressionsAdapter;

	private ArrayList<StreamAsset> mCollegeList;
	private ArrayList<StreamAsset> mAllCollegesList;
	private ArrayList<StreamAsset> mFriendsList;
	private Campaigns mCue;
	private STREAM_TYPE mCurrentStreamType;
	private ListOnScrollListenerImpl mGridViewScrollListener;
	private ListOnScrollListenerImpl mListViewScrollListener;
	private FrameLayout mPostLayout;
	private DisplayImageOptions options;
	private boolean mIsGridviewInUse = true;
	private boolean mRefreshRequired = false;

	private final String SAVE_COLLEGE_LIST = "save_college_list";
	private final String SAVE_ALLCOLLEGES_LIST = "save_allcolleges_list";
	private final String SAVE_FRIENDS_LIST = "save_friends_list";
	private final String SAVE_CUE = "save_cue";

	// for feedback and create ideas
	private static LinearLayout feedbackOptsLayout;
	private RelativeLayout ideasMainLayout;
	private ImageView createIdeaImage;

	// for feedback click events
	private ImageView awesomeFeedBackImg;
	private ImageView avgFeedBackImg;
	private ImageView badFeedBackImg;

	// feedback message
	private static TextView feedBackMessage;
	private static TextView yourOpinionTxt;

	private boolean isFeedback = true;

	private static RelativeLayout globelFeedbackBottomLayout;

	private static TextView headerText;

	/**
	 * Default Constructor
	 */
	public GlobalStreamsFragment() {
		super();
	}

	public GlobalStreamsFragment(Campaigns cue) {
		super();
		this.mCue = cue;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.global_streams_layout, container,
				false);

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(SAVE_COLLEGE_LIST)) {
				mCollegeList = savedInstanceState
						.getParcelableArrayList(SAVE_COLLEGE_LIST);
			}
			if (savedInstanceState.containsKey(SAVE_ALLCOLLEGES_LIST)) {
				mAllCollegesList = savedInstanceState
						.getParcelableArrayList(SAVE_ALLCOLLEGES_LIST);
			}
			if (savedInstanceState.containsKey(SAVE_FRIENDS_LIST)) {
				mFriendsList = savedInstanceState
						.getParcelableArrayList(SAVE_FRIENDS_LIST);
			}
			if (savedInstanceState.containsKey(SAVE_CUE)) {
				mCue = savedInstanceState.getParcelable(SAVE_CUE);
			}

		}

		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(getActivity()));
		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();

		initUI(rootView);
		final StreamRequest request = new StreamRequest();
		request.setUser_id(AppPropertiesUtil.getUserID(getActivity()));
		// refreshQueueLayout();
		// setAdapter();
		// Fetch list from server
		mCurrentStreamType = STREAM_TYPE.COLLEGE;

		fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, false);
		return rootView;
	}

	public static class FeedBackCallBack implements Callback<Response> {
		private int retryCount = 0;
		private static final int MAX_RETRY_COUNT = 3;
		private FeedBack request;
		private Context mContext;

		public FeedBackCallBack(final Context context, final int retryCount,
				final FeedBack request) {
			this(context, request);
			this.retryCount = retryCount;
			this.mContext = context;
		}

		public FeedBackCallBack(final Context context, final FeedBack request) {
			this.request = request;
			this.mContext = context;
		}

		@Override
		public void failure(RetrofitError arg0) {
			if (arg0.getCause() instanceof EOFException) {
				if (retryCount <= MAX_RETRY_COUNT) {
					retryCount++;
					RestClient.getInstance().getRestClient(mContext)
							.postFeedBack(request, this);
				} else {
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.server_failure), Toast.LENGTH_LONG)
							.show();
				}
			} else {
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.server_failure)
								+ arg0.getCause(), Toast.LENGTH_LONG).show();
			}
		}

		@Override
		public void success(Response arg0, Response arg1) {
			try {
				FeedBackResponse response = ResponseParser.parseResponse(arg1,
						FeedBackResponse.class);
				if (response.getErrorCode() == HttpStatus.SC_OK) {
					mCueDetailsBackgroundWideImage.setVisibility(View.GONE);
					feedbackOptsLayout.setVisibility(View.GONE);
					feedBackMessage.setText(mContext.getResources().getString(
							R.string.thank_you));
					feedBackMessage.setTextSize(25);
					yourOpinionTxt.setText(mContext.getResources().getString(
							R.string.providing_feedback));
					yourOpinionTxt.setTextColor(Color.BLACK);
					headerText.setText(mContext.getResources().getString(
							R.string.thank_you));
					feedBackMessage.setTextColor(mContext.getResources()
							.getColor(R.color.apspeak_green));

				}
			} catch (IOException e) {
				Toast.makeText(
						mContext,
						mContext.getResources().getString(
								R.string.server_failure), Toast.LENGTH_LONG)
						.show();
			}
		}

	}

	private void initUI(View rootView) {
		GlobalStreamsOnClickListeners listener = new GlobalStreamsOnClickListeners(
				this);
		if (rootView != null) {
			mCueDetailsView = (FrameLayout) rootView
					.findViewById(R.id.global_streams_cuedetails_layout);
			mCueDetailsBackgroundWideImage = (FullWidthImageView) mCueDetailsView
					.findViewById(R.id.global_streams_wideimage);

			int bcolor;
			Random random = new Random();
			try {
				bcolor = Color.parseColor(mCue.getBackgroundColor());
			} catch (Exception e) {
				bcolor = Color.argb(255, random.nextInt(256),
						random.nextInt(256), random.nextInt(256));
			}
			mCueDetailsView.setBackgroundColor(bcolor);
			// Download background wide image and set to cue details view
			fetchCueDetailsViewBackgroundWideImage();
			mPostLayout = (FrameLayout) mCueDetailsView
					.findViewById(R.id.global_streams_cuedetails_layout);
			mCreatePost = (Button) mCueDetailsView
					.findViewById(R.id.global_stream_make_post);

			mQueueLayout = (LinearLayout) rootView
					.findViewById(R.id.global_streams_queue_layout);
			mQueueIcon = (ImageView) rootView
					.findViewById(R.id.global_streams_queue_icon);
			mQueueMessage = (TextView) rootView
					.findViewById(R.id.global_streams_queue_message);

			// Initialize ListView
			mPullToRefreshListView = (PullToRefreshListView) rootView
					.findViewById(R.id.globalstreams_list_view);
			mPullToRefreshListView.setScrollingWhileRefreshingEnabled(true);
			mPullToRefreshListView
					.setOnRefreshListener(new OnRefreshListener<ListView>() {
						@Override
						public void onRefresh(
								PullToRefreshBase<ListView> refreshView) {
							fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, true);
						}
					});
			mListView = mPullToRefreshListView.getRefreshableView();
			mPullToRefreshListView.setVisibility(View.GONE);

			// Initialize GridView
			mPullToRefreshGridView = (PullToRefreshGridView) rootView
					.findViewById(R.id.globalstreams_grid_view);

			mPullToRefreshGridView.setScrollingWhileRefreshingEnabled(true);
			mPullToRefreshGridView
					.setMode(mPullToRefreshGridView.getMode() == Mode.BOTH ? Mode.PULL_FROM_START
							: Mode.BOTH);
			mGridView = mPullToRefreshGridView.getRefreshableView();
			mPullToRefreshGridView.setVisibility(View.GONE);

			mProgressBar = (ProgressBar) rootView
					.findViewById(R.id.global_streams_progressBar);
			mProgressBar.setVisibility(View.GONE);
			mErrorMessageView = (TextView) rootView
					.findViewById(R.id.global_streams_errorMessage);
			mErrorMessageView.setVisibility(View.INVISIBLE);

			mCollege = (TextView) rootView.findViewById(R.id.awesome);
			mAllColleges = (TextView) rootView.findViewById(R.id.average);
			mFriends = (TextView) rootView.findViewById(R.id.bad);

			mCollegeLayout = (LinearLayout) rootView
					.findViewById(R.id.college_button_layout);
			mAllCollegesLayout = (LinearLayout) rootView
					.findViewById(R.id.allcolleges_button_layout);
			// mFriendsLayout = (LinearLayout)
			// rootView.findViewById(R.id.friends_button_layout);

			// mAllColleges.setTextColor(getResources().getColor(R.color.white));
			mCollegeLayout.setBackgroundColor(getResources().getColor(
					R.color.yellow));

			mCollegeLayout.setOnClickListener(listener);
			mAllCollegesLayout.setOnClickListener(listener);
			// mFriendsLayout.setOnClickListener(listener);

			mCreatePost.setOnClickListener(listener);
			mPostLayout.setOnClickListener(listener);
			mCurrentStreamType = STREAM_TYPE.COLLEGE;

			// Set a listener to be invoked when the list should be refreshed.
			mPullToRefreshGridView
					.setOnRefreshListener(new OnRefreshListener2<GridView>() {

						@Override
						public void onPullDownToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// Fetch list from server
							fetchNextBatch(0, Constants.IDEAS_FETCHLIMIT, true);
						}

						@Override
						public void onPullUpToRefresh(
								PullToRefreshBase<GridView> refreshView) {
							// Fetch next batch from server
							if (mGridView != null) {
								BaseAdapter adapter = (BaseAdapter) mGridView
										.getAdapter();
								if (adapter != null) {
									int count = adapter.getCount();
									fetchNextBatch(count,
											Constants.IDEAS_FETCHLIMIT, true);
								}
							}
						}
					});
			feedbackOptsLayout = (LinearLayout) rootView
					.findViewById(R.id.feedback_layout);
			ideasMainLayout = (RelativeLayout) rootView
					.findViewById(R.id.create_idea_main_layout);
			createIdeaImage = (ImageView) rootView
					.findViewById(R.id.create_idea_image);

			awesomeFeedBackImg = (ImageView) rootView
					.findViewById(R.id.img_btn_awesome);
			avgFeedBackImg = (ImageView) rootView
					.findViewById(R.id.img_btn_average);
			badFeedBackImg = (ImageView) rootView
					.findViewById(R.id.img_btn_bad);
			awesomeFeedBackImg.setOnClickListener(this);
			avgFeedBackImg.setOnClickListener(this);
			badFeedBackImg.setOnClickListener(this);
			ideasMainLayout.setOnClickListener(this);
			createIdeaImage.setOnClickListener(this);
			headerText = (TextView) rootView
					.findViewById(R.id.globel_header_text);

			feedBackMessage = (TextView) rootView
					.findViewById(R.id.feedback_message_text);
			yourOpinionTxt = (TextView) rootView
					.findViewById(R.id.your_opinion_txt);
			globelFeedbackBottomLayout = (RelativeLayout) rootView
					.findViewById(R.id.global_streams_bottom_layout);
			mIsGridviewInUse = true;
			mCollege.setTextColor(getResources().getColor(R.color.black));
			mAllColleges.setTextColor(getResources().getColor(R.color.black));
			mFriends.setTextColor(getResources().getColor(R.color.black));

			TextView headerText = (TextView) rootView
					.findViewById(R.id.globel_header_text);
			headerText.setText(getResources().getString(
					R.string.poll_your_opinion));
			showGridView();

			if (mCue != null) {
				feedBackMessage.setText(mCue.getCueMessage());
			}

		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			if (mCollegeList != null) {
				outState.putParcelableArrayList(SAVE_COLLEGE_LIST, mCollegeList);
			}
			if (mAllCollegesList != null) {
				outState.putParcelableArrayList(SAVE_ALLCOLLEGES_LIST,
						mAllCollegesList);
			}
			if (mFriendsList != null) {
				outState.putParcelableArrayList(SAVE_FRIENDS_LIST, mFriendsList);
			}
			if (mCue != null) {
				outState.putParcelable(SAVE_CUE, mCue);
			}
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshQueueLayout();
	}

	public void setAdapter() {

		if (isAdded() && mGridView != null && mListView != null) {
			if (mIsGridviewInUse) {
				if (mGridView != null) {
					if (mCurrentStreamType == STREAM_TYPE.COLLEGE) {
						mGridAdapter = new GlobalStreamsAdapter(this,
								mCollegeList);
					} else if (mCurrentStreamType == STREAM_TYPE.ALLCOLLEGES) {
						mGridAdapter = new GlobalStreamsAdapter(this,
								mAllCollegesList);
					} else if (mCurrentStreamType == STREAM_TYPE.FRIENDS) {
						mGridAdapter = new GlobalStreamsAdapter(this,
								mFriendsList);
					}
					mGridViewScrollListener = new ListOnScrollListenerImpl(
							this, false);
					mGridView.setAdapter(mGridAdapter);
					mGridView.setOnScrollListener(mGridViewScrollListener);
					mGridAdapter.notifyDataSetChanged();
					mGridView.setVisibility(View.VISIBLE);
				}
			} else {
				if (mListView != null) {
					if (mCurrentStreamType == STREAM_TYPE.COLLEGE) {
						mListAdapter = new UserStreamAdapter(this, mCollegeList);
					} else if (mCurrentStreamType == STREAM_TYPE.ALLCOLLEGES) {
						mListAdapter = new UserStreamAdapter(this,
								mAllCollegesList);
					} else if (mCurrentStreamType == STREAM_TYPE.FRIENDS) {
						mListAdapter = new UserStreamAdapter(this, mFriendsList);
					}
					mListViewScrollListener = new ListOnScrollListenerImpl(
							this, false);
					mListAdapter.setIsGridViewInUseFlag(false);
					mListAdapter.showProfilePicAndName(false);
					mListView.setAdapter(mListAdapter);
					mListView.setOnScrollListener(mListViewScrollListener);
					mListAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	/**
	 * Fetches batched stream assets from server
	 * 
	 * @param startIndex
	 * @param limit
	 */
	public void fetchNextBatch(int startIndex, int limit,
			boolean isPullToRefresh) {

		if (startIndex == 0) {
			if (!isPullToRefresh) {
				if (mProgressBar != null
						&& mProgressBar.getVisibility() != View.VISIBLE) {
					mProgressBar.setVisibility(View.GONE);
				}
				if (mErrorMessageView != null
						&& mErrorMessageView.getVisibility() != View.INVISIBLE) {
					mErrorMessageView.setVisibility(View.INVISIBLE);
				}
			}
		} else {
			if (mProgressBar != null
					&& mProgressBar.getVisibility() == View.VISIBLE) {
				mProgressBar.setVisibility(View.GONE);
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

	/**
	 * Fetches batched stream from server
	 */
	private void fetchBatchedStreamFromServer(String userIdToFetchStream,
			int startIndex, int limit) {
		if (Util.isOnline(getActivity())) {

			GetStreamHandler handler = new GetStreamHandler(this);
			String groupId = AppPropertiesUtil.getGroupId(getActivity());
			String cueId = null;
			if (mCue != null) {
				cueId = mCue.getCueId();
			}
			if (mCurrentStreamType == STREAM_TYPE.COLLEGE) {
				FetchGlobalStreamsTask task = new FetchGlobalStreamsTask(
						getActivity(), handler, userIdToFetchStream, groupId,
						cueId, STREAM_TYPE.COLLEGE, startIndex, limit);
				task.start();
			} else if (mCurrentStreamType == STREAM_TYPE.ALLCOLLEGES) {
				FetchGlobalStreamsTask task = new FetchGlobalStreamsTask(
						getActivity(), handler, userIdToFetchStream, null,
						cueId, STREAM_TYPE.ALLCOLLEGES, startIndex, limit);
				task.start();
			} else if (mCurrentStreamType == STREAM_TYPE.FRIENDS) {
				FetchGlobalStreamsTask task = new FetchGlobalStreamsTask(
						getActivity(), handler, userIdToFetchStream, null,
						cueId, STREAM_TYPE.FRIENDS, startIndex, limit);
				task.start();
			}
		} else {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.no_network_pull_refresh),
					Toast.LENGTH_SHORT).show();
			// Call onRefreshComplete so that Pull Down/Pull Up view will get
			// resetted
			if (mIsGridviewInUse) {
				mPullToRefreshGridView.onRefreshComplete();
			} else {
				mPullToRefreshListView.onRefreshComplete();
			}
			if (mGridViewScrollListener != null) {
				mGridViewScrollListener.setShouldLoadMoreFlag(true);
			}
			if (mListViewScrollListener != null) {
				mListViewScrollListener.setShouldLoadMoreFlag(true);
			}
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
			if (mGridViewScrollListener != null) {
				mGridViewScrollListener.setShouldLoadMoreFlag(true);
			}
			if (mListViewScrollListener != null) {
				mListViewScrollListener.setShouldLoadMoreFlag(true);
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
					if (streamType == STREAM_TYPE.COLLEGE) {
						mCollegeList = streamAssets;
					} else if (streamType == STREAM_TYPE.ALLCOLLEGES) {
						mAllCollegesList = streamAssets;
					} else if (streamType == STREAM_TYPE.FRIENDS) {
						mFriendsList = streamAssets;
					}
					setAdapter();
				} else {
					updateAdapter(streamAssets, streamType);
				}
			}
			// Call onRefreshComplete when the list has been refreshed.
			if (mIsGridviewInUse) {
				if (!isFeedback) {
					mPullToRefreshGridView.setVisibility(View.VISIBLE);
				}
				mPullToRefreshGridView.onRefreshComplete();
			} else {
				mPullToRefreshListView.onRefreshComplete();
			}
		}
	}

	/**
	 * Updates grid view adapter by appending next batch assets
	 * 
	 * @param streamAssets
	 */
	public synchronized void updateAdapter(ArrayList<StreamAsset> streamAssets,
			STREAM_TYPE streamType) {
		if (streamAssets != null && streamAssets.size() > 0 && isAdded()) {
			if (streamType == STREAM_TYPE.COLLEGE) {
				if (mCollegeList != null) {
					mCollegeList.addAll(streamAssets);
				} else {
					mCollegeList = streamAssets;
				}
			} else if (streamType == STREAM_TYPE.ALLCOLLEGES) {
				if (mAllCollegesList != null) {
					mAllCollegesList.addAll(streamAssets);
				} else {
					mAllCollegesList = streamAssets;
				}
			} else if (streamType == STREAM_TYPE.FRIENDS) {
				if (mFriendsList != null) {
					mFriendsList.addAll(streamAssets);
				} else {
					mFriendsList = streamAssets;
				}
			}
			if (mGridAdapter != null) {
				mGridAdapter.notifyDataSetChanged();
			}
			if (mListAdapter != null) {
				mListAdapter.notifyDataSetChanged();
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
			if (mIsGridviewInUse) {
				mPullToRefreshGridView.onRefreshComplete();
			} else {
				mPullToRefreshListView.onRefreshComplete();
			}
			if (mGridViewScrollListener != null) {
				mGridViewScrollListener.setShouldLoadMoreFlag(true);
			}
			if (mListViewScrollListener != null) {
				mListViewScrollListener.setShouldLoadMoreFlag(true);
			}
			if ((mGridAdapter != null && mGridAdapter.getCount() > 0 && mIsGridviewInUse)
					|| (mListAdapter != null && mListAdapter.getCount() > 0 && !mIsGridviewInUse)) {
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

	/**
	 * Launches Image Selection activity, that shows Camera, Gallery and
	 * Background tabs for image selection
	 */
	public void launchImageSelectionActivity() {
		if (mPostLayout != null) {
			Animation bounceInAnim = AnimationUtils.loadAnimation(
					this.getActivity(), R.anim.bounce_in);
			mPostLayout.startAnimation(bounceInAnim);
			bounceInAnim.setAnimationListener(new AnimationListener() {
				public void onAnimationStart(Animation animation) {
				}

				public void onAnimationRepeat(Animation animation) {
				}

				public void onAnimationEnd(Animation animation) {
					Animation bounceOutAnim = AnimationUtils.loadAnimation(
							GlobalStreamsFragment.this.getActivity(),
							R.anim.bounce_out_anim);
					mPostLayout.startAnimation(bounceOutAnim);
					bounceOutAnim.setAnimationListener(new AnimationListener() {
						public void onAnimationStart(Animation animation) {
						}

						public void onAnimationRepeat(Animation animation) {
						}

						public void onAnimationEnd(Animation animation) {

							// Intent intent = new Intent(getActivity(),
							// ImageSelectionFragmentActivity.class);
							// if (mCue != null
							// && !TextUtils.isEmpty(mCue.getCueId())) {
							// intent.putExtra(Constants.CUE_ID,
							// mCue.getCueId());
							// }
							// getActivity().startActivityForResult(intent,
							// AppNewChildActivity.SUBMIT_EXPRESSION);

						}
					});
				}
			});

		}

	}

	/**
	 * Handles the functionality on tapping/clicking one of the 3 tabs(College,
	 * All Colleges, Friends)
	 * 
	 * @param viewId
	 */
	public void handleStreamTypeSelected(int viewId) {
		if (mErrorMessageView != null
				&& mErrorMessageView.getVisibility() == View.VISIBLE) {
			mErrorMessageView.setVisibility(View.GONE);
		}
		// Clear current adapter items
		if (mGridAdapter != null && mIsGridviewInUse) {
			mGridAdapter.clearAll();
		}
		if (mListAdapter != null && !mIsGridviewInUse) {
			mListAdapter.clearAll();
		}
		mErrorMessageView.setVisibility(View.INVISIBLE);
		switch (viewId) {
		case R.id.college_button_layout:// Feedback for APSpeak
			isFeedback = true;
			mQueueIcon.setVisibility(View.GONE);
			mQueueMessage.setVisibility(View.GONE);
			// chaning header title
			headerText.setText(getResources().getString(
					R.string.poll_your_opinion));
			mPullToRefreshGridView.setVisibility(View.GONE);
			mProgressBar.setVisibility(View.GONE);
			mCueDetailsBackgroundWideImage.setVisibility(View.VISIBLE);
			feedBackMessage.setVisibility(View.VISIBLE);
			feedBackMessage.setText(mCue.getCueMessage());
			yourOpinionTxt.setVisibility(View.VISIBLE);
			yourOpinionTxt.setText(getResources().getString(
					R.string.please_poll_your_opinion));
			mGridView.setVisibility(View.GONE);
			ideasMainLayout.setVisibility(View.GONE);
			feedbackOptsLayout.setVisibility(View.VISIBLE);
			mCollege.setTextColor(getResources().getColor(R.color.black));
			mAllColleges.setTextColor(getResources().getColor(R.color.black));
			mFriends.setTextColor(getResources().getColor(R.color.black));
			TextView feedback = (TextView) mCollegeLayout
					.findViewById(R.id.feedback);
			feedback.setTextColor(getResources().getColor(R.color.red));
			TextView ideas = (TextView) mAllCollegesLayout
					.findViewById(R.id.ideas);
			ideas.setTextColor(getResources().getColor(R.color.yellow));
			// To show the full width image in feedback enable the view because
			// its hiding in ideas tab
			mCueDetailsBackgroundWideImage.setVisibility(View.VISIBLE);
			mCollegeLayout.setBackgroundColor(getResources().getColor(
					R.color.yellow));
			mAllCollegesLayout.setBackgroundColor(Color.TRANSPARENT);
			globelFeedbackBottomLayout.setVisibility(View.VISIBLE);

			mCurrentStreamType = STREAM_TYPE.COLLEGE;
			fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, false);
			// mFriendsLayout.setBackgroundColor(getResources().getColor(R.color.white));
			// if (mCollegeList != null && mCollegeList.size() > 0) {
			// setAdapter();
			// } else {
			// fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, false);
			// }
			// break;
			break;

		case R.id.allcolleges_button_layout: // Ideas tab for APSpeak
			mErrorMessageView.setVisibility(View.VISIBLE);
			isFeedback = false;
			// changing header title to ideas
			headerText.setText(getResources().getString(R.string.ideas));
			mQueueIcon.setVisibility(View.VISIBLE);
			mQueueMessage.setVisibility(View.VISIBLE);

			ideasMainLayout.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
			feedbackOptsLayout.setVisibility(View.GONE);
			mCueDetailsBackgroundWideImage.setVisibility(View.INVISIBLE);
			feedBackMessage.setVisibility(View.INVISIBLE);
			yourOpinionTxt.setVisibility(View.INVISIBLE);
			mCurrentStreamType = STREAM_TYPE.ALLCOLLEGES;
			mCollege.setTextColor(getResources().getColor(R.color.black));
			mAllColleges.setTextColor(getResources().getColor(R.color.black));
			mFriends.setTextColor(getResources().getColor(R.color.black));
			feedback = (TextView) mCollegeLayout.findViewById(R.id.feedback);
			feedback.setTextColor(getResources().getColor(R.color.tab_color));

			ideas = (TextView) mAllCollegesLayout.findViewById(R.id.ideas);
			ideas.setTextColor(getResources().getColor(R.color.red));
			feedback.setTextColor(getResources().getColor(R.color.yellow));
			mCollegeLayout.setBackgroundColor(Color.TRANSPARENT);
			mAllCollegesLayout.setBackgroundColor(getResources().getColor(
					R.color.yellow));
			mCueDetailsBackgroundWideImage.setVisibility(View.GONE);
			if (mGridView != null) {
				mGridView.setNumColumns(2);
				fetchNextBatch(0, Constants.IDEAS_FETCHLIMIT, false);
			}
			// mFriendsLayout.setBackgroundColor(getResources().getColor(R.color.white));
			// if (mAllCollegesList != null && mAllCollegesList.size() > 0) {
			// setAdapter();
			// } else {
			// fetchNextBatch(0, Constants.IDEAS_FETCHLIMIT, false);
			// }
			break;

		/*
		 * case R.id.friends_button_layout: mCurrentStreamType =
		 * STREAM_TYPE.FRIENDS; mCollege.setTextColor(getResources().getColor
		 * (R.color.tab_color));
		 * mAllColleges.setTextColor(getResources().getColor
		 * (R.color.tab_color));
		 * mFriends.setTextColor(getResources().getColor(R.color.white));
		 * mCollegeLayout
		 * .setBackgroundColor(getResources().getColor(R.color.white));
		 * mAllCollegesLayout
		 * .setBackgroundColor(getResources().getColor(R.color.white));
		 * mFriendsLayout
		 * .setBackgroundColor(getResources().getColor(R.color.tab_color)); if
		 * (mFriendsList != null && mFriendsList.size() > 0) { setAdapter(); }
		 * else { fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, false); } break;
		 */default:
			break;
		}
	}

	/**
	 * Sets asset's image
	 * 
	 * @param position
	 * @param assetImage
	 */
	public void setAssetImage(final String imageURL, final ImageView imageView,
			final ProgressWheel progresswheel) {
		if (imageView == null) {
			return;
		}
		if (!TextUtils.isEmpty(imageURL)) {
			imageView.setVisibility(View.INVISIBLE);
			ImageLoader.getInstance().displayImage(imageURL, imageView,
					options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {

							if (progresswheel != null) {
								progresswheel.incrementProgress(0);
								progresswheel.setVisibility(View.VISIBLE);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(View.GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							if (progresswheel != null)
								progresswheel.setVisibility(View.GONE);
							imageView.setVisibility(View.VISIBLE);
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub
							if (progresswheel != null)
								progresswheel.setVisibility(View.GONE);
						}
					}, new ImageLoadingProgressListener() {

						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							if (progresswheel != null)
								progresswheel.incrementProgress((current * 360)
										/ total);
						}
					});

		}
	}

	private void fetchCueDetailsViewBackgroundWideImage() {
		if (mCue != null && !TextUtils.isEmpty(mCue.getBackgroundURLWide())) {
			WhatsayAssetDownloadHandler handler = new WhatsayAssetDownloadHandler(
					this);
			WhatsayAssetDownloadThread thread = new WhatsayAssetDownloadThread(
					getActivity(), mCue.getBackgroundURLWide(), handler);
			thread.start();
		}
	}

	public void assetDownloadStatus(String assetDownloadURL,
			String assetDownloadPath, boolean isSucees) {
		if (isSucees && mCueDetailsBackgroundWideImage != null
				&& !TextUtils.isEmpty(assetDownloadPath)) {
			File file = new File(assetDownloadPath);
			if (file.exists()) {
				try {
					BitmapDrawable bmpDrawable = new BitmapDrawable(
							getResources(), file.getAbsolutePath());
					if (bmpDrawable != null
							&& bmpDrawable.getMinimumWidth() > 0
							&& bmpDrawable.getMinimumHeight() > 0) {
						mCueDetailsBackgroundWideImage
								.setImageDrawable(bmpDrawable);
						mCueDetailsBackgroundWideImage
								.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
					Logger.logStackTrace(e);
				}
			}
		}
	}

	/**
	 * Refreshes stream so that we will be displaying latest expressions as well
	 */
	public void refreshStream() {
		fetchNextBatch(0, Constants.BATCH_FETCHLIMIT, true);
	}

	/**
	 * Refreshes Queue layout with number of expressions that are in queue for
	 * submission
	 */
	public void refreshQueueLayout() {
		ExpressionsSubmitQueueDAO dao = new ExpressionsSubmitQueueDAO(
				getActivity());
		if (dao != null) {
			String groupId = AppPropertiesUtil.getGroupId(getActivity());
			String userId = AppPropertiesUtil.getUserID(getActivity());
			String cueId = null;
			if (mCue != null) {
				cueId = mCue.getCueId();
			}
			// Get all the expressions count from db (Both with status PENDING
			// and FAILED)
			int count = dao.getExpressionsCountFromDB(groupId, userId, cueId,
					null);
			int pendingCount = dao.getExpressionsCountFromDB(groupId, userId,
					cueId, SUBMISSION_STATUS.PENDING);
			int failedExpressionsCount = dao.getExpressionsCountFromDB(groupId,
					userId, cueId, SUBMISSION_STATUS.FAILED);
			if (pendingCount > 0) {
				mRefreshRequired = true;
			}
			if (mQueueLayout != null) {
				if (count > 0) {
					// Update Queued expressions summary message
					if (mQueueMessage != null) {
						if (count == failedExpressionsCount) {
							// All the expressions are with FAILED status
							mQueueMessage
									.setText(count
											+ getResources()
													.getString(
															R.string.failed_expressions_count_message));
						} else {
							mQueueMessage
									.setText(count
											+ getResources()
													.getString(
															R.string.queued_expressions_count_message));
						}
					}
					// Show expression thumbnail
					if (mQueueIcon != null) {
						if (count == 1) {
							ArrayList<ExpressionSubmitQueueBean> expressions = dao
									.getExpressionsFromDB(groupId, userId,
											cueId, null);
							if (expressions != null && expressions.size() > 0) {
								ExpressionSubmitQueueBean expression = expressions
										.get(0);
								if (expression != null) {
									String filePath = expression.getFilePath();
									if (!TextUtils.isEmpty(filePath)) {
										File file = new File(filePath);
										if (file != null && file.exists()) {
											try {
												Bitmap expressionBitmap = BitmapFactory
														.decodeFile(file
																.getAbsolutePath());
												mQueueIcon
														.setImageBitmap(expressionBitmap);
											} catch (Exception e) {

											}
										} else {
											mQueueIcon
													.setImageResource(R.drawable.queue_icon);
										}
									}
								}
							}
						} else {
							mQueueIcon.setImageResource(R.drawable.queue_icon);
						}
					}
					// Enable Queue layout clickable
					mQueueLayout.setClickable(true);
					mQueueLayout.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							extractAndProvideQueuedExpressionsListView();
						}
					});
					// Refresh stream when expressions with PENDING status count
					// reaches zero and refreshRequired flag is true
					if (pendingCount == 0 && mRefreshRequired) {
						mRefreshRequired = false;
						refreshStream();
					}
					mQueueLayout.setVisibility(View.VISIBLE);
				} else {
					if (mQueueLayout.getVisibility() == View.VISIBLE) {
						// Refresh stream when all the expressions with PENDING
						// status are submitted
						// to server
						refreshStream();
					}
					mQueueLayout.setVisibility(View.GONE);
				}
			}

			// If QueuedExpressions list dialog is being showed then refresh it
			if (mQueuedExpressionsDialog != null
					&& mQueuedExpressionsDialog.isShowing()) {
				ArrayList<ExpressionSubmitQueueBean> queuedExpressions = dao
						.getExpressionsFromDB(groupId, userId, cueId, null);
				if (mQueuedExpressionsListView != null) {
					// Dismiss dialog if there is no expression to display in
					// the list
					if (queuedExpressions == null
							|| queuedExpressions.size() < 1) {
						mQueuedExpressionsDialog.dismiss();
					} else {
						// Refresh Queued expressions list
						mQueuedExpressionsAdapter = new QueuedExpressionsAdapter(
								this, queuedExpressions);
						mQueuedExpressionsListView
								.setAdapter(mQueuedExpressionsAdapter);
						mQueuedExpressionsAdapter.notifyDataSetChanged();
					}
				}
			}
		}

		// Trigger submission of queued expressions to Whatsay server

		AutoSendManager.getInstance(getActivity().getApplicationContext())
				.startSending();
	}

	/**
	 * Shows stream content in ListView by hiding current GridView
	 */
	public void showListView() {
		if (mPullToRefreshGridView != null) {
			mPullToRefreshGridView.setVisibility(View.GONE);
		}
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.setVisibility(View.GONE);
		}
		/*
		 * if (mListView != null) { mListView.setVisibility(View.VISIBLE); } if
		 * (mGridView != null) { mGridView.setVisibility(View.INVISIBLE); }
		 */
		mIsGridviewInUse = false;
		if (mListAdapter != null) {
			mListAdapter.setIsGridViewInUseFlag(false);
		}
		setAdapter();
	}

	/**
	 * Shows stream content in GridView by hiding current ListView
	 */
	public void showGridView() {
		if (mPullToRefreshListView != null) {
			mPullToRefreshListView.setVisibility(View.GONE);
		}
		if (mPullToRefreshGridView != null && !isFeedback) {
			mPullToRefreshGridView.setVisibility(View.VISIBLE);
		}
		/*
		 * if (mGridView != null) { mGridView.setVisibility(View.VISIBLE); } if
		 * (mListView != null) { mListView.setVisibility(View.INVISIBLE); }
		 */
		mIsGridviewInUse = true;
		if (mListAdapter != null) {
			mListAdapter.setIsGridViewInUseFlag(true);
		}
		setAdapter();
	}

	public void showListViewOrGridView() {
		if (mGridView != null) {
			mGridView.setNumColumns(2);
			showGridView();
		}
	}

	/**
	 * Returns the flag that says whether gridview is in use or not
	 * 
	 * @return
	 */
	public boolean isGridViewInUse() {
		return this.mIsGridviewInUse;
	}

	public void showUserOptionsDialog(final String assetId, final String userId) {

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
				if (Util.isOnline(getActivity())) {
					if (!TextUtils.isEmpty(assetId)) {
						AssetInappropriateThread thread = new AssetInappropriateThread(
								GlobalStreamsFragment.this.getActivity(),
								assetId, userId, new InappropriateFlagHandler(
										GlobalStreamsFragment.this));
						thread.start();
					}
				} else {
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_network),
							Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});

		userFlag.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (Util.isOnline(getActivity())) {
					if (!TextUtils.isEmpty(assetId)) {
						UserInappropriateThread thread = new UserInappropriateThread(
								GlobalStreamsFragment.this.getActivity(), uId,
								userId, new InappropriateFlagHandler(
										GlobalStreamsFragment.this));
						thread.start();
					}
				} else {
					Toast.makeText(getActivity(),
							getResources().getString(R.string.no_network),
							Toast.LENGTH_SHORT).show();
				}
				dialog.dismiss();
			}
		});
		deleteTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (Util.isOnline(getActivity())) {
					if (!TextUtils.isEmpty(assetId)) {
						if (mProgressBar != null
								&& (mProgressBar.getVisibility() == View.INVISIBLE || mProgressBar
										.getVisibility() == View.GONE)) {
							mProgressBar.setVisibility(View.GONE);
						}
						AssetDeleteHandler handler = new AssetDeleteHandler(
								GlobalStreamsFragment.this);
						AssetDeleteThread thread = new AssetDeleteThread(
								GlobalStreamsFragment.this.getActivity(),
								assetId, handler);
						thread.start();
					}
				} else {
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
		Toast.makeText(this.getActivity().getApplicationContext(),
				getResources().getString(R.string.server_call_success),
				Toast.LENGTH_LONG).show();
	}

	public void inappropriateFailure() {
		Toast.makeText(this.getActivity().getApplicationContext(),
				getResources().getString(R.string.server_failure),
				Toast.LENGTH_LONG).show();
	}

	public void assetDeleteSuccess(String assetId) {
		if (!TextUtils.isEmpty(assetId) && isAdded()) {
			ArrayList<StreamAsset> streamAssets = null;
			if (mCurrentStreamType != null) {
				if (mCurrentStreamType == STREAM_TYPE.ALLCOLLEGES) {
					streamAssets = mAllCollegesList;
				} else if (mCurrentStreamType == STREAM_TYPE.COLLEGE) {
					streamAssets = mCollegeList;
				} else if (mCurrentStreamType == STREAM_TYPE.FRIENDS) {
					streamAssets = mFriendsList;
				}

				if (streamAssets != null) {
					for (int i = 0; i < streamAssets.size(); i++) {
						if (streamAssets.get(i).getAssetId().equals(assetId)) {
							streamAssets.remove(i);
							if (mIsGridviewInUse) {
								if (mGridAdapter != null) {
									mGridAdapter.notifyDataSetChanged();
								}
							} else {
								if (mListAdapter != null) {
									mListAdapter.notifyDataSetChanged();
								}
							}
							if (mProgressBar != null
									&& (mProgressBar.getVisibility() == View.VISIBLE)) {
								mProgressBar.setVisibility(View.GONE);
							}
							Toast.makeText(
									this.getActivity().getApplicationContext(),
									getResources().getString(
											R.string.delete_success),
									Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		}
	}

	public void assetDeleteFailed(String assetId) {
		if (mProgressBar != null
				&& (mProgressBar.getVisibility() == View.VISIBLE)) {
			mProgressBar.setVisibility(View.GONE);
		}
		Toast.makeText(this.getActivity().getApplicationContext(),
				getResources().getString(R.string.delete_failure),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * Inflates queued expressions list view
	 */
	public void extractAndProvideQueuedExpressionsListView() {
		if (getActivity() != null) {
			View queuedExpressionsListView = (View) LayoutInflater.from(
					getActivity()).inflate(R.layout.failedexpressions_list,
					null);
			mQueuedExpressionsListView = (ListView) queuedExpressionsListView
					.findViewById(R.id.failedExpressionsListView);
			mQueuedExpressionsListView.setCacheColorHint(Color.TRANSPARENT);
			String groupId = AppPropertiesUtil.getGroupId(getActivity());
			String userId = AppPropertiesUtil.getUserID(getActivity());
			String cueId = null;
			if (mCue != null) {
				cueId = mCue.getCueId();
			}
			ExpressionsSubmitQueueDAO dao = new ExpressionsSubmitQueueDAO(
					getActivity());
			if (dao != null) {
				ArrayList<ExpressionSubmitQueueBean> queuedExpressions = dao
						.getExpressionsFromDB(groupId, userId, cueId, null);
				if (queuedExpressions != null && queuedExpressions.size() > 0) {
					mQueuedExpressionsAdapter = new QueuedExpressionsAdapter(
							GlobalStreamsFragment.this, queuedExpressions);
					mQueuedExpressionsListView
							.setAdapter(mQueuedExpressionsAdapter);
					addQueuedExpressionsListView(queuedExpressionsListView);
				}
			}
		}
	}

	/**
	 * Removes dialog that shows Queued Expressions ListView
	 */
	public void removeQueuedExpressionsListView() {
		if (mQueuedExpressionsDialog != null
				&& mQueuedExpressionsDialog.isShowing()) {
			mQueuedExpressionsDialog.dismiss();
		}
	}

	/**
	 * Add dialog that shows Queued Expressions
	 * 
	 * @param queuedExpressionsListView
	 */
	public void addQueuedExpressionsListView(View queuedExpressionsListView) {
		if (queuedExpressionsListView != null) {
			mQueuedExpressionsDialog = new Dialog(getActivity(),
					android.R.style.Theme_Light_NoTitleBar);
			mQueuedExpressionsDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			mQueuedExpressionsDialog
					.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mQueuedExpressionsDialog.setContentView(queuedExpressionsListView);
			mQueuedExpressionsDialog
					.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
							mQueuedExpressionsListView = null;
							refreshQueueLayout();
						}
					});
			mQueuedExpressionsDialog.show();
		}
	}

	@Override
	public void onClick(View v) {

		FeedBackType feedBackType = FeedBackType.GOOD;
		switch (v.getId()) {
		case R.id.img_btn_awesome:
			feedBackType = FeedBackType.GOOD;
			break;
		case R.id.img_btn_average:
			feedBackType = FeedBackType.AVERAGE;
			break;
		case R.id.img_btn_bad:
			feedBackType = FeedBackType.BAD;
			break;
		case R.id.create_idea_main_layout:
		case R.id.create_idea_image:
			Intent intent = new Intent(getActivity(),
					ImageSelectionFragmentActivity.class);
			if (mCue != null && !TextUtils.isEmpty(mCue.getCueId())) {
				intent.putExtra(Constants.CUE_ID, mCue.getCueId());
			}
			getActivity().startActivityForResult(intent,
					AppNewChildActivity.SUBMIT_EXPRESSION);
			break;
		default:
			break;
		}
		FeedBack feedBack = new FeedBack();
		if (mCollegeList != null && mCollegeList.size() > 0) {
			feedBack.setAsset_id(mCollegeList.get(0).getAssetId());
		}
		feedBack.setUser_id(AppPropertiesUtil.getUserID(getActivity()));
		feedBack.setFeedback(feedBackType);

		RestClient
				.getInstance()
				.getRestClient(getActivity())
				.postFeedBack(feedBack,
						new FeedBackCallBack(getActivity(), feedBack));

	}
}
