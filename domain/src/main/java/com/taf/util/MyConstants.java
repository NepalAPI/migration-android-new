package com.taf.util;

/**
 * Created by julian on 12/9/15.
 */
public class MyConstants {

    public static final String YOUTUBE_API_KEY = "AIzaSyAbqEgRif7zCL7R_OoK0rJK1Cc2LzOTWVE";
    public static final int APP_CACHE_VERSION = 1;
    public static final long MAX_CACHE_SIZE = Long.MAX_VALUE;

    public enum DataParent {
        COUNTRY,
        JOURNEY,
        POST,
        INFO
    }

    public static final class Country {
        public static final String MALAYSIA = "मलेसिया";
        public static final String SAUDI_ARABIA = "साउदी अरेबिया";
        public static final String QATAR = "कतार";
        public static final String UAE = "यूएई";
        public static final String KUWAIT = "कुवेत";
        public static final String NEPAL = "नेपाल";

        public static final String[] COUNTRY_LIST = {MALAYSIA, SAUDI_ARABIA, QATAR, UAE, KUWAIT,
                NEPAL};

        public static final String CURRENCY_KUWAIT = "Kuwaity Dinar";
        public static final String ABV_CURRENCY_KUWAIT = "KWD";
        public static final String CURRENCY_SAUDI_ARABIA = "Saudi Arabian riyal";
        public static final String ABV_CURRENCY_SAUDI_ARABIA = "SAR";
        public static final String CURRENCY_QATAR = "Qatari riyal";
        public static final String ABV_CURRENCY_QATAR = "QR";
        public static final String CURRENCY_MALAYSIA = "Malaysian ringgit";
        public static final String ABV_CURRENCY_MALAYSIA = "MYR";
        public static final String CURRENCY_UAE = "UAE Dirham";
        public static final String ABV_CURRENCY_UAE = "AED";

        public static final String[] CURRENCY_LIST = {CURRENCY_KUWAIT, CURRENCY_SAUDI_ARABIA,
                CURRENCY_QATAR, CURRENCY_MALAYSIA, CURRENCY_UAE};

        public static String getCurrencyKey(String country) {
            switch (country) {
                case MALAYSIA:
                    return CURRENCY_MALAYSIA;
                case SAUDI_ARABIA:
                    return CURRENCY_SAUDI_ARABIA;
                case QATAR:
                    return CURRENCY_QATAR;
                case UAE:
                    return CURRENCY_UAE;
                case KUWAIT:
                    return CURRENCY_KUWAIT;
                case NEPAL:
                default:
                    return "";
            }
        }

        public static String getCurrency(String country) {
            switch (country) {
                case MALAYSIA:
                    return ABV_CURRENCY_MALAYSIA;
                case SAUDI_ARABIA:
                    return ABV_CURRENCY_SAUDI_ARABIA;
                case QATAR:
                    return ABV_CURRENCY_QATAR;
                case UAE:
                    return ABV_CURRENCY_UAE;
                case KUWAIT:
                    return ABV_CURRENCY_KUWAIT;
                case NEPAL:
                default:
                    return "NPR";
            }
        }
    }

    public static final class UseCase {
        public static final String CASE_COUNTRY_LIST = "country_list";

        public static final String CASE_CHANNEL_LIST = "channel_list";
    }

    public static final class Preferences {
        public static final String PREF_NAME = "nrna_app_preferences";
        public static final String LAST_UPDATE_STAMP = "last_update_stamp_1";
        public static final String LAST_DELETE_STAMP = "last_delete_stamp_1";
        public static final String DOWNLOAD_REFERENCES = "download_references";

        // --------- new preferences --------------
        public static final String USERNAME = "username";
        public static final String BIRTHDAY = "birthday";
        public static final String ORIGINAL_LOCATION = "original-district";
        public static final String PREVIOUS_WORK_STATUS = "past-work-status";
        public static final String GENDER = "gender";
        public static final String FIRST_LAUNCH = "first-launch";
        public static final String LOCATION = "location";
        public static final String DEFAULT_LOCATION = "Nepal";

        public static final String COUNTRY_LIST_CALL = "country-list-call";
        public static final String COUNTRY_LIST = "country-list";

        public static final String FAV_POSTS = "favourite-posts";
        public static final String NOTICE_DISMISS_ID = "home-notice-dismiss-id";
        public static final String COUNTRY_NOTICE_DISMISS_ID = "country-notice-dismiss-id";
        public static final String LAST_TRACK = "last_track";
    }

    public static final class Language {
        public static final int ENGLISH = 0;
        public static final int NEPALI = 1;
    }

    public static final class API {
        public static final String LATEST_CONTENT = "api/latest";
        public static final String SYNC_DATA = "api/sync";
        public static final String DELETED_CONTENT = "api/trash";
        public static final String DESTINATION = "api/destinations";

        public static final String HOME = "api/screen/home";
        public static final String OPEN_WEATHER = "data/2.5/weather";
        public static final String FOREX = "fetchForex";
        public static final String JOURNEY = "api/screen/journey";
        public static final String PODCASTS = "api/podcasts";
        public static final String POSTS = "api/posts";
        public static final String POST = "api/posts/{id}";
        public static final String POST_FAVOURITE = "api/post/{id}/favorite";
        public static final String POST_SHARE = "api/post/{id}/share";
        public static final String DESTINATION_DETAIL = "api/screen/destination/{id}";

