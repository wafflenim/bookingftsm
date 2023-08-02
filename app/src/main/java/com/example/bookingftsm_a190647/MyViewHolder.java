package com.example.bookingftsm_a190647;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MyViewHolder extends RecyclerView.ViewHolder {

//  ImageView imageView;
    public TextView roomName;
    public TextView roomLocation;
    public TextView roomCapacity;
    public TextView roomKuliah;
    public TextView roomAktivitiKelab;
    public TextView roomMesyuarat;
    public TextView roomMakmal;

    View mView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        });


        roomName = itemView.findViewById(R.id.tvRoomName);
        roomLocation = itemView.findViewById(R.id.tvRoomLocation);
        roomCapacity = itemView.findViewById(R.id.tvRoomCapacity);
        roomKuliah = itemView.findViewById(R.id.tvKuliah);
        roomAktivitiKelab = itemView.findViewById(R.id.tvAktivitiKelab);
        roomMesyuarat = itemView.findViewById(R.id.tvMesyuarat);
        roomMakmal = itemView.findViewById(R.id.tvMakmal);
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
