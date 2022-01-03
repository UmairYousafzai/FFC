package com.example.ffccloud.Target.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.DoctorModel;

import java.util.List;

public class TargetViewModel extends AndroidViewModel {

    private final TargetRepository repository;
    private final LiveData<List<DoctorModel>> allMorningDoctors;
    private final LiveData<List<DoctorModel>> allEveningDoctors;
    private final LiveData<List<DoctorModel>> allDoctors;


    public TargetViewModel(@NonNull Application application) {
        super(application);

        repository= new TargetRepository(application);
        allEveningDoctors = repository.getAllEveningDoctors();
        allMorningDoctors = repository.getAllMorningDoctors();
        allDoctors = repository.getAllDoctors();
    }

    public LiveData<List<DoctorModel>> getAllEveningDoctorsByDate(String date) {
        return repository.getAllEveningDoctorsByDate(date);
    }

    public LiveData<List<DoctorModel>> getAllMorningDoctorsByDate(String date) {
        return repository.getAllMorningDoctorsByDate(date);
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
