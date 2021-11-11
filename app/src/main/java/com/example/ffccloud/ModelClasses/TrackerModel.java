package com.example.ffccloud.ModelClasses;

public class TrackerModel {

    private int id;
    private String time;
    private String coordinates;
    private String date;


    public TrackerModel(int id, String time, String coordinates, String date) {
        this.id = id;
        this.time = time;
        this.coordinates = coordinates;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
