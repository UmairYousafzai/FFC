package com.example.ffccloud.dashboard.customer.viewmodel;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_CUSTOMER_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_RESPONSE;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.ffccloud.dashboard.customer.adapter.SuggestedCustomerAdapter;
import com.example.ffccloud.dashboard.customer.repository.CustomerRepository;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.DashBoardCustomer;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import java.util.List;

public class CustomerViewModel extends AndroidViewModel {

    private SuggestedCustomerAdapter adapter;
    private MutableLiveData<String> toastMessage;
    private MutableLiveData<DashBoardCustomer>  customerMutableLiveData;
    private DashBoardCustomer customer;
    private String type;

    public CustomerViewModel(@NonNull Application application) {
        super(application);

        adapter = new SuggestedCustomerAdapter(this);
        toastMessage= new MutableLiveData<>();
        getServerResponse();
        customerMutableLiveData= new MutableLiveData<>();
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SuggestedCustomerAdapter getAdapter() {
        return adapter;
    }

    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }

    public void fetchSuggestedCustomer()
    {
        int userID= SharedPreferenceHelper.getInstance(getApplication()).getUserID();
        String token= SharedPreferenceHelper.getInstance(getApplication()).getToken();

        CustomerRepository.getInstance().getSuggestedCustomer(userID,token);

    }
   public void fetchCancelCustomer()
    {
        int userID= SharedPreferenceHelper.getInstance(getApplication()).getUserID();
        String token= SharedPreferenceHelper.getInstance(getApplication()).getToken();

        CustomerRepository.getInstance().getCancelCustomer(userID,token);

    }

    public void onClick(DashBoardCustomer customer, int key)
    {

            customer.setAction(String.valueOf(key));
            customerMutableLiveData.setValue(customer);


        this.customer= customer;
    }

    public void updateStatusCustomer(DashBoardCustomer customer)
    {
        String token = SharedPreferenceHelper.getInstance(getApplication()).getToken();
        int userID= SharedPreferenceHelper.getInstance(getApplication()).getUserID();
        CustomerRepository.getInstance().updateSuggestedCustomerStatus(token,String.valueOf(customer.getDoctorId()),customer.getAction(),userID);
        getServerResponse();
    }

    public MutableLiveData<DashBoardCustomer> getCustomerMutableLiveData() {
        return customerMutableLiveData;
    }

    public void getServerResponse()
    {
        CustomerRepository.getInstance().setListener(new NetworkCallListener() {
            @Override
            public void onCallResponse(Object response, int key) {

                if (response!=null)
                {
                    if (key== SERVER_CUSTOMER_RESPONSE)
                    {
                        adapter.setDashBoardCustomerList((List<DashBoardCustomer>) response);
                    }
                    else if (key==SERVER_ERROR_RESPONSE)
                    {
                        toastMessage.setValue((String) response);
                    }
                    else if (key== SERVER_RESPONSE)
                    {
                        UpdateStatus  updateStatus = (UpdateStatus) response;
                        toastMessage.setValue(updateStatus.getStrMessage());
                        if (type.equals("Cancel"))
                        {
                            fetchCancelCustomer();
                        }
                        else
                        {
                            fetchSuggestedCustomer();

                        }
                    }
                }

            }
        });

    }
}
