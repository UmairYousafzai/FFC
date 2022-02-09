package com.example.ffccloud.Tracking;

import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.Tracking.Adapter.TrackingUserRecyclerAdapter;
import com.example.ffccloud.UserModel;
import com.example.ffccloud.databinding.FragmentUsersListBinding;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersListFragment extends Fragment {

    private FragmentUsersListBinding mBinding;
    private TrackingUserRecyclerAdapter adapter;
    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding= FragmentUsersListBinding.inflate(inflater,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        progressDialog= new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.getWindow().
                setBackgroundDrawable(new ColorDrawable(ResourcesCompat.getColor(getResources(), R.color.white,null)));
        progressDialog.show();

        setupRecyclerView();
        getUserList();

        searchViewTextListener();
        setPullToFresh();
    }

    private void searchViewTextListener() {

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

    private void setupRecyclerView() {

        mBinding.userListRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter= new TrackingUserRecyclerAdapter(requireContext(),this);

        mBinding.userListRecyclerView.setAdapter(adapter);
    }

    private void getUserList() {

        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int empID= SharedPreferenceHelper.getInstance(requireContext()).getEmpID();

        Call<List<UserModel>> call = ApiClient.getInstance().getApi().getUsersForTracking(token,empID);

        call.enqueue(new Callback<List<UserModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<UserModel>> call, @NotNull Response<List<UserModel>> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        List<UserModel> userModelList = response.body();

                        adapter.setUserList(userModelList);
                    }



                }
                else
                {
                    if (response.message().equals("Unauthorized"))
                    {
                        CustomsDialog.getInstance().loginAgain(requireActivity(),requireContext());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<List<UserModel>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    public void setPullToFresh() {
        mBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.swipeLayout.setRefreshing(false);
                getUserList();
            }
        });
    }
}