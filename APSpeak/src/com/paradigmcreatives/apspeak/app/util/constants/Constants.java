package com.paradigmcreatives.apspeak.app.util.constants;

import java.util.HashMap;
import java.util.Map;

import com.paradigmcreatives.apspeak.doodleboard.layers.Layer.LayerType;

/**
 * Defines a number of constants used throughout the application.
 * 
 * @author
 * 
 */
public class Constants {

	// Debug
	public final static boolean DEBUG = true;
	public static final String PLATFORM_VALUE = "ANDROID";
	public static final long SPLASH_SCREEN_DURATION = 1500;

	public static final String KEY_APP_PREFERENCE_NAME = "com.paradigmcreatives.apspeak";
	public static final String KEY_APP_VERSION = "com.neuv.doodlydoo.app.version";
	public static final String KEY_GCM_ID = "com.paradigmcreatives.apspeak.gcmid";
	public static final String KEY_SESSION_ID = "com.paradigmcreatives.apspeak.sessionid";
	public static final String KEY_USER_ID = "com.paradigmcreatives.apspeak.userid";
	public static final String KEY_USER_PROFILE_NAME = "com.paradigmcreatives.apspeak.userprofile.name";
	public static final String KEY_USER_PROFILE_UNIQUEHANDLE = "com.neuv.whasay.userprofile.uniquehandle";
	public static final String KEY_USER_PROFILE_GENDER = "com.neuv.whasay.userprofile.gender";
	public static final String KEY_USER_PROFILE_EMAIL = "com.neuv.whasay.userprofile.country";
	public static final String KEY_USER_PROFILE_PASSWORD = "com.neuv.whasay.userprofile.password";
	public static final String KEY_GROUP_ID = "com.paradigmcreatives.apspeak.groupid";
	public static final String KEY_GROUP_NAME = "com.paradigmcreatives.apspeak.groupname";
	public static final String KEY_FEATURED_FLAG = "com.paradigmcreatives.apspeak.featured.flag";
	public static final String KEY_NOTIFICATIONS_COUNT = "com.paradigmcreatives.apspeak.notifications.count";
	public static final String KEY_NOTIFICATION_ID = "com.paradigmcreatives.apspeak.notification.id";
	public static final String KEY_USER_ADDED_TO_GROUP_FLAG = "com.paradigmcreatives.apspeak.isUserAddedToGroup";

	public static final String IS_USER_PROFILE_COMPLETE = "com.neuv.whasay.is.userprofile.complete";
	public static final String IS_FACEBOOK_PROFILE_CAPTURED = "com.neuv.whasay.is.facebookprofile.captured";
	public static final String GCM_REGISTRATION_NOTIFICATION = ".REGISTRATION";

	
	    public static final int IDEAS_FETCHLIMIT = 10000;
	// Image circle
	public static final int HEADER_IMAGE_SIZE = 65;

	// Image compression quality
	public static final int COMPRESSION_QUALITY_HIGH = 75;

	// Water-marking of images being shared
	public static final int WATERMARK_ALPHA = 150;

	// Facebook
	public static final String FACEBOOK_APPID = "865329090157392";

	// Flag use to enable disable session while making server requests
	public static boolean IS_SESSION_ENABLED = true;

	// Response header names
	public static final String RESPONSE_SET_COOKIE = "Set-Cookie";
	public static final String REQUEST_COOKIE = "Cookie";

	public static final String[] FB_PERMISSIONS_ARRAY = { "user_birthday",
			"user_location", "user_likes", "email", "user_about_me",
			"user_hometown", "user_interests", "user_photos", "user_friends",
			"user_education_history", "user_notes", "user_work_history",
			"friends_birthday", "friends_relationship_details",
			"friends_interests", "friends_relationships", "friends_likes",
			"xmpp_login" };

	public static final String FACEBOOK_CONNECT_FRAGMENT_TAG = "facebook_connect_fragment";
	public static final String FRIENDS_NETWORK_FRAGMENT_TAG = "friends_network_fragment";

