package com.example.ffccloud.RoueActivity;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Route_Activity")
    public class RouteActivityModel {


        @PrimaryKey(autoGenerate = true)
        int Activity_id;


        @Nullable
        @ColumnInfo(name = "Main_Activity")
        String Main_Activity;


        @Nullable
        @ColumnInfo(name = "Sub_Activity")
        String Sub_Activity;


        @Nullable
        @ColumnInfo(name = "Task_Id")
        int Task_Id;


        @Nullable
        @ColumnInfo(name = "Start_date_time")
        String Start_date_time;


        @Nullable
        @ColumnInfo(name = "End_date_time")
        String End_date_time;

        @Nullable
        @ColumnInfo(name = "Start_coordinates")
        String Start_coordinates;

        @Nullable
        @ColumnInfo(name = "End_coordinates")
        String End_coordinates;

        @Nullable
        @ColumnInfo(name = "Is_Uploaded")
        Boolean Is_Uploaded;

        public int getActivity_id() {
            return Activity_id;
        }

        public void setActivity_id(int activity_id) {
            Activity_id = activity_id;
        }

        public String getMain_Activity() {
            return Main_Activity;
        }

        public void setMain_Activity(String main_Activity) {
            Main_Activity = main_Activity;
        }

        public String getSub_Activity() {
            return Sub_Activity;
        }

        public void setSub_Activity(String sub_Activity) {
            Sub_Activity = sub_Activity;
        }

        public int getTask_Id() {
            return Task_Id;
        }

        public void setTask_Id(int task_Id) {
            Task_Id = task_Id;
        }

        public String getStart_date_time() {
            return Start_date_time;
        }

        public void setStart_date_time(String start_date_time) {
            Start_date_time = start_date_time;
        }

        public String getEnd_date_time() {
            return End_date_time;
        }

        public void setEnd_date_time(String end_date_time) {
            End_date_time = end_date_time;
        }

        public Boolean getIs_Uploaded() {
            return Is_Uploaded;
        }

        public void setIs_Uploaded(Boolean is_Uploaded) {
            Is_Uploaded= is_Uploaded;
        }

        public String getStart_coordinates() {
            return Start_coordinates;
        }

        public void setStart_coordinates(String start_coordinates) {
            Start_coordinates = start_coordinates;
        }

        public String getEnd_coordinates() {
            return End_coordinates;
        }

        public void setEnd_coordinates(String end_coordinates) {
            End_coordinates = end_coordinates;
        }


    }

