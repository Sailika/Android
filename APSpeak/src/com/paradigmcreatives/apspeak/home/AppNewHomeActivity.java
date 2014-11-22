package com.paradigmcreatives.apspeak.home;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AppEventsLogger;
import com.paradigmcreatives.apspeak.R;
import com.paradigmcreatives.apspeak.app.invite.fragments.InviteFriendsFragment;
import com.paradigmcreatives.apspeak.app.util.AppPropertiesUtil;
import com.paradigmcreatives.apspeak.app.util.Util;
import com.paradigmcreatives.apspeak.app.util.constants.Constants;
import com.paradigmcreatives.apspeak.app.util.whatsapp.WhatsAppManager;
import com.paradigmcreatives.apspeak.cues.fragments.CuesFragment;
import com.paradigmcreatives.apspeak.featured.fragments.FeaturedFragment;
import com.paradigmcreatives.apspeak.globalstream.AppNewChildActivity;
import com.paradigmcreatives.apspeak.navdrawer.adapter.NavDrawerAdapter;
import com.paradigmcreatives.apspeak.navdrawer.adapter.NavDrawerItem;
import com.paradigmcreatives.apspeak.notification.NotificationsCountBroadcastReceiver;
import com.paradigmcreatives.apspeak.registration.handlers.AddUserToGroupHandler;
import com.paradigmcreatives.apspeak.registration.tasks.AddUserToGroupThread;
import com.paradigmcreatives.apspeak.registration.tasks.GetUserGroupsListThread;
import com.paradigmcreatives.apspeak.user.fragments.ProfileFragment;

/**
 * Activity that acts as application's home screen with Cues, Featured, Profile
 * and Notifications options in bottom bar
 * 
 * @author Dilep | neuv
 * 
 */
public class AppNewHomeActivity extends FragmentActivity {
	

	//Slide Menu Items
	 private String[] navMenuTitles;
	    private TypedArray navMenuIcons;
	    private ArrayList<NavDrawerItem> navDrawerItems;
	    private NavDrawerAdapter adapter;
	    
   private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout menuLayout;
    private LinearLayout frame;
    private TranslateAnimation anim;
    private float moveFactor, lastTranslate = 0.0f;
    private ListView navList;

	private LinearLayout mCuesLayout;
	private LinearLayout mFeaturedLayout;
	private LinearLayout mProfileLayout;
	private LinearLayout mNotificationsLayout;

	private LinearLayout mCuesIconLayout;
	private LinearLayout mFeaturedIconLayout;
	private LinearLayout mProfileIconLayout;
	private LinearLayout mNotificationsIconLayout;

	private ImageView mInviteIcon;
	private ImageView mFeedbackIcon;
	private ImageView mSettingsIcon;
	private ImageView mCuesIcon;
	private ImageView mFeaturedIcon;
	private ImageView mProfileIcon;
	private ImageView mNotificationsIcon;
	private ImageView mHeaderOptionsBack;
	private ImageView mHeaderLogo;
	private TextView mNotificationsCount;
	private TextView mProfileName;

	private FragmentManager mFragmentManager;
	private CuesFragment mCuesFragment;
	private FeaturedFragment mFeaturedFragment;
	private ProfileFragment mProfileFragment;
	//private MyFeedFragment mMyFeedFragment;
	private InviteFriendsFragment mMyFeedFragment;
	private Fragment currentFragment;
	private boolean mShowAnnouncement = false;
	private String mAnnouncementId;
	private String mAnnouncementMessage;
	private String TAG = "AppNewHomeActivity";

