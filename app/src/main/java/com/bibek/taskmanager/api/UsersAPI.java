package com.bibek.taskmanager.api;

import com.bibek.taskmanager.model.Users;
import com.bibek.taskmanager.serverresponse.ImageResponse;
import com.bibek.taskmanager.serverresponse.SignUpResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface UsersAPI {
    @POST("users/signup")
    Call<SignUpResponse> registerUser(@Body Users users);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    @FormUrlEncoded
    @POST("users/login")
    Call<SignUpResponse> checkUser(@Body String username, String password);

}
