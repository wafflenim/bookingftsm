package com.example.bookingftsm_a190647.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingftsm_a190647.Bookings;
import com.example.bookingftsm_a190647.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Context context;
    private List<Bookings> bookingList;
    private ClickListener clickListener;

    public HistoryAdapter(Context context, List<Bookings> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.history_model_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bookings booking = bookingList.get(position);
        holder.textViewRoomName.setText(booking.getRoomName());
        holder.textViewDate.setText(booking.getDate());
        holder.textViewTimeSlot.setText(booking.getTimeSlot());
        holder.textViewReason.setText(booking.getReason());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnItemLongClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRoomName;
        TextView textViewDate;
        TextView textViewTimeSlot;
        TextView textViewReason;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewRoomName = itemView.findViewById(R.id.textViewRoomName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewTimeSlot = itemView.findViewById(R.id.textViewTimeSlot);
            textViewReason = itemView.findViewById(R.id.textViewReason);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) {
                        clickListener.onItemClick(view, getAdapterPosition());
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (clickListener != null) {
                        clickListener.onItemLongClick(view, getAdapterPosition());
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}