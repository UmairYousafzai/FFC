package com.example.ffccloud.dashboard.workplan.viewmodel;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_WORK_PLAN_RESPONSE;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.dashboard.workplan.adapter.AllWorkPlanAdapter;
import com.example.ffccloud.dashboard.workplan.repository.WorkPlanRepository;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.WorkPlan;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

public class AllWorkPlanViewModel extends AndroidViewModel {
    private final AllWorkPlanAdapter allWorkPlanAdapter;
    private final MutableLiveData<String> serverErrorLiveData;


    public AllWorkPlanViewModel(@NonNull Application application) {
        super(application);
        allWorkPlanAdapter= new AllWorkPlanAdapter(getApplication(),1);
        serverErrorLiveData = new MutableLiveData<>();

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


    private void getServerResponse() {

        WorkPlanRepository.getInstance().setNetWorkCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {
                if (response!=null)
                {
                   if (key== SERVER_ALL_WORK_PLAN_RESPONSE)
                    {
                        List<WorkPlan> list = (List<WorkPlan>) response;
                        List<DoctorModel> doctorModelList= new ArrayList<>();
                        for (WorkPlan model:list)
                        {
                            DoctorModel doctorModel= new DoctorModel();
                            doctorModel.setName(model.getWorkPlanNo());
                            doctorModel.setWork_Plan(model.getWorkPlan());
                            doctorModel.setToDate(model.getCreatedOn());
                            doctorModel.setCoordinates(model.getVisitCord());
                            doctorModelList.add(doctorModel);
                        }
                        allWorkPlanAdapter.setDoctorModelList(doctorModelList);

                    }

                }
            }
        });
    }
}
