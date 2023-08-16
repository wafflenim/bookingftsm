package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.bookingftsm_a190647.adapter.NotificationAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(this, notificationList);
        recyclerView.setAdapter(notificationAdapter);

        db = FirebaseFirestore.getInstance();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Query the "Notifications" collection to fetch the notifications for the current user
        db.collection("Notifications")
                .whereEqualTo("userEmail", userEmail)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String message = document.getString("message");
                        Notification notification = new Notification(message);
                        notificationList.add(notification);
                    }
                    notificationAdapter.notifyDataSetChanged();
                    Log.d("NotificationActivity", "Notification List Size: " + notificationList.size());
                })
                .addOnFailureListener(e -> {
                    // Failed to fetch notifications
                    // Handle the error if needed
                    Log.e("NotificationActivity", "Error fetching notifications: " + e.getMessage());
                });

    }
}