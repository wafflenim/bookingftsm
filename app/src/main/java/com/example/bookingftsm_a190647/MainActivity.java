package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ImageView tempah_ruang_main_btn, noti_main_btn, tetapan_main_btn, sejarah_main_btn;
    Button btn_logout;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser sFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempah_ruang_main_btn = findViewById(R.id.tempah_ruang_main_btn);
        noti_main_btn = findViewById(R.id.noti_main_btn);
        tetapan_main_btn = findViewById(R.id.tetapan_main_btn);
        sejarah_main_btn = findViewById(R.id.sejarah_main_btn);
        btn_logout = findViewById(R.id.btn_logout);
        mFirebaseAuth = FirebaseAuth.getInstance();
        sFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseAuth == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }

        tempah_ruang_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchRoomActivity.class);
                startActivity(intent);
                //               finish();
            }
        });

        sejarah_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                startActivity(intent);
                //               finish();
            }
        });

        tetapan_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(intent);
                //               finish();
            }
        });

        noti_main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(intent);
                //               finish();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
    }

    }



 //   @Override
  //  protected void onPostResume() {
 //       super.onPostResume();
  //      mFirebaseAuth = FirebaseAuth.getInstance();
   //     sFirebaseUser = mFirebaseAuth.getCurrentUser();

    //    if (mFirebaseAuth==null)
  //      {
   //         //go to login page
   //         Intent intent = new Intent (MainActivity.this, LoginActivity.class);
    //        startActivity(intent);
   //     }

  //  }
