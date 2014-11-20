package com.paradigmcreatives.apspeak.stream.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ConcurrentSkipListSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.paradigmcreatives.apspeak.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.paradigmcreatives.apspeak.app.model.ASSOCIATION_TYPE;
import com.paradigmcreatives.apspeak.app.model.Friend;
import com.paradigmcreatives.apspeak.app.model.StreamAsset;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.JSONConstants;
import com.paradigmcreatives.apspeak.app.util.dialogs.ProgressWheel;
import com.paradigmcreatives.apspeak.app.util.dialogs.WhatsayDialogsUtil;
import com.paradigmcreatives.apspeak.app.util.share.ShareUtil;
import com.paradigmcreatives.apspeak.discovery.fragments.UserNetworkFragment.UserNetwork;
import com.paradigmcreatives.apspeak.discovery.listeners.UserNetworkListClickListener;
import com.paradigmcreatives.apspeak.doodleboard.send.AssetSubmitHelper;
import com.paradigmcreatives.apspeak.stream.adapters.AssetAddedTagsAdapter;
import com.paradigmcreatives.apspeak.stream.adapters.AssetLikedPeopleAdapter;
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
 * Fragment that displays details of a stream asset, such as, asset image, likes, reposts, original tag, description,
 * list of people who liked asset, list of tags added by user, etc
 * 
 * @author Dileep | neuv
 * 
 */
public class AssetDetailsFragment extends Fragment {

    private StreamAsset asset;
    private String assetId;
    private DisplayImageOptions options;

    // Asset main layout
    private View rootView;
    private ImageView assetOwnerPic;
    private TextView assetOwnerName;
    private TextView assetCreatedTimestamp;
    private ImageView assetImage;
    private ImageView assetLoveAnimationIcon;
    // private DoodleView assetDoodleView;
    private TextView assetLoves;
    private TextView assetReposts;
    // private ImageView assetShareAction;
    private ImageView assetLoveAction;
    // private ImageView assetRepostAction;
    private ImageView assetCommentAction;

    // Asset description layout
    private RelativeLayout assetTagDescriptionLayout;
    private TextView assetOriginallyTaggedTextView;
    private TextView assetOriginalTag;
    private TextView assetDescription;

    // Asset liked people layout
    private RelativeLayout assetPeopleLikedLayout;
    private GridView lovedUsersGrid;
    private TextView seeAll;

    // Asset added tags layout
    private RelativeLayout assetTagsLayout;
    private GridView addedTagsGrid;

    // ProgressBar
    private ProgressWheel progresswheel;
    private ProgressDialog progressDialog;
    
    // Asset liked users
    ArrayList<Friend> assetLikedUsersList;

    private String assetFolderPath;

    private Typeface mRobotoBold;
    private Typeface mRobotoRegular;

    private final String STREAM_ASSET = "stream_asset";
    private final String STREAM_ASSET_ID = "stream_asset_id";

    private final int VIEWVISIBLE = 1;
    private final int VIEWGONE = 2;
    private final int PROGRESS = 3;
    private final int INITIALLOADING = 4;
    private final int FINALLOADING = 5;

    /**
     * Default Constructor
     */
    public AssetDetailsFragment() {
	super();
    }

    /**
     * Constructors that accepts StreamAsset object as parameter
     * 
     * @param asset
     */
    public AssetDetailsFragment(StreamAsset asset) {
	super();
	this.asset = asset;
	this.assetId = (asset != null) ? asset.getAssetId() : null;
    }

