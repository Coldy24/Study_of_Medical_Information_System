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
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_setting);

        getFragmentManager().beginTransaction().replace(R.id.mainLayoutContainer, new SettingPreferenceFragment()).commit();
    }


}
