package com.example.clotingstoremanagementapp.api;

import com.example.clotingstoremanagementapp.entity.AdminEntity;
import com.example.clotingstoremanagementapp.entity.CategoryEntity;
import com.example.clotingstoremanagementapp.entity.OrderEntity;
import com.example.clotingstoremanagementapp.entity.OrderItemEntity;
import com.example.clotingstoremanagementapp.entity.ProductEntity;
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
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.example.clotingstoremanagementapp.entity.TopProductEntity;
import com.example.clotingstoremanagementapp.entity.RevenueEntity;

public interface ApiService {

    HttpLoggingInterceptor httpLogging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

    OkHttpClient.Builder okhttpBuilder = new OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
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

    @GET("/Dashboard_statistical/getRevenueByMonth")
    Call<List<RevenueEntity>> getRevenueByMonth(@Header("Authorization") String token);

    @GET("/Dashboard_statistical/getRevenueByYear")
    Call<List<RevenueEntity>> getRevenueByYear(@Header("Authorization") String token);

    @GET("/Dashboard_statistical/getTopSellingProducts")
    Call<List<TopProductEntity>> getTopSellingProducts(@Header("Authorization") String token);
    @GET("/Dashboard_category/getAllCategories")
    Call<List<CategoryEntity>> getAllCategories(@Header ("Authorization") String token);

    @GET("/Dashboard_product/getAllProducts")
    Call<List<ProductEntity>> getAllProducts(@Header ("Authorization") String token);

    @GET("/Dashboard_staff/getAllStaffs")
    Call<List<AdminEntity>> getAllStaffs(@Header ("Authorization") String token);

    @FormUrlEncoded
    @POST("/Dashboard_staff/AddStaff")
    Call<LoginResponse> addStaff(@Header ("Authorization") String token, @Field("username") String username,
                                 @Field("password") String password, @Field("role") String role);

    @FormUrlEncoded
    @POST("/Dashboard_staff/EditStaff")
    Call<LoginResponse> editStaff(@Header ("Authorization") String token, @Field("username") String username,
                                  @Field("role") String role);

    @FormUrlEncoded
    @POST("/Dashboard_staff/DeleteStaff")
    Call<LoginResponse> deleteStaff(@Header ("Authorization") String token, @Field("username") String username);

    @FormUrlEncoded
    @POST("/Dashboard_staff/ResetPassword")
    Call<LoginResponse> resetPassword(@Header ("Authorization") String token, @Field("username") String username,
                                    @Field("password") String password);
    @GET("/Dashboard_order/getOrders")
    Call<List<OrderEntity>> getOrders(@Header("Authorization") String token);
}
