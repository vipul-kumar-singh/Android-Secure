package com.vkstech.androidsecure.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.vkstech.androidsecure.activity.LoginActivity;
import com.vkstech.androidsecure.constants.ApplicationConstants;
import com.vkstech.androidsecure.constants.SharedPreferencesKeys;
import com.vkstech.androidsecure.dto.AccessTokenResponse;
import com.vkstech.androidsecure.utils.ApiUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class RefreshTokenInterceptor implements Interceptor {

    private SharedPreferences sharedPreferences;

    private String accessToken;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // try the request
        Response response = chain.proceed(request);

        if (response.code() == 401) {

            response.close();

            // get a new access token using refresh token
            String newAccessToken = getAccessTokenFromRefreshToken();

            String bearerToken = ApplicationConstants.BEARER_TOKEN_TITLE + newAccessToken;

            // create a new request and modify it accordingly using the new token
            Request newRequest = request.newBuilder()
                    .header(ApplicationConstants.AUTHORIZATION_HEADER, bearerToken)
                    .build();

            // retry the request
            return chain.proceed(newRequest);
        }

        // otherwise just pass the original response on
        return response;
    }

    public String getAccessTokenFromRefreshToken() {

        final String refreshToken = getSharedPreferences().getString(SharedPreferencesKeys.REFRESH_TOKEN, null);

        RequestBody refreshTokenRequest = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("grant_type", "refresh_token")
                .addFormDataPart("refresh_token", refreshToken)
                .build();

        try {
            retrofit2.Response<AccessTokenResponse> response = ApiUtil.getAuthApiRequest().refreshToken(refreshTokenRequest).execute();
            AccessTokenResponse accessTokenResponse;
            if (response.code() == 200 && response.body() != null) {
                accessTokenResponse = response.body();

                accessToken = accessTokenResponse.getAccessToken();
                getSharedPreferences().edit().putString(SharedPreferencesKeys.ACCESS_TOKEN, accessToken).apply();
                getSharedPreferences().edit().putString(SharedPreferencesKeys.REFRESH_TOKEN, accessTokenResponse.getRefreshToken()).apply();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return accessToken;

    }

    private SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = LoginActivity.contextOfApplication.getSharedPreferences(ApplicationConstants.BASE_PACKAGE_NAME, Context.MODE_PRIVATE);
        return sharedPreferences;
    }

}