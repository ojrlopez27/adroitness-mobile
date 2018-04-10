package edu.cmu.adroitness.client.commons.control;
import java.util.HashMap;

/**
 * Created by oscarr on 12/18/14.
 */
public final class Constants {
    /** Messages **/
    public static final String MSG_LAUNCH_BASE_NEWS_ACTIVITY = "MSG_LAUNCH_BASE_NEWS_ACTIVITY";
    public static final String MSG_LAUNCH_EXT_NEWS_ACTIVITY = "MSG_LAUNCH_EXT_NEWS_ACTIVITY";
    public static final String MSG_REQUEST_NEWS_ITEMS = "MSG_REQUEST_NEWS_ITEMS";
    public static final String MSG_SHOW_MODIFIED_NEWS = "MSG_SHOW_MODIFIED_NEWS";
    public static final String MSG_LOGIN_NEWS = "MSG_LOGIN_NEWS";
    public static final String MSG_GET_USER_PROFILE = "MSG_GET_USER_PROFIL";
    public static final String MSG_UPDATE_NEWS = "MSG_UPDATE_NEWS";
    public static final String MSG_GET_NEWS_ITEMS = "MSG_GET_NEWS_ITEMS";
    public static final String MSG_APPLY_FILTERS = "MSG_APPLY_FILTERS";
    public static final String MSG_SHOW_NEWS_ARTICLE = "MSG_SHOW_NEWS_ARTICLE";
    public static final String MSG_EXPAND_NEWS_ARTICLE = "MSG_EXPAND_NEWS_ARTICLE";
    public static final String MSG_LAUNCH_ACTIVITY = "MSG_LAUNCH_ACTIVITY";
    public static final String MSG_GET_ARTICLE_POSITION = "MSG_GET_ARTICLE_POSITION";
    public static final String MSG_SHOW_CURRENT_NEWS_ARTICLE = "MSG_SHOW_CURRENT_NEWS_ARTICLE";
    public static final String MSG_SHOW_NEXT_NEWS_ARTICLE = "MSG_SHOW_NEXT_NEWS_ARTICLE";
    public static final String MSG_SHOW_PREVIOUS_NEWS_ARTICLE = "MSG_SHOW_PREVIOUS_NEWS_ARTICLE";
    public static final String MSG_START_AUDIO_RECORD = "MSG_START_AUDIO_RECORD";
    public static final String MSG_STOP_AUDIO_RECORD = "MSG_STOP_AUDIO_RECORD";
    public static final String MSG_UPLOAD_TO_SERVER = "MSG_UPLOAD_TO_SERVER";
    public static final String MSG_FILTER_NEWS_BY_EMAIL = "MSG_FILTER_NEWS_BY_EMAIL";
    public static final String MSG_GET_MODEL_DISTRIBUTIONS = "MSG_GET_MODEL_DISTRIBUTIONS";
    public static final String MSG_PROCESS_EVENTS_CALENDAR = "MSG_PROCESS_EVENTS_CALENDAR";
    public static final String MSG_CHOOSE_ACCOUNT = "MSG_CHOOSE_ACCOUNT";
    public static final String MSG_DEVICE_ONLINE = "MSG_DEVICE_ONLINE";
    public static final String MSG_GET_SELECTED_ACCOUNT_NAME = "MSG_GET_SELECTED_ACCOUNT_NAME";
    public static final String MSG_GET_AWARE_SETTINGS = "MSG_GET_AWARE_SETTINGS";
    public static final String MSG_START_AWARE_PLUGIN = "MSG_START_AWARE_PLUGIN";
    public static final String MSG_STOP_AWARE_PLUGIN = "MSG_STOP_AWARE_PLUGIN";
    public static final String MSG_WEATHER_CHANGE_FORECAST_MODE = "MSG_WEATHER_CHANGE_FORECAST_MODE";
    public static final String MSG_SET_LOCATION = "MSG_SET_LOCATION";
    public static final String MSG_WRITE_LOCATION_DATA_FILE = "MSG_WRITE_LOCATION_DATA_FILE";
    public static final String MSG_ENABLE_HISTORY_LOCATION = "MSG_ENABLE_HISTORY_LOCATION";
    public static final String MSG_SET_MAX_NUM_HISTORY_LOCATION = "MSG_SET_MAX_NUM_HISTORY_LOCATION";
    public static final String MSG_SET_DEFAULT_CURRENT_PLACE="MSG_SET_DEFAULT_CURRENT_PLACE";
    public static final String MSG_GET_CURRENT_LOCATION_BY_COORDINATES="MSG_GET_CURRENT_LOCATION_BY_COORDINATES";
    public static final String MSG_WEATHER_SUBSCRIBE_TO_FORECAST = "MSG_WEATHER_SUBSCRIBE_TO_FORECAST";
    public static final String MSG_SEARCH_HOTEL = "MSG_SEARCH_HOTEL";
    public static final String MSG_STOP_SENSORS = "MSG_STOP_SENSORS";
    public static final String MSG_STOP_PLUGINS = "MSG_STOP_PLUGINS";
    public static final String MSG_STATUS_BATTERY = "MSG_STATUS_BATTERY";
    public static final String MSG_CONFIG_ACCOUNT_NAME = "MSG_CONFIG_ACCOUNT_NAME";
    public static final String MSG_GOOGLE_PLAY_AVAILABLE = "MSG_GOOGLE_PLAY_AVAILABLE";
    public static final String MSG_GOOGLE_PLAY_AVAILABLE_ERROR = "MSG_GOOGLE_PLAY_AVAILABLE_ERROR";
    public static final String MSG_GET_CALENDAR_EVENTS = "MSG_GET_CALENDAR_EVENTS";
    public static final String MSG_GET_AR_NAME = "MSG_GET_AR_NAME";
    public static final String MSG_SENSOR_SETTINGS = "MSG_SENSOR_SETTINGS";
    public static final String MSG_WEATHER_GET_REPORT = "MSG_WEATHER_GET_REPORT";
    public static final String MSG_WEATHER_UNSUBSCRIBE_TO_FORECAST = "MSG_WEATHER_UNSUBSCRIBE_TO_FORECAST";
    public static final String MSG_GET_LOCATION = "MSG_GET_LOCATION";
    public static final String MSG_RESET_CURRENT_LOCATION = "MSG_RESET_CURRENT_LOCATION";
    public static final String MSG_SET_LOCATION_SETTINGS = "MSG_SET_LOCATION_SETTINGS";
    public static final String MSG_GET_HISTORY_LOCATIONS = "MSG_GET_HISTORY_LOCATIONS";
    public static final String MSG_RESET_HISTORY_LOCATIONS = "MSG_RESET_HISTORY_LOCATIONS";
    public static final String MSG_SET_STREAMING_SETTINGS = "MSG_SET_STREAMING_SETTINGS";
    public static final String MSG_GET_CAMERA_STREAMING = "MSG_GET_CAMERA_STREAMING";
    public static final String MSG_STREAMING_UNSUBSCRIBE = "MSG_STREAMING_UNSUBSCRIBE";
    public static final String MSG_STREAMING_SUBSCRIBE = "MSG_STREAMING_SUBSCRIBE";
    public static final String MSG_STREAMING_START_PREVIEW = "MSG_STREAMING_START_PREVIEW";
    public static final String MSG_STREAMING_STOP_STREAM = "MSG_STREAMING_STOP_STREAM";
    public static final String MSG_CREATE_DECISION_RULE = "MSG_CREATE_DECISION_RULE";
    public static final String MSG_REMOVE_DECISION_RULE = "MSG_REMOVE_DECISION_RULE";
    public static final String MSG_ALARM_PLAY_RINGTONE = "MSG_ALARM_PLAY_RINGTONE";
    public static final String MSG_TASK_UNDERSTANDING = "MSG_TASK_UNDERSTANDING";
    public static final String MSG_SET_CALENDAR_SYNC_TIME = "MSG_SET_CALENDAR_SYNC_TIME";
    public static final String MSG_OAQA_QUESTION = "MSG_OAQA_QUESTION";
    public static final String MSG_NEIL_ENTITIES_FROM_URL = "MSG_NEIL_ENTITIES_FROM_URL";
    public static final String MSG_EMAIL_UNDERSTANDING_FEATURES = "MSG_EMAIL_UNDERSTANDING_FEATURES";
    public static final String MSG_EMAIL_UNDERSTANDING_SUMMARY= "MSG_EMAIL_UNDERSTANDING_SUMMARY";
    public static final String MSG_EFFECTOR_SEND_SMS = "MSG_EFFECTOR_SEND_SMS";

    public static final String MSG_APP_TRACKER_START_TRACKER = "MSG_APP_TRACKER_START_TRACKER";
    public static final String MSG_APP_TRACKER_STOP_TRACKER = "MSG_APP_TRACKER_STOP_TRACKER";
    public static final String MSG_APP_TRACKER_GET_TRACKING_SCRIPTS = "MSG_APP_TRACKER_GET_TRACKING_SCRIPTS";
    public static final String MSG_APP_TRACKER_GET_TRACKING_SCRIPT = "MSG_APP_TRACKER_GET_TRACKING_SCRIPT";
    public static final String MSG_APP_TRACKER_START_RECORDER = "MSG_APP_TRACKER_START_RECORDER";
    public static final String MSG_APP_TRACKER_STOP_RECORDER = "MSG_APP_TRACKER_STOP_RECORDER";
    public static final String MSG_APP_TRACKER_RUN_SCRIPT = "MSG_APP_TRACKER_RUN_SCRIPT";
    public static final String MSG_APP_TRACKER_RUN_JSON = "MSG_APP_TRACKER_RUN_JSON";
    public static final String MSG_APP_TRACKER_GET_RECORDING_SCRIPTS = "MSG_APP_TRACKER_GET_RECORDING_SCRIPTS";
    public static final String MSG_APP_TRACKER_ADD_AS_JSON_SCRIPT = "MSG_APP_TRACKER_ADD_AS_JSON_SCRIPT";
    public static final String MSG_APP_TRACKER_GET_RECORDING_SCRIPT = "MSG_APP_TRACKER_GET_RECORDING_SCRIPT";
    public static final String MSG_APP_TRACKER_CLEAR_TRACKING_LIST = "MSG_APP_TRACKER_CLEAR_TRACKING_LIST";
    public static final String MSG_APP_TRACKER_GET_ALL_PACKAGE_VOCAB = "MSG_APP_TRACKER_GET_ALL_PACKAGE_VOCAB";
    public static final String MSG_APP_TRACKER_GET_PACKAGE_VOCAB = "MSG_APP_TRACKER_GET_PACKAGE_VOCAB";
    public static final String MSG_APP_TRACKER_PROCESS_MULTIPURPOSE_REQUEST = "MSG_APP_TRACKER_PROCESS_MULTIPURPOSE_REQUEST";

