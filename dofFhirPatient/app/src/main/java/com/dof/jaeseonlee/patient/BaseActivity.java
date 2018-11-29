package com.dof.jaeseonlee.patient;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
/**
 * Created by 이재선 on 2018-10-29.
 */
public class BaseActivity extends AppCompatActivity {


    protected SharedPreferences sharedPreferences = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if(sharedPreferences == null){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        }
        //setContentView(R.layout.activity_main);

        /*homeFragment = new HomeFragment();
        logFragment = new LogFragment();
        settingActivity = new SettingActivity();*/

    }
}




