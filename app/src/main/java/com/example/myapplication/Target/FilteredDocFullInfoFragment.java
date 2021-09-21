package com.example.myapplication.Target;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.FilteredDoctorInfomationModel;
import com.example.myapplication.MainActivity;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.Target.Adapters.ScheduleAdapter;
import com.example.myapplication.databinding.FragmentFilteredDocFullInfoBinding;
import com.example.myapplication.utils.SharedPreferenceHelper;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilteredDocFullInfoFragment extends Fragment {

    private FragmentFilteredDocFullInfoBinding mBinding;
    private int doc_id;
    private FilteredDoctorInfomationModel doctoredFullInfoModel;
    private ScheduleAdapter adapter;
    private SweetAlertDialog progressDialog;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("oncreateView"," full info key on create view me ha");
        ((MainActivity) requireActivity()).getSupportActionBar().hide();
        mBinding = FragmentFilteredDocFullInfoBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("onViewCreated","full info key on view created me ha");

        progressDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#286A9C"));
        progressDialog.setTitleText("Loading");
        //pDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        navController= NavHostFragment.findNavController(this);

        doc_id = FilteredDocFullInfoFragmentArgs.fromBundle(getArguments()).getDoctorId();

        setRecyclerView();
        getFilteredDoc();
        btnListener();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (((AppCompatActivity)getActivity()).getSupportActionBar().isShowing()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }
        Log.e("onDestroy","full info key on destroy me ha");

    }
    private void btnListener() {

        mBinding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (doctoredFullInfoModel!=null)
                {

                    new SweetAlertDialog(requireContext(),SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Do you want to edit this profile ?")
                            .setConfirmText("Yes")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    FilteredDocFullInfoFragmentDirections.ActionFilteredDocFullInfoFragmentToDoctorFormFragment action = FilteredDocFullInfoFragmentDirections.actionFilteredDocFullInfoFragmentToDoctorFormFragment();
                                    action.setFilterDoc(doctoredFullInfoModel);
                                    navController.navigate(action);
                                }
                            })
                            .setCancelText("No")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                }
                            }).show();


                }
                else
                {
                    Toast.makeText(requireContext(),"Doctor Is Empty",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }






    public void getFilteredDoc()
    {
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        Call<FilteredDoctorInfomationModel> call = ApiClient.getInstance().getApi().GetFullFilteredDoctor(token,doc_id);
        call.enqueue(new Callback<FilteredDoctorInfomationModel>() {
            @Override
            public void onResponse(Call<FilteredDoctorInfomationModel> call, Response<FilteredDoctorInfomationModel> response) {
                if(response.body()!=null)
                {
                    doctoredFullInfoModel = response.body();
                    mBinding.setDoctor(doctoredFullInfoModel);
                    mBinding.executePendingBindings();
                    if (doctoredFullInfoModel.getGender()==1)
                    {
                        mBinding.gender.setText("Male");
                    }
                    else if (doctoredFullInfoModel.getGender()==2)
                    {
                        mBinding.gender.setText("Female");

                    }
                    else if (doctoredFullInfoModel.getGender()==3)
                    {
                        mBinding.gender.setText("Other");

                    }
                    adapter.setScheduleModelList(doctoredFullInfoModel.getScheduleModels());
                    adapter.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<FilteredDoctorInfomationModel> call, Throwable t) {
                Toast.makeText(requireContext(),""+t.getMessage(),Toast.LENGTH_LONG).show();


            }
        });
    }

    public  void setRecyclerView()
    {
        mBinding.docInfoSchedulesRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ScheduleAdapter();
        mBinding.docInfoSchedulesRecyclerview.setAdapter(adapter);

    }
}