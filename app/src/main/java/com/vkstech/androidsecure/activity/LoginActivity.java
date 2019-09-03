package com.vkstech.androidsecure.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.vkstech.androidsecure.R;
import com.vkstech.androidsecure.constants.ApplicationConstants;
import com.vkstech.androidsecure.constants.SharedPreferencesKeys;
import com.vkstech.androidsecure.dto.AccessTokenResponse;
import com.vkstech.androidsecure.dto.LoginForm;
import com.vkstech.androidsecure.dto.RefreshTokenRequest;
import com.vkstech.androidsecure.dto.ResponseObject;
import com.vkstech.androidsecure.exceptionHandler.ErrorMessageHandler;
import com.vkstech.androidsecure.utils.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private String accessToken;
    private Gson gson;
    private EditText username, password;
    private ErrorMessageHandler errorHandler;
    private ProgressBar loader;

    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        contextOfApplication = this.getApplicationContext();

        getSupportActionBar().setTitle("Login");

        errorHandler = new ErrorMessageHandler();

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loader = findViewById(R.id.progressBar);
        loader.setVisibility(View.GONE);

        sharedPreferences = this.getSharedPreferences(ApplicationConstants.BASE_PACKAGE_NAME, Context.MODE_PRIVATE);

        accessToken = sharedPreferences.getString(SharedPreferencesKeys.ACCESS_TOKEN, null);

        if (accessToken != null) {
            gotoMainActivity();
        }

        gson = new Gson();
    }

    public void login(View view) {
        loader.setVisibility(View.VISIBLE);

        LoginForm loginForm = new LoginForm();
        loginForm.setUsername(username.getText().toString());
        loginForm.setPassword(password.getText().toString());

        ApiUtil.getAuthApiRequest().login(loginForm).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                AccessTokenResponse accessTokenResponse;
                if (response.code() == 200 && response.body() != null) {
                    String jsonString = gson.toJson(response.body().getData());
                    accessTokenResponse = gson.fromJson(jsonString, AccessTokenResponse.class);

                    Toast.makeText(LoginActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().putString(SharedPreferencesKeys.ACCESS_TOKEN, accessTokenResponse.getAccessToken()).apply();
                    sharedPreferences.edit().putString(SharedPreferencesKeys.REFRESH_TOKEN, accessTokenResponse.getRefreshToken()).apply();
                    gotoMainActivity();

                } else {
                    loader.setVisibility(View.GONE);
                    errorHandler.handleRetrofitErrorMessage(response, LoginActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("error", t.getMessage());
                loader.setVisibility(View.GONE);
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        loader.setVisibility(View.GONE);
        startActivity(intent);
        finish();
    }

    public void gotoSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
