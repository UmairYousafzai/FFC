package com.example.ffccloud.Customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.Customer.Adapter.ContactRecyclerAdapter;
import com.example.ffccloud.CustomerModel;
import com.example.ffccloud.ModelClasses.RegionModel;
import com.example.ffccloud.ModelClasses.UpdateStatus;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.AddContactDialogBinding;
import com.example.ffccloud.databinding.FragmentAddCustomerBinding;
import com.example.ffccloud.utils.SharedPreferenceHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerFragment extends Fragment {

    private FragmentAddCustomerBinding mBinding;
    private final ArrayList<String> cityList = new ArrayList<>();
    private final HashMap<String, Integer> cityHashMap = new HashMap<>();
    private String creditLimitPromptType;
    private boolean applyCredit = false,isCompany= false;
    private List<ContactPersons> contactPersonsList = new ArrayList<>();
    private ContactRecyclerAdapter adapter;
    private CustomerModel customerModel;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddCustomerBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();

        btnListener();
        getCity();
        setupRecyclerView();

        customerModel = AddCustomerFragmentArgs.fromBundle(getArguments()).getCustomer();

        if (customerModel!=null)
        {
            setUpFields();
        }


    }

    private void setUpFields() {

        mBinding.etPartName.setText(customerModel.getPartyName());
        mBinding.etPartAbbreviation.setText(customerModel.getPartyAbbreviation());
        mBinding.etFocalPersonName.setText(customerModel.getFocal_Person());
        mBinding.etFocalPersonCNIC.setText(customerModel.getCNICNo());
        if (customerModel.isIs_Company())
        {
            mBinding.companyCheckBox.setChecked(true);
        }
        mBinding.etSaleTax.setText(customerModel.getSales_Tax_No());
        mBinding.etNtnNum.setText(customerModel.getNTN());
        mBinding.etCreditDays.setText(String.valueOf(customerModel.getCr_Limit()) );
        mBinding.etCreditLimit.setText(String.valueOf(customerModel.getCr_Limit_Amount()));

        if (customerModel.getPrompt_Type().equals("1"))
        {
            mBinding.creditLimitNaRadio.setChecked(true);
        }
        else if (customerModel.getPrompt_Type().equals("2"))
        {
            mBinding.creditLimitWarningRadio.setChecked(true);

        }
        else if (customerModel.getPrompt_Type().equals("3"))
        {
            mBinding.creditLimitCriticalRadio.setChecked(true);

        }

        if (customerModel.isApply_Cr_Limit())
        {
            mBinding.applyCreditLimitCheckBox.setChecked(true);
        }

        mBinding.etEmail.setText(customerModel.getEmail());
        mBinding.etEmailCc.setText(customerModel.getEMail_CC());
        mBinding.etBccEmail.setText(customerModel.getEMail_BCC());
        mBinding.etContact.setText(customerModel.getContacts());
        mBinding.etFax.setText(customerModel.getFax_No());
        mBinding.etAddress.setText(customerModel.getAddress());
        mBinding.etPayee.setText(customerModel.getPayee());
        mBinding.etInstruction.setText(customerModel.getInstruction());
        mBinding.etComment.setText(customerModel.getComments());

        adapter.setContactPersonsList(customerModel.getContact_PersonsList());

    }

    private void setupRecyclerView() {

        mBinding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ContactRecyclerAdapter();
        mBinding.contactRecyclerView.setAdapter(adapter);

    }

    private void btnListener() {

        mBinding.addBtn.setOnClickListener(v -> showDialog());
        mBinding.saveBtn.setOnClickListener(v -> insertCustomer());

        mBinding.applyCreditLimitCheckBox.setOnClickListener(v -> applyCredit = mBinding.applyCreditLimitCheckBox.isChecked());
        mBinding.companyCheckBox.setOnClickListener(v -> isCompany = mBinding.companyCheckBox.isChecked());
        mBinding.creditLimitRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton radioButton = mBinding.getRoot().findViewById(checkedId);
            if (radioButton.getText().toString().equals("N/A")) {
                creditLimitPromptType = "1";
                Toast.makeText(requireContext(), "Credit Limits Prompt Type " + radioButton.getText().toString(), Toast.LENGTH_SHORT).show();
            } else if (radioButton.getText().toString().equals("Warning")) {
                creditLimitPromptType = "2";

                Toast.makeText(requireContext(), "Credit Limits Prompt Type " + radioButton.getText().toString(), Toast.LENGTH_SHORT).show();

            } else if (radioButton.getText().toString().equals("Critical")) {
                creditLimitPromptType = "3";

                Toast.makeText(requireContext(), "Credit Limits Prompt Type " + radioButton.getText().toString(), Toast.LENGTH_SHORT).show();

            }

        });
    }

    private void insertCustomer() {

        int userID = SharedPreferenceHelper.getInstance(requireContext()).getUserID();
        String partyName = Objects.requireNonNull(mBinding.etPartName.getText()).toString().trim();
        double crLimit = Double.parseDouble(Objects.requireNonNull(mBinding.etCreditDays.getText()).toString());
        double crLimitAmount = Double.parseDouble(Objects.requireNonNull(mBinding.etCreditLimit.getText()).toString());
        int cityId = cityHashMap.get(mBinding.citySpinner.getSelectedItem().toString());
        String email = Objects.requireNonNull(mBinding.etEmail.getText()).toString().trim();
        String emailCC = Objects.requireNonNull(mBinding.etEmailCc.getText()).toString().trim();
        String emailBCC = Objects.requireNonNull(mBinding.etBccEmail.getText()).toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



        if (cityId > 0) {

            if (email.matches(emailPattern) )
            {
                if (emailBCC.matches(emailPattern))
                {
                    if (emailCC.matches(emailPattern))
                    {
                        CustomerModel customerModel = new CustomerModel();

                        customerModel.setCompany_Id(1);
                        customerModel.setLocation_Id(1);
                        customerModel.setProject_Id(1);
                        customerModel.setCountry_Id(1);
                        customerModel.setSupplier_Id(0);
                        customerModel.setUserID(userID);
                        customerModel.setSalesManID(0);
                        customerModel.setUserTypeName("C");
                        customerModel.setPartyName(partyName);
                        customerModel.setCr_Limit(crLimit);
                        customerModel.setCr_Limit_Amount(crLimitAmount);
                        customerModel.setApply_Cr_Limit(applyCredit);
                        customerModel.setEmail(email);
                        customerModel.setEMail_BCC(emailBCC);
                        customerModel.setEMail_CC(emailCC);
                        customerModel.setCity_Id(cityId);
                        customerModel.setIs_Company(isCompany);
                        customerModel.setPrompt_Type(creditLimitPromptType);
                        customerModel.setContact_PersonsList(contactPersonsList);
                        customerModel.setComments(Objects.requireNonNull(mBinding.etComment.getText()).toString());
                        customerModel.setInstruction(Objects.requireNonNull(mBinding.etInstruction.getText()).toString());
                        customerModel.setAddress(Objects.requireNonNull(mBinding.etAddress.getText()).toString());
                        customerModel.setPayee(Objects.requireNonNull(mBinding.etPayee.getText()).toString());
                        customerModel.setFax_No(Objects.requireNonNull(mBinding.etFax.getText()).toString());
                        customerModel.setContacts( Objects.requireNonNull(mBinding.etContact.getText()).toString());
                        customerModel.setNTN(Objects.requireNonNull(mBinding.etNtnNum.getText()).toString());
                        customerModel.setSales_Tax_No(Objects.requireNonNull(mBinding.etSaleTax.getText()).toString());
                        customerModel.setCNICNo(Objects.requireNonNull(mBinding.etFocalPersonCNIC.getText()).toString());
                        customerModel.setFocal_Person( Objects.requireNonNull(mBinding.etFocalPersonName.getText()).toString());
                        customerModel.setPartyAbbreviation( Objects.requireNonNull(mBinding.etPartAbbreviation.getText()).toString());


                        apiCallforsavingCustomer(customerModel);
                    }
                    else
                    {
                        mBinding.etEmailCcLayout.setError("Enter Valid Email Format");

                    }

                }
                else {
                    mBinding.etEmailBccLayout.setError("Enter Valid Email Format");
                }

            }
            else {
                mBinding.etEmailLayout.setError("Enter Valid Email Format");
            }

        }


    }

    private void apiCallforsavingCustomer(CustomerModel customerModel) {
        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertCustomer(customerModel);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {
                if (response!=null)
                {
                    UpdateStatus updateStatus = response.body();
                    Toast.makeText(requireContext(), updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(requireContext(), " " +response.errorBody(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " on Failure" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showDialog() {

        AddContactDialogBinding binding = AddContactDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(binding.getRoot()).setCancelable(false).create();
        alertDialog.show();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.etName.getText().toString();
                String phone = binding.etPhone.getText().toString();

                if (name!=null)
                {
                    if (phone!=null)
                    {
                        ContactPersons contactPersons = new ContactPersons();
                        contactPersons.setContact_Person_ContactNo(phone);
                        contactPersons.setContact_Person_Design(binding.etDesignation.getText().toString());
                        contactPersons.setContact_Person_Email(binding.etEmail.getText().toString());
                        contactPersons.setContact_Person_Name(name);
                        contactPersonsList.add(contactPersons);
                        adapter.setContactPersonsList(contactPersonsList);
                    }
                    else
                    {
                        binding.etPhoneLayout.setError("Enter Phone Number");
                    }

                }
                else
                {
                    binding.etNameLayout.setError("Enter name");
                }



                alertDialog.dismiss();
            }
        });
    }

    public void getCity() {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
        Call<List<RegionModel>> call = ApiClient.getInstance().getApi().getRegion(token, 1, 1, 1, id);

        call.enqueue(new Callback<List<RegionModel>>() {
            @Override
            public void onResponse(@NotNull Call<List<RegionModel>> call, @NotNull Response<List<RegionModel>> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    cityHashMap.clear();
                    cityList.clear();
                    List<RegionModel> regionModelList = new ArrayList<>();
                    regionModelList = response.body();
                    for (RegionModel model : regionModelList) {
                        cityList.add(model.getName());
                        cityHashMap.put(model.getName(), model.getRegionId());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cityList);
                        mBinding.citySpinner.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(requireContext(), " " + response.errorBody(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RegionModel>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }
}