        //TODO: need to change after api for channel is build
        public static final String CHANNEL = "api/radio/categories";
        public static final String SEARCH_POSTS = "api/posts";
    }

    public static final class Adapter {
        public static final int TYPE_AUDIO = 1;
        public static final int TYPE_VIDEO = 2;
        public static final int TYPE_TEXT = 3;
        public static final int TYPE_NEWS = 4;
        public static final int TYPE_JOURNEY_CATEGORY = 5;
        public static final int TYPE_PLACE = 6;
        public static final int TYPE_COUNTRY = 7;
        public static final int TYPE_SECTION = 8;
        public static final int TYPE_NOTIFICATION = 9;
        public static final int TYPE_INFO = 10;
        public static final int TYPE_CATEGORY_HEADER = 11;
        public static final int TYPE_PODCAST = 12;
        public static final int TYPE_COUNTRY_SELECTED = 13;
        public static final int TYPE_COUNTRY_HEADER = 14;
        public static final int TYPE_BLOCK = 15;
        public static final int TYPE_COUNTRY_WIDGET = 16;
        public static final int VIEW_TYPE_LIST = 17;
        public static final int VIEW_TYPE_SLIDER = 18;
        public static final int VIEW_TYPE_NOTICE = 19;
        public static final int VIEW_TYPE_RADIO_WIDGET = 20;
        public static final int TYPE_CHANNEL = 21;
        public static final int TYPE_CHANNEL_HEADER = 22;
        public static final int TYPE_CHANNEL_SELECTED = 23;
    }


    public static class Media {
        public static final String ACTION_MEDIA_BUFFER_START = "media.action.BUFFER_START";
        public static final String ACTION_MEDIA_BUFFER_STOP = "media.action.BUFFER_STOP";
        public static final String ACTION_MEDIA_COMPLETE = "media.action.COMPLETE";
        public static final String ACTION_STATUS_PREPARED = "media.action.STATUS_PREPARED";
        public static final String ACTION_PLAY_STATUS_CHANGE = "media.action.PLAY_STATUS_CHANGED";
        public static final String ACTION_PROGRESS_CHANGE = "media.action.PROGRESS";

        public static final String ACTION_MEDIA_PLAYBACK_CHANGE = "media.action.MEDIA_CHANGE";
        public static final String ACTION_MEDIA_SEEK_TO = "media.action.SEEK_TO";
        public static final String ACTION_PLAY_PAUSE_TOGGLE = "media.action.MEDIA_PLAY_PAUSE";
        public static final String ACTION_HIDE_MINI_PLAYER = "media.action.HIDE_PLAYER";
    }

    public static final class Extras {
        public static final String KEY_PAGER_POSITION = "pager-position";
        public static final String KEY_ID = "key_id";
        public static final String KEY_POST = "key_post";
        public static final String KEY_AUDIO = "key_audio";
        public static final String KEY_VIDEO = "key_video";
        public static final String KEY_ARTICLE = "key_article";
        public static final String KEY_PLACE = "key_place";
        public static final String KEY_PLAY_STATUS = "key_play_status";
        public static final String KEY_FAVOURITES_ONLY = "key_favourites_only";
        public static final String KEY_FAVOURITE_STATUS = "key_favourite_status";
        public static final String KEY_FAVOURITE_COUNT = "key_favourite_count";
        public static final String KEY_SUBCATEGORY = "key_subcategory";
        public static final String KEY_CATEGORY = "key_category";
        public static final String KEY_TAG = "key_tag";
        public static final String KEY_CATEGORY_ID = "key_category_id";
        public static final String KEY_FROM_CATEGORY = "key_from_category";
        public static final String KEY_VIEW_COUNT = "key_view_count";
        public static final String KEY_EXCLUDE_LIST = "key_exclude";
        public static final String KEY_SHARE_COUNT = "key_share_count";
        public static final String KEY_TITLE = "key_title";
        public static final String KEY_FILTER_FAVOURITES_ONLY = "key_filter_fav_only";
        public static final String KEY_SUB_CATEGORY = "key_sub_category";
        public static final String KEY_TYPE = "key_post_type";
        public static final String KEY_COUNTRY = "key_country";
        public static final String KEY_CHANNEL_ID = "key_channel_id";
    }

    public static final class Intent {
        public static final String GCM_REGISTRATION_COMPLETE = "gcm_registration_complete";
        public static final String ACTION_SHOW_RADIO = "intent.action.SHOW_RADIO";
    }

    public static class SECTION {
        public static final String JOURNEY = "journey";
        public static final String COUNTRY = "country";
        public static final String INFO = "info";
    }

    public static class OnBoarding {

        public static final int USERNAME = 0;
        public static final int BIRTHDAY = 1;
        public static final int GENDER = 2;
        public static final int ORIGINAL_LOCATION = 3;
        public static final int WORK_STATUS = 4;
        public static final int PREFERRED_DESTINATION = 5;
    }

    public static final class WEATHER {

        public static final String TYPE_CLEAR_SKY = "clear sky";
        public static final String TYPE_FEW_CLOUDS = "few clouds";
        public static final String TYPE_SCATTERED_CLOUDS = "scattered clouds";
        public static final String TYPE_BROKEN_CLOUDS = "broken clouds";
        public static final String TYPE_SHOWER_RAIN = "shower rain";
        public static final String TYPE_RAIN = "rain";
        public static final String TYPE_THUNDERSTORM = "thunderstorm";
        public static final String TYPE_SNOW = "snow";
        public static final String TYPE_MIST = "mist";
    }
}
