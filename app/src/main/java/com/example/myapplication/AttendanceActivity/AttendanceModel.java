package com.example.myapplication.AttendanceActivity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "Attendance")
public class AttendanceModel {

    @PrimaryKey (autoGenerate = true)
    private int attendanceID;

    private String attendanceCoordinates;

    private String dateTime;

    private int empID;

    private String imeiNumber;

    private String attendanceImage;


    public AttendanceModel() {
    }

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public String getAttendanceCoordinates() {
        return attendanceCoordinates;
    }

    public void setAttendanceCoordinates(String attendanceCoordinates) {
        this.attendanceCoordinates = attendanceCoordinates;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getImeiNumber() {
        return imeiNumber;
    }

    public void setImeiNumber(String imeiNumber) {
        this.imeiNumber = imeiNumber;
    }

    public String getAttendanceImage() {
        return attendanceImage;
    }

    public void setAttendanceImage(String attendanceImage) {
        this.attendanceImage = attendanceImage;
    }
}
