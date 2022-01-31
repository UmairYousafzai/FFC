package com.example.ffccloud.salesOrder;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.model.RateListModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.FragmentProductInfoBottomSheetDialogBinding;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductInfoBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private FragmentProductInfoBottomSheetDialogBinding mBinding;
    private NavController navController;
    private InsertProductModel productModel= new InsertProductModel();
    private List<String> priceTypeList=new ArrayList<>();
    private String saleOrderDate;
    private int supplierID;
    private RateListModel rateListModel = new RateListModel();
    private boolean flag= true, flag2= true;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentProductInfoBottomSheetDialogBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        saleOrderDate = ProductInfoBottomSheetDialogFragmentArgs.fromBundle(getArguments()).getDate();
        supplierID = ProductInfoBottomSheetDialogFragmentArgs.fromBundle(getArguments()).getSupplierId();
        productModel= ProductInfoBottomSheetDialogFragmentArgs.fromBundle(getArguments()).getProductModel();



    }

    @Override
    public void onResume() {
        super.onResume();



        navController = NavHostFragment.findNavController(this);

        btnListener();
        editTextListener();
        setUpSpinner();


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
                        long discountPercentage =Long.parseLong(mBinding.etDiscountPercentage.getText().toString());
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
                        Toast.makeText(requireContext(), ""+exception.getMessage(), Toast.LENGTH_SHORT).show();
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


                String amoutString = mBinding.etAmount.getText().toString();
                if (!amoutString.equals(""))
                {
                    if (!s.toString().isEmpty())
                    {
                        long amount = Long.parseLong(amoutString);
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



                try {
                    productModel.setAmount(Long.parseLong(Objects.requireNonNull(mBinding.etAmount.getText()).toString()));
                    productModel.setComments(mBinding.etComment.getText().toString());
                    productModel.setDisc_Amount(Long.parseLong(Objects.requireNonNull(mBinding.etDiscount.getText()).toString()));
                    productModel.setDisc_Percentage(Long.parseLong(Objects.requireNonNull(mBinding.etDiscountPercentage.getText()).toString()));
                    productModel.setDiscounted_Value(Long.parseLong(Objects.requireNonNull(mBinding.etDiscountedValue.getText()).toString()));
                    productModel.setNet_Amount(Long.parseLong(Objects.requireNonNull(mBinding.etNetAmount.getText()).toString()));
                    productModel.setQty(Long.parseLong(Objects.requireNonNull(mBinding.etOrderQuantity.getText()).toString()));
                    productModel.setST_Amount(Long.parseLong(Objects.requireNonNull(mBinding.etGst.getText()).toString()));
                    productModel.setST_Percentage(Long.parseLong(Objects.requireNonNull(mBinding.etGstPercentage.getText()).toString()));
                    productModel.setItem_Code(productModel.getItem_Code());
                    productModel.setRate(Double.parseDouble(Objects.requireNonNull(mBinding.etRate.getText()).toString()));

                    Objects.requireNonNull(navController.getPreviousBackStackEntry()).getSavedStateHandle().set(CONSTANTS.PRODUCT_MODEL,productModel);


                    navController.popBackStack();
                }
                catch (Exception e)
                {
                    Toast.makeText(requireContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }



            }
        });

        mBinding.priceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position==4)
                {
                    mBinding.etDiscountPercentage.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
//                    mBinding.etDiscountPercentage.setFocusable(true);
//                    mBinding.etDiscountPercentage.requestFocus();
                }
                else {
                    mBinding.etDiscountPercentage.setInputType(InputType.TYPE_NULL);
//                    mBinding.etDiscountPercentage.setFocusable(false);

                }
                getRateList(position);
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
                    progressDialog.dismiss();

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
}