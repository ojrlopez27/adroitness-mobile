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
    public static final String MSG_EFFECTOR_SEND_SMS = "MSG_EFFECTOR_SEND_SMS";

    public static final String MSG_RED5_STREAMING_STOP = "MSG_RED5_STREAMING_STOP";
    public static final String MSG_RED5_STREAMING_START = "MSG_RED5_STREAMING_START";
    public static final String MSG_RED5_STREAMING_TOGGLE_CAMERA = "MSG_RED5_STREAMING_TOGGLE_CAMERA";
    public static final String MSG_RED5_STREAMING_GET_CAMERA_SIZES = "MSG_RED5_STREAMING_GET_CAMERA_SIZES";
    public static final String MSG_RED5_STREAMING_ATTACH_CAMERA_FRAGMENT="MSG_RED5_STREAMING_ATTACH_CAMERA_FRAGMENT";

    public static final String MSG_GMAIL_GET_MESSAGE="MSG_GMAIL_GET_MESSAGE";
    public static final String MSG_GMAIL_GET_LABEL="MSG_GMAIL_GET_LABEL";
    public static final String MSG_GMAIL_GET_USER_PROFILE="MSG_GMAIL_GET_USER_PROFILE";
    public static final String MSG_GMAIL_INITIALIZE="MSG_GMAIL_INITIALIZE";
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
    public static final String MSG_GMAIL_UNREAD_NUM = "MSG_GMAIL_UNREAD_NUM";
    public static final String MSG_GMAIL_GET_SENDER = "MSG_GMAIL_GET_SENDER";

    public static final String MSG_MULTIUSER_START = "MSG_MULTIUSER_START";
    public static final String MSG_MULTIUSER_STOP = "MSG_MULTIUSER_STOP";
    public static final String MSG_NELL_ANALIZE_ENTITY = "MSG_NELL_ANALIZE_ENTITY";
    public static final String MSG_NELL_ANALIZE_DOCUMENT = "MSG_NELL_ANALIZE_DOCUMENT";


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


    /** Bundle fields **/
    public static final String BUNDLE_ACTIVITY_NAME = "BUNDLE_ACTIVITY_NAME";
    public static final String BUNDLE_ARTICLE_ID = "BUNDLE_ARTICLE_ID";
    public static final String BUNDLE_MESSAGE_TYPE = "BUNDLE_MESSAGE_TYPE";


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


    /** Set constant values **/
    public static final String AUDIO = "AUDIO";
    public static final String SET_AUDIO_SAMPLE_RATE = "SET_AUDIO_SAMPLE_RATE";
    public static final String SET_AUDIO_CHANNEL_CONFIG = "SET_AUDIO_CHANNEL_CONFIG";
    public static final String SET_AUDIO_ENCODING = "SET_AUDIO_ENCODING";
    public static final String SET_SUBSCRIBER = "SET_SUBSCRIBER";
    public static final String SET_AUDIO_BUFFER_ELEMENTS_TO_REC = "SET_AUDIO_BUFFER_ELEMENTS_TO_REC";
    public static final String SET_AUDIO_BYTES_PER_ELEMENT = "SET_AUDIO_BYTES_PER_ELEMENT";



    /** Configuration variables **/
    public static final String CONFIG_PROPERTIES = "config.properties";
    public static final String MAPPINGS_PROPERTIES = "mappings.properties";
    public static final String ENDPOINT_NELL_MACROREADER = "ENDPOINT_NELL_MACROREADER";
    public static final String ENDPOINT_NELL_MICROREADER = "ENDPOINT_NELL_MICROREADER";


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
    public static final String MULTIUSER_IP_ADDRESS = "MULTIUSER_IP_ADDRESS";

    /** GOOGLE SERVICES **/
    public static final String GOOGLE_PLAY = "GOOGLE_PLAY";
    public static final String GOOGLE_ACCOUNT = "GOOGLE_ACCOUNT";
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;



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


    public static final int DEFAULT_PORT = 5555;




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


    public static final int REGISTER = 1;
    public static final int UNREGISTER = 2;
    public static final int RESPONSE = 3;
    public static final int RESPONSE_EXCEPTION = 10;


    /** AWARE **/
    public static final String AWARE = "AWARE";

    /** BATTERY **/
    public static final String BATTERY = "BATTERY";


    /** ADD SERVICES **/
    public static final String ADD_SERVICE_ACTIVITY_RECOGNITION = "ADD_SERVICE_ACTIVITY_RECOGNITION";
    public static final String ADD_SERVICE_HOTEL_RESERVATION = "ADD_SERVICE_HOTEL_RESERVATION";
    public static final String ADD_SERVICE_LOCATION = "ADD_SERVICE_LOCATION";
    public static final String ADD_SERVICE_WEATHER = "ADD_SERVICE_WEATHER";
    public static final String ADD_SERVICE_CALENDAR = "ADD_SERVICE_CALENDAR";
    public static final String ADD_SERVICE_AWARE = "ADD_SERVICE_AWARE";
    public static final String ADD_SERVICE_EXTERNAL_COMMUNICATION = "ADD_SERVICE_EXTERNAL_COMMUNICATION";
    public static final String ADD_SERVICE_RED5STREAMING = "ADD_SERVICE_RED5STREAMING";
    public static final String ADD_SERVICE_GMAIL_SERVICE = "ADD_SERVICE_GMAIL_SERVICE";
    public static final String ADD_SERVICE_IMAP_SERVICE = "ADD_SERVICE_IMAP_SERVICE";
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