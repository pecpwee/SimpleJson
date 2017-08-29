package com.pecpwee.simplejson;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.pecpwee.lib.simplejson.SimpleJson;
import com.pecpwee.simplejson.model.ScanPojo;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.btnstart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });

    }

    private void test() {
        testToJson();
        testFromJson();
    }



    private void testToJson() {
        ScanPojo scanPojo = new ScanPojo(6);
        scanPojo.initData();
        long time = SystemClock.elapsedRealtime();
        String simpleResult = new SimpleJson().toJson(scanPojo);
        Log.d(TAG, "SimpleJson tojson time cost:" + (SystemClock.elapsedRealtime() - time));
        Log.d(TAG, simpleResult);


        time = SystemClock.elapsedRealtime();
        String gsonResult = new Gson().toJson(scanPojo);
        Log.d(TAG, "Gson tojson time cost:" + (SystemClock.elapsedRealtime() - time));
        Log.d(TAG, gsonResult);
        Assert.assertEquals(simpleResult, gsonResult);

    }

    public void testFromJson() {

        ScanPojo scanPojo = new ScanPojo(2);
        scanPojo.initData();
        String str = new Gson().toJson(scanPojo);
        long time = 0;
        ScanPojo scanpojo = null;

        time = SystemClock.elapsedRealtime();
        scanpojo = new SimpleJson().fromJson(str, ScanPojo.class);
        Log.d(TAG, "simpleJson from json time cost:" + (SystemClock.elapsedRealtime() - time));
        String simpleResult = new Gson().toJson(scanpojo);
        Log.d(TAG, "simplejson from json str: " + simpleResult);

        time = SystemClock.elapsedRealtime();
        scanpojo = new Gson().fromJson(str, ScanPojo.class);
        String gsonResult = new Gson().toJson(scanpojo);
        Log.d(TAG, "Gson from json time cost:" + (SystemClock.elapsedRealtime() - time));
        Log.d(TAG, "Gson from json str: " + gsonResult);
        Assert.assertEquals(simpleResult, gsonResult);
    }
}