    /**
     * Constructors that accepts asset id as parameter
     * 
     * @param asset
     */
    public AssetDetailsFragment(String assetId) {
	super();
	this.assetId = assetId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	rootView = inflater.inflate(R.layout.stream_itemdetails_layout_v2, container, false);
	if (savedInstanceState != null) {
	    if (savedInstanceState.containsKey(STREAM_ASSET)) {
		asset = savedInstanceState.getParcelable(STREAM_ASSET);
	    }
	    if (savedInstanceState.containsKey(STREAM_ASSET_ID)) {
		assetId = savedInstanceState.getString(STREAM_ASSET_ID);
	    }
	}

	// Initialize progress dialog
	progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
	progressDialog.setTitle("Asset...");
	// progressDialog.setMessage("Getting asset details. Please give us a moment");
	progressDialog.setMessage("Please wait...");
	progressDialog.setCancelable(false);

	initUI(rootView);

	/*
	 * IF asset exists then download the doodle content(if type is DOODLE), ELSE if assetId exists then fetch asset
	 * details from server
	 */
	/*
	 * TODO: Uncomment when DOODLE type asset is enabled if (asset != null) {
	 * downloadDoodleIfRequired(asset.getAssetId()); } else if (!TextUtils.isEmpty(assetId)) {
	 * downloadAssetFromServer(assetId); }
	 */
	// Fetch Asset's Likes from server
	fetchAssetLikesFromServer(assetId);
	// Fetch Asset's Comments from Server
	fetchAssetCommentsFromServer(assetId);
	return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	if (outState != null) {
	    if (asset != null) {
		outState.putParcelable(STREAM_ASSET, asset);
	    }
	    if (assetId != null) {
		outState.putString(STREAM_ASSET_ID, assetId);
	    }
	}
	super.onSaveInstanceState(outState);
    }

    private void initUI(View rootView) {
	if (rootView != null && asset != null) {
	    mRobotoBold = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Bold.ttf");
	    // Asset main layout
	    assetOwnerPic = (ImageView) rootView.findViewById(R.id.asset_owner_pic);
	    assetOwnerName = (TextView) rootView.findViewById(R.id.asset_owner_name);
	    assetCreatedTimestamp = (TextView) rootView.findViewById(R.id.asset_created_date);
	    assetImage = (ImageView) rootView.findViewById(R.id.asset_image);
	    assetLoveAnimationIcon = (ImageView) rootView.findViewById(R.id.asset_love_animation_image);
	    // assetDoodleView = (DoodleView) rootView.findViewById(R.id.asset_doodleview);
	    assetLoves = (TextView) rootView.findViewById(R.id.asset_loves);
	    assetReposts = (TextView) rootView.findViewById(R.id.asset_reposts);
	    // assetShareAction = (ImageView) rootView.findViewById(R.id.asset_action_share);
	    assetLoveAction = (ImageView) rootView.findViewById(R.id.assetbottom_action_love);
	    // assetRepostAction = (ImageView) rootView.findViewById(R.id.asset_action_repost);
	    assetCommentAction = (ImageView) rootView.findViewById(R.id.assetbottom_action_comment);

	    // Asset description layout
	    assetTagDescriptionLayout = (RelativeLayout) rootView.findViewById(R.id.asset_tag_description_layout);
	    assetOriginallyTaggedTextView = (TextView) rootView.findViewById(R.id.asset_originally_tagged);
	    assetOriginalTag = (TextView) rootView.findViewById(R.id.asset_original_tag);
	    assetDescription = (TextView) rootView.findViewById(R.id.asset_description);

	    // Asset liked people layout
	    assetPeopleLikedLayout = (RelativeLayout) rootView.findViewById(R.id.asset_people_liked_layout);
	    lovedUsersGrid = (GridView) rootView.findViewById(R.id.asset_people_liked_grid_view);
	    seeAll = (TextView) rootView.findViewById(R.id.asset_see_all);
	    seeAll.setTypeface(mRobotoBold);

	    assetOwnerName.setTypeface(mRobotoBold);
	    assetCreatedTimestamp.setTypeface(mRobotoBold);
	    assetLoves.setTypeface(mRobotoRegular);
	    assetReposts.setTypeface(mRobotoRegular);
	    assetOriginallyTaggedTextView.setTypeface(mRobotoRegular);
	    assetOriginalTag.setTypeface(mRobotoBold);
	    assetDescription.setTypeface(mRobotoBold);
	    TextView assetPeopleLiked = (TextView) rootView.findViewById(R.id.asset_people_liked);
	    assetPeopleLiked.setTypeface(mRobotoBold);
	    TextView assetTagsAdded = (TextView) rootView.findViewById(R.id.asset_tags_added);
	    assetTagsAdded.setTypeface(mRobotoBold);

	    // Asset added tags layout
	    assetTagsLayout = (RelativeLayout) rootView.findViewById(R.id.asset_tags_layout);
	    addedTagsGrid = (GridView) rootView.findViewById(R.id.asset_added_tags_grid_view);

	    // Progress Bar
	    progresswheel = (ProgressWheel) rootView.findViewById(R.id.progressBarwheelone);

	    options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
		    .displayer(new FadeInBitmapDisplayer(250)).build();
	    StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
	    assetUtil.setAssetOwnerPic(asset, assetOwnerPic);
	    assetUtil.setOwnerName(asset, assetOwnerName);
	    assetUtil.setAssetCreateDate(asset, assetCreatedTimestamp);
	    assetUtil.setAssetImage(asset, assetImage, null);
	    assetUtil.setAssetLikeIcon(asset, assetLoveAction);
	    assetUtil.setAssetLoves(asset, assetLoves);
	    // assetUtil.setAssetRepostIcon(asset, assetRepostAction);
	    assetUtil.setAssetReposts(asset, assetReposts);

	    UserStreamClickListener listener = new UserStreamClickListener(this, asset);
	    listener.setAssetAnimationIconHolder(assetLoveAnimationIcon);
	    assetOwnerPic.setOnClickListener(listener);
	    assetOwnerName.setOnClickListener(listener);
	    assetLoveAction.setOnClickListener(listener);
	    assetCommentAction.setOnClickListener(listener);

	    asset.setAssetSnapBitmap(((BitmapDrawable) assetImage.getDrawable()).getBitmap());
	    UserStreamGestureListener gestureListener = new UserStreamGestureListener(this, asset);
	    gestureListener.setAssetAnimationIconHolder(assetLoveAnimationIcon);
	    GestureDetector gd = new GestureDetector(getActivity().getApplicationContext(), gestureListener);
	    UserStreamOnTouchListener touchListener = new UserStreamOnTouchListener(gd,this);
	    assetImage.setOnTouchListener(touchListener);

	    loadAssetDescriptionLayout();
	    //loadPeopleWhoLikedAsset(assetLikedUsersList);
	    //loadTagsAddedByUsers();
	}
    }

