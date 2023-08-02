package com.example.bookingftsm_a190647;

public class TimeSlot {


    public String timeslot;
    private String status;

    public TimeSlot() {
    }

    public TimeSlot(String timeslot, String status) {
        this.timeslot = timeslot;
        this.status = status;
    }

    public String getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(String timeslot) {
        this.timeslot = timeslot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
