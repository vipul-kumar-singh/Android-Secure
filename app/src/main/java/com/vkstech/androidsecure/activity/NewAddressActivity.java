package com.vkstech.androidsecure.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vkstech.androidsecure.R;
import com.vkstech.androidsecure.constants.ApplicationConstants;
import com.vkstech.androidsecure.constants.SharedPreferencesKeys;
import com.vkstech.androidsecure.dto.AddressDto;
import com.vkstech.androidsecure.dto.ResponseObject;
import com.vkstech.androidsecure.exceptionHandler.ErrorMessageHandler;
import com.vkstech.androidsecure.utils.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAddressActivity extends AppCompatActivity {

    private ProgressBar loader;
    private EditText houseNumber, area, city, state, pinCode;

    private SharedPreferences sharedPreferences;
    private String accessToken;
    private ErrorMessageHandler errorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        getSupportActionBar().setTitle("Add New Address");

        houseNumber = findViewById(R.id.houseNumberEditText);
        area = findViewById(R.id.areaEditText);
        city = findViewById(R.id.cityEditText);
        state = findViewById(R.id.stateEditText);
        pinCode = findViewById(R.id.pinCodeEditText);

        loader = findViewById(R.id.addNewAddressprogressBar);
        loader.setVisibility(View.GONE);

        errorHandler = new ErrorMessageHandler();
        sharedPreferences = getApplicationContext().getSharedPreferences(ApplicationConstants.BASE_PACKAGE_NAME, Context.MODE_PRIVATE);
    }

    public void addNewAddress(View view) {
        loader.setVisibility(View.VISIBLE);

        accessToken = sharedPreferences.getString(SharedPreferencesKeys.ACCESS_TOKEN, null);

        if (accessToken == null) {
            Toast.makeText(this, "Please login to continue..", Toast.LENGTH_SHORT).show();
            gotoLoginActivity();
            return;
        }

        String bearerToken = ApplicationConstants.BEARER_TOKEN_TITLE + accessToken;

        AddressDto addressDto = new AddressDto();
        addressDto.setHouseNumber(houseNumber.getText().toString().isEmpty() ? null : Integer.valueOf(houseNumber.getText().toString()));
        addressDto.setCity(city.getText().toString());
        addressDto.setArea(area.getText().toString());
        addressDto.setState(state.getText().toString());
        addressDto.setPinCode(pinCode.getText().toString().isEmpty() ? null : Integer.valueOf(pinCode.getText().toString()));

        ApiUtil.getResourceApiRequest().addAddress(bearerToken, addressDto).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {

                if (response.code() == 200 && response.body() != null) {

                    Toast.makeText(NewAddressActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    loader.setVisibility(View.GONE);

                    gotoMainActivity();
                } else {
                    loader.setVisibility(View.GONE);
                    errorHandler.handleRetrofitErrorMessage(response, NewAddressActivity.this);
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toast.makeText(NewAddressActivity.this, "Error while connecting..", Toast.LENGTH_SHORT).show();
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

    private void gotoLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        loader.setVisibility(View.GONE);
        startActivity(intent);
        finish();
    }
}
