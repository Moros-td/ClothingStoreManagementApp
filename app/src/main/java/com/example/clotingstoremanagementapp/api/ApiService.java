package com.example.clotingstoremanagementapp.api;

import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.example.clotingstoremanagementapp.response.LoginResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    HttpLoggingInterceptor httpLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            //.addInterceptor(interceptor)
            .addInterceptor(httpLogging);

    Gson gson = new GsonBuilder()
            .setDateFormat("dd-MM-yyyy HH:mm:ss")
            .create();

    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8097/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okhttpBuilder.build())
            .build()
            .create(ApiService.class);


    @FormUrlEncoded
    @POST("/Auth/Login")
    Call<LoginResponse> logIn(@Field("username") String username,  // Dữ liệu email
                              @Field("password") String password);  // Dữ liệu password);

    @FormUrlEncoded
    @POST("/Auth/Logout")
    Call<LoginResponse> loginOut(@Header ("Authorization") String token,
                                 @Field("username") String username);

    @GET("/Dashboard_category/getAllCategories")
    Call<List<CategoryEntity>> getAllCategories(@Header ("Authorization") String token);
}
