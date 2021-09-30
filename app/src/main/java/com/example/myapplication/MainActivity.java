package com.example.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.Login.GetUserInfoModel;
import com.example.myapplication.ModelClasses.Activity;
import com.example.myapplication.TargetMenu.TargetMainFragmentDirections;
import com.example.myapplication.ui.home.HomeFragmentDirections;
import com.example.myapplication.utils.ActivityViewModel;
import com.example.myapplication.utils.CONSTANTS;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.Permission;
import com.example.myapplication.utils.SyncDataToDB;
import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.NavigationDrawerHeaderBinding;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FfcDatabase ffcDatabase;
    private NavController navController;
    private ActivityMainBinding mbinding;
    private Permission permission;
    private ActivityViewModel activityViewModel;
    private List<Activity> runningActivity;
    private boolean menuCheck = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mbinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mbinding.getRoot();
        setContentView(view);


        setSupportActionBar(mbinding.customToolbar);
        ffcDatabase = FfcDatabase.getInstance(getApplicationContext());
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(mbinding.drawerLayout)
                .build();
        NavigationUI.setupWithNavController(mbinding.navView, navController);
        NavigationUI.setupWithNavController(mbinding.customToolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mbinding.bottomNavigation, navController);

        setDrawerHeader();

        permission = new Permission(this, this);

        if (SharedPreferenceHelper.getInstance(getApplication()).getGetDocListState()) {
            int id = SharedPreferenceHelper.getInstance(getApplication()).getUserId();
            SyncDataToDB syncDataToDB = new SyncDataToDB(getApplication(), this);
            syncDataToDB.saveDoctorsList(id);
            syncDataToDB.SyncData(id);
            SharedPreferenceHelper.getInstance(getApplication()).setGetDocListState(false);
        }

        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);

        activityViewModel.getQueryActivity().observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {
                if ( activities.size()>0)
                {
                    runningActivity= activities;
                }

            }
        });
        drawerItemSelectedListener();

    }

    private void drawerItemSelectedListener() {

        mbinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.end_day) {
                    if (SharedPreferenceHelper.getInstance(getBaseContext()).getStart()) {

                        closeActivity();
                        mbinding.drawerLayout.closeDrawer(GravityCompat.START);

                    }
                } else {
                    NavigationUI.onNavDestinationSelected(item,navController);
                    mbinding.drawerLayout.closeDrawer(GravityCompat.START);

                }


                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                            {
                                mbinding.bottomNavigation.setVisibility(View.VISIBLE);

                                if (menuCheck) {
                                    final Menu menu = mbinding.navView.getMenu();
                                    setMenus(menu);
                                    menuCheck= false;
                                }

                            }
                            else
                            {
                                permission.getCOARSELocationPermission();
                            }


                        } else {
                            permission.getStoragePermission();

                        }
                    } else {
                        permission.getCameraPermission();
                    }

                } else {
                    permission.getSMSPermission();
                }
            } else {
                permission.getCallPermission();
            }

        } else {
            permission.getLocationPermission();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    public void setDrawerHeader() {
        View headerView = mbinding.navView.getHeaderView(0);
        NavigationDrawerHeaderBinding headerBinding = NavigationDrawerHeaderBinding.bind(headerView);
        GetUserInfoModel loginUser = ffcDatabase.dao().getLoginUser();

        headerBinding.profileName.setText(loginUser.getUserName());
        headerBinding.profileEmail.setText(loginUser.getEmail());

        Glide.with(this)
                .load(loginUser.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_profile)
                .into(headerBinding.profileImage);


    }

    public void setMenus(Menu menu) {

        ArrayList<String> menuIds = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_items_ids)));
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("Select *from User_Menu Order By Menu_Order Asc");
        ffcDatabase.dao().sortMenus();
        List<String> menuStateList = ffcDatabase.dao().get_menu_State();

        if (menuStateList != null && !menuStateList.isEmpty()) {
            List<String> filterList = new ArrayList<>();

            for (String m : menuStateList) {
                if (!filterList.contains(m)) {
                    filterList.add(m);
                }
            }


            if (!filterList.isEmpty()) {
                for (int ii = 0; ii < filterList.size(); ii++) {
                    if (menuIds.contains(filterList.get(ii))) {

                        switch (filterList.get(ii)) {
                            case "Ac_Home":
                                menu.add(1, R.id.nav_home, 1, "Home").setIcon(getResources().getDrawable(R.drawable.ic_home));
                                break;
                            case "Ac_Target":
                                menu.add(1, R.id.nav_start_day, 2, "Target").setIcon(getResources().getDrawable(R.drawable.ic_target));
                                break;
                            case "Ac_Expense":
                                menu.add(1, R.id.nav_expense_list, 3, "Expense").setIcon(getResources().getDrawable(R.drawable.ic_expense));
                                break;
                            case "Ac_SOrder":
                                menu.add(1, R.id.doctorListFragment, 4, "Doctor Profile").setIcon(getResources().getDrawable(R.drawable.ic_profile));
                                break;
                            case "Ac_MSetting":
                                menu.add(1, R.id.nav_home, 5, "Master Setting").setIcon(getResources().getDrawable(R.drawable.ic_settings));
                                break;
                            case "Ac_EndDay":
                                menu.add(1, R.id.end_day, 6, "End Day").setIcon(getResources().getDrawable(R.drawable.ic_end_day));
                                break;
                            case "Ac_MyProfile":
                                menu.add(1, R.id.nav_home, 7, "My Profile").setIcon(getResources().getDrawable(R.drawable.ic_account));
                                break;
                        }

                    }
                }
            }
        } else {
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_expense_list)
                    .setOpenableLayout(mbinding.drawerLayout)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        }


    }


    public void closeActivity()
    {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("Select *From activity Where Main_Activity = 'Start Day' ");


        CustomLocation customLocation= new CustomLocation();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.getDefault());
        String formattedDate = df.format(c);
        Permission permission= new Permission(this,this);

        if (runningActivity!=null)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                if (permission.isLocationEnabled())
                {
                    CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {
                            String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

                            for (Activity activity:runningActivity)
                            {
                                activity.setEndCoordinates(locationString);
                                activity.setEndDateTime(formattedDate);
                                String totalTime=calculateTotalTime(formattedDate,activity.getStartDateTime());
                                activity.setTotalTime(totalTime);
                                activityViewModel.updateActivity(activity);
                            }

                            mbinding.bottomNavigation.getMenu().findItem(R.id.nav_start_day).setEnabled(true);
                            mbinding.navView.getMenu().findItem(R.id.nav_start_day).setEnabled(true);

                            SharedPreferenceHelper.getInstance(getParent()).setStart(false);

                            navController.navigate(R.id.nav_home);


                        }
                    };

                    customLocation.getLastLocation(this,this,locationResults);
                }
                else
                {
                    showDialog();
                }

            }
            else {
                permission.getLocationPermission();
            }

        }


    }
    private String calculateTotalTime(String formattedDate, String startDateTime) {

        String endTime= formattedDate.substring(10,17),startTime=startDateTime.substring(10,17);
        int endHours= Integer.parseInt(formattedDate.substring(10,11)), endMinutes= Integer.parseInt(formattedDate.substring(13,14))
                ,endSeconds=Integer.parseInt(formattedDate.substring(16,17));
        int startHours= Integer.parseInt(startDateTime.substring(10,11)), startMinutes= Integer.parseInt(startDateTime.substring(13,14))
                ,startSeconds=Integer.parseInt(startDateTime.substring(16,17));

        return (Math.abs(endHours-startHours))+":"+(Math.abs(endMinutes-startMinutes))+":"+(Math.abs(endSeconds-startSeconds));

    }

    public void showDialog()
    {
        new SweetAlertDialog(this)
                .setTitleText("Please turn on  location for this action.")
                .setContentText("Do you want to open location setting.")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        sweetAlertDialog.dismiss();
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                      startActivity(intent);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                }).show();
    }
}