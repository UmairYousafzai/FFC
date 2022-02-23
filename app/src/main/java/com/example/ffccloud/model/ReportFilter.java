package com.example.ffccloud.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReportFilter implements Parcelable {

    private int empId;
    private String fromDate;
    private String toDate;
    private int statusID;

    public ReportFilter() {
    }

    protected ReportFilter(Parcel in) {
        empId = in.readInt();
        fromDate = in.readString();
        toDate = in.readString();
        statusID = in.readInt();
    }

    public static final Creator<ReportFilter> CREATOR = new Creator<ReportFilter>() {
        @Override
        public ReportFilter createFromParcel(Parcel in) {
            return new ReportFilter(in);
        }

        @Override
        public ReportFilter[] newArray(int size) {
            return new ReportFilter[size];
        }
    };

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(empId);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeInt(statusID);
    }
}