    /**
     * Loads content in Asset Description layout
     */
    private void loadAssetDescriptionLayout() {
	if (asset != null) {
	    String originalTag = "";
	    String assetAsJSON = asset.getAssetAsJSON();
	    if (!TextUtils.isEmpty(assetAsJSON)) {
		try {
		    JSONObject assetJSON = new JSONObject(assetAsJSON);
		    if (assetJSON != null && assetJSON.has(JSONConstants.TAGS_CREATED_BY_USER)) {
			JSONArray tagsArray = assetJSON.getJSONArray(JSONConstants.TAGS_CREATED_BY_USER);
			if (tagsArray != null) {
			    for (int i = 0; i < tagsArray.length(); i++) {
				String tag = tagsArray.getString(i);
				if (!TextUtils.isEmpty(tag)) {
				    originalTag = originalTag + "#" + tag + " ";
				}
			    }
			}
		    }
		} catch (JSONException e) {

		}
	    }

	    // Set original tag string to TextView
	    if (!TextUtils.isEmpty(originalTag)) {
		if (assetOriginalTag != null) {
		    assetOriginalTag.setText(originalTag);
		}
	    } else {
		if (assetOriginallyTaggedTextView != null) {
		    assetOriginallyTaggedTextView.setVisibility(View.GONE);
		}
		if (assetOriginalTag != null) {
		    assetOriginalTag.setVisibility(View.GONE);
		}
	    }
	    // Set asset description to TextView
	    if (!TextUtils.isEmpty(asset.getAssetDescription())) {
		if (assetDescription != null) {
		    assetDescription.setText(asset.getAssetDescription());
		}
	    } else {
		if (assetDescription != null) {
		    assetDescription.setVisibility(View.GONE);
		}
	    }
	    // Hide complete layout if both Original Tag and Description are
	    // empty
	    if (TextUtils.isEmpty(originalTag) && TextUtils.isEmpty(asset.getAssetDescription())) {
		if (assetTagDescriptionLayout != null) {
		    assetTagDescriptionLayout.setVisibility(View.GONE);
		}
	    }
	}
    }

