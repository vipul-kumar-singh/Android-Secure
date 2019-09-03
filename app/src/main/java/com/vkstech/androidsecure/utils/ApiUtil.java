package com.vkstech.androidsecure.utils;

import com.vkstech.androidsecure.api.AuthApi;
import com.vkstech.androidsecure.api.ResourceApi;
import com.vkstech.androidsecure.constants.ApplicationConstants;

public class ApiUtil {

    public static AuthApi getAuthApiRequest(){
        return RetrofitClientInstance
                .getAuthRetrofit(ApplicationConstants.AUTH_BASE_URL)
                .create(AuthApi.class);
    }

    public static ResourceApi getResourceApiRequest(){
        return RetrofitClientInstance
                .getResourceRetrofit(ApplicationConstants.RESOURCE_BASE_URL)
                .create(ResourceApi.class);
    }
}