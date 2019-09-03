package com.vkstech.androidsecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vkstech.androidsecure.R;
import com.vkstech.androidsecure.constants.ApplicationConstants;
import com.vkstech.androidsecure.constants.SharedPreferencesKeys;
import com.vkstech.androidsecure.dto.AccessTokenResponse;
import com.vkstech.androidsecure.dto.ResponseObject;
import com.vkstech.androidsecure.dto.SignUpForm;
import com.vkstech.androidsecure.exceptionHandler.ErrorMessageHandler;
import com.vkstech.androidsecure.utils.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private EditText username, password, email, confirmedPassword;
    private SharedPreferences sharedPreferences;
    private String accessToken;
    private Gson gson;
    private ErrorMessageHandler errorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().setTitle("Sign Up");

        sharedPreferences = this.getSharedPreferences(ApplicationConstants.BASE_PACKAGE_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        errorHandler = new ErrorMessageHandler();

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmedPassword = findViewById(R.id.confirmPassword);
    }

    public void signUp(View view) {

        SignUpForm signUpForm = new SignUpForm();
        signUpForm.setUsername(username.getText().toString());
        signUpForm.setPassword(password.getText().toString());
        signUpForm.setEmail(email.getText().toString());

        ApiUtil.getAuthApiRequest().signup(signUpForm).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                AccessTokenResponse accessTokenResponse;
                if (response.code() == 200 && response.body() != null) {
                    String jsonString = gson.toJson(response.body().getData());
                    accessTokenResponse = gson.fromJson(jsonString, AccessTokenResponse.class);

                    accessToken = accessTokenResponse.getAccessToken();
                    Toast.makeText(SignUpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putString(SharedPreferencesKeys.ACCESS_TOKEN, accessToken).apply();

                    gotoMainActivity(accessToken);

                } else {
                    errorHandler.handleRetrofitErrorMessage(response,SignUpActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("error", t.getMessage());
            }
        });
    }

    private void gotoMainActivity(String accessToken) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("accessToken",accessToken);
        startActivity(intent);
        finish();
    }

    public void gotoLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
