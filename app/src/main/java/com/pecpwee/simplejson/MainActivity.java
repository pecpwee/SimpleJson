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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button btnStart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart= (Button) findViewById(R.id.btnstart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });

    }

    private void test(){
        testToJson();
        testFromJson();
        List list = new ArrayList();
        Log.d(TAG, "fuck" + list.getClass().getName() + isListObject(list.getClass()));

    }


    private static boolean isListObject(Class type) {
        Class[] interfaces = type.getInterfaces();
        Log.d(TAG, "interface" + interfaces.length);

        for (Class clazz : interfaces) {
            Log.d(TAG, "interface" + clazz.getName());

            if (clazz == List.class) {

                return true;
            }
        }
        return false;
    }

    private void testToJson() {
        ScanPojo scanPojo = new ScanPojo();
        scanPojo.initData();
        long time = SystemClock.elapsedRealtime();
        String s = new SimpleJson().toJson(scanPojo);
        Log.d(TAG, "SimpleJson tojson time cost:" + (SystemClock.elapsedRealtime() - time));
        Log.d(TAG, s);


        time = SystemClock.elapsedRealtime();
        String s1 = new Gson().toJson(scanPojo);
        Log.d(TAG, "Gson tojson time cost:" + (SystemClock.elapsedRealtime() - time));
        Log.d(TAG, s1);

    }

    public void testFromJson() {

        ScanPojo scanPojo = new ScanPojo();
        scanPojo.initData();
        String str = new Gson().toJson(scanPojo);
        long time = 0;
        ScanPojo scanpojo = null;

        time = SystemClock.elapsedRealtime();
        scanpojo = new SimpleJson().fromJson(str, ScanPojo.class);
        Log.d(TAG, "simpleJson from json time cost:" + (SystemClock.elapsedRealtime() - time));

        Log.d(TAG, "simplejson from json str: " + new Gson().toJson(scanpojo));

        time = SystemClock.elapsedRealtime();
        scanpojo = new Gson().fromJson(str, ScanPojo.class);
        Log.d(TAG, "Gson from json time cost:" + (SystemClock.elapsedRealtime() - time));
        Log.d(TAG, "Gson from json str: " + new Gson().toJson(scanpojo));


    }
}
