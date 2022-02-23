package com.example.ffccloud.dashboard.report.viewmodel;

import android.app.Application;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.UserModel;
import com.example.ffccloud.dashboard.report.repository.ReportFilterRepository;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.ReportFilter;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReportFilterViewModel extends AndroidViewModel {

    private final MutableLiveData<Integer> clickMutableLivedata;
    private final MutableLiveData<String> fromDateMutableLivedata;
    private final MutableLiveData<String> toDateMutableLivedata;
    private final MutableLiveData<ReportFilter> reportFilterMutableLiveData;
    private ObservableField<ArrayAdapter<String>> employeeSpinnerAdapter;
    private ObservableField<ArrayAdapter<String>> statusSpinnerAdapter;
    private final HashMap<String,String> employeeHashMap;
    private final HashMap<String,Integer> statusHashMap;
    private ObservableField<String> selectedEmployee;
    private ObservableField<String> selectedStatus;
    private int empID=0,statusID=0;
    private MutableLiveData<String> fromDateError;
    private MutableLiveData<String> toDateError;

    public ReportFilterViewModel(@NonNull Application application) {
        super(application);
        clickMutableLivedata= new MutableLiveData<>();
        fromDateMutableLivedata= new MutableLiveData<>();
        reportFilterMutableLiveData= new MutableLiveData<>();
        toDateError= new MutableLiveData<>();
        fromDateError= new MutableLiveData<>();
        toDateMutableLivedata= new MutableLiveData<>();
        employeeHashMap= new HashMap<>();
        statusHashMap= new HashMap<>();
        employeeSpinnerAdapter = new ObservableField<>();
        statusSpinnerAdapter = new ObservableField<>();
        selectedEmployee= new ObservableField<>();
        selectedStatus= new ObservableField<>();
        setUpStatusSpinner();
    }

    public void onClick(int key)
    {
        if (key!=3)
        {
            clickMutableLivedata.setValue(key);

        }
        else
        {
            if (fromDateMutableLivedata.getValue()!=null && !fromDateMutableLivedata.getValue().isEmpty())
            {
                if (toDateMutableLivedata.getValue()!=null && !toDateMutableLivedata.getValue().isEmpty())
                {
                    ReportFilter reportFilter = new ReportFilter();
                    reportFilter.setFromDate(fromDateMutableLivedata.getValue());
                    reportFilter.setEmpId(empID);
                    reportFilter.setStatusID(statusID);
                    reportFilter.setToDate(toDateMutableLivedata.getValue());
                    reportFilterMutableLiveData.setValue(reportFilter);
                }
                else
                {
                    toDateError.setValue("Please Select Date");
                }
            }
            else
            {
                fromDateError.setValue("Please Select Date");
            }

        }
    }

    public MutableLiveData<String> getFromDateError() {
        return fromDateError;
    }

    public MutableLiveData<String> getToDateError() {
        return toDateError;
    }

    public MutableLiveData<ReportFilter> getReportFilterMutableLiveData() {
        return reportFilterMutableLiveData;
    }

    public ObservableField<String> getSelectedStatus() {

        if (selectedStatus!=null&& selectedStatus.get()!=null)
        {
            statusID= statusHashMap.get(selectedStatus.get());

        }
        return selectedStatus;
    }

    public void setSelectedStatus(ObservableField<String> selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

    public ObservableField<ArrayAdapter<String>> getStatusSpinnerAdapter() {

        return statusSpinnerAdapter;
    }

    public void setStatusSpinnerAdapter(ObservableField<ArrayAdapter<String>> statusSpinnerAdapter) {
        this.statusSpinnerAdapter = statusSpinnerAdapter;
    }

    public ObservableField<String> getSelectedEmployee() {
        String emp= employeeHashMap.get(selectedEmployee.get());
        if (emp!=null)
        {
            empID= Integer.parseInt(emp);

        }
        return selectedEmployee;
    }

    public void setSelectedEmployee(ObservableField<String> selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public ObservableField<ArrayAdapter<String>> getEmployeeSpinnerAdapter() {
        return employeeSpinnerAdapter;
    }

    public void setEmployeeSpinnerAdapter(ObservableField<ArrayAdapter<String>> employeeSpinnerAdapter) {
        this.employeeSpinnerAdapter = employeeSpinnerAdapter;
    }

    public MutableLiveData<String> getFromDateMutableLivedata() {
        return fromDateMutableLivedata;
    }

    public MutableLiveData<String> getToDateMutableLivedata() {
        return toDateMutableLivedata;
    }

    public MutableLiveData<Integer> getClickMutableLivedata() {
        return clickMutableLivedata;
    }

    public void getEmployees()
    {
        int empID= SharedPreferenceHelper.getInstance(getApplication()).getEmpID();
        String token = SharedPreferenceHelper.getInstance(getApplication()).getToken();

        ReportFilterRepository.getInstance().getEmployees(token,empID);
        getServerResponse();
    }

    public void getServerResponse()
    {
        ReportFilterRepository.getInstance().setCallListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {
                if (response!=null)
                {
                    if (key== CONSTANTS.SERVER_USERS_RESPONSE)
                    {
                        List<UserModel> list = (List<UserModel>) response;
                        setupEmployeeSpinner(list);
                    }
                }
            }
        });
    }

    private void setupEmployeeSpinner(List<UserModel> list) {

        employeeHashMap.clear();
        List<String> empList= new ArrayList<>();
        empList.add("All");
        employeeHashMap.put("All","0");
        for (UserModel model:list)
        {
            empList.add(model.getUserName());
            employeeHashMap.put(model.getUserName(),model.getId());
        }
       ArrayAdapter<String> adapter= new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item,empList);

        employeeSpinnerAdapter.set(adapter);
    }

    private void setUpStatusSpinner()
    {
        List<String> statusList= new ArrayList<>();
        statusList.add("All");
        statusHashMap.put("All",10);

        statusList.add("Pending");
        statusHashMap.put("Pending",0);

        statusList.add("Completed");
        statusHashMap.put("Completed",2);

        statusList.add("Cancel");
        statusHashMap.put("Cancel",3);

        ArrayAdapter<String> adapter= new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_dropdown_item,statusList);

        statusSpinnerAdapter.set(adapter);
    }
}
