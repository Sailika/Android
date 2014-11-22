package com.paradigmcreatives.apspeak.registration.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
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

/**
 * This fragment contains the UI for the Facebook Login. It also shows the
 * promotional screens of Whatsay sent from the server.
 * 
 * @author Dileep | neuv
 * 
 */
public class FacebookConnectAnimationFragment extends
		FragmentImplOpenGraphRequest {

	private static final String TAG = "FacebookConnectAnimationFragment";

	// Variables for FB Integration
	private UiLifecycleHelper uiHelper;
	private boolean isFBFreshLogin = false;
	private Session session = null;
	/**
	 * This is the button from Facebook's SDK which handles the authentication
	 */
	private LoginButton fbLoginButton = null;

	/**
	 * This is an app level button which is shown when the app is logged in and
	 * we don't want to show the fbLoginButton in the Logout mode
	 */
	private Button fbConnect = null;

	private ProgressBar mProgressBar;
	private Button mGroupSelectionButton = null;
	private FrameLayout mGroupLoginLayout = null;
	private RelativeLayout mSignupLayout = null;
	private Dialog mDialog;
	private ListView mGroupsListView;
	private GroupsAdapter mGroupsAdapter;
	private ArrayList<GroupBean> mGroupsList = null;
	private GroupBean mSelectedGroup = null;
	private boolean isAnimationsAlreadyPerformed = false;
	private String mGroupSearchText = "";

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {

			onSessionStateChange(session, state, exception);

		}
	};

	public FacebookConnectAnimationFragment() {
		super();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.signup_v4a, container, false);
		initView(view);
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
			Logger.info(TAG, "Already logged in");
			onSessionStateChange(session, session.getState(), null);
		}
		GoogleAnalyticsHelper.sendScreenViewToGA(getActivity(),
				GoogleAnalyticsConstants.FACEBOOK_LOGIN_SCREEN);
		if (mProgressBar != null
				&& mProgressBar.getVisibility() != View.VISIBLE) {
			mProgressBar.setVisibility(View.VISIBLE);
		}
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
		mSignupLayout = (RelativeLayout) view
				.findViewById(R.id.signup_relativelayout);
		mGroupSelectionButton = (Button) view.findViewById(R.id.chooseCollege);
		// General click listener for the components in this Fragment
		FBConnectClickListeners clickListeners = new FBConnectClickListeners(
				this, getActivity());

		mGroupLoginLayout = (FrameLayout) view.findViewById(R.id.fbbutton);
		// The FB button which appears if the user is already logged in via FB.
		// In case the user is not logged in then
		// the login button from FB SDK appears
		fbConnect = (Button) view.findViewById(R.id.fbconnect);
		fbConnect.setGravity(Gravity.CENTER);
		fbConnect.setTextColor(getResources().getColor(R.color.white));
		fbConnect.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources()
				.getDimension(R.dimen.com_facebook_loginview_text_size));
		fbConnect.setTypeface(Typeface.DEFAULT_BOLD);
		fbConnect.setText(getResources()
				.getString(R.string.connectWithFacebook));
		fbConnect.setBackgroundResource(R.drawable.com_facebook_button_blue);
		fbConnect.setCompoundDrawablesWithIntrinsicBounds(
				R.drawable.com_facebook_inverse_icon, 0, 0, 0);
		fbConnect
				.setCompoundDrawablePadding(getResources()
						.getDimensionPixelSize(
								R.dimen.com_facebook_loginview_compound_drawable_padding));
		fbConnect.setPadding(
				getResources().getDimensionPixelSize(
						R.dimen.com_facebook_loginview_padding_left),
				getResources().getDimensionPixelSize(
						R.dimen.com_facebook_loginview_padding_top),
				getResources().getDimensionPixelSize(
						R.dimen.com_facebook_loginview_padding_right),
				getResources().getDimensionPixelSize(
						R.dimen.com_facebook_loginview_padding_bottom));
		fbConnect.setOnClickListener(clickListeners);

		fbLoginButton = (LoginButton) view.findViewById(R.id.authButton);
		fbLoginButton.setReadPermissions(Arrays
				.asList(Constants.FB_PERMISSIONS_ARRAY));

		mGroupSelectionButton.setOnClickListener(clickListeners);

		startGroupSelectionButtonAnimationBottomToCenter();
	}

	@Override
	public Session getSession() {
		return session;
	}

	@Override
	public void sendLodingTime() {
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

		AlertDialog dialog = new AlertDialog.Builder(getActivity(),
				AlertDialog.THEME_HOLO_LIGHT).create();
		dialog.setTitle(R.string.congratulations);
		dialog.setMessage(getResources().getString(R.string.welcomemsg));
		dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Continue",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Activity activity = getActivity();
						if (getActivity() instanceof LoginActivity) {
							dialog.dismiss();
							((LoginActivity) activity)
									.launchAppNewHomeActivity();
							activity.finish();
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
		// Launch Handle Selection fragment
		// ((LoginActivity) getActivity()).showFragment(
		// Constants.HANDLE_SELECTION_FRAGMENT_TAG, false, true);

		// TODO Start registration
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
		if (Util.isOnline(this.getActivity())) {
			GetGroupsListHandler handler = new GetGroupsListHandler(this);
			GetGroupsListThread thread = new GetGroupsListThread(handler);
			thread.start();
		} else {
			Toast.makeText(this.getActivity(),
					getResources().getString(R.string.no_network),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void setGroupsList(ArrayList<GroupBean> groupsList) {
		this.mGroupsList = groupsList;
		if (mProgressBar != null
				&& mProgressBar.getVisibility() == View.VISIBLE) {
			mProgressBar.setVisibility(View.GONE);
		}
		if (mGroupsListView != null) {
			mGroupsAdapter = new GroupsAdapter(getActivity(),
					performGroupsListSearch(mGroupSearchText));
			mGroupsListView.setAdapter(mGroupsAdapter);
			mGroupsAdapter.notifyDataSetChanged();
		}
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
			mDialog = new Dialog(getActivity(),
					android.R.style.Theme_Light_NoTitleBar_Fullscreen);
			mDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			mDialog.setContentView(groupsListView);
			mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					mGroupsListView = null;
					if (mSelectedGroup == null) {
						startGroupSelectionButtonAnimationTopToCenter(false);
					}
				}
			});
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(mDialog.getWindow().getAttributes());
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.MATCH_PARENT;

			mDialog.show();
			mDialog.getWindow().setAttributes(lp);
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

			removeGroupsListView();
			String mGroupName = group.getGroupName();
			if (mGroupSelectionButton != null && group != null
					&& !TextUtils.isEmpty(mGroupName)) {
				if (mGroupName.equals(getResources().getString(
						R.string.request_college))) {
					Intent emailIntent = Util.getPreFormattedEmailIntent(this
							.getActivity());
					if (emailIntent != null) {
						try {
							startActivity(Intent.createChooser(emailIntent,
									("Choose")));
							startGroupSelectionButtonAnimationTopToCenter(false);
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
					mGroupSelectionButton.setText(mGroupName);
					AppPropertiesUtil.setGroupID(getActivity(),
							mSelectedGroup.getGroupId());
					AppPropertiesUtil.setUserAddedToGroup(getActivity(), false);
					showFBConnectLayout(true);
				}
			}

		} else {
			mSelectedGroup = null;
			Logger.warn(TAG, "Country value is invalid");
		}
	}

	public void showFBConnectLayout(boolean showGroupLoginButton) {
		if (mSelectedGroup != null) {
			if (mSignupLayout != null
					&& mSignupLayout.getVisibility() != View.VISIBLE) {
				mSignupLayout.setVisibility(View.VISIBLE);
			}
			if (!isAnimationsAlreadyPerformed) {
				if (mGroupLoginLayout != null
						&& mGroupLoginLayout.getVisibility() != View.INVISIBLE) {
					mGroupLoginLayout.setVisibility(View.INVISIBLE);
				}
				startGroupSelectionButtonAnimationTopToCenter(showGroupLoginButton);
			} else {
				if (mGroupLoginLayout != null
						&& mGroupLoginLayout.getVisibility() != View.VISIBLE) {
					mGroupLoginLayout.setVisibility(View.VISIBLE);
				}
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

	private void startGroupSelectionButtonAnimationBottomToCenter() {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 20.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		animation.setFillEnabled(true);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (mSignupLayout != null) {
					mSignupLayout.clearAnimation();
				}
			}
		});
		if (mSignupLayout != null) {
			mSignupLayout.startAnimation(animation);
		}
	}

	public void startGroupSelectionButtonAnimationCenterToTop() {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, -20.0f);
		animation.setDuration(1000);
		animation.setFillAfter(true);
		animation.setFillEnabled(true);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (mSignupLayout != null) {
					mSignupLayout.clearAnimation();
					mSignupLayout.setVisibility(View.INVISIBLE);
					extractAndProvideCollegeListView();
				}

			}
		});
		if (mSelectedGroup == null) {
			if (mSignupLayout != null) {
				mSignupLayout.startAnimation(animation);
			}
		} else {
			extractAndProvideCollegeListView();
		}
	}

	private void startGroupSelectionButtonAnimationTopToCenter(
			final boolean showGroupLoginButton) {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -10.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(800);
		if (showGroupLoginButton) {
			animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
					0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
					Animation.RELATIVE_TO_SELF, -8.0f,
					Animation.RELATIVE_TO_SELF, 0.0f);
			animation.setDuration(1000);
		}
		animation.setFillAfter(true);
		animation.setFillEnabled(true);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (mSignupLayout != null) {
					mSignupLayout.clearAnimation();
				}
				if (showGroupLoginButton) {
					if (mGroupLoginLayout != null) {
						mGroupLoginLayout.setVisibility(View.VISIBLE);
					}
					startSignupButtonAnimationTopToCenter();
				}
			}
		});
		if (mSignupLayout != null) {
			mSignupLayout.setVisibility(View.VISIBLE);
			mSignupLayout.startAnimation(animation);
		}
	}

	private void startSignupButtonAnimationTopToCenter() {
		Animation animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, -2.0f,
				Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(600);
		animation.setFillAfter(true);
		animation.setFillEnabled(true);
		animation.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (mGroupLoginLayout != null) {
					mGroupLoginLayout.clearAnimation();
				}
				isAnimationsAlreadyPerformed = true;
			}
		});
		if (mGroupLoginLayout != null) {
			mGroupLoginLayout.startAnimation(animation);
		}
	}

	/**
	 * Inflates the college list view and provides it to facebook connect
	 * fragment
	 */
	public void extractAndProvideCollegeListView() {
		if (getActivity() != null) {
			View groupsListView = (View) LayoutInflater.from(getActivity())
					.inflate(R.layout.colleges_list, null);
			mProgressBar = (ProgressBar) groupsListView
					.findViewById(R.id.progressBar);
			mGroupsListView = (ListView) groupsListView
					.findViewById(R.id.colleges);
			EditText searchField = (EditText) groupsListView
					.findViewById(R.id.search_field);
			mGroupsListView.setCacheColorHint(Color.TRANSPARENT);
			ArrayList<GroupBean> groupsArrayList = getGroupsList();
			if (groupsArrayList == null) {
				groupsArrayList = new ArrayList<GroupBean>();
			}
			if (groupsArrayList.size() == 0) {
				mProgressBar.setVisibility(View.VISIBLE);
			}
			mGroupsAdapter = new GroupsAdapter(getActivity(), groupsArrayList);
			mGroupsListView.setAdapter(mGroupsAdapter);
			searchField.setInputType(InputType.TYPE_CLASS_TEXT);
			searchField.setImeOptions(EditorInfo.IME_ACTION_DONE);
			searchField
					.setOnEditorActionListener(new GroupsSearchActionListenerImpl(
							this));
			searchField.addTextChangedListener(new SearchTextWatcherImpl(this,
					mGroupsListView, SEARCH_TYPE.GROUPS));
			mGroupsListView
					.setOnItemClickListener(new GroupItemClickListenerImpl(this));
			addGroupsListView(groupsListView);
		}
	}

	public void setGroupSearchText(CharSequence text) {
		if (text != null) {
			mGroupSearchText = text.toString();
		} else {
			mGroupSearchText = "";
		}
	}

	/**
	 * This method performs the search on the groups list bases on group name
	 * 
	 * @param context
	 *            : Context
	 * @param searchTerm
	 *            :The term to search
	 * @return :The list of groups which were matched with the given searchTerm
	 */
	public ArrayList<GroupBean> performGroupsListSearch(String searchTerm) {
		ArrayList<GroupBean> searchGroupsList = new ArrayList<GroupBean>();
		ArrayList<GroupBean> groupsList = new ArrayList<GroupBean>();
		groupsList = getGroupsList();

		String groupName = "";
		try {
			Pattern pattern = Pattern.compile(searchTerm,
					Pattern.CASE_INSENSITIVE);
			if (groupsList != null) {
				for (int i = 0; i < groupsList.size(); i++) {
					groupName = groupsList.get(i).getGroupName();
					boolean matchName = !TextUtils.isEmpty(groupName)
							&& pattern.matcher(groupName).find();
					if (matchName) {
						searchGroupsList.add(groupsList.get(i));
					} // else nothing to do
				}
			}
		} catch (Exception e) {
		}
		return searchGroupsList;
	}

}
