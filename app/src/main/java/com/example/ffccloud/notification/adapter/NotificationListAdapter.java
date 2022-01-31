package com.example.ffccloud.notification.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ffccloud.model.Notification;
import com.example.ffccloud.databinding.NotificationCardBinding;

import java.util.ArrayList;
import java.util.List;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationViewHolder>
{
    private List<Notification>  notificationList;
    private LayoutInflater layoutInflater;
    private SetOnClickListener listener;

    public NotificationListAdapter() {

        notificationList= new ArrayList<>();
    }

    public void setClickListener(SetOnClickListener listener)
    {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater==null)
        {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        NotificationCardBinding binding = NotificationCardBinding.inflate(layoutInflater,parent,false);
        return new NotificationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        Notification notification = new Notification();
        notification= notificationList.get(position);

        holder.mBinding.setNotification(notification);
        holder.mBinding.executePendingBindings();
        if (notification.getNotificationTitle()!=null && notification.getNotificationTitle().length()>0)
        {
            String text = notification.getNotificationMessage().substring(0,1);
            holder.mBinding.tvDrawable.setText(text);
        }


    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public void setNotificationList(List<Notification> list)
    {
        if (list!=null && list.size()>0)
        {
            notificationList.clear();
            notificationList.addAll(list);
        }
        else
        {
            notificationList.clear();
        }

        notifyDataSetChanged();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        NotificationCardBinding mBinding;

        public NotificationViewHolder(@NonNull NotificationCardBinding binding) {
            super(binding.getRoot());

            mBinding= binding;

            mBinding.btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener!=null && getAdapterPosition() != RecyclerView.NO_POSITION)
                    {
                        listener.onCloseBtnClick(notificationList.get(getAdapterPosition()));
                    }

                }
            });
        }
    }

    public interface SetOnClickListener
    {
        void onCloseBtnClick(Notification notification);
    }
}