    /**
     * Loads people who liked asset
     */
    private void loadPeopleWhoLikedAsset(ArrayList<Friend> likedUsers) {
	if (likedUsers != null && likedUsers.size() > 0) {
	    try {
		    Collection<Friend> users = new ConcurrentSkipListSet<Friend>(likedUsers);
		    if (users != null) {
			ArrayList<Friend> usersList = new ArrayList<Friend>(users);
			if (usersList != null && usersList.size() > 0) {
			    if (assetPeopleLikedLayout != null) {
				assetPeopleLikedLayout.setVisibility(View.VISIBLE);
			    }
			    // Set adapter to gridview
			    AssetLikedPeopleAdapter adapter = new AssetLikedPeopleAdapter(getActivity(), usersList);
			    if (lovedUsersGrid != null) {
				lovedUsersGrid.setAdapter(adapter);
				lovedUsersGrid.setOnItemClickListener(new UserNetworkListClickListener(getActivity(),
					this, UserNetwork.LOVED_USERS));
				adapter.notifyDataSetChanged();
			    }

			    if (users.size() > 5 && seeAll != null) {
				seeAll.setVisibility(View.VISIBLE);
				UserStreamClickListener listener = new UserStreamClickListener(this, asset);
				seeAll.setOnClickListener(listener);
			    } else {
				seeAll.setVisibility(View.GONE);
			    }
			} else {
			    // Hide People Liked Asset layout
			    if (assetPeopleLikedLayout != null) {
				assetPeopleLikedLayout.setVisibility(View.GONE);
			    }
			}
		    }
	    } catch (Exception e) {

	    }
	}
    }

    /**
     * Loads tags added by users
     */
    private void loadTagsAddedByUsers() {
	if (asset != null) {
	    String assetAsJSON = asset.getAssetAsJSON();
	    if (!TextUtils.isEmpty(assetAsJSON)) {
		try {
		    JSONObject assetJSON = new JSONObject(assetAsJSON);
		    if (assetJSON != null && assetJSON.has(JSONConstants.TAGS_NOT_CREATED_BY_USER)) {
			JSONArray tagsArray = assetJSON.getJSONArray(JSONConstants.TAGS_NOT_CREATED_BY_USER);
			if (tagsArray != null) {
			    if (tagsArray.length() > 0) {
				assetTagsLayout.setVisibility(View.VISIBLE);
				// Parse asset tags
				ArrayList<String> tags = new ArrayList<String>();
				for (int i = 0; i < tagsArray.length(); i++) {
				    String tag = tagsArray.getString(i);
				    if (!TextUtils.isEmpty(tag)) {
					tags.add(tag);
				    }
				}
				// Set adapter to addedTagsGridView
				AssetAddedTagsAdapter adapter = new AssetAddedTagsAdapter(getActivity(), tags);
				if (addedTagsGrid != null) {
				    addedTagsGrid.setAdapter(adapter);
				    adapter.notifyDataSetChanged();
				}

			    } else {
				// Hide asset added tags layout
				if (assetTagsLayout != null) {
				    assetTagsLayout.setVisibility(View.GONE);
				}
			    }

			}
		    }
		} catch (JSONException e) {

		}
	    }
	}
    }

    /**
     * Downloads asset details from server
     * 
     * @param assetId
     */
    private void downloadAssetFromServer(String assetId) {
	/*
	 * TODO: Uncomment when DOODLE type Asset is enabled if (!TextUtils.isEmpty(assetId)) { if (progressDialog !=
	 * null) { progressDialog.show(); } AssetDetailsDownloadHandler handler = new AssetDetailsDownloadHandler(this);
	 * AssetDetailsDownloadThread thread = new AssetDetailsDownloadThread(getActivity(), assetId, handler);
	 * thread.start(); }
	 */
    }

