package com.example.myapplication.Target;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.DoctorModel;
import com.example.myapplication.MainActivity;
import com.example.myapplication.ModelClasses.UpdateStatus;
import com.example.myapplication.ModelClasses.UpdateWorkPlanStatus;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.R;
import com.example.myapplication.Target.utils.TargetViewModel;
import com.example.myapplication.databinding.CustomCompeleteDialogBinding;
import com.example.myapplication.databinding.FragmentTargetPostMenuBinding;
import com.example.myapplication.utils.CustomLocation;
import com.example.myapplication.utils.SharedPreferenceHelper;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TargetPostMenuFragment extends Fragment {

    private View view;
    private FragmentTargetPostMenuBinding mBinding;
    private DoctorModel doctorModel;
    private  AlertDialog alertDialog;
    private Location location;
    private TargetViewModel targetViewModel;
    private boolean check= false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding= FragmentTargetPostMenuBinding.inflate(inflater,container,false);
        view = mBinding.getRoot();

        return view;


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        targetViewModel = new ViewModelProvider(requireActivity()).get(TargetViewModel.class);

        setInfoWorkPlan();
        btnListener();
    }

    public void setInfoWorkPlan()
    {
        Bundle bundle= getArguments();
        doctorModel = TargetPostMenuFragmentArgs.fromBundle(bundle).getDoctorModel();
        mBinding.workName.setText(doctorModel.getName());
        mBinding.workLocation.setText(doctorModel.getAddress());
        mBinding.workDistanceFromCurrentLoc.setText(doctorModel.getDistance());
    }

    public void btnListener()
    {
        mBinding.btnCompelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAndDeleteWorkPlan(3);
            }
        });
        mBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completeAndDeleteWorkPlan(4);
            }
        });

        mBinding.callCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorModel.getPhone()!=null&&!doctorModel.getPhone().isEmpty())
                {
                    String PhoneString= "tel:"+doctorModel.getPhone();

                    Uri callUri= Uri.parse(PhoneString);
                    Intent callIntent= new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(callUri);
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermission();
                    }
                    else
                    {
                        startActivity(callIntent);

                    }
                }
                else
                {
                    Toast.makeText(requireContext(),"Phone Number Is Empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        mBinding.messageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doctorModel.getPhone()!=null&&!doctorModel.getPhone().isEmpty())
                {
                    String smsPhoneString= "smsto:"+doctorModel.getPhone();
                    Uri smsUri= Uri.parse(smsPhoneString);
                    Intent smsIntent= new Intent(Intent.ACTION_SENDTO);
                    smsIntent.setData(smsUri);
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermission();
                    }
                    else
                    {
                        startActivity(smsIntent);

                    }
                }
                else
                {
                    Toast.makeText(requireContext(),"Phone Number Is Empty",Toast.LENGTH_LONG).show();
                }
            }
        });

        mBinding.findCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (doctorModel.getCoordinates() != null &&! doctorModel.getCoordinates().isEmpty())
                {
                    String locationString= "geo:"+doctorModel.getCoordinates();
                    Uri mapIntentUri= Uri.parse(locationString);
                    Intent mapIntent= new Intent(Intent.ACTION_VIEW,mapIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");

                    if (mapIntent.resolveActivity(requireActivity().getPackageManager())!=null)
                    {
                        startActivity(mapIntent);
                    }
                }
                else
                {
                    Toast.makeText(requireContext(),"Location Coordinates Are Empty",Toast.LENGTH_LONG).show();

                }

            }
        });
    }

    private void completeAndDeleteWorkPlan(int key) {

        ProgressDialog progressDialog= new ProgressDialog(getContext());
        progressDialog.setTitle("Loading");
        CustomCompeleteDialogBinding dialogBinding= CustomCompeleteDialogBinding.inflate(getLayoutInflater());
        alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();
        alertDialog.show();

        CustomLocation.CustomLocationResults locationResults= new CustomLocation.CustomLocationResults() {
            @Override
            public void gotLocation(Location location1) {
                location=location1;
            }
        };

        CustomLocation customLocation= new CustomLocation();
        customLocation.getLastLocation(getContext(),getActivity(),locationResults);
        dialogBinding.saveRemarksBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String check=dialogBinding.remarksEdittext.getText().toString();


                if (!check.equals("") ) {
                    if (location != null) {
                        List<UpdateWorkPlanStatus> list = new ArrayList<>();
                        String remarks = dialogBinding.remarksEdittext.getText().toString();
                        int userId = SharedPreferenceHelper.getInstance(getActivity()).getUserId();
                        String token = SharedPreferenceHelper.getInstance(getActivity()).getToken();
                        String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        Date date =  Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat=new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.getDefault());
        String dateString = dateFormat.format(date);
                        UpdateWorkPlanStatus updateWorkPlanStatus = new UpdateWorkPlanStatus();

                        updateWorkPlanStatus.setDoctor_Id(doctorModel.getDoctorId());
                        updateWorkPlanStatus.setEmp_Id(userId);
                        updateWorkPlanStatus.setRemarks(remarks);
                        updateWorkPlanStatus.setStatus(String.valueOf(key));
                        updateWorkPlanStatus.setVisit_On(String.valueOf(dateString));
                        updateWorkPlanStatus.setVisit_Cord(locationString);

                        if (doctorModel.getWorkId() > 0) {
                            updateWorkPlanStatus.setWork_Id(String.valueOf(doctorModel.getWorkId()));
                        }

                        list.add(updateWorkPlanStatus);
                        Call<UpdateStatus> call = ApiClient.getInstance().getApi().UpdateWorkPlanStatus(token, "application/json", list);
                        call.enqueue(new Callback<UpdateStatus>() {
                            @Override
                            public void onResponse(Call<UpdateStatus> call, Response<UpdateStatus> response) {

                                if (response.body() != null) {
                                    UpdateStatus updateStatus = response.body();
                                    Toast.makeText(getContext(), updateStatus.getStrMessage(), Toast.LENGTH_LONG).show();
                                    targetViewModel.DeleteDoctor(doctorModel);
                                    alertDialog.dismiss();
                                    progressDialog.dismiss();
                                    Navigation.findNavController(view).navigate(R.id.action_targetPostMenuFragment_to_target_fragment);



                                }


                            }

                            @Override
                            public void onFailure(Call<UpdateStatus> call, Throwable t) {

                                t.getMessage();
                                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                    else {
                        Toast.makeText(getActivity(),"Please turn on location",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    dialogBinding.remarksEdittext.setError("Please Enter The Remarks");
                }
            }
        });

        dialogBinding.btnCloseCompeleteDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public void requestPermission(){


        String[] permissionArray= new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS};
        SweetAlertDialog alertDialog=  new SweetAlertDialog(requireContext(),SweetAlertDialog.ERROR_TYPE);

        alertDialog .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ActivityCompat.requestPermissions(requireActivity(),
                                permissionArray,
                                MainActivity.PERMISSION_REQUEST_CODE);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });



        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.ACCESS_FINE_LOCATION)&&
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.CALL_PHONE)&&
                ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),Manifest.permission.SEND_SMS))
        {

            alertDialog.setTitle("Permission Needed");
            alertDialog.setContentText("These Permission Needed For The Better Experience Of The App. ");
            alertDialog.show();


        }
        else
        {
            ActivityCompat.requestPermissions(requireActivity(), permissionArray, MainActivity.PERMISSION_REQUEST_CODE);

        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==MainActivity.PERMISSION_REQUEST_CODE)
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



            }

        }

    }
}