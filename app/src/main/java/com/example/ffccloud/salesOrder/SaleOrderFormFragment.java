package com.example.ffccloud.salesOrder;

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
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.ModelClasses.InsertSaleOrderModel;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentSaleOrderFormBinding;
import com.example.ffccloud.salesOrder.adapter.InsertProductRecyclerAdapter;
import com.example.ffccloud.utils.CONSTANTS;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;


public class SaleOrderFormFragment extends Fragment {

    private FragmentSaleOrderFormBinding mBinding;
    private NavController navController;
    private CustomerModel customerModel;
    private BottomSheetBehavior bottomSheetBehavior;
    private int numberOfLines=0;
    private List<InsertProductModel> productModelList;
    private InsertProductRecyclerAdapter adapter;


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
        for (int i=0;i<5;i++)
        {
            addLine();
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

    private void saveSaleOrder() {



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

                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_up_24);
                        mBinding.bottomSheet.headerBtn.setImageDrawable(drawable);
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
        numberOfLines++;
    }
}