    public static final String MSG_SUGILITE_SET_ACCESSIBILITY_SERVICE = "MSG_SUGILITE_SET_ACCESSIBILITY_SERVICE";
    public static final String MSG_SUGILITE_START_TRACKER = "MSG_SUGILITE_START_TRACKER";
    public static final String MSG_SUGILITE_STOP_TRACKER = "MSG_SUGILITE_STOP_TRACKER";
    public static final String MSG_SUGILITE_GET_TRACKING_SCRIPTS = "MSG_SUGILITE_GET_TRACKING_SCRIPTS";
    public static final String MSG_SUGILITE_GET_TRACKING_SCRIPT = "MSG_SUGILITE_GET_TRACKING_SCRIPT";
    public static final String MSG_SUGILITE_START_RECORDER = "MSG_SUGILITE_START_RECORDER";
    public static final String MSG_SUGILITE_STOP_RECORDER = "MSG_SUGILITE_STOP_RECORDER";
    public static final String MSG_SUGILITE_RUN_SCRIPT = "MSG_SUGILITE_RUN_SCRIPT";
    public static final String MSG_SUGILITE_RUN_JSON = "MSG_SUGILITE_RUN_JSON";
    public static final String MSG_SUGILITE_GET_RECORDING_SCRIPTS = "MSG_SUGILITE_GET_RECORDING_SCRIPTS";
    public static final String MSG_SUGILITE_ADD_AS_JSON_SCRIPT = "MSG_SUGILITE_ADD_AS_JSON_SCRIPT";
    public static final String MSG_SUGILITE_GET_RECORDING_SCRIPT = "MSG_SUGILITE_GET_RECORDING_SCRIPT";
    public static final String MSG_SUGILITE_CLEAR_TRACKING_LIST = "MSG_SUGILITE_CLEAR_TRACKING_LIST";
    public static final String MSG_SUGILITE_GET_ALL_PACKAGE_VOCAB = "MSG_SUGILITE_GET_ALL_PACKAGE_VOCAB";
    public static final String MSG_SUGILITE_GET_PACKAGE_VOCAB = "MSG_SUGILITE_GET_PACKAGE_VOCAB";
    public static final String MSG_SUGILITE_PROCESS_MULTIPURPOSE_REQUEST = "MSG_SUGILITE_PROCESS_MULTIPURPOSE_REQUEST";

    public static final String MSG_RED5_STREAMING_STOP = "MSG_RED5_STREAMING_STOP";
    public static final String MSG_RED5_STREAMING_START = "MSG_RED5_STREAMING_START";
    public static final String MSG_RED5_STREAMING_SHOW_CAMERA = "MSG_RED5_STREAMING_SHOW_CAMERA";
    public static final String MSG_RED5_STREAMING_STOP_CAMERA = "MSG_RED5_STREAMING_STOP_CAMERA";
    public static final String MSG_RED5_STREAMING_TOGGLE_CAMERA = "MSG_RED5_STREAMING_TOGGLE_CAMERA";
    public static final String MSG_RED5_STREAMING_GET_CAMERA_SIZES = "MSG_RED5_STREAMING_GET_CAMERA_SIZES";
    public static final String MSG_RED5_STREAMING_GET_IP_SERVER="MSG_RED5_STREAMING_GET_IP_SERVER";
    public static final String MSG_RED5_STREAMING_ATTACH_CAMERA_FRAGMENT="MSG_RED5_STREAMING_ATTACH_CAMERA_FRAGMENT";

    public static final String MSG_DIALOGUE_GMAIL_SERVICE="MSG_DIALOGUE_GMAIL_SERVICE";
    public static final String MSG_DIALOGUE_IMAP_SERVICE="MSG_DIALOGUE_IMAP_SERVICE";
    public static final String MSG_DIALOGUE_INITIALIZE="MSG_DIALOGUE_INITIALIZE";
    public static final String MSG_DIALOGUE_ASR_TEST = "MSG_DIALOGUE_ASR_TEST";
    public static final String MSG_DIALOGUE_START = "MSG_DIALOGUE_START";
    public static final String MSG_DIALOGUE_START_SERVICE="MSG_DIALOGUE_START_SERVICE";
    public static final String MSG_DIALOGUE_STOP = "MSG_DIALOGUE_STOP";
    public static final String MSG_DIALOGUE_SERVICE = "MSG_DIALOGUE_SERVICE";
    public static final String MSG_DIALOGUE_PROCESS = "MSG_DIALOGUE_PROCESS";
    public static final String MSG_RELEASE_RESOURCES="MSG_RELEASE_RESOURCES";

    public static final String MSG_RAPPORT_SUBSCRIBE = "MSG_RAPPORT_SUBSCRIBE";
    public static final String MSG_RAPPORT_UNSUBSCRIBE = "MSG_RAPPORT_UNSUBSCRIBE";

    public static final String MSG_GMAIL_GET_MESSAGE="MSG_GMAIL_GET_MESSAGE";
    public static final String MSG_GMAIL_GET_LABEL="MSG_GMAIL_GET_LABEL";
    public static final String MSG_GMAIL_GET_USER_PROFILE="MSG_GMAIL_GET_USER_PROFILE";
    public static final String MSG_GMAIL_INITIALIZE="MSG_GMAIL_INITIALIZE";
    public static final String MSG_GMAIL_UNREAD_NUM="MSG_GMAIL_UNREAD_NUM";
    public static final String MSG_GMAIL_GET_SENDER="MSG_GMAIL_GET_SENDER";
    public static final String MSG_GMAIL_GET_DATA_FROM_API="MSG_GMAIL_GET_DATA_FROM_API";
    public static final String MSG_GMAIL_FILTER_MESSAGES_BY_QUERY="MSG_GMAIL_FILTER_MESSAGES_BY_QUERY";
    public static final String MSG_GMAIL_GET_MIME_MESSAGE="MSG_GMAIL_GET_MIME_MESSAGE";
    public static final String MSG_GMAIL_UPDATE_LABEL_LIST_FROM_GMAIL="MSG_GMAIL_UPDATE_LABEL_LIST_FROM_GMAIL";
    public static final String MSG_GMAIL_GET_JSON_EMAIL_MESSAGE_DETAILS="MSG_GMAIL_GET_JSON_EMAIL_MESSAGE_DETAILS";
    public static final String MSG_GMAIL_GET_MESSAGE_CONTENT="MSG_GMAIL_GET_MESSAGE_CONTENT";
    public static final String MSG_GMAIL_SEND_EMAIL_MESSAGE="MSG_GMAIL_SEND_EMAIL_MESSAGE";
    public static final String MSG_GMAIL_SEND_EMAIL_MESSAGE_WITH_ATTACHMENT="MSG_GMAIL_SEND_EMAIL_MESSAGE_WITH_ATTACHMENT";
    public static final String MSG_GMAIL_GET_DATA_FROM_GMAIL ="MSG_GMAIL_GET_DATA_FROM_GMAIL";
    public static final String MSG_GMAIL_GET_ATTACHMENTS="MSG_GMAIL_GET_ATTACHMENTS";
    public static final String MSG_GMAIL_REPLY="MSG_GMAIL_REPLY";
    public static final String MSG_GMAIL_FORWARD="MSG_GMAIL_FORWARD";

    public static final String MSG_MULTIUSER_START = "MSG_MULTIUSER_START";
    public static final String MSG_MULTIUSER_STOP = "MSG_MULTIUSER_STOP";
    public static final String MSG_MULTIUSER_SEND = "MSG_MULTIUSER_SEND";
    public static final String MSG_MULTIUSER_RECEIVE = "MSG_MULTIUSER_RECEIVE";

    public static final String MSG_HELPR_GET_APP_RECOMMENDATION = "MSG_HELPR_GET_APP_RECOMMENDATION";

    public static final String MSG_IMAP_CHECK_INBOX="MSG_IMAP_CHECK_INBOX";
    public static final String MSG_IMAP_PARSE_MESSAGE="MSG_IMAP_PARSE_MESSAGE";
    public static final String MSG_IMAP_PARSE_SENDER="MSG_IMAP_PARSE_SENDER";
    public static final String MSG_IMAP_CLOSE_INBOX="MSG_IMAP_CLOSE_INBOX";
    public static final String MSG_IMAP_MARK_ALL_READ="MSG_IMAP_MARK_ALL_READ";
    public static final String MSG_IMAP_GET_UNREAD_NUM="MSG_IMAP_GET_UNREAD_NUM";
    public static final String MSG_IMAP_SEARCH_CONTENT="MSG_IMAP_SEARCH_CONTENT";
    public static final String MSG_IMAP_GET_MESSAGE="MSG_IMAP_GET_MESSAGE";
    public static final String MSG_MOVIE_RECOMMENDATION_QUERY="MSG_MOVIE_RECOMMENDATION_QUERY";
    public static final String MSG_MOVIE_RECOMMENDATION_RERANKING="MSG_MOVIE_RECOMMENDATION_RERANKING";
    public static final String MSG_MOVIE_RECOMMENDATION_FEEDBACK="MSG_MOVIE_RECOMMENDATION_FEEDBACK";
    public static final String MSG_CM_QUERY="MSG_CM_QUERY";
    public static final String MSG_CM_GET_EXAMPLES="MSG_CM_GET_EXAMPLES";
    public static final String MSG_NELL_ANALIZE_ENTITY = "MSG_NELL_ANALIZE_ENTITY";
    public static final String MSG_NELL_ANALIZE_DOCUMENT = "MSG_NELL_ANALIZE_DOCUMENT";
    public static final String MSG_MULTISENSE_SUBSCRIBE="MSG_MULTISENSE_SUBSCRIBE";
    public static final String MSG_MULTISENSE_UNSUBSCRIBE="MSG_MULTISENSE_UNSUBSCRIBE";

    public static final String MSG_GEOTAGGED_KEYWORDS_GET_CURRENT_LOCATION="MSG_GEOTAGGED_KEYWORDS_GET_CURRENT_LOCATION";
    public static final String MSG_GEOTAGGED_KEYWORDS_GET_PAST_LOCATION="MSG_GEOTAGGED_KEYWORDS_GET_PAST_LOCATION";
    public static final String MSG_GEOTAGGED_KEYWORDS_GET_METADATA_LOCATION="MSG_GEOTAGGED_KEYWORDS_GET_METADATA_LOCATION";
    public static final String MSG_GEOTAGGED_SURVEY_SEND_RESULT="MSG_GEOTAGGED_SURVEY_SEND_RESULT";

    public static final String MSG_CHORUS_BEGIN="MSG_CHORUS_BEGIN";
    public static final String MSG_CHORUS_SEND_USER_CHAT="MSG_CHORUS_SEND_USER_CHAT";
    public static final String MSG_CHORUS_RECEIVE_ALL="MSG_CHORUS_RECEIVE_ALL";
    public static final String MSG_CHORUS_QUERY_CONVERSATION="MSG_CHORUS_QUERY_CONVERSATION";
    public static final String MSG_CHORUS_END="MSG_CHORUS_END";

