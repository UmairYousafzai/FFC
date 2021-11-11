package com.example.ffccloud;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.ffccloud.Login.LoginModel;
import com.example.ffccloud.Login.TokenResponseModel;

import static java.util.Objects.requireNonNull;

public class LoginViewModel<loginViewModel> extends ViewModel {
    public LoginViewModel loginViewModel;
    LoginModel loginUser;

    public MutableLiveData<String> EmailAddress = new MutableLiveData<>();
    public MutableLiveData<String> Password = new MutableLiveData<>();

    private MutableLiveData<LoginModel> userMutableLiveData;

    private LiveData<TokenResponseModel> tokenResponseModelLiveData;

    public LiveData<TokenResponseModel> getTokenResponseLiveData() {
        return tokenResponseModelLiveData;
    }

    public MutableLiveData<LoginModel> getUser() {

        if (userMutableLiveData == null) {
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;

    }
    public void onClick(View view) {
        loginUser = new LoginModel(EmailAddress.getValue(), Password.getValue());
        userMutableLiveData.setValue(loginUser);
    }
}