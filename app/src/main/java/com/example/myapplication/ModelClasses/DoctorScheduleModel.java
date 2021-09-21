package com.example.myapplication.ModelClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DoctorScheduleModel implements Parcelable {


    @SerializedName("DoctorSchedules_Id")
    public long scheduleId;
    @SerializedName("Primary_Loc")
    public boolean Primary_Loc;
    @SerializedName("Ac_No")
    public long Ac_No;
    @SerializedName("Sub_Head_Code")
    public long Sub_Head_Code;
    @SerializedName("Day_Id")
    public int Day_Id;
    @SerializedName("Opening_Time")
    public String Opening_Time;
    @SerializedName("Closing_Time")
    public String Closing_Time;
    @SerializedName("Coordinates")
    public String Coordinates;

    public DoctorScheduleModel(){

    }

    protected DoctorScheduleModel(Parcel in) {
        scheduleId = in.readLong();
        Primary_Loc = in.readByte() != 0;
        Ac_No = in.readLong();
        Sub_Head_Code = in.readLong();
        Day_Id = in.readInt();
        Opening_Time = in.readString();
        Closing_Time = in.readString();
        Coordinates = in.readString();
    }

    public static final Creator<DoctorScheduleModel> CREATOR = new Creator<DoctorScheduleModel>() {
        @Override
        public DoctorScheduleModel createFromParcel(Parcel in) {
            return new DoctorScheduleModel(in);
        }

        @Override
        public DoctorScheduleModel[] newArray(int size) {
            return new DoctorScheduleModel[size];
        }
    };

    public long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public boolean isPrimary_Loc() {
        return Primary_Loc;
    }

    public void setPrimary_Loc(boolean primary_Loc) {
        Primary_Loc = primary_Loc;
    }

    public long getAc_No() {
        return Ac_No;
    }

    public void setAc_No(long ac_No) {
        Ac_No = ac_No;
    }

    public long getSub_Head_Code() {
        return Sub_Head_Code;
    }

    public void setSub_Head_Code(long sub_Head_Code) {
        Sub_Head_Code = sub_Head_Code;
    }

    public int getDay_Id() {
        return Day_Id;
    }

    public void setDay_Id(int day_Id) {
        Day_Id = day_Id;
    }

    public String getOpening_Time() {
        return Opening_Time;
    }

    public void setOpening_Time(String opening_Time) {
        Opening_Time = opening_Time;
    }

    public String getClosing_Time() {
        return Closing_Time;
    }

    public void setClosing_Time(String closing_Time) {
        Closing_Time = closing_Time;
    }

    public String getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(String coordinates) {
        Coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(scheduleId);
        dest.writeByte((byte) (Primary_Loc ? 1 : 0));
        dest.writeLong(Ac_No);
        dest.writeLong(Sub_Head_Code);
        dest.writeInt(Day_Id);
        dest.writeString(Opening_Time);
        dest.writeString(Closing_Time);
        dest.writeString(Coordinates);
    }
}
