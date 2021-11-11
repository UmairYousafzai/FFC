package com.example.ffccloud.PushNotification;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAGn3kNAY:APA91bEKJ7LkJw8SN9373Q8l5JnNOWsB_DDZ_mCFzRMNijq-1fmytmzHqvDDU6sDmZ9dEn8uAx6HvM3dPyliCeBAmPo63BHUs7UCViLAdhxPwDy8pE6wsoqiP7be6T3lO-0r1zN6PCbW" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body JsonObject payLoad);
}

