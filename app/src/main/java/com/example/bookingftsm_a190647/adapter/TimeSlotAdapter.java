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

    Context context;
    List<TimeSlot> modelList;
    List<CardView> cardViewList;


    public TimeSlotAdapter(Context context, List<TimeSlot> modelList) {
        this.context = context;
        this.modelList = modelList;
        cardViewList = new ArrayList<>();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.slot_model_layout, parent, false);
        }

        TextView txtTimeSlot = convertView.findViewById(R.id.txt_time_slot);
        TextView txtTimeSlotDescription = convertView.findViewById(R.id.txt_time_slot_description);

        TimeSlot timeSlot = modelList.get(position);
        txtTimeSlot.setText(timeSlot.getTimeslot());

        if (modelList.size() == 0) {
            txtTimeSlotDescription.setText("Available");
            convertView.setEnabled(true);
            convertView.setBackgroundResource(android.R.color.holo_red_light);
        } else {
            boolean isBooked = false;
            for (TimeSlot slotValue : modelList) {
                String slot = slotValue.getTimeslot();
                if (slot.equals(position)) {
                    isBooked = true;
                    break;
                }
            }
            if (isBooked) {
                txtTimeSlotDescription.setText("Booked");
                convertView.setEnabled(false);
                convertView.setBackgroundResource(android.R.color.darker_gray);
            } else {
                txtTimeSlotDescription.setText("Available");
                convertView.setEnabled(true);
                convertView.setBackgroundResource(android.R.color.holo_red_light);
            }
        }

        return convertView;
    }
}








