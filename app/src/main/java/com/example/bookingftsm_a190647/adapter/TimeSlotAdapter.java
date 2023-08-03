package com.example.bookingftsm_a190647.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.bookingftsm_a190647.Common;
import com.example.bookingftsm_a190647.R;
import com.example.bookingftsm_a190647.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends BaseAdapter {

    private Context context;
    private List<TimeSlot> modelList;
    private int selectedPosition = -1; // -1 means no time slot is selected initially

    public TimeSlotAdapter(Context context, List<TimeSlot> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // Define the OnItemClickListener interface
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Method to set the selected position
    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged(); // Notify the adapter to refresh the views
    }

    // You can also add a method to get the selected time slot
    public TimeSlot getSelectedTimeSlot() {
        if (selectedPosition != -1) {
            return modelList.get(selectedPosition);
        } else {
            return null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.slot_model_layout, parent, false);
        }

        TextView txtTimeSlot = convertView.findViewById(R.id.txt_time_slot);
        TextView txtTimeSlotDescription = convertView.findViewById(R.id.txt_time_slot_description);

        TimeSlot timeSlot = modelList.get(position);
        String slotValue = Common.convertTimeSlotToString(position);
        txtTimeSlot.setText(slotValue);

        String status = timeSlot.getStatus();

        if (status.equals("Booked")) {
            txtTimeSlotDescription.setText("Booked");
            convertView.setEnabled(false); // Disable booked time slots
            convertView.setBackgroundResource(android.R.color.darker_gray);
        } else {
            txtTimeSlotDescription.setText("Available");
            convertView.setEnabled(true);
            convertView.setBackgroundResource(selectedPosition == position ? R.color.blue : android.R.color.holo_red_light);
        }

        return convertView;
    }


    // Method to update the background color of the time slot based on the booking status
    public void updateTimeSlotBackground(String timeslot, String status) {
        // Find the TimeSlot object with the given timeslot value
        for (TimeSlot timeSlot : modelList) {
            if (timeSlot.getTimeslot().equals(timeslot)) {
                // Update the booking status for the specific time slot
                timeSlot.setStatus(status);
                break;
            }
        }

        // Notify the adapter that the data has changed and refresh the view
        notifyDataSetChanged();
    }
}
