package com.example.ffccloud.salesOrder;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ProductInfoBottomSheetDialogFragment extends BottomSheetDialogFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_info_bottom_sheet_dialog, container, false);
    }
}