    public static final String MSG_CAPTION_GENERATION_SERVICE="MSG_CAPTION_GENERATION_SERVICE";
    public static final String MSG_GET_CAPTION_FROM_URL="MSG_GET_CAPTION_FROM_URL";
    public static final String MSG_GET_CAPTION_FROM_IMAGE="MSG_GET_CAPTION_FROM_IMAGE";
    public static final String MSG_CHORUS_GET_CONV_ID_STATUS="MSG_CHORUS_GET_CONV_ID_STATUS";
    public static final String MSG_PERFORMANCE_TEST = "DUMMY_";
    public static final String MULTIUSER_STOPPED = "MULTIUSER_STOPPED";


    public static HashMap<String, String> IDS = new HashMap<>();
    static {
        IDS.put(MSG_LAUNCH_BASE_NEWS_ACTIVITY, "SERVICES.NEWS");
        IDS.put( MSG_LAUNCH_EXT_NEWS_ACTIVITY, "SERVICES.NEWS");
        IDS.put( MSG_REQUEST_NEWS_ITEMS, "SERVICES.NEWS");
        IDS.put( MSG_SHOW_MODIFIED_NEWS, "SERVICES.NEWS");
        IDS.put( MSG_LOGIN_NEWS, "SERVICES.NEWS");
        IDS.put(MSG_GET_USER_PROFILE, "SERVICES.NEWS");
        IDS.put( MSG_UPDATE_NEWS, "SERVICES.NEWS");
        IDS.put( MSG_GET_NEWS_ITEMS, "SERVICES.NEWS");
        IDS.put( MSG_APPLY_FILTERS, "SERVICES.NEWS");
        IDS.put( MSG_SHOW_NEWS_ARTICLE, "SERVICES.NEWS");
        IDS.put( MSG_EXPAND_NEWS_ARTICLE, "SERVICES.NEWS");
        IDS.put( MSG_LAUNCH_ACTIVITY, "CONTROL.ACTIVITY");
        IDS.put( MSG_GET_ARTICLE_POSITION, "SERVICES.NEWS");
        IDS.put( MSG_SHOW_CURRENT_NEWS_ARTICLE, "SERVICES.NEWS");
        IDS.put( MSG_SHOW_NEXT_NEWS_ARTICLE, "SERVICES.NEWS");
        IDS.put( MSG_SHOW_PREVIOUS_NEWS_ARTICLE, "SERVICES.NEWS");
        IDS.put( MSG_START_AUDIO_RECORD, "SENSORS.AUDIO");
        IDS.put( MSG_STOP_AUDIO_RECORD, "SENSORS.AUDIO");
        IDS.put( MSG_UPLOAD_TO_SERVER, "SERVICES.UPLOAD");
        IDS.put( MSG_FILTER_NEWS_BY_EMAIL, "SERVICES.NEWS");
        IDS.put( MSG_GET_MODEL_DISTRIBUTIONS, "SERVICES.NEWS");
        IDS.put( MSG_PROCESS_EVENTS_CALENDAR, "SERVICES.CALENDAR");
        IDS.put( MSG_CHOOSE_ACCOUNT, "CONTROL.GOOGLE_SERVICES");
        IDS.put( MSG_DEVICE_ONLINE, "CONTROL.GOOGLE_SERVICES");
        IDS.put( MSG_GET_SELECTED_ACCOUNT_NAME, "CONTROL.GOOGLE_SERVICES");
        IDS.put( MSG_GET_AWARE_SETTINGS, "SENSORS");
        IDS.put( MSG_START_AWARE_PLUGIN, "SENSORS");
        IDS.put( MSG_STOP_AWARE_PLUGIN, "SENSORS");
        IDS.put( MSG_WEATHER_CHANGE_FORECAST_MODE, "SERVICES.WEATHER");
        IDS.put( MSG_SET_LOCATION, "SERVICES.LOCATION");
        IDS.put( MSG_WEATHER_SUBSCRIBE_TO_FORECAST, "SERVICES.WEATHER");
        IDS.put( MSG_SEARCH_HOTEL, "SERVICES.HOTEL");
        IDS.put( MSG_STOP_SENSORS, "SENSORS");
        IDS.put( MSG_STOP_PLUGINS, "SENSORS");
        IDS.put( MSG_STATUS_BATTERY, "SENSORS.BATERY");
        IDS.put( MSG_CONFIG_ACCOUNT_NAME, "CONTROL.GOOGLE_SERVICES");
        IDS.put( MSG_GOOGLE_PLAY_AVAILABLE, "CONTROL.GOOGLE_SERVICES");
        IDS.put( MSG_GOOGLE_PLAY_AVAILABLE_ERROR, "CONTROL.GOOGLE_SERVICES");
        IDS.put( MSG_GET_CALENDAR_EVENTS, "SERVICES.CALENDAR");
        IDS.put( MSG_GET_AR_NAME, "SERVICES.ACTIVITY_RECOGNITION");
        IDS.put( MSG_SENSOR_SETTINGS, "SENSORS");
        IDS.put( MSG_WEATHER_GET_REPORT, "SERVICES.WEATHER");
        IDS.put( MSG_WEATHER_UNSUBSCRIBE_TO_FORECAST, "SERVICES.WEATHER");
        IDS.put( MSG_GET_LOCATION, "SERVICES.LOCATION");
        IDS.put( MSG_RESET_CURRENT_LOCATION, "SERVICES.LOCATION");
        IDS.put( MSG_SET_LOCATION_SETTINGS, "SERVICES.LOCATION");
        IDS.put( MSG_GET_HISTORY_LOCATIONS, "SERVICES.LOCATION");
        IDS.put( MSG_RESET_HISTORY_LOCATIONS, "SERVICES.LOCATION");
        IDS.put( MSG_SET_STREAMING_SETTINGS, "SERVICES.STREAMING");
        IDS.put( MSG_GET_CAMERA_STREAMING, "SERVICES.STREAMING");
        IDS.put( MSG_STREAMING_UNSUBSCRIBE, "SERVICES.STREAMING");
        IDS.put( MSG_STREAMING_SUBSCRIBE, "SERVICES.STREAMING");
        IDS.put( MSG_STREAMING_START_PREVIEW, "SERVICES.STREAMING");
        IDS.put( MSG_STREAMING_STOP_STREAM, "SERVICES.STREAMING");
        IDS.put( MSG_CREATE_DECISION_RULE, "SERVICES.DECISION_RULE");
        IDS.put( MSG_REMOVE_DECISION_RULE, "SERVICES.DECISION_RULE");
        IDS.put( MSG_ALARM_PLAY_RINGTONE, "EFFECTORS.ALARM");
        IDS.put( MSG_TASK_UNDERSTANDING, "SERVICES.TASK_UNDERSTANDING");
        IDS.put( MSG_SET_CALENDAR_SYNC_TIME, "SERVICES.CALENDAR");
    }

    public static String getID(String id){
        String idS = IDS.get(id);
        return idS == null? ""+id : idS;
    }

    /** Request sync execution **/
    public static final String REQUEST_START = "REQUEST_START";
    public static final String REQUEST_DONE = "REQUEST_DONE";

    /** News Personalization **/
    public static final int PERSONALIZATION_MULTIBAND = 1;
    public static final int PERSONALIZATION_FROM_ADVISE = 2;
    public static final int PERSONALIZATION_MERGED = 3;
    public static final int PERSONALIZATION_NELL = 4;


    /** Tasks Understanding - Alex Rudnicky **/
    public static final String TASKS_UNDERSTANDING_QUERY = "TASKS_UNDERSTANDING_QUERY";
    public static final String TASKS_UNDERSTANDING_USER = "TASKS_UNDERSTANDING_USER";

    /** Bundle fields **/
    public static final String BUNDLE_ACTIVITY_NAME = "BUNDLE_ACTIVITY_NAME";
    public static final String BUNDLE_MODIFIED_NEWS = "BUNDLE_MODIFIED_NEWS";
    public static final String BUNDLE_FILTERS = "BUNDLE_FILTERS";
    public static final String BUNDLE_ARTICLE_ID = "BUNDLE_ARTICLE_ID";
    public static final String BUNDLE_TYPE_FILTER = "BUNDLE_TYPE_FILTER";
    public static final String BUNDLE_DRAWER_MANAGER = "BUNDLE_DRAWER_MANAGER";
    public static final String BUNDLE_CLEAR_FILTERS = "BUNDLE_CLEAR_FILTERS";
    public static final String BUNDLE_MESSAGE_TYPE = "BUNDLE_MESSAGE_TYPE";
    public static final String BUNDLE_MAIN_LAYOUT_ID = "BUNDLE_MAIN_LAYOUT_ID";
    public static final String BUNDLE_RESET_SAVED_INSTANCE = "BUNDLE_RESET_SAVED_INSTANCE";


    /** qualifiers **/
    public static final String QUALIFIER_NEWS = "QUALIFIER_NEWS";

    /** Flags **/
    public static final String FLAG_FORCE_RELOAD = "FORCE_RELOAD";
    public static final String FLAG_RETURN_JSON = "FLAG_RETURN_JSON";
    public static final String FLAG_UPDATE_NEWS = "FLAG_UPDATE_NEWS";
    public static final String FLAG_SEND_EVENT = "FLAG_SEND_EVENT";
    public static final String FLAG_REFRESH = "FLAG_REFRESH";


    /** layouts **/
    public static final String UI_PORTRAIT_LAYOUT = "UI_PORTRAIT_LAYOUT";
    public static final String UI_LANDSCAPE_LAYOUT = "UI_LANDSCAPE_LAYOUT";
    public static final String UI_NEWS_RANK = "UI_NEWS_RANK";
    public static final String UI_NEWS_TITLE = "UI_NEWS_TITLE";
    public static final String UI_NEWS_SCORE = "UI_NEWS_SCORE";
    public static final String UI_NEWS_SUMMARY = "UI_NEWS_SUMMARY";
    public static final String UI_NEWS_FEAT = "UI_NEWS_FEAT";
    public static final String UI_NEWS_FEAT2 = "UI_NEWS_FEAT2";
    public static final String UI_NEWS_PUBLISHER = "UI_NEWS_PUBLISHER";
    public static final String UI_NEWS_REASON = "UI_NEWS_REASON";
    public static final String UI_NEWS_IMG = "UI_NEWS_IMG";
    public static final String UI_NEWS_SHARE_FB = "UI_NEWS_SHARE_FB";
    public static final String UI_NEWS_SHARE_TWITTER = "UI_NEWS_SHARE_TWITTER";
    public static final String UI_NEWS_SHARE_TMBLR = "UI_NEWS_SHARE_TMBLR";
    public static final String UI_NEWS_SHARE_MORE = "UI_NEWS_SHARE_MORE";
    public static final String UI_NEWS_LIKE = "UI_NEWS_LIKE";
    public static final String UI_NEWS_DISLIKE = "UI_NEWS_DISLIKE";
    public static final String UI_NEWS_COMMENTS = "UI_NEWS_COMMENTS";

