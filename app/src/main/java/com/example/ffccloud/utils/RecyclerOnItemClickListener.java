package com.example.ffccloud.utils;

import com.example.ffccloud.DoctorModel;

public interface RecyclerOnItemClickListener {
    void GetDoctorOnClick(DoctorModel doctorModel, int position, int check);
}