	private NotificationsCountBroadcastReceiver mNotificationsCountBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.appnewhome_layout);

		if (AppPropertiesUtil.getUserAddedToGroupFlag(this)) {
			completeInitialization();
		} else {
			showRetryDialog();
		}
	}

	private void initUI() {
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuLayout = (RelativeLayout) findViewById(R.id.navdrawer_listLayout);
        navList = (ListView) findViewById(R.id.navDrawer_list);
        frame = (LinearLayout) findViewById(R.id.frame);
		mCuesLayout = (LinearLayout) findViewById(R.id.home_cues);
		drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
		mFeaturedLayout = (LinearLayout) findViewById(R.id.home_featured);
		mProfileLayout = (LinearLayout) findViewById(R.id.home_profile);
		mNotificationsLayout = (LinearLayout) findViewById(R.id.home_notifications);

		mCuesIconLayout = (LinearLayout) findViewById(R.id.home_cues_icon_layout);
		mFeaturedIconLayout = (LinearLayout) findViewById(R.id.home_featured_icon_layout);
		if (AppPropertiesUtil.getFeaturedFlag(this)) {
			mFeaturedIconLayout.setVisibility(View.VISIBLE);
		} else {
			mFeaturedIconLayout.setVisibility(View.GONE);
		}
		mProfileIconLayout = (LinearLayout) findViewById(R.id.home_profile_icon_layout);
		mNotificationsIconLayout = (LinearLayout) findViewById(R.id.home_notifications_icon_layout);

		mInviteIcon = (ImageView) findViewById(R.id.header_option_invite);
		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);
		mHeaderOptionsBack = (ImageView) findViewById(R.id.header_option_back);
		mFeedbackIcon = (ImageView) findViewById(R.id.header_option_feedback);
		mSettingsIcon = (ImageView) findViewById(R.id.header_option_settings);
		mCuesIcon = (ImageView) findViewById(R.id.home_cues_icon);
		mFeaturedIcon = (ImageView) findViewById(R.id.home_featured_icon);
		mProfileIcon = (ImageView) findViewById(R.id.home_profile_icon);
		mNotificationsIcon = (ImageView) findViewById(R.id.home_notifications_icon);
		mNotificationsCount = (TextView) findViewById(R.id.home_notifications_count);
		mProfileName = (TextView) findViewById(R.id.header_txt_profile);
		showHideNotificationsCount();
		final FrameLayout welcomeFrame = (FrameLayout) findViewById(R.id.welcome_screen);
		ImageView welcomeClose = (ImageView) findViewById(R.id.welcome_close_view);
		if (AppPropertiesUtil.isWelcomeScreenShownBefore(this)) {
			welcomeFrame.setVisibility(View.GONE);
		} else {
			welcomeFrame.setVisibility(View.GONE);
			AppPropertiesUtil.setWelcomeScreenShownBeforeToTrue(this);
		}
		welcomeClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				welcomeFrame.setVisibility(View.GONE);

			}
		});

		welcomeFrame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);

			}
		});

		AppNewHomeOnClickListener listener = new AppNewHomeOnClickListener(this);
		mInviteIcon.setOnClickListener(listener);
		mSettingsIcon.setOnClickListener(listener);
		mCuesIconLayout.setOnClickListener(listener);
		mFeaturedIconLayout.setOnClickListener(listener);
		mProfileIconLayout.setOnClickListener(listener);
		mFeedbackIcon.setOnClickListener(listener);
		mNotificationsIconLayout.setOnClickListener(listener);
		mHeaderOptionsBack.setOnClickListener(listener);
	}

	/**
	 * Handles each item click on bottom bar
	 * 
	 * @param view
	 */
	public void handleItemSelected(int viewId) {
		if (mFragmentManager == null) {
			mFragmentManager = getSupportFragmentManager();
		}
		switch (viewId) {
		
		case R.id.header_option_back:
			
			onBackPressed();
			break;
		case R.id.header_option_invite:
			launchInviteFBFriendsFragment();
			break;

		case R.id.header_option_settings:
			launchSettingsFragment();
			break;

		case R.id.home_cues_icon_layout:
			if (mCuesFragment == null) {
				mCuesFragment = new CuesFragment();
				FragmentTransaction fragmentTransaction = mFragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.home_cues, mCuesFragment);
				fragmentTransaction.commit();
				currentFragment = mCuesFragment;
			}
			mCuesIcon.setImageResource(R.drawable.dashboard_icon_selected);
			mFeaturedIcon.setImageResource(R.drawable.featured);
			mProfileIcon.setImageResource(R.drawable.my_profile_icon);
			mNotificationsIcon.setImageResource(R.drawable.invite_friends_icon);
			showHideNotificationsCount();

			mInviteIcon.setVisibility(View.VISIBLE);
			mSettingsIcon.setVisibility(View.INVISIBLE);
			mFeedbackIcon.setVisibility(View.VISIBLE);
			mCuesLayout.setVisibility(View.VISIBLE);
			mFeaturedLayout.setVisibility(View.INVISIBLE);
			mProfileLayout.setVisibility(View.INVISIBLE);
			mNotificationsLayout.setVisibility(View.INVISIBLE);
			mHeaderOptionsBack.setVisibility(View.INVISIBLE);
			mProfileName.setText(getResources().getString(R.string.campaigns));
			mProfileName.setVisibility(View.VISIBLE);
			mHeaderLogo.setVisibility(View.GONE);
			break;

		case R.id.home_featured_icon_layout:
			if (mFeaturedFragment == null) {
				mFeaturedFragment = new FeaturedFragment();
				FragmentTransaction fragmentTransaction = mFragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.home_featured,
						mFeaturedFragment);
				fragmentTransaction.commit();
				currentFragment = mFeaturedFragment;
			}
			mCuesIcon.setImageResource(R.drawable.dashboard_icon);
			mFeaturedIcon.setImageResource(R.drawable.featured_select);
			mProfileIcon.setImageResource(R.drawable.me);
			mNotificationsIcon.setImageResource(R.drawable.myfeed);
			showHideNotificationsCount();

			mInviteIcon.setVisibility(View.INVISIBLE);
			mSettingsIcon.setVisibility(View.INVISIBLE);
			mFeedbackIcon.setVisibility(View.INVISIBLE);
			mCuesLayout.setVisibility(View.INVISIBLE);
			mFeaturedLayout.setVisibility(View.VISIBLE);
			mProfileLayout.setVisibility(View.INVISIBLE);
			mNotificationsLayout.setVisibility(View.INVISIBLE);
			mHeaderOptionsBack.setVisibility(View.INVISIBLE);
			mProfileName.setVisibility(View.INVISIBLE);
			mHeaderLogo.setVisibility(View.GONE);
			break;

		case R.id.home_profile_icon_layout:
			if (mProfileFragment == null) {
				mProfileFragment = new ProfileFragment();
				FragmentTransaction fragmentTransaction = mFragmentManager
						.beginTransaction();
				fragmentTransaction
						.replace(R.id.home_profile, mProfileFragment);
				fragmentTransaction.commit();

				currentFragment = mProfileFragment;
			}
			mCuesIcon.setImageResource(R.drawable.dashboard_icon);
			mFeaturedIcon.setImageResource(R.drawable.featured);
			mProfileIcon.setImageResource(R.drawable.my_profile_icon_selected);
			mNotificationsIcon.setImageResource(R.drawable.invite_friends_icon);
			showHideNotificationsCount();

			mInviteIcon.setVisibility(View.INVISIBLE);
			mSettingsIcon.setVisibility(View.VISIBLE);
			mFeedbackIcon.setVisibility(View.INVISIBLE);
			mCuesLayout.setVisibility(View.INVISIBLE);
			mFeaturedLayout.setVisibility(View.INVISIBLE);
			mProfileLayout.setVisibility(View.VISIBLE);
			mNotificationsLayout.setVisibility(View.INVISIBLE);
			mHeaderLogo.setVisibility(View.GONE);
			mHeaderOptionsBack.setVisibility(View.VISIBLE);
			mProfileName.setText(getResources().getString(R.string.my_profile));
			mProfileName.setVisibility(View.VISIBLE);

			break;

		case R.id.home_notifications_icon_layout:
			if (mMyFeedFragment == null) {
				mMyFeedFragment = new InviteFriendsFragment(
						AppPropertiesUtil.getUserID(this), "0", "200",
						mAnnouncementId, mAnnouncementMessage);
				
				//commented the code for ApSpeak
				
/*				mMyFeedFragment = new MyFeedFragment(
						AppPropertiesUtil.getUserID(this), "0", "200",
						mAnnouncementId, mAnnouncementMessage);*/
				mShowAnnouncement = false;
				FragmentTransaction ft = mFragmentManager.beginTransaction();
				ft.replace(R.id.home_notifications, mMyFeedFragment);
				ft.commit();
				currentFragment = mMyFeedFragment;
			} else {
				//commented the code for ApSpeak
				
				//mMyFeedFragment.reloadFeed();
			}
			mCuesIcon.setImageResource(R.drawable.dashboard_icon);
			mFeaturedIcon.setImageResource(R.drawable.featured);
			mProfileIcon.setImageResource(R.drawable.my_profile_icon);
			mNotificationsIcon.setImageResource(R.drawable.invite_friends_icon_selected);
			// Reset notifications count value
			AppPropertiesUtil.setNotificationsCount(AppNewHomeActivity.this, 0);
			showHideNotificationsCount();
			// Clear notifications from Notification bar
			NotificationManager manager = (NotificationManager) AppNewHomeActivity.this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			if(manager != null){
				manager.cancelAll();
			}

			mInviteIcon.setVisibility(View.INVISIBLE);
			mSettingsIcon.setVisibility(View.INVISIBLE);
			mCuesLayout.setVisibility(View.INVISIBLE);
			mFeaturedLayout.setVisibility(View.INVISIBLE);
			mProfileLayout.setVisibility(View.INVISIBLE);
			mNotificationsLayout.setVisibility(View.VISIBLE);
			mHeaderOptionsBack.setVisibility(View.VISIBLE);
			mProfileName.setText(getResources().getString(R.string.invite_friends_heading));
			mProfileName.setVisibility(View.VISIBLE);
			mHeaderLogo.setVisibility(View.GONE);
			mFeedbackIcon.setVisibility(View.INVISIBLE);
			break;
		case R.id.header_option_feedback:
			feedbackAction();

			if (drawerLayout.isDrawerVisible(Gravity.LEFT)) {
                return;
            } else {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
			break;
		case R.id.invite_contacts:
			inviteViaMessaging();
			break;
		case R.id.invite_email:
			inviteViaEmail();
			break;
		case R.id.invite_facebook:
			inviteViaFacbebook();
			break;
		case R.id.invite_twitter:
			inviteViaTwitter();
			break;
		case R.id.invite_whatsap:
			inviteViaWhatsapp();
			break;
		
		}
	}

	private void inviteViaFacbebook() {
		// TODO Auto-generated method stub
		Util.shareTextOnFacebookViaIntent(this, getString(R.string.invitation_message),
			    getString(R.string.invite_friends_heading));
	}

	private void inviteViaWhatsapp() {
		// TODO Auto-generated method stub
		
		  WhatsAppManager.launchWhatsAppWithText(this, getResources().getString(R.string.invitation_message));
		
	}

	private void inviteViaTwitter() {
		// TODO Auto-generated method stub
		Util.launchAppWithTwitterText(this, getResources().getString(R.string.invitation_message));
	}
	private void inviteViaEmail() {
		// TODO Auto-generated method stub
		 Intent emailIntent = Util.getPreFormattedEmailIntent(getString(R.string.invitation_message),
				    getString(R.string.app_name), "");
			   startActivity(emailIntent);
	}

	private void inviteViaMessaging() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBackPressed() {
		// Launch Cues Fragment when back button is pressed from any other
		// fragment from bottom bar
		if (mCuesLayout != null
				&& mCuesLayout.getVisibility() == View.INVISIBLE) {
			handleItemSelected(R.id.home_cues_icon_layout);
		} else {
			super.onBackPressed();
		}
	}

	public void userAddedSuccessfully() {
		completeInitialization();
	}

	public void showError(String msg, int errorCode) {
		if (errorCode == Constants.USER_ADDITON_TO_GROUP_FAILED_ERROR) {
			showRetryDialog();
		}
	}

	private void showRetryDialog() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View eulaDialog = inflater.inflate(R.layout.logout_dialog, null);
		final Dialog dialog = new Dialog(this,
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

				finish();

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
		AddUserToGroupThread thread = new AddUserToGroupThread(this, handler,
				AppPropertiesUtil.getGroupId(this),
				AppPropertiesUtil.getUserID(this));
		thread.start();
	}

	/**
	 * Gets current user's groups list from server
	 */
	public void getUserGroupsListFromServer() {
		GetUserGroupsListThread thread = new GetUserGroupsListThread(this,
				AppPropertiesUtil.getUserID(this));
		thread.start();
	}

	private void completeInitialization() {
		initUI();

		getUserGroupsListFromServer();

		createFragmentBasedOnIntentValues();

		/*
		 * if (mCuesFragment == null) { mCuesFragment = new CuesFragment(); }
		 * mFragmentManager = getSupportFragmentManager(); FragmentTransaction
		 * fragmentTransaction = mFragmentManager.beginTransaction();
		 * fragmentTransaction.replace(R.id.home_cues, mCuesFragment);
		 * fragmentTransaction.commit();
		 */
	}

	/**
	 * Launches Facebook Friends fragment
	 */
	private void launchInviteFBFriendsFragment() {
		Intent inviteIntent = new Intent(AppNewHomeActivity.this,
				AppNewChildActivity.class);
		inviteIntent.putExtra(Constants.LAUNCH_INVITE_FBFRIENDS_SCREEN, true);
		startActivity(inviteIntent);
		}

	/**
	 * Launches Settings Fragment
	 */
	private void launchSettingsFragment() {
		Intent inviteIntent = new Intent(AppNewHomeActivity.this,
				AppNewChildActivity.class);
		inviteIntent.putExtra(Constants.LAUNCH_SETTINGS_SCREEN, true);
		startActivity(inviteIntent);
	}

	/**
	 * Shows/Hides Featured Screen from app home screen depends on passed
	 * boolean value
	 * 
	 * @param flag
	 */
	public void showHideFeaturedScreen(boolean flag) {
		if (flag) {
			if (mFeaturedIconLayout != null) {
				mFeaturedIconLayout.setVisibility(View.VISIBLE);
			}
			handleItemSelected(R.id.home_featured_icon_layout);
		} else {
			if (mFeaturedIconLayout != null) {
				mFeaturedIconLayout.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * Creates Fragment based on Intent Values
	 */
	private void createFragmentBasedOnIntentValues() {
		Bundle data = getIntent().getExtras();
		if (data != null) {
			if (data.containsKey(Constants.LAUNCH_NOTIFICATIONS_FEED_SCREEN)
					&& data.getBoolean(Constants.LAUNCH_NOTIFICATIONS_FEED_SCREEN)) {
				mShowAnnouncement = true;
				mAnnouncementId = data.getString(Constants.ANNOUNCEMENTID);
				mAnnouncementMessage = data
						.getString(Constants.ANNOUNCEMNETMESSAGE);
				handleItemSelected(R.id.home_notifications_icon_layout);
			} else {
				handleItemSelected(R.id.home_cues_icon_layout);
			}
		} else {
			handleItemSelected(R.id.home_cues_icon_layout);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		mNotificationsCountBroadcastReceiver = new NotificationsCountBroadcastReceiver(
				this);
		registerReceiver(mNotificationsCountBroadcastReceiver,
				new IntentFilter(Constants.NOTIFICATIONSCOUNT_BROADCAST_ACTION));
	}

	@Override
	protected void onResume() {
		showHideNotificationsCount();
		super.onResume();
		AppEventsLogger.activateApp(getApplicationContext(),
				Constants.FACEBOOK_APPID);
	}

	@Override
	protected void onStop() {
		try {
			if (mNotificationsCountBroadcastReceiver != null) {
				unregisterReceiver(mNotificationsCountBroadcastReceiver);
			}
		} catch (Exception e) {

		}
		super.onStop();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Constants.ASSET_DETAILS_RESULT_CODE:
				if (currentFragment != null
						&& currentFragment instanceof ProfileFragment) {
					((ProfileFragment) currentFragment).refreshStream();
				}
				break;

			default:
				break;
			}
		}

	}

	/**
	 * Shows or Hides Notifications Count on Notifications Icon in bottom layout
	 */
	public void showHideNotificationsCount() {
		if (mNotificationsCount != null) {
			int notificationsCount = AppPropertiesUtil
					.getNotificationsCount(this);
			if (notificationsCount > 0) {
				mNotificationsCount.setText(notificationsCount + "");
				mNotificationsCount.setVisibility(View.VISIBLE);
			} else {
				mNotificationsCount.setVisibility(View.GONE);
			}
		}
	}

	private void feedbackAction() {

//		try {
//			Intent feedbackIntent = new Intent(this, FeedbackActivity.class);
//			startActivityForResult(feedbackIntent,
//					Constants.SETTINGS_OPTIONS_REQUESTCODE);
//		} catch (ActivityNotFoundException anfe) {
//			Logger.warn(TAG,
//					"Activity not found : " + anfe.getLocalizedMessage());
//		} catch (Exception e) {
//			Logger.warn(TAG, "Unknown Exception : " + e.getLocalizedMessage());
//		}
//	      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//	                android.R.layout.simple_list_item_1, R.array.navDrawerItems);
//
//	        navList.setAdapter(adapter);
		
		  // load slide menu items

        navMenuTitles = getResources().getStringArray(R.array.navDrawerItems);

        // nav drawer icons from resources

        navMenuIcons = getResources()

                .obtainTypedArray(R.array.navDrawerIcons);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items to array

for(int i=0;i<navMenuTitles.length;i++){
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[i], navMenuIcons.getResourceId(i, -1)));
}

        // Recycle the typed array

        navMenuIcons.recycle();

        // setting the nav drawer list adapter

        adapter = new NavDrawerAdapter(getApplicationContext(),

                navDrawerItems);

        navList.setAdapter(adapter);
        navList.setOnItemClickListener(new DrawerItemClickListener());



	        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
	                R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

	            public void onDrawerClosed(View view) {

	            }

	            public void onDrawerOpened(View drawerview) {

	            }

	            @SuppressLint("NewApi")
	            public void onDrawerSlide(View drawerView, float slideOffset) {

	               
	                moveFactor = (menuLayout.getWidth() * slideOffset);
	                drawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
	                      Gravity.LEFT);
	                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	                  frame.setTranslationX(moveFactor);
	                } else {
	                  anim = new TranslateAnimation(lastTranslate, moveFactor,
	                          0.0f, 0.0f);
	                  anim.setDuration(0);
	                  anim.setFillAfter(true);
	                  frame.startAnimation(anim);
	                  lastTranslate = moveFactor;
	                }
	            }
	        };

	        drawerLayout.setDrawerListener(drawerToggle);

	      

//	        navList.setOnItemClickListener(new OnItemClickListener() {
//
//	            @Override
//	            public void onItemClick(AdapterView<?> parent, View view,
//	                    int position, long id) {
//	                if (position == 0) {
//	                    drawerLayout.closeDrawers();
//	                }
//	            }
//	        });
		
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView parent, View view, int position, long id) {
	  	  Toast.makeText(AppNewHomeActivity.this, getResources().getString(R.string.app_name) , Toast.LENGTH_SHORT).show();
//		    drawerLayout.closeDrawer(navList);

	      //  selectItem(position);
	    }
	}

	/** Swap the  fragments in the main content view */
	private void selectItem(int position) {
	  Toast.makeText(AppNewHomeActivity.this, getResources().getString(R.string.app_name) , Toast.LENGTH_SHORT).show();
	}
	
	

}