    /**
     * Downloads doodle asset from server if not exists
     */
    private void downloadDoodleIfRequired(String assetId) {
	/*
	 * Create asset_id folder and download the doodle if asset folder not exists
	 */
	/*
	 * TODO: Uncomment when Doodle View is enabled if (asset != null && !TextUtils.isEmpty(assetId)) { if
	 * (SaveUtil.isAssetFolderExists(getActivity(), assetId)) { // Fetch asset folder path String appRoot =
	 * AppPropertiesUtil.getAppDirectory(getActivity()); String doodleFolderPath =
	 * getActivity().getResources().getString(R.string.doodle_folder); if (!TextUtils.isEmpty(appRoot) &&
	 * !TextUtils.isEmpty(doodleFolderPath)) { assetFolderPath = appRoot + doodleFolderPath + assetId + "/"; } //
	 * Start playing the doodle startPlayingDoodle(); } else { // Create asset_id folder and download the asset
	 * content from server assetFolderPath = SaveUtil.createEmptyDoodleAssetFolder(getActivity(), assetId); if
	 * (!TextUtils.isEmpty(assetFolderPath) && !TextUtils.isEmpty(asset.getAssetDownloadURL())) { // Download the
	 * asset String doodleDownloadURL = asset.getAssetDownloadURL(); DoodleDownloadHandler handler = new
	 * DoodleDownloadHandler(this); DoodleDownloadThread thread = new DoodleDownloadThread(getActivity(),
	 * doodleDownloadURL, assetFolderPath, handler, new DoodleDownloadThread.DoodleDownloadListener() {
	 * 
	 * @Override public void startDownload() {
	 * 
	 * Message msg = new Message(); msg.what = VIEWVISIBLE; viewHandler.sendMessage(msg);
	 * 
	 * Message msgone = new Message(); msgone.what = PROGRESS; Random r = new Random(); msgone.arg1 = r.nextInt(15);
	 * viewHandler.sendMessageDelayed(msgone, 1000);
	 * 
	 * }
	 * 
	 * @Override public void progressDownload(long current, long total) {
	 * 
	 * Message msg = new Message(); msg.what = PROGRESS; msg.arg1 = (int) ((current * 360) / total);
	 * viewHandler.sendMessage(msg);
	 * 
	 * }
	 * 
	 * @Override public void completeDownload() { Message msg = new Message(); msg.what = FINALLOADING;
	 * viewHandler.sendMessage(msg); }
	 * 
	 * @Override public void cancelDownload() { Message msg = new Message(); msg.what = VIEWGONE;
	 * viewHandler.sendMessage(msg); } }); thread.start(); } } }
	 */
    }

    private Handler viewHandler = new Handler() {

	@Override
	public void handleMessage(Message msg) {

	    switch (msg.what) {
	    case VIEWGONE:
		if (progresswheel != null) {

		    progresswheel.setVisibility(View.GONE);
		}
		break;
	    case VIEWVISIBLE:
		if (progresswheel != null) {
		    progresswheel.setVisibility(View.VISIBLE);
		}
		break;
	    case PROGRESS:
		int progress = msg.arg1;

		if (progress != 360)
		    progresswheel.incrementProgress(progress);
		else
		    progresswheel.incrementProgress(345);

		break;

	    case FINALLOADING:
		progresswheel.incrementProgress(360);
		break;
	    default:
		break;
	    }
	}
    };

    /**
     * Gets called when asset relationship(LOVE, etc) is updated with server
     * 
     * @param assetId
     * @param relationship
     */
    public void assetRelationshipUpdated(String assetId, ASSOCIATION_TYPE relationship) {

	/*
	 * TODO: Uncomment for Google Analytics GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
	 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME, GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
	 * GoogleAnalyticsConstants.DSD_LOVE_SUCCESS_BUTTON);
	 */

	/*
	 * Change the LIKE icon and total likes count for the asset
	 */
	if (asset != null && isAdded()) {

	    if (asset.getAssetId().equals(assetId)) {
		int likesCount = 0;
		HashMap<ASSOCIATION_TYPE, Integer> associations = asset.getAssetAssociations();
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
		asset.setAssetAssociations(associations);
		asset.setAssetIsLoved(true);
		StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
		assetUtil.setAssetLikeIcon(asset, assetLoveAction);
		// assetUtil.startAnimation(asset, assetLoveAnimationIcon);
		assetUtil.setAssetLoves(asset, assetLoves);
	    }

	}
    }

