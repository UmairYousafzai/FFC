package com.example.myapplication.Target;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.myapplication.ModelClasses.AreasByEmpIdModel;
import com.example.myapplication.ModelClasses.DoctorsByAreaIdsModel;
import com.example.myapplication.NetworkCalls.ApiClient;
import com.example.myapplication.SplashScreen.SplashActivity;
import com.example.myapplication.Target.Adapters.AddWorkPlanDialogAdapter;
import com.example.myapplication.databinding.CustomAddWorkplanDialogBinding;
import com.example.myapplication.utils.SharedPreferenceHelper;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddWorkPlanDialogFragment extends DialogFragment   {

    private CustomAddWorkplanDialogBinding mBinding;
    private AddWorkPlanDialogAdapter adapter;
    private List<AreasByEmpIdModel> areaList= new ArrayList<>();
    private final List<AreasByEmpIdModel> areaListForOtherFragment=new ArrayList<>();
    private List<DoctorsByAreaIdsModel> doctorList =  new ArrayList<>();
    private final List<DoctorsByAreaIdsModel> docListForOtherFragment= new ArrayList<>();
    private int key;
    private ProgressDialog progressDialog;
    private NavController navController;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mBinding = CustomAddWorkplanDialogBinding.inflate(inflater,container,false);
        View view = mBinding.getRoot();
        setCancelable(false);

        key= AddWorkPlanDialogFragmentArgs.fromBundle(getArguments()).getKey();
        if (key==2)
        {
            mBinding.titleDialog.setText("Select Area");
        }
        else if (key==3)
        {
            mBinding.titleDialog.setText("Select Target");
        }



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);

        getdata();
        setupRecyclerView();
        btnListener();
        searchFilter();
        mBinding.docListDialogSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void searchFilter() {

    }

    public void setupRecyclerView()
    {
        adapter= new AddWorkPlanDialogAdapter(key, new AddWorkPlanDialogAdapter.DialogRecyclerItemListener() {
            @Override
            public void onItemListenerArea(AreasByEmpIdModel model,boolean add) {
                if (key==2&& add) {
                    areaListForOtherFragment.add(model);
                }
                else
                {
                    areaListForOtherFragment.remove(model);
                }

            }

            @Override
            public void onItemListenerTarget(DoctorsByAreaIdsModel model, boolean add) {
                if (key==3&&add) {
                    docListForOtherFragment.add(model);
                }
                else
                {
                    docListForOtherFragment.remove(model);
                }
            }
        });

        mBinding.docListDialogRecycler.setAdapter(adapter);

    }

    public void getdata()
    {
        progressDialog= new ProgressDialog(requireContext());
        progressDialog.setTitle("Loading Areas");
        progressDialog.show();
        if (key==2) {
            String token= SharedPreferenceHelper.getInstance(requireContext()).getToken();
            int id = SharedPreferenceHelper.getInstance(requireContext()).getUserId();
            Call<List<AreasByEmpIdModel>> call= ApiClient.getInstance().getApi().GetAreaByEmpId(token,id);

            call.enqueue(new Callback<List<AreasByEmpIdModel>>() {
                @Override
                public void onResponse(Call<List<AreasByEmpIdModel>> call, Response<List<AreasByEmpIdModel>> response) {
                    if (response.body()!=null) {
                        areaList = response.body();
                        adapter.setAreaModelList(areaList);

                    }
                    else
                    {
                        new SweetAlertDialog( requireContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText(response.message()+"\nSession Expire Please Login Again")
                                .setConfirmText("Cancel")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        SharedPreferenceHelper.getInstance(requireContext()).setLogin_State(false);
                                        Intent intent = new Intent(requireContext(), SplashActivity.class);
                                        requireActivity().startActivity(intent);

                                    }
                                })
                                .show();
                    }

                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<List<AreasByEmpIdModel>> call, Throwable t) {
                    new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Add Work Plan")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        }
        else if (key==3)
        {
            String token= SharedPreferenceHelper.getInstance(requireContext()).getToken();
            String dataIds= AddWorkPlanDialogFragmentArgs.fromBundle(getArguments()).getDataIds();

            Call<List<DoctorsByAreaIdsModel>> call= ApiClient.getInstance().getApi().GetDoctorsByAreaIds(token,dataIds);
            call.enqueue(new Callback<List<DoctorsByAreaIdsModel>>() {
                @Override
                public void onResponse(Call<List<DoctorsByAreaIdsModel>> call, Response<List<DoctorsByAreaIdsModel>> response) {
                    if (response.body()!=null)
                    {
                        doctorList= response.body();
                        adapter.setDoctorModelList(doctorList);
                    }
                    else
                    {
                        new SweetAlertDialog( requireContext(), SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                            .setContentText(response.message()+"\nSession Expire Please Login Again")
                            .setConfirmText("Cancel")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    SharedPreferenceHelper.getInstance(requireContext()).setLogin_State(false);
                                    Intent intent = new Intent(requireContext(), SplashActivity.class);
                                    requireActivity().startActivity(intent);

                                }
                            })
                            .show();
                    }
                    progressDialog.dismiss();

                }

                @Override
                public void onFailure(Call<List<DoctorsByAreaIdsModel>> call, Throwable t) {
                    new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Add Work Plan")
                            .setContentText(t.getMessage())
                            .show();
                }
            });
        }
    }



    public void btnListener()
    {
        mBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mBinding.btnSetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (docListForOtherFragment!=null||areaListForOtherFragment!=null)
                {
//                    NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
//                    assert navHostFragment != null;
//                     navController = navHostFragment.getNavController();
//                    AddWorkPlanDialogFragmentDirections.ActionAddWorkPlanDialogFragmentToAddworkPlanFragment action = AddWorkPlanDialogFragmentDirections.actionAddWorkPlanDialogFragmentToAddworkPlanFragment();
//
//                    action.setKey(key);
//                    action.setAreaIds(dataIDs);
//                    action.setAreaTittles(dataTittl);
//
//                      navController.navigate(action);
                    if (key==2)
                    {
                        navController.getPreviousBackStackEntry().getSavedStateHandle().set("Object",areaListForOtherFragment);
                        navController.popBackStack();
                    }
                    else
                    {
                        navController.getPreviousBackStackEntry().getSavedStateHandle().set("Object2",docListForOtherFragment);
                        navController.popBackStack();
                    }


                }
                else
                {
                    Toast.makeText(requireContext(),"Please Select At least One Location",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
