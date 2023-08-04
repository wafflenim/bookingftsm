package com.example.bookingftsm_a190647;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class HistoryViewHolder extends RecyclerView.ViewHolder {

    //  ImageView imageView;
    public TextView historyTujuan;
    public TextView historyRuang;
    public TextView historyTarikh;
    public TextView historyMasa;


    View mView;

    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getAdapterPosition());
                return true;
            }
        });


        historyTujuan = itemView.findViewById(R.id.textViewReason);
        historyRuang = itemView.findViewById(R.id.textViewRoomName);
        historyTarikh = itemView.findViewById(R.id.textViewDate);
        historyMasa = itemView.findViewById(R.id.textViewTimeSlot);

    }

    private MyViewHolder.ClickListener mClickListener;

    //interface for click listener
    public  interface  ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);

        void onBindViewHolder(@NonNull MyViewHolder holder, int position);

        int getItemCount();
    }
    public void setOnClickListener(MyViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }
}