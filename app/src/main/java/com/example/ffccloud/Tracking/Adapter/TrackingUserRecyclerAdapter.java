package com.example.ffccloud.Tracking.Adapter;

import static com.example.ffccloud.utils.CONSTANTS.LOCATION_NOTIFICATION_TITLE;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.ffccloud.utils.CONSTANTS;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class TrackingUserRecyclerAdapter extends RecyclerView.Adapter<TrackingUserRecyclerAdapter.UserListViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    public List<UserModel> userListFull;
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
        userList= new ArrayList<>();
        userListFull= new ArrayList<>();
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
            userList.clear();
            userList.addAll(list);
            userListFull.addAll(list);
            notifyDataSetChanged();

        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<UserModel> filterList = new ArrayList<>();

            if (constraint!=null&&constraint.length()>0)
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                if (userListFull!=null&& userListFull.size()>0)
                {
                    for (UserModel model :userListFull)
                    {
                        if (model.getUserName().toLowerCase().trim().contains(filterPattern))
                        {
                            filterList.add(model);
                        }
                    }
                }

            }
            else
            {
                if (userListFull != null) {
                    filterList.addAll(userListFull);
                }
            }

            FilterResults filterResults= new FilterResults();
            filterResults.values= filterList;

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            userList.clear();
            if (results.values!=null)
            {
                userList.addAll((List) results.values);
            }
            notifyDataSetChanged();

        }
    };


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
                                                            Toast.makeText(context, "Location Request Sent", Toast.LENGTH_SHORT).show();


                                                        } else {
                                                            Toast.makeText(context, "Location Request Already send", Toast.LENGTH_SHORT).show();
                                                        }
                                                    } catch (Exception e) {
                                                        Toast.makeText(context, " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                    progressDialog.dismiss();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                            progressDialog.dismiss();

                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Log.e("Add User: ", " " + error.getMessage());
                                Toast.makeText(context, "Something went wrong"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

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
                            String email = user.getEmail();

                        Query query = firebaseDatabase.getReference("Users").orderByChild("email").equalTo(email);

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists())
                                {

                                    FirebaseDatabase.getInstance().getReference("Users").child(user.getId()).child("requestAccepted")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                    if (snapshot.getValue()!=null)
                                                    {
                                                        boolean isRequestAccepted ;
                                                        try
                                                        {
                                                            isRequestAccepted = snapshot.getValue(Boolean.class);
                                                        }
                                                        catch (Exception e)
                                                        {
                                                            isRequestAccepted=false;
                                                        }

                                                        if (isRequestAccepted) {
                                                            NavController navController = NavHostFragment.findNavController(fragment);
                                                            UsersListFragmentDirections.ActionUsersListFragmentToTrackerFragment action = UsersListFragmentDirections.actionUsersListFragmentToTrackerFragment();
                                                            action.setRecevierID(user.getId());
                                                            progressDialog.dismiss();
                                                            navController.navigate(action);

                                                        } else {
                                                            Toast.makeText(context, "Please wait Location Request First", Toast.LENGTH_SHORT).show();
                                                            progressDialog.dismiss();
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(context, "Add user first", Toast.LENGTH_SHORT).show();
                                }

                                progressDialog.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    sendNoticationClass.sendNotifications(receiverUserToken, senderUser,LOCATION_NOTIFICATION_TITLE, message, context);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
}
