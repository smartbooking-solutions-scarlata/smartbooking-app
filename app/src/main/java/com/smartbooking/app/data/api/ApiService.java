package com.smartbooking.app.data.api;

import com.smartbooking.app.data.model.LoginRequest;
import com.smartbooking.app.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/login/")
    Call<LoginResponse> login(@Body LoginRequest body);
}