package com.example.ffccloud.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DoctorsByAreaIdsModel implements Parcelable {

    @SerializedName("Doctor_Id")
    long id;
    @SerializedName("Name")
    String name;

    private boolean ischecked;

    public DoctorsByAreaIdsModel() {
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    protected DoctorsByAreaIdsModel(Parcel in) {
        id = in.readLong();
        name = in.readString();
    }

    public static final Creator<DoctorsByAreaIdsModel> CREATOR = new Creator<DoctorsByAreaIdsModel>() {
        @Override
        public DoctorsByAreaIdsModel createFromParcel(Parcel in) {
            return new DoctorsByAreaIdsModel(in);
        }

        @Override
        public DoctorsByAreaIdsModel[] newArray(int size) {
            return new DoctorsByAreaIdsModel[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }
}
