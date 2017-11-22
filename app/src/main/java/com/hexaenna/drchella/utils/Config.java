package com.hexaenna.drchella.utils;

/**
 * Created by admin on 11/21/2017.
 */

public class Config {
    // Google Console APIs developer key
    // Replace this key with your's
    public static final String DEVELOPER_KEY = "AIzaSyDjv06RUfxV6rRzmuUQWpolG9Bm2eCqPvA";

    // YouTube video id
    public static final String YOUTUBE_VIDEO_CODE = "dWndJ20ykSg";
    private static Config ourInstance = new Config();
    private int millSec = 0;

    public static Config getInstance() {
        return ourInstance;
    }
    public int getMillSec() {
        return millSec;
    }

    public void setMillSec(int millSec) {
        this.millSec = millSec;
    }


    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}