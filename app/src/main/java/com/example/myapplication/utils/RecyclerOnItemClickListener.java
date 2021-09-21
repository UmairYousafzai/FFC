package com.example.myapplication.utils;

import android.view.View;

import com.example.myapplication.DoctorModel;

public interface RecyclerOnItemClickListener {
    void GetDoctorOnClick(DoctorModel doctorModel, int position, int check);
}
