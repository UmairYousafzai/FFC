package com.example.ffccloud.salesOrder;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.FragmentProductInfoBottomSheetDialogBinding;
import com.example.ffccloud.model.RateListModel;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInfoBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private FragmentProductInfoBottomSheetDialogBinding mBinding;
    private NavController navController;
    private InsertProductModel productModel= new InsertProductModel();
    private final List<String> priceTypeList=new ArrayList<>();
    private String saleOrderDate=" ";
    private int supplierID=0;
    private RateListModel rateListModel = new RateListModel();

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentProductInfoBottomSheetDialogBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            saleOrderDate = ProductInfoBottomSheetDialogFragmentArgs.fromBundle(getArguments()).getDate();
            supplierID = ProductInfoBottomSheetDialogFragmentArgs.fromBundle(getArguments()).getSupplierId();
            productModel= ProductInfoBottomSheetDialogFragmentArgs.fromBundle(getArguments()).getProductModel();
        }




    }

    @Override
    public void onResume() {
        super.onResume();



        navController = NavHostFragment.findNavController(this);

        btnListener();
        editTextListener();
        setUpSpinner();
        openKeyBoard();


    }

    private void setUpSpinner() {

        priceTypeList.add("Default Price");
        priceTypeList.add("Scheme  N/A");
        priceTypeList.add("Bonus Adjust");
        priceTypeList.add("Add Bonus");
        priceTypeList.add("Special Discount");
        priceTypeList.add("Sample");
        priceTypeList.add("Gift");
        priceTypeList.add("Bonus");

        ArrayAdapter<String> adapter= new ArrayAdapter<>(requireContext(),android.R.layout.simple_spinner_dropdown_item,priceTypeList);
        mBinding.priceTypeSpinner.setAdapter(adapter);

    }

    private void editTextListener() {

        mBinding.etOrderQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty())
                {
                    try {
                        long rate = (long)rateListModel.getSaleRate();
                        long discountPercentage=0;
                        if (mBinding.etDiscountPercentage.getText()!=null)
                        {
                             discountPercentage =Long.parseLong(mBinding.etDiscountPercentage.getText().toString());

                        }
                        long gstPercentage = (long)rateListModel.getsTPercnt();
                        long quantity = Long.parseLong(s.toString());
                        long  amount =rate*quantity ;
                        long  discount =(discountPercentage*amount)/100;
                        long discountedValue = amount - discount;
                        long gst = (discountedValue *gstPercentage)/100;
                        long netAmount = discountedValue+gst;

                        mBinding.etAmount.setText(String.valueOf(amount));
                        mBinding.etDiscount.setText(String.valueOf(discount));
                        mBinding.etDiscountedValue.setText(String.valueOf(discountedValue));
                        mBinding.etGst.setText(String.valueOf(gst));
                        mBinding.etNetAmount.setText(String.valueOf(netAmount));
                    }catch (Exception exception)
                    {
                        Log.e("product Price Error:",exception.getMessage());
                    }

                }


            }
        });



        mBinding.etDiscountPercentage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String amountString="0";

                if (mBinding.etAmount.getText()!=null)
                {
                     amountString = mBinding.etAmount.getText().toString();

                }
                    if (!amountString.equals(""))
                    {
                        if (!s.toString().isEmpty())
                        {
                            long amount = Long.parseLong(amountString);
                            long percentage = Long.parseLong(s.toString());

                            long discount= getPercentageAmount(amount,percentage);
                            mBinding.etDiscount.setText(String.valueOf(discount));

                            long discountedValue = amount - discount;
                            mBinding.etDiscountedValue.setText(String.valueOf(discountedValue));

                            long gstPercentage = (long)rateListModel.getsTPercnt();

                            long gst = (discountedValue *gstPercentage)/100;
                            mBinding.etGst.setText(String.valueOf(gst));
                            
                            long netAmount = discountedValue+gst;

                            mBinding.etNetAmount.setText(String.valueOf(netAmount));
                        }


                }

            }
        });
    }

    private void btnListener() {

        mBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                closeKeyBoard();
                if (productModel!=null)
                {
                    if (rateListModel.getSaleRate()!=0.0)
                    {
                        try {
                            if(mBinding.etAmount.getText()!=null)
                            {
                                if (mBinding.etAmount.getText().toString().length()>0)
                                {
                                    productModel.setAmount(Long.parseLong(mBinding.etAmount.getText().toString()));

                                }

                            }
                            if (mBinding.etComment.getText()!=null)
                            {

                                    productModel.setComments(mBinding.etComment.getText().toString());


                            }
                            if (mBinding.etDiscount.getText()!=null)
                            {
                                if (mBinding.etDiscount.getText().toString().length()>0)
                                {
                                    productModel.setDisc_Amount(Long.parseLong(mBinding.etDiscount.getText().toString()));
                                }

                            }
                            if (mBinding.etDiscountPercentage.getText()!=null)
                            {
                                if (mBinding.etDiscountPercentage.getText().toString().length()>0)
                                {

                                    productModel.setDisc_Percentage(Long.parseLong(mBinding.etDiscountPercentage.getText().toString()));
                                }

                            }
                            if (mBinding.etDiscountedValue.getText()!=null)
                            {
                                if (mBinding.etDiscountedValue.getText().toString().length()>0)
                                {
                                    productModel.setDiscounted_Value(Long.parseLong(mBinding.etDiscountedValue.getText().toString()));

                                }

                            }
                            if (mBinding.etNetAmount.getText()!=null)
                            {
                                if (mBinding.etNetAmount.getText().toString().length()>0)
                                {

                                    productModel.setNet_Amount(Long.parseLong(mBinding.etNetAmount.getText().toString()));
                                }

                            }

                            if (mBinding.etOrderQuantity.getText()!=null)
                            {
                                if (mBinding.etOrderQuantity.getText().toString().length()>0)
                                {
                                    productModel.setQty(Long.parseLong(mBinding.etOrderQuantity.getText().toString()));
                                }

                            }

                            if (mBinding.etGst.getText()!=null)
                            {
                                if( mBinding.etGst.getText().toString().length()>0)
                                {
                                    productModel.setST_Amount(Long.parseLong(mBinding.etGst.getText().toString()));
                                }

                            }
                            if (mBinding.etGstPercentage.getText()!=null)
                            {
                                if( mBinding.etGstPercentage.getText().toString().length()>0)
                                {
                                    productModel.setST_Percentage(Long.parseLong(mBinding.etGstPercentage.getText().toString()));

                                }

                            }

                            if (productModel!=null)
                            {
                                productModel.setItem_Code(productModel.getItem_Code());
                            }


                            if (mBinding.etRate.getText()!=null)
                            {
                                if( mBinding.etRate.getText().toString().length()>0)
                                {
                                    productModel.setRate(Double.parseDouble(mBinding.etRate.getText().toString()));
                                }

                            }

                            if (navController.getPreviousBackStackEntry()!=null)
                            {
                                navController.getPreviousBackStackEntry().getSavedStateHandle().set(CONSTANTS.PRODUCT_MODEL,productModel);
                                navController.popBackStack();
                            }




                        }
                        catch (Exception e)
                        {
                            Log.e("Product pricing Error: ",e.getMessage());
                        }
                    }
                    else
                    {
                        Toast.makeText(requireContext() , "Cannot save product, because product rate is Null", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(requireContext() , "Cannot save product, because product is Null", Toast.LENGTH_SHORT).show();
                }





            }
        });

        mBinding.priceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==4)
                {
                    mBinding.etDiscountPercentage.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                    mBinding.etDiscountPercentage.setFocusable(true);
                    mBinding.etDiscountPercentage.setFocusableInTouchMode(true);
                    mBinding.etDiscountPercentage.setCursorVisible(true);

                }
                else {
                    mBinding.etDiscountPercentage.setInputType(InputType.TYPE_NULL);
                    mBinding.etDiscountPercentage.setFocusable(false);
                    mBinding.etDiscountPercentage.setFocusableInTouchMode(false);
                    mBinding.etDiscountPercentage.setCursorVisible(false);

                }
                if (isNetworkConnected())
                {
                    getRateList(position);

                }
                else
                {
                    Toast.makeText(requireContext(), " Please connect to internet", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getRateList(int sampleID) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String token= SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int itemID=productModel.getItem_Code();

        Call<List<RateListModel>> call = ApiClient.getInstance().getApi().getRateList(token,1,1,1,saleOrderDate,
                sampleID,supplierID,itemID);

        call.enqueue(new Callback<List<RateListModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<RateListModel>> call, @NotNull Response<List<RateListModel>> response) {
                if (response.body()!=null)
                {
                    if (response.body().size()>0)
                    {
                        List<RateListModel> list = response.body();
                        rateListModel = list.get(0);

                        setUpFields(rateListModel);
                    }
                }
                else {
                    Toast.makeText(requireContext(), ""+response.errorBody(), Toast.LENGTH_SHORT).show();


                    if (response.message().equals("Unauthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(requireActivity(),requireContext());
                    }
                }
                progressDialog.dismiss();

            }

            @Override
            public void onFailure(@NotNull Call<List<RateListModel>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void setUpFields(RateListModel model) {

        mBinding.etRate.setText(String.valueOf((long)model.getSaleRate()));
        mBinding.etDiscountPercentage.setText(String.valueOf( (long)model.getDiscPercentage()));
        mBinding.etGstPercentage.setText(String.valueOf((long)model.getsTPercnt()));

        String amountString =mBinding.etAmount.getText().toString();
        if (!amountString.equals(""))
        {
            long amount = Long.parseLong(amountString);
            long discountPercentage =  Long.parseLong(mBinding.etDiscountPercentage.getText().toString());

            long discountAmount = getPercentageAmount(amount,discountPercentage);
            mBinding.etDiscount.setText(String.valueOf(discountAmount));

            long discountedValue= amount-discountAmount;
            mBinding.etDiscountedValue.setText(String.valueOf(discountedValue));

            long gstAmount= getPercentageAmount(discountedValue,(long)model.getsTPercnt());
            mBinding.etGst.setText(String.valueOf(gstAmount));

            long netAmount = discountedValue+gstAmount;
            mBinding.etNetAmount.setText(String.valueOf(netAmount));

        }



    }

    private long getPercentageAmount(long amount, long Percentage) {

        return (amount*Percentage)/100;
    }

    public void closeKeyBoard()
    {
        View view = requireActivity().getCurrentFocus();

        if (view!=null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);

        }


    }

    public void openKeyBoard()
    {


        if (mBinding.orderQuantityLayout.requestFocus())
        {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(mBinding.orderQuantityLayout,InputMethodManager.SHOW_IMPLICIT);
        }




    }
    public boolean isNetworkConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return connected = true;
        } else {
            return connected = false;
        }

    }
}