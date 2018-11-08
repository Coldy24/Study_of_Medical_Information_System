package com.dof.jaeseonlee.patient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingPreference extends PreferenceFragment  implements Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener{
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.v("으잉으잉","settingPreference 으익으익");
        addPreferencesFromResource(R.xml.setting_preference);
        context = getPreferenceScreen().getContext();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
       // String
    }
}