    /** news reader **/
    //Constants of JSON paths for properties of user profile
    public static final String JSON_YAHOO_USER_PROFILE_PATH = "yahoo-coke:*/yahoo-coke:debug-scoring/feature-response/result";
    public static final String JSON_USER_GENDER = "JSON_USER_GENDER";
    public static final String JSON_USER_AGE = "JSON_USER_AGE";
    public static final String JSON_POSITIVE_DEC_WIKIID = "POSITIVE_DEC WIKIID";
    public static final String JSON_POSITIVE_DEC_YCT = "POSITIVE_DEC YCT";
    public static final String JSON_NEGATIVE_DEC_WIKIID = "NEGATIVE_DEC WIKIID";
    public static final String JSON_NEGATIVE_DEC_YCT = "NEGATIVE_DEC YCT";
    public static final String JSON_FB_WIKIID = "FB WIKIID";
    public static final String JSON_FB_YCT = "FB YCT";
    public static final String JSON_CAP_ENTITY_WIKI = "JSON_CAP_ENTITY_WIKI";
    public static final String JSON_CAP_YCT_ID = "JSON_CAP_YCT_ID";
    public static final String JSON_NEGATIVE_INF_WIKIID = "NEGATIVE_INF WIKIID";
    public static final String JSON_NEGATIVE_INF_YCT = "NEGATIVE_INF YCT";
    public static final String JSON_USER_PROPUSAGE = "JSON_USER_PROPUSAGE";
    //Constants of JSON paths for properties of news content
    public static final String JSON_YAHOO_COKE_STREAM_ELEMENTS = "yahoo-coke:stream/elements";
    public static final String JSON_SS_FAKE_USER_PROFILE_PARAM_NAME = "profile";


    /** news item attributes **/
    public static final String NEWS = "NEWS";
    public static final String ARTICLE_INDEX = "index";
    public static final String ARTICLE_TITLE = "title";
    public static final String ARTICLE_UUID = "uuid";
    public static final String ARTICLE_REASON = "explain/reason";
    public static final String ARTICLE_URL = "snippet/url";
    public static final String ARTICLE_CATEGORIES = "snippet/categories";
    public static final String ARTICLE_SCORE = "score";
    public static final String ARTICLE_CAP_FEATURES = "cap_features";
    public static final String ARTICLE_RAW_SCORE_MAP = "raw_score_map";
    public static final String ARTICLE_PUBLISHER = "publisher";
    public static final String ARTICLE_IMAGE_URL = "snippet/image/original/url";
    public static final String ARTICLE_SUMMARY = "snippet/summary";
    public static final String ARTICLE_PREVIOUS_POSITION = "ARTICLE_PREVIOUS_POSITION";
    public static final String ARTICLE_NEXT_POSITION = "ARTICLE_NEXT_POSITION";

    /** rule validation operators **/
    public static final String OPERATOR_HIGHER_THAN = "OPERATOR_HIGHER_THAN";
    public static final String OPERATOR_EQUALS_TO = "OPERATOR_EQUALS_TO";
    public static final String OPERATOR_LOWER_THAN = "OPERATOR_LOWER_THAN";
    public static final String OPERATOR_CONTAINS_STRING = "OPERATOR_CONTAINS_STRING";
    public static final String OPERATOR_DATE_BEFORE = "OPERATOR_DATE_BEFORE";
    public static final String OPERATOR_DATE_AFTER = "OPERATOR_DATE_AFTER";
    public static final String OPERATOR_DATE_EQUAL = "OPERATOR_DATE_EQUAL";
    public static final String OPERATOR_TIME_EQUAL = "OPERATOR_TIME_EQUAL";
    public static final String OPERATOR_TIME_BEFORE = "OPERATOR_TIME_BEFORE";
    public static final String OPERATOR_TIME_AFTER = "OPERATOR_TIME_AFTER";

    /** Contents **/
    public static final String CONTENT_NEWS_LIST = "CONTENT_NEWS_LIST";
    public static final String CONTENT = "CONTENT";

    /** Set constant values **/
    public static final String AUDIO = "AUDIO";
    public static final String SET_NEWS_LIST_SIZE = "SET_NEWS_LIST_SIZE";
    public static final String SET_REFRESH_TIME = "SET_REFRESH_TIME";
    public static final String SET_UPDATE_TIME = "SET_UPDATE_TIME";
    public static final String SET_AUDIO_SAMPLE_RATE = "SET_AUDIO_SAMPLE_RATE";
    public static final String SET_AUDIO_CHANNEL_CONFIG = "SET_AUDIO_CHANNEL_CONFIG";
    public static final String SET_AUDIO_ENCODING = "SET_AUDIO_ENCODING";
    public static final String SET_SUBSCRIBER = "SET_SUBSCRIBER";
    public static final String SET_AUDIO_BUFFER_ELEMENTS_TO_REC = "SET_AUDIO_BUFFER_ELEMENTS_TO_REC";
    public static final String SET_AUDIO_BYTES_PER_ELEMENT = "SET_AUDIO_BYTES_PER_ELEMENT";
    public static final String SET_VIDEO_QUALITY = "SET_VIDEO_QUALITY";



    /** Configuration variables **/
    public static final String ENDPOINT_PERSONALIZATION_NELL = "ENDPOINT_PERSONALIZATION_NELL";
    public static final String ENDPOINT_FEEDBACK_LEARNING_FROM_NELL = "ENDPOINT_FEEDBACK_LEARNING_FROM_NELL";
    public static final String ENDPOINT_PERSONALIZATION_MULTIBANDIT_LEARNING = "ENDPOINT_PERSONALIZATION_MULTIBANDIT_LEARNING";
    public static final String ENDPOINT_PERSONALIZATION_LEARNING_FROM_ADVISE = "ENDPOINT_PERSONALIZATION_LEARNING_FROM_ADVISE";
    public static final String ENDPOINT_FEEDBACK_MULTIBANDIT_LEARNING = "ENDPOINT_FEEDBACK_MULTIBANDIT_LEARNING";
    public static final String ENDPOINT_FEEDBACK_LEARNING_FROM_ADVISE = "ENDPOINT_FEEDBACK_LEARNING_FROM_ADVISE";
    public static final String ENDPOINT_NEWS_FILTERED_BY_EMAIL = "ENDPOINT_NEWS_FILTERED_BY_EMAIL";
    public static final String CONFIG_PROPERTIES = "config.properties";
    public static final String MAPPINGS_PROPERTIES = "mappings.properties";
    public static final String CONFIG_ID_PERSONALIZATION = "CONFIG_ID_PERSONALIZATION";
    public static final String CONFIG_ID_FEEDBACK = "CONFIG_ID_FEEDBACK";
    public static final String CONFIG_NEWS_RANKING_OPTION = "CONFIG_NEWS_RANKING_OPTION";
    public static final String ENDPOINT_NEWS_MODELS_DISTRIBUTION = "ENDPOINT_NEWS_MODELS_DISTRIBUTION";
    public static final String ENDPOINT_NEIL_SERVER = "ENDPOINT_NEIL_SERVER";
    public static final String ENDPOINT_NELL_MACROREADER = "ENDPOINT_NELL_MACROREADER";
    public static final String ENDPOINT_NELL_MICROREADER = "ENDPOINT_NELL_MICROREADER";
    public static final String ENDPOINT_TASK_UNDERSTANDING_SERVER = "ENDPOINT_TASK_UNDERSTANDING_SERVER";
    public static final String ENDPOINT_QA_SERVER = "ENDPOINT_QA_SERVER";
    public static final String ENDPOINT_EMAIL_UNDERSTANDING_SERVER = "ENDPOINT_EMAIL_UNDERSTANDING_SERVER";
    public static final String ENDPOINT_EMAIL_SUMMARIZATION_SERVER = "ENDPOINT_EMAIL_SUMMARIZATION_SERVER";
    public static final String ENDPOINT_AWS_STREAMING = "ENDPOINT_AWS_STREAMING";
    public static final String ENDPOINT_MULTISENSE = "ENDPOINT_MULTISENSE";
    public static String ENDPOINT_SARA = "ENDPOINT_SARA"; // no final since it can be modified by user



    /** HTTP **/
    public static final String HTTP_REQUEST_SERVER_URL = "HTTP_REQUEST_SERVER_URL";
    public static final String HTTP_REQUEST_BODY = "HTTP_REQUEST_BODY";
    public static final String HTTP_MIME_TYPE = "HTTP_MIME_TYPE";
    public static final String HTTP_RESOURCE_NAME = "HTTP_RESOURCE_NAME";
    public static final String HTTP_FILE_EXTENSION = "HTTP_FILE_EXTENSION";
    public static final String IMG_YUV_FORMAT = "IMG_YUV_FORMAT";
    public static final String IMG_QUALITY = "IMG_QUALITY";
    public static final String IMG_COMPRESS_FORMAT = "IMG_COMPRESS_FORMAT";
    public static final String IMG_HEIGHT = "IMG_HEIGHT";
    public static final String IMG_WITH = "IMG_WITH";

    /** Multiuser Framework **/
    public static final String MULTIUSER = "MULTIUSER";
    public static final String MULTIUSER_IP_ADDRESS = "MULTIUSER_IP_ADDRESS";
    public static final String MULTIUSER_LISTENER = "MULTIUSER_LISTENER";
    public static final String MULTIUSER_SEND_MESSAGE = "MULTIUSER_SEND_MESSAGE";
    public static final String MULTIUSER_STARTED = "MULTIUSER_STARTED";


    /** GOOGLE SERVICES **/
    public static final String GOOGLE_PLAY = "GOOGLE_PLAY";
    public static final String GOOGLE_ACCOUNT = "GOOGLE_ACCOUNT";
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;



    /** CALENDAR CONSTANTS **/
    public static final String CALENDAR = "CALENDAR";
    public static final String CALENDAR_AFTER_CREATE_EVENT = "CALENDAR_AFTER_CREATE_EVENT";
    public static final String CALENDAR_AFTER_UPDATE_EVENT = "CALENDAR_AFTER_UPDATE_EVENT";
    public static final String CALENDAR_AFTER_DELETE_EVENTS = "CALENDAR_AFTER_DELETE_EVENTS";
    public static final String CALENDAR_AFTER_DELETE_ALL_EVENTS = "CALENDAR_AFTER_DELETE_ALL_EVENTS";
    public static final String CALENDAR_AFTER_QUERY_EVENTS = "CALENDAR_AFTER_QUERY_EVENTS";
    public static final String CALENDAR_AVAILABILITY_EXCEPTION = "CALENDAR_AVAILABILITY_EXCEPTION";
    public static final String CALENDAR_USER_RECOVERABLE_EXCEPTION = "CALENDAR_USER_RECOVERABLE_EXCEPTION";
    public static final String CALENDAR_MODE = "CALENDAR_MODE";
    public static final String CALENDAR_EVENT_DATA = "CALENDAR_EVENT_DATA";
    public static final String CALENDAR_CHECK_REFRESH_CALENDAR = "CALENDAR_CHECK_REFRESH_CALENDAR";
    public static final String CALENDAR_FOR_SPECIFIC_DATE = "CALENDAR_FOR_SPECIFIC_DATE";
    public static final String CALENDAR_NUMBER_OF_MONTHS = "CALENDAR_NUMBER_OF_MONTHS";
    public static final String CALENDAR_START_DATE = "CALENDAR_START_DATE";
    public static final String CALENDAR_END_DATE = "CALENDAR_END_DATE";
    public static final String CALENDAR_START_TIME = "CALENDAR_START_TIME";
    public static final String CALENDAR_END_TIME = "CALENDAR_END_TIME";
    public static final String CALENDAR_SYNC_TIME = "CALENDAR_SYNC_TIME";
    public static final String CALENDAR_MBREQUEST_ID="CALENDAR_MBREQUEST_ID";
    public static final int CALENDAR_GET_EVENTS = 0;
    public static final int CALENDAR_INSERT_EVENT = 1;
    public static final int CALENDAR_DELETE_EVENTS = 2;
    public static final int CALENDAR_UPDATE_EVENT = 3;
    public static final int CALENDAR_DELETE_ALL_EVENTS = 4;


