package com.example.bookingftsm_a190647;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingftsm_a190647.adapter.AdapterSearchRoom;
import com.example.bookingftsm_a190647.adapter.HistoryAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HistoryAdapter historyAdapter;
    private List<Bookings> bookingList;
    FirebaseFirestore db;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookingList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, bookingList);
        recyclerView.setAdapter(historyAdapter);

        pd = new ProgressDialog(this);
        pd.setTitle("Sedang memuat turun senarai penempahan....");
        pd.show();

        db = FirebaseFirestore.getInstance();
        // Get the current user's email
        showData("");

        historyAdapter.setOnItemLongClickListener(new HistoryAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Implement the item click action if needed
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Bookings booking = bookingList.get(position);
                // Show a confirmation dialog before deleting the booking
                showDeleteConfirmationDialog(booking);
            }
        });
//        historyAdapter.setOnItemLongClickListener(new HistoryAdapter.ClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Bookings booking = bookingList.get(position);
//                // Show a confirmation dialog before deleting the booking
//                showDeleteConfirmationDialog(booking);
//            }
//            @Override
//            public void onItemLongClick(View view, int position) {
//                Bookings booking = bookingList.get(position);
//                // Show a confirmation dialog before deleting the booking
//                showDeleteConfirmationDialog(booking);
//            }
//        });
    }

    private void deleteBookingFromDatabase(Bookings booking) {
        // Get the document ID of the booking to be deleted
        CollectionReference bookedTimeSlotsRef = db.collection("BookedList");
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Query query = bookedTimeSlotsRef.whereEqualTo("user", userEmail)
                .whereEqualTo("roomName", booking.getRoomName())
                .whereEqualTo("date", booking.getDate())
                .whereEqualTo("timeSlot", booking.getTimeSlot());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Delete the document from the collection based on the document ID
                    document.getReference().delete()
                            .addOnSuccessListener(aVoid -> {
                                // Successfully deleted
                                String notificationMessage = "Tempahan anda untuk " + booking.getReason() + " di " + booking.getRoomName() + " pada " + booking.getDate() + " pada pukul " + booking.getTimeSlot() + " telah dibatalkan.";
                                addNotificationToDatabase(notificationMessage);
                                Toast.makeText(HistoryActivity.this, "Tempahan berjaya dibatalkan", Toast.LENGTH_SHORT).show();
                                // Refresh the booking list
                                showData("");
                            })
                            .addOnFailureListener(e -> {
                                // Error deleting the booking
                                Log.d("HistoryActivity", "Failed to delete booking: " + e.getMessage());
                                Toast.makeText(HistoryActivity.this, "Pembatalan gagal", Toast.LENGTH_SHORT).show();
                            });
                }
            } else {
                // Error occurred while fetching data
                Toast.makeText(HistoryActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDeleteConfirmationDialog(Bookings booking) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Batal Tempahan");
        builder.setMessage("Adakah anda pasti untuk membatalkan tempahan ini?");
        builder.setPositiveButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Call the method to delete the booking from the database
                Log.d("HistoryActivity", "Deleting booking from dialog: " + booking.getRoomName() + " - " + booking.getDate() + " - " + booking.getTimeSlot());
                deleteBookingFromDatabase(booking);
            }
        });
        builder.setNegativeButton("Kembali", null);
        builder.show();
    }

    private void showData(String searchText) {
        CollectionReference bookedTimeSlotsRef = db.collection("BookedList");
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String userEmail = firebaseAuth.getCurrentUser().getEmail();
        Query query = bookedTimeSlotsRef.whereEqualTo("user", userEmail);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                bookingList.clear();
                pd.dismiss();

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        String roomName = doc.getString("roomName");
                        String reason = doc.getString("reason");
                        String date = doc.getString("date");
                        String timeSlot = doc.getString("timeSlot");

                        // Retrieve the document ID directly and pass it to the adapter
                        String documentId = doc.getId();
                        Bookings model = new Bookings(roomName, reason, date, timeSlot, userEmail);
                        bookingList.add(model);
                    }

                    historyAdapter.notifyDataSetChanged(); // Notify the adapter of the data change
                } else {
                    Toast.makeText(HistoryActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNotificationToDatabase(String notificationMessage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        // Create a new notification document in the "Notifications" collection
        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("userEmail", userEmail);
        notificationData.put("message", notificationMessage);
        notificationData.put("timestamp", FieldValue.serverTimestamp());

        db.collection("Notifications").add(notificationData)
                .addOnSuccessListener(documentReference -> {
                    // Notification added successfully
                    // You can perform any additional actions here if needed
                })
                .addOnFailureListener(e -> {
                    // Failed to add notification
                    // Handle the error if needed
                });
    }

}