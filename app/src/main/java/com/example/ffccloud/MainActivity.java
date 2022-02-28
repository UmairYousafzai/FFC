package com.example.ffccloud;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.ffccloud.Customer.utils.CustomerViewModel;
import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.PushNotification.SendNoticationClass;
import com.example.ffccloud.SplashScreen.SplashActivity;
import com.example.ffccloud.Target.utils.DoctorViewModel;
import com.example.ffccloud.Target.utils.TargetViewModel;
import com.example.ffccloud.databinding.ActivityMainBinding;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.NavigationDrawerHeaderBinding;
import com.example.ffccloud.expense.utils.ExpenseViewModel;
import com.example.ffccloud.model.Activity;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.notification.NotificationViewModel;
import com.example.ffccloud.utils.ActivityViewModel;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.Permission;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.SyncDataToDB;
import com.example.ffccloud.utils.UserViewModel;
import com.example.ffccloud.worker.utils.UploadDataRepository;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private FfcDatabase ffcDatabase;
    private NavController navController;
    private ActivityMainBinding mbinding;
    private Permission permission;
    private ActivityViewModel activityViewModel;
    private List<Activity> runningActivity;
    private UploadDataRepository uploadDataRepository;
    private NotificationViewModel notificationViewModel;
    private UserViewModel userViewModel;
    private boolean isEndDay = false;
    private CustomerViewModel customerViewModel;
    private ExpenseViewModel expenseViewModel;


    private boolean menuCheck = true;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mbinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mbinding.getRoot();
        setContentView(view);

        SendNoticationClass sendNoticationClass = new SendNoticationClass(this);
        sendNoticationClass.UpdateToken();


        ffcDatabase = FfcDatabase.getInstance(getApplicationContext());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        uploadDataRepository = new UploadDataRepository(this);
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        permission = new Permission(this, this);


        activityViewModel = new ViewModelProvider(this).get(ActivityViewModel.class);
        notificationViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);

        activityViewModel.getQueryActivity().observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {
                if (activities != null) {
                    runningActivity = activities;
                }

            }
        });

        activityViewModel.getWithoutTaskActivity().observe(this, new Observer<List<Activity>>() {
            @Override
            public void onChanged(List<Activity> activities) {

                isEndDay = activities != null && !activities.isEmpty();
            }
        });


        setupToolbar();
        syncData();
        setUpNavigation();
        setDrawerHeader();
        drawerItemSelectedListener();
        checkNotifications();

    }

    private void checkNotifications() {

        notificationViewModel.getNotificationCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer numberOfNotifications) {
                int menuID = mbinding.bottomNavigation.getMenu().getItem(4).getItemId();
                BadgeDrawable badgeDrawable = mbinding.bottomNavigation.getOrCreateBadge(menuID);
                if (numberOfNotifications != 0) {

                    badgeDrawable.setVisible(true);
                    badgeDrawable.setNumber(numberOfNotifications);
                } else {
                    badgeDrawable.setVisible(false);
                }

            }
        });


    }

    private void setUpNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
                .setOpenableLayout(mbinding.drawerLayout)
                .build();
        NavigationUI.setupWithNavController(mbinding.navView, navController);
        NavigationUI.setupWithNavController(mbinding.customToolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mbinding.bottomNavigation, navController);


        mbinding.bottomNavigation.getMenu().getItem(0).setVisible(false);
        mbinding.bottomNavigation.getMenu().getItem(1).setVisible(false);
        mbinding.bottomNavigation.getMenu().getItem(2).setVisible(false);
        mbinding.bottomNavigation.getMenu().getItem(3).setVisible(false);


    }

    private void syncData() {

        if (SharedPreferenceHelper.getInstance(getApplication()).getGetDocListState()) {
            int id = SharedPreferenceHelper.getInstance(getApplication()).getEmpID();
            new SendNoticationClass(this).UpdateToken();

            SyncDataToDB.getInstance().SyncData(id, this, this);
            SyncDataToDB.getInstance().saveDoctorsList(id, this, this);
            SharedPreferenceHelper.getInstance(getApplication()).setGetDocListState(false);
        }

    }

    private void setupToolbar() {
        setSupportActionBar(mbinding.customToolbar);

    }

    private void drawerItemSelectedListener() {

        mbinding.navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.end_day) {
                    if (SharedPreferenceHelper.getInstance(getBaseContext()).getStart()) {


                        if (isNetworkConnected()) {
                            if (!uploadDataRepository.isWorkPlanExists() && !uploadDataRepository.isWorkPlanStatusExists()) {
                                showDialog();
                            } else {
                                Toast.makeText(MainActivity.this, "Please wait for uploading pending data \n Please try again later", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(MainActivity.this, "Please connect to internet", Toast.LENGTH_SHORT).show();
                        }


                        mbinding.drawerLayout.closeDrawer(GravityCompat.START);


                    }
                } else {
                    NavigationUI.onNavDestinationSelected(item, navController);
                    mbinding.drawerLayout.closeDrawer(GravityCompat.START);

                }


                return false;
            }
        });
    }