	// Facebook profile related KEYs
	public static final String KEY_FACEBOOK_PROFILE_ID = "com.neuv.doodlydoo.facebookprofile.id";
	public static final String KEY_FACEBOOK_PROFILE_ACCESSTOKEN = "com.neuv.doodlydoo.facebookprofile.accesstoken";
	public static final String KEY_FACEBOOK_PROFILE_USERNAME = "com.neuv.doodlydoo.facebookprofile.username";
	public static final String KEY_FACEBOOK_PROFILE_FIRSTNAME = "com.neuv.doodlydoo.facebookprofile.firstname";
	public static final String KEY_FACEBOOK_PROFILE_LASTNAME = "com.neuv.doodlydoo.facebookprofile.lastname";
	public static final String KEY_FACEBOOK_PROFILE_BIRTHDAY = "com.neuv.doodlydoo.facebookprofile.birthday";
	public static final String KEY_FACEBOOK_PROFILE_BIO = "com.neuv.doodlydoo.facebookprofile.bio";
	public static final String KEY_FACEBOOK_PROFILE_PICTURE_URL = "com.neuv.doodlydoo.facebookprofile.picture.url";
	public static final String KEY_FACEBOOK_PROFILE_COVERIMAGE_URL = "com.neuv.doodlydoo.facebookprofile.coverimage.url";

	public static final String APP_IS_LAUNCHED = "app_is_launched";
	public static final String IS_WELCOME_SCREEN_DISPLAYED = "app_is_welcome_screen_displayed";
	public static final String IS_UNIQUE_HANDLE_CREATED = "com.neuv.doodlydoo.is.uniquehandle.created";

	public static final int BUBBLE_IMAGE_SIZE = 50;
	public static final String NEW_USER_KEY = "newuserkey";
	public static final String NEW_USER = "newuser";
	public static final int BATCH_FETCHLIMIT = 20;
	public static final int FRIENDS_FETCHLIMIT = 1000;
	public static final String URL_SLASH = "/";

	// Intent extra constants
	public static final String LAUNCH_ASSET_DETAILS = "assetDetails";
	public static final String LAUNCH_ASSETLIKED_USERSLIST = "assetLikedUsersList";
	public static final String LAUNCH_ASSETREPOSTED_USERSLIST = "assetRepostedUsersList";
	public static final String LAUNCH_GLOBALSTREAM_SCREEN = "globalStreamScreen";
	public static final String LAUNCH_INVITE_FBFRIENDS_SCREEN = "inviteFBFriendsScreen";
	public static final String LAUNCH_SETTINGS_SCREEN = "settingsScreen";
	public static final String LAUNCH_NOTIFICATIONS_FEED_SCREEN = "notificationsFeedScreen";
	public static final String LIKED_USERS = "likedUsers";
	public static final String ASSET_AS_JSON = "assetAsJSON";
	public static final String ASSET_OBJECT = "assetObject";
	public static final String CUE_OBJECT = "cueObject";
	public static final String CUE_ID = "cueId";

	// Sharing packages
	public static final String FACEBOOK_APP_PACKAGE = "com.facebook.katana";
	public static final String WHATSAPP_APP_PACKAGE = "com.whatsapp";
	public static final String TWITTER_APP_PACKAGE = "com.twitter.android";
	public static final String PINTEREST_APP_PACKAGE = "com.pinterest";
	public static final String DOODLY_DOO_APP_PACKAGE = "com.neuv.doodlydoo";
	public static final String MESSAGING_APP_PACKAGE = "com.android.mms";
	public static final String ASSETID = "asset_id";
	public static final String ANNOUNCEMENTID = "announcement_id";
	public static final String ANNOUNCEMNETMESSAGE = "announcement_message";

	// Initializing the exit states
	public static enum ExitState {
		NORMAL_EXIT, CONNECTION_INTERRUPTED, PARSING_FAILED, WRONG_ENCODING, UNKNOWN, WRONG_CLIENT_PROTOCOL
	}

	public static final String ISFRONTCAMERA = "IsFrontCamera";
	public static final String ISFROMGALLERY = "IsFromGallery";
	public final static String CANVAS_INTENT_SOURCE_KEY = "source";
	public final static String CANVAS_INTENT_SOURCE_CAMERA = "camera";
	public final static String CANVAS_INTENT_SOURCE_OTHER = "other";
	public final static String CANVAS_INTENT_BITMAP_PATH = "bitmap_file";
	public final static String ROTATE_BITMAP = "rotate_bitmap";
	public final static String IS_FILTERED_IMAGE = "is_filtered_image";

	public static final String APP_NAME = "Whatsay";
	public static final String DOODLE_RECEIVE_COUNT = "doodle_receive_count";
	public static final String DOODLE_SAVE_COUNT = "doodle_save_count";

	// Scaling
	public static final float MAX_SCALE = 5.0f;
	public static final float MIN_SCALE = 0.05f;

