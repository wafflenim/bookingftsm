//package com.example.bookingftsm_a190647;
//
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.app.AppCompatDelegate;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.LinearLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//public class SettingActivity extends AppCompatActivity {
//
//    Switch switch_darkmode;
//    SharedPreferences sharedPref;
//    SharedPreferences.Editor editor;
//    String SP_DARKMODE = "darkmode";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // Set the theme based on the state of switch_darkmode before calling super.onCreate()
//        if (isDarkModeEnabled()) {
//            setTheme(R.style.AppTheme_Dark);
//        } else {
//            setTheme(R.style.AppTheme);
//        }
//
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_setting);
//
//        switch_darkmode = findViewById(R.id.switch_darkmode);
//
//        sharedPref = getSharedPreferences("app_setting", MODE_PRIVATE);
//        editor = sharedPref.edit();
//
//        switch_darkmode.setChecked(sharedPref.getBoolean(SP_DARKMODE, false));
//
//        switch_darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    editor.putBoolean(SP_DARKMODE, true);
//                } else {
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    editor.putBoolean(SP_DARKMODE, false);
//                }
//                editor.apply();
//                // Recreate the activity when the theme changes
//                recreate();
//            }
//        });
//    }
//
//    // Helper method to check if dark mode is enabled
//    private boolean isDarkModeEnabled() {
//        return sharedPref.getBoolean(SP_DARKMODE, false);
//    }
//}
//

package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class SettingActivity extends AppCompatActivity {

    TextView tv_language;
    LinearLayout ll_setting_notification, ll_setting_darkmode, ll_setting_logout;
    Switch switch_notif, switch_darkmode;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    String SP_NOTIFICATION = "notification";
    String SP_DARKMODE = "darkmode";

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set the theme based on the state of switch_darkmode before calling super.onCreate()
        if (isDarkModeEnabled()) {
        //    setTheme(R.style.AppTheme_Dark);
        } else {
      //      setTheme(R.style.AppTheme_Light);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //   tv_language = findViewById(R.id.tv_language);
        switch_darkmode = findViewById(R.id.switch_darkmode);
        switch_notif = findViewById(R.id.switch_notif);
        ll_setting_notification = findViewById(R.id.ll_setting_notif);
        ll_setting_darkmode = findViewById(R.id.ll_setting_darkmode);
        ll_setting_logout = findViewById(R.id.ll_setting_logout);

        sharedPref = getSharedPreferences("app_setting", MODE_PRIVATE);
        editor = sharedPref.edit();

        switch_notif.setChecked(sharedPref.getBoolean(SP_NOTIFICATION, false));
        switch_darkmode.setChecked(sharedPref.getBoolean(SP_DARKMODE, false));

        switch_notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SP_NOTIFICATION, isChecked);
                editor.commit();
            }
        });

        switch_darkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean(SP_DARKMODE, isChecked);
                editor.commit();

                // Recreate the activity when the theme changes
                recreate();
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

        ll_setting_darkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean switchState = switch_darkmode.isChecked();
                switch_darkmode.setChecked(!switchState);
                editor.putBoolean(SP_DARKMODE, !switchState);
                editor.commit();

                // Recreate the activity when the theme changes
                recreate();
            }
        });

        ll_setting_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    // Helper method to check if dark mode is enabled
    private boolean isDarkModeEnabled() {
        return sharedPref.getBoolean(SP_DARKMODE, false);
    }
}

