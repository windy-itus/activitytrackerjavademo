package com.example.activity_tracker_sdk_java.data.network;

import com.example.activity_tracker_sdk_java.data.models.RequestData;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public interface EventApiService {
    @PUT
    Call<Response<ResponseBody>> sendData(@Url String url, @Body RequestData requestData);
}
