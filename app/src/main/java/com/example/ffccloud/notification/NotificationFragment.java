package com.example.ffccloud.notification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.model.Notification;
import com.example.ffccloud.databinding.FragmentNotificationBinding;
import com.example.ffccloud.notification.adapter.NotificationListAdapter;

import java.util.List;


public class NotificationFragment extends Fragment {

    private FragmentNotificationBinding mBinding;
    private NotificationViewModel notificationViewModel;
    private NavController navController;
    private NotificationListAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding= FragmentNotificationBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController= NavHostFragment.findNavController(this);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        setupRecyclerView();
        getNotifications();
        btnListener();
    }

    private void btnListener() {

        adapter.setClickListener(new NotificationListAdapter.SetOnClickListener() {
            @Override
            public void onCloseBtnClick(Notification notification) {

                notificationViewModel.deleteNotification(notification);
            }
        });

        mBinding.tvRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notificationViewModel.deleteAllNotifications();
            }
        });
    }

    private void setupRecyclerView() {

        mBinding.notificationsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter= new NotificationListAdapter();

        mBinding.notificationsRecyclerView.setAdapter(adapter);
    }

    private void getNotifications() {

        notificationViewModel.getAllNotifications().observe(getViewLifecycleOwner(), new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {

                adapter.setNotificationList(notifications);

            }
        });
    }
}