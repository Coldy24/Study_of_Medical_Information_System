package com.dof.jaeseonlee.patient;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class SettingActivity extends BaseActivity{

    public Context context= SettingActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        getFragmentManager().beginTransaction().replace(R.id.settingFrame, new SettingPreference()).commit();
    }


}
