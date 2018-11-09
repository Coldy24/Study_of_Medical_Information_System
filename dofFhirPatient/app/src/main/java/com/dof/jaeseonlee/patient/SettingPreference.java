package com.dof.jaeseonlee.patient;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.View;

public class SettingPreference extends PreferenceFragment  implements View.OnClickListener, Preference.OnPreferenceClickListener, SharedPreferences.OnSharedPreferenceChangeListener{
    private Context context;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.setting_preference);
        context = getPreferenceScreen().getContext();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);


    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        String sharedValue = sharedPreferences.getString(key,"default");
        CharSequence sharedSequence = getPreferenceManager().findPreference(key).getTitle();

        switch(sharedPreferences.toString()){
            case "사용자 이름":
            case "보호자 이름":
            case "보호자 전화번호":
            case "사용자 나이":
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(sharedPreferences.toString());
                alertDialogBuilder.setMessage(sharedValue + "으로 설정되었습니다.").setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

            case "사용자 성별":

                break;

            case "사용자 흡연여부":
                break;


        }



    }

    @Override
    public void onClick(View view) {

    }
}
