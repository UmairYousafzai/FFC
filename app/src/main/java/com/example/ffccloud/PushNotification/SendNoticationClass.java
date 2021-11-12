package com.example.ffccloud.PushNotification;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.R;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendNoticationClass {

    private String refreshToken;
    private ProgressDialog progressDialog;
    private final Context context;

    public SendNoticationClass(Context context) {
        this.context = context;
    }

    public void UpdateToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            refreshToken=task.getResult();
            Token token= new Token(refreshToken);
            String userID= String.valueOf(SharedPreferenceHelper.getInstance(context).getUserID());
            FirebaseDatabase.getInstance().getReference("Tokens").child(userID).setValue(token);
        });

    }
    public void sendNotifications(String usertoken, GetUserInfoModel senderUser, String title, String message, Context context) {


        APIService apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);




        JsonObject payload = new JsonObject();
        payload.addProperty("to", usertoken);
        payload.addProperty("priority","high");

        // compose data payload here
        JsonObject data = new JsonObject();
        data.addProperty("title", title);
        data.addProperty("message", message);
        data.addProperty("email",senderUser.getEmail());
        data.addProperty("userID",senderUser.getID());
        data.addProperty("userName",senderUser.getUserName());

        // add data payload
        payload.add("data", data);
        apiService.sendNotifcation(payload).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    if (response.body().success != 1) {
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG).show();

                    }
                    else {
                        Log.d("Notification", "onResponse: Request sent Successfully ");
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<MyResponse> call, @NotNull Throwable t) {
                Log.d("Notification", "onResponse: "+t.getMessage());

            }
        });
    }

}
