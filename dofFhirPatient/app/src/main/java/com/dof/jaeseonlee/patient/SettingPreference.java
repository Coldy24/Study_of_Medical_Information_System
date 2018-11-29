package com.dof.jaeseonlee.patient;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by 이재선 on 2018-11-06.
 */
public class SettingPreference extends PreferenceFragment  implements  SharedPreferences.OnSharedPreferenceChangeListener, Preference.OnPreferenceClickListener{
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
        alertDialogBuilder.show();

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
       /* final CharSequence  birthDate = "사용자 생년월일";
        final CharSequence gender = "사용자 성별";
        switch(preference.getTitleRes()){
            case R.string.user_Birth_Date: {
                Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
                DatePickerDialog dialog1 = new DatePickerDialog(context);
                DatePickerDialog dialog = new DatePickerDialog(context, ,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.show();
                return true;
            }

            case R.string.user_Gender: {

                return true;
            }

        }*/
        return false;
    }
}
