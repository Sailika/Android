package com.paradigmcreatives.apspeak.app.util.constants;

/**
 * All Constants related to Whatsay server
 * 
 * @author Dileep | neuv
 * 
 */
public class ServerConstants {

	// Development server urls
	public static final String SERVER_URL = "http://smacx-node-server-dev.herokuapp.com/rv3";
	public static final String NODE_SERVER_URL = "http://smacx-node-server-dev.herokuapp.com/api/v1";

	// Production server urls
	//public static final String SERVER_URL = "https://rest.whatsayapp.com/rv3";
	//public static final String NODE_SERVER_URL = "http://node.whatsayapp.com/api/v1";

	// Timeout
	public static final int CONNECTION_TIMEOUT = 60000;

	// suffixes
	public static final String REGISTER_USER = "/user/register";
	public static final String USER_SIGNIN = "/user/signin";
	public static final String FOLLOWING = "/following/";
	public static final String FOLLOWERS = "/followers/";
	public static final String INVITE_FB_LIST = "/user/friends/";
	public static final String USER_PROFILE_NETWORK_FETCH = "/profile/user";
	public static final String FOLLOW_USER = "/followuser";
	public static final String UNFOLLOW_USER = "/unfollowuser";
	public static final String INVITE_USER = "/invite/fb/send";
	public static final String DOES_FOLLOW_PREFIX = "/does/user/";
	public static final String DOES_FOLLOW_BETWEEN = "/follow/";
	public static final String USER_STREAM_5_FETCH_SUFFIX = "/stream5/";
	public static final String USER_STREAM_FETCH_SUFFIX = "/stream/";
	public static final String PERSONAL_STREAM_FETCH_SUFFIX = "personal/";
	public static final String USER_STREAM_FETCH_PREFIX = "/user/";
	public static final String ASSET_REPOST = "/asset/repost";
	public static final String DELETE_ASSET_RELATIONSHIP = "/asset/delete/relationship";
	public static final String CREATE_ASSET_RELATIONSHIP = "/asset/create/relationship";
	public static final String ASSET = "/asset/";
	public static final String ASSET_SUBMIT = "/userstream/submit";
	public static final String USER_PROFILE_HANDLE = "/user/profile/handle/check/";
	public static final String SETTINGS = "/settings";
	public static final String REQUIRE_FOLLOW_PERMISSION = "/requireFollowPermission/";
	public static final String COMMENTS = "/comments/";
	public static final String COMMENT_CREATE = "/userstream/comment/";
	public static final String USERASSET = "/user/asset";
	public static final String ASSOCIATIONS = "/associations";
	public static final String USER_COMPACTPROFILE_FETCH = "/compact";

	public static final String GREETINGS_ASSET_URL = "/greeting/list";
	public static final String RAGE_FACE_ASSET_URL = "/rageface/list";
	public static final String DOODLEWORLD_ASSET_URL = "/doodleworld/list";
	public static final String EMOJI_ASSET_URL = "/emoji/list";
	public static final String FRAME_ASSETSLIST_URL = "/frame/list";
	public static final String GET_CUE_LIST = "/cue/list";

	public static final String GET_CUE_BACKGROUNDS = "/cue/";
	public static final String GET_CUE_BACKGROUNDS_SIMPLIFY = "/simplify";

	public static final String MYFEED_URL = "/notification/feed/user/";
	public static final String GET_GROUPS_LIST = "/group/list";
	public static final String ADD_USER_TO_GROUP = "/group/member/add";
	public static final String GET_USER_GROUPS_LIST = "/group/user";
	public static final String GET_ANNOUCEMENT_LIST = "/announcement/list";
	public static final String LIST = "/list";

	// Assets' Category URLs
	public static final String GREETINGS_CATEGORIES_URL = "/greeting/categories";
	public static final String RAGE_FACE_CATEGORIES_URL = "/rageface/categories";
	public static final String EMOJI_CATEGORIES_URL = "/emoji/categories";
	public static final String DOODLEWORLD_CATEGORIES_URL = "/doodleworld/categories";
	public static final String FRAME_CATEGORIES_URL = "/frame/categories";
	public static final String STICKER_CATEGORIES_URL = "/sticker/categories";
	public static final String INAPPROPRIATE_URL = "/as/inappropriate";
	public static final String FLAG_ASSET_URL = "/flag/asset/";
	public static final String FLAG_USER_URL = "/user/action/flag";

	public static final String ASSET_DELETE = "/userstream/asset/delete";
	// Content type
	public static final String CONTENT_TYPE_JSON = "application/json";

}
