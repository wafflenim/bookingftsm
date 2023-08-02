package com.example.bookingftsm_a190647;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeSlotViewHolder extends RecyclerView.ViewHolder {

    public TextView timeSlot;
    public TextView status;

    View mview;

    public TimeSlotViewHolder(@NonNull View itemView) {
        super(itemView);

        mview = itemView;
        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        timeSlot = itemView.findViewById(R.id.txt_time_slot); //display time slot
        status = itemView.findViewById(R.id.txt_time_slot_description); //display availbility


    }


    private TimeSlotViewHolder.ClickListener mClickListener;

    //interface for click listener
    public  interface  ClickListener{
        void onItemClick(View view, int position);

        void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position);

        int getItemCount();
    }
    public void setOnClickListener(TimeSlotViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}