    /** CLOCK **/
    public static final String CLOCK_NOW = "CLOCK_NOW";
    public static final String CLOCK_TODAY = "CLOCK_TODAY";
    public static final String CLOCK_TOMORROW = "CLOCK_TOMORROW";
    public static final String CLOCK_YESTERDAY = "CLOCK_YESTERDAY";
    public static final String CLOCK_MONDAYS = "CLOCK_MONDAY";
    public static final String CLOCK_TUESDAYS = "CLOCK_TUESDAY";
    public static final String CLOCK_WEDNESDAYS = "CLOCK_WEDNESDAY";
    public static final String CLOCK_THURSDAYS = "CLOCK_THURSDAY";
    public static final String CLOCK_FRIDAYS = "CLOCK_FRIDAY";
    public static final String CLOCK_SATURDAYS = "CLOCK_SATURDAY";
    public static final String CLOCK_SUNDAYS = "CLOCK_SUNDAY";



    /** SERVICES **/
    public static final String SERVICE_SETTINGS = "SERVICE_SETTINGS";
    public static final String SERVICE_NAME = "SERVICE_NAME";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String SERVICE_LOCATION = "Location";
    public static final String SERVICE_AR = "SERVICE_AR";

    /** SENSORS **/
    public static final String SENSOR_SETTING = "SENSOR_SETTING";
    public static final String SENSOR_NAME = "SENSOR_NAME";
    public static final String SENSOR_STOP = "SENSOR_STOP";
    public static final String SENSOR_START = "SENSOR_START";
    public static final String SENSOR_ACCELEROMETER = "SENSOR_ACCELEROMETER";
    public static final String SENSOR_SMS = "SENSOR_SMS";

    /** EFFECTORS **/
    public static final String EFFECTOR_DATA = "EFFECTOR_DATA";



    /** LOCATION **/
    public static final String LOCATION = "LOCATION";
    public static final String LOCATION_LONGITUDE = "LOCATION_LONGITUDE";
    public static final String LOCATION_LATITUDE = "LOCATION_LATITUDE";
    public static final String LOCATION_COUNTRY = "LOCATION_COUNTRY";
    public static final String LOCATION_CITY = "LOCATION_CITY";
    public static final String LOCATION_COUNTRY_CODE = "LOCATION_COUNTRY_CODE";
    public static final String LOCATION_STATE_CODE = "LOCATION_STATE_CODE";
    public static final String LOCATION_SUB_AREA = "LOCATION_SUB_AREA";
    public static final String LOCATION_CURRENT_PLACE = "LOCATION_CURRENT_PLACE";
    public static final String LOCATION_WRITE_READ_DATA_FROM_FILE = "LOCATION_WRITE_READ_DATA_FROM_FILE";
    public static final String LOCATION_PLACE_NAME = "LOCATION_PLACE_NAME";
    public static final String LOCATION_HISTORY = "LOCATION_HISTORY";
    public static final String LOCATION_MAX_HISTORY = "LOCATION_MAX_HISTORY";


    /** HOTEL **/
    public static final String HOTEL = "HOTEL";
    public static final String HOTEL_URL = "HOTEL_URL";
    public static final String HOTEL_CRITERIA = "HOTEL_CRITERIA";


    /** GOOGLEAPIS **/
    public static final String GOOGLE_CONNECTION_STATUS = "GOOGLE_CONNECTION_STATUS";

    /** COMMON **/
    public static final String APP_CONTEXT = "APP_CONTEXT";
    public static final String RESULTS_LOGIN = "RESULTS_LOGIN";

    /** TOAST **/
    public static final String TOAST = "TOAST";
    public static final String TOAST_MESSAGE = "TOAST_MESSAGE";

    /** WEATHER **/
    public static final String WEATHER = "WEATHER";
    public static final String WEATHER_MODE = "WEATHER_MODE";
    public static final String WEATHER_PLACE = "WEATHER_PLACE";
    public static final String WEATHER_FORCE_REFRESH = "WEATHER_FORCE_REFRESH";
    public static final String WEATHER_REFRESH_TIME = "WEATHER_REFRESH_TIME";
    public static final String WEATHER_CONDITION = "WEATHER_CONDITION";
    public static final String WEATHER_FEELS_LIKE_ENG = "WEATHER_FEELS_LIKE_ENG";
    public static final String WEATHER_FEELS_LIKE_METRIC = "WEATHER_FEELS_LIKE_METRIC";
    public static final String WEATHER_HIGH_TEMP_ENG = "WEATHER_HIGH_TEMP_ENG";
    public static final String WEATHER_HIGH_TEMP_METRIC = "WEATHER_HIGH_TEMP_METRIC";
    public static final String WEATHER_LOW_TEMP_ENG = "WEATHER_LOW_TEMP_ENG";
    public static final String WEATHER_LOW_TEMP_METRIC = "WEATHER_LOW_TEMP_METRIC";
    public static final String WEATHER_HOUR = "WEATHER_HOUR";
    public static final String WEATHER_HUMIDITY = "WEATHER_HUMIDITY";
    public static final String WEATHER_DATE = "WEATHER_DATE";
    public static final String WEATHER_DAY = "WEATHER_DAY";
    public static final String WEATHER_MONTH = "WEATHER_MONTH";
    public static final String WEATHER_YEAR = "WEATHER_YEAR";
    public static final String WEATHER_SNOW_ENG = "WEATHER_SNOW_ENG";
    public static final String WEATHER_SNOW_METRIC = "WEATHER_SNOW_METRIC";
    public static final String WEATHER_DAY_OF_WEEK = "WEATHER_DAY_OF_WEEK";
    public static final String WEATHER_CONDITION_RULES = "WEATHER_CONDITION_RULES";
    public static final int WEATHER_FORECAST_DAILY = 0;
    public static final int WEATHER_FORECAST_HOURLY = 1;

    /** ACTIVITY RECOGNITION **/
    public static final String ACTIVITY_RECOGNITION = "ACTIVITY_RECOGNITION";
    public static final String AR_ACTIVITY_TYPE = "AR_ACTIVITY_TYPE";

    /** ALARM **/
    public static final String ALARM = "ALARM";
    public static final String ALARM_RELATIVE_TIME = "ALARM_RELATIVE_TIME";
    public static final String ALARM_CONDITION_AFTER = "ALARM_CONDITION_AFTER";
    public static final String ALARM_REFERENCE_TIME = "ALARM_REFERENCE_TIME";
    public static final String ALARM_CONDITION_BEFORE = "ALARM_CONDITION_BEFORE";
    public static final String ALARM_ABSOLUTE_TIME = "ALARM_ABSOLUTE_TIME";
    public static final String ALARM_CONDITION_AT = "ALARM_CONDITION_AT";
    public static final String ALARM_RINGTONE_TYPE = "ALARM_RINGTONE_TYPE";
    public static final String ALARM_TIME_NOW = "ALARM_TIME_NOW";

    /** PHONE CALL **/
    public static final String PHONECALL_START_TIME = "PHONECALL_START_TIME";
    public static final String PHONECALL = "PHONECALL";

    /** SMS **/
    public static final String SMS = "SMS";


    /** RULES **/
    public static final String DECISION_RULE = "DECISION_RULE";
    public static final String DECISION_RULE_ELEMENT = "DECISION_RULE_ELEMENT";
    public static final String DECISION_RULE_JSON = "DECISION_RULE_JSON";
    public static final String DECISION_RULE_ID = "DECISION_RULE_ID";
    public static final String DECISION_RULE_TERM = "DECISION_RULE_TERM";
    public static final String DECISION_RULE_CONDITION = "DECISION_RULE_CONDITION";

    /** PROPOSITIONS **/
    public static final String PROPOSITION_TYPE = "PROPOSITION_TYPE";
    public static final String PROPOSITION_ATTRIBUTE = "PROPOSITION_ATTRIBUTE";
    public static final String PROPOSITION_OPERATOR = "PROPOSITION_OPERATOR";
    public static final String PROPOSITION_VALUE = "PROPOSITION_VALUE";
    public static final String PROPOSITION_REF_ATTRIBUTE = "PROPOSITION_REF_ATTRIBUTE";


    /** ACTIONS **/
    public static final String ACTION_TYPE = "ACTION_TYPE";
    public static final String ACTION_SET_ALARM = "ACTION_SET_ALARM";
    public static final String ACTION_SEND_SMS = "ACTION_SEND_SMS";
    public static final String ACTION_GOOGLE_ACTIVITY_RECOGNITION = "ACTION_GOOGLE_ACTIVITY_RECOGNITION";
    public static final String ACTION_HOTEL_RESERVATION = "ACTION_HOTEL_RESERVATION";
    public static final String ACTION_CALENDAR = "ACTION_CALENDAR";
    public static final String ACTION_WEATHER = "ACTION_WEATHER";
    public static final String ACTION_OAQA = "ACTION_OAQA";
    public static final String ACTION_HELPR = "ACTION_HELPR";
    public static final String ACTION_NEIL = "ACTION_NEIL";
    public static final String ACTION_NELL = "ACTION_NELL";
    public static final String ACTION_EMAIL_UNDERSTANDING = "ACTION_EMAIL_UNDERSTANDING";
    public static final String ACTION_APP_TRACKER = "ACTION_APP_TRACKER";
    public static final String ACTION_SUGILITE = "ACTION_SUGILITE";
    public static final String ACTION_EXTERNAL_COMMUNICATION = "ACTION_EXTERNAL_COMMUNICATION";
    public static final String ACTION_RED5STREAMING = "ACTION_RED5STREAMING";
    public static final String ACTION_DIALOGUE = "ACTION_DIALOGUE";
    public static final String ACTION_RAPPORT = "ACTION_RAPPORT";
    public static final String ACTION_MOVIERECOMMENDATION="ACTION_MOVIERECOMMENDATION";
    public static final String ACTION_CHERISHEDMEMORIES="ACTION_CHERISHEDMEMORIES";
    public static final String ACTION_MULTISENSE = "ACTION_MULTISENSE";
    public static final String ACTION_GEOTAGGEDPHOTO="ACTION_GEOTAGGEDPHOTO";
    public static final String ACTION_GEOTAGGEDSURVEY="ACTION_GEOTAGGEDSURVEY";
    public static final String ACTION_CHORUS="ACTION_CHORUS";
    public static final String ACTION_CAPTION_GENERATION="ACTION_CAPTION_GENERATION";
    public static final String ACTION_NLG = "ACTION_NLG";
    public static final String ACTION_MULTIUSER = "ACTION_MULTIUSER";
    public static final String ACTION_GOOGLE_SPEECH_RECOGNITION="ACTION_GOOGLE_SPEECH_RECOGNITION";