	public final static String ASSET_PARCELABLE = "asset_parcelable";

	// Max limit of assets to be fetched from the server
	public final static int MAX_ASSETS_FETCH_LIMIT = 500;

	/**
	 * Represents the state of the thread
	 * 
	 * @author robin
	 * 
	 */
	public static enum State {
		RUNNING, STOPPED
	}

	public static final String KEY_DOODLEWORLD_SYNC_TS = "com.paradigmcreatives.apspeak.doodleworld.sync.ts";
	public static final String KEY_GREETINGS_SYNC_TS = "com.paradigmcreatives.apspeak.greetings.sync.ts";
	public static final String KEY_RAGE_FACES_SYNC_TS = "com.paradigmcreatives.apspeak.ragefaces.sync.ts";
	public static final String KEY_EMOJIS_SYNC_TS = "com.paradigmcreatives.apspeak.emojis.sync.ts";

	// Asset sync cool off period
	public static final long ASSET_SYNC_COOLOFF = 86400000; // one day

	// Play speed. Its inversely proportional
	public final static int DOODLE_SPEED_SLOW = 30;
	public final static int DOODLE_SPEED_MEDIUM = 1;
	public final static int DOODLE_SPEED_HIGH = 0;
	public final static int DEFAULT_DOODLE_SPEED = DOODLE_SPEED_HIGH;

	public final static int BRUSH_SIZE1 = 1;
	public final static int BRUSH_SIZE2 = 5;
	public final static int BRUSH_SIZE3 = 15;
	public final static int BRUSH_SIZE4 = 25;
	public final static int BRUSH_SIZE5 = 35;
	public final static int BRUSH_SIZE6 = 50;

	public final static int DEFAULT_BRUSH_SIZE = 5;
	public final static String BRUSH_COLOR_WHITE = "#ffffff";
	public final static String BRUSH_COLOR_BLACK = "#000000";
	public final static String DEFAULT_BACKGROUND_COLOR = BRUSH_COLOR_WHITE;
	public final static String DEFAULT_BRUSH_COLOR = BRUSH_COLOR_BLACK;
	public final static String DEFAULT_BRUSH_BACKGROUND = "#d6d6d6";

	// Doodle View Integration
	public final static int LAYER_SELECTION_ALPHA = 120;
	public final static String LOGO_BLUE = "#1A9FE0";
	public final static String LOGO_RED = "#E51400";
	public final static String LOGO_YELLOW = "#F1A40A";
	public final static String LOGO_GREEN = "#59AA1D";

	public final static Map<LayerType, String> LAYER_SELECTION_MASK;
	static {
		LAYER_SELECTION_MASK = new HashMap<LayerType, String>();
		LAYER_SELECTION_MASK.put(LayerType.EMOJI, LOGO_BLUE);
		LAYER_SELECTION_MASK.put(LayerType.TEXT, LOGO_RED);
		LAYER_SELECTION_MASK.put(LayerType.GOOGLE_IMAGES, LOGO_YELLOW);
		LAYER_SELECTION_MASK.put(LayerType.RAGE_FACE, LOGO_GREEN);
		LAYER_SELECTION_MASK.put(LayerType.CAMERA, LOGO_BLUE);
		LAYER_SELECTION_MASK.put(LayerType.MAP, LOGO_YELLOW);
		LAYER_SELECTION_MASK.put(LayerType.GREETINGS, LOGO_GREEN);
		LAYER_SELECTION_MASK.put(LayerType.GALLERY, LOGO_RED);
		LAYER_SELECTION_MASK.put(LayerType.VIA_INTENT, LOGO_RED);
		LAYER_SELECTION_MASK.put(LayerType.FRAME, LOGO_BLUE);
		LAYER_SELECTION_MASK.put(LayerType.STICKER, LOGO_BLUE);
	}

	// Text properties constants
	public final static String DEFAULT_TEXT_COLOR = DEFAULT_BRUSH_COLOR;
	public final static int DEFAULT_TEXT_SIZE = 40;
	public final static int DEFAULT_TEXT_FONT_INDEX = 13;
	public final static int MAX_TEXT_SIZE = 150;
	public final static int MIN_TEXT_SIZE = 10;

