package com.example.ffccloud.NetworkCalls;

import android.content.Context;

import com.example.ffccloud.BuildConfig;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    static Context context;
    private static final String base_url = SharedPreferenceHelper.getInstance(context).getBaseUrl();
    private static Retrofit retrofitClient;
    private static final String BASE_URL = base_url;
    private static ApiClient mInstance;



    ApiClient() {
        if (retrofitClient == null) {

            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
            /*builder.readTimeout(1, TimeUnit.MINUTES);
            builder.connectTimeout(1, TimeUnit.MINUTES);
            builder.writeTimeout(1, TimeUnit.MINUTES);*/
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }
            retrofitClient = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
        }
    }


    public static synchronized ApiClient getInstance(){
        if(mInstance==null){
            mInstance = new ApiClient();
        }
        return mInstance;
    }
    public Api getApi(){
        return retrofitClient.create(Api.class);
    }
}

