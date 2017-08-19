package com.pecpwee.simplejson.model;

/**
 * Created by pw on 2017/7/31.
 */

class WifiPojo {
    private String ssid;
    private long time;

    public WifiPojo(long time, String ssid) {
        this.time = time;
        this.ssid = ssid;
    }
}
