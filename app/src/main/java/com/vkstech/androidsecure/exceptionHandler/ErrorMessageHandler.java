package com.vkstech.androidsecure.exceptionHandler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.vkstech.androidsecure.dto.ResponseObject;

import retrofit2.Response;

public class ErrorMessageHandler {

    public void handleRetrofitErrorMessage(Response<ResponseObject> response, Context context){
        Log.d("Error Response", "onResponse - Status : " + response.code());
        Gson gson = new Gson();
        ResponseObject registerResponse;
        TypeAdapter<ResponseObject> adapter = gson.getAdapter(ResponseObject.class);
        try {
            if (response.errorBody() != null) {
                String responseJson = response.errorBody().string();
                registerResponse = adapter.fromJson(responseJson);
                Log.e("Error", registerResponse.getMessage());
                Toast.makeText(context, registerResponse.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
