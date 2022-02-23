package com.example.ffccloud.dashboard.report.viewmodel;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.dashboard.report.adapter.ReportRecyclerAdapter;
import com.example.ffccloud.dashboard.report.repository.ReportFilterRepository;
import com.example.ffccloud.dashboard.report.repository.ReportListRepository;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.LookUpWorkPlan;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.List;

public class ReportListViewModel extends AndroidViewModel {

    private final ReportRecyclerAdapter adapter;
    private final MutableLiveData<String> toastMessage;
    private final MutableLiveData<String> coordinatesMutableLiveData;
    private final MutableLiveData<LookUpWorkPlan> reportMutableLiveData;



    public ReportListViewModel(@NonNull Application application) {
        super(application);

        adapter= new ReportRecyclerAdapter(this);
        toastMessage= new MutableLiveData<>();
        coordinatesMutableLiveData= new MutableLiveData<>();
        reportMutableLiveData= new MutableLiveData<>();
    }

    public void onClick(String coordinates,int key,LookUpWorkPlan report)
    {
        if (key==1)
        {
            this.coordinatesMutableLiveData.setValue(coordinates);

        }
        else if (key==2)
        {
            reportMutableLiveData.setValue(report);
        }
    }

    public MutableLiveData<LookUpWorkPlan> getReportMutableLiveData() {
        return reportMutableLiveData;
    }

    public MutableLiveData<String> getCoordinatesMutableLiveData() {
        return coordinatesMutableLiveData;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public ReportRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void getReports(String fromDate, String toDate, int empId, int statusID )
    {
        String token= SharedPreferenceHelper.getInstance(getApplication()).getToken();
        ReportListRepository.getInstance().getReports(token,fromDate,toDate,empId,statusID);
        getServerResponse();
    }

    public void getServerResponse()
    {
        ReportListRepository.getInstance().setCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {
                if (response!=null)
                {
                    if (key== SERVER_ALL_WORK_PLAN_RESPONSE)
                    {
                        List<LookUpWorkPlan> list = (List<LookUpWorkPlan>) response;

                        adapter.setReportList(list);

                    }
                    else if (key==SERVER_ERROR_RESPONSE)
                    {
                        toastMessage.setValue((String) response);
                    }

                }
            }
        });
    }
}
