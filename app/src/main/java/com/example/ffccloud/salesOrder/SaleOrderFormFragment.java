package com.example.ffccloud.salesOrder;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.model.DeliveryModeModel;
import com.example.ffccloud.model.GetLedgerBalanceModel;
import com.example.ffccloud.model.GetSaleOrderDetail;
import com.example.ffccloud.model.InsertSaleOrderModel;
import com.example.ffccloud.model.TermAndConditionModel;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.FragmentSaleOrderFormBinding;
import com.example.ffccloud.databinding.PdfViewDialogBinding;
import com.example.ffccloud.salesOrder.adapter.InsertProductRecyclerAdapter;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.Permission;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private List<InsertProductModel> productModelList = new ArrayList<>();
    private InsertProductRecyclerAdapter adapter;
    private final ArrayList<String> byPriorityList = new ArrayList<>(), deliverModeList = new ArrayList<>();
    private final HashMap<String, Integer> byPriorityHashMap = new HashMap<>();
    private final HashMap<String, Integer> deliverModeHashMapId = new HashMap<>();
    private final HashMap<Integer, String> deliverModeHashMapTitle = new HashMap<>();
    private String saleOrderDate = "", deliveryDate = "";
    private final List<EditText> editTextList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private UserViewModel userViewModel;
    private int saleOrderID = 0, supplierID = 0, ledgerBalance = 0, creditLimit = 0;
    private boolean flag = false;
    private List<TermAndConditionModel> termAndConditionModelList = new ArrayList<>();
    private boolean isSpinnerUpdate = false, isSaved = false;
    private int priorityPosition = -1, deliveryModePosition = -1;
    private PdfDocument pdfDocument;
    private File file;

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

        if (!isSpinnerUpdate) {
            isSpinnerUpdate = true;
            setUpSpinners();

        }

        setUpRecyclerView();
        setUpBottomSheet();
        bottomSheetBtnListener();
        getLiveData();


        if (getArguments() != null) {
            saleOrderID = SaleOrderFormFragmentArgs.fromBundle(getArguments()).getSaleOrderId();
        }
        if (saleOrderID != 0 && !flag) {
            flag = true;
            getSaleOrder();

        }


    }

    public void getLiveData() {
        MutableLiveData<CustomerModel> customerModelMutableLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.CUSTOMER_KEY);
        customerModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<CustomerModel>() {
            @Override
            public void onChanged(CustomerModel model) {
                if (model != null) {
                    customerModel = model;
                    mBinding.tvSelectCustomer.setText(customerModel.getPartyName());
                    supplierID = customerModel.getSupplier_Id();
                    getLedgerBalance();


                }
                setUpAgain();

            }
        });
        MutableLiveData<List<InsertProductModel>> productModelMutableLiveData = Objects.requireNonNull(navController.getCurrentBackStackEntry())
                .getSavedStateHandle()
                .getLiveData(CONSTANTS.PRODUCT_MODEL);
        productModelMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<InsertProductModel>>() {
            @Override
            public void onChanged(List<InsertProductModel> insertProductModels) {
                if (!productModelList.equals(insertProductModels)) {
                    productModelList.addAll(insertProductModels);


                }
                setUpAgain();

            }
        });
    }

    private void setUpAgain() {
        mBinding.tvLedgerBalance.setText(String.valueOf(ledgerBalance));
        mBinding.tvCreditLimit.setText(String.valueOf(creditLimit));
        mBinding.tvSalesOrderDate.setText(saleOrderDate);
        mBinding.tvDeliveryDate.setText(deliveryDate);
        adapter.setProductModelList(productModelList);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, byPriorityList);
        mBinding.prioritySpinner.setAdapter(adapter2);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, deliverModeList);
        mBinding.deliverModeSpinner.setAdapter(adapter);

        if (deliveryModePosition != -1) {
            mBinding.deliverModeSpinner.setSelection(deliveryModePosition);
        }

        if (priorityPosition != -1) {
            mBinding.prioritySpinner.setSelection(priorityPosition);
        }

        int i = 0;
        for (TermAndConditionModel model : termAndConditionModelList) {
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
                if (supplierID > 0) {
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        saveSaleOrder();
                    } else {
                        showNotification();
                    }
                } else {
                    Permission permission = new Permission(requireContext(),requireActivity());
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                        {
                            saveSaleOrder();
                        }else
                        {
                            permission.getWriteStoragePermission();
                        }


                        }
                    else
                    {
                        permission.getStoragePermission();
                    }

                }


            }
        });


        mBinding.prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priorityPosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mBinding.deliverModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deliveryModePosition = position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                    deliverModeHashMapTitle.put((int) modeModel.getDeliveryModeId(), modeModel.getDeliveryMode());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, deliverModeList);
                mBinding.deliverModeSpinner.setAdapter(adapter);

                progressDialog.dismiss();
            }
        });


    }

    public void getSaleOrder() {
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<GetSaleOrderDetail> call = ApiClient.getInstance().getApi().getSaleOrderDetail(1, 1, 1, 1, saleOrderID);

        call.enqueue(new Callback<GetSaleOrderDetail>() {
            @Override
            public void onResponse(@NotNull Call<GetSaleOrderDetail> call, @NotNull Response<GetSaleOrderDetail> response) {
                if (response.body() != null) {
                    GetSaleOrderDetail getSaleOrderDetail = response.body();
                    setUpFields(getSaleOrderDetail);
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(@NotNull Call<GetSaleOrderDetail> call, @NotNull Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setUpFields(GetSaleOrderDetail getSaleOrderDetail) {

        saleOrderDate = getSaleOrderDetail.getTable().get(0).getSaleOrderDate();
        deliveryDate = getSaleOrderDetail.getTable().get(0).getDeliveryDate();
        ledgerBalance = (int) getSaleOrderDetail.getTable().get(0).getLedgerBalance();
        creditLimit = (int) getSaleOrderDetail.getTable().get(0).getCreditLimit();
        supplierID = (int) getSaleOrderDetail.getTable().get(0).getSupplierId();
        mBinding.tvSalesOrderDate.setText(saleOrderDate.substring(0, 9));
        mBinding.tvDeliveryDate.setText(deliveryDate.substring(0, 9));
        mBinding.etAddress.setText(getSaleOrderDetail.getTable().get(0).getDeliveryLocation());

        int priority = (int) getSaleOrderDetail.getTable().get(0).getPriorityId();

        if (priority == 1) {
            mBinding.prioritySpinner.setSelection(0);
        } else if (priority == 2) {
            mBinding.prioritySpinner.setSelection(1);

        } else if (priority == 3) {
            mBinding.prioritySpinner.setSelection(2);

        }

        int deliverModeID = (int) getSaleOrderDetail.getTable().get(0).getDeliveryModeId();
        String deliveryModeTitle = deliverModeHashMapTitle.get(deliverModeID);
        mBinding.deliverModeSpinner.setSelection(deliverModeList.indexOf(deliveryModeTitle));

        mBinding.tvLedgerBalance.setText(String.valueOf((int) getSaleOrderDetail.getTable().get(0).getLedgerBalance()));
        mBinding.tvCreditLimit.setText(String.valueOf((int) getSaleOrderDetail.getTable().get(0).getCreditLimit()));
        productModelList = getSaleOrderDetail.getTable1();
        adapter.setProductModelList(productModelList);
        termAndConditionModelList = getSaleOrderDetail.getTable3();
        int i = 0;
        for (TermAndConditionModel model : termAndConditionModelList) {
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

        for (int i = 0; i < productModelList.size(); i++) {
            productModelList.get(i).setSale_Order_Id(saleOrderID);
        }


        String address = mBinding.etAddress.getText() != null ? mBinding.etAddress.getText().toString() : " ";
        InsertSaleOrderModel saleOrderModel = new InsertSaleOrderModel(productModelList, termAndConditionModelList, 1, 1, 1, 1, saleOrderID,
                saleOrderDate, deliveryDate,
                address,
                ledgerBalance,
                creditLimit,
                supplierID, priorityId,
                deliverModeHashMapId.get(mBinding.deliverModeSpinner.getSelectedItem().toString()));

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertSaleOrder(saleOrderModel);
        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response.isSuccessful()) {
                    UpdateStatus updateStatus = response.body();
                    if (updateStatus != null) {
                        Toast.makeText(requireContext(), " " + updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                        if (updateStatus.getStrMessage().equals("Sales Order saved successfully.")) {
                            createPDF(saleOrderModel);

                        }
                    }
                    progressDialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "" + response.errorBody(), Toast.LENGTH_SHORT).show();
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
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
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
                        ledgerBalance = model.getLedgerBalance();
                        creditLimit = model.getCreditLimit();
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

    private void createPDF(InsertSaleOrderModel saleOrderModel) {


        pdfDocument = new PdfDocument();
        int height = (productModelList.size() * 20) + 330 + 100;


        String[] pdfColumnName = {"Customer Name :", "Delivery Date :", "Ledger Balance :", "Credit Limit :"};
        Bitmap bitMapHeaderFooter, headerFooterScaleableBitMap, bitMapLogo, LogoScaledBitMap;

        bitMapHeaderFooter = BitmapFactory.decodeResource(getResources(), R.drawable.pdf_header_footer);
        bitMapLogo = BitmapFactory.decodeResource(getResources(), R.drawable.img_erp_cloud_logo);
        headerFooterScaleableBitMap = Bitmap.createScaledBitmap(bitMapHeaderFooter, 350, 20, false);
        LogoScaledBitMap = Bitmap.createScaledBitmap(bitMapLogo, 30, 30, false);


        Paint paint = new Paint();

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(350, height, 1).create();

        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        canvas.drawBitmap(headerFooterScaleableBitMap, 0, 0, paint);
        canvas.drawBitmap(headerFooterScaleableBitMap, 0, pageInfo.getPageHeight() - 10, paint);
        canvas.drawBitmap(LogoScaledBitMap, 300, pageInfo.getPageHeight() - 50, paint);

//        paint.setTextAlign(Paint.Align.LEFT);
//        paint.setTextSize(10f);
//        canvas.drawText("FFC",10,20,paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(16f);
        paint.setColor(Color.rgb(40, 106, 156));
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        canvas.drawText("Sale Order", pageInfo.getPageWidth() / 2, 70, paint);

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(Color.rgb(40, 106, 156));
        canvas.drawText("Sale Order Detail", 10, 130, paint);

        int startXPosition = 10;
        int startYPosition = 160;
        int endXPosition = pageInfo.getPageWidth() - 10;
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        paint.setTextSize(12);


        for (int i = 0; i < pdfColumnName.length; i++) {
            paint.setColor(Color.rgb(40, 106, 156));
            canvas.drawText(pdfColumnName[i], startXPosition, startYPosition, paint);
            paint.setColor(Color.GRAY);
            canvas.drawLine(startXPosition, startYPosition + 5, endXPosition, startYPosition + 5, paint);
            paint.setColor(Color.BLACK);

            if (i == 0) {
                canvas.drawText(customerModel.getPartyName(), 130, startYPosition, paint);

            } else if (i == 1) {
                canvas.drawText(saleOrderModel.getDelivery_Date(), 130, startYPosition, paint);

            } else if (i == 2) {
                if (saleOrderModel.getLedger_Balance() != 0) {
                    canvas.drawText(String.valueOf(saleOrderModel.getLedger_Balance()), 130, startYPosition, paint);

                } else {
                    canvas.drawText("0", 130, startYPosition, paint);

                }

            } else if (i == 3) {
                if (saleOrderModel.getCredit_Limit() != 0) {
                    canvas.drawText(String.valueOf(saleOrderModel.getCredit_Limit()), 130, startYPosition, paint);

                } else {
                    canvas.drawText("0", 130, startYPosition, paint);

                }

            }
            startYPosition += 20;
        }

        startYPosition += 20;

        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(14);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        paint.setColor(Color.rgb(40, 106, 156));
        canvas.drawText("Products", 10, startYPosition, paint);
        startYPosition += 20;

        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        String[] productColumnName = {"Name", "Unit", "Quantity", "Rate", "Amount"};
        int xPosition = 10;
        for (int i = 0; i < 5; i++) {
            paint.setTextSize(12);
            paint.setColor(Color.rgb(40, 106, 156));
            canvas.drawText(productColumnName[i], xPosition, startYPosition, paint);

            xPosition += 66;

        }

        startYPosition += 20;


        paint.setColor(Color.GRAY);
        canvas.drawLine(10, startYPosition, pageInfo.getPageWidth() - 10, startYPosition, paint);
        startYPosition += 30;

        for (InsertProductModel model : productModelList) {
            xPosition = 10;

            paint.setColor(Color.BLACK);

            for (int i = 0; i < 5; i++) {
                paint.setTextSize(10);
                if (i == 0) {
                    if (model.getTitleProduct().length() > 12) {
                        canvas.drawText(model.getTitleProduct().substring(0, 12), xPosition, startYPosition, paint);
                    } else {
                        canvas.drawText(model.getTitleProduct(), xPosition, startYPosition, paint);

                    }

                } else if (i == 1) {
                    canvas.drawText(model.getUnitProduct(), xPosition + 10, startYPosition, paint);

                } else if (i == 2) {
                    double quantity = model.getQty();
                    if (quantity != 0) {
                        canvas.drawText(String.valueOf(quantity), xPosition, startYPosition, paint);

                    } else {
                        canvas.drawText("0", xPosition, startYPosition, paint);

                    }

                } else if (i == 3) {
                    double rate = model.getRate();
                    if (rate != 0) {
                        canvas.drawText(String.valueOf(rate), xPosition, startYPosition, paint);

                    } else {
                        canvas.drawText("0", xPosition, startYPosition, paint);

                    }

                } else {
                    double amount = model.getAmount();
                    if (amount != 0) {
                        canvas.drawText(String.valueOf(amount), xPosition, startYPosition, paint);

                    } else {
                        canvas.drawText("0", xPosition, startYPosition, paint);

                    }
                }

                xPosition += 66;

            }
            paint.setColor(Color.GRAY);
            canvas.drawLine(10, startYPosition + 5, pageInfo.getPageWidth() - 10, startYPosition + 5, paint);
            startYPosition += 20;


        }

        pdfDocument.finishPage(page);
        String stringFilePath;
//        if (android.os.Build.VERSION.SDK_INT== Build.VERSION_CODES.Q) {
//            stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/SaleOrder.pdf";
//
//        }
//        else
//        {
//            stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/SaleOrder.pdf";
//
//        }


        stringFilePath = Environment.getExternalStorageDirectory().getPath() + "/Download/SaleOrder.pdf";
        Date now = new Date();
        DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);


        file = new File(stringFilePath);
        savePdf(file);


        showPdfDialog(height);
    }

    private void showNotification() {
        CustomAlertDialogBinding binding = CustomAlertDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(binding.getRoot()).setCancelable(false).create();

        alertDialog.show();

        binding.title.setText("Permission Needed");
        binding.body.setText("Please allow to manage storage in this device for the better experience of app.");
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(requireContext(), "Pdf Not generated", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        binding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupSettingIntent();
                alertDialog.dismiss();
            }
        });

    }

    private void showPdfDialog(int height) {


        PdfViewDialogBinding binding = PdfViewDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                .setView(binding.getRoot())
                .setCancelable(true)
                .create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//
//        lp.copyFrom(alertDialog.getWindow().getAttributes());
//        lp.width = 350;
//        lp.height = height+150;
//        lp.x=-170;
//        lp.y=100;
//        alertDialog.getWindow().setAttributes(lp);
        alertDialog.show();

        ParcelFileDescriptor parcelFileDescriptor = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            try {
                parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                PdfRenderer pdfRenderer = new PdfRenderer(parcelFileDescriptor);
                PdfRenderer.Page page = pdfRenderer.openPage(0);
                Bitmap bitmap = Bitmap.createBitmap(500, height + 300, Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                binding.pdfView.setImageBitmap(bitmap);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(requireContext(), "PDF Not Viewer Not Supported on this android version", Toast.LENGTH_SHORT).show();
        }

        binding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = true;
                Toast.makeText(requireContext(), "Invoice saved", Toast.LENGTH_SHORT).show();
            }
        });
        binding.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePdf(file);
            }
        });


    }

    private void sharePdf(File file) {

//        Uri uri = Uri.fromFile(file);
//
//        Intent share = new Intent();
//        share.setAction(Intent.ACTION_SEND);
//        share.setType("application/pdf");
//        share.putExtra(Intent.EXTRA_STREAM, uri);
//        requireActivity().startActivity(share);
//        pdfDocument.close();

//        Intent intent = new Intent(Intent.ACTION_SEND);
//        Uri uri = Uri.parse(file.getPath());
//        String mimeType = "application/pdf";
//
//        intent.setDataAndType(uri, mimeType);
//        try {
//            requireActivity().startActivity(intent);
//
//        }
//        catch (Exception e){
//            Toast.makeText(requireContext(), "Nothing Found to open this file", Toast.LENGTH_SHORT).show();
//        }

        Uri uri = Uri.parse(file.getAbsolutePath());
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, "The text you wanted to share");
        whatsappIntent.putExtra(Intent.EXTRA_STREAM, uri);
        whatsappIntent.setType("application/pdf");
        whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            requireActivity().startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(requireContext(), "Nothing Found to open this file", Toast.LENGTH_SHORT).show();
        }

    }

    private void savePdf(File file) {


        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(requireContext(), "Sale Order PDF generated ", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    public void deletePdf(File file) {

        if (file != null) {
            Log.d("Delete", "Directory to find ... " + file.getAbsolutePath());
            if (file.exists()) {
                Log.d("Delete", "File Exists");
                if (file.delete()) {
                    Log.d("Deleted", "" + file.getAbsolutePath());
                }

            } else {
                Toast.makeText(requireContext(), "File Not Exists", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isSaved) {
            deletePdf(file);
        }
        if
        (pdfDocument != null) {
            pdfDocument.close();

        }
    }

    public void setupSettingIntent() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", requireActivity().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

}