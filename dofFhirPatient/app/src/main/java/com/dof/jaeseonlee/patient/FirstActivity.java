package com.dof.jaeseonlee.patient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;
/**
 * Created by 이재선 on 2018-10-29.
 */
public class FirstActivity extends AppCompatActivity {
    private TimerTask waitTask;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        waitTask = new TimerTask() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        };

        timer = new Timer();
        timer.schedule(waitTask, 3000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        timer.cancel();
    }


}
