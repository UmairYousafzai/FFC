package com.example.ffccloud.salesOrder;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.SaleOrderModel;
import com.example.ffccloud.databinding.FragmentSalesOrderListBinding;
import com.example.ffccloud.databinding.SaleOrderSearchDialogBinding;
import com.example.ffccloud.salesOrder.adapter.SaleOrderAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesOrderListFragment extends Fragment {
    private FragmentSalesOrderListBinding mBinding;
    private SaleOrderAdapter adapter;
    private ProgressDialog progressDialog;
    private NavController navController;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSalesOrderListBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        navController = NavHostFragment.findNavController(this);
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        setUpRecyclerView();
        getSalesOrder();
        btnListener();
    }

    private void btnListener() {
        mBinding.fromDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear,mDay,mMonth;
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mDate;
                        mDate=month+"/"+dayOfMonth+"/"+year;
                        mBinding.tvFromDate.setText(mDate);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
        mBinding.toDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear,mDay,mMonth;
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mDate;
                        mDate=month+"/"+dayOfMonth+"/"+year;
                        mBinding.tvToDate.setText(mDate);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        mBinding.tvSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesOrderListFragmentDirections.ActionSalesOrderListFragmentToCustomerListFragment action = SalesOrderListFragmentDirections.actionSalesOrderListFragmentToCustomerListFragment();
                action.setCallingFragmentKey(1);
                navController.navigate(action);
            }
        });


        mBinding.searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setUpRecyclerView() {

        mBinding.salesOrderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SaleOrderAdapter();
        mBinding.salesOrderRecyclerView.setAdapter(adapter);


    }

    private void getSalesOrder() {

        Call<List<SaleOrderModel>> call = ApiClient.getInstance().getApi().getSalesOrder(1,1,1,1,
                "20211111"
        ,"20211111",0,0,5,0);

        call.enqueue(new Callback<List<SaleOrderModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<SaleOrderModel>> call, @NotNull Response<List<SaleOrderModel>> response) {
                if (response.body()!=null)
                {

                    adapter.setSaleOrderModelList(response.body());
                    progressDialog.dismiss();
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), "web response"+ response.errorBody(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<SaleOrderModel>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), "web response"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.filter).setVisible(true);
        menu.findItem(R.id.search1).setVisible(true);
        menu.findItem(R.id.search).setVisible(false);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.filter)
        {
            if (mBinding.salesOrderFilterLayout.getVisibility()==View.GONE)
            {
                Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);
                mBinding.salesOrderFilterLayout.setAnimation(animation);
                mBinding.salesOrderFilterLayout.setVisibility(View.VISIBLE);

            }
            else
            {
                Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                mBinding.salesOrderFilterLayout.setAnimation(animation);
                mBinding.salesOrderFilterLayout.setVisibility(View.GONE);
            }
        }

        return true;
    }


}