package com.dof.jaeseonlee.patient;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.util.Log;

/**
 * Created by 이재선 on 2018-11-06.
 */
public class SettingActivity extends PreferenceFragment{

    @Override
    public void onCreate(Bundle savedInstanceState){
        Log.e("hello","여기까지됨1111");
        super.onCreate(savedInstanceState);
        Log.e("hello","여기까지됨2222");
        Log.e("hello","여기까지됨3333");
      //  setContentView(R.layout.activity_setting);

        Log.e("hello","여기까지됨4444");
        Log.e("hello","여기까지됨5555");
        getFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new SettingPreferenceFragment()).commit();
    }


}