    Runnable changeProgresswheel = new Runnable() {

	@Override
	public void run() {

	}
    };

    /**
     * Gets called when asset relationship(LOVE, etc) update is failed in server
     * 
     * @param assetId
     * @param relationship
     */
    public void assetRelationshipUpdateFailed(String assetId, ASSOCIATION_TYPE relationship) {
	/*
	 * TODO: Uncomment for Google Analytics GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
	 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME, GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
	 * GoogleAnalyticsConstants.DSD_LOVE_FAILURE_BUTTON);
	 */

	/*
	 * Handle the failure by changing respective relationship icon/count/details,etc
	 */
    }

    /**
     * Gets called when asset relationship(LOVE, etc) is removed with server
     * 
     * @param assetId
     * @param relationship
     */
    public void assetRelationshipRemoveSuccess(String assetId, ASSOCIATION_TYPE relationship) {
	/*
	 * Change the LIKE icon and total likes count for the asset
	 */
	if (asset != null && isAdded()) {

	    if (asset.getAssetId().equals(assetId)) {
		int likesCount = 0;
		HashMap<ASSOCIATION_TYPE, Integer> associations = asset.getAssetAssociations();
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
		associations.put(ASSOCIATION_TYPE.LOVE, (likesCount < 0) ? 0 : likesCount);
		asset.setAssetAssociations(associations);
		asset.setAssetIsLoved(false);
		StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
		assetUtil.setAssetLikeIcon(asset, assetLoveAction);
		assetUtil.setAssetLoves(asset, assetLoves);
	    }

	}
    }

    /**
     * Gets called when asset relationship(LOVE, etc) remove is failed in server
     * 
     * @param assetId
     * @param relationship
     */
    public void assetRelationshipRemoveFailed(String assetId, ASSOCIATION_TYPE relationship) {
	/*
	 * Handle the failure by changing respective relationship icon/count/details,etc
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
	 * TODO: Uncomment for Google Analytics GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
	 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME, GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
	 * GoogleAnalyticsConstants.DSD_REPOST_SUCCESS_BUTTON);
	 */

	/*
	 * Handle the success by increasing reposts count and display on asset
	 */
	if (asset != null && isAdded()) {

	    if (asset.getAssetId().equals(assetId)) {
		int repostsCount = 0;
		// Increment the reposts count by 1
		repostsCount = asset.getAssetRepostsCount() + 1;
		// Set new reposts count to asset associations
		asset.setAssetRepostsCount(repostsCount);
		asset.setAssetIsReposted(true);
		StreamAssetUtil assetUtil = new StreamAssetUtil(this, options);
		assetUtil.setAssetReposts(asset, assetReposts);
	    }

	}
    }

    /**
     * Gets called when asset repost is failed in server
     * 
     * @param assetId
     * @param relationship
     */
    public void assetRepostFailed(String assetId) {
	/*
	 * TODO: Uncomment for Google Analytics GoogleAnalytics.sendEventTrackingInfoToGA(getActivity(),
	 * GoogleAnalyticsConstants.DOODLE_STREAM_DETAILS_CAT_NAME, GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
	 * GoogleAnalyticsConstants.DSD_REPOST_FAILURE_BUTTON);
	 */

	/*
	 * Handle the failure
	 */
    }

