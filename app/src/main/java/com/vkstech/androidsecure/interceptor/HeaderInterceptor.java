package com.vkstech.androidsecure.interceptor;

import com.vkstech.androidsecure.constants.ApplicationConstants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        if (request.url().encodedPath().equalsIgnoreCase("/common/logout")) {
            return  chain.proceed(request);
        }


        request = request
                .newBuilder()
                .addHeader(ApplicationConstants.AUTHORIZATION_HEADER, ApplicationConstants.BASIC_TOKEN)
                .build();
        return chain.proceed(request);
    }
}
