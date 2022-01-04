package com.example.ffccloud.salesOrder;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.GetSaleOrderModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentSalesOrderListBinding;
import com.example.ffccloud.salesOrder.adapter.SaleOrderAdapter;
import com.example.ffccloud.utils.CONSTANTS;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesOrderListFragment extends Fragment {
    private FragmentSalesOrderListBinding mBinding;
    private SaleOrderAdapter adapter;
    private ProgressDialog progressDialog;
    private NavController navController;
    private CustomerModel customerModel;
    private ArrayList<String> byDateList = new ArrayList<>(), byStatusList = new ArrayList<>(), byPriorityList = new ArrayList<>();
    private HashMap<String, Integer>  byDateHashmap = new HashMap<>(), byStatusHashmap = new HashMap<>(), byPriorityHashMap = new HashMap<>();
    private int byDateKey = 0 , byStatusKey = 0 , byPriorityKey = 0;
    private String fromDate, toDate;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter2;
    private  boolean isSpinnerSet = false;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);

        MutableLiveData<CustomerModel> liveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.CUSTOMER_KEY);
        liveData.observe(getViewLifecycleOwner(), new Observer<CustomerModel>() {
            @Override
            public void onChanged(CustomerModel model) {
                if (model!=null)
                {
                    customerModel = model;
                    mBinding.tvSelectCustomer.setText(customerModel.getPartyName());
                }


            }
        });
        if (!isSpinnerSet)
        {
            setUpFilterSpinners();
            isSpinnerSet=true;
        }
        else
        {
            setSpinnerAdapter();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        Calendar calendar = Calendar.getInstance();

        int year =calendar.get(Calendar.YEAR);
        int month =calendar.get(Calendar.MONTH)+1;
        int day =calendar.get(Calendar.DAY_OF_MONTH);


        String date =String.valueOf(calendar.get(Calendar.YEAR))+getDate(month,day);
        setUpRecyclerView();
        getSalesOrder(0,date,date,0,0,0);
        btnListener();
        setPullToFresh();
    }

    public String getDate(int month,int day)
    {
        int checkmonth = (month % 10);
        int checkday = (day % 10);
        String mMonth;
        if (checkmonth > 0 && month < 9) {
            mMonth = "0" + (month);

        } else {
            mMonth = String.valueOf(month + 1);

        }

        String mDay;
        if (checkday > 0 && day < 10) {
            mDay = "0" + (day);

        } else {
            mDay = String.valueOf(day);

        }
        return mMonth+mDay;
    }

    private void setUpFilterSpinners() {
        //setting for by date spinner
        byDateList.add("Show All");
        byDateHashmap.put("Show All",0);
        byDateList.add("Today");
        byDateHashmap.put("Today",1);
        byDateList.add("Yesterday");
        byDateHashmap.put("Yesterday",2);
        byDateList.add("This Week");
        byDateHashmap.put("This Week",3);
        byDateList.add("This Month");
        byDateHashmap.put("This Month",4);
        byDateList.add("This Year");
        byDateHashmap.put("This Year",4);


        //setting for by status spinner
        byStatusList.add("Show All");
        byStatusHashmap.put("Show All",0);
        byStatusList.add("UnPosted ");
        byStatusHashmap.put("UnPosted ",1);
        byStatusList.add("Posted");
        byStatusHashmap.put("Posted",2);
        byStatusList.add("Closed");
        byStatusHashmap.put("Closed",3);
        byStatusList.add("Canceled");
        byStatusHashmap.put("Canceled",4);


        //setting for by priority spinner
        byPriorityList.add("Show All");
        byPriorityHashMap.put("Show All",0);
        byPriorityList.add("High");
        byPriorityHashMap.put("High",1);
        byPriorityList.add("Normal");
        byPriorityHashMap.put("Normal",2);
        byPriorityList.add("Low");
        byPriorityHashMap.put("Low",3);

        setSpinnerAdapter();

    }

    private void setSpinnerAdapter() {
        arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, byDateList);
        mBinding.dateSpinner.setAdapter(arrayAdapter);

        adapter1 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, byStatusList);
        mBinding.statusSpinner.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, byPriorityList);
        mBinding.prioritySpinner.setAdapter(adapter2);
    }

    private void btnListener() {
        mBinding.fromDateLayout.setOnClickListener(v -> {
            int mYear,mDay,mMonth;
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mMonth = calendar.get(Calendar.MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                String mDate;
                mDate=month+1+"/"+dayOfMonth+"/"+year;
                fromDate = year +getDate(month+1,dayOfMonth);
                mBinding.tvFromDate.setText(mDate);
            },mYear,mMonth,mDay);
            datePickerDialog.show();
        });
        mBinding.toDateLayout.setOnClickListener(v -> {
            int mYear,mDay,mMonth;
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mMonth = calendar.get(Calendar.MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
                String mDate;
                mDate=month+1+"/"+dayOfMonth+"/"+year;
                toDate =year +getDate(month+1,dayOfMonth);

                mBinding.tvToDate.setText(mDate);
            },mYear,mMonth,mDay);
            datePickerDialog.show();
        });

        mBinding.tvSelectCustomer.setOnClickListener(v -> {
            SalesOrderListFragmentDirections.ActionSalesOrderListFragmentToCustomerListFragment action = SalesOrderListFragmentDirections.actionSalesOrderListFragmentToCustomerListFragment();
            action.setCallingFragmentKey(1);
            navController.navigate(action);
        });


        mBinding.searchBtn.setOnClickListener(v -> {
            if (customerModel!=null)
            {
                if (customerModel.getSupplier_Id()!=0)
                {
                    if (fromDate!=null)
                    {
                        if (toDate!=null)
                        {
                            getSalesOrder(customerModel.getSupplier_Id(),fromDate,toDate,byStatusKey,byDateKey,byPriorityKey);
                        }
                        else {
                            mBinding.tvToDate.setError("Please select date");
                        }
                    }
                    else
                    {
                        mBinding.tvFromDate.setError("Please select date");
                    }
                }
                else {
                    Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                mBinding.tvSelectCustomer.setError("Select customer please");
            }


        });
        mBinding.setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String date =String.valueOf(calendar.get(Calendar.YEAR))+String.valueOf(calendar.get(Calendar.MONTH))+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
                byDateKey = byDateHashmap.get(mBinding.dateSpinner.getSelectedItem());
                byStatusKey = byStatusHashmap.get(mBinding.statusSpinner.getSelectedItem());
                byPriorityKey = byPriorityHashMap.get(mBinding.prioritySpinner.getSelectedItem());
                Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                mBinding.salesOrderFilterLayout.setAnimation(animation);
                mBinding.salesOrderFilterLayout.setVisibility(View.GONE);
                int supplierId=0;
                if (customerModel!=null)
                {
                    supplierId= customerModel.getSupplier_Id();
                }
                if (fromDate!=null&&toDate!=null)
                {
                    getSalesOrder(supplierId,fromDate,toDate,byStatusKey,byDateKey,byPriorityKey);
                }else
                {
                    getSalesOrder(supplierId,date,date,byStatusKey,byDateKey,byPriorityKey);
                }



            }
        });

        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections action = SalesOrderListFragmentDirections.actionSalesOrderListFragmentToSaleOrderFormFragment();

                navController.navigate(action);

            }
        });
    }


    public void setPullToFresh() {
        mBinding.swipeLayout.setOnRefreshListener(() -> {
            Calendar calendar = Calendar.getInstance();
            String date =String.valueOf(calendar.get(Calendar.YEAR))+getDate(calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DAY_OF_MONTH));

            mBinding.swipeLayout.setRefreshing(false);
            getSalesOrder(0,date,date,0,0,0);
        });
    }
    private void setUpRecyclerView() {

        mBinding.salesOrderRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new SaleOrderAdapter(this);
        mBinding.salesOrderRecyclerView.setAdapter(adapter);


    }

    private void getSalesOrder(int supplierID,String fromDate, String toDate, int byStatusKey, int byDateKey, int byPriorityKey) {
        progressDialog.show();

        Call<List<GetSaleOrderModel>> call = ApiClient.getInstance().getApi().getSalesOrder(1,1,1,1,
                fromDate,
                toDate,supplierID,byStatusKey,byDateKey,byPriorityKey);

        call.enqueue(new Callback<List<GetSaleOrderModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<GetSaleOrderModel>> call, @NotNull Response<List<GetSaleOrderModel>> response) {

                if (response.body()!=null)
                {
                    if (response.body().size()==0)
                    {
                        adapter.setGetSaleOrderModelList(response.body());
                        mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();

                    }
                    else {
                        mBinding.tvNothingFound.setVisibility(View.GONE);
                        adapter.setGetSaleOrderModelList(response.body());
                        progressDialog.dismiss();
                    }

                }
                else {
                    mBinding.tvNothingFound.setVisibility(View.VISIBLE);

                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), "web response"+ response.errorBody(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<GetSaleOrderModel>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), "web response"+ t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                mBinding.tvNothingFound.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.filter).setVisible(true);
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