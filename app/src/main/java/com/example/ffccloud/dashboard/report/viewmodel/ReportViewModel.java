package com.example.ffccloud.dashboard.report.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class ReportViewModel extends AndroidViewModel {

    private final MutableLiveData<String> coordinatesMutableLiveData;

    public ReportViewModel(@NonNull Application application) {
        super(application);

        coordinatesMutableLiveData= new MutableLiveData<>();
    }

    public void onClick(String coordinates)
    {
        coordinatesMutableLiveData.setValue(coordinates);
    }

    public MutableLiveData<String> getCoordinatesMutableLiveData() {
        return coordinatesMutableLiveData;
    }
}