//    private void generateWorkRequest() {
//
//        Data data = new Data.Builder()
//                .putString(CONSTANTS.WORK_REQUEST_END_DAY, "Work in done")
//                .build();
//
//        Constraints constraints = new Constraints.Builder().setRequiresBatteryNotLow(true).setRequiredNetworkType(NetworkType.CONNECTED).build();
//        OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
//                .setInputData(data)
//                .setConstraints(constraints)
//                .build();
//        WorkManager.getInstance().enqueue(oneTimeWorkRequest);
//
//        WorkManager.getInstance().getWorkInfoByIdLiveData(oneTimeWorkRequest.getId())
//                .observe(this, new Observer<WorkInfo>() {
//                    @Override
//                    public void onChanged(WorkInfo workInfo) {
//                        Toast.makeText(MainActivity.this, "" + workInfo.getState().name(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume:", "onResume of main activity");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {


                                    mbinding.bottomNavigation.setVisibility(View.VISIBLE);

                                    if (menuCheck) {
                                        final Menu menu = mbinding.navView.getMenu();
                                        setMenus(menu);
                                        menuCheck = false;
                                    }
                                } else {
                                    permission.getWriteStoragePermission();
                                }


                            } else {
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
//        invalidateOptionsMenu();
//
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.search).setVisible(false);
        menu.findItem(R.id.upload).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {






        if (item.getItemId() == R.id.signOut) {
            signOut();

        }
        else if (item.getItemId()== R.id.action_download_master_data)
        {
            downloadMaterData();
        }


        return super.onOptionsItemSelected(item);
    }

    public void downloadMaterData()
    {
        int id = SharedPreferenceHelper.getInstance(getApplication()).getEmpID();
        userViewModel.deleteAllGrades();
        userViewModel.deleteAllQualification();
        userViewModel.deleteAllClassification();
        userViewModel.deleteAllDeliveryModes();
        userViewModel.deleteAllUsers();
        userViewModel.deleteAllExpenseType();

        SyncDataToDB.getInstance().SyncData(id, this, this);

    }

    public void signOut()
    {
        DoctorViewModel doctorViewModel;
        TargetViewModel targetViewModel;
        if (!isEndDay) {
            targetViewModel = new ViewModelProvider(this).get(TargetViewModel.class);
            doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);

            targetViewModel.DeleteAllDoctor();

            customerViewModel.deleteAllCustomers();
            activityViewModel.deleteAllActivity();
            doctorViewModel.deleteAllSchedule();
            doctorViewModel.deleteAllFilterDoctor();

            notificationViewModel.deleteAllNotifications();
            userViewModel.deleteAllMenus();
            userViewModel.deleteAllGrades();
            userViewModel.deleteAllQualification();
            userViewModel.deleteAllClassification();
            userViewModel.deleteAllDeliveryModes();
            userViewModel.deleteAllUsers();
            userViewModel.deleteAllExpenseType();
            userViewModel.deleteAllMenus();

            String password = SharedPreferenceHelper.getInstance(this).getUserPassword();
            String url = SharedPreferenceHelper.getInstance(this).getBaseUrl();
            SharedPreferenceHelper.getInstance(this).deleteSharedPreference();
            SharedPreferenceHelper.getInstance(this).setUserPassword(password);
            SharedPreferenceHelper.getInstance(this).setBaseUrl(url);

            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Please end day before Sign Out", Toast.LENGTH_SHORT).show();
        }


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

        if (loginUser != null) {
            headerBinding.profileName.setText(loginUser.getUserName());
            headerBinding.profileEmail.setText(loginUser.getEmail());
            headerBinding.profileDesignation.setVisibility(View.GONE);

            if (loginUser.getImage()!=null&& ! loginUser.getImage().isEmpty())
            {
                byte[] data = Base64.decode(loginUser.getImage(), Base64.DEFAULT);
                Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                headerBinding.profileImage.setImageBitmap(bmp);
            }

//            Glide.with(this)
//                    .load(loginUser.getImage())
//                    .centerCrop()
//                    .placeholder(R.drawable.ic_profile)
//                    .into(headerBinding.profileImage);
//
        }


    }

    public void setMenus(Menu menu) {
//        menu.add(1, R.id.nav_start_day, 2, "Target").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_target, null));
//        menu.add(1, R.id.end_day, 6, "End Day").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_end_day, null));
//        menu.add(1, R.id.expenseListFragment, 3, "Expense").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expense, null));
//
//        menu.add(1, R.id.showRouteFragment, 8, "Routes").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_route_svgrepo_com, null));
//        menu.add(1, R.id.meetingFragment, 9, "Meetings").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_meeting, null));
//        menu.add(1, R.id.mapsFragment, 10, "Tracker").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_location, null));
//        menu.add(1, R.id.tableLayout, 11, "Doctor Reports").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_target, null));
//        menu.add(1, R.id.usersListFragment, 12, "Tracking").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_gps_fixed_24, null));
//        menu.add(1, R.id.customerListFragment, 13, "Customer").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_client_profile_svgrepo_com, null));
//        menu.add(1, R.id.salesOrderListFragment, 14, "Sales Order").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_orders, null));
//        menu.add(1, R.id.farmListFragment, 15, "Farm").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_farm_svgrepo_com, null));
//        menu.add(1, R.id.medicalStoreListFragment, 16, "Medical Store").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_medical_pharmacy_store, null));
//        menu.add(1, R.id.hospitalListFragment, 17, "Hospital").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hospital, null));
//        menu.add(1, R.id.SupplierDoctorFragment, 18, "Doctor").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_doctor, null));
        ArrayList<String> menuIds = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_items_ids)));
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
                                mbinding.bottomNavigation.getMenu().getItem(0).setVisible(true);

                                menu.add(1, R.id.nav_home, 1, "Home").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home, null));
                                break;
                                case "Ac_Dashboard":

                                menu.add(1, R.id.DashBoardFragment, 1, "DashBoard").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dashboard, null));
                                break;
                            case "Ac_Target":
                                mbinding.bottomNavigation.getMenu().getItem(1).setVisible(true);

                                menu.add(1, R.id.nav_start_day, 2, "Target").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_target, null));
                                break;
                            case "Ac_Expense":
                                menu.add(1, R.id.expenseListFragment, 3, "Expense").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_expense, null));
                                break;

                            case "Ac_EndDay":
                                menu.add(1, R.id.end_day, 4, "End Day").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_end_day, null));
                                break;
                            case "Ac_MyProfile":
                                menu.add(1, R.id.nav_home, 5, "My Profile").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
                                break;
                            case "Ac_SaleOrder":
                                menu.add(1, R.id.salesOrderListFragment, 6, "Sale Order").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_orders, null));
                                break;

                            case "Ac_Doctor":
                                menu.add(1, R.id.SupplierDoctorFragment, 7, "Doctor").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_doctor, null));
                                break;
                            case "Ac_Farm":
                                menu.add(1, R.id.farmListFragment, 8, "Farm").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_farm_svgrepo_com, null));
                                break;

                            case "Ac_Customer":
                                menu.add(1, R.id.customerListFragment, 9, "Customer").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_client_profile_svgrepo_com, null));
                                break;
                            case "Ac_Hospital":
                                menu.add(1, R.id.hospitalListFragment, 10, "Hospital").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_hospital, null));
                                break;

                            case "Ac_MedicalStore":
                                menu.add(1, R.id.medicalStoreListFragment, 11, "Medical Store").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_medical_pharmacy_store, null));
                                break;


                            case "Ac_Meeting":
                                menu.add(1, R.id.meetingFragment, 12, "Meeting").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_meeting, null));
                                break;
                            case "Ac_Tracking":
                                mbinding.bottomNavigation.getMenu().getItem(3).setVisible(true);
                                menu.add(1, R.id.usersListFragment, 13, "Tracking").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_gps_fixed_24, null));
                                break;
                            case "Ac_Chat":
                                mbinding.bottomNavigation.getMenu().getItem(2).setVisible(true);

                                menu.add(1, R.id.chatUserListFragment, 14, "Chats").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_chat_24, null));
                                break;

                            case "Ac_MSetting":
                                menu.add(1, R.id.nav_home, 15, "Master Setting").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_settings, null));
                                break;

                            case "Ac_Reset_Password":
                                menu.add(1, R.id.nav_home, 16, "Reset Password").setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_account, null));
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


    public void closeActivity() {

        CustomLocation customLocation = new CustomLocation(this);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a", Locale.getDefault());
        String formattedDate = df.format(c);
        Permission permission = new Permission(this, this);

        if (runningActivity != null && !runningActivity.isEmpty()) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (permission.isLocationEnabled()) {
                    if (isNetworkConnected()) {
                        CustomLocation.CustomLocationResults locationResults = new CustomLocation.CustomLocationResults() {
                            @Override
                            public void gotLocation(Location location) {
                                String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

                                for (Activity activity : runningActivity) {
                                    String[] locationStringArray = activity.getStartCoordinates().split(",");
                                    Location startLocation = new Location("");

                                    startLocation.setLatitude(Double.parseDouble(locationStringArray[0]));
                                    startLocation.setLongitude(Double.parseDouble(locationStringArray[1]));

                                    String distance = String.valueOf(location.distanceTo(startLocation));


                                    String endAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                    activity.setEndAddress(endAddress);
                                    activity.setDistance(distance);
                                    activity.setEndCoordinates(locationString);
                                    activity.setEndDateTime(formattedDate);
                                    String totalTime = calculateTotalTime(formattedDate, activity.getStartDateTime());
                                    activity.setTotalTime(totalTime);
                                    activityViewModel.updateActivity(activity);
                                }

                                mbinding.bottomNavigation.getMenu().findItem(R.id.nav_start_day).setEnabled(true);
                                mbinding.navView.getMenu().findItem(R.id.nav_start_day).setEnabled(true);

                                SharedPreferenceHelper.getInstance(getParent()).setStart(false);

                                navController.navigate(R.id.nav_home);


                            }
                        };

                        customLocation.getLastLocation(locationResults);
                    } else {
                        Toast.makeText(this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();

                    }

                } else {
                    CustomsDialog.getInstance().showOpenLocationSettingDialog(this, this);

                }

            } else {
                permission.getLocationPermission();
            }

        }


    }

    private String calculateTotalTime(String formattedDate, String startDateTime) {

        int endHours = 0, endMinutes = 0, endSeconds = 0, startHours = 0, startMinutes = 0, startSeconds = 0;
        if (!startDateTime.isEmpty() && !startDateTime.isEmpty()) {
            endHours = Integer.parseInt(formattedDate.substring(11, 13));
            endMinutes = Integer.parseInt(formattedDate.substring(14, 16));
            endSeconds = Integer.parseInt(formattedDate.substring(17, 19));
            startHours = Integer.parseInt(startDateTime.substring(11, 13));
            startMinutes = Integer.parseInt(startDateTime.substring(14, 16));
            startSeconds = Integer.parseInt(startDateTime.substring(17, 19));
        }


        return (Math.abs(endHours - startHours)) + ":" + (Math.abs(endMinutes - startMinutes)) + ":" + (Math.abs(endSeconds - startSeconds));

    }


    public boolean isNetworkConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return connected = true;
        } else {
            return connected = false;
        }

    }

    public void showDialog() {

        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(this).setView(dialogBinding.getRoot()).setCancelable(true).create();
        dialogBinding.title.setText(R.string.end_day);
        dialogBinding.body.setText("Are you sure, you want to end day");

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_warning_24, null);
        dialogBinding.icon.setImageDrawable(drawable);

        alertDialog.show();

        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                if (expenseViewModel.isExpenseExists()) {
                    expenseViewModel.getAllExpense().observe(MainActivity.this, new Observer<List<ExpenseModelClass>>() {
                        @Override
                        public void onChanged(List<ExpenseModelClass> expenseModelClasses) {
                            if (expenseModelClasses!=null)
                            {
                                uploadExpenses(expenseModelClasses);
                            }
                        }
                    });

                } else {

                    closeActivity();
                    CustomsDialog.getInstance().showDialog("Day Ended Successfully", "End Day", MainActivity.this, MainActivity.this, 1);

                }
            }
        });
        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void uploadExpenses(List<ExpenseModelClass> expenseModelClassList) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Expenses");
        progressDialog.setMessage("Uploading....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String token = SharedPreferenceHelper.getInstance(this).getToken();
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertExpenses(token, expenseModelClassList);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        UpdateStatus updateStatus = response.body();
                        Toast.makeText(MainActivity.this, "Expense:" + updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                        if (updateStatus.getStatus() == 1) {
                            expenseViewModel.deleteAllExpense();
                            closeActivity();

                            CustomsDialog.getInstance().showDialog("Day Ended Successfully", "End Day", MainActivity.this, MainActivity.this, 1);
                        }

                    }
                } else {
                    Toast.makeText(MainActivity.this, "Expense:" + response.message(), Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {

                Toast.makeText(MainActivity.this, "Expense:" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}