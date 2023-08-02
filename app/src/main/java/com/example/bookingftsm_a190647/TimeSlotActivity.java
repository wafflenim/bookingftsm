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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.CollectionReference;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
    RecyclerView mRecyclerView;

    //layout manager for recycler view
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

        recyclerViewTimeSlot = findViewById(R.id.recyclerViewTimeSlot);
        //set recycler view properties
        layoutManager = new LinearLayoutManager(this);
        recyclerViewTimeSlot.setLayoutManager(layoutManager);
        recyclerViewTimeSlot.setHasFixedSize(true);

        Button btnKembali = findViewById(R.id.btn_kembali);
        Button btnTempah = findViewById(R.id.btn_tempah);

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
                showData(roomName, "");

            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchRoomActivity.class);
                startActivity(intent);
            }
        });

        btnTempah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //masuk data dlm db
                String roomName = tvRoomName.getText().toString().trim();
                String reason = inputTujuan.getText().toString().trim();

                //call function to upload
                //   uploadData(roomName, reason, selected_date, timeSlot);
            }
        });
    }

        //String data == the text in the search input. if "" then just display all
        private void showData (String roomName, String text){


            // Get a reference to the "Room Booking" collection
            CollectionReference dataRef  = db.collection("Timeslot");

            // Use the whereGreaterThanOrEqualTo and whereLessThanOrEqualTo methods for partial search
            Query query = dataRef;

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        TimeSlot model = new TimeSlot(
                                doc.getString("timeslot"),
                                doc.getString("status")
                        );

                        modelList.add(model);
                    }

                        adapterTimeSlot = new TimeSlotAdapter(TimeSlotActivity.this, modelList);
                        recyclerViewTimeSlot.setAdapter(adapterTimeSlot);
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

        // Get a reference to the "Room Bookings" collection
        CollectionReference roomBookingsRef = db.collection("Room Bookings");

        // Get a reference to the specific room's subcollection using the roomName
        CollectionReference roomSubcollectionRef = roomBookingsRef.document(roomName).collection(roomName);

        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("roomName", roomName);
        doc.put("reason", reason);
        doc.put("date", selected_date);
        doc.put("slot", timeslot);

        // Create a new booking document in the subcollection
    //    roomSubcollectionRef.document(id).set(new Bookings(id, roomName, reason, selected_date, timeSlot))
         //      .addOnCompleteListener(new OnCompleteListener<Void>() {
            //        @Override
            //        public void onComplete(@NonNull Task<Void> task) {
            //            pd.dismiss();
            //            if (task.isSuccessful()) {
            //                Toast.makeText(TimeSlotActivity.this, "Booking added successfully!", Toast.LENGTH_SHORT).show();
             //           } else {
             //               Toast.makeText(TimeSlotActivity.this, "Failed to add booking", Toast.LENGTH_SHORT).show();
             //           }
            //        }
           //     });
    }


}