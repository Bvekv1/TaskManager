package com.bibek.taskmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bibek.taskmanager.api.UsersAPI;
import com.bibek.taskmanager.model.Users;
import com.bibek.taskmanager.serverresponse.ImageResponse;
import com.bibek.taskmanager.serverresponse.SignUpResponse;
import com.bibek.taskmanager.strictmode.StrictModeClass;
import com.bibek.taskmanager.url.URL;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

public class RegisterActivity extends AppCompatActivity {
    EditText  etFirstName, etLastName, etUser, etPassword, etConfirmPassword;
    Button btnBack, btnSignUp;
    ImageView imageView;
    String imagePath;
    String imageName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnBack = findViewById(R.id.btnBack);
        imageView = findViewById(R.id.imageView);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        etConfirmPassword = findViewById(R.id.etPassword);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString()) ) {
                    saveImageOnly();
                    registerUser();
                }
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }
    private  void back(){
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private  void loadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(this, "select image", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri = data.getData();

        imagePath = getRealPathFromURI(uri);
        imageView.setImageURI(uri);
        //previewImage(imagePath);
        Toast.makeText(this,"load" +uri,Toast.LENGTH_SHORT);
    }
    private  String getRealPathFromURI(Uri uri){
        String[] projection ={MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null,null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return  result;
    }
    private void saveImageOnly(){
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",file.getName(),requestBody);

        UsersAPI usersAPI  = URL.getInstance().create(UsersAPI.class);
        Call<ImageResponse> responseBodyCall = usersAPI.uploadImage(body);

        StrictModeClass.StrictMode();
        try {

            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            imageName = imageResponseResponse.body().getFilename();
        } catch (IOException e){
            Toast.makeText(this,"Error " + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private  void registerUser(){
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        String username = etUser.getText().toString();
        String password = etPassword.getText().toString();


        Users users = new Users(firstName,lastName,username,password,imageName);
        UsersAPI usersAPI =URL.getInstance().create(UsersAPI.class);
        Call<SignUpResponse> usersCall = usersAPI.registerUser(users);

        usersCall.enqueue(new Callback<SignUpResponse>() {
            @Override
            public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                if (!response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(RegisterActivity.this, "Sucessfully added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SignUpResponse> call, Throwable t) {
                  Toast.makeText(RegisterActivity.this, "Error" + t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });




    }

}