    /**
     * Gets called to update doodle download status
     * 
     * @param isDoodleDownloaded
     */
    public void setDoodleDownloadStatus(boolean isDoodleDownloaded) {
	// If the download is success then start playing the doodle else do nothing
	if (isAdded()) {
	    if (isDoodleDownloaded) {
		progresswheel.setVisibility(View.GONE);
		// Start playing the doodle
		startPlayingDoodle();
	    } else {
		// do nothing
	    }
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
		this.asset = asset;
		// Update UI with fetched asset details
		initUI(rootView);
	    } else {
		// do nothing
	    }
	    if (progressDialog != null) {
		progressDialog.dismiss();
	    }
	}
    }

    /**
     * Starts playing doodle by replacing image view with doodle view
     */
    private void startPlayingDoodle() {
	/*
	 * if (assetImage != null) { assetImage.setVisibility(View.INVISIBLE); } if (assetDoodleView != null &&
	 * !TextUtils.isEmpty(assetFolderPath)) { assetDoodleView.setVisibility(View.VISIBLE); ParseDoodle doodleParser
	 * = new ParseDoodle(getActivity(), assetFolderPath); DoodleViewProperties doodleViewProperties =
	 * doodleParser.parseProperties(assetFolderPath); ArrayList<Object> doodle =
	 * doodleParser.parseDoodlePoints(assetFolderPath); if (doodleViewProperties != null && doodle != null) {
	 * assetDoodleView.setAssetDetailsFlag(true); assetDoodleView.play(doodle, doodleViewProperties); } }
	 */
    }

    /**
     * Initiates sharing asset via Social Networking or Third party apps
     */
    public void shareAsset() {
	shareImageViaAppsDialog(saveBitmap(asset.getAssetSnapBitmap()));
    }

    /**
     * Launches the Share Via Apps dialog from which asset image can be shared
     * 
     * @param shareURL
     */
    public void shareImageViaAppsDialog(String thumbnailPath) {
	if (!TextUtils.isEmpty(thumbnailPath)) {
	    WhatsayDialogsUtil.shareDoodleDialog(getActivity(), thumbnailPath, "", "",
		    ShareUtil.getLinkShareAppsList(getActivity()), false).show();
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
	    String tempImageFilePath = AppPropertiesUtil.getAppDirectory(context) + "/"
		    + context.getString(R.string.whatsay_folder);
	    File tempFile = new File(tempImageFilePath);
	    if (!tempFile.exists()) {
		tempFile.mkdir();
	    }

	    String tempImageName = context.getString(R.string.temp_image_name);
	    boolean isCreated = AssetSubmitHelper.saveBitmapToTempLocation(context, expressionBitmap);
	    if (isCreated) {
		fullImageFilePath = tempImageFilePath + tempImageName;
	    }
	}
	return fullImageFilePath;
    }

    /**
     * Fetches asset likes from server
     */
    private void fetchAssetLikesFromServer(String assetId){
	if(!TextUtils.isEmpty(assetId)){
	    GetAssetLikesHandler handler = new GetAssetLikesHandler(this);
	    GetAssetLikesTask thread = new GetAssetLikesTask(getActivity(), handler, assetId);
	    thread.start();
	}
    }
    
    /**
     * Fetches asset comments from server
     */
    private void fetchAssetCommentsFromServer(String assetId) {
	if (!TextUtils.isEmpty(assetId)) {
	    GetStreamHandler handler = new GetStreamHandler(this);
	    GetStreamThread thread = new GetStreamThread(getActivity(), assetId, handler, STREAM_TYPE.COMMENTS_STREAM,
		    0, 10);
	    thread.start();
	}
    }

    /**
     * Launches the canvas activity
     */
    public void launchCanvas() {
//	if (getActivity() != null) {
//	    Intent intent = new Intent(getActivity(), DoodlyDooCameraActivity.class);
//	    intent.putExtra(Constants.IS_COMMENT, true);
//	    intent.putExtra(Constants.COMMENT_ON_ASSET_ID, assetId);
//	    // starting CanvasActivity
//	    try {
//		getActivity().startActivity(intent);
//	    } catch (ActivityNotFoundException anfe) {
//	    } catch (Exception e) {
//	    }
//	} else {
//	}
    }

    /**
     * Sets asset liked users list and triggers UI update
     * @param likedUsers
     */
    public void setAssetLikedUsers(ArrayList<Friend> likedUsers){
	this.assetLikedUsersList = likedUsers;
	loadPeopleWhoLikedAsset(assetLikedUsersList);
    }
}
