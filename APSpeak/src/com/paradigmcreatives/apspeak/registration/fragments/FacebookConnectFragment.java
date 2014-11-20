package com.paradigmcreatives.apspeak.registration.fragments;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.analytics.GoogleAnalyticsHelper;
import com.paradigmcreatives.apspeak.app.model.GroupBean;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.constants.GoogleAnalyticsConstants;
import com.paradigmcreatives.apspeak.logging.Logger;
import com.paradigmcreatives.apspeak.registration.FragmentImplOpenGraphRequest;
import com.paradigmcreatives.apspeak.registration.LoginActivity;
import com.paradigmcreatives.apspeak.registration.adapters.GroupsAdapter;
import com.paradigmcreatives.apspeak.registration.handlers.AddUserToGroupHandler;
import com.paradigmcreatives.apspeak.registration.handlers.GetGroupsListHandler;
import com.paradigmcreatives.apspeak.registration.handlers.ProfileBuildupHandler;
import com.paradigmcreatives.apspeak.registration.listeners.FBConnectClickListeners;
import com.paradigmcreatives.apspeak.registration.listeners.GroupItemClickListenerImpl;
import com.paradigmcreatives.apspeak.registration.listeners.GroupsSearchActionListenerImpl;
import com.paradigmcreatives.apspeak.registration.listeners.SearchTextWatcherImpl;
import com.paradigmcreatives.apspeak.registration.listeners.SearchTextWatcherImpl.SEARCH_TYPE;
import com.paradigmcreatives.apspeak.registration.tasks.AddUserToGroupThread;
import com.paradigmcreatives.apspeak.registration.tasks.GetGroupsListThread;
import com.paradigmcreatives.apspeak.registration.tasks.ProfileBuildupThread;
import com.viewpagerindicator.LinePageIndicator;

/**
 * This fragment contains the UI for the Facebook Login. It also shows the
 * promotional screens of Whatsay sent from the server.
 * 
 * @author Dileep | neuv
 * 
 */
public class FacebookConnectFragment extends FragmentImplOpenGraphRequest {

	private static final String TAG = "HomeScreenFragment";

	// Variables for FB Integration
	private UiLifecycleHelper uiHelper;
	private boolean isFBFreshLogin = false;
	private Session session = null;
	// private long loadingtime = 0;
	/**
	 * This is the button from Facebook's SDK which handles the authentication
	 */
	private LoginButton fbLoginButton = null;

	/**
	 * This is an app level button which is shown when the app is logged in and
	 * we don't want to show the fbLoginButton in the Logout mode
	 */
	private Button fbConnect = null;

