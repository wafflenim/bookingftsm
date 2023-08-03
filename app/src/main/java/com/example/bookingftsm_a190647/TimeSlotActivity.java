package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookingftsm_a190647.adapter.AdapterSearchRoom;
import com.example.bookingftsm_a190647.adapter.TimeSlotAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class TimeSlotActivity extends AppCompatActivity {

    // Declare the TimeSlotsListener interface inside the TimeSlotActivity class
    public interface TimeSlotsListener {
        void onTimeSlotsLoaded(List<TimeSlot> timeSlots);
    }

    EditText inputTujuan;
    RecyclerView recyclerViewTimeSlot;
    FirebaseRecyclerOptions<TimeSlot> options;
    FirebaseRecyclerAdapter<TimeSlot, MyViewHolder> adapter;
    CollectionReference DataRef;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    RecyclerView.LayoutManager layoutManager;

    List<TimeSlot> modelList = new ArrayList<>();
    private int selectedPosition = -1;

    GridView gridViewTimeSlot;
    //firestore instance
    FirebaseFirestore db;
    TimeSlotAdapter adapterTimeSlot;
    TextView selectDate;
    private String selectedDate = "";
    String userEmail = currentUser.getEmail();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);

        Button btnKembali = findViewById(R.id.btn_kembali);
        Button btnTempah = findViewById(R.id.btn_tempah);

        interface TimeSlotsListener {
            void onTimeSlotsLoaded(List<TimeSlot> timeSlots);
        }

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchRoomActivity.class);
                startActivity(intent);
            }
        });

        //select date
        selectDate = findViewById(R.id.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        // Retrieve the room name from the intent extras
        String roomName = getIntent().getStringExtra("roomName");


        // Now you can use the roomName variable to display the room name in the activity as needed
        TextView tvRoomName = findViewById(R.id.nama_bilik);
        tvRoomName.setText(roomName);

        inputTujuan = findViewById(R.id.et_tujuan);

        db = FirebaseFirestore.getInstance();

        //open calendar
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance(); //fetch calendar object
                int year_m = calendar.get(Calendar.YEAR); //current year
                int month_m = calendar.get(Calendar.MONTH); //current month
                int day_m = calendar.get(Calendar.DAY_OF_MONTH); //CURRENT DAY

                //date picker dialog
                DatePickerDialog datePicker = new DatePickerDialog(TimeSlotActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        selectedDate = day + "." + (month + 1) + "." + year; // Update the selectedDate variable

                        selectDate.setText(selectedDate); // Display the selected date on the TextView

                        // Now call the showTimeSlot method with the selectedDate
                        showTimeSlot(roomName, selectedDate);
                    }
                }, year_m, month_m, day_m);
                datePicker.show();
            }
        });

        // Set up the GridView for time slots
        gridViewTimeSlot = findViewById(R.id.gridViewTimeSlot);
        layoutManager = new LinearLayoutManager(this);
        gridViewTimeSlot.setNumColumns(2);
        gridViewTimeSlot.setHorizontalSpacing(8);
        gridViewTimeSlot.setVerticalSpacing(8);
        gridViewTimeSlot.setPadding(8, 8, 8, 8);

        btnTempah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected time slot from the adapter
                TimeSlot selectedTimeSlot = adapterTimeSlot.getSelectedTimeSlot();

                // Check if a time slot is selected
                if (selectedTimeSlot != null) {
                    // Get the time slot value
                    String timeSlot = selectedTimeSlot.getTimeslot();

                    // Get other input values (roomName, reason, selected_date)
                    String roomName = tvRoomName.getText().toString().trim();
                    String reason = inputTujuan.getText().toString().trim();
                    String selected_date = selectDate.getText().toString().trim();

                    // Call the function to upload and start the SuccessBookingActivity

                    uploadData(roomName, reason, selected_date, timeSlot, userEmail);
                } else {
                    Toast.makeText(TimeSlotActivity.this, "Please select a time slot.", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    private void showTimeSlot(String roomName, String selectedDate) {
        // Clear the modelList before adding new data
        modelList.clear();

        // Create a list of predefined time slots
        List<String> predefinedTimeSlots = Arrays.asList(
                "7:00AM-8:00AM",
                "8:00AM-9:00AM",
                "9:00AM-10:00AM",
                "10:00AM-11:00AM",
                "11:00AM-12:00PM",
                "12:00PM-1:00PM",
                "1:00PM-2:00PM",
                "2:00PM-3:00PM",
                "3:00PM-4:00PM",
                "4:00PM-5:00PM",
                "5:00PM-6:00PM",
                "6:00PM-7:00PM"
        );

        // Get a reference to the "BookedList" collection
        CollectionReference bookedTimeSlotsRef = db.collection("BookedList");

        // Loop through the predefined time slots
        for (String timeSlot : predefinedTimeSlots) {
            // Create the document ID for each booking using the format "roomName + selectedDate + timeslot"
            String documentId = roomName + " + " + selectedDate + " + " + timeSlot;

            // Get a reference to the specific document using the unique identifier
            DocumentReference timeSlotDocRef = bookedTimeSlotsRef.document(documentId);

            // Query the document to check if it exists
            timeSlotDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // The time slot is already booked, add it to the modelList with status "Booked"
                        modelList.add(new TimeSlot(timeSlot, "Booked"));
                    } else {
                        // The time slot is available, add it to the modelList with status "Available"
                        modelList.add(new TimeSlot(timeSlot, "Available"));
                    }

                    // Check if all queries are completed
                    if (modelList.size() == predefinedTimeSlots.size()) {
                        // Sort the modelList based on the timeslot
                        Collections.sort(modelList, new Comparator<TimeSlot>() {
                            SimpleDateFormat sdf = new SimpleDateFormat("h:mma", Locale.US);

                            @Override
                            public int compare(TimeSlot ts1, TimeSlot ts2) {
                                try {
                                    Date time1 = sdf.parse(ts1.getTimeslot().split("-")[0]);
                                    Date time2 = sdf.parse(ts2.getTimeslot().split("-")[0]);
                                    return time1.compareTo(time2);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return 0;
                            }
                        });

                        // Initialize the TimeSlotAdapter with the populated modelList
                        adapterTimeSlot = new TimeSlotAdapter(TimeSlotActivity.this, modelList);

                        // Set the adapter to the gridViewTimeSlot
                        gridViewTimeSlot.setAdapter(adapterTimeSlot);

                        gridViewTimeSlot.setOnItemClickListener((parent, view, position, id) -> {
                            // Update the selected position and refresh the adapter
                            selectedPosition = position;
                            adapterTimeSlot.setSelectedPosition(position);
                        });
                    }
                } else {
                    Toast.makeText(TimeSlotActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    // Upload booking data to Firestore
    private void uploadData(String roomName, String reason, String selected_date, String timeslot, String userEmail) {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Adding Booking to Database");
        pd.show();

        // Random id for the booking
    //    String id = UUID.randomUUID().toString();
        // Create a unique identifier for each document using the combination of room name, date, and time slot
        String id = roomName + " + " + selectedDate + " + " + timeslot;

        // Create the booking data as a HashMap
        Map<String, Object> doc = new HashMap<>();
    //   doc.put("id", id);
        doc.put("roomName", roomName);
        doc.put("reason", reason);
        doc.put("date", selected_date);
        doc.put("slot", timeslot);
        doc.put("user", userEmail );

        // Get a reference to the "Room Booking" collection
        CollectionReference bookedTimeSlotsRef = db.collection("BookedList");


        // Add the booking data to the "Bookings" subcollection
        bookedTimeSlotsRef.document(id).set(new Bookings(roomName, reason, selected_date, timeslot))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            // Success, booking added to Firestore
                            Toast.makeText(TimeSlotActivity.this, "Booking added successfully!", Toast.LENGTH_SHORT).show();

                            // Call a method to update the Firestore document for the selected time slot as booked
                          //  updateSelectedTimeSlotStatus(selected_date, timeslot);

                            // Create an explicit intent to launch the SuccessBookingActivity
                            Intent successBookingIntent = new Intent(TimeSlotActivity.this, SuccessBookingActivity.class);

                            // Pass the booking details as extras to the SuccessBookingActivity
                            successBookingIntent.putExtra("roomName", roomName);
                            successBookingIntent.putExtra("reason", reason);
                            successBookingIntent.putExtra("selectedDate", selected_date);
                            successBookingIntent.putExtra("timeSlot", timeslot);

                            // Start the SuccessBookingActivity
                            startActivity(successBookingIntent);
                        } else {
                            // Failed to add booking
                            Toast.makeText(TimeSlotActivity.this, "Failed to add booking", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(TimeSlotActivity.this, "Failed to add booking", Toast.LENGTH_SHORT).show();
                    }
                });
    }



}




