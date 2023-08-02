package com.example.bookingftsm_a190647;

public class Room {

  //  private String ImageURL;
    private String RoomName;
    private String RoomCapacity;
    private String RoomLocation;
    private String Kuliah;
    private String AktivitiKelab;
    private String Mesyuarat;
    private String Makmal;

    public Room(String roomName, String roomCapacity, String roomLocation, String kuliah, String aktivitiKelab, String mesyuarat, String makmal) {
        RoomName = roomName;
        RoomCapacity = roomCapacity;
        RoomLocation = roomLocation;
        Kuliah = kuliah;
        AktivitiKelab = aktivitiKelab;
        Mesyuarat = mesyuarat;
        Makmal = makmal;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getKuliah() {
        return Kuliah;
    }

    public void setKuliah(String kuliah) {
        Kuliah = kuliah;
    }

    public String getAktivitiKelab() {
        return AktivitiKelab;
    }

    public void setAktivitiKelab(String aktivitiKelab) {
        AktivitiKelab = aktivitiKelab;
    }

    public String getMesyuarat() {
        return Mesyuarat;
    }

    public void setMesyuarat(String mesyuarat) {
        Mesyuarat = mesyuarat;
    }

    public String getMakmal() {
        return Makmal;
    }

    public void setMakmal(String makmal) {
        Makmal = makmal;
    }

    public String getRoomCapacity() {
        return RoomCapacity;
    }

    public void setRoomCapacity(String roomCapacity) {
        RoomCapacity = roomCapacity;
    }

    public String getRoomLocation() {
        return RoomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        RoomLocation = roomLocation;
    }



    public Room()
    {

    }
}
