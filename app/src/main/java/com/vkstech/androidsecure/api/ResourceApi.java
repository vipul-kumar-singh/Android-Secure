package com.vkstech.androidsecure.api;

import com.vkstech.androidsecure.constants.ApplicationConstants;
import com.vkstech.androidsecure.dto.ResponseObject;
import com.vkstech.androidsecure.dto.SignUpForm;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ResourceApi {

    @GET("user/address/all")
    Call<ResponseObject> getMyAddresses(@Header(ApplicationConstants.AUTHORIZATION_HEADER) String bearerToken);

    @POST("user/address")
    Call<ResponseObject> addAddress(@Body SignUpForm signUpForm);

}
