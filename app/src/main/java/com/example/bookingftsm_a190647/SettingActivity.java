package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {

    TextView tv_language;
    LinearLayout ll_setting_language, ll_setting_notification, ll_setting_wifi, ll_setting_data;
    Switch switch_notif, switch_wifi, switch_data;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String SP_LANGUAGE = "language";
    String SP_NOTIFICATION = "notification";
    String SP_WIFI = "wifi";
    String SP_DATA = "data";

    String[] values = {"English", "Melayu", "Chinese", "Tamil"};

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        tv_language = findViewById(R.id.tv_language);

        switch_wifi = findViewById(R.id.switch_wifi);
        switch_data = findViewById(R.id.switch_data);
        switch_notif = findViewById(R.id.switch_notif);
        ll_setting_language = findViewById(R.id.ll_setting_language);
        ll_setting_notification = findViewById(R.id.ll_setting_notif);
        ll_setting_wifi = findViewById(R.id.ll_setting_wifi);
        ll_setting_data = findViewById(R.id.ll_setting_data);

        sharedPref = getSharedPreferences("app_setting", MODE_PRIVATE);
        editor = sharedPref.edit();

        switch_notif.setChecked(sharedPref.getBoolean(SP_NOTIFICATION, false));
        switch_wifi.setChecked(sharedPref.getBoolean(SP_WIFI, false));
        switch_data.setChecked(sharedPref.getBoolean(SP_DATA, false));
        tv_language.setText(values[sharedPref.getInt(SP_LANGUAGE,0)]);

        switch_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SP_NOTIFICATION, isChecked);
                editor.commit();
            }
        });

        switch_wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SP_WIFI, isChecked);
                editor.commit();
            }
        });

        switch_data.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SP_DATA, isChecked);
                editor.commit();
            }
        });

        ll_setting_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean switchState = switch_notif.isChecked();
                switch_notif.setChecked(!switchState);
                editor.putBoolean(SP_NOTIFICATION, !switchState);
                editor.commit();

            }
        });

        ll_setting_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowLanguageOptions();
            }
        });

        ll_setting_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean switchState = switch_wifi.isChecked();
                switch_wifi.setChecked(!switchState);
                editor.putBoolean(SP_WIFI, !switchState);
                editor.commit();
            }
        });

        ll_setting_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean switchState = switch_data.isChecked();
                switch_data.setChecked(!switchState);
                editor.putBoolean(SP_DATA, !switchState);
                editor.commit();
            }
        });



    }

    public void ShowLanguageOptions(){

        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("Select your language");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i){

                    case 0:
                        editor.putInt(SP_LANGUAGE, 0);
                        editor.commit();
                        break;

                    case 1:
                        editor.putInt(SP_LANGUAGE, 1);
                        editor.commit();
                        break;

                    case 2:
                        editor.putInt(SP_LANGUAGE, 2);
                        editor.commit();
                        break;

                    case 3:
                        editor.putInt(SP_LANGUAGE, 3);
                        editor.commit();
                        break;
                }
                alertDialog.dismiss();
                tv_language.setText(values[sharedPref.getInt(SP_LANGUAGE,0)]);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();

    }



}