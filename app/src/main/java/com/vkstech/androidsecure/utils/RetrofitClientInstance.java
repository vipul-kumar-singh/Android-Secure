package com.vkstech.androidsecure.utils;

import com.vkstech.androidsecure.interceptor.HeaderInterceptor;
import com.vkstech.androidsecure.interceptor.RefreshTokenInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit AuthRetrofit;
    private static Retrofit ResourceRetrofit;

    public static Retrofit getAuthRetrofit(String url) {
        if (AuthRetrofit == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new HeaderInterceptor())
                    .build();

            AuthRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return AuthRetrofit;
    }

    public static Retrofit getResourceRetrofit(String url) {
        if (ResourceRetrofit == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new RefreshTokenInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .build();

            ResourceRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return ResourceRetrofit;
    }
}
