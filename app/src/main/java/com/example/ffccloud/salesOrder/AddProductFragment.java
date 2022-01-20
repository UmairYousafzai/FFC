package com.example.ffccloud.salesOrder;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.GetProductModel;
import com.example.ffccloud.InsertProductModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentAddProductBinding;
import com.example.ffccloud.salesOrder.adapter.GetProductRecyclerAdapter;
import com.example.ffccloud.utils.CONSTANTS;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductFragment extends Fragment {
    private FragmentAddProductBinding  mBinding;
    private GetProductRecyclerAdapter adapter;
    private NavController navController;
    private List<InsertProductModel> productModelList=new ArrayList<>();
    private int key =0,supplierId=0;
    private String date= "";


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddProductBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //key is to check from which location we have requested this fragment

        assert getArguments() != null;
        key = AddProductFragmentArgs.fromBundle(getArguments()).getKey();
        supplierId = AddProductFragmentArgs.fromBundle(getArguments()).getSupplierId();
        date = AddProductFragmentArgs.fromBundle(getArguments()).getDate();

    }

    @Override
    public void onResume() {
        super.onResume();



        navController = NavHostFragment.findNavController(this);
        NavBackStackEntry navBackStackEntry = navController.getBackStackEntry(R.id.addProductFragment  );

        // Create our observer and add it to the NavBackStackEntry's lifecycle
        final LifecycleEventObserver observer = (source, event) -> {
            if (event.equals(Lifecycle.Event.ON_RESUME) && navBackStackEntry.getSavedStateHandle().contains(CONSTANTS.PRODUCT_MODEL)) {
                InsertProductModel productModel = navBackStackEntry.getSavedStateHandle().get(CONSTANTS.PRODUCT_MODEL);
                productModelList.add(productModel);
                Toast.makeText(requireContext(), "Number Of Product Added :"+productModelList.size(), Toast.LENGTH_SHORT).show();

                navController.getPreviousBackStackEntry().getSavedStateHandle().set(CONSTANTS.PRODUCT_MODEL, productModelList);

            }
        };
        navBackStackEntry.getLifecycle().addObserver(observer);

        // As addObserver() does not automatically remove the observer, we
        // call removeObserver() manually when the view lifecycle is destroyed
        getViewLifecycleOwner().getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                    navBackStackEntry.getLifecycle().removeObserver(observer);

                }
            }
        });


        setUpRecyclerView();
        getData("");
        searchViewListener();
        setPullToFresh();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void getData(String productTitle) {

        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        progressDialog.show();


        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        Call<List<GetProductModel>> call = ApiClient.getInstance().getApi().getAllProducts(token,productTitle,1,1,0,1);

        call.enqueue(new Callback<List<GetProductModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<GetProductModel>> call, @NotNull Response<List<GetProductModel>> response) {


                if(response.isSuccessful())
                {
                    if(response.body()!=null)
                    {
                        if (response.body().size()==0)
                        {
                            mBinding.tvNothingFound.setVisibility(View.VISIBLE);

                        }
                        else {
                            mBinding.tvNothingFound.setVisibility(View.GONE);
                            adapter.setProductModelList(response.body());
                        }
                        progressDialog.dismiss();

                    }
                    else
                    {
                        progressDialog.dismiss();
                        mBinding.tvNothingFound.setVisibility(View.VISIBLE);

                    }
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(requireContext(), ""+response.message(), Toast.LENGTH_SHORT).show();

                    if (response.message().equals("Unauthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(requireActivity(),requireContext());
                    }
                }


            }

            @Override
            public void onFailure(@NotNull Call<List<GetProductModel>> call, @NotNull Throwable t) {
                progressDialog.dismiss();
                mBinding.tvNothingFound.setVisibility(View.VISIBLE);
            }
        });
    }
    private void searchViewListener() {
        mBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    private void setUpRecyclerView() {

        mBinding.productRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter= new GetProductRecyclerAdapter(this);
        mBinding.productRecyclerView.setAdapter(adapter);
        adapter.setKey(key);
        adapter.setDate(date);
        adapter.setSupplierID(supplierId);
    }

    public void setPullToFresh() {
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeLayout.setRefreshing(false);
                getData("");
            }
        });
    }
}