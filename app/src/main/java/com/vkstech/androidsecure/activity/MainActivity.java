package com.vkstech.androidsecure.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vkstech.androidsecure.R;
import com.vkstech.androidsecure.constants.ApplicationConstants;
import com.vkstech.androidsecure.constants.SharedPreferencesKeys;
import com.vkstech.androidsecure.dto.AccessTokenResponse;
import com.vkstech.androidsecure.dto.Address;
import com.vkstech.androidsecure.dto.ResponseObject;
import com.vkstech.androidsecure.exceptionHandler.ErrorMessageHandler;
import com.vkstech.androidsecure.utils.AddressRecyclerViewAdapter;
import com.vkstech.androidsecure.utils.ApiUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SharedPreferences sharedPreferences;

    private RecyclerView recyclerView;
    private AddressRecyclerViewAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<Address> addressList;

    private ErrorMessageHandler errorHandler;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("Address Book");

        sharedPreferences = getApplicationContext().getSharedPreferences(ApplicationConstants.BASE_PACKAGE_NAME, Context.MODE_PRIVATE);

        swipeRefreshLayout = findViewById(R.id.swipe_container);
        recyclerView = findViewById(R.id.recyclerView);
        addressList = new ArrayList<>();
        adapter = new AddressRecyclerViewAdapter(addressList, this);

        gson = new Gson();

        errorHandler = new ErrorMessageHandler();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {

                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(true);
                loadData();
            }
        });


    }

    private void loadData() {
        String accessToken = sharedPreferences.getString(SharedPreferencesKeys.ACCESS_TOKEN, null);

        if (accessToken == null)
            return;

        addressList.clear();

        String bearerToken = ApplicationConstants.BEARER_TOKEN_TITLE + accessToken;

        ApiUtil.getResourceApiRequest().getMyAddresses(bearerToken).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {

                if (response.code() == 200 && response.body() != null) {
                    String jsonString = gson.toJson(response.body().getData());
                    addressList = gson.fromJson(jsonString, new TypeToken<List<Address>>() {
                    }.getType());

                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                    adapter.updateDataSet(addressList);
                    swipeRefreshLayout.setRefreshing(false);

                    recyclerView.scrollToPosition(addressList.size() - 1);
                } else {
                    errorHandler.handleRetrofitErrorMessage(response, MainActivity.this);
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Log.e("error", t.getMessage());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    public void gotoNewAddressActivity(View view) {
        Intent intent = new Intent(this, NewAddressActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {

            case R.id.logout:
                logout();
                return true;

            default:
                return false;
        }
    }

    private void logout() {
        swipeRefreshLayout.setRefreshing(true);

        String accessToken = sharedPreferences.getString(SharedPreferencesKeys.ACCESS_TOKEN, null);

        if (accessToken == null)
            return;

        String bearerToken = ApplicationConstants.BEARER_TOKEN_TITLE + accessToken;

        ApiUtil.getAuthApiRequest().logout(bearerToken).enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
            }
        });

        sharedPreferences
                .edit()
                .remove(SharedPreferencesKeys.ACCESS_TOKEN)
                .remove(SharedPreferencesKeys.REFRESH_TOKEN)
                .commit();

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();

    }
}
