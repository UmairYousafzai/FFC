package com.example.ffccloud.dashboard.workplan.viewmodel;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.dashboard.workplan.adapter.AllWorkPlanAdapter;
import com.example.ffccloud.dashboard.workplan.repository.WorkPlanRepository;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.LookUpWorkPlan;
import com.example.ffccloud.model.WorkPlan;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class AllWorkPlanViewModel extends AndroidViewModel {
    private final AllWorkPlanAdapter allWorkPlanAdapter;
    private final MutableLiveData<String> serverErrorLiveData;
    private final MutableLiveData<String> coordinatesMutableLiveData;
    private final MutableLiveData<String> toastMessage;



    public AllWorkPlanViewModel(@NonNull Application application) {
        super(application);
        allWorkPlanAdapter= new AllWorkPlanAdapter(this);
        serverErrorLiveData = new MutableLiveData<>();
        coordinatesMutableLiveData = new MutableLiveData<>();
        toastMessage = new MutableLiveData<>();

    }
    public AllWorkPlanAdapter getAllWorkPlanAdapter() {
        return allWorkPlanAdapter;
    }

    public void getAllWorkPlan(int workPlanID)
    {
        String token = SharedPreferenceHelper.getInstance(getApplication()).getToken();

        WorkPlanRepository.getInstance().getAllWorkPlan(token,workPlanID);
        getServerResponse();
    }

    public void onClick(String coordinates)
    {
        this.coordinatesMutableLiveData.setValue(coordinates);
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<String> getCoordinatesMutableLiveData() {
        return coordinatesMutableLiveData;
    }

    private void getServerResponse() {

        WorkPlanRepository.getInstance().setNetWorkCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {
                if (response!=null)
                {
                   if (key== SERVER_ALL_WORK_PLAN_RESPONSE)
                    {
                        List<LookUpWorkPlan> list = (List<LookUpWorkPlan>) response;

                        allWorkPlanAdapter.setWorkPlanList(list);

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
