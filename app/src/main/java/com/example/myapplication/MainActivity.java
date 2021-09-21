package com.example.myapplication;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.Login.GetUserInfoModel;
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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public final static int PERMISSION_REQUEST_CODE=22;
    private AppBarConfiguration mAppBarConfiguration;
    private FfcDatabase ffcDatabase;
    private NavController navController;
    private ActivityMainBinding mbinding;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mbinding= ActivityMainBinding.inflate(getLayoutInflater());
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
        NavigationUI.setupWithNavController(mbinding.navView,navController);
        NavigationUI.setupWithNavController(mbinding.customToolbar,navController, appBarConfiguration);
        NavigationUI.setupWithNavController(mbinding.bottomNavigation,navController);
        setDrawerHeader();


        if (SharedPreferenceHelper.getInstance(getApplication()).getGetDocListState())
        {
            int id = SharedPreferenceHelper.getInstance(getApplication()).getUserId();
            SyncDataToDB syncDataToDB = new SyncDataToDB(getApplication(),this);
            syncDataToDB.saveDoctorsList(id);
            syncDataToDB.SyncData(id);
            SharedPreferenceHelper.getInstance(getApplication()).setGetDocListState(false);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermission();
        }
        else
        {
            mbinding.bottomNavigation.setVisibility(View.VISIBLE);
            final Menu menu = mbinding.navView.getMenu();
            setMenus(menu);
        }


//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                NavigationUI.setupWithNavController(navigationView, navController);
//                navigationView.setNavigationItemSelectedListener(MainActivity.this);
//            }
//        },2000);
//
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_target_main)
//                .setDrawerLayout(drawer)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
////        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(bottomNavigationView,navController);
//        NavigationUI.setupWithNavController(navigationView, navController);
//
//
//        BadgeDrawable badgeDrawable = bottomNavigationView.getOrCreateBadge(R.id.nav_notifications);
//        badgeDrawable.setBackgroundColor(Color.parseColor("#0080FF" ));
//        badgeDrawable.setBadgeTextColor(Color.parseColor("#FFFFFF"));
//        badgeDrawable.setMaxCharacterCount(5);
//        badgeDrawable.setNumber(5);
//        badgeDrawable.setVisible(true);
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

    public void setDrawerHeader()
    {
        View headerView = mbinding.navView.getHeaderView(0);
        NavigationDrawerHeaderBinding headerBinding= NavigationDrawerHeaderBinding.bind(headerView);
       GetUserInfoModel loginUser= ffcDatabase.dao().getLoginUser();

       headerBinding.profileName.setText(loginUser.getUserName());
       headerBinding.profileEmail.setText(loginUser.getEmail());

           Glide.with(this)
                   .load(loginUser.getImage())
                   .centerCrop()
                   .placeholder(R.drawable.ic_profile)
                   .into(headerBinding.profileImage);


    }

    public void setMenus(Menu menu){

        ArrayList<String> menuIds = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menu_items_ids)));
        SimpleSQLiteQuery query= new SimpleSQLiteQuery("Select *from User_Menu Order By Menu_Order Asc");
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

                            switch (filterList.get(ii))
                            {
                                case "Ac_Home" :
                                    menu.add(1,R.id.nav_home,1,"Home").setIcon(getResources().getDrawable(R.drawable.ic_home));
                                    break;
                                case "Ac_Target":
                                    menu.add(1,R.id.nav_start_day,2,"Target").setIcon(getResources().getDrawable(R.drawable.ic_target));
                                    break;
                                case "Ac_Expense":
                                    menu.add(1,R.id.nav_expense_list,3,"Expense").setIcon(getResources().getDrawable(R.drawable.ic_expense));
                                    break;
                                case "Ac_SOrder":
                                    menu.add(1,R.id.doctorListFragment,4,"Doctor Profile").setIcon(getResources().getDrawable(R.drawable.ic_profile));
                                    break;
                                case "Ac_MSetting":
                                    menu.add(1,R.id.nav_home,5,"Master Setting").setIcon(getResources().getDrawable(R.drawable.ic_settings));
                                    break;
                                case "Ac_EndDay":
                                    menu.add(1,R.id.nav_home,6,"End Day").setIcon(getResources().getDrawable(R.drawable.ic_end_day));
                                    break;
                                case "Ac_MyProfile":
                                    menu.add(1,R.id.nav_home,7,"My Profile").setIcon(getResources().getDrawable(R.drawable.ic_account));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        String id = item.getTitle().toString();
//        List<String> config = ffcDatabase.dao().getOnConfigurations();
//        String activity = SharedPreferenceHelper.getInstance(getApplicationContext()).getActivity();
//
//        if(id.equals("Target")){
//            if(config.contains("Route_Activity")) {
//                if(activity.equals("Start Day")){
//                    navController.navigate(R.id.nav_start_day);
//                } else if (activity.equals("Local Travel") ){
//                    navController.navigate(R.id.nav_local_travel);
//                }else if (activity.equals("Private Travel") ){
//                    navController.navigate(R.id.nav_private_travel);
//                }else if (activity.equals("Office") ){
//                    navController.navigate(R.id.nav_office);
//                }else if (activity.equals("Attendance") ){
//                    navController.navigate(R.id.nav_attendance);
//                }else if(activity.equals("Target")){
//                    navController.navigate(R.id.nav_target_main);
//                }else{
//                    navController.navigate(R.id.nav_start_day);
//                }
//
//            }
//            else if(config.contains("Attendance_Activity")){
//                navController.navigate(R.id.nav_attendance);
//            }
//            else{
//                navController.navigate(R.id.nav_target_sub_menu);
//            }
//
//        }
//
//        drawer.closeDrawer(GravityCompat.START,false);
        return true;
    }

    public void requestPermission(){


        String[] permissionArray= new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS};
      SweetAlertDialog alertDialog=  new SweetAlertDialog(this,SweetAlertDialog.ERROR_TYPE);

               alertDialog .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ActivityCompat.requestPermissions(MainActivity.this,
                                permissionArray,
                                PERMISSION_REQUEST_CODE);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });



        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)&&
                ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CALL_PHONE)&&
                ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.SEND_SMS))
        {

            alertDialog.setTitle("Permission Needed");
            alertDialog.setContentText("These Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        }
        else
        {
            ActivityCompat.requestPermissions(this, permissionArray, PERMISSION_REQUEST_CODE);

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean check= false;
        if (requestCode==PERMISSION_REQUEST_CODE)
        {
            if (permissions.length>0 && grantResults.length==permissions.length)
            {
                for (int i=0;i<permissions.length;i++)
                {
                    if (grantResults[i]==PackageManager.PERMISSION_GRANTED)
                    {
                        check= true;
                    }
                    else
                    {
                        check=false;
                        return;
                    }
                }

                if (check)
                {
                    mbinding.bottomNavigation.setVisibility(View.VISIBLE);
                    final Menu menu = mbinding.navView.getMenu();
                    setMenus(menu);
                }

            }

        }

    }
}