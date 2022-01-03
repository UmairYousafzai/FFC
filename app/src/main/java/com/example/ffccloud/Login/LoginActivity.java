package com.example.ffccloud.Login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.biometrics.BiometricManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.LoginViewModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.MainActivity;

import com.example.ffccloud.R;
import com.example.ffccloud.databinding.ActivityLoginBinding;
import com.example.ffccloud.databinding.CustomDialogBiometricBinding;
import com.example.ffccloud.databinding.DialogCustomImeiNumberBinding;
import com.example.ffccloud.databinding.DialogCustomResetUrlBinding;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executor;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class LoginActivity extends AppCompatActivity {
    SweetAlertDialog pDialog;
    public LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    String a, b, emailPattern, email_dialog;
    Dialog mydialog;
    TextView heading_reset_password, txtclose, heading_reset_server, text_reset_server, heading_config_imei, text_config_imei, text_imei_number, get_imei_number;
    MaterialButton set_button, password_reset_button, set_imei_button;
    TextInputLayout Text_Input_Email_Dialog, Text_Input_Code_Dialog, Text_Input_New_Password_Dialog;
    TextInputEditText Edit_Text_Email_Dialog, Edit_Text_Code_Dialog, Edit_Text_New_Password_Dialog;
    EditText reset_edit_text;
    String urll;
    String token;
    FfcDatabase ffcDatabase;

    String IMEINumber, IMEINumber2;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(LoginActivity.this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_login);

        binding.setLifecycleOwner(LoginActivity.this);

        binding.setLoginViewModel(loginViewModel);

        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        mydialog = new Dialog(LoginActivity.this);

        urll = SharedPreferenceHelper.getInstance(getApplication()).getBaseUrl();

        ffcDatabase = FfcDatabase.getInstance(getApplicationContext());


        if (urll == null) {
            SharedPreferenceHelper.getInstance(getApplication()).setBaseUrl("http://161.97.178.106/FFCCloudapi/");
        }

        showOptionalBiometricDialog();


        loginViewModel.getUser().observe(this, new Observer<LoginModel>() {
            @Override
            public void onChanged(@Nullable LoginModel loginUser) {
                binding.TextInputEmail.setError(null);
                binding.TextInputPassword.setError(null);

                a = loginUser.getStrEmailAddress();
                b = loginUser.getStrPassword();

                if (a == null) {
                    binding.TextInputEmail.setError("Email field should not be empty");
                    binding.TextInputEmail.requestFocus();
                }
                if (b == null) {
                    binding.TextInputPassword.setError("Password field should not be empty");
                    binding.TextInputPassword.requestFocus();
                }
                if (a != null) {
                    if (!a.matches(emailPattern)) {
                        binding.TextInputEmail.setError("Please enter a valid e-mail");
                        binding.TextInputPassword.requestFocus();
                    }
                }
                if (a != null && b != null && a.matches(emailPattern)) {
                    binding.TextInputEmail.setError(null);
                    binding.TextInputPassword.setError(null);

                    String email = a;
                    String password = b;
                    String grant_type = "password";
                    pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#286A9C"));
                    pDialog.setTitleText("Loading");
                    //pDialog.setCancelable(false);
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.show();
                    login(email, password, grant_type);
                }
            }
        });

        binding.getImeiNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogCustomImeiNumberBinding dialogBinding = DialogCustomImeiNumberBinding.inflate(getLayoutInflater());
                mydialog.setContentView(dialogBinding.getRoot());
                heading_config_imei = mydialog.findViewById(R.id.heading_config_imei);

                dialogBinding.setImeiButton.setVisibility(View.INVISIBLE);
                mydialog.setCanceledOnTouchOutside(false);
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogBinding.txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });

                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    IMEINumber = telephonyManager.getDeviceId(0);
                }
                dialogBinding.textImeiNumber.setText(IMEINumber);

                mydialog.show();
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydialog.setContentView(R.layout.dialog_custom_forgot_password);
                heading_reset_password = mydialog.findViewById(R.id.heading_reset_password);
                Text_Input_Email_Dialog = mydialog.findViewById(R.id.Text_Input_Email_Dialog);
                Edit_Text_Email_Dialog = mydialog.findViewById(R.id.Edit_Text_Email_Dialog);

                Text_Input_Code_Dialog = mydialog.findViewById(R.id.Text_Input_Code_Dialog);
                Edit_Text_Code_Dialog = mydialog.findViewById(R.id.Edit_Text_Code_Dialog);

                Text_Input_New_Password_Dialog = mydialog.findViewById(R.id.Text_Input_New_Password_Dialog);
                Edit_Text_New_Password_Dialog = mydialog.findViewById(R.id.Edit_Text_New_Password_Dialog);

                txtclose = mydialog.findViewById(R.id.txtclose);
                set_button = mydialog.findViewById(R.id.set_button);
                password_reset_button = mydialog.findViewById(R.id.reset_button);

                mydialog.setCanceledOnTouchOutside(false);
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });

                set_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Text_Input_Email_Dialog.setError(null);
                        email_dialog = Edit_Text_Email_Dialog.getText().toString();
                        if (email_dialog.isEmpty()) {
                            Text_Input_Email_Dialog.setError("Email field should not be empty");
                        }
                        if (!email_dialog.matches(emailPattern) && !email_dialog.isEmpty()) {
                            Text_Input_Email_Dialog.setError("Please enter a valid e-mail");
                        }
                        if (!email_dialog.isEmpty() && email_dialog.matches(emailPattern)) {
                            pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Loading");
                            //pDialog.setCancelable(false);
                            pDialog.setCanceledOnTouchOutside(false);
                            pDialog.show();
                            forgot_password(email_dialog);
                        }
                    }
                });

                password_reset_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Text_Input_Code_Dialog.setError(null);
                        Text_Input_New_Password_Dialog.setError(null);
                        String code_dialog = Edit_Text_Code_Dialog.getText().toString();
                        String new_password_dialog = Edit_Text_New_Password_Dialog.getText().toString();
                        if (code_dialog.isEmpty()) {
                            Text_Input_Code_Dialog.setError("Code field should not be empty");
                        }
                        if (new_password_dialog.isEmpty()) {
                            Text_Input_New_Password_Dialog.setError("Password field should not be empty");
                        }
                        if (!code_dialog.isEmpty() && !new_password_dialog.isEmpty()) {
                            pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Loading");
                            //pDialog.setCancelable(false);
                            pDialog.setCanceledOnTouchOutside(false);
                            pDialog.show();
                            SetNewPassword(email_dialog, code_dialog, new_password_dialog);
                        }
                    }
                });

                mydialog.show();
            }

        });

        binding.ResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogCustomResetUrlBinding dialogBinding = DialogCustomResetUrlBinding.inflate(getLayoutInflater());
                mydialog.setContentView(dialogBinding.getRoot());
                mydialog.setCanceledOnTouchOutside(false);
                mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                dialogBinding.resetEditText.setText(urll);


                dialogBinding.setButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String url = dialogBinding.resetEditText.getText().toString();
                        if (!url.isEmpty()) {
                            if ((url.substring(url.length() - 1).equals("/")) && url.contains("http://") && url.contains(".") && !url.contains(" ")) {
                                dialogBinding.resetEditText.setText(url);
                                SharedPreferenceHelper.getInstance(getApplication()).setBaseUrl(url);
                                mydialog.dismiss();
                            } else {
                                dialogBinding.resetEditText.setError("Invalid Url");
                            }
                        } else {
                            dialogBinding.resetEditText.setError("Please enter Url");
                        }

                    }
                });

                dialogBinding.txtclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mydialog.dismiss();
                    }
                });

                mydialog.show();
            }
        });
    }

    private void forgot_password(String email_dialog) {

        Call<ForgotPasswordModel> call = ApiClient.getInstance().getApi().forgot_password(email_dialog);
        call.enqueue(new Callback<ForgotPasswordModel>() {
            @Override
            public void onResponse(Call<ForgotPasswordModel> call, Response<ForgotPasswordModel> response) {
                ForgotPasswordModel forgotPasswordModel = response.body();
                if (response.isSuccessful()) {
                    response.body().getStrMessage();

                    String msg = response.body().getStrMessage();
                    int status = response.body().getStatus();

                    if (status == 0) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Try Again")
                                .setContentText("Something went wrong.")
                                .show();
                        pDialog.cancel();
                    }
                    if (msg.equals("Code is Successfully Sent to your Email Please Check Your Email to Get the Security Code!!") && status == 1) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Success")
                                .setContentText("Code successfully sent to your given email address.")
                                .show();
                        pDialog.cancel();
                        Text_Input_Email_Dialog.setVisibility(GONE);
                        set_button.setVisibility(GONE);

                        Text_Input_Code_Dialog.setVisibility(View.VISIBLE);
                        Text_Input_New_Password_Dialog.setVisibility(View.VISIBLE);
                        password_reset_button.setVisibility(View.VISIBLE);
                    }

                    //Toast.makeText(LoginActivity.this, "Message  " + msg , Toast.LENGTH_SHORT).show();
                    pDialog.cancel();


                } else {
                    int code = response.code();
                    switch (code) {
                        case 400: {
                            pDialog.cancel();
                            new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Bad Request")
                                    .setContentText("Provided username or password is incorrect.")
                                    .show();
                            pDialog.cancel();
                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<ForgotPasswordModel> call, Throwable t) {
                pDialog.cancel();
                String error = t.getMessage();

                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setConfirmText("OK")
                        .setTitleText("Try Again")
                        .show();
            }
        });

    }

    public void login(String email, String password, String grant_type) {

        Call<TokenResponseModel> call = ApiClient.getInstance().getApi().token(email, password, grant_type);
        call.enqueue(new Callback<TokenResponseModel>() {
            @Override
            public void onResponse(@NotNull Call<TokenResponseModel> call, @NotNull Response<TokenResponseModel> response) {
                TokenResponseModel tokenResponseModel = response.body();
                if (response.isSuccessful()) {

                    String token = response.body() != null ? response.body().getAccessToken() : "";


                    SharedPreferenceHelper.getInstance(getApplication()).setToken("bearer " + token);

                    getUserInfo(email, password);
                    SharedPreferenceHelper.getInstance(LoginActivity.this).setUserPassword(password);

                    String check = SharedPreferenceHelper.getInstance(LoginActivity.this).getUserPassword();

                    pDialog.cancel();
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();

                } else {


                    new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(response.message())
                            .show();

                }


            }


            @Override
            public void onFailure(Call<TokenResponseModel> call, Throwable t) {
                pDialog.cancel();
                String error = t.getMessage();
                Log.e("url", "" + urll);

                Log.e("error login", "" + error);
                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setConfirmText("OK")
                        .setTitleText("Try Again")
                        .show();
            }
        });

    }

    private void getUserInfo(String email, String password) {

        Call<GetUserInfoModel> call = ApiClient.getInstance().getApi().get_user(email, password);
        call.enqueue(new Callback<GetUserInfoModel>() {
            @Override
            public void onResponse(Call<GetUserInfoModel> call, Response<GetUserInfoModel> response) {
                GetUserInfoModel getUserInfoModel = new GetUserInfoModel();
                if (response.isSuccessful()) {
                    getUserInfoModel = response.body();
                    ffcDatabase.dao().delete_previous_user();
                    ffcDatabase.dao().insertUserInfo(getUserInfoModel);

                    SharedPreferenceHelper.getInstance(getApplicationContext()).setEmpID(getUserInfoModel.getEmpId());
                    SharedPreferenceHelper.getInstance(getApplicationContext()).setUserID(getUserInfoModel.getID());

                    String a = ffcDatabase.dao().device_config().toString();

                    if (a.equals("true")) {

                        mydialog.setContentView(R.layout.dialog_custom_imei_number);
                        heading_config_imei = mydialog.findViewById(R.id.heading_config_imei);
                        text_config_imei = mydialog.findViewById(R.id.text_config_imei);
                        text_imei_number = mydialog.findViewById(R.id.text_imei_number);
                        set_imei_button = mydialog.findViewById(R.id.set_imei_button);
                        set_imei_button.setVisibility(View.VISIBLE);
                        txtclose = mydialog.findViewById(R.id.txtclose);
                        mydialog.setCanceledOnTouchOutside(false);
                        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        txtclose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mydialog.dismiss();
                            }
                        });

                        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE);
                            return;
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            IMEINumber = telephonyManager.getDeviceId(0);
                        }
                        text_imei_number.setText(IMEINumber);

                        set_imei_button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String imei = ffcDatabase.dao().device_address();
                                if (imei.equals(IMEINumber)) {
                                    int id = response.body().getID();

                                    token = SharedPreferenceHelper.getInstance(getApplication()).getToken();

                                    SharedPreferenceHelper.getInstance(getApplication()).setID(id);
                                    getUserMenu(token, id);
                                } else {
                                    Toast.makeText(LoginActivity.this, "NOTTT Match", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                        mydialog.show();
                    } else if (a.equals("false")) {
                        int id = response.body().getID();

                        token = SharedPreferenceHelper.getInstance(getApplication()).getToken();

                        SharedPreferenceHelper.getInstance(getApplication()).setID(id);

                        pDialog.cancel();

                        getUserMenu(token, id);

                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserInfoModel> call, Throwable t) {
                pDialog.cancel();
                String error = t.getMessage();

                    /*new SweetAlertDialog(LoginActivity.this,SweetAlertDialog.ERROR_TYPE)
                            .setConfirmText("OK")
                            .setTitleText(t.getMessage().toString())
                            .show();*/
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getUserMenu(String token, int id) {
        Call<List<GetUserMenuModel>> call = ApiClient.getInstance().getApi().get_user_menu(token, id);
        call.enqueue(new Callback<List<GetUserMenuModel>>() {
            @Override
            public void onResponse(Call<List<GetUserMenuModel>> call, Response<List<GetUserMenuModel>> response) {
                GetUserMenuModel getUserMenuModels = new GetUserMenuModel();

                if (response.isSuccessful()) {
                    ffcDatabase.dao().delete_all_menu();
                    assert response.body() != null;
                    int size = response.body().size();
                    for (int j = 0; j < size; j++) {
                        getUserMenuModels = response.body().get(j);
                        ffcDatabase.dao().insertMenu(getUserMenuModels);
                    }

                    List<String> a = ffcDatabase.dao().get_menu_State();


                    SharedPreferenceHelper.getInstance(getApplication()).setLogin_State(true);


                    getUserSetting(token, id);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    pDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<List<GetUserMenuModel>> call, Throwable t) {
                pDialog.cancel();
                String error = t.getMessage();

                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setConfirmText("OK")
                        .setTitleText(t.getMessage().toString())
                        .show();
            }
        });

    }

    private void getUserSetting(String token, int id) {
        Call<List<GetUserSettingModel>> call = ApiClient.getInstance().getApi().get_user_setting(token, id);
        call.enqueue(new Callback<List<GetUserSettingModel>>() {
            @Override
            public void onResponse(Call<List<GetUserSettingModel>> call, Response<List<GetUserSettingModel>> response) {
                GetUserSettingModel getUserSettingModel = new GetUserSettingModel();

                if (response.isSuccessful()) {
                    ffcDatabase.dao().delete_all_setting();
                    int size = response.body().size();
                    for (int j = 0; j < size; j++) {
                        getUserSettingModel = response.body().get(j);
                        ffcDatabase.dao().insertSetting(getUserSettingModel);
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    pDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<List<GetUserSettingModel>> call, Throwable t) {
                pDialog.cancel();
                String error = t.getMessage();

                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setConfirmText("OK")
                        .setTitleText(t.getMessage().toString())
                        .show();
            }
        });

    }

    public void SetNewPassword(String email, String code, String new_password) {

        Call<String> call = ApiClient.getInstance().getApi().new_password(email, code, new_password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String new_password_model = response.body();
                if (response.isSuccessful()) {
                    if (new_password_model.equals("Password Update Successfully.")) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setConfirmText("Success")
                                .setTitleText("Password has been changed successfully.")
                                .show();
                        mydialog.dismiss();
                    }

                    if (new_password_model.equals("Please Enter a Valid Code.")) {
                        new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setConfirmText("Try Again")
                                .setTitleText("Please enter correct code.")
                                .show();
                    }

                    pDialog.cancel();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pDialog.cancel();
                String error = t.getMessage();

                new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setConfirmText("OK")
                        .setTitleText("Try Again")
                        .show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }


    public void showOptionalBiometricDialog() {
        GetUserInfoModel userInfoModel = ffcDatabase.dao().getLoginUser();
        String password = SharedPreferenceHelper.getInstance(this).getUserPassword();

        if (userInfoModel != null) {

            if (userInfoModel.getEmail() != null && !userInfoModel.getEmail().isEmpty() && password != null && !password.isEmpty()) {
                CustomDialogBiometricBinding dialogBinding = CustomDialogBiometricBinding.inflate(getLayoutInflater());

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

                bottomSheetDialog.setContentView(dialogBinding.getRoot());


                dialogBinding.doneBtn.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onClick(View v) {


                        bottomSheetDialog.dismiss();
                        biometricAuthentication();


                    }
                });
                dialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onClick(View v) {


                        bottomSheetDialog.dismiss();


                    }
                });
                bottomSheetDialog.show();
            }
        }
    }

    public void biometricAuthentication() {
        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        androidx.biometric.BiometricManager biometricManager = androidx.biometric.BiometricManager.from(LoginActivity.this);
        switch (biometricManager.canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG | androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS: {


                break;

            }


            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE: {
                Toast.makeText(LoginActivity.this, "This device doesn't have a fingerprint sensor", Toast.LENGTH_SHORT).show();
                break;
            }


            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE: {
                Toast.makeText(LoginActivity.this, "The biometric sensor is currently unavailable", Toast.LENGTH_SHORT).show();
                break;
            }


            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(LoginActivity.this, "Your device doesn't have fingerprint saved,please check your security settings", Toast.LENGTH_SHORT).show();
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(LoginActivity.this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                GetUserInfoModel model = ffcDatabase.dao().getuserInfo();

                if (model != null) {
                    String email = model.getEmail();
                    String password = SharedPreferenceHelper.getInstance(LoginActivity.this).getUserPassword();

                    if (email != null && !email.isEmpty() && password != null && !password.isEmpty()) {
                        pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#286A9C"));
                        pDialog.setTitleText("Loading");
                        //pDialog.setCancelable(false);
                        pDialog.setCanceledOnTouchOutside(false);
                        pDialog.show();
                        login(email, password, "password");
                    } else {
                        Toast.makeText(LoginActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }


                }


            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("FFC")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        biometricPrompt.authenticate(promptInfo);

    }

}