package com.example.myapplication.Target.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.DoctorModel;

import java.util.ArrayList;
import java.util.List;

public class TargetViewModel extends AndroidViewModel {

    private final TargetRepository repository;
    private final LiveData<List<DoctorModel>> allMorningDoctors;
    private final LiveData<List<DoctorModel>> allEveningDoctors;
    private final LiveData<List<DoctorModel>> allDoctors;
    private  LiveData<List<DoctorModel>> allEveningDoctorsByDate;
    private  LiveData<List<DoctorModel>> allMorningDoctorsByDate;


    public TargetViewModel(@NonNull Application application) {
        super(application);

        repository= new TargetRepository(application);
        allEveningDoctors = repository.getAllEveningDoctors();
        allMorningDoctors = repository.getAllMorningDoctors();
        allDoctors = repository.getAllDoctors();
    }

    public LiveData<List<DoctorModel>> getAllEveningDoctorsByDate(String date) {
        allEveningDoctorsByDate = repository.getAllEveningDoctorsByDate(date);
        return allEveningDoctorsByDate;
    }

    public LiveData<List<DoctorModel>> getAllMorningDoctorsByDate(String date) {
        allMorningDoctorsByDate= repository.getAllMorningDoctorsByDate(date);
        return allMorningDoctorsByDate;
    }

    public void DeleteDoctor(DoctorModel doctorModel)
    {
        repository.DeleteDoctor(doctorModel);
    }
    public void DeleteAllDoctor( )
    {
        repository.DeleteAllDoctor();
    }

    public void UpdateDoctor(DoctorModel doctorModel)
    {
        repository.UpdateDoctor(doctorModel);
    }
    public LiveData<List<DoctorModel>> getAllMorningDoctorLiveData()
    {
        return allMorningDoctors;
    }

    public LiveData<List<DoctorModel>> getAllEveningDoctorLiveData()
    {
        return allEveningDoctors;
    }
    public LiveData<List<DoctorModel>> getAllDoctorLiveData()
    {
        return allDoctors;
    }
}
