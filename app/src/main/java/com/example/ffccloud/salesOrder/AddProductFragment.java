package com.example.ffccloud.salesOrder;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ffccloud.ProductModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.FragmentAddProductBinding;
import com.example.ffccloud.salesOrder.adapter.ProductRecyclerAdapter;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductFragment extends Fragment {
    private FragmentAddProductBinding  mBinding;
    private ProductRecyclerAdapter adapter;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddProductBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpRecyclerView();
        getData("");
        searchViewListener();
        setPullToFresh();
    }

    private void getData(String productTitle) {

         ProgressDialog progressDialog = new ProgressDialog(requireContext());
         progressDialog.setMessage("Loading...");
         progressDialog.show();


        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        Call<List<ProductModel>> call = ApiClient.getInstance().getApi().getAllProducts(token,productTitle,1,1,0,1);

        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<ProductModel>> call, @NotNull Response<List<ProductModel>> response) {
                if (response.body().size()==0)
                {
                    mBinding.tvNothingFound.setVisibility(View.VISIBLE);

                }
                else {
                    mBinding.tvNothingFound.setVisibility(View.GONE);

                }
                if(response.body()!=null)
                {
                    adapter.setProductModelList(response.body());
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
                    mBinding.tvNothingFound.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(@NotNull Call<List<ProductModel>> call, @NotNull Throwable t) {
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
        adapter= new ProductRecyclerAdapter(this);
        mBinding.productRecyclerView.setAdapter(adapter);
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