	// Brush color constants
	public final static String BRUSH_COLOR_YELLOW = "#feda02";
	public final static String BRUSH_COLOR_PURPLE = "#8709dc";
	public final static String BRUSH_COLOR_GREEN = "#01e202";
	public final static String BRUSH_COLOR_BLUE = "#0789dd";
	public final static String BRUSH_COLOR_ORANGE = "#ff8202";
	public final static String BRUSH_COLOR_RED = "#f40000";
	public final static String BRUSH_COLOR_COOL_BLUE = "#00c5f2";

	// Auto sync interval
	public static final int MIN_AUTO_SYNC_INTERVAL = 5;

	public static final String KEY_DOODLE_PLAY_SPEED = "com.paradigmcreatives.apspeak.playspeed";
	public static final int SETTINGS_OPTIONS_REQUESTCODE = 111;

	public static final String EMOJI_CATEGORIES_FILENAME = "emoji_categories.txt";
	public static final String FRAME_CATEGORIES_FILENAME = "frame_categories.txt";
	public static final String EMOJI_LIST_FILENAME = "emoji_list.txt";
	public static final String FRAME_LIST_FILENAME = "frame_list.txt";
	public static final String FRIENDS_LIST_FILENAME = "friends_list.txt";

	public static final String IS_COMMENT = "isComment";
	public static final String COMMENT_ON_ASSET_ID = "comment_on_asset_id";

	// Push notification related constants
	public final static String USER_FOLLOWED = "USER_FOLLOWED";
	public final static String NEW_FRIEND = "NEW_FRIEND";
	public final static String JOIN = "JOIN";
	public final static String ASSET_LOVED = "LOVE";
	public final static String ASSET_COMMENTED = "COMMENT";
	public final static String NEW_EXPRESSION = "NEW_EXPRESSION";
	public final static String EXPRESSION = "EXPRESSION";
	public final static String ANNOUNCEMENT = "ANNOUNCEMENT";
	public final static String USER = "USER";
	public final static String EMOTE = "EMOTE";
	public final static String SHOW_FEATURED = "SHOW_FEATURED";
	public final static String ACCEPTED = "ACCEPTED";
	public final static String FOLLOW = "FOLLOW";
	public static final String USERID = "user_id";
	public static final String PROFILE_PICTURE_URL = "PROFILE_PICTURE_URL";
	public static final String FACEBOOK_PROFILE_PICTURE = "FB_PROFILE_PICTURE";
	public static final String FRIEND_OBJECT = "friend_object";
	public static final String ALERT_MESSAGE = "alert_message";
	public static final String NOTIFICATION_LARGE_ICON = "notification_large_icon";
	public static final String NOTIFICATION_INTENT = "notificationIntent";
	public static final int WHATSAY_ASSET_LOVED = 1;
	public static final int WHATSAY_NEW_FRIEND_JOINED_IDs = 2;
	public static final int WHATSAY_USER_FOLLOWED_IDs = 3;
	public static final int WHATSAY_ASSET_COMMENTEDs = 4;
	public static final int WHATSAY_NEW_EXPRESSIONs = 5;
	public static final int WHATSAY_ANNOUNCEMENTs = 6;

	public final static String KEY_USERSTREAM_HELPOVERLAY = "key_userstream_helpOverlay";
	public final static String KEY_ASSETDETAILS_HELPOVERLAY = "key_assetdetails_helpOverlay";
	public final static String KEY_TEXT_HELPOVERLAY = "key_text_helpOverlay";
	public final static String KEY_CAMERA_HELPOVERLAY = "key_camera_helpOverlay";
	public final static String KEY_CANVAS_HELPOVERLAY = "key_canvas_helpOverlay";

	public static final String LAUNCH_FILTERS_FRAGMENT = "launfiltersfragment";
	public static final int USER_ADDITON_TO_GROUP_FAILED_ERROR = 99;

	public static final int ASSET_DETAILS_RESULT_CODE = 410;

	// Notifications count broadcast action
	public static final String NOTIFICATIONSCOUNT_BROADCAST_ACTION = "notificationscount_broadcast_action";
	// AutoSend Success broadcast action
	public static final String AUTOSEND_STATUS_BRAODCAST_ACTION = "autosend_status_broadcast_action";

	public static final String KEY_CAMERA_FLASH_MODE = "key_camera_flash_mode";
	public static final int FLASH_MODE_AUTOMATIC = 1;
	public static final int FLASH_MODE_ON = 2;
	public static final int FLASH_MODE_OFF = 3;
	public static final String KEY_CAMERA_FACING = "key_camera_facing";
	public static final String DOODLE = "DOODLE";
	public static final String IMAGE = "IMAGE";
	
}// end of class

