package com.example.ffccloud.dashboard.workplan.viewmodel;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_WORK_PLAN_RESPONSE;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.Target.Adapters.DoctorMorningRecyclerAdapter;
import com.example.ffccloud.dashboard.workplan.adapter.AllWorkPlanAdapter;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.WorkPlan;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.dashboard.workplan.adapter.WorkPlanListAdapter;
import com.example.ffccloud.dashboard.workplan.repository.WorkPlanRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WorkPlanViewModel extends AndroidViewModel {

    private final ObservableField<String> selectedMonth;
    private final MutableLiveData<String> serverErrorLiveData;
    private final ArrayAdapter<String> customSpinnerAdapter;
    private final HashMap<String, String> monthHashMap;
    private final List<String> monthList;
    private final WorkPlanListAdapter pendingAdapter;
    private WorkPlan workPlan;
    private final MutableLiveData<WorkPlan> selectedWorkPlan;
    private final MutableLiveData<String> toastMessage;



    public WorkPlanViewModel(@NonNull Application application) {
        super(application);
        monthList = new ArrayList<>();
        monthHashMap= new HashMap<>();
        serverErrorLiveData = new MutableLiveData<>();
        toastMessage = new MutableLiveData<>();
        workPlan= new WorkPlan();
        setUpMonthSpinner();
        customSpinnerAdapter= new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item,monthList);
        pendingAdapter = new WorkPlanListAdapter(this);

        selectedMonth= new ObservableField<>();
        selectedWorkPlan= new MutableLiveData<>();

    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public MutableLiveData<WorkPlan> getSelectedWorkPlan() {
        return selectedWorkPlan;
    }





    public MutableLiveData<String> getServerErrorLiveData() {
        return serverErrorLiveData;
    }

    public WorkPlanListAdapter getPendingAdapter() {
        return pendingAdapter;
    }

    public String getSelectedMonth() {
        return selectedMonth.get();
    }

    public void setSelectedMonth(String selectedMonth) {
        this.selectedMonth.set(selectedMonth);

        String month= monthHashMap.get(selectedMonth);
        String token= SharedPreferenceHelper.getInstance(getApplication()).getToken();
        int userID = SharedPreferenceHelper.getInstance(getApplication()).getUserID();
        WorkPlanRepository.getInstance().getPendingWorkPlan(month,userID,token);

        getServerResponse();
    }



    private void getServerResponse() {

        WorkPlanRepository.getInstance().setNetWorkCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {
                if (response!=null)
                {
                    if (key== SERVER_WORK_PLAN_RESPONSE)
                    {
                        List<WorkPlan> list = (List<WorkPlan>) response;
                        pendingAdapter.setWorkPlanList(list);
                    }
                    else if (key==SERVER_ERROR_RESPONSE)
                    {
                        serverErrorLiveData.setValue((String) response);
                        pendingAdapter.setWorkPlanList(null);
                        toastMessage.setValue((String) response);
                    }
                    else if (key==SERVER_RESPONSE)
                    {
                        serverErrorLiveData.setValue((String) response);
                        pendingAdapter.removeFromList(workPlan);
                        toastMessage.setValue((String) response);

                    }
                }
            }
        });
    }

    public ArrayAdapter<String> getCustomSpinnerAdapter() {



        return customSpinnerAdapter;


    }

    public void onClick(WorkPlan workPlan,int action)
    {
        workPlan.setAction(String.valueOf(action));

            this.workPlan= workPlan;

            selectedWorkPlan.setValue(workPlan);


    }

    public void updateWorkPlan(WorkPlan workPlan)
    {
        String  token= SharedPreferenceHelper.getInstance(getApplication()).getToken();
        int userID = SharedPreferenceHelper.getInstance(getApplication()).getUserID();
        WorkPlanRepository.getInstance().UpdateWorkPlanStatus(workPlan.getAction(), userID,token,String.valueOf(workPlan.getWorkPlanMId()));

        getServerResponse();
    }

    public void setUpMonthSpinner()
    {
        monthHashMap.clear();

        Calendar calendar = Calendar.getInstance();
        monthList.add("January");
        monthHashMap.put(monthList.get(0), calendar.get(Calendar.YEAR) + "01");

        monthList.add("February");
        monthHashMap.put(monthList.get(1), calendar.get(Calendar.YEAR) + "02");

        monthList.add("March");
        monthHashMap.put(monthList.get(2), calendar.get(Calendar.YEAR) + "03");

        monthList.add("April");
        monthHashMap.put(monthList.get(3), calendar.get(Calendar.YEAR) + "04");

        monthList.add("May");
        monthHashMap.put(monthList.get(4), calendar.get(Calendar.YEAR) + "05");

        monthList.add("June");
        monthHashMap.put(monthList.get(5), calendar.get(Calendar.YEAR) + "06");

        monthList.add("July");
        monthHashMap.put(monthList.get(6), calendar.get(Calendar.YEAR) + "07");

        monthList.add("August");
        monthHashMap.put(monthList.get(7), calendar.get(Calendar.YEAR) + "08");

        monthList.add("September");
        monthHashMap.put(monthList.get(8), calendar.get(Calendar.YEAR) + "09");

        monthList.add("October");
        monthHashMap.put(monthList.get(9), calendar.get(Calendar.YEAR) + "10");

        monthList.add("November");
        monthHashMap.put(monthList.get(10), calendar.get(Calendar.YEAR) + "11");

        monthList.add("December");
        monthHashMap.put(monthList.get(11), calendar.get(Calendar.YEAR) + "12");
    }
}
