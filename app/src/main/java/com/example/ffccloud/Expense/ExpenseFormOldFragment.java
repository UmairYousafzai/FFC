package com.example.ffccloud.Expense;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.ffccloud.ExpenseModelClass;
import com.example.ffccloud.Expense.utils.ExpenseViewModel;
import com.example.ffccloud.model.ExpenseType;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.FragmentExpenseFormOldBinding;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseFormOldFragment extends Fragment {

    private FragmentExpenseFormOldBinding mBinding;
    private List<ExpenseType> expenseTypeList = new ArrayList<>();
    private List<String> expenseTypesName = new ArrayList<>();
    private HashMap<String, Integer> expenseTypeIDHashMap = new HashMap<>();
    private HashMap<Integer, String> expenseTypeNameHashMap = new HashMap<>();
    private String date = "";
    private ExpenseViewModel expenseViewModel;
    private NavController navController;
    private int expenseIDPk=0;
    private ExpenseModelClass expenseModelClass = new ExpenseModelClass(), expenseForDelete = new ExpenseModelClass();


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        mBinding = FragmentExpenseFormOldBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
        navController = NavHostFragment.findNavController(this);

        setupDatePicker();
        getExpenseType();
        btnListener();

        setInitialDate();
        if (getArguments() != null) {
            expenseModelClass = ExpenseFormOldFragmentArgs.fromBundle(getArguments()).getExpense();
            setupFields();
        }

    }

    private void setupFields() {


        if (expenseModelClass != null) {

            if (expenseModelClass.getAmount() != null) {
                mBinding.etAmount.setText(expenseModelClass.getAmount());
                expenseForDelete.setAmount(expenseModelClass.getAmount());
            }

            if (expenseModelClass.getDate() != null && expenseModelClass.getDate().length() == 10) {
                int day = 0, month = 0, year = 0;
                try {
                    day = Integer.parseInt(expenseModelClass.getDate().substring(0, 2));
                    month = Integer.parseInt(expenseModelClass.getDate().substring(3, 5));
                    year = Integer.parseInt(expenseModelClass.getDate().substring(6, 10));
                    DateTime dateTime = new DateTime();
                    mBinding.datePicker.setDate(dateTime.minusDays(day));

                    date = expenseModelClass.getDate();
                    expenseForDelete.setDate(expenseModelClass.getDate());
                } catch (Exception e) {
                    Log.e("DateError Edit Expense:", e.getMessage());
                }
            }

            if (expenseModelClass.getExpenseType() != null && expenseModelClass.getExpenseTypeId() != -1) {
                expenseForDelete.setExpenseID_pk(expenseModelClass.getExpenseID_pk());
                expenseForDelete.setExpenseType(expenseModelClass.getExpenseType());
                expenseForDelete.setExpenseTypeId(expenseModelClass.getExpenseTypeId());
                mBinding.expenseTypeSpinner.setSelection(expenseTypesName.indexOf(expenseModelClass.getExpenseType()));
            }

            if (expenseModelClass.getRemarks() != null) {
                expenseForDelete.setRemarks(expenseModelClass.getRemarks());
                expenseForDelete.setUserID(expenseModelClass.getUserID());
                mBinding.etRemarks.setText(expenseModelClass.getRemarks());
            }
        }

    }

    private void setInitialDate() {


        DateTime dateTime = new DateTime();
        int month = dateTime.getMonthOfYear(), day = dateTime.getDayOfMonth();

        String monthString = "", dayString = "";

        if ((month % 10) > 0 && month < 10) {
            monthString = "0" + month;
        } else {
            monthString = String.valueOf(month);
        }

        if ((day % 10) > 0 && day < 10) {
            dayString = "0" + day;
        } else {
            dayString = String.valueOf(day);
        }
        date = monthString + "-" + dayString + "-" + dateTime.getYear();

    }

    private void btnListener() {

        mBinding.datePicker.setListener(new DatePickerListener() {
            @Override
            public void onDateSelected(DateTime dateSelected) {

                int month = dateSelected.getMonthOfYear(), day = dateSelected.getDayOfMonth();

                String monthString = "", dayString = "";

                if ((month % 10) > 0 && month < 10) {
                    monthString = "0" + month;
                } else {
                    monthString = String.valueOf(month);
                }

                if ((day % 10) > 0 && day < 10) {
                    dayString = "0" + day;
                } else {
                    dayString = String.valueOf(day);
                }
                date = monthString + "-" + dayString + "-" + dateSelected.getYear();
            }
        });

        mBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expenseModelClass == null) {
                    saveExpense();
                } else {
                    expenseViewModel.deleteExpense(expenseForDelete);
                    saveExpense();
                    navController.popBackStack();

                }


            }
        });
    }

    private void saveExpense() {

        ExpenseModelClass expenseModelClass = new ExpenseModelClass();

        if (mBinding.etAmount.getText() != null) {
            if (mBinding.etAmount.getText().toString().length() > 0) {
                expenseModelClass.setAmount(mBinding.etAmount.getText().toString());
            }
        }

        if (mBinding.etRemarks.getText() != null) {
            if (mBinding.etRemarks.getText().toString().length() > 0) {
                expenseModelClass.setRemarks(mBinding.etRemarks.getText().toString());
            }
        }

        if (mBinding.expenseTypeSpinner.getSelectedItem() != null) {
            try {
                expenseModelClass.setExpenseTypeId(expenseTypeIDHashMap.get(mBinding.expenseTypeSpinner.getSelectedItem().toString()));
            } catch (Exception e) {
                Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            expenseModelClass.setExpenseType(mBinding.expenseTypeSpinner.getSelectedItem().toString());
        }

        int userID = SharedPreferenceHelper.getInstance(requireContext()).getUserID();

        expenseModelClass.setUserID(userID);

        if (date != null) {
            expenseModelClass.setDate(date);
        }


        expenseViewModel.insertExpense(expenseModelClass);

        Toast.makeText(requireContext(), "Save SuccessFully", Toast.LENGTH_SHORT).show();

        clearFields();

    }

    private void clearFields() {

        mBinding.etRemarks.setText("");
        mBinding.etAmount.setText("");

    }


    private void setupDatePicker() {


        mBinding.datePicker
                .setOffset(30)
                .setDateSelectedColor(getResources().getColor(R.color.APP_Theme_Color))
                .setDateSelectedTextColor(Color.WHITE)
                .setMonthAndYearTextColor(getResources().getColor(R.color.APP_Theme_Color))
                .setTodayDateTextColor(getResources().getColor(R.color.white))
                .setTodayDateBackgroundColor(Color.GRAY)
                .setUnselectedDayTextColor(getResources().getColor(R.color.APP_Theme_Color))
                .setDayOfWeekTextColor(getResources().getColor(R.color.APP_Theme_Color))
                .setUnselectedDayTextColor(getResources().getColor(R.color.black))
                .showTodayButton(false)
                .init();

        mBinding.datePicker.setDate(new DateTime());
    }


    private void setupExpenseTypeSpinner() {

        if (expenseTypeList.size() > 0) {
            for (ExpenseType model : expenseTypeList) {
                expenseTypesName.add(model.getExpenseTitle());
                expenseTypeIDHashMap.put(model.getExpenseTitle(), model.getExpenseID());
                expenseTypeNameHashMap.put(model.getExpenseID(), model.getExpenseTitle());
            }
        }
        if (expenseTypesName.size() > 0) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, expenseTypesName);
            mBinding.expenseTypeSpinner.setAdapter(adapter);

        }
    }

    private void getExpenseType() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Expense Types");
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();

        Call<List<ExpenseType>> call = ApiClient.getInstance().getApi().getExpenseType(token, 1, 1, 1);

        call.enqueue(new Callback<List<ExpenseType>>() {
            @Override
            public void onResponse(@NotNull Call<List<ExpenseType>> call, @NotNull Response<List<ExpenseType>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        expenseTypeList.addAll(response.body());
                        setupExpenseTypeSpinner();
                    } else {
                        Toast.makeText(requireContext(), "Expense types not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireContext(), "" + response.message(), Toast.LENGTH_SHORT).show();
                    CustomsDialog.getInstance().loginAgain(requireActivity(), requireContext());
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<List<ExpenseType>> call, @NotNull Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}