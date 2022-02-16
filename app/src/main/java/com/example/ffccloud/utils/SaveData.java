package com.example.ffccloud.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Vibrator;

import androidx.core.app.NotificationCompat;

import com.example.ffccloud.DoctorModel;
import com.example.ffccloud.model.AddNewWorkPlanModel;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.model.UpdateWorkPlanStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.worker.utils.UploadDataRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SaveData {

    private static SaveData mInstance;
    private  Context mContext;
    private final UploadDataRepository uploadDataRepository;

    public SaveData(Context context) {
        this.mContext = context;
        uploadDataRepository= new UploadDataRepository(context);

    }

    public static synchronized SaveData getInstance(Context context){
        if(mInstance==null){
            mInstance = new SaveData(context);
        }
        return mInstance;
    }

    public void UpdateWorkPlanStatusRequest(List<UpdateWorkPlanStatus> list)
    {

        String token = SharedPreferenceHelper.getInstance(mContext).getToken();

        createNotification("Updating Work Plan Status",1,CONSTANTS.NOTIFICATION_WORK_PLAN_STATUS_ID);
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().UpdateWorkPlanStatus(token, "application/json", list);
        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response.body() != null) {

                    for (UpdateWorkPlanStatus model: list)
                    {
                        DoctorModel doctorModel = uploadDataRepository.getWorkPlanById(model.getDoctor_Id());
                        if (doctorModel!=null)
                        {
                            uploadDataRepository.deleteWorkPlan(doctorModel);
                        }
                    }

                    uploadDataRepository.DeleteAllWorkPlanStatus();
                    UpdateStatus updateStatus = response.body();
                    createNotification(updateStatus.getStrMessage(),2,CONSTANTS.NOTIFICATION_WORK_PLAN_STATUS_ID);

                }else
                {
                    createNotification(""+response.errorBody(),2,CONSTANTS.NOTIFICATION_ERROR_ID);
                    clearNotification(CONSTANTS.NOTIFICATION_WORK_PLAN_STATUS_ID);
                }


            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                createNotification(""+t.getMessage(),2,CONSTANTS.NOTIFICATION_ERROR_ID);
                clearNotification(CONSTANTS.NOTIFICATION_WORK_PLAN_STATUS_ID);

            }
        });
    }

    public void SaveWorkPlanStatus()
    {

        UpdateWorkPlanStatusRequest(uploadDataRepository.getAllWorkPlanStatus());


    }

    public void UpdateWorkPlan(List<AddNewWorkPlanModel> list)
    {

        final UpdateStatus[] updateStatus = new UpdateStatus[1];
        String token = SharedPreferenceHelper.getInstance(mContext).getToken();

        createNotification("Updating Work Plan",1,CONSTANTS.NOTIFICATION_WORK_PLAN_ID);
        for (AddNewWorkPlanModel model:list)
        {
            Call<UpdateStatus> call = ApiClient.getInstance().getApi().RescheduleWorkPlan(token, model);
            call.enqueue(new Callback<UpdateStatus>() {
                @Override
                public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                    if (response.body() != null) {


                        uploadDataRepository.DeleteAllWorkPlan();
//                        DoctorModel doctorModel =uploadDataRepository.getWorkPlanById(model.getDoctorId());
//                        uploadDataRepository.deleteWorkPlan(doctorModel);
                         updateStatus[0] = response.body();
                        clearNotification(CONSTANTS.NOTIFICATION_WORK_PLAN_ID);
                        createNotification(""+response.body().getStrMessage(),2,CONSTANTS.NOTIFICATION_ERROR_ID);

                    }
                    else
                    {
                        createNotification(""+response.errorBody(),2,CONSTANTS.NOTIFICATION_ERROR_ID);
                        clearNotification(CONSTANTS.NOTIFICATION_WORK_PLAN_ID);

                    }



                }

                @Override
                public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {

                    createNotification(""+t.getMessage(),2,CONSTANTS.NOTIFICATION_ERROR_ID);
                    clearNotification(CONSTANTS.NOTIFICATION_WORK_PLAN_ID);
                }
            });


        }

    }

    public void saveWorkPlan()
    {
        UpdateWorkPlan(uploadDataRepository.getAllWorkPlan());
    }



    public void createNotification( String message, int key,int id) {
        NotificationManager mNotificationManager;

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
        r.play();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            r.setLooping(false);
        }

        // vibration
        Vibrator v = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {100, 300, 300, 300};
        v.vibrate(pattern, -1);




        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "CHANNEL_ID");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.drawable.img_erp_cloud_logo);
        } else {
            builder.setSmallIcon(R.drawable.img_erp_cloud_logo);
        }


        builder.setContentTitle("FFC");
        builder.setContentText(message);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setProgress(0, 0, key == 1);


        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channelId);
        }


// notificationId is a unique int for each notification that you must define
        mNotificationManager.notify(id, builder.build());
    }
    public void clearNotification(int NOTIFICATION_ID) {
        NotificationManager notificationManager = (NotificationManager) mContext
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
