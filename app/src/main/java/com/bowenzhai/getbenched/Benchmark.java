package com.bowenzhai.getbenched;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;

public class Benchmark extends AppCompatActivity {

    private TextView result;
    private Button compute;
    private String testString;
    private String HashValue;
    private Long ttShared;
    private String output;
    private long tsLong;

    Handler handler3 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Long tt = (Long) msg.obj;
            ttShared = tt;
            printout();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);
        compute = (Button)findViewById(R.id.btn1);
        result = (TextView)findViewById(R.id.textView2);
        testString = getResources().getString(R.string.testString);
    }

    public void printout() {
        compute.setText("TEST AGAIN...");
        Integer score = Math.round(ttShared / 100000000);
        result.setText("SHA-1 hash: " + " " + HashValue + "SHA-1 Time Taken: " + ttShared.toString() +" nanoseconds\nScore: " +
                score.toString() + " (Lower result is faster)");

    }

    public void computeSHAHash(String password) {
        Runnable r = new Runnable() {
            public void run() {
                Long tsLong = System.nanoTime();
                for (Integer i = 0; i < 20000; i++) {
                    MessageDigest mdSha1 = null;
                    try {
                        mdSha1 = MessageDigest.getInstance("SHA-1");
                    } catch (NoSuchAlgorithmException e1) {
                        Log.e("Benchmark", "Error initializing SHA1");
                    }
                    try {
                        mdSha1.update("The big bad wolf".getBytes("ASCII"));
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    byte[] data = mdSha1.digest();
                    StringBuffer sb = new StringBuffer();
                    String hex = null;
                    hex = Base64.encodeToString(data, 0, data.length, 0);

                    sb.append(hex);
                    HashValue = sb.toString();
                }

                Long ttLong3 = System.nanoTime() - tsLong;
                Message msg = Message.obtain();
                msg.obj = ttLong3;
                msg.setTarget(handler3);
                msg.sendToTarget();

            }

        };
        Thread newThread = new Thread(r);
        newThread.start();

    }

    public void onBeginClick (View view) {
        computeSHAHash(testString);
        compute.setText("CALCULATING...");
    }
}
