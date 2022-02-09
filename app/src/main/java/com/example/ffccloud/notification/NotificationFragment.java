package com.example.ffccloud.notification;

import static com.example.ffccloud.utils.CONSTANTS.LOCATION_NOTIFICATION_TITLE;
import static com.example.ffccloud.utils.CONSTANTS.MESSAGE_NOTIFICATION_TITLE;

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

import com.example.ffccloud.R;
import com.example.ffccloud.model.Notification;
import com.example.ffccloud.databinding.FragmentNotificationBinding;
import com.example.ffccloud.notification.adapter.NotificationListAdapter;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        clearNotifications();
    }


    private void clearNotifications() {

        BottomNavigationView bottomNavigation = requireActivity().findViewById(R.id.bottom_navigation);
        int menuID = bottomNavigation.getMenu().getItem(4).getItemId();
        BadgeDrawable badgeDrawable = bottomNavigation.getOrCreateBadge(menuID);
        badgeDrawable.setNumber(0);
        badgeDrawable.setVisible(false);
        SharedPreferenceHelper.getInstance(requireContext()).setNUMBER_OF_NOTIFICATION(0);



    }

    private void btnListener() {

        adapter.setClickListener(new NotificationListAdapter.SetOnClickListener() {
            @Override
            public void onCloseBtnClick(Notification notification) {
                if (notification!=null)
                {
                    notificationViewModel.deleteNotification(notification);

                }

            }

            @Override
            public void onClick(Notification notification) {

                if (notification!=null)
                {
                    if (notification.getNotificationTitle()!=null &&notification.getNotificationTitle().equals(LOCATION_NOTIFICATION_TITLE))
                    {
                        navController.navigate(R.id.action_notification_to_LocationRequestFragment);
                    }
                    else if (notification.getNotificationTitle()!=null &&notification.getNotificationTitle().equals(MESSAGE_NOTIFICATION_TITLE))
                    {
                        String[] name= notification.getNotificationMessage().split(" ");
                        NotificationFragmentDirections.ActionNotificationToChatFragment action = NotificationFragmentDirections.actionNotificationToChatFragment();
                        action.setRecevierID(notification.getSenderId());
                        action.setRecevierUserName(name[0]);
                        navController.navigate(action);

                    }
                    notificationViewModel.deleteNotification(notification);

                }
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