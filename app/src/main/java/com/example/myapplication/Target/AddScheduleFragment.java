package com.example.myapplication.Target;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.myapplication.Login.LoginActivity;
import com.example.myapplication.ModelClasses.AreaModel;
import com.example.myapplication.ModelClasses.DoctorScheduleModel;
import com.example.myapplication.ModelClasses.RegionModel;
import com.example.myapplication.ModelClasses.SaveDoctorModel;
import com.example.myapplication.ModelClasses.UpdateStatus;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.R;
import com.example.myapplication.ScheduleModel;
import com.example.myapplication.Target.Adapters.ScheduleAdapter;
import com.example.myapplication.Target.utils.DoctorViewModel;
import com.example.myapplication.databinding.FragmentAddScheduleBinding;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddScheduleFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private SaveDoctorModel saveDoctorModel;
    private FragmentAddScheduleBinding mBinding;
    private List<ScheduleModel> scheduleModelList= new ArrayList<>();
    private ArrayList<String> regionList = new ArrayList<>(), areaList = new ArrayList<>();
    private HashMap<String, Integer> regionHashmap = new HashMap<>(), areaHashmap = new HashMap<>(), dayhashMap = new HashMap<>();
    private int mHour, mMinute;
    private String openingTime="", closingTime="";
    private boolean primaryLocation=false,primaryLocationLock=false, checkPrimaryLoation=false;;
    private String[] days;
    private ScheduleAdapter adapter;
    private String locationString= "";
    private DoctorViewModel doctorViewModel;
    private SweetAlertDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (!getArguments().isEmpty()) {
            saveDoctorModel = AddScheduleFragmentArgs.fromBundle(getArguments()).getDoctorToSave();
        }


        mBinding = FragmentAddScheduleBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (saveDoctorModel.getDoctor_Id()==0)
        {

        }

        //checking user primary location is selected already or not

        getRegion();
        btn_listener();
        setDaySpinner();
        setRecyclerView();
        doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
        doctorViewModel.getAllschedule().observe(getViewLifecycleOwner(), new Observer<List<ScheduleModel>>() {
            @Override
            public void onChanged(List<ScheduleModel> list) {
                if (list.size()>0&&list!=null)
                {
                    scheduleModelList=list;
                    adapter.setScheduleModelList(scheduleModelList);


                    if (scheduleModelList!=null)
                    {
                        for (ScheduleModel model:scheduleModelList)
                        {
                            if (model.getPrimaryLoc()) {
                                checkPrimaryLoation=model.getPrimaryLoc();
                                //this can prevent user from selecting different primary location
                                primaryLocationLock= true;
                                mBinding.primaryLocationBtn.setChecked(false);
                                mBinding.primaryLocationBtn.setEnabled(false);
                                return;

                            }
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        doctorViewModel.deleteAllSchedule();
        doctorViewModel.insertSchedule(scheduleModelList);
    }

    public void setRecyclerView() {

        mBinding.scheduleRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ScheduleAdapter();
        mBinding.scheduleRecyclerview.setAdapter(adapter);
        if (scheduleModelList.size()>0&&scheduleModelList!=null)
        {
        }
    }


    public void btn_listener() {

        mBinding.regionSpinner.setOnItemSelectedListener(this);
        mBinding.areaSpinner.setOnItemSelectedListener(this);

        mBinding.openingTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        openingTime = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                        mBinding.openingTimeText.setText(openingTime);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        mBinding.closingTimeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        closingTime = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                        mBinding.closingTimeText.setText(closingTime);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        mBinding.locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationEnabled())
                {
                    CustomLocation customLocation= new CustomLocation();
                    CustomLocation.CustomLocationResults results= new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {


                            if (mBinding.locationBtn.isChecked()) {
                                String address = getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                mBinding.textLocation.setText(address);
                                locationString = String.valueOf(location.getLongitude())+","+String.valueOf(location.getLatitude());
                            }
                            else
                            {
                                locationString="";
                                mBinding.textLocation.setText(locationString);
                            }



                        }
                    };
                    customLocation.getLastLocation(requireContext(),requireActivity(),results);
                }
                else
                {
                    new SweetAlertDialog(requireContext(),SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Please turn on  location for this action.")
                            .setContentText("Do you want to open location setting.")
                            .setConfirmText("Yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                    requireContext().startActivity(intent);
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelText("Cancel")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    mBinding.locationBtn.setChecked(false);
                                }
                            }).show();
                }

            }
        });

        mBinding.btnAddSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Adding Schedule.")
                        .setContentText("Do you want to Save schedule?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                if (mBinding.primaryLocationBtn.isChecked()||checkPrimaryLoation)
                                {
                                    //this can prevent user from selecting different primary location
                                    primaryLocationLock= true;
                                    mBinding.primaryLocationBtn.setChecked(false);
                                    mBinding.primaryLocationBtn.setEnabled(false);
                                }

                                ScheduleModel model= new ScheduleModel();
                                int regionId,areaId,dayId;

                                if (mBinding.regionSpinner.getSelectedItem().toString().isEmpty())
                                {
                                    mBinding.regionSpinner.requestFocus();
                                    Toast.makeText(requireContext(),"Please Select region for adding schedule",Toast.LENGTH_SHORT).show();
                                }
                                else if (mBinding.areaSpinner.getSelectedItem().toString().isEmpty())
                                {
                                    mBinding.areaSpinner.requestFocus();
                                    Toast.makeText(requireContext(),"Please Select area for adding schedule",Toast.LENGTH_SHORT).show();
                                }
                                else if (mBinding.daySpinner.getSelectedItem().toString().isEmpty())
                                {
                                    mBinding.daySpinner.requestFocus();
                                    Toast.makeText(requireContext(),"Please Select day for adding schedule",Toast.LENGTH_SHORT).show();
                                }
                                else if (openingTime.isEmpty())
                                {
                                    mBinding.openingTimeText.requestFocus();
                                    mBinding.openingTimeText.setError("Please Select open timing for adding schedule");
                                    Toast.makeText(requireContext(),"Please Select open timing for adding schedule",Toast.LENGTH_SHORT).show();
                                }
                                else if (closingTime.isEmpty())
                                {
                                    mBinding.closingTimeText.requestFocus();
                                    mBinding.closingTimeText.setError("Please Select close timing for adding schedule");
                                    Toast.makeText(requireContext(),"Please Select close timing for adding schedule",Toast.LENGTH_SHORT).show();
                                }
                                else if(locationString.isEmpty())
                                {
                                    mBinding.locationBtn.requestFocus();
                                    mBinding.textLocation.setError("Please Select turn on location for adding schedule");
                                    Toast.makeText(requireContext(),"Please turn on location for adding schedule",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {

                                    regionId= regionHashmap.get(mBinding.regionSpinner.getSelectedItem().toString());
                                    areaId = areaHashmap.get(mBinding.areaSpinner.getSelectedItem().toString());
                                    dayId = dayhashMap.get(mBinding.daySpinner.getSelectedItem().toString());

                                    model.setAreaTittle(mBinding.areaSpinner.getSelectedItem().toString());
                                    model.setSubHeadCode(areaId);
                                    model.setRegionTittle(mBinding.regionSpinner.getSelectedItem().toString());
                                    model.setAcNo(regionId);
                                    model.setOpeningTime(openingTime);
                                    model.setClosingTime(closingTime);
                                    model.setCoordinates(locationString);
                                    model.setDayId(dayId);
                                    model.setPrimaryLoc(primaryLocation);

                                    if (!scheduleModelList.contains(model))
                                    {
                                        scheduleModelList.add(model);
                                        getRegion();
                                        mBinding.textLocation.setText("");
                                        mBinding.closingTimeText.setText("");
                                        mBinding.openingTimeText.setText("");
                                        mBinding.locationBtn.setChecked(false);
                                        openingTime="";
                                        closingTime="";
                                        Toast.makeText(requireContext(),"Schedule Added",Toast.LENGTH_SHORT).show();
                                        adapter.setScheduleModelList(scheduleModelList);
                                    }
                                    else
                                    {
                                        Toast.makeText(requireContext(),"Schedule Already Exist",Toast.LENGTH_SHORT).show();
                                    }

                                }
                                sweetAlertDialog.dismiss();
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
        });

        mBinding.primaryLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!primaryLocationLock)
                {
                    if (mBinding.primaryLocationBtn.isChecked())
                    {

                        new SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Primary Location Alert")
                                .setContentText("Do you want set Primary Location for this schedule?")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        primaryLocation= true;
                                        sweetAlertDialog.dismiss();
                                    }
                                })
                                .setCancelText("Cancel")
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        primaryLocation= false;
                                        mBinding.primaryLocationBtn.setChecked(false);
                                        sweetAlertDialog.dismiss();
                                    }
                                }).show();

                    }
                }

            }
        });

        mBinding.btnSaveDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBinding.btnSaveDoctor.setEnabled(false);
                new SweetAlertDialog(requireContext(),SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Adding Doctor.")
                        .setContentText("Do you want to Save Doctor?")
                        .setConfirmText("Yes")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                int id = SharedPreferenceHelper.getInstance(requireContext()).getUserId();
                                String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
                                saveDoctorModel.setSchedules(getSchedule());
                                saveDoctorModel.setSuggested_UserId(id);
                                progressDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
                                progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                progressDialog.setTitleText("Loading");
                                //pDialog.setCancelable(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                                Call<UpdateStatus> call = ApiClient.getInstance().getApi().SendSaveDoctor(token,"application/json",saveDoctorModel);
                                call.enqueue(new Callback<UpdateStatus>() {
                                    @Override
                                    public void onResponse(Call<UpdateStatus> call, Response<UpdateStatus> response) {

                                        UpdateStatus updateStatus = response.body();
                                        mBinding.primaryLocationBtn.setEnabled(false);
                                        progressDialog.dismiss();
                                        new SweetAlertDialog(requireContext())
                                                .setTitleText(updateStatus.getStrMessage())
                                                .show();

                                    }

                                    @Override
                                    public void onFailure(Call<UpdateStatus> call, Throwable t) {

                                        Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_LONG).show();

                                    }
                                });

                                mBinding.btnSaveDoctor.setEnabled(true);
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelText("Cancel")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                mBinding.btnSaveDoctor.setEnabled(true);

                            }
                        }).show();




            }
        });
    }

    public void setDaySpinner() {
        days = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (int i = 0; i < days.length; i++) {
            dayhashMap.put(days[i], i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, days);
        mBinding.daySpinner.setAdapter(adapter);

    }

    public void getRegion() {
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int id = SharedPreferenceHelper.getInstance(requireContext()).getUserId();
        Call<List<RegionModel>> call = ApiClient.getInstance().getApi().getRegion(token, 1, 1, 1, id);

        call.enqueue(new Callback<List<RegionModel>>() {
            @Override
            public void onResponse(Call<List<RegionModel>> call, Response<List<RegionModel>> response) {
                if (response.isSuccessful()) {
                    regionHashmap.clear();
                    regionList.clear();
                    List<RegionModel> regionModelList = new ArrayList<>();
                    regionModelList = response.body();
                    for (RegionModel model : regionModelList) {
                        regionList.add(model.getName());
                        regionHashmap.put(model.getName(), model.getRegionId());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, regionList);
                        mBinding.regionSpinner.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RegionModel>> call, Throwable t) {

            }
        });
    }

    public void getArea(String key) {
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int id = SharedPreferenceHelper.getInstance(requireContext()).getUserId();
        if (!key.isEmpty()) {
            int regionId = regionHashmap.get(mBinding.regionSpinner.getSelectedItem().toString());
            Call<List<AreaModel>> call = ApiClient.getInstance().getApi().getArea(token, 1, 1, 1, regionId, id);

            call.enqueue(new Callback<List<AreaModel>>() {
                @Override
                public void onResponse(Call<List<AreaModel>> call, Response<List<AreaModel>> response) {
                    if (response.isSuccessful()) {
                        areaHashmap.clear();
                        areaList.clear();
                        List<AreaModel> areaModelList = new ArrayList<>();
                        areaModelList = response.body();
                        for (AreaModel model : areaModelList) {
                            areaList.add(model.getNewAcTitle());
                            areaHashmap.put(model.getNewAcTitle(), Integer.valueOf(model.getNodeid()));

                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, areaList);
                        mBinding.areaSpinner.setAdapter(adapter);
                    }
                }

                @Override
                public void onFailure(Call<List<AreaModel>> call, Throwable t) {

                }
            });
        } else {
            mBinding.areaSpinner.requestFocus();
            Toast.makeText(requireContext(), "Please Select region first.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.region_spinner) {
            String key = mBinding.regionSpinner.getSelectedItem().toString();

            getArea(key);

        } else if (parent.getId() == R.id.area_spinner) {

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public List<DoctorScheduleModel> getSchedule()
    {
        List<DoctorScheduleModel> doctorScheduleModelList = new ArrayList<>();
        DoctorScheduleModel doctorScheduleModel= new DoctorScheduleModel();

        for (ScheduleModel model:scheduleModelList)
        {
            doctorScheduleModel.setAc_No(model.getAcNo());
            doctorScheduleModel.setSub_Head_Code(model.getSubHeadCode());
            doctorScheduleModel.setClosing_Time(model.getClosingTime());
            doctorScheduleModel.setCoordinates(model.getCoordinates());
            doctorScheduleModel.setDay_Id(model.getDayId());
            doctorScheduleModel.setOpening_Time(model.getOpeningTime());
            doctorScheduleModel.setPrimary_Loc(primaryLocation);
            doctorScheduleModel.setScheduleId(0);
            doctorScheduleModelList.add(doctorScheduleModel);
        }

        return doctorScheduleModelList;
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("MyCurrentloctionaddress", strReturnedAddress.toString());
            } else {
                Log.w("MyCurrentloctionaddress", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("MyCurrentloctionaddress", "Canont get Address!");
        }
        return strAdd;
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
}