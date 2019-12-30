package com.bibek.taskmanager.bbl;

import com.bibek.taskmanager.api.UsersAPI;
import com.bibek.taskmanager.serverresponse.SignUpResponse;
import com.bibek.taskmanager.url.URL;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginLogic {
    private Boolean isSuccess = false;

    public boolean checkUser(String username, String password){
        UsersAPI usersAPI = URL.getInstance().create(UsersAPI.class);
        Call<SignUpResponse> userCall = usersAPI.checkUser(username, password);

        try {
            Response<SignUpResponse> loginResponse = userCall.execute();
            if (loginResponse.isSuccessful() && loginResponse.body().getStatus().equals("Login sucess")){
                URL.token = loginResponse.body().getToken();
                isSuccess= true;
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;

    }
}
