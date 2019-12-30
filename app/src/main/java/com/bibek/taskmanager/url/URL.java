package com.bibek.taskmanager.url;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class URL {
    public static final String BASE_URL = "http://10.0.2.2:3000/";
    public static String token= "";
    // public static final String BASE_URL = "http://172.100.100.5:3000";

    public static Retrofit getInstance() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory
                        .create())
                .build();
        return retrofit;
    }
}
