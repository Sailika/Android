package com.paradigmcreatives.apspeak.stream.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.model.User;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.app.util.dialogs.WhatsayDialogsUtil;
import com.paradigmcreatives.apspeak.app.util.download.handlers.AssetDetailsDownloadHandler;
import com.paradigmcreatives.apspeak.app.util.download.tasks.AssetDetailsDownloadThread;
import com.paradigmcreatives.apspeak.app.util.share.ShareUtil;
import com.paradigmcreatives.apspeak.assets.handlers.AssetDeleteHandler;
import com.paradigmcreatives.apspeak.assets.handlers.InappropriateFlagHandler;
import com.paradigmcreatives.apspeak.assets.tasks.AssetDeleteThread;
import com.paradigmcreatives.apspeak.assets.tasks.AssetInappropriateThread;
import com.paradigmcreatives.apspeak.assets.tasks.UserInappropriateThread;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.discovery.listeners.UserNetworkListClickListener;
import com.paradigmcreatives.apspeak.doodleboard.send.AssetSubmitHelper;
import com.paradigmcreatives.apspeak.stream.adapters.AssetDetailsWithCommentsAdapter;
import com.paradigmcreatives.apspeak.stream.adapters.AssetLikedPeopleAdapter;
import com.paradigmcreatives.apspeak.stream.adapters.AssetDetailsWithCommentsAdapter.ViewHolder;
import com.paradigmcreatives.apspeak.stream.handlers.GetAssetLikesHandler;
import com.paradigmcreatives.apspeak.stream.handlers.GetStreamHandler;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamClickListener;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamGestureListener;
import com.paradigmcreatives.apspeak.stream.listeners.UserStreamOnTouchListener;
import com.paradigmcreatives.apspeak.stream.tasks.GetAssetLikesTask;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread;
import com.paradigmcreatives.apspeak.stream.tasks.GetStreamThread.STREAM_TYPE;
import com.paradigmcreatives.apspeak.stream.util.StreamAssetUtil;

