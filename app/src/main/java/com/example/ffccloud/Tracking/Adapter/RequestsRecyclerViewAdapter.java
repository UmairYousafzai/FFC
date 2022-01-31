package com.example.ffccloud.Tracking.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.LocationRequestedUser;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.PushNotification.SendNoticationClass;
import com.example.ffccloud.R;
import com.example.ffccloud.Tracking.Services.LocationService;
import com.example.ffccloud.UserModel;
import com.example.ffccloud.databinding.LocationRequestCardBinding;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.Permission;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RequestsRecyclerViewAdapter extends RecyclerView.Adapter<RequestsRecyclerViewAdapter.RequestsViewholder> {


    private LayoutInflater layoutInflater;
    public List<LocationRequestedUser> userList;
    private Context context;
    private LocationRequestedUser user = new LocationRequestedUser();
    private ProgressDialog progressDialog;
    private Activity activity;


    public RequestsRecyclerViewAdapter(Context context, Activity activity) {
        userList = new ArrayList<>();
        this.context = context;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(context.getResources(), R.color.white, null)));
        this.activity = activity;
    }

    @NonNull
    @Override
    public RequestsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        LocationRequestCardBinding binding = LocationRequestCardBinding.inflate(layoutInflater, parent, false);

        return new RequestsViewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestsViewholder holder, int position) {
        LocationRequestedUser user = userList.get(position);

        //sharing state 1= pending , 2= running, 3= stop
        user.setSharingState(1);
        holder.mBinding.status.setText("Loading");
        holder.mBinding.setUser(user);
        holder.mBinding.executePendingBindings();
        if (SharedPreferenceHelper.getInstance(context).getLocationDoneButtonState(user.getId()))
        {
            holder.mBinding.btnDone.setVisibility(View.GONE);
        }else
        {
            holder.mBinding.btnDone.setVisibility(View.VISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void setUserList(List<LocationRequestedUser> list) {
        if (list != null && list.size() > 0) {
            userList.clear();
            userList.addAll(list);

        } else {
            userList.clear();
        }
        notifyDataSetChanged();
    }


    public class RequestsViewholder extends RecyclerView.ViewHolder {
        LocationRequestCardBinding mBinding;

        public RequestsViewholder(@NonNull LocationRequestCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

            mBinding.btnDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Permission permission = new Permission(context, activity);
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        if (permission.isLocationEnabled()) {
                            if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                                progressDialog.show();
                                user = userList.get(getAdapterPosition());
                                UserModel userModel = new UserModel();
                                userModel.setId(user.getId());
                                userModel.setEmail(user.getEmail());
                                userModel.setUserName(user.getUserName());

                                String userID = String.valueOf(SharedPreferenceHelper.getInstance(context).getUserID());

                                databaseReference.child(userID).child("requestAccepted").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SharedPreferenceHelper.getInstance(context).setLocationDoneButtonState(user.getId(),true);
                                        sendNotification(userModel);

                                        Intent intent = new Intent(context, LocationService.class);
                                        intent.setAction(CONSTANTS.ACTION_START_LOCATION_SERVICE);
                                        context.startService(intent);

                                        Toast.makeText(context, "Location Sharing Enable", Toast.LENGTH_SHORT).show();
                                        mBinding.btnDone.setVisibility(View.GONE);
                                        progressDialog.dismiss();


                                    }
                                }).addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        SharedPreferenceHelper.getInstance(context).setLocationDoneButtonState(user.getId(),false);

                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Enable to start location service", Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        } else {
                            permission.showOpenLocationSettingDialog();
                        }
                    } else {
                        permission.getLocationPermission();
                    }


                }


            });

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        user = userList.get(getAdapterPosition());
                        UserModel userModel = new UserModel();
                        userModel.setId(user.getId());
                        userModel.setEmail(user.getEmail());
                        userModel.setUserName(user.getUserName());
                        String userID = String.valueOf(SharedPreferenceHelper.getInstance(context).getUserID());
                        SharedPreferenceHelper.getInstance(context).setLocationDoneButtonState(user.getId(),false);

                        progressDialog.show();
                        LocationRequestedUser user = userList.get(getAdapterPosition());
                        databaseReference.child(userID).child("requestAccepted").setValue(false).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                FirebaseDatabase.getInstance().getReference("Users").child(userID).child("lastLocation").setValue("");
                                Intent intent = new Intent(context, LocationService.class);
                                intent.setAction(CONSTANTS.ACTION_STOP_LOCATION_SERVICE);
                                context.startService(intent);
                                Toast.makeText(context, "Location Sharing Disable", Toast.LENGTH_SHORT).show();
                                FfcDatabase ffcDatabase = FfcDatabase.getInstance(context);
                                ffcDatabase.dao().deleteUser(user);
                                progressDialog.dismiss();
                            }
                        });

                    }


                }
            });


        }
        private void sendNotification(UserModel user) {
            SendNoticationClass sendNoticationClass= new SendNoticationClass(context);
            FfcDatabase ffcDatabase = FfcDatabase.getInstance(context);
            GetUserInfoModel senderUser=ffcDatabase.dao().getLoginUser();
            String message = senderUser.getUserName()+ " has accepted your request.";
            sendNoticationClass.UpdateToken();

            String userID= user.getId();

            FirebaseDatabase.getInstance().getReference().child("Tokens").child(userID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String receiverUserToken = dataSnapshot.getValue(String.class);
                    progressDialog.dismiss();
                    sendNoticationClass.sendNotifications(receiverUserToken, senderUser, "Location Request", message, context);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
