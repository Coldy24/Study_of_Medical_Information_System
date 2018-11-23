package com.dof.jaeseonlee.patient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

public class SettingPreference extends PreferenceFragment  implements  SharedPreferences.OnSharedPreferenceChangeListener{
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
        context = getPreferenceScreen().getContext();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String sharedValue = sharedPreferences.getString(key,"default");
        Log.e("앙","key = "+key);
        CharSequence sharedSequence = getPreferenceManager().findPreference(key).getTitle();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("설정완료");
        alertDialogBuilder.setMessage(sharedValue + "으로 설정되었습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });



    }
}