/**
 * Fragment that displays details of a stream asset, such as, asset image,
 * likes, reposts, original tag, description, list of people who liked asset,
 * list of tags added by user, etc
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetDetailsWithCommentsFragment extends Fragment {

	private String assetId;
	private DisplayImageOptions options;

	private View rootView;
	private ListView mListView;
	private AssetDetailsWithCommentsAdapter mAdapter;
	private ProgressBar progressBar;
	private ViewHolder mFirstChildViewHolder;
	private StreamAsset mFirstChildAsset;
	private ImageView assetLoveAction;
	private ImageView assetCommentAction;
	private ProgressDialog progressDialog;
	ArrayList<Friend> mAssetLikedUsersList;
	ArrayList<StreamAsset> mCommentsList;
	private boolean isRefreshPending;
	private Typeface mRobotoBold;
	private Typeface mRobotoRegular;
	private final String STREAM_ASSET = "stream_asset";
	private final String STREAM_ASSET_ID = "stream_asset_id";

	private Dialog assetDetailsOverlayDialog;
	private RelativeLayout assetDetailsOverlayLayout;
	private ImageView helpOverlayClose;

	/**
	 * Default Constructor
	 */
	public AssetDetailsWithCommentsFragment() {
		super();
	}

	/**
	 * Constructors that accepts StreamAsset object as parameter
	 * 
	 * @param asset
	 */
	public AssetDetailsWithCommentsFragment(StreamAsset asset) {
		super();
		this.mFirstChildAsset = asset;
		this.assetId = (asset != null) ? asset.getAssetId() : null;
	}

	/**
	 * Constructors that accepts asset id as paramete
	 * 
	 * @param asset
	 */
	public AssetDetailsWithCommentsFragment(String assetId) {
		super();
		this.assetId = assetId;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.assetdetails_withcomments_layout,
				container, false);

		assetDetailsOverlayDialog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		assetDetailsOverlayDialog.setContentView(R.layout.help_asset_details);
		assetDetailsOverlayLayout = (RelativeLayout) assetDetailsOverlayDialog
				.findViewById(R.id.assets_details_overlay_layout);
		helpOverlayClose = (ImageView) assetDetailsOverlayDialog
				.findViewById(R.id.close_view);
		if (!AppPropertiesUtil.getAssetDetailsHelpOverlayStatus(getActivity())) {
			showOverLay();
			AppPropertiesUtil.setAssetDetailsHelpOverlayStatus(getActivity(),
					true);
		} else {
			assetDetailsOverlayLayout.setVisibility(View.GONE);
		}

		if (savedInstanceState != null) {
			if (savedInstanceState.containsKey(STREAM_ASSET)) {
				mFirstChildAsset = savedInstanceState
						.getParcelable(STREAM_ASSET);
			}
			if (savedInstanceState.containsKey(STREAM_ASSET_ID)) {
				assetId = savedInstanceState.getString(STREAM_ASSET_ID);
			}
		}

		// Initialize progress dialog
		progressDialog = new ProgressDialog(getActivity(),
				ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog
				.setTitle(getString(R.string.asset_details_progress_title));
		// progressDialog.setMessage("Getting asset details. Please give us a moment");
		progressDialog
				.setMessage(getString(R.string.asset_details_progress_text));
		progressDialog.setCancelable(false);

		initUI(rootView);

		if (mFirstChildAsset != null) {
			ArrayList<StreamAsset> list = new ArrayList<StreamAsset>();
			list.add(mFirstChildAsset);
			setAdapter(list);
			// Fetch Asset's Likes from server
			fetchAssetLikesFromServer(assetId);
			// Fetch Asset's Comments from Server
			// fetchAssetCommentsFromServer(assetId);
		} else {
			// As asset bean not available, hence fetch details from server
			fetchAssetDetailsFromServer(assetId);
		}

		return rootView;
	}

	@Override
	public void onResume() {
		if (isRefreshPending) {
			isRefreshPending = false;
			refreshView();
		}
		super.onResume();
	}

	@Override
	public void onPause() {
		isRefreshPending = true;
		super.onPause();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (outState != null) {
			if (mFirstChildAsset != null) {
				outState.putParcelable(STREAM_ASSET, mFirstChildAsset);
			}
			if (assetId != null) {
				outState.putString(STREAM_ASSET_ID, assetId);
			}
		}
		super.onSaveInstanceState(outState);
	}

	private void initUI(View rootView) {
		if (rootView != null && mFirstChildAsset != null) {

			mRobotoBold = Typeface.createFromAsset(getActivity().getAssets(),
					"Roboto-Bold.ttf");
			mListView = (ListView) rootView
					.findViewById(R.id.assetdetails_listview);
			progressBar = (ProgressBar) rootView
					.findViewById(R.id.assetdetails_progressBar);
			assetLoveAction = (ImageView) rootView
					.findViewById(R.id.assetbottom_action_love);
			assetCommentAction = (ImageView) rootView
					.findViewById(R.id.assetbottom_action_comment);
			// mShareView = (ImageView) rootView.findViewById(R.id.share_image);
			StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
			assetUtil.setAssetLikeIcon(mFirstChildAsset, assetLoveAction);
			assetUtil.setAssetCommentIcon(mFirstChildAsset, assetCommentAction);

			UserStreamClickListener listener = new UserStreamClickListener(
					this, mFirstChildAsset);
			assetLoveAction.setOnClickListener(listener);
			assetCommentAction.setOnClickListener(listener);
			// mShareView.setOnClickListener(listener);
		}
	}

	public View initFirstChild(View view, ViewHolder holder) {
		ViewHolder viewHolder = holder;
		view = LayoutInflater.from(getActivity()).inflate(
				R.layout.assetdetails_firstchild_layout, null);

		// Asset main layout
		viewHolder.assetOwnerPic = (ImageView) view
				.findViewById(R.id.asset_owner_pic);
		viewHolder.assetOwnerName = (TextView) view
				.findViewById(R.id.asset_owner_name);
		viewHolder.assetOwnerGroupName = (TextView) view
				.findViewById(R.id.asset_owner_group_name);
		viewHolder.assetCreatedTimestamp = (TextView) view
				.findViewById(R.id.asset_created_date);
		viewHolder.assetImage = (ImageView) view.findViewById(R.id.asset_image);
		viewHolder.assetLoveAnimationImage = (ImageView) view
				.findViewById(R.id.asset_love_animation_image);
		viewHolder.assetLoves = (TextView) view.findViewById(R.id.asset_loves);
		viewHolder.assetComments = (TextView) view
				.findViewById(R.id.asset_comments);
		viewHolder.optionsImage = (ImageView) view
				.findViewById(R.id.asset_options);
		viewHolder.progreswheel = (ProgressWheel) view
				.findViewById(R.id.progressBarwheelone);
		// Asset liked people layout
		viewHolder.assetLovedUsersLayout = (RelativeLayout) view
				.findViewById(R.id.asset_people_liked_layout);
		viewHolder.assetLovedUsersGrid = (GridView) view
				.findViewById(R.id.asset_people_liked_grid_view);
		viewHolder.optionsImage = (ImageView) view
				.findViewById(R.id.asset_options);
		viewHolder.assetLovedUsersSeeAll = (TextView) view
				.findViewById(R.id.asset_see_all);
		viewHolder.assetLovedUsersSeeAll.setTypeface(mRobotoBold);

		viewHolder.assetOwnerName.setTypeface(mRobotoBold);
		viewHolder.assetCreatedTimestamp.setTypeface(mRobotoBold);
		viewHolder.assetLoves.setTypeface(mRobotoRegular);
		TextView assetPeopleLiked = (TextView) view
				.findViewById(R.id.asset_people_liked);
		assetPeopleLiked.setTypeface(mRobotoBold);

		options = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).displayer(new FadeInBitmapDisplayer(250))
				.build();
		StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
		assetUtil.setAssetOwnerPic(mFirstChildAsset, viewHolder.assetOwnerPic);
		assetUtil.setOwnerName(mFirstChildAsset, viewHolder.assetOwnerName);
		assetUtil.setOwnerGroupName(mFirstChildAsset,
				viewHolder.assetOwnerGroupName);
		assetUtil.setAssetCreateDate(mFirstChildAsset,
				viewHolder.assetCreatedTimestamp);
		assetUtil.setAssetImage(mFirstChildAsset, viewHolder.assetImage,
				viewHolder.progreswheel);
		assetUtil.setAssetLoves(mFirstChildAsset, viewHolder.assetLoves);
		assetUtil.setAssetComments(mFirstChildAsset, viewHolder.assetComments);

		UserStreamClickListener listener = new UserStreamClickListener(this,
				mFirstChildAsset);
		listener.setAssetAnimationIconHolder(viewHolder.assetLoveAnimationImage);
		viewHolder.assetOwnerPic.setOnClickListener(listener);
		viewHolder.assetOwnerName.setOnClickListener(listener);
		viewHolder.optionsImage.setOnClickListener(listener);
		viewHolder.assetLoves.setOnClickListener(listener);
		Drawable drawable = viewHolder.assetImage.getDrawable();
		if (drawable != null) {
			mFirstChildAsset.setAssetSnapBitmap(((BitmapDrawable) drawable)
					.getBitmap());
		}
		UserStreamGestureListener gestureListener = new UserStreamGestureListener(
				this, mFirstChildAsset);
		gestureListener
				.setAssetAnimationIconHolder(viewHolder.assetLoveAnimationImage);
		GestureDetector gd = new GestureDetector(getActivity()
				.getApplicationContext(), gestureListener);
		UserStreamOnTouchListener touchListener = new UserStreamOnTouchListener(
				gd, this);
		viewHolder.assetImage.setOnTouchListener(touchListener);

		mFirstChildViewHolder = viewHolder;
		// loadPeopleWhoLikedAsset(mAssetLikedUsersList);
		return view;
	}

	/**
	 * Refreshes view with latest data
	 */
	public void refreshView() {
		refreshAdapter();
		fetchAssetLikesFromServer(this.assetId);
		// fetchAssetCommentsFromServer(this.assetId);
	}

	/**
	 * Loads people who liked asset
	 */
	private void loadPeopleWhoLikedAsset(ArrayList<Friend> likedUsers) {
		if (isAdded() && likedUsers != null && likedUsers.size() > 0
				&& mFirstChildViewHolder != null) {
			try {
				Collection<Friend> users = new ConcurrentSkipListSet<Friend>(
						likedUsers);
				if (users != null) {
					ArrayList<Friend> usersList = new ArrayList<Friend>(users);

					// Loves count and Loved Icon
					StreamAssetUtil assetUtil = new StreamAssetUtil(this,
							options);
					assetUtil.setAssetLikeIcon(mFirstChildAsset,
							assetLoveAction);
					if (mFirstChildViewHolder != null) {
						assetUtil.setAssetLoves(mFirstChildAsset,
								mFirstChildViewHolder.assetLoves);
						assetUtil.setAssetLikeIcon(mFirstChildAsset,
								assetLoveAction);
					}

					if (usersList != null && usersList.size() > 0) {
						// Set VISIBLE to Liked Users layout
						if (mFirstChildViewHolder.assetLovedUsersLayout != null) {
							mFirstChildViewHolder.assetLovedUsersLayout
									.setVisibility(View.VISIBLE);
						}
						// Set adapter to gridview
						AssetLikedPeopleAdapter adapter = new AssetLikedPeopleAdapter(
								getActivity(), usersList);
						if (mFirstChildViewHolder.assetLovedUsersGrid != null) {
							mFirstChildViewHolder.assetLovedUsersGrid
									.setAdapter(adapter);
							mFirstChildViewHolder.assetLovedUsersGrid
									.setOnItemClickListener(new UserNetworkListClickListener(
											getActivity(), this,
											UserNetwork.LOVED_USERS));
							adapter.notifyDataSetChanged();
						}

						if (users.size() > 5
								&& mFirstChildViewHolder.assetLovedUsersSeeAll != null) {
							mFirstChildViewHolder.assetLovedUsersSeeAll
									.setVisibility(View.VISIBLE);
							UserStreamClickListener listener = new UserStreamClickListener(
									this, mFirstChildAsset);
							mFirstChildViewHolder.assetLovedUsersSeeAll
									.setOnClickListener(listener);
						} else {
							mFirstChildViewHolder.assetLovedUsersSeeAll
									.setVisibility(View.GONE);
						}
					} else {
						// Hide People Liked Asset layout
						if (mFirstChildViewHolder.assetLovedUsersLayout != null) {
							mFirstChildViewHolder.assetLovedUsersLayout
									.setVisibility(View.GONE);
						}
					}
				}
			} catch (Exception e) {

			}
		}
	}

	/**
	 * Gets called when asset relationship(LOVE, etc) is updated with server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRelationshipUpdated(String assetId,
			ASSOCIATION_TYPE relationship) {

		/*
		 * Change the LIKE icon and total likes count for the asset
		 */
		if (mFirstChildAsset != null && isAdded()) {

			if (mFirstChildAsset.getAssetId().equals(assetId)) {
				int likesCount = 0;
				HashMap<ASSOCIATION_TYPE, Integer> associations = mFirstChildAsset
						.getAssetAssociations();
				if (associations != null) {
					if (associations.containsKey(ASSOCIATION_TYPE.LOVE)) {
						likesCount = associations.get(ASSOCIATION_TYPE.LOVE);
					}
				} else {
					associations = new HashMap<ASSOCIATION_TYPE, Integer>();
				}
				// Increment the likes/LOVE count by 1
				likesCount = likesCount + 1;
				// Set new likes/LOVE count to asset associations
				associations.put(ASSOCIATION_TYPE.LOVE, likesCount);
				mFirstChildAsset.setAssetAssociations(associations);
				mFirstChildAsset.setAssetIsLoved(true);
				StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
				assetUtil.setAssetLikeIcon(mFirstChildAsset, assetLoveAction);
				if (mFirstChildViewHolder != null) {
					assetUtil.setAssetLoves(mFirstChildAsset,
							mFirstChildViewHolder.assetLoves);
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
		 * TODO: Uncomment for Google Analytics
		 * GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
		 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME,
		 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
		 * GoogleAnalyticsConstants.DSD_LOVE_FAILURE_BUTTON);
		 */

		/*
		 * Handle the failure by changing respective relationship
		 * icon/count/details,etc
		 */
	}

	/**
	 * Gets called when asset relationship(LOVE, etc) is removed with server
	 * 
	 * @param assetId
	 * @param relationship
	 */
	public void assetRelationshipRemoveSuccess(String assetId,
			ASSOCIATION_TYPE relationship) {
		/*
		 * Change the LIKE icon and total likes count for the asset
		 */
		if (mFirstChildAsset != null && isAdded()) {

			if (mFirstChildAsset.getAssetId().equals(assetId)) {
				int likesCount = 0;
				HashMap<ASSOCIATION_TYPE, Integer> associations = mFirstChildAsset
						.getAssetAssociations();
				if (associations != null) {
					if (associations.containsKey(ASSOCIATION_TYPE.LOVE)) {
						likesCount = associations.get(ASSOCIATION_TYPE.LOVE);
					}
				} else {
					associations = new HashMap<ASSOCIATION_TYPE, Integer>();
				}
				// Increment the likes/LOVE count by 1
				likesCount = likesCount - 1;
				// Set new likes/LOVE count to asset associations
				associations.put(ASSOCIATION_TYPE.LOVE, (likesCount < 0) ? 0
						: likesCount);
				mFirstChildAsset.setAssetAssociations(associations);
				mFirstChildAsset.setAssetIsLoved(false);
				StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
				assetUtil.setAssetLikeIcon(mFirstChildAsset, assetLoveAction);
				if (mFirstChildViewHolder != null) {
					assetUtil.setAssetLoves(mFirstChildAsset,
							mFirstChildViewHolder.assetLoves);
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
	 * Initiates sharing asset via Social Networking or Third party apps
	 */
	public void shareAsset() {
		if (mFirstChildViewHolder != null
				&& mFirstChildViewHolder.assetImage != null) {
			/*
			 * if(progressDialog != null){ progressDialog.show(); }
			 */
			Bitmap bmp = null;
			try {
				bmp = ((BitmapDrawable) mFirstChildViewHolder.assetImage
						.getDrawable()).getBitmap();
			} catch (Exception e) {
				// Do nothing
			}
			if (bmp != null) {
				shareImageViaAppsDialog(saveBitmap(bmp));
			}
		}
	}

	/**
	 * Launches the Share Via Apps dialog from which asset image can be shared
	 * 
	 * @param shareURL
	 */
	public void shareImageViaAppsDialog(String thumbnailPath) {
		if (!TextUtils.isEmpty(thumbnailPath)) {
			// //TODO Open the image share dialog
			// Intent share = new Intent(Intent.ACTION_SEND);
			// share.setType("image/jpeg");
			// share.putExtra(Intent.EXTRA_STREAM, Uri.parse(thumbnailPath));
			// startActivity(Intent.createChooser(share,
			// getString(R.string.share_title_text)));

			Dialog dialog = WhatsayDialogsUtil.shareDoodleDialog(getActivity(),
					thumbnailPath, "", "",
					ShareUtil.getLinkShareAppsList(getActivity()), false);
			/*
			 * if(progressDialog != null){ progressDialog.dismiss(); }
			 */
			if (dialog != null) {
				dialog.show();
			}
		}
	}

	/**
	 * Saves bitmap to a file
	 * 
	 * @param expressionBitmap
	 * @return
	 */
	public String saveBitmap(final Bitmap expressionBitmap) {
		String fullImageFilePath = null;
		if (expressionBitmap != null) {
			Activity context = getActivity();
			String tempImageFilePath = AppPropertiesUtil
					.getAppDirectory(context)
					+ "/"
					+ context.getString(R.string.whatsay_folder);
			File tempFile = new File(tempImageFilePath);
			if (!tempFile.exists()) {
				tempFile.mkdir();
			}

			String tempImageName = context.getString(R.string.temp_image_name);
			boolean isCreated = AssetSubmitHelper.saveBitmapToTempLocation(
					context, expressionBitmap);
			if (isCreated) {
				fullImageFilePath = tempImageFilePath + tempImageName;
			}
		}
		return fullImageFilePath;
	}

	/**
	 * Fetches asset details from server
	 * 
	 * @param assetId
	 */
	public void fetchAssetDetailsFromServer(String assetId) {
		if (!TextUtils.isEmpty(assetId)) {
			if (progressDialog != null) {
				progressDialog.show();
			}
			AssetDetailsDownloadHandler handler = new AssetDetailsDownloadHandler(
					this);
			AssetDetailsDownloadThread thread = new AssetDetailsDownloadThread(
					getActivity(), assetId, handler);
			thread.start();
		}
	}

	/**
	 * Fetches asset likes from server
	 */
	public void fetchAssetLikesFromServer(String assetId) {
		if (!TextUtils.isEmpty(assetId)) {
			GetAssetLikesHandler handler = new GetAssetLikesHandler(this);
			GetAssetLikesTask thread = new GetAssetLikesTask(getActivity(),
					handler, assetId);
			thread.start();
		}
	}

	/**
	 * Fetches asset comments from server
	 */
	private void fetchAssetCommentsFromServer(String assetId) {
		if (!TextUtils.isEmpty(assetId)) {
			GetStreamHandler handler = new GetStreamHandler(this);
			GetStreamThread thread = new GetStreamThread(getActivity(),
					assetId, handler, STREAM_TYPE.COMMENTS_STREAM, 0, 10);
			thread.start();
		}
	}

	/**
	 * Launches the canvas activity
	 */
	public void launchCanvas() {
		// if (isAdded() && getActivity() != null) {
		// Intent intent = new Intent(getActivity(),
		// DoodlyDooCameraActivity.class);
		// intent.putExtra(Constants.IS_COMMENT, true);
		// intent.putExtra(Constants.COMMENT_ON_ASSET_ID, assetId);
		// // starting CanvasActivity
		// try {
		// getActivity().startActivity(intent);
		// } catch (ActivityNotFoundException anfe) {
		// } catch (Exception e) {
		// }
		// } else {
		// }
	}

	/**
	 * Sets adapter with new set of assets
	 * 
	 * @param assets
	 */
	public void setAdapter(ArrayList<StreamAsset> assets) {
		if (isAdded() && mListView != null && assets != null) {
			AssetDetailsWithCommentsAdapter adapter = new AssetDetailsWithCommentsAdapter(
					this, assets);
			mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mAdapter = adapter;
		}
	}

	public void updateAdapter(ArrayList<StreamAsset> commentsStreamAssets) {
		if (isAdded() && mAdapter != null && commentsStreamAssets != null
				&& commentsStreamAssets.size() > 0) {
			mAdapter.updateAdapter(commentsStreamAssets);
		}
	}

	/**
	 * Refreshes adapter with latest updated data
	 */
	public void refreshAdapter() {
		if (isAdded() && mListView != null && mFirstChildAsset != null) {
			/*
			 * // Update Loves Count if(mAssetLikedUsersList != null &&
			 * mAssetLikedUsersList.size() > 0){ HashMap<ASSOCIATION_TYPE,
			 * Integer> associations = mFirstChildAsset.getAssetAssociations();
			 * associations.put(ASSOCIATION_TYPE.LOVE,
			 * mAssetLikedUsersList.size());
			 * mFirstChildAsset.setAssetAssociations(associations); } // Update
			 * Comments Count if(mAssetCommentedUsersList != null &&
			 * mAssetCommentedUsersList.size() > 0){
			 * mFirstChildAsset.setAssetCommentsCount
			 * (mAssetCommentedUsersList.size()); }
			 */
			ArrayList<StreamAsset> assets = new ArrayList<StreamAsset>();
			assets.add(mFirstChildAsset);
			if (mCommentsList != null && mCommentsList.size() > 0) {
				assets.addAll(mCommentsList);
			}
			AssetDetailsWithCommentsAdapter adapter = new AssetDetailsWithCommentsAdapter(
					this, assets);
			mListView.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mAdapter = adapter;
		}
	}

	/**
	 * Gets called to update asset details
	 * 
	 * @param asset
	 */
	public void setAssetDetailsFetchStatus(StreamAsset asset) {
		// If asset details are fetched then update UI
		if (isAdded()) {
			if (asset != null) {
				this.mFirstChildAsset = asset;
				this.assetId = asset.getAssetId();
				// Update UI with fetched asset details
				initUI(rootView);
				// Update ListView with first child item
				refreshAdapter();
				fetchAssetLikesFromServer(assetId);
				// fetchAssetCommentsFromServer(assetId);
			}
			if (progressDialog != null) {
				progressDialog.dismiss();
			}
		}
	}

	/**
	 * Sets asset liked users list and triggers UI update
	 * 
	 * @param likedUsers
	 */
	public void setAssetLikedUsers(ArrayList<Friend> likedUsers) {
		if (isAdded()) {
			this.mAssetLikedUsersList = likedUsers;
			if (mFirstChildAsset != null && likedUsers != null) {
				// Update Likes/Loves count
				HashMap<ASSOCIATION_TYPE, Integer> associations = mFirstChildAsset
						.getAssetAssociations();
				if (associations == null) {
					associations = new HashMap<ASSOCIATION_TYPE, Integer>();
				}
				associations.put(ASSOCIATION_TYPE.LOVE,
						mAssetLikedUsersList.size());
				mFirstChildAsset.setAssetAssociations(associations);

				// Update whether current user liked or not
				mFirstChildAsset
						.setAssetIsLoved(isUserLiked(mAssetLikedUsersList));

				// Update asset love action
				StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
				assetUtil.setAssetLikeIcon(mFirstChildAsset, assetLoveAction);
			}
			// loadPeopleWhoLikedAsset(mAssetLikedUsersList);
		}
	}

	/**
	 * Returns list of asset liked users
	 * 
	 * @return
	 */
	public ArrayList<Friend> getAssetLikedUsers() {
		return this.mAssetLikedUsersList;
	}

	/**
	 * Sets Asset's comments stream
	 * 
	 * @param commentsStreamAssets
	 */
	public void setCommentsStream(ArrayList<StreamAsset> commentsStreamAssets) {

		if (isAdded()) {
			mCommentsList = commentsStreamAssets;
			if (commentsStreamAssets != null && commentsStreamAssets.size() > 0) {
				if (mFirstChildAsset != null) {
					// Update Comments count
					mFirstChildAsset.setAssetCommentsCount(commentsStreamAssets
							.size());
					// Update whether current user commented or not
					mFirstChildAsset
							.setAssetIsCommented(isUserCommented(mCommentsList));
					// Update asset comment action
					StreamAssetUtil assetUtil = new StreamAssetUtil(this,
							options);
					assetUtil.setAssetCommentIcon(mFirstChildAsset,
							assetCommentAction);
				}
				refreshAdapter();
			} else {
				Toast.makeText(getActivity(), "No comments stream to display",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * Checks whether current user liked the asset or not
	 * 
	 * @param likedUsersList
	 * @return
	 */
	private boolean isUserLiked(ArrayList<Friend> likedUsersList) {
		boolean isLiked = false;
		if (mFirstChildAsset != null && likedUsersList != null) {
			String currentUserId = AppPropertiesUtil.getUserID(getActivity());
			if (!TextUtils.isEmpty(currentUserId)) {
				for (int i = 0; i < likedUsersList.size(); i++) {
					Friend friend = likedUsersList.get(i);
					if (friend != null
							&& friend.getUserId().equals(currentUserId)) {
						isLiked = true;
						break;
					}
				}
			}
		}
		return isLiked;
	}

	/**
	 * Checks whether current user has commented or not
	 * 
	 * @param assetCommentsList
	 * @return
	 */
	private boolean isUserCommented(ArrayList<StreamAsset> assetCommentsList) {
		boolean isCommented = false;
		if (mFirstChildAsset != null && assetCommentsList != null) {
			String currentUserId = AppPropertiesUtil.getUserID(getActivity());
			if (!TextUtils.isEmpty(currentUserId)) {
				for (int i = 0; i < assetCommentsList.size(); i++) {
					StreamAsset comment = assetCommentsList.get(i);
					if (comment != null && comment.getAssetOwner() != null) {
						User commentOwner = comment.getAssetOwner();
						if (!TextUtils.isEmpty(commentOwner.getUserId())
								&& commentOwner.getUserId().equals(
										currentUserId)) {
							isCommented = true;
							break;
						}
					}
				}
			}
		}
		return isCommented;
	}

	public void showUserOptionsDialog() {

		LayoutInflater inflater = (LayoutInflater) this.getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View optionsDialog = inflater.inflate(
				R.layout.user_asset_options_dialog, null);
		final String assetUserId = mFirstChildAsset.getAssetOwner().getUserId();
		final String uId = AppPropertiesUtil
				.getUserID(AssetDetailsWithCommentsFragment.this.getActivity());
		final Dialog dialog = new Dialog(this.getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		TextView deleteTextView = (TextView) optionsDialog
				.findViewById(R.id.delete_image);
		TextView userFlag = (TextView) optionsDialog
				.findViewById(R.id.user_inappropriate);
		TextView assetFlag = (TextView) optionsDialog
				.findViewById(R.id.flag_inappropriate);
		if (assetUserId.equals(uId)) {
			deleteTextView.setVisibility(View.VISIBLE);
			userFlag.setVisibility(View.GONE);
			assetFlag.setVisibility(View.GONE);
		} else {
			deleteTextView.setVisibility(View.GONE);
			userFlag.setVisibility(View.VISIBLE);
			assetFlag.setVisibility(View.VISIBLE);
		}

		deleteTextView.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (Util.isOnline(getActivity())) {
					if (!TextUtils.isEmpty(assetId)) {
						if (progressBar != null
								&& (progressBar.getVisibility() == View.INVISIBLE || progressBar
										.getVisibility() == View.GONE)) {
							progressBar.setVisibility(View.VISIBLE);
						}
						AssetDeleteHandler handler = new AssetDeleteHandler(
								AssetDetailsWithCommentsFragment.this);
						AssetDeleteThread thread = new AssetDeleteThread(
								AssetDetailsWithCommentsFragment.this
										.getActivity(), assetId, handler);
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
		assetFlag.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (Util.isOnline(getActivity())) {
					if (!TextUtils.isEmpty(assetId)) {
						if (progressBar != null
								&& (progressBar.getVisibility() == View.INVISIBLE)
								&& (progressBar.getVisibility() == View.GONE)) {
							progressBar.setVisibility(View.VISIBLE);
						}
						AssetInappropriateThread thread = new AssetInappropriateThread(
								AssetDetailsWithCommentsFragment.this
										.getActivity(), assetId, uId,
								new InappropriateFlagHandler(
										AssetDetailsWithCommentsFragment.this));
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
						if (progressBar != null
								&& (progressBar.getVisibility() == View.INVISIBLE)
								&& (progressBar.getVisibility() == View.GONE)) {
							progressBar.setVisibility(View.VISIBLE);
						}
						UserInappropriateThread thread = new UserInappropriateThread(
								AssetDetailsWithCommentsFragment.this
										.getActivity(), uId, assetUserId,
								new InappropriateFlagHandler(
										AssetDetailsWithCommentsFragment.this));
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

	public void assetDeleteSuccess(String assetId) {
		if (progressBar != null
				&& (progressBar.getVisibility() == View.VISIBLE)) {
			progressBar.setVisibility(View.GONE);
		}
		Toast.makeText(getActivity(),
				getResources().getString(R.string.delete_success),
				Toast.LENGTH_SHORT).show();
		// On successful delete set a flag in the activity result so that the
		// view can be refreshed
		Activity activity = getActivity();
		activity.setResult(Activity.RESULT_OK);
		activity.finish();
		/*
		 * if (mListView != null && !TextUtils.isEmpty(assetId) && mCommentsList
		 * != null && isAdded()) { for (int i = 0; i < mCommentsList.size();
		 * i++) { if (mCommentsList.get(i).getAssetId().equals(assetId)) {
		 * mCommentsList.remove(i); mAdapter.notifyDataSetChanged();
		 * Toast.makeText(this.getActivity().getApplicationContext(),
		 * getResources().getString(R.string.delete_success),
		 * Toast.LENGTH_LONG).show(); } } }
		 */
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

	private void showOverLay() {
		assetDetailsOverlayLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				assetDetailsOverlayDialog.dismiss();
			}
		});
		helpOverlayClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				assetDetailsOverlayDialog.dismiss();
			}
		});
		assetDetailsOverlayDialog.show();
	}

}
