package com.example.bookingftsm_a190647;

import java.util.Objects;

public class Bookings {

    private String roomName;
    private String reason;
    private String date;
    private String timeSlot;
    private String user;

    public Bookings(String roomName, String reason, String date, String timeSlot, String user) {

        this.roomName = roomName;
        this.reason = reason;
        this.date = date;
        this.timeSlot = timeSlot;
        this.user = user;
    }

    public Bookings() {
        // Required empty constructor
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bookings bookings = (Bookings) o;
        return Objects.equals(roomName, bookings.roomName) &&
                Objects.equals(reason, bookings.reason) &&
                Objects.equals(date, bookings.date) &&
                Objects.equals(timeSlot, bookings.timeSlot) &&
                Objects.equals(user, bookings.user);

    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName, reason, date, timeSlot,user);
    }

}