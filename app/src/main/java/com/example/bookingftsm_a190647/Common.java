package com.example.bookingftsm_a190647;

public class Common {

    public static final Object DISABLE_TAG = "DISABLE";

    public static String convertTimeSlotToString(int slot)
{
    switch (slot)
    {
        case 0:
            return "7:00AM-8:00AM";
        case 1:
            return "8:00AM-9:00AM";
        case 2:
            return "9:00AM-10:00AM";
        case 3:
            return "10:00AM-11:00AM";
        case 4:
            return "11:00AM-12:00PM";
        case 5:
            return "12:00PM-1:00PM";
        case 6:
            return "1:00PM-2:00PM";
        case 7:
            return "2:00PM-3:00PM";
        case 8:
            return "3:00PM-4:00PM";
        case 9:
            return "4:00PM-5:00PM";
        case 10:
            return "5:00PM-6:00PM";
        case 11:
            return "6:00PM-7:00PM";
        default:
            return "Locked";
    }
}
}
