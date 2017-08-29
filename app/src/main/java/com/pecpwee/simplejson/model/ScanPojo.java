package com.pecpwee.simplejson.model;

import android.os.SystemClock;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by pw on 2017/7/31.
 */

public class ScanPojo extends ParentPojo {
    private transient static final int SIZE = 5;
    private transient int transientVar;
    public Object nullobj = null;
//    public byte wifiage;
//    public int ver;
//    public long gpstime;
//    public boolean hasgps;
//    public float newdis;
//    public double dis;

    private String collectver;
    private List<WifiPojo> listpojo = new ArrayList<>();
    public LinkedList<WifiPojo> linklistpojo = new LinkedList<>();
    public WifiPojo[] objArray = new WifiPojo[SIZE];
    private int[] intArray = new int[SIZE];

    public ScanPojo(int type) {
        super(type);
    }

    public void initData() {
        for (int i = 0; i < SIZE; i++) {
            listpojo.add(new WifiPojo(23, SystemClock.elapsedRealtimeNanos(), "list" + i));
            intArray[i] = i;
            linklistpojo.add(new WifiPojo(22, SystemClock.elapsedRealtimeNanos(), "linked" + i));
        }

        transientVar = 123;
//        wifiage = 0;
//        ver = 3;
//        gpstime = 0;
//        hasgps = false;
//        newdis = -432.34e-43f;
//        dis = 123.4E5d;
        collectver = "uid def10\t\u0012\"";
    }
}
