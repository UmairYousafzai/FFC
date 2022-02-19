package com.example.ffccloud.dashboard.customer.repository;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_CUSTOMER_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_RESPONSE;

import androidx.annotation.NonNull;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.DashBoardCustomer;
import com.example.ffccloud.model.UpdateStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRepository {

    private static CustomerRepository customerRepository;
    private NetworkCallListener listener;



    public static  synchronized CustomerRepository getInstance()
    {
        if (customerRepository==null)
        {
            customerRepository=new CustomerRepository();
        }

        return customerRepository;
    }

    public void setListener(NetworkCallListener listener)
    {
        this.listener= listener;
    }

    public void getSuggestedCustomer(int userID,String token)
    {
        Call<List<DashBoardCustomer>> call = ApiClient.getInstance().getApi().getSuggestedCustomer(token,1,1,userID);
        call.enqueue(new Callback<List<DashBoardCustomer>>() {
            @Override
            public void onResponse(@NonNull Call<List<DashBoardCustomer>> call, @NonNull Response<List<DashBoardCustomer>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().size()>0)
                        {
                            listener.onCallResponse(response.body(),SERVER_CUSTOMER_RESPONSE );
                        }
                        else
                        {
                            listener.onCallResponse("Nothing Found",SERVER_ERROR_RESPONSE);
                        }
                    }
                    else
                    {
                        listener.onCallResponse(response.message(),SERVER_ERROR_RESPONSE);
                    }
                }
                else
                {
                    if (response.errorBody() != null) {
                        listener.onCallResponse(response.errorBody().toString(), SERVER_ERROR_RESPONSE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DashBoardCustomer>> call, @NonNull Throwable t) {

                listener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);

            }
        });
    }
    public void getCancelCustomer(int userID,String token)
    {
        Call<List<DashBoardCustomer>> call = ApiClient.getInstance().getApi().getCanceledCustomer(token,1,1,userID);
        call.enqueue(new Callback<List<DashBoardCustomer>>() {
            @Override
            public void onResponse(@NonNull Call<List<DashBoardCustomer>> call, @NonNull Response<List<DashBoardCustomer>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().size()>0)
                        {
                            listener.onCallResponse(response.body(),SERVER_CUSTOMER_RESPONSE );
                        }
                        else
                        {
                            listener.onCallResponse("Nothing Found",SERVER_ERROR_RESPONSE);
                        }
                    }
                    else
                    {
                        listener.onCallResponse(response.message(),SERVER_ERROR_RESPONSE);
                    }
                }
                else
                {
                    if (response.errorBody() != null) {
                        listener.onCallResponse(response.errorBody().toString(), SERVER_ERROR_RESPONSE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DashBoardCustomer>> call, @NonNull Throwable t) {

                listener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);

            }
        });
    }

    public void updateSuggestedCustomerStatus(String token,String customerID,String action,int userID)
    {
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().updateSuggestedCustomerStatus(token,"1",
                "1","1","1",customerID,action,userID,1);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NonNull Call<UpdateStatus> call, @NonNull Response<UpdateStatus> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                       listener.onCallResponse(response.body(),SERVER_RESPONSE );

                    }
                    else
                    {
                        listener.onCallResponse(response.message(),SERVER_ERROR_RESPONSE);
                    }
                }
                else
                {
                    if (response.errorBody() != null) {
                        listener.onCallResponse(response.errorBody().toString(), SERVER_ERROR_RESPONSE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UpdateStatus> call, @NonNull Throwable t) {
                listener.onCallResponse(t.getMessage(), SERVER_ERROR_RESPONSE);

            }
        });
    }
}
