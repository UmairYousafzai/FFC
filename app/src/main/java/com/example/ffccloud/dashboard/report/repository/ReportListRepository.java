package com.example.ffccloud.dashboard.report.repository;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;

import androidx.annotation.NonNull;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.LookUpWorkPlan;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportListRepository {

    private static ReportListRepository repository=null;
    private NetworkCallListener callListener;


    public void setCallListener(NetworkCallListener callListener) {
        this.callListener = callListener;
    }

    public static synchronized ReportListRepository getInstance()
    {
        if (repository==null)
        {
            repository = new ReportListRepository();
        }

        return repository;
    }

    public void getReports(String token,String fromDate,String toDate,int empID,int statusID)
    {
        Call<List<LookUpWorkPlan>> call = ApiClient.getInstance().getApi().getAllDashBoardWorkPlan(token,fromDate,toDate,empID,statusID,0);

        call.enqueue(new Callback<List<LookUpWorkPlan>>() {
            @Override
            public void onResponse(@NonNull Call<List<LookUpWorkPlan>> call, @NonNull Response<List<LookUpWorkPlan>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().size()>0)
                        {
                            callListener.onCallResponse(response.body(),SERVER_ALL_WORK_PLAN_RESPONSE);
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
            public void onFailure(@NonNull Call<List<LookUpWorkPlan>> call, @NonNull Throwable t) {

                callListener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);
            }
        });
    }
}
