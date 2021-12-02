package com.example.ffccloud.salesOrder;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.ModelClasses.InsertSaleOrderModel;
import com.example.ffccloud.ModelClasses.TermAndConditionModel;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentSaleOrderFormBinding;
import com.example.ffccloud.salesOrder.adapter.InsertProductRecyclerAdapter;
import com.example.ffccloud.utils.CONSTANTS;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SaleOrderFormFragment extends Fragment {

    private FragmentSaleOrderFormBinding mBinding;
    private NavController navController;
    private CustomerModel customerModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private int numberOfLines=0;
    private List<InsertProductModel> productModelList;
    private InsertProductRecyclerAdapter adapter;
    private ArrayList<String>  byPriorityList = new ArrayList<>();
    private HashMap<String, Integer> byPriorityHashMap = new HashMap<>();
    private String saleOrderDate,deliveryDate;
    private InsertSaleOrderModel saleOrderModel;
    private List<EditText> editTextList= new ArrayList<>();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSaleOrderFormBinding.inflate(inflater,container,false);

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior= BottomSheetBehavior.from(mBinding.bottomSheet.getRoot());

        setUpBottomSheet();
        bottomSheetBtnListener();
        setUpSpinners();
        for (int i=0;i<5;i++)
        {
            addLine();
            numberOfLines++;
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        btnListener();
        setUpRecyclerView();

        navController = NavHostFragment.findNavController(this);
        MutableLiveData<CustomerModel> customerModelMutableLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.CUSTOMER_KEY);
        customerModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<CustomerModel>() {
            @Override
            public void onChanged(CustomerModel model) {
                if (model!=null)
                {
                    customerModel = model;
                    mBinding.tvSelectCustomer.setText(customerModel.getPartyName());
                }


            }
        });
        MutableLiveData<List<InsertProductModel>> productModelMutableLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.PRODUCT_MODEL);
        productModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<InsertProductModel>>() {
            @Override
            public void onChanged(List<InsertProductModel> insertProductModels) {
                 productModelList= insertProductModels;
                 adapter.setProductModelList(productModelList);
            }
        });



    }

    private void setUpRecyclerView() {

        mBinding.productRecylerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter= new InsertProductRecyclerAdapter(this);
        mBinding.productRecylerView.setAdapter(adapter);
    }

    private void btnListener() {

        mBinding.salesOrderDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
               int mYear = calendar.get(Calendar.YEAR);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                int mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int checkMonth = month % 10, checkday = (dayOfMonth % 10);
                        ;
                        String mMonth, mDay,mDate;
                        if (checkMonth > 0 && month < 9) {
                            mMonth = "0" + (month + 1);
                        } else {
                            mMonth = String.valueOf(month + 1);

                        }

                        if (checkday > 0 && dayOfMonth < 10) {
                            mDay = "0" + (dayOfMonth);

                        } else {
                            mDay = String.valueOf(dayOfMonth);

                        }
                        mDate = mMonth + "/" + mDay + "/" + year;
                        saleOrderDate=String.valueOf(year)+ month + dayOfMonth;
                        mBinding.tvSalesOrderDate.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mBinding.deliveryDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                int mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int checkMonth = month % 10, checkday = (dayOfMonth % 10);
                        ;
                        String mMonth, mDay,mDate;
                        if (checkMonth > 0 && month < 9) {
                            mMonth = "0" + (month + 1);
                        } else {
                            mMonth = String.valueOf(month + 1);

                        }

                        if (checkday > 0 && dayOfMonth < 10) {
                            mDay = "0" + (dayOfMonth);

                        } else {
                            mDay = String.valueOf(dayOfMonth);

                        }
                        mDate = mMonth + "/" + mDay + "/" + year;
                        deliveryDate=String.valueOf(year)+ month + dayOfMonth;
                        mBinding.tvDeliveryDate.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        mBinding.addProductBtn.setOnClickListener(v -> {
            NavDirections action = SaleOrderFormFragmentDirections.actionSaleOrderFormFragmentToAddProductFragment();
            navController.navigate(action);

        });

        mBinding.tvSelectCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaleOrderFormFragmentDirections.ActionSaleOrderFormFragmentToCustomerListFragment action = SaleOrderFormFragmentDirections.actionSaleOrderFormFragmentToCustomerListFragment();
                action.setCallingFragmentKey(1);
                navController.navigate(action);
            }
        });

        mBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               saveSaleOrder();
            }
        });
    }

    private void setUpSpinners() {

        //setting for by priority spinner
        byPriorityList.add("High");
        byPriorityHashMap.put("High",1);
        byPriorityList.add("Normal");
        byPriorityHashMap.put("Normal",2);
        byPriorityList.add("Low");
        byPriorityHashMap.put("Low",3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, byPriorityList);
        mBinding.prioritySpinner.setAdapter(adapter2);
    }

    private void saveSaleOrder() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        int priorityId= byPriorityHashMap.get(mBinding.prioritySpinner.getSelectedItem().toString());
        List<TermAndConditionModel> termAndConditionModelList = new ArrayList<>();

        for (int i =0;i<editTextList.size();i++)
        {
            TermAndConditionModel model= new TermAndConditionModel();
            model.setSale_Order_Id(0);
            model.setSale_Order_Terms_Id(0);
            model.setTerms_Details(editTextList.get(i).getText().toString());
            termAndConditionModelList.add(model);
        }


        saleOrderModel = new InsertSaleOrderModel(productModelList,termAndConditionModelList,1,1,1,1,0,
                saleOrderDate,deliveryDate,
                mBinding.etAddress.getText().toString(),
                Integer.parseInt(mBinding.tvLedgerBalance.getText().toString()),
                Integer.parseInt(mBinding.tvCreditLimit.getText().toString()),
                customerModel.getPartyID(),priorityId,0);

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertSaleOrder(saleOrderModel);
        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response!=null)
                {
                    UpdateStatus updateStatus = response.body();
                    Toast.makeText(requireContext()," "+updateStatus.getStrMessage(),Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " "+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    private void setUpBottomSheet() {

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        mBinding.bottomSheet.headerBtn.setImageDrawable(drawable);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24);
                        mBinding.bottomSheet.headerBtn.setImageDrawable(drawable);
                        mBinding.btnSave.setVisibility(View.VISIBLE);

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        mBinding.bottomSheet.headerBtn.setImageDrawable(drawable);
                        mBinding.btnSave.setVisibility(View.GONE);

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });




    }

    public void bottomSheetBtnListener()
    {
        mBinding.bottomSheet.addLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLine();
            }
        });
    }
    public void addLine() {
        LinearLayout linearLayout = (LinearLayout)mBinding.getRoot().findViewById(R.id.term_and_condition_bottom_sheet);
        // add edittext
        EditText editText = new EditText(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,15,0,0);

        editText.setLayoutParams(layoutParams);
        editText.setPadding(5,5,5,5);
        editText.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.spinner_bg,null));
        editText.setText("Text");
        editText.setId(numberOfLines + 1);
        linearLayout.addView(editText);
        editTextList.add(editText);
        numberOfLines++;
    }

}