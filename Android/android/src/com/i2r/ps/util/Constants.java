package com.i2r.ps.util;

public class Constants {

	public static final long LIMIT_PER_PAGE = 50;
	public static final long LIMIT_INIT_LOAD = 100;

	public static final int HTTP_REQUEST_TIME_OUT = 5000;

	public static final String URL_BASE = "http://54.254.151.32";
	public static final String PATH_POST_LIST = "/mpost/list/";
	public static final String PATH_POST_IMAGE = "/images/posts/";
	public static final String PATH_POST_UPLOAD = "/mpost/upload/";
	public static final String PATH_USERS = "/mpost/users/";
	public static final String PATH_LOGIN = "/mpost/login";
	public static final String PATH_IMAGES = "/images/avatar/";
	public static final String PATH_THUMBS = "/mpost/thumbs/";
	public static final String PATH_GET_REQUESTS="/mpost/getRequests/";
	public static final String PATH_GET_USER="/mpost/getuser/uid/";
	

	public static final String PARAM_USER_EMAIL = "User[email]";
	public static final String PARAM_USER_PSD = "User[password]";
	public static final String PARAM_PAGE_NUM = "page_num";
	public static final String PARAM_POST_IMAGE = "Post[image]";
	public static final String PARAM_POST_DESC = "Post[description]";
	public static final String PARAM_POST_TAG = "Post[tag]";
	public static final String PARAM_POST_SEVERITY = "Post[severity]";
	public static final String PARAM_POST_UID = "Post[uid]";
	public static final String PARAM_POST_LAT = "Post[lat]";
	public static final String PARAM_POST_LNG = "Post[lng]";
	public static final String PARAM_POST_CATEGORY = "Post[category]";
	public static final String PARAM_POST_ADDRESS = "Post[address]";
	public static final String PARAM_SORT = "SORT_BY";
	
	public static final String PARAM_ENDORSEMENT_ENDORSER="Endorsement[endorser]";
	public static final String PARAM_ENDORSEMENT_ENDORSEE="Endorsement[endorsee]";
	public static final String PARAM_ENDORSEMENT_STATE="Endorsement[state]";
	
	public static final String PARAM_THUMBS_POST_ID="Thumbs[postid]";
	public static final String PARAM_THUMBS_USER_ID="Thumbs[uid]";
	public static final String PARAM_THUMBS_VALUE="Thumbs[value]";

	public static final String MIME_PLAIN_TYPE = "text/plain";
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String PREF_SHARE = "pref_share";
	public static final String PREF_ABOUT = "pref_about";
	public static final String PREF_FEEDBACK = "pref_feedback";
	public static final CharSequence PREF_LOGIN = "pref_login";
	public static final String PREF_ENDORSEMENT_REQUESTS= "pref_endorsement_requests";
	public static final String PREF_ENDORSING_REQUESTS= "pref_endorsing_requests";

	public static final String CONTACT_EMAIL = "";

	public static final String FEEDBACK_URL = "http://54.254.151.32";
	public static final String PATH_FEEDBACK = "/mpost/feedback";

	public static final String PARAM_PACKAGE_NAME = "Feedback[email]";
	public static final String PARAM_FEED_BACK_CONTENT = "Feedback[content]";
	public static final String PARAM_FEED_BACK_EMAIL = "Feedback[email]";
	public static final String PARAM_FEED_BACK_ANDROID_VERION = "Feedback[api_version]";
	public static final String PARAM_FEED_BACK_DEVICE_NAME = "Feedback[device_name]";

	public static final int MAP_DEFAULT_LEVEL2 = 12;
	public static final double MAP_DEFAULT_LAT = 1.304042;
	public static final double MAP_DEFAULT_LNG = 103.830407;
	public static final String NO_ENDORSEMENT="0";
	public static final String REQUEST_TO_ENDORSE="1";
	public static final String REQUEST_TO_BE_ENDORSE="2";
	public static final String HAVING_ENDORSEMENT="3";
	
	public static final String THUMBS_UP_VALUE="1";
	public static final String THUMBS_NORMAL_VALUE="0";
	public static final String THUMBS_DOWN_VALUE="-1";
	
	public static final int DEFAULT_USER_ID_WITHOUT_LOGIN=-1;

	public static final int SERVERITY_DEFAULT_LEVEL = 5;
	
	public static final String REQUEST_TYPE="request_type";
	public static final int ENDORSEMENT_REQUESTS=2;
	public static final int ENDORSRING_REQUESTS=1;
}
