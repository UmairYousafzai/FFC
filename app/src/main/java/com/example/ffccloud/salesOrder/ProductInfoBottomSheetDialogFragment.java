package com.example.ffccloud.salesOrder;

import android.os.Bundle;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.databinding.FragmentProductInfoBottomSheetDialogBinding;
import com.example.ffccloud.utils.CONSTANTS;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class ProductInfoBottomSheetDialogFragment extends BottomSheetDialogFragment {
    private FragmentProductInfoBottomSheetDialogBinding mBinding;
    private NavController navController;
    private InsertProductModel productModel= new InsertProductModel();


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentProductInfoBottomSheetDialogBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        productModel= ProductInfoBottomSheetDialogFragmentArgs.fromBundle(getArguments()).getProductModel();


        navController = NavHostFragment.findNavController(this);

        btnListener();
        editTextListener();

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
                double rate = productModel.getRate();
                double quantity = Double.parseDouble(s.toString());
                String  amount =String.valueOf(rate*quantity) ;
                mBinding.etAmount.setText(amount);
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



                productModel.setAmount(Double.parseDouble(mBinding.etAmount.getText().toString()));
                productModel.setComments(mBinding.etComment.getText().toString());
                productModel.setDisc_Amount(Double.parseDouble(mBinding.etDiscount.getText().toString()));
                productModel.setDisc_Percentage(Double.parseDouble(mBinding.etDiscountPercentage.getText().toString()));
                productModel.setDiscounted_Value(Double.parseDouble(mBinding.etDiscountedValue.getText().toString()));
                productModel.setNet_Amount(Double.parseDouble(mBinding.etNetAmount.getText().toString()));
                productModel.setQty(Double.parseDouble(mBinding.etOrderQuantity.getText().toString()));
                productModel.setST_Amount(Double.parseDouble(mBinding.etGst.getText().toString()));
                productModel.setST_Percentage(Double.parseDouble(mBinding.etGstPercentage.getText().toString()));

                navController.getPreviousBackStackEntry().getSavedStateHandle().set(CONSTANTS.PRODUCT_MODEL,productModel);

                navController.popBackStack();


            }
        });
    }
}