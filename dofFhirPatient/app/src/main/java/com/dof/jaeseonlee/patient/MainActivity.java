package com.dof.jaeseonlee.patient;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

/**
 * Created by 이재선 on 2018-11-01.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener{

    private Context mContext = MainActivity.this;

    private HomeFragment homeFragment;
    private LogFragment logFragment;
    private SettingPreferenceFragment settingPreferenceFragment;





    public static int hrm;
    public static String CarePhoneNum;
    public static String PatientName;
    public static String PatientGender;
    public static String PatientTelecom;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("권한허가를 하지 않으면 서비스를 이용할 수 없을수도 있습니다.\n\n 설정->권한에서 권한을 설정해주세요.")
                .setPermissions(Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE,
                        Manifest.permission.INTERNET, Manifest.permission.VIBRATE,
                        Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_NUMBERS)
                .check();



        Button homeButton, logButton, settingButton;
        homeButton = (Button)findViewById(R.id.homeButton);
        logButton = (Button)findViewById(R.id.logButton);
        settingButton = (Button)findViewById(R.id.settingButton);
        homeButton.setOnClickListener(this);
        logButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new HomeFragment()).commit();


    }

    @Override
    public void onClick(View view) {

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mainLayoutContainer);
        frameLayout.removeAllViews();


        switch (view.getId()){
            case R.id.homeButton: {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer,new HomeFragment()).commit();
                break;
            }
            case R.id.logButton:{
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new LogFragment()).commit();
                break;
            }
            case R.id.settingButton:{
                getFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new SettingPreferenceFragment()).commit();
            }
        }
    }

    private void getSettingData(){
        sharedPreferences.getString(CarePhoneNum,"값 없음");
    }


    PermissionListener permissionListener = new PermissionListener() {

        @Override
        public void onPermissionGranted() {

        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this,"권한 거부됨\n" + deniedPermissions.toString(),Toast.LENGTH_SHORT).show();
        }
    };


    private void getSharedData(){
        String familyName;
        String givenName;
        String gender;
        String birthDate;
        boolean smoking;

        String careName;
        String carePhoneNum;




    }



    /*

      if(!prefs.getString("sound_list", "").equals("")){
            soundPreference.setSummary(prefs.getString("sound_list", "카톡"));
        }

        if(!prefs.getString("keyword_sound_list", "").equals("")){
            keywordSoundPreference.setSummary(prefs.getString("keyword_sound_list", "카톡"));
        }

        if(prefs.getBoolean("keyword", false)){
            keywordScreen.setSummary("사용");
        }

        prefs.registerOnSharedPreferenceChangeListener(prefListener);



     */


}
