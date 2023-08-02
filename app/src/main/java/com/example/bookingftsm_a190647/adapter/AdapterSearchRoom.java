package com.example.bookingftsm_a190647.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingftsm_a190647.MyViewHolder;
import com.example.bookingftsm_a190647.R;
import com.example.bookingftsm_a190647.Room;
import com.example.bookingftsm_a190647.SearchRoomActivity;
import com.example.bookingftsm_a190647.TimeSlotActivity;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class AdapterSearchRoom extends RecyclerView.Adapter<MyViewHolder>{
        SearchRoomActivity listActivity;
        List<Room> modelList;
        Context context;

    public AdapterSearchRoom(SearchRoomActivity listActivity, List<Room> modelList) {
        this.listActivity = listActivity;
        this.modelList = modelList;
        }


@NonNull
@Override
public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View itemView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.room_model_layout,parent,false);

        MyViewHolder myViewHolder = new MyViewHolder(itemView);


        //handle item click here
        myViewHolder.setOnClickListener(new MyViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String name = modelList.get(position).getRoomName();
                String location = modelList.get(position).getRoomLocation();
                String capacity = modelList.get(position).getRoomCapacity();
                String kuliah = modelList.get(position).getKuliah();
                String kelab = modelList.get(position).getAktivitiKelab();
                String mesyuarat = modelList.get(position).getMesyuarat();
                String makmal = modelList.get(position).getMakmal();
                Toast.makeText(listActivity, name +"\n"+location+"\n"+capacity+"\n"+kuliah+"\n"+kelab+"\n"+mesyuarat+"\n"+makmal
                        , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(listActivity, TimeSlotActivity.class);
                intent.putExtra("roomName", name);
                listActivity.startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                String name = modelList.get(position).getRoomName();
                String location = modelList.get(position).getRoomLocation();
                String capacity = modelList.get(position).getRoomCapacity();
                String kuliah = modelList.get(position).getKuliah();
                String kelab = modelList.get(position).getAktivitiKelab();
                String mesyuarat = modelList.get(position).getMesyuarat();
                String makmal = modelList.get(position).getMakmal();
                Toast.makeText(listActivity, name +"\n"+location+"\n"+capacity+"\n"+kuliah+"\n"+kelab+"\n"+mesyuarat+"\n"+makmal+"\n hehehehe"
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
                holder.roomName.setText(modelList.get(position).getRoomName());
                holder.roomLocation.setText(modelList.get(position).getRoomLocation());
                holder.roomCapacity.setText(modelList.get(position).getRoomCapacity());
            //    if (modelList.get(position).getKuliah().equalsIgnoreCase("Kuliah"))
                holder.roomKuliah.setText(modelList.get(position).getKuliah());
                holder.roomAktivitiKelab.setText(modelList.get(position).getAktivitiKelab());
                holder.roomMesyuarat.setText(modelList.get(position).getMesyuarat());
                holder.roomMakmal.setText(modelList.get(position).getMakmal());
            }

            @Override
            public int getItemCount() {
                return modelList.size();
            }
        });


    return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.roomName.setText(modelList.get(position).getRoomName());
        holder.roomLocation.setText(modelList.get(position).getRoomLocation());
        holder.roomCapacity.setText(modelList.get(position).getRoomCapacity());
        holder.roomKuliah.setText(modelList.get(position).getKuliah());
        holder.roomAktivitiKelab.setText(modelList.get(position).getAktivitiKelab());
        holder.roomMesyuarat.setText(modelList.get(position).getMesyuarat());
        holder.roomMakmal.setText(modelList.get(position).getMakmal());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }
}

