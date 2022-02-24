package com.example.ffccloud.meeting.viewmodel;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_Employee_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_RESPONSE;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.meeting.repository.MeetingRepository;
import com.example.ffccloud.model.Employee;
import com.example.ffccloud.model.Meeting;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MeetingViewModel extends AndroidViewModel {

    private final MeetingRepository repository;
    private final MutableLiveData<List<Employee>> employeeMutableLiveData;
    private Meeting meeting;
    private MutableLiveData<Meeting> meetingMutableLiveData;
    private MutableLiveData<String> toastMessage;
    private MutableLiveData<String> serverResponseMessage;


    public MeetingViewModel(@NonNull Application application) {
        super(application);

        repository= new MeetingRepository();
        employeeMutableLiveData = new MutableLiveData<>();
        meetingMutableLiveData = new MutableLiveData<>();
        toastMessage = new MutableLiveData<>();
        serverResponseMessage = new MutableLiveData<>();
        meeting=new Meeting();

    }

    public void onClick()
    {
        if (meeting!=null)
        {
            meetingMutableLiveData.setValue(meeting);
        }
    }

    public MutableLiveData<String> getServerResponseMessage() {
        return serverResponseMessage;
    }

    public MutableLiveData<Meeting> getMeetingMutableLiveData() {
        return meetingMutableLiveData;
    }

    public void setMeetingMutableLiveData(MutableLiveData<Meeting> meetingMutableLiveData) {
        this.meetingMutableLiveData = meetingMutableLiveData;
    }



    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public MutableLiveData<List<Employee>> getEmployeeMutableLiveData() {
        return employeeMutableLiveData;
    }

    public void getEmployees()
    {
        String token = SharedPreferenceHelper.getInstance(getApplication()).getToken();

        repository.getAllEmployees(token);
        getServerResponse();
    }

    public void saveMeeting(Meeting meeting)
    {
        String token= SharedPreferenceHelper.getInstance(getApplication()).getToken();

        repository.saveMeeting(token,meeting);
        getServerResponse();

    }

    private void getServerResponse() {

        repository.setCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {

                if (response!=null)
                {
                    if (key== SERVER_Employee_RESPONSE)
                    {
                        List<Employee> list= (List<Employee>) response;
                        employeeMutableLiveData.setValue(list);
                    }  else if (key==SERVER_ERROR_RESPONSE)
                    {

                        toastMessage.setValue((String) response);
                    }
                    else if (key==SERVER_RESPONSE)
                    {

                        serverResponseMessage.setValue((String) response);

                    }
                }

            }
        });

    }



}
