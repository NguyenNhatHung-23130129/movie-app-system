package com.example.movie_app.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movie_app.models.LoginRequest;
import com.example.movie_app.models.LoginResponse;
import com.example.movie_app.models.RegisterRequest;
import com.example.movie_app.models.RegisterResponse;
import com.example.movie_app.network.ApiService;
import com.example.movie_app.network.RetrofitClient;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private ApiService apiService;

    public AuthRepository() {
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
    }

    public LiveData<LoginResponse> login(String email, String password) {
        MutableLiveData<LoginResponse> data = new MutableLiveData<>();
        LoginRequest request = new LoginRequest(email, password);

        apiService.loginUser(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                }

                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                } else {
                    LoginResponse errorResponse = new LoginResponse();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Server báo lỗi: " + response.code());
                    data.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginResponse errorResponse = new LoginResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Lỗi mạng: " + t.getMessage());
                data.postValue(errorResponse);
            }
        });
        return data;
    }

    public LiveData<RegisterResponse> register(String name, String email, String password) {
        MutableLiveData<RegisterResponse> data = new MutableLiveData<>();
        RegisterRequest request = new RegisterRequest(name, email, password);

        // Gọi API Đăng ký
        apiService.registerUser(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    data.postValue(response.body());
                } else {
                    RegisterResponse errorResponse = new RegisterResponse();
                    errorResponse.setSuccess(false);
                    errorResponse.setMessage("Tài khoản đã tồn tại hoặc xảy ra lỗi!");
                    data.postValue(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                RegisterResponse errorResponse = new RegisterResponse();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Lỗi kết nối mạng: " + t.getMessage());
                data.postValue(errorResponse);
            }
        });
        return data;
    }
}