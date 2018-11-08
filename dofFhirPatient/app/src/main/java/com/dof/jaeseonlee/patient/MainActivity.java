package com.dof.jaeseonlee.patient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/* create 이재선, 2018-11-01 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext = MainActivity.this;
    private HomeFragment homeFragment;
    private LogFragment logFragment;
    private SettingActivity settingActivity;

    protected void onCreate(Bundle savedInstanceState) {
        Log.v("jsjs","으익으익들어옴");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        logFragment = new LogFragment();
        settingActivity = new SettingActivity();

        Button homeButton, logButton, settingButton;
        homeButton = (Button)findViewById(R.id.homeButton);
        logButton = (Button)findViewById(R.id.logButton);
        settingButton = (Button)findViewById(R.id.settingButton);
        homeButton.setOnClickListener(this);
        logButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Log.v("jsjs","으익으익 온클릭");
        switch (view.getId()){
            case R.id.homeButton: {
                transaction.replace(R.id.mainLayoutContainer,homeFragment);
                break;
            }
            case R.id.logButton:{
                transaction.replace(R.id.mainLayoutContainer,logFragment);
                break;
            }
            case R.id.settingButton:{
                Log.v("재선재선재선","으익으익 클릭");
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    }





}