	private LinearLayout mGroupSelectionLayout;
	private RelativeLayout mGroupPickerLayout;
	private RelativeLayout mGroupLoginLayout;
	private TextView mGroupName;
	private Button mNextButton;
	private Dialog mDialog;
	private ArrayList<GroupBean> mGroupsList = null;
	private GroupBean mSelectedGroup = null;

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {

			/*
			 * TODO: Uncomment for Google Analytics if (loadingtime == 0) {
			 * loadingtime = System.currentTimeMillis();
			 * ((InitialUserSetupActivity)
			 * FacebookConnectFragment.this.getActivity
			 * ()).setFbRegisterStartTime(loadingtime);
			 * GoogleAnalytics.sendEventTrackingInfoToGA(
			 * (InitialUserSetupActivity)
			 * FacebookConnectFragment.this.getActivity(),
			 * GoogleAnalyticsConstants.HOME_SCREEN_CAT_NAME,
			 * GoogleAnalyticsConstants.EVENT_ACTION_BUTTON,
			 * GoogleAnalyticsConstants.MAIN_FBSIGNUP_BUTTON);
			 * 
			 * }
			 */

			onSessionStateChange(session, state, exception);

		}
	};

	public FacebookConnectFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.signup_v3, container, false);
		initView(view);
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Logger.info(TAG, "Already logged in");
			onSessionStateChange(session, session.getState(), null);
		}
		GoogleAnalyticsHelper.sendScreenViewToGA(getActivity(),
				GoogleAnalyticsConstants.FACEBOOK_LOGIN_SCREEN);
		fetchGroupsListFromServer();
		return view;
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
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		uiHelper.onSaveInstanceState(bundle);
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

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		this.session = session;
		Logger.info(TAG, session.getState() + "");
		if (state == SessionState.CREATED) {
			isFBFreshLogin = true;
		} else if (state.isOpened()) {
			if (isFBFreshLogin) {
				isFBFreshLogin = false;
				if (Util.isOnline(getActivity())) {
					fbConnect.performClick();
				} else {
					Toast.makeText(getActivity(),
							getActivity().getString(R.string.NoNetworkmsg),
							Toast.LENGTH_SHORT).show();
				}
			} else {
				fbLoginButton.setVisibility(View.GONE);
				fbConnect.setVisibility(View.VISIBLE);
			}
		} else if (state.isClosed()) {
			fbLoginButton.setVisibility(View.VISIBLE);
			fbConnect.setVisibility(View.GONE);
		}
		if (exception != null
				&& exception.getMessage().contains("Couldn't find the URL")
				&& !Util.isOnline(getActivity())) {
			Toast.makeText(getActivity(),
					getActivity().getString(R.string.NoNetworkmsg),
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Initializes various view components of this fragment
	 * 
	 * @param view
	 *            View to initialize
	 */
	private void initView(View view) {
		mGroupSelectionLayout = (LinearLayout) view
				.findViewById(R.id.groupselection_layout);
		mGroupLoginLayout = (RelativeLayout) view
				.findViewById(R.id.grouplogin_layout);
		mGroupPickerLayout = (RelativeLayout) view
				.findViewById(R.id.grouppicker_layout);
		mGroupName = (TextView) view.findViewById(R.id.group_name);
		mNextButton = (Button) view.findViewById(R.id.next_button);

		mGroupSelectionLayout.setVisibility(View.VISIBLE);
		mGroupLoginLayout.setVisibility(View.INVISIBLE);

		TextView termsAndConditions = (TextView) view
				.findViewById(R.id.terms_conditions_text);
		termsAndConditions.setFocusable(true);
		termsAndConditions.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// Perform action on click
				showEULADialog();
			}
		});
		// General click listener for the components in this Fragment
		FBConnectClickListeners clickListeners = new FBConnectClickListeners(
				this, getActivity());

		// The FB button which appears if the user is already logged in via FB.
		// In case the user is not logged in then
		// the login button from FB SDK appears
		fbConnect = (Button) view.findViewById(R.id.fbconnect);
		fbConnect.setOnClickListener(clickListeners);

		fbLoginButton = (LoginButton) view.findViewById(R.id.authButton);
		fbLoginButton.setReadPermissions(Arrays
				.asList(Constants.FB_PERMISSIONS_ARRAY));

		mGroupPickerLayout.setOnClickListener(clickListeners);
		mNextButton.setOnClickListener(clickListeners);

		/*
		 * PromotionalScreenAdapter adapter = new
		 * PromotionalScreenAdapter(this); ViewPager pager = (ViewPager)
		 * view.findViewById(R.id.view_pager); pager.setAdapter(adapter);
		 */
		final float density = getResources().getDisplayMetrics().density;
		LinePageIndicator pageIndicator = (LinePageIndicator) view
				.findViewById(R.id.indicator);
		// pageIndicator.setViewPager(pager);
		pageIndicator.setSelectedColor(0xFFFA6900);
		pageIndicator.setUnselectedColor(0xFF666666);
		pageIndicator.setStrokeWidth(4 * density);
		pageIndicator.setLineWidth(30 * density);
		// pager.setPageTransformer(true, new DepthPageTransformer());

		// TODO: Uncomment for Google Analytics
		// GoogleAnalytics.sendScreenInfoToGA(getActivity(),
		// GoogleAnalyticsConstants.HOME_SCREEN_NAME);

	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public void sendLodingTime() {
		/*
		 * TODO: Uncomment for Google Analytics loadingtime =
		 * System.currentTimeMillis() - loadingtime;
		 * GoogleAnalytics.sendLoadingTimeToGA(getActivity(), loadingtime,
		 * GoogleAnalyticsConstants.USER_TIMEING_NAME,
		 * GoogleAnalyticsConstants.SIGNUP_LOADING_TIME);
		 */
	}

	@Override
	public void showError(String msg, int errorCode) {
		if (errorCode == Constants.USER_ADDITON_TO_GROUP_FAILED_ERROR) {
			// Show a dialog to user for RETRY
			showRetryDialog();
		} else {
			Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Show a dialog informing the user about successful registration
	 */
	public void processSuccessfulRegistration(String userID) {
		// loadingtime = System.currentTimeMillis() - loadingtime;
		/*
		 * // sending Registration loading time to Google analytics
		 * GoogleAnalytics.sendLoadingTimeToGA(getActivity(), loadingtime,
		 * GoogleAnalyticsConstants.RESOURCE_NAME,
		 * GoogleAnalyticsConstants.REGISTRATION_TIMING_NAME);
		 */

		AlertDialog dialog = new AlertDialog.Builder(getActivity(),
				AlertDialog.THEME_HOLO_LIGHT).create();
		dialog.setTitle(R.string.congratulations);
		dialog.setMessage(getResources().getString(R.string.welcomemsg));
		dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (getActivity() instanceof LoginActivity) {
							dialog.dismiss();
							((LoginActivity) getActivity()).showFragment(
									Constants.FRIENDS_NETWORK_FRAGMENT_TAG,
									false, true);
						}
					}
				});
		dialog.show();

	}

	/**
	 * Show the dialog informing the user about the failure
	 */
	public void showErrorRegistrationDialog(int error, int errorCode) {
		if (errorCode == Constants.USER_ADDITON_TO_GROUP_FAILED_ERROR) {
			makeAddUserToGroupRequest();
		} else {
			AlertDialog dialog = new AlertDialog.Builder(getActivity(),
					AlertDialog.THEME_HOLO_LIGHT).create();
			dialog.setTitle("Registration Failed!");
			dialog.setMessage(getResources().getString(error));
			dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (getActivity() instanceof LoginActivity) {
								dialog.dismiss();
							}
						}
					});
			dialog.show();

		}
	}

	/**
	 * Makes server request to create profile by selecting handle
	 */
	public void makeProfileCreateRequest() {
		/*
		 * // Perform the registration ProfileBuildupHandler handler = new
		 * ProfileBuildupHandler(this); ProfileBuildupThread thread = new
		 * ProfileBuildupThread(this, handler); Thread t = new Thread(thread);
		 * t.start();
		 */
		// Launch Handle Selection fragment
		// ((LoginActivity) getActivity()).showFragment(
		// Constants.HANDLE_SELECTION_FRAGMENT_TAG, false, true);
		ProfileBuildupHandler handler = new ProfileBuildupHandler(this);
		ProfileBuildupThread thread = new ProfileBuildupThread(this, handler);
		Thread t = new Thread(thread);
		t.start();

	}

	public void showEULADialog() {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View eulaDialog = inflater.inflate(R.layout.eula_dialog, null);
		final Dialog dialog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		WebView licenseWebView = (WebView) eulaDialog
				.findViewById(R.id.eula_dialog_webview);
		licenseWebView.loadUrl("http://whatsayapp.com/eula.html");
		Button ok = (Button) eulaDialog.findViewById(R.id.eula_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();

			}

		});
		dialog.setContentView(eulaDialog);
		dialog.show();
	}

	private void fetchGroupsListFromServer() {
		GetGroupsListHandler handler = new GetGroupsListHandler(this);
		GetGroupsListThread thread = new GetGroupsListThread(handler);
		thread.start();
	}

	public void setGroupsList(ArrayList<GroupBean> groupsList) {
		this.mGroupsList = groupsList;
	}

	public ArrayList<GroupBean> getGroupsList() {
		return this.mGroupsList;
	}

	public void loadGroupsData() {
		if (isAdded() && mGroupsList != null && mGroupsList.size() > 0) {
			// Load groups list so that user can select a group and login
		}
	}

	/**
	 * Removes the groups list view
	 */
	public void removeGroupsListView() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
		}
	}

	/**
	 * Add the country list view
	 * 
	 * @param countryListView
	 */
	public void addGroupsListView(View groupsListView) {
		if (groupsListView != null) {
			// this.mGroupsListView = groupsListView;
			// mMainLayout.addView(mGroupsListView);
			// phoneNumberField.setEnabled(false);
			// countryAdapterSelected = true;
			mDialog = new Dialog(getActivity(),
					android.R.style.Theme_Translucent_NoTitleBar);
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(groupsListView);
			mDialog.show();
		}
	}

	/**
	 * Sets the group code using the group value provided
	 * 
	 * @param group
	 */
	public void setGroup(GroupBean group) {
		if (group != null) {
			mSelectedGroup = group;
			// Save the group id in shared preferences
			AppPropertiesUtil.setGroupID(getActivity(),
					mSelectedGroup.getGroupId());
			AppPropertiesUtil.setUserAddedToGroup(getActivity(), false);
			removeGroupsListView();
			if (mGroupName != null && group != null
					&& !TextUtils.isEmpty(group.getGroupName())) {
				if (mGroupName.equals(getResources().getString(
						R.string.request_college))) {
					Intent emailIntent = Util.getPreFormattedEmailIntent(this
							.getActivity());
					if (emailIntent != null) {
						try {
							startActivity(Intent.createChooser(emailIntent,
									("Choose")));
						} catch (ActivityNotFoundException anfe) {
							Logger.warn(
									TAG,
									"Activity not found : "
											+ anfe.getLocalizedMessage());
						} catch (Exception e) {
							Logger.warn(
									TAG,
									"Unknown Exception : "
											+ e.getLocalizedMessage());
						}
					} else {
						Logger.warn(TAG, "email intent is null");
					}

				} else {
					mGroupName.setText(group.getGroupName());
				}
			}
		} else {
			mSelectedGroup = null;
			Logger.warn(TAG, "Country value is invalid");
		}
	}

	public void showFBConnectLayout() {
		if (mSelectedGroup != null) {
			if (mGroupSelectionLayout != null) {
				mGroupSelectionLayout.setVisibility(View.INVISIBLE);
			}
			if (mGroupLoginLayout != null) {
				mGroupLoginLayout.setVisibility(View.VISIBLE);
			}
		} else {
			Toast.makeText(getActivity(), "Please select a college",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void showRetryDialog() {
		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View eulaDialog = inflater.inflate(R.layout.logout_dialog, null);
		final Dialog dialog = new Dialog(getActivity(),
				android.R.style.Theme_Translucent_NoTitleBar);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		TextView retryMessage = (TextView) eulaDialog
				.findViewById(R.id.empty_doodle_title);
		retryMessage.setText("Failed to add as a member. Retry?");

		Button yesButton = (Button) eulaDialog.findViewById(R.id.yes_button);
		Button noButton = (Button) eulaDialog.findViewById(R.id.no_button);
		noButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				getActivity().finish();

			}

		});

		yesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				dialog.dismiss();
				makeAddUserToGroupRequest();
			}

		});
		dialog.setContentView(eulaDialog);
		dialog.show();

	}

	public void makeAddUserToGroupRequest() {
		AddUserToGroupHandler handler = new AddUserToGroupHandler(this);
		AddUserToGroupThread thread = new AddUserToGroupThread(getActivity(),
				handler, AppPropertiesUtil.getGroupId(getActivity()),
				AppPropertiesUtil.getUserID(getActivity()));
		thread.start();
	}

	/**
	 * Inflates the colelge list view and provides it to facebook connect
	 * fragment
	 */
	public void extractAndProvideCollegeListView() {
		if (getActivity() != null) {
			View groupsListView = (View) LayoutInflater.from(getActivity())
					.inflate(R.layout.colleges_list, null);
			ListView groupsList = (ListView) groupsListView
					.findViewById(R.id.colleges);
			EditText searchField = (EditText) groupsListView
					.findViewById(R.id.search_field);
			// setting adapter for countries list
			groupsList.setCacheColorHint(Color.TRANSPARENT);
			ArrayList<GroupBean> groupsArrayList = getGroupsList();
			if (groupsArrayList != null) {
				GroupsAdapter countriesAdapter = new GroupsAdapter(
						getActivity(), groupsArrayList);
				groupsList.setAdapter(countriesAdapter);
				searchField.setInputType(InputType.TYPE_CLASS_TEXT);
				searchField.setImeOptions(EditorInfo.IME_ACTION_DONE);
				searchField
						.setOnEditorActionListener(new GroupsSearchActionListenerImpl(
								this));
				searchField.addTextChangedListener(new SearchTextWatcherImpl(
						this, groupsList, SEARCH_TYPE.GROUPS));
				groupsList
						.setOnItemClickListener(new GroupItemClickListenerImpl(
								this));
				addGroupsListView(groupsListView);
			}
		}
	}

}
