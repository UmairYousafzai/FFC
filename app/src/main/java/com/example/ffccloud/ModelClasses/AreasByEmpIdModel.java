package com.example.ffccloud.ModelClasses;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class AreasByEmpIdModel implements Parcelable {

    @SerializedName("Sub_Head_Code")
    int areaId;
    @SerializedName("Sub_Head_Desc")
    String areaTittle;

    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    protected AreasByEmpIdModel(Parcel in) {
        areaId = in.readInt();
        areaTittle = in.readString();
    }

    public static final Creator<AreasByEmpIdModel> CREATOR = new Creator<AreasByEmpIdModel>() {
        @Override
        public AreasByEmpIdModel createFromParcel(Parcel in) {
            return new AreasByEmpIdModel(in);
        }

        @Override
        public AreasByEmpIdModel[] newArray(int size) {
            return new AreasByEmpIdModel[size];
        }
    };

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getAreaTittle() {
        return areaTittle;
    }

    public void setAreaTittle(String areaTittle) {
        this.areaTittle = areaTittle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(areaId);
        dest.writeString(areaTittle);
    }
}
