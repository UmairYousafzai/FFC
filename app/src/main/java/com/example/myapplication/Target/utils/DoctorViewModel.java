package com.example.myapplication.Target.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.FilteredDoctoredModel;
import com.example.myapplication.ScheduleModel;

import java.util.List;

public class DoctorViewModel extends AndroidViewModel {

    private DoctorRepository doctorRepository;
    private final LiveData<List<FilteredDoctoredModel>> allFilterDoctor;
    private  LiveData<List<ScheduleModel>> allSchedule;


    public DoctorViewModel(@NonNull Application application) {
        super(application);
        doctorRepository = new DoctorRepository(application);
        allFilterDoctor = doctorRepository.getAllFilterDoctor();
        allSchedule = doctorRepository.getAllSchedule();

    }

    public LiveData<List<FilteredDoctoredModel>> getAllQueryFilterDoctor( String doc_classification, String doc_grade)
    {
        return doctorRepository.getQueryFilterDocList(doc_classification,doc_grade);
    }
    public void insertFilterDoctors(List<FilteredDoctoredModel> list)
    {
        doctorRepository.InsertFilterDoctor(list);
    }

    public void deleteAllFilterDoctor()
    {
        doctorRepository.DeleteAllFilterDoctor();
    }

    public void insertSchedule(List<ScheduleModel> list)
    {
        doctorRepository.insertSchedule(list);
    }

    public void deleteAllSchedule()
    {
        doctorRepository.deleteAllSchedule();
    }

    public LiveData<List<ScheduleModel>> getAllschedule( )
    {
        return doctorRepository.getAllSchedule();
    }
    public LiveData<List<FilteredDoctoredModel>> getAllFilterDoctor()
    {
        return allFilterDoctor;
    }


}
