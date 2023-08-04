
package com.example.bookingftsm_a190647;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

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
                Bookings booking = bookingList.get(position);
                // Show a confirmation dialog before deleting the booking
                showDeleteConfirmationDialog(booking);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Bookings booking = bookingList.get(position);
                // Show a confirmation dialog before deleting the booking
                showDeleteConfirmationDialog(booking);
            }
        });
    }

    private void deleteBookingFromDatabase(Bookings booking) {
        // Get the document ID of the booking to be deleted
        CollectionReference bookedTimeSlotsRef = db.collection("BookedList");
        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Query query = bookedTimeSlotsRef.whereEqualTo("user", userEmail)
                .whereEqualTo("roomName", booking.getRoomName())
                .whereEqualTo("date", booking.getDate())
                .whereEqualTo("timeslot", booking.getTimeSlot());

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Delete the document from the collection based on the document ID
                    document.getReference().delete()
                            .addOnSuccessListener(aVoid -> {
                                // Successfully deleted
                                Toast.makeText(HistoryActivity.this, "Booking deleted successfully", Toast.LENGTH_SHORT).show();
                                // Refresh the booking list
                                showData("");
                            })
                            .addOnFailureListener(e -> {
                                // Error deleting the booking
                                Toast.makeText(HistoryActivity.this, "Failed to delete booking", Toast.LENGTH_SHORT).show();
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
        builder.setTitle("Delete Booking");
        builder.setMessage("Are you sure you want to delete this booking?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Call the method to delete the booking from the database
                deleteBookingFromDatabase(booking);
            }
        });
        builder.setNegativeButton("Cancel", null);
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
                        String timeSlot = doc.getString("timeslot");

                        // Retrieve the document ID directly and pass it to the adapter
                        String documentId = doc.getId();
                        Bookings model = new Bookings(roomName, reason, date, timeSlot, userEmail);
                        bookingList.add(model);
                    }

                    historyAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HistoryActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
