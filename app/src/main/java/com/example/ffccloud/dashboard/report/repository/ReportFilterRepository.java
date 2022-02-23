package com.example.ffccloud.dashboard.report.repository;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_UNAUTHORIZED_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_USERS_RESPONSE;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.UserModel;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.LookUpWorkPlan;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomsDialog;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportFilterRepository {

    private static  ReportFilterRepository repository=null;
    private NetworkCallListener callListener;


    public static  synchronized ReportFilterRepository getInstance()
    {
        if (repository==null)
        {
            repository= new ReportFilterRepository();
        }
        return repository;
    }

    public void setCallListener(NetworkCallListener callListener) {
        this.callListener = callListener;
    }


    public void getEmployees(String token, int empID)
    {
        Call<List<UserModel>> call = ApiClient.getInstance().getApi().getUsersForTracking(token,empID);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserModel>> call, @NotNull Response<List<UserModel>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (callListener!=null)
                        {
                            callListener.onCallResponse(response.body(), SERVER_USERS_RESPONSE);
                        }
                    }else
                    {
                        callListener.onCallResponse("Nothing Found",SERVER_ERROR_RESPONSE);
                    }



                }
                else
                {
                    if (response.message().equals("Unauthorized"))
                    {
                        callListener.onCallResponse(true,SERVER_UNAUTHORIZED_RESPONSE);
                    }
                    else
                    {
                        if (response.errorBody() != null) {
                            callListener.onCallResponse(response.errorBody().toString(),SERVER_ERROR_RESPONSE);
                        }
                        else
                        {
                            callListener.onCallResponse(response.message(),SERVER_ERROR_RESPONSE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<UserModel>> call, @NotNull Throwable t) {
                callListener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);
            }
        });
    }


}