    /** STREAMING **/
    public static final String STREAMING = "STREAMING";
    public final static String STREAMING_ERROR_CAMERA_ALREADY_IN_USE = "STREAMING_ERROR_CAMERA_ALREADY_IN_USE";
    public final static String STREAMING_ERROR_CONFIGURATION_NOT_SUPPORTED = "STREAMING_ERROR_CONFIGURATION_NOT_SUPPORTED";
    public final static String STREAMING_ERROR_STORAGE_NOT_READY = "STREAMING_ERROR_STORAGE_NOT_READY";
    public final static String STREAMING_ERROR_CAMERA_HAS_NO_FLASH = "STREAMING_ERROR_CAMERA_HAS_NO_FLASH";
    public final static String STREAMING_ERROR_INVALID_SURFACE = "STREAMING_ERROR_INVALID_SURFACE";
    public final static String STREAMING_ERROR_UNKNOWN_HOST = "STREAMING_ERROR_UNKNOWN_HOST";
    public final static String STREAMING_ERROR_OTHER = "STREAMING_ERROR_OTHER";
    public static final String STREAMING_SURFACE_DESTROYED = "STREAMING_SURFACE_DESTROYED";
    public static final String STREAMING_SURFACE_CREATED = "STREAMING_SURFACE_CREATED";
    public static final String STREAMING_SURFACE_CHANGED = "STREAMING_SURFACE_CHANGED";
    public static final String STREAMING_SESSION_STOPPED = "STREAMING_SESSION_STOPPED";
    public static final String STREAMING_SESSION_STARTED = "STREAMING_SESSION_STARTED";
    public static final String STREAMING_PREVIEW_STARTED = "STREAMING_PREVIEW_STARTED";
    public static final String STREAMING_BITRATE_UPDATE = "STREAMING_BITRATE_UPDATE";
    public static final String STREAMING_ERROR_RTSP_CONNECTION_FAILED = "STREAMING_ERROR_RTSP_CONNECTION_FAILED";
    public static final String STREAMING_ERROR_RSTP_WRONG_CREDENTIALS = "STREAMING_ERROR_RSTP_WRONG_CREDENTIALS";
    public static final String SET_TOGGLE_STREAM = "SET_TOGGLE_STREAM";
    public static final String SET_STREAMING_TOGGLE_FLASH = "SET_STREAMING_TOGGLE_FLASH";
    public static final String SET_STREAMING_SWITCH_CAMERA = "SET_STREAMING_SWITCH_CAMERA";
    public static final String STREAMING_SUBSCRIBER = "STREAMING_SUBSCRIBER";
    public static final String STREAMING_TYPE = "STREAMING_TYPE";
    public static final String STREAMING_BOTH = "STREAMING_BOTH";
    public static final String STREAMING_VIDEO = "STREAMING_VIDEO";
    public static final String STREAMING_AUDIO = "STREAMING_AUDIO";
    public static final String STREAMING_SUBCRIPTION_CONFIG = "STREAMING_SUBCRIPTION_CONFIG";
    public final static String STREAMING_VIDEO_ENCODER_NONE = "STREAMING_VIDEO_ENCODER_NONE";
    public final static String STREAMING_VIDEO_ENCODER_H264 = "STREAMING_VIDEO_ENCODER_H264";
    public final static String STREAMING_VIDEO_ENCODER_H263 = "STREAMING_VIDEO_ENCODER_H263";
    public final static String STREAMING_AUDIO_ENCODER_NONE = "STREAMING_AUDIO_ENCODER_NONE";
    public final static String STREAMING_AUDIO_ENCODER_AMRNB = "STREAMING_AUDIO_ENCODER_AMRNB";
    public final static String STREAMING_AUDIO_ENCODER_AAC = "STREAMING_AUDIO_ENCODER_AAC";
    public static final String STREAMING_USERNAME = "STREAMING_USERNAME";
    public static final String STREAMING_PASSWORD = "STREAMING_PASSWORD";
    public static final String STREAMING_DESTINATION = "STREAMING_DESTINATION";
    public static final String STREAMING_SESSION_CONFIGURED = "STREAMING_SESSION_CONFIGURED";

    /** ZMQ **/
    public static final String REQUEST_CONNECT = "REQUEST_CONNECT";
    public static final String REQUEST_DISCONNECT = "REQUEST_DISCONNECT";
    public static final String RESPONSE_ALREADY_CONNECTED = "RESPONSE_ALREADY_CONNECTED";
    public static final String RESPONSE_UNKNOWN_SESSION = "RESPONSE_UNKNOWN_SESSION";
    public static final String RESPONSE_NOT_VALID_OPERATION = "RESPONSE_NOT_VALID_OPERATION";
    public static final String REQUEST_PAUSE = "REQUEST_PAUSE";
    public static final String REQUEST_RESUME = "REQUEST_RESUME";
    public static final String SHUTDOWN = "shutdown";
    public static final String SESSION_INITIATED = "SESSION_INITIATED";
    public static final String SESSION_CLOSED = "SESSION_CLOSED";
    public static final String SESSION_PAUSED = "SESSION_PAUSED";
    public static final String SESSION_RESUMED = "SESSION_RESUMED";
    //public static final int DEFAULT_PORT = 5555;

    //communication
    public static final int CONNECTION_NEW = 0;
    public static final int CONNECTION_STARTED = 1;
    public static final int CONNECTION_FINISHED = 2;
    public static final int CONNECTION_STOPPED = 3;
    public static final int DEFAULT_PORT = 5555;

    /** CHORUS SERVICE **/
    public static final String ENDPOINT_CHORUS_SERVER="ENDPOINT_CHORUS_SERVER";
    public static final String CHORUS_USERNAME="CHORUS_USERNAME";
    public static final String CHORUS_PASSWORD="CHORUS_PASSWORD";
    public static final String CHORUS_BEGIN="CHORUS_BEGIN";
    public static final String CHORUS_END="CHORUS_END";
    public static final String CHORUS_QUERY="CHORUS_PASSWORD";
    public static final String CHORUS_SEND="CHORUS_PASSWORD";
    public static final String CHORUS_RECEIVE="CHORUS_PASSWORD";
    public static final String CHORUS_USER_CHAT_VO="CHORUS_USER_CHAT_VO";
    public static final String CHORUS_AUTH_VO="CHORUS_AUTH_VO";
    public static final String CHORUS="CHORUS";
    public static final String CHORUS_CHAT_LINE="CHORUS_CHAT_LINE";
    public static final String CHORUS_IS_SAVE="CHORUS_IS_SAVE";
    public static final String CHORUS_FINISHED ="finished";
    public static final String CHORUS_ACTIVATED="activated";
    public static final String CHORUS_SEND_CHAT_FLAG="CHORUS_SEND_CHAT_FLAG";
    public static final String CHORUS_CHAT_ENDED_BY_CHORUS="CHORUS_CHAT_ENDED_BY_CHORUS";
    public static final String CHORUS_CHAT_RESPONSE_TIMEOUT="CHORUS_CHAT_RESPONSE_TIMEOUT";
    public static final String CHORUS_NOT_ACTIVATED="not-activated";
    public static final String COUNTDOWN_TIMER_STARTED="COUNTDOWN_TIMER_STARTED";
    public static final String TIMER_COUNTDOWN_COMPLETED="TIMER_COUNTDOWN_COMPLETED";



    /** RED5STREAMING **/
    public static final String RED5STREAMING = "RED5STREAMING";
    public static final String RED5STREAMING_START_STREAMING = "STREAMING_START_STREAMING";
    public static final String RED5STREAMING_STOP_STREAMING = "STREAMING_STOP_STREAMING";
    public static final String RED5STREAMING_SURFACEVIEW = "RED5STREAMING_SURFACEVIEW";
    public static final String RED5STREAMING_ACTIVITY = "RED5STREAMING_ACTIVITY";
    public static final String RED5STREAMING_FLAG="RED5STREAMING_FLAG";
    public static final String RED5STREAMING_CAMERA = "RED5STREAMING_CAMERA";
    public static final String RED5STREAMING_CONFIG = "RED5STREAMING_CONFIG";
    public static final String RED5STREAMING_STREAM = "RED5STREAMING_STREAM";
    public static final String STREAMING_PORT = "STREAMING_PORT";
    public static final String RED5STREAMING_SERVER_URL="RED5STREAMING_SERVER_URL";
    public static final String RED5_STREAMING_STATUS_CONNECTED="CONNECTED";
    public static final String RED5_STREAMING_STATUS_DISCONNECTED="DISCONNECTED";
    public static final String RED5_STREAMING_STATUS_START_STREAMING="START_STREAMING";
    public static final String RED5_STREAMING_STATUS_STOP_STREAMING="STOP_STREAMING";
    public static final String RED5_STREAMING_STATUS_CLOSE="CLOSE";
    public static final String RED5_STREAMING_STATUS_TIMEOUT="TIMEOUT";
    public static final String RED5_STREAMING_STATUS_ERROR="ERROR";
    public static final String RED5_STREAMING_STATUS_READY="RED5_STREAMING_STATUS_READY";

    /** ACCELEROMETER **/
    public static final String ACCELEROMETER_FREQUENCY = "ACCELEROMETER_FREQUENCY";
    public static final String ACCELEROMETER_X_AXIS = "ACCELEROMETER_X_AXIS";
    public static final String ACCELEROMETER_Y_AXIS = "ACCELEROMETER_Y_AXIS";
    public static final String ACCELEROMETER_Z_AXIS = "ACCELEROMETER_Z_AXIS";
    public static final String ACCELEROMETER_VECTOR_SUM = "ACCELEROMETER_VECTOR_SUM";
    public static final String ACCELEROMETER_ACCURACY = "ACCELEROMETER_ACCURACY";
    public static final String ACCELEROMETER_FREE_FALL = "ACCELEROMETER_FREE_FALL";
    public static final String ACCELEROMETER = "ACCELEROMETER";


