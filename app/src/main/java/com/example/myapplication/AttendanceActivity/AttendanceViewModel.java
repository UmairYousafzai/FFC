package com.example.myapplication.AttendanceActivity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.myapplication.utils.ActivityRepository;

import java.util.List;


public class AttendanceViewModel extends AndroidViewModel {

    private final AttendanceRepository repository;
    private final LiveData<List<AttendanceModel>> allAttendance;


    public AttendanceViewModel(@NonNull Application application) {
        super(application);
        repository = new AttendanceRepository(application);
        allAttendance = repository.getAllAttendance();

    }

    public LiveData<List<AttendanceModel>> getAllAttendance( )
    {
        return allAttendance;
    }

    public void insertAttendance(AttendanceModel attendanceModel) {
        repository.insertAttendance(attendanceModel);
    }

    public void deleteAttendance(AttendanceModel attendanceModel) {
        repository.deleteAttendance(attendanceModel);
    }
    public void deleteAllAttendance( ) {
        repository.deleteAllAttendance();
    }
}
