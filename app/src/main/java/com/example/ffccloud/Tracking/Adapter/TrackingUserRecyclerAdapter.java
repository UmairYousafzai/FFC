package com.example.ffccloud.Tracking.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;


import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.PushNotification.SendNoticationClass;
import com.example.ffccloud.R;
import com.example.ffccloud.Tracking.UsersListFragmentDirections;
import com.example.ffccloud.UserModel;
import com.example.ffccloud.databinding.UserCardBinding;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class TrackingUserRecyclerAdapter extends RecyclerView.Adapter<TrackingUserRecyclerAdapter.UserListViewHolder> {

    private LayoutInflater layoutInflater;
    public List<UserModel> userList;
    private final Context context;
    private SendNoticationClass sendNoticationClass;
    //    private final FirebaseAuth mAuth;
    private Fragment fragment;
    private ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;

    public TrackingUserRecyclerAdapter(Context context, Fragment fragment) {
        userList = new ArrayList<>();
        this.context = context;
//        mAuth = FirebaseAuth.getInstance();
        this.fragment = fragment;
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Adding User...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(context.getResources(), R.color.white, null)));
        firebaseDatabase = FirebaseDatabase.getInstance();

        sendNoticationClass = new SendNoticationClass(context);
    }

    @NonNull
    @Override
    public UserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        UserCardBinding binding = UserCardBinding.inflate(layoutInflater, parent, false);

        return new UserListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListViewHolder holder, int position) {
        UserModel user = userList.get(position);

        holder.mBinding.setUser(user);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }

    public void setUserList(List<UserModel> list) {
        if (list != null && list.size() > 0) {
            userList = list;
            notifyDataSetChanged();

        }
    }


    public class UserListViewHolder extends RecyclerView.ViewHolder {
        UserCardBinding mBinding;

        public UserListViewHolder(@NonNull UserCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;

            mBinding.btnSendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        UserModel user = userList.get(getAdapterPosition());
                        String email = user.getEmail();

                        Query query = firebaseDatabase.getReference("Users").orderByChild("email").equalTo(email);
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if (snapshot.exists()) {
                                    progressDialog.show();


                                    String userID = user.getId();
                                    FirebaseDatabase.getInstance().getReference("Users").child(userID).child("requestAccepted")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    try {
                                                        boolean isRequestAccepted = snapshot.getValue(Boolean.class);
                                                        if (!isRequestAccepted) {
                                                            sendNotification(user);

                                                        } else {
                                                            Toast.makeText(context, "Location Request Already send", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e) {
                                                        Toast.makeText(context, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                } else {
                                    Toast.makeText(context, "User Not add for tracking ", Toast.LENGTH_SHORT).show();
                                    progressDialog.show();

                                    String userId = user.getId();
                                    firebaseDatabase.getReference("Users").child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            progressDialog.dismiss();
                                            Toast.makeText(context, "User Added, now you can track user. ", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnCanceledListener(new OnCanceledListener() {
                                        @Override
                                        public void onCanceled() {
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Add User: ", " " + error.getMessage());

                            }
                        });

                    }

                }
            });


            mBinding.btnTrack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        progressDialog.show();

                        UserModel user = userList.get(getAdapterPosition());

                        FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("requestAccepted")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                        boolean isRequestAccepted = snapshot.getValue(Boolean.class);
                                        if (isRequestAccepted) {
                                            NavController navController = NavHostFragment.findNavController(fragment);
                                            UsersListFragmentDirections.ActionUsersListFragmentToTrackerFragment action = UsersListFragmentDirections.actionUsersListFragmentToTrackerFragment();
                                            action.setRecevierID(user.getId());
                                            progressDialog.dismiss();
                                            navController.navigate(action);

                                        } else {
                                            Toast.makeText(context, "Please Send Location Request First", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                }
            });


        }


        private void sendNotification(UserModel user) {
            FfcDatabase ffcDatabase = FfcDatabase.getInstance(context);
            GetUserInfoModel senderUser = ffcDatabase.dao().getLoginUser();
            String message = senderUser.getUserName() + " Want To Access Your Live Location.";
            sendNoticationClass.UpdateToken();

            String userID = user.getId();

            FirebaseDatabase.getInstance().getReference().child("Tokens").child(userID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String receiverUserToken = dataSnapshot.getValue(String.class);
                    progressDialog.dismiss();
                    sendNoticationClass.sendNotifications(receiverUserToken, senderUser, "Device Tracker", message, context);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
