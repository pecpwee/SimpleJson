package com.pecpwee.simplejson.model;

/**
 * Created by pw on 2017/7/31.
 */

class WifiPojo extends ParentPojo {
    private String ssid;
    private long time;

    public WifiPojo(int type, long time, String ssid) {
        super(type);
        this.time = time;
        this.ssid = ssid;
    }
}