    /** SLINGSTONE **/
    public static final String SLINGSTONE_SS_URL = "ENDPOINT_SLINGSTONE_SS";
    public static final String SLINGSTONE_I13N_URL = "ENDPOINT_SLINGSTONE_I13N";
    public static final String SLINGSTONE_LOGIN_URL = "ENDPOINT_SLINGSTONE_LOGIN";
    public static final String SLINGSTONE_UUIDTRACKER_URL = "ENDPOINT_SLINGSTONE_UUIDTRACKER";

    /** OAQA **/
    public static final String OAQA = "OAQA";
    public static final String OAQA_ANSWER_RESULT = "OAQA_ANSWER_RESULT";
    public static final String OAQA_QUESTION = "OAQA_QUESTION";

    /** NEIL **/
    public static final String NEIL = "NEIL";
    public static final String NEIL_IMG_URL = "NEIL_IMG_URL";


    /** EMAIL UNDERSTANDING **/
    public static final String EMAIL = "EMAIL";
    public static final String EMAIL_UNDERSTANDING_EXTRACT_FEATURES = "EMAIL_UNDERSTANDING_EXTRACT_FEATURES";
    public static final String EMAIL_UNDERSTANDING_SUMMARIZATION = "EMAIL_UNDERSTANDING_SUMMARIZATION";
    public static final String EMAIL_REQUEST_EXTRACT = "EMAIL_REQUEST_EXTRACT";

    /** DIALOGUE **/
    public static final int KEYWD_DETECTED = 0;
    public static final int ASR_TIME_OUT = 1;
    public static final int ASR_OUTPUT = 2;
    public static final int TTS_COMPLETE = 3;
    public static final String PREFERENCE_NAME = "adroitness_preference";
    public static final String LOGINED_FLAG = "logined";
    public static final String USERNAME_FLAG = "username";
    public static final String HOST_FLAG = "host";
    public static final String PWD_FLAG = "pwd";
    public static final int RECEIVE_IMAP_LOGIN = 1003;
    public static final String START_KEY = "in mind agent";
    public static final String TERMINATE_WORD = "terminate";
    public static final String REPLY_EMAIL = "reply email";
    public static final String DIALOGUE_MESSAGE = "DIALOGUE_MESSAGE";
    public static final String DIALOGUE_HANDLER = "DIALOGUE_HANDLER";
    public static final String DIALOGUE_CONTEXT = "DIALOGUE_CONTEXT";
    public static final String DIALOGUE_ACTIVITY = "DIALOGUE_ACTIVITY";
    public static final String IMAP_VO = "IMAP_VO";
    public static final String DIALOGUE = "DIALOGUE";

    /** IMAP MANAGER **/
    public static final String IMAP_MESSAGE = "IMAP_MESSAGE";
    public static final String RAW_CONTENT = "RAW_CONTENT";
    public static final String IMAP_MESSAGE_ORDER = "IMAP_MESSAGE_ORDER";
    public static final String IMAP_QUERY = "IMAP_QUERY";
    public static final String IMAP_MANAGER="IMAP_MANAGER";

    /** GMAIL MANAGER **/
    public static final String GMAIL_MESSAGE = "GMAIL_MESSAGE";
    public static final String GMAIL_MESSAGE_ID = "GMAIL_MESSAGE_ID";
    public static final String GMAIL_LABEL="GMAIL_LABEL";
    public static final String GMAIL_SAVE_TO_PHONE_FLAG = "GMAIL_SAVE_TO_PHONE_FLAG";
    public static final String GMAIL_USER_ID="GMAIL_USER_ID";
    public static final String GMAIL_FILTER_QUERY_INPUT_VO = "GMAIL_GMAIL_FILTER_QUERY_INPUT_VO";
    public static final String GMAIL_RESPONSE = "GMAIL_RESPONSE";
    public static final String GMAILMANAGER="GMAILMANAGER";
    public static final String GMAIL_MESSAGE_VO="GMAIL_MESSAGE_VO";
    public static final String GMAIL_LISTVIEW_FLAG="GMAIL_LISTVIEW_FLAG";
    public static final String GMAIL_TO_EMAILID="GMAIL_TO_EMAILID";
    public static final String GMAIL_FROM_EMAILID="GMAIL_FROM_EMAILID";
    public static final String GMAIL_SUBJECT="GMAIL_SUBJECT";
    public static final String GMAIL_BODY_TEXT="GMAIL_BODY_TEXT";
    public static final String GMAIL_FILE_ATTACHMENT="GMAIL_FILE_ATTACHMENT";
    public static final String GMAIL_REPLY_ALL_FLAG="GMAIL_REPLY_ALL_FLAG";
    public static final String GMAIL_ACTIVITY_REQUEST_FLAG="GMAIL_ACTIVITY_REQUEST_FLAG";
    public static final String GMAIL_ADD_RECIPIENTS="GMAIL_ADD_RECIPIENTS";

    public static final String GMAIL_AFTER_SEND_EMAIL = "GMAIL_AFTER_SEND_NEW_EMAIL";
    public static final String GMAIL_AFTER_REPLY_EMAIL = "GMAIL_AFTER_REPLY_EMAIL";
    public static final String GMAIL_AFTER_FORWARD_EMAIL = "GMAIL_AFTER_FORWARD_EMAIL";
    public static final String GMAIL_GET_MESSAGE_CONTENT = "GMAIL_GET_MESSAGE_CONTENT";
    public static final String GMAIL_GET_NEW_EMAILS = "GMAIL_GET_NEW_EMAILS";
    public static final String GMAIL_GET_ALL_EMAILS = "GMAIL_GET_ALL_EMAILS";
    public static final String GMAIL_GET_MESSAGE_VO = "GMAIL_GET_MESSAGE_VO";
    public static final String GMAIL_GET_ATTACHMENTS = "GMAIL_GET_ATTACHMENTS";
    public static final String GMAIL_AFTER_FILTER_QUERY ="GMAIL_AFTER_FILTER_QUERY";
    public static final String GMAIL_AFTER_GET_LABEL ="GMAIL_AFTER_GET_LABEL";
    public static final String GMAIL_AFTER_UPDATE_LABEL_LIST ="GMAIL_AFTER_UPDATE_LABEL_LIST";
    public static final String GMAIL_AFTER_GET_UNREAD ="GMAIL_AFTER_GET_UNREAD";
    public static final String GMAIL_AFTER_USER_PROFILE="GMAIL_AFTER_USER_PROFILE";
    public static final String GMAIL_AFTER_GET_ATTACHMENTS="GMAIL_AFTER_GET_ATTACHMENTS";

    /** APP TRACKER AND SUGILITE **/
    public static final String SUGILITE = "SUGILITE";
    public static final String APP_TRACKER = "APP_TRACKER";
    public static final String ACCESSIBILITY_SERVICE_CLASS = "ACCESSIBILITY_SERVICE_CLASS";
    public static final String SCRIPT_NAME = "SCRIPT_NAME";
    public static final String CALLBACK_STRING = "CALLBACK_STRING";
    public static final String SCRIPT = "SCRIPT";
    public static final String PACKAGE_NAME = "PACKAGE_NAME";
    public static final String JSON_STRING = "JSON_STRING";
    public static final String SHOULD_SEND_CALLBACK ="SHOULD_SEND_CALLBACK";
    public static final String FLAG_RETURN_SCRIPT = "FLAG_RETURN_SCRIPT";
    public static final int REGISTER = 1;
    public static final int UNREGISTER = 2;
    public static final int RESPONSE = 3;
    public static final int START_TRACKING = 4;
    public static final int STOP_TRACKING = 5;
    public static final int GET_ALL_TRACKING_SCRIPTS = 6;
    public static final int GET_TRACKING_SCRIPT = 7;
    public static final int APP_TRACKER_EXCEPTION = 8;
    public static final int RUN = 9;
    public static final int RESPONSE_EXCEPTION = 10;
    public static final int START_RECORDING = 11;
    public static final int STOP_RECORDING = 12;
    public static final int GET_ALL_RECORDING_SCRIPTS = 13;
    public static final int GET_RECORDING_SCRIPT = 14;
    public static final int ACCESSIBILITY_EVENT = 15;
    public static final int RUN_SCRIPT = 16;
    public static final int END_RECORDING_EXCEPTION = 17;
    public static final int START_RECORDING_EXCEPTION = 18;
    public static final int FINISHED_RECORDING = 19;
    public static final int RUN_SCRIPT_EXCEPTION = 20;
    public static final int RUN_JSON = 21;
    public static final int RUN_JSON_EXCEPTION = 22;
    public static final int ADD_JSON_AS_SCRIPT = 23;
    public static final int ADD_JSON_AS_SCRIPT_EXCEPTION = 24;
    public static final int CLEAR_TRACKING_LIST = 25;
    public static final int GET_ALL_PACKAGE_VOCAB = 26;
    public static final int GET_PACKAGE_VOCAB = 27;
    public static final int MULTIPURPOSE_REQUEST = 28;


    /** AWARE **/
    public static final String AWARE = "AWARE";

    /** BATTERY **/
    public static final String BATTERY = "BATTERY";

    /** TASK UNDERSTANDING **/
    public static final String TASK_UNDERSTANDING = "TASK_UNDERSTANDING";

