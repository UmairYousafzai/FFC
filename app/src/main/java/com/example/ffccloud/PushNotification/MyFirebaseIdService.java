package com.example.ffccloud.PushNotification;

import androidx.annotation.NonNull;

import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseIdService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);



            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                   String  refreshToken=task.getResult();
                    updateToken(refreshToken);

                }
            });
    }
    private void updateToken(String refreshToken){
        Token token1= new Token(refreshToken);
        String userID= String.valueOf(SharedPreferenceHelper.getInstance(this).getUserID());
        FirebaseDatabase.getInstance().getReference("Tokens").child(userID).setValue(token1);
    }
}
