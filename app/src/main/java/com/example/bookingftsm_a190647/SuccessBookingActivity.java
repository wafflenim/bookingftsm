package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SuccessBookingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_booking);
        // Get the booking details from the intent extras
        Intent intent = getIntent();
        String roomName = intent.getStringExtra("roomName");
        String reason = intent.getStringExtra("reason");
        String selectedDate = intent.getStringExtra("selectedDate");
        String timeSlot = intent.getStringExtra("timeSlot");

        // Set the booking details in the views
        TextView tvRoomName = findViewById(R.id.tv_namaruang2);
        tvRoomName.setText(roomName);

        TextView tvReason = findViewById(R.id.tv_tujuan2);
        tvReason.setText(reason);

        TextView tvSelectedDate = findViewById(R.id.tv_tarikh2);
        tvSelectedDate.setText(selectedDate);

        TextView tvTimeSlot = findViewById(R.id.tv_masa2);
        tvTimeSlot.setText(timeSlot);

        // Set click listener for the "Menu Utama" button
        Button btnMainMenu = findViewById(R.id.btn_mainmenu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to the main menu activity
                Intent mainMenuIntent = new Intent(SuccessBookingActivity.this, MainActivity.class);
                mainMenuIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear all previous activities from the stack
                startActivity(mainMenuIntent);
                finish(); // Finish the current activity so that the user cannot navigate back to it from the main menu
            }
        });
    }

}