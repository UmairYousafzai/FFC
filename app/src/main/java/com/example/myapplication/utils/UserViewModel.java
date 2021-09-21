package com.example.myapplication.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.ModelClasses.ClassificationModel;
import com.example.myapplication.ModelClasses.GradingModel;
import com.example.myapplication.ModelClasses.QualificationModel;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private final LiveData<List<QualificationModel>> allQualification;
    private final LiveData<List<ClassificationModel>> allClassification;
    private final  LiveData<List<GradingModel>> allGrades;

    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allClassification = userRepository.getAllClassification();
        allGrades = userRepository.getAllGrades();
        allQualification = userRepository.getAllQualification();
    }

    public void deleteAllClassification()
    {
        userRepository.DeleteAllClassification();
    }

    public void deleteAllGrades()
    {
        userRepository.DeleteAllGrades();
    }
    public void deleteAllQualification()
    {
        userRepository.DeleteAllQualification();
    }

    public LiveData<List<ClassificationModel>> getAllClassification()
    {
        return allClassification;
    }

    public LiveData<List<GradingModel>> getAllGrades()
    {
        return allGrades;
    }
    public LiveData<List<QualificationModel>> getAllQualification()
    {
        return allQualification;
    }
}
