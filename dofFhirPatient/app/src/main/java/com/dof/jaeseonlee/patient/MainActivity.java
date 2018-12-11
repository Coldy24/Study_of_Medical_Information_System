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
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

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
    public static String careName;
    public static String carePhoneNum;
    public static String patientFamilyName;
    public static String patientGivenName;
    public static String patientGender;
    public static String patientBirthDate;
    public static String patientTelecom;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFragment = new HomeFragment();
        logFragment = new LogFragment();
        settingPreferenceFragment = new SettingPreferenceFragment();


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

        getSharedData();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new HomeFragment().newInstance(patientFamilyName+patientGivenName, patientGender, patientBirthDate, careName,carePhoneNum)).commit();

    }

    @Override
    public void onClick(View view) {

        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mainLayoutContainer);
        frameLayout.removeAllViews();


        switch (view.getId()){
            case R.id.homeButton: {
                getSharedData();
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer,new HomeFragment().newInstance(patientFamilyName+patientGivenName, patientGender, patientBirthDate, careName,carePhoneNum)).commit();
                Log.e("으익클릭","홈클릭");
                break;
            }
            case R.id.logButton:{
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new LogFragment()).commit();
                Log.e("으익클릭","로그클릭");
                break;
            }
            case R.id.settingButton:{
                getFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new SettingPreferenceFragment()).commit();
                Log.e("으익클릭","세팅클릭");
            }
        }
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

        patientFamilyName = sharedPreferences.getString("User_Family_Name","값없음");
        patientGivenName = sharedPreferences.getString("User_Given_Name","값없음");
        patientGender = sharedPreferences.getString("User_Gender","값없음");
        patientBirthDate = sharedPreferences.getString("User_BirthDay","값없음");
        carePhoneNum=sharedPreferences.getString("Protector_Phone","값없음");
        careName = sharedPreferences.getString("Protector_Name","값없음");

    }

    @Override
    public void onResume() {
        super.onResume();
        getSharedData();
        Log.e("으익클릭","onResume클릭");
    }


}
