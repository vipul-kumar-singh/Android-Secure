package com.vkstech.androidsecure.dto;

import com.google.gson.annotations.SerializedName;

public class RefreshTokenRequest {

    @SerializedName("grant_type")
    private String grantType;

    @SerializedName("refresh_token")
    private String refreshToken;

    public RefreshTokenRequest() {
    }

    public RefreshTokenRequest(String refreshToken) {
        this.grantType = "refresh_token";
        this.refreshToken = refreshToken;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
