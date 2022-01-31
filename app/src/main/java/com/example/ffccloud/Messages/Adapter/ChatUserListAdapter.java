package com.example.ffccloud.Messages.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.example.ffccloud.Messages.ChatUserListFragmentDirections;
import com.example.ffccloud.R;
import com.example.ffccloud.UserModel;
import com.example.ffccloud.databinding.ChatUserCardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ChatUserListViewHolder> implements Filterable {

    private LayoutInflater layoutInflater;
    private List<UserModel> userList;
    private List<UserModel> userListFull;
    private Fragment fragment;
    private FirebaseDatabase firebaseDatabase;
    private Context context;
    private ProgressDialog progressDialog;

    public ChatUserListAdapter(Fragment fragment,Context context) {

        userList = new ArrayList<>();
        userListFull = new ArrayList<>();

        this.fragment = fragment;
        firebaseDatabase= FirebaseDatabase.getInstance();
        this.context = context;

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Checking...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(context.getResources(), R.color.white, null)));
    }

    @NonNull
    @Override
    public ChatUserListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ChatUserCardBinding binding = ChatUserCardBinding.inflate(layoutInflater, parent, false);

        return new ChatUserListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserListViewHolder holder, int position) {
        UserModel user;
        user = userList.get(position);

        holder.mBinding.setUser(user);
        holder.mBinding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return userList == null ? 0 : userList.size();
    }




    public void setUserList(List<UserModel> list) {

        if (list!=null && list.size()>0) {

            userList.addAll(list);
            userListFull.addAll(list);
        } else {
            userList.clear();
        }
        notifyDataSetChanged();
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


    public class ChatUserListViewHolder extends RecyclerView.ViewHolder {
        ChatUserCardBinding mBinding;

        public ChatUserListViewHolder(@NonNull ChatUserCardBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.chatUserCard.setOnClickListener(v -> {


                if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                    progressDialog.show();
                    UserModel user = userList.get(getAdapterPosition());
                    String email = user.getEmail();

                    Query query = firebaseDatabase.getReference("Users").orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                progressDialog.dismiss();
                                NavController navController = NavHostFragment.findNavController(fragment);

                                ChatUserListFragmentDirections.ActionChatUserListFragmentToChatFragment action = ChatUserListFragmentDirections.actionChatUserListFragmentToChatFragment();
                                action.setRecevierID(user.getId());
                                action.setRecevierUserName(user.getUserName());
                                navController.navigate(action);


                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(context, "User Not add for Messages ", Toast.LENGTH_SHORT).show();
                                progressDialog.setMessage("Adding...");
                                progressDialog.show();

                                String userId = user.getId();
                                firebaseDatabase.getReference("Users").child(userId).setValue(user).addOnSuccessListener(aVoid -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "User Added, now you can message user. ", Toast.LENGTH_SHORT).show();
                                }).addOnCanceledListener(() -> {
                                    progressDialog.dismiss();
                                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Add User: ", " " + error.getMessage());
                            progressDialog.dismiss();
                        }
                    });

                }


            });
        }
    }
}
