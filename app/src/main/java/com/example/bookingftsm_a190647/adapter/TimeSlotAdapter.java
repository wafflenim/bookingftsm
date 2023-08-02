package com.example.bookingftsm_a190647.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingftsm_a190647.Common;
import com.example.bookingftsm_a190647.MyViewHolder;
import com.example.bookingftsm_a190647.R;
import com.example.bookingftsm_a190647.Room;
import com.example.bookingftsm_a190647.SearchRoomActivity;
import com.example.bookingftsm_a190647.TimeSlot;
import com.example.bookingftsm_a190647.TimeSlotActivity;
import com.example.bookingftsm_a190647.TimeSlotViewHolder;

import java.util.ArrayList;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotViewHolder>{

    TimeSlotActivity cardViewList;
    List<TimeSlot> modelList;


    public TimeSlotAdapter(TimeSlotActivity cardviewList, List<TimeSlot> modelList) {
       this.cardViewList = cardviewList;
       this.modelList = modelList;
    }


    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slot_model_layout,parent,false);
        TimeSlotViewHolder MyViewHolder = new TimeSlotViewHolder(itemView);


        MyViewHolder.setOnClickListener(new TimeSlotViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
                holder.timeSlot.setText(modelList.get(position).getTimeslot());
                holder.status.setText(modelList.get(position).getStatus());
            }

            @Override
            public int getItemCount() {
                return modelList.size();
            }
        });

        return MyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        holder.timeSlot.setText(modelList.get(position).getTimeslot());
        holder.status.setText(modelList.get(position).getStatus());

    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView txt_time_slot, txt_time_slot_description;
//        CardView card_time_slot;
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            card_time_slot = (CardView)itemView.findViewById(R.id.card_time_slot);
//            txt_time_slot = (TextView)itemView.findViewById(R.id.txt_time_slot);
//            txt_time_slot_description = (TextView)itemView.findViewById(R.id.txt_time_slot_description);
//        }
//    }
}




