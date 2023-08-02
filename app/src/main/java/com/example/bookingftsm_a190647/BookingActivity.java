package com.example.bookingftsm_a190647;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormatSymbols;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BookingActivity extends AppCompatActivity {

    private EditText reasonEditText, dateEditText, startTimeEditText, endTimeEditText;
    private TextView roomNameTV, selectedDayTV, bookingIDTV;
    private Button submitBtn;

    FirebaseFirestore db;

    private Calendar calendar;
    private SimpleDateFormat dateFormatter, timeFormatter, dayFormatter;
    private DateFormatSymbols malayDateFormatSymbols;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        db = FirebaseFirestore.getInstance();

        roomNameTV = findViewById(R.id.roomNameTV);
        reasonEditText = findViewById(R.id.reasonEditText);
      //  bookingIDTV = findViewById(R.id.bookingIDTV);
        dateEditText = findViewById(R.id.booking_editTextDate);
        selectedDayTV = findViewById(R.id.selectedDayTV);
        startTimeEditText = findViewById(R.id.startTimeEditText);
        endTimeEditText = findViewById(R.id.endTimeEditText);
        submitBtn = findViewById(R.id.submitBtn);

        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        dayFormatter = new SimpleDateFormat("EEEE", Locale.getDefault());
        malayDateFormatSymbols = new DateFormatSymbols(new Locale("ms", "MY")); // Create DateFormatSymbols for Malay


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        startTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialogStart(startTimeEditText);
            }
        });

        endTimeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialogEnd(endTimeEditText);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String roomName = roomNameTV.getText().toString();
                String reason = reasonEditText.getText().toString();
             //   String id = bookingIDTV.getText().toString();
                String date = dateEditText.getText().toString();
                String day = dayFormatter.format(calendar.getTime());
                String startTime = startTimeEditText.getText().toString();
                String endTime = endTimeEditText.getText().toString();
                uploadData(roomName, reason, date, day, startTime, endTime);
            }
        });
    }

    private void uploadData(String roomName, String reason, String date, String day, String startTime, String endTime)
    {
        String id = UUID.randomUUID().toString();
        Map<String, Object> doc = new HashMap<>();
        doc.put("id", id);
        doc.put("roomName", roomName);
        doc.put("reason", reason);
        doc.put("date", date);
        doc.put("day", day);
        doc.put("startTime", startTime);
        doc.put("endTime", endTime);

        db.collection("Bookings").document(id).set(doc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Toast.makeText(BookingActivity.this, "Booking uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(BookingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String selectedDate = dateFormatter.format(calendar.getTime());
                String selectedDay = getLocalizedDayName(calendar.get(Calendar.DAY_OF_WEEK));
                //klau english, ambik yg bawah ni
               // String selectedDay = dayFormatter.format(calendar.getTime());
                dateEditText.setText(selectedDate);
                selectedDayTV.setText(selectedDay);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialogStart(final EditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String selectedTime = timeFormatter.format(calendar.getTime());
                startTimeEditText.setText(selectedTime);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void showTimePickerDialogEnd(final EditText editText) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                String selectedTime = timeFormatter.format(calendar.getTime());
                endTimeEditText.setText(selectedTime);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private String getLocalizedDayName(int dayOfWeek) {
        String[] dayNames = malayDateFormatSymbols.getWeekdays();
        return dayNames[dayOfWeek];
    }

    private void onSubmitButtonClicked() {


        // Perform any necessary validation or processing with the input data

        // Store the booking details in the database or perform other operations
    }
}