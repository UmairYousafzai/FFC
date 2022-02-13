package com.example.ffccloud.workplan.viewmodel;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_WORK_PLAN_RESPONSE;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.WorkPlan;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.workplan.adapter.WorkPlanListAdapter;
import com.example.ffccloud.workplan.repository.WorkPlanRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class WorkPlanViewModel extends AndroidViewModel {

    private ObservableField<String> selectedMonth;
    private final MutableLiveData<String> serverErrorLiveData;
    private final ArrayAdapter<String> customSpinnerAdapter;
    private final HashMap<String, String> monthHashMap;
    private final List<String> monthList;
    private final WorkPlanListAdapter adapter;



    public WorkPlanViewModel(@NonNull Application application) {
        super(application);
        monthList = new ArrayList<>();
        monthHashMap= new HashMap<>();
        serverErrorLiveData = new MutableLiveData<>();
        setUpMonthSpinner();
        customSpinnerAdapter= new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item,monthList);
        adapter= new WorkPlanListAdapter(this);
        selectedMonth= new ObservableField<>();

    }

    public MutableLiveData<String> getServerErrorLiveData() {
        return serverErrorLiveData;
    }

    public WorkPlanListAdapter getAdapter() {
        return adapter;
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
                        adapter.setWorkPlanList(list);
                    }
                    else if (key==SERVER_ERROR_RESPONSE)
                    {
                        serverErrorLiveData.setValue((String) response);
                        adapter.setWorkPlanList(null);
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
