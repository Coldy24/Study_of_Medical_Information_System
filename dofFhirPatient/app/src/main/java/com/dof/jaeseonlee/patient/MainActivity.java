package com.dof.jaeseonlee.patient;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by 이재선 on 2018-11-01.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext = MainActivity.this;
   // private HomeFragment homeFragment;
    private LogFragment logFragment;
    private SettingActivity settingActivity;





    public static int hrm;
    public static String CarePhoneNum;
    public static String PatientName;
    public static String PatientGender;
    public static String PatientTelecom;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //homeFragment = new HomeFragment();
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
        switch (view.getId()){
            case R.id.homeButton: {
               // transaction.replace(R.id.mainLayoutContainer,homeFragment);
                break;
            }
            case R.id.logButton:{
                transaction.replace(R.id.mainLayoutContainer,logFragment);
                break;
            }
            case R.id.settingButton:{
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void getSettingData(){
        sharedPreferences.getString(CarePhoneNum,"값 없음");
    }





}
