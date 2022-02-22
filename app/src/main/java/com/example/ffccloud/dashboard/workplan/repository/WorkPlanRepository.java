package com.example.ffccloud.dashboard.workplan.repository;

import static com.example.ffccloud.utils.CONSTANTS.SERVER_ALL_WORK_PLAN_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_ERROR_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_RESPONSE;
import static com.example.ffccloud.utils.CONSTANTS.SERVER_WORK_PLAN_RESPONSE;

import androidx.annotation.NonNull;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.interfaces.NetworkCallListener;
import com.example.ffccloud.model.LookUpWorkPlan;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.model.WorkPlan;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkPlanRepository {

    private static WorkPlanRepository workPlanRepository;
    private NetworkCallListener listener;

    public static synchronized WorkPlanRepository getInstance()
    {
        if (workPlanRepository==null)
        {
            workPlanRepository = new WorkPlanRepository();
        }

        return workPlanRepository;
    }

    public void setNetWorkCallListener(NetworkCallListener listener)
    {
        this.listener = listener;
    }


    public void getPendingWorkPlan(String month,int userID,String token)
    {
        Call<List<WorkPlan>> call = ApiClient.getInstance().getApi().getPendingWorkPlan(token,month,userID);

        call.enqueue(new Callback<List<WorkPlan>>() {
            @Override
            public void onResponse(@NonNull Call<List<WorkPlan>> call, @NonNull Response<List<WorkPlan>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().size()>0)
                        {
                            listener.onCallResponse(response.body(),SERVER_WORK_PLAN_RESPONSE );
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
            public void onFailure(@NonNull Call<List<WorkPlan>> call, @NonNull Throwable t) {

                listener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);
            }
        });
    }


    public void getAllWorkPlan(String token,int workPlanID)
    {
        Call<List<LookUpWorkPlan>> call = ApiClient.getInstance().getApi().getAllDashBoardWorkPlan(token,"","",0,0,workPlanID);

        call.enqueue(new Callback<List<LookUpWorkPlan>>() {
            @Override
            public void onResponse(@NonNull Call<List<LookUpWorkPlan>> call, @NonNull Response<List<LookUpWorkPlan>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        if (response.body().size()>0)
                        {
                            listener.onCallResponse(response.body(),SERVER_ALL_WORK_PLAN_RESPONSE);
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
            public void onFailure(@NonNull Call<List<LookUpWorkPlan>> call, @NonNull Throwable t) {

                listener.onCallResponse(t.getMessage(),SERVER_ERROR_RESPONSE);
            }
        });
    }

    public void UpdateWorkPlanStatus(String action, int userID, String token,String workPlanID)
    {
        Call<UpdateStatus> call= ApiClient.getInstance().getApi().updatePendingWorkPlanStatus(token,"1",
                "1", "1","1",workPlanID,action,userID,1);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NonNull Call<UpdateStatus> call, @NonNull Response<UpdateStatus> response) {
                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {

                        if (response.body().getStatus()==1)
                        {
                            listener.onCallResponse(response.body().getStrMessage(),SERVER_RESPONSE );

                        }
                        else
                        {
                            listener.onCallResponse(response.message(),SERVER_ERROR_RESPONSE);

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
            public void onFailure(@NonNull Call<UpdateStatus> call, @NonNull Throwable t) {
                listener.onCallResponse(t.getMessage(), SERVER_ERROR_RESPONSE);

            }
        });

    }
}
