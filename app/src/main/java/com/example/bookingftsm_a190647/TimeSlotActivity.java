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

    EditText inputTujuan;
    RecyclerView recyclerViewTimeSlot;
    FirebaseRecyclerOptions<TimeSlot> options;
    FirebaseRecyclerAdapter<TimeSlot, MyViewHolder> adapter;
    CollectionReference DataRef;
    RecyclerView.LayoutManager layoutManager;

    List<TimeSlot> modelList = new ArrayList<>();
    private int selectedPosition = -1;

    GridView gridViewTimeSlot;
    //firestore instance
    FirebaseFirestore db;
    TimeSlotAdapter adapterTimeSlot;
    //Calendar calendar;
    DatePickerDialog datePicker;
    TextView selectDate;
    private String date;
    private String timeslot;

    SimpleDateFormat dateFormat;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_slot);

        Button btnKembali = findViewById(R.id.btn_kembali);
        Button btnTempah = findViewById(R.id.btn_tempah);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchRoomActivity.class);
                startActivity(intent);
            }
        });
        //select date
        selectDate = (TextView) findViewById(R.id.date); //SIMPAN DALAM FIREBASE
        dateFormat = new SimpleDateFormat("dd_MM_yyyy");

        // Retrieve the room name from the intent extras
        String roomName = getIntent().getStringExtra("roomName");

        // Now you can use the roomName variable to display the room name in the activity as needed
        TextView tvRoomName = findViewById(R.id.nama_bilik);
        tvRoomName.setText(roomName);

        inputTujuan = findViewById(R.id.et_tujuan);

        db = FirebaseFirestore.getInstance();

        pd = new ProgressDialog(this);
        //open calendar
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance(); //fetch calendar object
                int year_m = calendar.get(Calendar.YEAR); //current year
                int month_m = calendar.get(Calendar.MONTH); //current month
                int day_m = calendar.get(Calendar.DAY_OF_MONTH); //CURRENT DAY

                //date picker dialog
                datePicker = new DatePickerDialog(TimeSlotActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        selectDate.setText(day + "/" + (month + 1) + "/" + year);

                    }
                }, year_m, month_m, day_m);
                datePicker.show();
                //pd.setTitle("Memuat turun kalendar");
                //   pd.show();
                showTimeSlot(roomName, "");

            }
        });
//      set grid view
        gridViewTimeSlot = findViewById(R.id.gridViewTimeSlot);

        layoutManager = new LinearLayoutManager(this);
        gridViewTimeSlot.setNumColumns(2);
        gridViewTimeSlot.setHorizontalSpacing(8);
        gridViewTimeSlot.setVerticalSpacing(8);
        gridViewTimeSlot.setPadding(8, 8, 8, 8);

        btnTempah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //masuk data dlm db
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

                    // Call the function to upload
                    uploadData(roomName, reason, selected_date, timeSlot);
                } else {
                    Toast.makeText(TimeSlotActivity.this, "Please select a time slot.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

        //String data == the text in the search input. if "" then just display all
        private void showTimeSlot(String roomName, String text) {
        //    modelList.clear(); //ini tak sure lagi

            // Get a reference to the "Room Booking" collection
            CollectionReference dataRef  = db.collection("Timeslot");

            // Use the whereGreaterThanOrEqualTo and whereLessThanOrEqualTo methods for partial search
            Query query = dataRef;

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        TimeSlot model = new TimeSlot(doc.getString("timeslot"), doc.getString("status"));
                        modelList.add(model);
                    }
                    // Sort the modelList based on the timeslot
                    Collections.sort(modelList, new Comparator<TimeSlot>() {
                        SimpleDateFormat sdf = new SimpleDateFormat("h.mma", Locale.US);

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

                    gridViewTimeSlot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // Update the selected position and refresh the adapter
                            selectedPosition = position;
                            adapterTimeSlot.setSelectedPosition(position);
                        }
                    });


                } else {
                    Toast.makeText(TimeSlotActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });
        }

    private void uploadData(String roomName, String reason, String selected_date, String timeslot) {

        pd.setTitle("Adding Booking to Database");
        pd.show();
        //random id
        String id = UUID.randomUUID().toString();

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("roomName", roomName);
        doc.put("reason", reason);
        doc.put("date", selected_date);
        doc.put("slot", timeslot);

        // Get a reference to the "Room Booking" collection
        CollectionReference roomBookingRef = db.collection("Room Booking");

        // Get a reference to the specific room's document using the roomName
        DocumentReference roomDocRef = roomBookingRef.document(roomName);

        // Get a reference to the "Bookings" subcollection under the specific room's document
        CollectionReference bookingsRef = roomDocRef.collection("Bookings");
        // Get a reference to the specific room's subcollection using the roomName
        bookingsRef.document(id).set(new Bookings(id, roomName, reason, selected_date, timeslot))
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {

                            Toast.makeText(TimeSlotActivity.this, "Booking added successfully!", Toast.LENGTH_SHORT).show();
                            // Call a method to update the Firestore document for the selected time slot as booked
                            updateSelectedTimeSlotStatus(selected_date, timeslot);
                        } else {
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



        // Create a new booking document in the subcollection with the given id and data
    }

    private void updateSelectedTimeSlotStatus(String selected_date, String timeslot) {
        // Get a reference to the "Timeslot" collection
        CollectionReference timeSlotRef = db.collection("Timeslot");

        // Query for the specific time slot document using the selected_date and timeslot
        Query query = timeSlotRef.whereEqualTo("date", selected_date).whereEqualTo("timeslot", timeslot);

        // Execute the query
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Check if any document matches the query
                    if (!task.getResult().isEmpty()) {
                        // Get the first document (assuming there is only one matching document)
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Update the "status" field of the document to "booked"
                        document.getReference().update("status", "booked")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            // Success, document updated
                                            // Now notify the adapter to refresh the view and update the appearance
                                            adapterTimeSlot.notifyDataSetChanged();
                                        } else {
                                            // Failed to update the document
                                            Toast.makeText(TimeSlotActivity.this, "Failed to update time slot status", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        // No matching documents found
                        Toast.makeText(TimeSlotActivity.this, "Time slot document not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Failed to execute the query
                    Toast.makeText(TimeSlotActivity.this, "Error fetching time slot data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}