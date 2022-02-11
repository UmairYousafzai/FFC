package com.example.ffccloud.expense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.ffccloud.databinding.FragmentEmployeesExpenseListBinding;
import com.example.ffccloud.expense.utils.ExpenseViewModel;
import com.example.ffccloud.model.EmployeeExpense;

import java.util.List;

public class EmployeesExpensesListFragment extends Fragment {

    private FragmentEmployeesExpenseListBinding mBinding;
    private ExpenseViewModel expenseViewModel;
    private String month="";
    private NavController navController;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      mBinding= FragmentEmployeesExpenseListBinding.inflate(inflater,container,false);

      return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = NavHostFragment.findNavController(this);
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        mBinding.setEmployeeExpenseViewModel(expenseViewModel);
        expenseViewModelObservers();
        clickListener();

    }

    private void clickListener() {
//        mBinding.monthSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mBinding.monthSpinner.getText()!=null&& mBinding.monthSpinner.getText().toString().length()>0)
//                {
//                    String month= expenseViewModel.getMonth(mBinding.monthSpinner.getText().toString());
//                    expenseViewModel.fetchEmployeeExpenses(month);
//                }
//            }
//        });

    }

    private void expenseViewModelObservers() {



        expenseViewModel.getEmployeeExpenseList().observe(getViewLifecycleOwner(), new Observer<List<EmployeeExpense>>() {
            @Override
            public void onChanged(List<EmployeeExpense> employeeExpenses) {

                if (employeeExpenses!=null&&employeeExpenses.size()>0)
                {
                    expenseViewModel.setEmployeeExpenseAtAdapter(employeeExpenses);
                }
            }
        });

        expenseViewModel.getEmployeeExpenseOnClick().observe(getViewLifecycleOwner(), new Observer<EmployeeExpense>() {
            @Override
            public void onChanged(EmployeeExpense employeeExpense) {
                if (employeeExpense!=null)
                {
                    if (!month.isEmpty()&& employeeExpense.getUserId()>0)
                    {
                        EmployeesExpensesListFragmentDirections.
                                ActionEmployeeExpenseFragmentToEmployeeExpenseDetailFragment action=EmployeesExpensesListFragmentDirections.
                                actionEmployeeExpenseFragmentToEmployeeExpenseDetailFragment();
                        action.setMonth(month);
                        action.setUserId((int) employeeExpense.getUserId());
                        month="";
                        navController.navigate(action);
                    }

                }
            }
        });

        expenseViewModel.getServerError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                    expenseViewModel.clearAdapterList();
                    Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
                }
            }
        });

        expenseViewModel.getSelectedMonthLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                     month= expenseViewModel.getMonth(s);
                    expenseViewModel.fetchEmployeeExpenses(month);
                }
            }
        });
    }
}