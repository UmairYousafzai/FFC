package com.example.ffccloud.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.ffccloud.Target.utils.TargetRepository;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.SaveData;
import com.example.ffccloud.worker.utils.UploadDataRepository;

public class UploadWorker extends Worker {
    private UploadDataRepository uploadDataRepository;
    private Context mContext;
    private TargetRepository targetRepository;


    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.mContext= context;
        uploadDataRepository= new UploadDataRepository(context);
        targetRepository= new TargetRepository(context);

    }

    @NonNull
    @Override
    public Result doWork() {

        String data = getInputData().getString(CONSTANTS.WORK_REQUEST_TAG);
        if (data != null) {
            switch (data) {
                case CONSTANTS.WORK_REQUEST_CANCEL_WORK_PLAN:
                    SaveData.getInstance(mContext).SaveWorkPlanStatus();
                    break;
                case CONSTANTS.WORK_REQUEST_RESCHEDULE_WORK_PLAN:
                    SaveData.getInstance(mContext).saveWorkPlan();

                    break;

            }
        }


        return Result.success();
    }

//    public void updateWorkPlan(List<UpdateWorkPlanStatus> list)
//    {
//
//        Call<UpdateStatus> call = ApiClient.getInstance().getApi().UpdateWorkPlanStatus(token, "application/json", list);
//        call.enqueue(new Callback<UpdateStatus>() {
//            @Override
//            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {
//
//                if (response.body() != null) {
//
//
//                    UpdateStatus updateStatus = response.body();
//
//
//                }
//
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
//            }
//        });
//    }
//
//    public void getSavedWorkPlanStatus()
//    {
//
//        uploadDataRepository.getAllWorkPlanStatus().observe((AppCompatActivity) mContext, new Observer<List<UpdateWorkPlanStatus>>() {
//            @Override
//            public void onChanged(List<UpdateWorkPlanStatus> list) {
//                updateWorkPlan(list);
//            }
//        });
//
////       uploadDataRepository.getAllWorkPlanStatus().observeForever(new Observer<List<UpdateWorkPlanStatus>>() {
////           @Override
////           public void onChanged(List<UpdateWorkPlanStatus> list) {
////               updateWorkPlan(list);
////
////
////           }
////       });
//    }

    @Override
    public void onStopped() {
        super.onStopped();

    }
}