    /** ADD SERVICES **/
    public static final String ADD_SERVICE_PRIVACY = "ADD_SERVICE_PRIVACY";
    public static final String ADD_SERVICE_NEWS = "ADD_SERVICE_NEWS";
    public static final String ADD_SERVICE_ACTIVITY_RECOGNITION = "ADD_SERVICE_ACTIVITY_RECOGNITION";
    public static final String ADD_SERVICE_HOTEL_RESERVATION = "ADD_SERVICE_HOTEL_RESERVATION";
    public static final String ADD_SERVICE_LOCATION = "ADD_SERVICE_LOCATION";
    public static final String ADD_SERVICE_WEATHER = "ADD_SERVICE_WEATHER";
    public static final String ADD_SERVICE_CALENDAR = "ADD_SERVICE_CALENDAR";
    public static final String ADD_SERVICE_STREAMING = "ADD_SERVICE_STREAMING";
    public static final String ADD_SERVICE_TASK_UNDERSTANDING = "ADD_SERVICE_TASK_UNDERSTANDING";
    public static final String ADD_SERVICE_OAQA = "ADD_SERVICE_OAQA";
    public static final String ADD_SERVICE_AWARE = "ADD_SERVICE_AWARE";
    public static final String ADD_SERVICE_NEIL = "ADD_SERVICE_NEIL";
    public static final String ADD_SERVICE_EMAIL_UNDERSTANDING = "ADD_SERVICE_EMAIL_UNDERSTANDING";
    public static final String ADD_SERVICE_APP_TRACKER = "ADD_SERVICE_APP_TRACKER";
    public static final String ADD_SERVICE_EXTERNAL_COMMUNICATION = "ADD_SERVICE_EXTERNAL_COMMUNICATION";
    public static final String ADD_SERVICE_HELPR = "ADD_SERVICE_HELPR";
    public static final String ADD_SERVICE_SUGILITE = "ADD_SERVICE_SUGILITE";
    public static final String ADD_SERVICE_RED5STREAMING = "ADD_SERVICE_RED5STREAMING";
    public static final String ADD_SERVICE_DIALOGUE = "ADD_SERVICE_DIALOGUE";
    public static final String ADD_SERVICE_GMAIL_SERVICE = "ADD_SERVICE_GMAIL_SERVICE";
    public static final String ADD_SERVICE_IMAP_SERVICE = "ADD_SERVICE_IMAP_SERVICE";
    public static final String ADD_SERVICE_MOVIE_RECOMMENDATION = "ADD_SERVICE_MOVIE_RECOMMENDATION";
    public static final String ADD_SERVICE_RAPPORT = "ADD_SERVICE_RAPPORT";
    public static final String ADD_SERVICE_CHERISHED_MEMORIES = "ADD_SERVICE_CHERISHED_MEMORIES";
    public static final String ADD_SERVICE_NELL = "ADD_SERVICE_NELL";
    public static final String ADD_SERVICE_MULTISENSE = "ADD_SERVICE_MULTISENSE";
    public static final String ADD_SERVICE_GEOTAGGED_KEYWORDS = "ADD_SERVICE_GEOTAGGED_KEYWORDS";
    public static final String ADD_SERVICE_GEOTAGGED_SURVEY = "ADD_SERVICE_GEOTAGGED_SURVEY";
    public static final String ADD_SERVICE_CHORUS = "ADD_SERVICE_CHORUS";
    public static final String ADD_SERVICE_ASR_NLG = "ADD_SERVICE_ASR_NLG";
    public static final String ADD_SERVICE_CAPTION_GENERATION="ADD_SERVICE_CAPTION_GENERATION";
    public static final String ADD_SERVICE_MULTIUSER = "ADD_SERVICE_MULTIUSER";
    public static final String ADD_SERVICE_GOOGLE_SPEECH_RECOGNITION="ADD_SERVICE_GOOGLE_SPEECH_RECOGNITION";



    /** ID's for communication with external apps **/
    public static final int ID_APP_TRACKER = 1001;
    public static final int ID_HELPR = 1002;


    /** Movie Recommendation **/
    public static final String ENDPOINT_MOVIE_RERANKING_SERVER = "ENDPOINT_MOVIE_RERANKING_SERVER";
    public static final String ENDPOINT_MOVIE_FEEDBACK_SERVER = "ENDPOINT_MOVIE_FEEDBACK_SERVER";
    public static final String ENDPOINT_MOVIE_QUERY_SERVER = "ENDPOINT_MOVIE_QUERY_SERVER";
    public static final String MOVIERECOMMENDATION="MOVIERECOMMENDATION";
    public static final String MOVIE_RANKING_INPUT_VO ="MOVIE_RANKING_INPUT_VO";
    public static final String MOVIE_FEEDBACK_INPUT_VO ="MOVIE_FEEDBACK_INPUT_VO";
    public static final String MOVIE_QUERY_INPUT_VO="MOVIE_QUERY_INPUT_VO";

    /** Cherished Memories **/
    public static final String ENDPOINT_CM_GET_QUERY_SERVER = "ENDPOINT_CM_GET_QUERY_SERVER";
    public static final String ENDPOINT_CM_POST_QUERY_SERVER = "ENDPOINT_CM_POST_QUERY_SERVER";
    public static final String CM_QUERY_STRING="CM_QUERY_STRING";
    public static final String CM_REQUEST_TYPE="CM_REQUEST_TYPE";
    public static final String CHERISHEDMEMORIES="CHERISHEDMEMORIES";

    /*** ZMQ **/
    public static final String ZMQ_REQUEST_CONNECT = "REQUEST_CONNECT";
    public static final String ZMQ_REQUEST_DISCONNECT = "REQUEST_DISCONNECT";
    public static final String ZMQ_RESPONSE_ALREADY_CONNECTED = "RESPONSE_ALREADY_CONNECTED";
    public static final String ZMQ_RESPONSE_DISCONNECTED = "RESPONSE_DISCONNECTED";

    /*** RAPPORT **/
    public static final String RAPPORT = "RAPPORT";
    public static final String ENDPOINT_RAPPORT = "ENDPOINT_RAPPORT";
    public static final String RAPPORT_SERVER_IP = "RAPPORT_SERVER_IP";
    public static final String RAPPORT_SERVER_PORT = "RAPPORT_SERVER_PORT";

    /*** NELL **/
    public static final String NELL = "NELL";
    public static final String NELL_ENTITY = "NELL_ENTITY";
    public static final String NELL_DOCUMENT = "NELL_DOCUMENT";

    /** HELPR **/
    public static final String HELPR = "HELPR";
    public static final String USER_UTTERANCE = "USER_UTTERANCE";
    public static final int GET_APP_SCRIPT_RECOMMENDATION = 1;

    /*** COMMUNICATION **/
    public static final String SESSION_MANAGER_SERVICE = "session-manager";
    /*public static final String CONNECTION_NEW = "CONNECTION_NEW";
    public static final String CONNECTION_STARTED = "CONNECTION_STARTED";
    public static final String CONNECTION_FINISHED = "CONNECTION_FINISHED";
    public static final String CONNECTION_STOPPED = "CONNECTION_STOPPED";*/

    /*** MULTISENSE **/
    public static final String MULTISENSE = "MULTISENSE";

    /** Geotagged Photo Service **/
    public static final String ENDPOINT_GEOTAGGED_KEYWORDS_SERVER = "ENDPOINT_GEOTAGGED_KEYWORDS_SERVER";
    public static final String GEOTAGGED_KEYWORDS_CATEGORY="GEOTAGGED_PHOTO_CATEGORY";
    public static final String GEOTAGGED_KEYWORDS_LOCATION_TYPE="GEOTAGGED_LOCATION_TYPE";
    public static final String GEOTAGGEDKEYWORDS="GEOTAGGEDKEYWORDS";
    public static final String USE_CURRENT_LOCATION_DATA="USE_CURRENT_LOCATION_DATA";
    public static final String USE_PAST_LOCATION_DATA="USE_PAST_LOCATION_DATA";
    public static final String USE_PHOTO_METADATA = "USE_PHOTO_METADATA";

    /** Geotagged Survey Service **/
    public static final String GEOTAGGED_SURVEY_RESULT="GEOTAGGED_SURVEY_RESULT";
    public static final String GEOTAGGEDSURVEY="GEOTAGGEDSURVEY";

    /** Caption Generation Service **/
    public static final String CAPTION_GENERATION="CAPTION_GENERATION";
    public static final String ENDPOINT_CAPTION_GENERATION_SERVER="ENDPOINT_CAPTION_GENERATION_SERVER";
    public static final String CAPTION_GENERATION_IMAGE_URL="CAPTION_GENERATION_IMAGE_URL";
    public static final String CAPTION_GENERATION_MULTIPART_IMAGE="CAPTION_GENERATION_MULTIPART_IMAGE";
    public static final String IMAGE_URL="imageurl";
    public static final int CAPTION_GENERATION_SELECT_IMAGE=1;

    /** Google Speech recognition API Standard Constants: ***/
    public static final String GOOGLESPEECHRECOGNIZER="GOOGLESPEECHRECOGNIZER";
    public static final String GOOGLE_SPEECH_RECOGNITION="GOOGLE_SPEECH_RECOGNITION";
    public static final String MSG_GOOGLE_SPEECH_START_ASR="MSG_GOOGLE_SPEECH_START_ASR";
    public static final String MSG_GOOGLE_SPEECH_END_ASR="MSG_GOOGLE_SPEECH_END_ASR";
    public static final String GOOGLE_SPEECH_STOP_NOTIFICATION="GOOGLE_SPEECH_STOP_NOTIFICATION";
    public static final String GOOGLE_SPEECH_START_NOTIFICATION="GOOGLE_SPEECH_START_NOTIFICATION";
    public static final String ASR_TEXT = "ASR_TEXT";
    public static final String ASR_CONFIDENCE = "ASR_CONFIDENCE";
    public static final String ASR_NLG = "ASR_NLG";

    /** Performance Tets **/
    public static final String PERFORMANCE_TEST_VO = "PERFORMANCE_TEST_VO";
    public static final String PERFORMANCE_TEST_SHOULD_SIMULATE = "PERFORMANCE_TEST_SHOULD_SIMULATE";
    public static final String DUMMY_1 = "DUMMY_1";
    public static final String DUMMY_2 = "DUMMY_2";
    public static final String DUMMY_3 = "DUMMY_3";
    public static final String DUMMY_4 = "DUMMY_4";
    public static final String DUMMY_5 = "DUMMY_5";
    public static final String DUMMY_6 = "DUMMY_6";
    public static final String DUMMY_7 = "DUMMY_7";



    /** Gmail API Standard Constants: ***/
    public static String GMAIL_INVALID_FILTER="invalid";
    public static HashMap<Integer,String> STAR_COLORS= new HashMap<>();
    static {
            STAR_COLORS.put(0, "yellow-star");
            STAR_COLORS.put(1, "green-star");
            STAR_COLORS.put(2, "purple-star");
            STAR_COLORS.put(3, "orange-star");
            STAR_COLORS.put(4, "red-star");
            STAR_COLORS.put(5, "blue-star");
            STAR_COLORS.put(6, "red-bang");
            STAR_COLORS.put(7, "yellow-bang");
            STAR_COLORS.put(8, "green-check");
            STAR_COLORS.put(9, "blue-info");
            STAR_COLORS.put(10, "orange-guillemet");
            STAR_COLORS.put(11, "purple-question");
    }
    public static HashMap<Integer,String> GMAIL_FOLDERS= new HashMap<>();
    static {
        GMAIL_FOLDERS.put(0, "INBOX");
        GMAIL_FOLDERS.put(1, "SENT");
        GMAIL_FOLDERS.put(2, "DRAFTS");
        GMAIL_FOLDERS.put(3, "CHATS");
    }

    public static HashMap<Integer,String> GMAIL_DEFAULT_LABELS= new HashMap<>();
    static {
        GMAIL_DEFAULT_LABELS.put(0, "UNREAD");
        GMAIL_DEFAULT_LABELS.put(1, "READ");
        GMAIL_DEFAULT_LABELS.put(2, "SOCIAL");
        GMAIL_DEFAULT_LABELS.put(3, "PROMOTIONS");
        GMAIL_DEFAULT_LABELS.put(4, "FORUMS");
        GMAIL_DEFAULT_LABELS.put(5, "UPDATES");
        GMAIL_DEFAULT_LABELS.put(6, "SPAM");
        GMAIL_DEFAULT_LABELS.put(7, "TRASH");
        GMAIL_DEFAULT_LABELS.put(8, "IMPORTANT");
    }

    public enum TypeOfQuery{
        SEND, TIMER_QUERY, QUERY_CONV_ID, RECEIVE_ALL
    }
}