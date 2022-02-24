package com.example.ffccloud.meeting.repository;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_Employee_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_RESPONSE;

import androidx.annotation.NonNull;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.Employee;
import com.example.ffccloud.model.LookUpWorkPlan;
import com.example.ffccloud.model.Meeting;
import com.example.ffccloud.model.UpdateStatus;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRepository {

    private NetworkCallListener callListener;

    public void setCallListener(NetworkCallListener callListener) {
        this.callListener = callListener;
    }

    public void getAllEmployees(String token)
    {
        Call<List<Employee>> call = ApiClient.getInstance().getApi().getEmployees(token);

        call.enqueue(new Callback<List<Employee>>() {
            @Override
            public void onResponse(@NonNull Call<List<Employee>> call, @NonNull Response<List<Employee>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().size()>0)
                        {
                            callListener.onCallResponse(response.body(),SERVER_Employee_RESPONSE);
                        }
                        else
                        {
                            callListener.onCallResponse("Nothing Found",SERVER_ERROR_RESPONSE);
                        }
                    }
                    else
                    {
                        callListener.onCallResponse(response.message(),SERVER_ERROR_RESPONSE);
                    }
                }
                else
                {

                    if (response.errorBody() != null) {
                        callListener.onCallResponse(response.errorBody().toString(), SERVER_ERROR_RESPONSE);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<Employee>> call, @NonNull Throwable t) {

                callListener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);
            }
        });
    }

    public void saveMeeting(String token, Meeting meeting)
    {
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().saveMeeting(token,meeting);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NonNull Call<UpdateStatus> call, @NonNull Response<UpdateStatus> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {

                        if (response.body().getStatus()==1)
                        {
                            callListener.onCallResponse(response.body().getStrMessage(),SERVER_RESPONSE);

                        }else
                        {
                            callListener.onCallResponse(response.message(),SERVER_ERROR_RESPONSE);

                        }


                    }
                    else
                    {
                    }
                }
                else
                {

                    if (response.errorBody() != null) {
                        callListener.onCallResponse(response.errorBody().toString(), SERVER_ERROR_RESPONSE);
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<UpdateStatus> call, @NonNull Throwable t) {

                callListener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);
            }
        });
    }
}
