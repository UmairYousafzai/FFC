package com.example.ffccloud.expense;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ffccloud.databinding.FragmentExpenseListBinding;
import com.example.ffccloud.expense.adapter.ExpenseListAdapter;
import com.example.ffccloud.expense.utils.ExpenseViewModel;
import com.example.ffccloud.ExpenseModelClass;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ExpenseListFragment extends Fragment {

    private FragmentExpenseListBinding mBinding;
    private NavController navController;
    private ExpenseViewModel expenseViewModel;
    private ExpenseListAdapter adapter;
    private List<ExpenseModelClass> expenseModelClassList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();


    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding= FragmentExpenseListBinding.inflate(inflater,container,false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);


        setupRecyclerView();
    }


    @Override
    public void onResume() {
        super.onResume();

        getExpenseLiveData();
        btnListener();
    }

    private void getExpenseLiveData() {

        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Expenses");
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        expenseViewModel.getAllExpense().observe(getViewLifecycleOwner(), new Observer<List<ExpenseModelClass>>() {
            @Override
            public void onChanged(List<ExpenseModelClass> expens) {
                expenseModelClassList.clear();
                if (expens !=null&& expens.size()>0)
                {
                    expenseModelClassList.addAll(expens);
                    mBinding.tvNothingFound.setVisibility(View.GONE);
                    mBinding.expenseListRecycler.setVisibility(View.VISIBLE);
                }
                else
                {
                    mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                    mBinding.expenseListRecycler.setVisibility(View.GONE);
                }
                adapter.setExpenseModelClassList(expenseModelClassList);

                progressDialog.dismiss();
            }
        });
    }


    private void setupRecyclerView() {

        mBinding.expenseListRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter= new ExpenseListAdapter();
        mBinding.expenseListRecycler.setAdapter(adapter);

    }

    private void btnListener() {

        mBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NavDirections navDirections = ExpenseListFragmentDirections.actionExpenseListFragmentToExpenseFormFragment(null);
                navController.navigate(navDirections);

            }
        });
        adapter.setOnClickListener(new ExpenseListAdapter.SetOnClickListener() {
            @Override
            public void setOnClickListener(ExpenseModelClass expenseModelClass, int key) {


                if (expenseModelClass !=null)
                {
                    if (key==1)
                    {
                        expenseViewModel.deleteExpense(expenseModelClass);
                        expenseModelClassList.remove(expenseModelClass);
                    }
                    else if (key==2)
                    {
                        ExpenseListFragmentDirections.ActionExpenseListFragmentToExpenseFormFragment action =
                                ExpenseListFragmentDirections.actionExpenseListFragmentToExpenseFormFragment(expenseModelClass);

                        navController.navigate(action);
                    }

                }

            }
        });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.upload).setVisible(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()== com.example.ffccloud.R.id.upload)
        {
            uploadExpenses();
        }

        return true;
    }

    private void uploadExpenses() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setTitle("Expenses");
        progressDialog.setMessage("Uploading....");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertExpenses(token,expenseModelClassList);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        UpdateStatus updateStatus = response.body();
                        Toast.makeText(requireContext(), ""+updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                        if (updateStatus.getStatus()==1)
                        {
                            expenseViewModel.deleteAllExpense();
                            mBinding.tvNothingFound.setVisibility(View.VISIBLE);
                            mBinding.expenseListRecycler.setVisibility(View.GONE);
                        }

                    }
                }
                else
                {
                    Toast.makeText(requireContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {

                Toast.makeText(requireContext(), ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }
}