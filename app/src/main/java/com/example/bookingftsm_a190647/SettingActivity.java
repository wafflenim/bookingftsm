package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {

    private Switch switchDarkMode;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String DARK_MODE = "isDarkModeOn";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        switchDarkMode = findViewById(R.id.switch_darkmode);
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        boolean isDarkModeOn = sharedPreferences.getBoolean(DARK_MODE, false);
        switchDarkMode.setChecked(isDarkModeOn);

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state of the dark mode switch
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(DARK_MODE, isChecked);
                editor.apply();

                // Apply the selected mode immediately
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }
}
