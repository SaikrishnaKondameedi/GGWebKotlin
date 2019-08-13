package com.example.myapplication;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface GetData {
    @GET(".")
    Call<ResponseBody> getData();
}
