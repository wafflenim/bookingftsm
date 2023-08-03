package com.example.bookingftsm_a190647;

public class Bookings {

    private String roomName;
    private String reason;
    private String date;
    private String timeSlot;

    public Bookings(String roomName, String reason, String date, String timeSlot) {

        this.roomName = roomName;
        this.reason = reason;
        this.date = date;
        this.timeSlot = timeSlot;
    }



    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }
}