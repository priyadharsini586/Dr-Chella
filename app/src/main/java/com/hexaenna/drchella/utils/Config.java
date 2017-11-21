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
}