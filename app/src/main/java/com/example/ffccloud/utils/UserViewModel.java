package com.example.ffccloud.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.ffccloud.ModelClasses.ClassificationModel;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.ModelClasses.QualificationModel;

import java.util.List;

public class UserViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private final LiveData<List<QualificationModel>> allQualification;
    private final LiveData<List<ClassificationModel>> allClassification;
    private final  LiveData<List<GradingModel>> allGrades;
    private final LiveData<List<LocationRequestedUser>> allUsers;


    public UserViewModel(@NonNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allClassification = userRepository.getAllClassification();
        allGrades = userRepository.getAllGrades();
        allQualification = userRepository.getAllQualification();
        allUsers = userRepository.getAllUser();
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


    public void insertUser(LocationRequestedUser user)
    {
        userRepository.insertUser(user);
    }

    public void deleteAllUsers()
    {
        userRepository.deleteAllUser();
    }


    public void deleteUser(LocationRequestedUser user)
    {
        userRepository.deleteUser(user);
    }
    public LiveData<List<LocationRequestedUser>> getAllUser( )
    {
        return userRepository.getAllUser();
    }

}
