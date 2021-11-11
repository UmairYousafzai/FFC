package com.example.ffccloud.ModelClasses;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activity")
public class Activity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Activity_id")
    private int activityId;

    @ColumnInfo(name = "Main_Activity")
    private String mainActivity;

    @ColumnInfo(name = "Sub_Activity")
    private String subActivity;

    @ColumnInfo(name = "Task_Id")
    private int taskID;

    @ColumnInfo(name = "Start_DateTime")
    private String startDateTime;

    @ColumnInfo(name = "End_DateTime")
    private String endDateTime;

    @ColumnInfo(name = "Total_Time")
    private String totalTime;

    @ColumnInfo (name="Start_Cord")
    private String startCoordinates;

    @ColumnInfo(name = "End_Cord")
    private String endCoordinates;

    private String distance;

    private String startAddress;

    private String endAddress;
    public Activity() {
    }


    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }

    public String getSubActivity() {
        return subActivity;
    }

    public void setSubActivity(String subActivity) {
        this.subActivity = subActivity;
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getStartCoordinates() {
        return startCoordinates;
    }

    public void setStartCoordinates(String startCoordinates) {
        this.startCoordinates = startCoordinates;
    }

    public String getEndCoordinates() {
        return endCoordinates;
    }

    public void setEndCoordinates(String endCoordinates) {
        this.endCoordinates = endCoordinates;
    }
}
