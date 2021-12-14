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
import androidx.lifecycle.ViewModelProvider;
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
import com.example.ffccloud.ModelClasses.DeliveryModeModel;
import com.example.ffccloud.ModelClasses.GetLedgerBalanceModel;
import com.example.ffccloud.ModelClasses.GetSaleOrderDetail;
import com.example.ffccloud.ModelClasses.InsertSaleOrderModel;
import com.example.ffccloud.ModelClasses.TermAndConditionModel;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentSaleOrderFormBinding;
import com.example.ffccloud.salesOrder.adapter.InsertProductRecyclerAdapter;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.UserViewModel;
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
    private int numberOfLines = 0;
    private List<InsertProductModel> productModelList= new ArrayList<>();
    private InsertProductRecyclerAdapter adapter;
    private final ArrayList<String> byPriorityList = new ArrayList<>(), deliverModeList = new ArrayList<>();
    private HashMap<String, Integer> byPriorityHashMap = new HashMap<>(), deliverModeHashMapId = new HashMap<>();
    private HashMap<Integer, String>  deliverModeHashMapTitle = new HashMap<>();
    private String saleOrderDate = "", deliveryDate = "";
    private InsertSaleOrderModel saleOrderModel;
    private List<EditText> editTextList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private UserViewModel userViewModel;
    private int saleOrderID = 0,supplierID=0,ledgerBalance=0,creditLimit=0;
    private boolean flag= false;
    private List<TermAndConditionModel> termAndConditionModelList= new ArrayList<>();
    private boolean isSpinnerUpdate=false;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSaleOrderFormBinding.inflate(inflater, container, false);

        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        bottomSheetBehavior = BottomSheetBehavior.from(mBinding.bottomSheet.getRoot());
        navController = NavHostFragment.findNavController(this);

        if (!isSpinnerUpdate)
        {
            isSpinnerUpdate=true;
            setUpSpinners();

        }

        setUpRecyclerView();
        setUpBottomSheet();
        bottomSheetBtnListener();
        getLiveData();


        saleOrderID = SaleOrderFormFragmentArgs.fromBundle(getArguments()).getSaleOrderId();
        if (saleOrderID!=0 && !flag)
        {
            flag=true;
            getSaleOrder();

        }


    }
    public void getLiveData()
    {
        MutableLiveData<CustomerModel> customerModelMutableLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.CUSTOMER_KEY);
        customerModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<CustomerModel>() {
            @Override
            public void onChanged(CustomerModel model) {
                if (model != null) {
                    customerModel = model;
                    mBinding.tvSelectCustomer.setText(customerModel.getPartyName());
                    supplierID= customerModel.getSupplier_Id();
                    getLedgerBalance();
                    setUpAgain();

                }


            }
        });
        MutableLiveData<List<InsertProductModel>> productModelMutableLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.PRODUCT_MODEL);
        productModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<InsertProductModel>>() {
            @Override
            public void onChanged(List<InsertProductModel> insertProductModels) {
                productModelList.addAll(insertProductModels);
                adapter.setProductModelList(productModelList);
                setUpAgain();

            }
        });
    }

    private void setUpAgain() {
        mBinding.tvLedgerBalance.setText(String.valueOf(ledgerBalance));
        mBinding.tvCreditLimit.setText(String.valueOf(creditLimit));
        mBinding.tvSalesOrderDate.setText(saleOrderDate);
        mBinding.tvDeliveryDate.setText(saleOrderDate);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, byPriorityList);
        mBinding.prioritySpinner.setAdapter(adapter2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, deliverModeList);
        mBinding.deliverModeSpinner.setAdapter(adapter);

        int i=0;
        for (TermAndConditionModel model: termAndConditionModelList)
        {
            addLine();
            editTextList.get(i).setText(model.getTerms_Details());
            i++;
        }
    }


    @Override
    public void onResume() {
        super.onResume();


        btnListener();



        getLedgerBalance();
    }


    private void setUpRecyclerView() {

        mBinding.productRecylerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new InsertProductRecyclerAdapter(this);
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
                        String mMonth, mDay, mDate;
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
                        saleOrderDate = mDate;
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
                        String mMonth, mDay, mDate;
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
                        deliveryDate = mDate;
                        mBinding.tvDeliveryDate.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        mBinding.addProductBtn.setOnClickListener(v -> {
            if (!saleOrderDate.isEmpty()) {
                if (supplierID>0) {
                    SaleOrderFormFragmentDirections.ActionSaleOrderFormFragmentToAddProductFragment action = SaleOrderFormFragmentDirections.actionSaleOrderFormFragmentToAddProductFragment();
                    action.setDate(saleOrderDate);
                    action.setSupplierId(supplierID);
                    navController.navigate(action);

                } else {
                    Toast.makeText(requireContext(), "Please select customer", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(requireContext(), "Please select sale order date", Toast.LENGTH_SHORT).show();
            }
            ;

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
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //setting for by priority spinner
        byPriorityList.add("High");
        byPriorityHashMap.put("High", 1);
        byPriorityList.add("Normal");
        byPriorityHashMap.put("Normal", 2);
        byPriorityList.add("Low");
        byPriorityHashMap.put("Low", 3);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, byPriorityList);
        mBinding.prioritySpinner.setAdapter(adapter2);

        userViewModel.getAllDeliveryModes().observe(getViewLifecycleOwner(), new Observer<List<DeliveryModeModel>>() {
            @Override
            public void onChanged(List<DeliveryModeModel> list) {
                for (DeliveryModeModel modeModel : list) {
                    deliverModeList.add(modeModel.getDeliveryMode());
                    deliverModeHashMapId.put(modeModel.getDeliveryMode(), (int) modeModel.getDeliveryModeId());
                    deliverModeHashMapTitle.put((int)modeModel.getDeliveryModeId(),  modeModel.getDeliveryMode());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, deliverModeList);
                mBinding.deliverModeSpinner.setAdapter(adapter);

                progressDialog.dismiss();
            }
        });


    }

    public void getSaleOrder()
    {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<GetSaleOrderDetail> call = ApiClient.getInstance().getApi().getSaleOrderDetail(1,1,1,1,saleOrderID);

        call.enqueue(new Callback<GetSaleOrderDetail>() {
            @Override
            public void onResponse(@NotNull Call<GetSaleOrderDetail> call, @NotNull Response<GetSaleOrderDetail> response) {
                if (response.body()!=null)
                {
                    GetSaleOrderDetail getSaleOrderDetail = response.body();
                    setUpFields(getSaleOrderDetail);
                    progressDialog.dismiss();
                }
                else
                {
                    Toast.makeText(requireContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(@NotNull Call<GetSaleOrderDetail> call, @NotNull Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(requireContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpFields(GetSaleOrderDetail getSaleOrderDetail) {

         saleOrderDate= getSaleOrderDetail.getTable().get(0).getSaleOrderDate();
         deliveryDate= getSaleOrderDetail.getTable().get(0).getDeliveryDate();
         ledgerBalance= (int) getSaleOrderDetail.getTable().get(0).getLedgerBalance();
         creditLimit= (int) getSaleOrderDetail.getTable().get(0).getCreditLimit();
        supplierID= (int) getSaleOrderDetail.getTable().get(0).getSupplierId();
        mBinding.tvSalesOrderDate.setText(saleOrderDate.substring(0,9));
        mBinding.tvDeliveryDate.setText(deliveryDate.substring(0,9));
        mBinding.etAddress.setText(getSaleOrderDetail.getTable().get(0).getDeliveryLocation());

        int priority=(int) getSaleOrderDetail.getTable().get(0).getPriorityId();

        if (priority==1)
        {
            mBinding.prioritySpinner.setSelection(0);
        }
        else if (priority==2)
        {
            mBinding.prioritySpinner.setSelection(1);

        }  else if (priority==3)
        {
            mBinding.prioritySpinner.setSelection(2);

        }

        int deliverModeID= (int) getSaleOrderDetail.getTable().get(0).getDeliveryModeId();
        String deliveryModeTitle = deliverModeHashMapTitle.get(deliverModeID);
        mBinding.deliverModeSpinner.setSelection(deliverModeList.indexOf(deliveryModeTitle));

        mBinding.tvLedgerBalance.setText(String.valueOf((int) getSaleOrderDetail.getTable().get(0).getLedgerBalance()));
        mBinding.tvCreditLimit.setText(String.valueOf((int) getSaleOrderDetail.getTable().get(0).getCreditLimit()));
        productModelList= getSaleOrderDetail.getTable1();
        adapter.setProductModelList(productModelList);
        termAndConditionModelList= getSaleOrderDetail.getTable3();
        int i=0;
        for (TermAndConditionModel model: termAndConditionModelList)
        {
            addLine();
            editTextList.get(i).setText(model.getTerms_Details());
            i++;
        }
    }

    private void saveSaleOrder() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();


        int priorityId = byPriorityHashMap.get(mBinding.prioritySpinner.getSelectedItem().toString());
        List<TermAndConditionModel> termAndConditionModelList = new ArrayList<>();

        for (int i = 0; i < editTextList.size(); i++) {
            TermAndConditionModel model = new TermAndConditionModel();
            model.setSale_Order_Id(saleOrderID);
            model.setSale_Order_Terms_Id(0);
            model.setTerms_Details(editTextList.get(i).getText().toString());
            termAndConditionModelList.add(model);
        }

        for (int i=0;i<productModelList.size();i++)
        {
            productModelList.get(i).setSale_Order_Id(saleOrderID);
        }

        saleOrderModel = new InsertSaleOrderModel(productModelList, termAndConditionModelList, 1, 1, 1, 1, saleOrderID,
                saleOrderDate, deliveryDate,
                mBinding.etAddress.getText().toString(),
                Integer.parseInt(mBinding.tvLedgerBalance.getText().toString()),
                Integer.parseInt(mBinding.tvCreditLimit.getText().toString()),
                supplierID, priorityId,
                deliverModeHashMapId.get(mBinding.deliverModeSpinner.getSelectedItem().toString()));

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertSaleOrder(saleOrderModel);
        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response != null) {
                    UpdateStatus updateStatus = response.body();
                    Toast.makeText(requireContext(), " " + updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void bottomSheetBtnListener() {
        mBinding.bottomSheet.addLineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLine();
            }
        });
    }

    public void addLine() {
        LinearLayout linearLayout = (LinearLayout) mBinding.getRoot().findViewById(R.id.term_and_condition_bottom_sheet);
        // add edittext
        EditText editText = new EditText(requireContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 15, 0, 0);

        editText.setLayoutParams(layoutParams);
        editText.setPadding(5, 5, 5, 5);
        editText.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.spinner_bg, null));
        editText.setText("Text");
        editText.setId(numberOfLines + 1);
        linearLayout.addView(editText);
        editTextList.add(editText);
        numberOfLines++;
    }

    private void getLedgerBalance() {

        if (customerModel != null) {
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
            Call<GetLedgerBalanceModel> call = ApiClient.getInstance().getApi().getLedgerBalance(token, 1,
                    1, 1, 1, customerModel.getSupplier_Id());

            call.enqueue(new Callback<GetLedgerBalanceModel>() {
                @Override
                public void onResponse(@NotNull Call<GetLedgerBalanceModel> call, @NotNull Response<GetLedgerBalanceModel> response) {

                    if (response.body() != null) {
                        GetLedgerBalanceModel model = new GetLedgerBalanceModel();
                        model = response.body();
                        ledgerBalance= model.getLedgerBalance();
                        creditLimit= model.getCreditLimit();
                        mBinding.tvLedgerBalance.setText(String.valueOf(model.getLedgerBalance()));
                        mBinding.tvCreditLimit.setText(String.valueOf(model.getCreditLimit()));
                        progressDialog.dismiss();
                    } else {
                        Toast.makeText(requireContext(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }

                @Override
                public void onFailure(@NotNull Call<GetLedgerBalanceModel> call, @NotNull Throwable t) {
                    Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();

                    progressDialog.dismiss();
                }
            });
        }


    }


}