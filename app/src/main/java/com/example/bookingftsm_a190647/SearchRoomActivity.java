package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookingftsm_a190647.adapter.AdapterSearchRoom;
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

import java.util.ArrayList;
import java.util.List;

public class SearchRoomActivity extends AppCompatActivity {

    EditText inputSearch;
    RecyclerView recyclerViewRoom;
    FirebaseRecyclerOptions<Room> options;
    FirebaseRecyclerAdapter<Room, MyViewHolder> adapter;
    CollectionReference DataRef;
    RecyclerView.LayoutManager layoutManager;

    List<Room> modelList = new ArrayList<>();
    RecyclerView mRecyclerView;

    //layout manager for recycler view
    //firestore instance
    FirebaseFirestore db;
    AdapterSearchRoom adapterSearchRoom;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_room);
        inputSearch = findViewById(R.id.action_search);

        String selectedRoom = inputSearch.getText().toString();

        db = FirebaseFirestore.getInstance();

        recyclerViewRoom = findViewById(R.id.recyclerViewRoom);
        //set recycler view properties
        layoutManager = new LinearLayoutManager(this);
        recyclerViewRoom.setLayoutManager(layoutManager);
        recyclerViewRoom.setHasFixedSize(true);

        pd = new ProgressDialog(this);
        pd.setTitle("Sedang memuat turun senarai bilik....");
        pd.show();

        showData(""); //show data without any search
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
            if (editable.toString()!=null)
            {
                showData(editable.toString()); //show data based on search
            }
            else
            {
                showData("");
            }
            }
        });

    }
    //String data == the text in the search input. if "" then just display all
    private void showData(String searchText) {


        // Get a reference to the "Room Booking" collection
        CollectionReference roomBookingRef = db.collection("Room Booking");

        // Use the whereGreaterThanOrEqualTo and whereLessThanOrEqualTo methods for partial search
        Query query = roomBookingRef.whereGreaterThanOrEqualTo("name", searchText)
                .whereLessThanOrEqualTo("name", searchText + "\uf8ff");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                modelList.clear();
                pd.dismiss();

                if (task.isSuccessful()) {
                    for (DocumentSnapshot doc : task.getResult()) {
                        Room model = new Room(
                                doc.getString("name"),
                                doc.getString("location"),
                                doc.getString("capacity"),
                                doc.getString("Mesyuarat"),
                                doc.getString("Makmal"),
                                doc.getString("Kuliah"),
                                doc.getString("Aktiviti Kelab")
                        );

                        modelList.add(model);
                    }

                    adapterSearchRoom = new AdapterSearchRoom(SearchRoomActivity.this, modelList);
                    recyclerViewRoom.setAdapter(adapterSearchRoom);
                } else {
                    Toast.makeText(SearchRoomActivity.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}