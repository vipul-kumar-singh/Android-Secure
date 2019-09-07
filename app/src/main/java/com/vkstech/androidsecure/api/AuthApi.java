package com.vkstech.androidsecure.api;

import com.vkstech.androidsecure.constants.ApplicationConstants;
import com.vkstech.androidsecure.dto.AccessTokenResponse;
import com.vkstech.androidsecure.dto.LoginForm;
import com.vkstech.androidsecure.dto.ResponseObject;
import com.vkstech.androidsecure.dto.SignUpForm;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {

    @POST("common/login")
    Call<ResponseObject> login(@Body LoginForm loginform);

    @POST("common/signup")
    Call<ResponseObject> signup(@Body SignUpForm signUpForm);

    @POST("oauth/token")
    Call<AccessTokenResponse> refreshToken(@Body RequestBody refreshTokenRequest);

    @POST("common/logout")
    Call<ResponseObject> logout(@Header(ApplicationConstants.AUTHORIZATION_HEADER) String bearerToken);

}
