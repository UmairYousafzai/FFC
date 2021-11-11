package com.example.ffccloud.PushNotification;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private static Retrofit retrofit=null;

    public static Retrofit getClient(String url)
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        }

//        if (retrofit == null) {
//
//            OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
//            /*builder.readTimeout(1, TimeUnit.MINUTES);
//            builder.connectTimeout(1, TimeUnit.MINUTES);
//            builder.writeTimeout(1, TimeUnit.MINUTES);*/
//            if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//                builder.addInterceptor(interceptor);
//            }
//            retrofitClient = new Retrofit.Builder()
//                    .client(builder.build())
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .baseUrl(BASE_URL)
//                    .build();
//        }
        return  retrofit;
    }
}
