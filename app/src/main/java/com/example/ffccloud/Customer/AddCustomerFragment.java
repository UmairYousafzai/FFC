package com.example.ffccloud.Customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ffccloud.ContactPersons;
import com.example.ffccloud.Customer.Adapter.ContactRecyclerAdapter;
import com.example.ffccloud.CustomerModel;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.AddContactDialogBinding;
import com.example.ffccloud.databinding.FragmentAddCustomerBinding;
import com.example.ffccloud.model.GetCustomerResponse;
import com.example.ffccloud.model.RegionModel;
import com.example.ffccloud.model.UpdateStatus;
import com.example.ffccloud.utils.CustomLocation;
import com.example.ffccloud.utils.CustomsDialog;
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
    private final HashMap<String, Integer> cityHashMapForID = new HashMap<>();
    private final HashMap<Integer, String> cityHashMapForName = new HashMap<>();
    private String creditLimitPromptType = "";
    private boolean applyCredit = false, isCompany = false, isFromOnPause = false,isLocationUpdate=false,isEmailCC=false,isEmailBcc=false;
    private List<ContactPersons> contactPersonsList = new ArrayList<>();
    private ContactRecyclerAdapter adapter;
    private CustomerModel customerModel;
    private String customerCode=" ";
    private int customerID = 0, userID, cityId;
    private String partyName = "", email = "", emailCC = "", emailBCC = "", comment = "", instruction = "", address = "", payee = "", fax = "", contact = "", NTN = "", saleTax = "", focalPerson = "", focalPeronCNIC = "", partyAbbreviation = "";
    private double crLimitAmount = 0.0, crLimitDays = 0.0;
    private GetCustomerResponse getCustomerResponse= new GetCustomerResponse();
    private String locationAddress="",locationString="";

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentAddCustomerBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNetworkConnected())
        {
            getCity();
        }
        else
        {
            Toast.makeText(requireContext(), " City Loading Failed \nInternet not connected\n please connect to internet", Toast.LENGTH_SHORT).show();
        }

        setupRecyclerView();


        if (getArguments() != null) {
            customerCode = AddCustomerFragmentArgs.fromBundle(getArguments()).getCustomerCode();
            if (customerCode!=null)
            {
                getCustomerByCode();
                isLocationUpdate= true;
            }

        }

        if (customerCode != null && isFromOnPause) {
            setUpFields();
        } else {
            initializeViews();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        btnListener();



    }


    @Override
    public void onPause() {
        super.onPause();
        insertCustomer(2);
        isFromOnPause = true;
        closeKeyBoard();
    }

    public void initializeViews() {

        mBinding.etPartName.setText(partyName);
        mBinding.etPartAbbreviation.setText(partyAbbreviation);
        mBinding.etFocalPersonName.setText(focalPerson);
        mBinding.etFocalPersonCNIC.setText(focalPeronCNIC);
        mBinding.companyCheckBox.setChecked(isCompany);
        mBinding.etSaleTax.setText(saleTax);
        mBinding.etNtnNum.setText(NTN);
        mBinding.etCreditDays.setText(String.valueOf(crLimitDays));
        mBinding.etCreditLimit.setText(String.valueOf(crLimitAmount));

        if (creditLimitPromptType.equals("1")) {
            mBinding.creditLimitNaRadio.setChecked(true);
        } else if (creditLimitPromptType.equals("2")) {
            mBinding.creditLimitWarningRadio.setChecked(true);

        } else if (creditLimitPromptType.equals("3")) {
            mBinding.creditLimitCriticalRadio.setChecked(true);

        }


        mBinding.applyCreditLimitCheckBox.setChecked(applyCredit);


        mBinding.etEmail.setText(email);
        mBinding.etEmailCc.setText(emailCC);
        mBinding.etBccEmail.setText(emailBCC);
        mBinding.etContact.setText(contact);
        mBinding.etFax.setText(fax);
        mBinding.etAddress.setText(address);
        mBinding.etPayee.setText(payee);
        mBinding.etInstruction.setText(instruction);
        mBinding.etComment.setText(comment);

        if (contactPersonsList!=null)
        {
            adapter.setContactPersonsList(contactPersonsList);

        }
    }


    private void setupRecyclerView() {

        mBinding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new ContactRecyclerAdapter();
        mBinding.contactRecyclerView.setAdapter(adapter);
    }

    private void btnListener() {

        mBinding.addBtn.setOnClickListener(v -> showDialog());
        mBinding.saveBtn.setOnClickListener(v -> {
            closeKeyBoard();
            if (isNetworkConnected())
            {
                insertCustomer(1);

            }
            else
            {
                Toast.makeText(requireContext(), "Save Failed\n Internet not connected\n please connect to internet", Toast.LENGTH_SHORT).show();
            }

        });

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

        mBinding.locationCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomLocation customLocation = new CustomLocation(requireContext());

                if (customLocation.isLocationEnabled()) {
                    CustomLocation.CustomLocationResults results = new CustomLocation.CustomLocationResults() {
                        @Override
                        public void gotLocation(Location location) {


                            if (mBinding.locationCheckbox.isChecked()) {
                                locationAddress = customLocation.getCompleteAddressString(location.getLatitude(), location.getLongitude());
                                mBinding.locationCheckbox.setText(locationAddress);
                                locationString = location.getLatitude() + "," +location.getLongitude() ;
                            } else {

                                mBinding.locationCheckbox.setText("Enable Location");
                                mBinding.locationCheckbox.setChecked(false);
                                locationString="";
                                locationAddress="";
                            }


                        }
                    };
                    customLocation.getLastLocation(results);
                } else {
                    CustomsDialog.getInstance().showOpenLocationSettingDialog(requireActivity(),requireContext());

                }
            }
        });

        adapter.SetOnClickListener(new ContactRecyclerAdapter.SetOnClickListener() {
            @Override
            public void onClick(ContactPersons contactPersons) {

                if (contactPersonsList!=null&& contactPersonsList.size()>0)
                {
                    contactPersonsList.remove(contactPersons);
                }
            }
        });

    }

    private void insertCustomer(int key) {


        if (key == 1) {
            try {



                userID = SharedPreferenceHelper.getInstance(requireContext()).getUserID();
                partyName = Objects.requireNonNull(mBinding.etPartName.getText()).toString().trim();
                crLimitDays = mBinding.etCreditDays.getText() != null ? Double.parseDouble(mBinding.etCreditDays.getText().toString()) : 0.0;
                crLimitAmount = mBinding.etCreditLimit.getText() != null ? Double.parseDouble((mBinding.etCreditLimit.getText()).toString()) : 0.0;
                cityId = cityHashMapForID.get(mBinding.citySpinner.getSelectedItem().toString());
                email = Objects.requireNonNull(mBinding.etEmail.getText()).toString().trim();
                emailCC = Objects.requireNonNull(mBinding.etEmailCc.getText()).toString().trim();
                emailBCC = Objects.requireNonNull(mBinding.etBccEmail.getText()).toString().trim();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                comment = mBinding.etComment.getText() != null ? mBinding.etComment.getText().toString() : "";
                instruction = mBinding.etInstruction.getText() != null ? mBinding.etInstruction.getText().toString() : "";
                address = mBinding.etAddress.getText() != null ? mBinding.etAddress.getText().toString() : "";
                payee = mBinding.etPayee.getText() != null ? mBinding.etPayee.getText().toString() : "";
                fax = mBinding.etFax.getText() != null ? mBinding.etFax.getText().toString() : "";
                contact = mBinding.etContact.getText() != null ? mBinding.etContact.getText().toString() : "";
                NTN = mBinding.etNtnNum.getText() != null ? mBinding.etNtnNum.getText().toString() : "";
                saleTax = mBinding.etSaleTax.getText() != null ? mBinding.etSaleTax.getText().toString() : "";
                focalPeronCNIC = mBinding.etFocalPersonCNIC.getText() != null ? mBinding.etFocalPersonCNIC.getText().toString() : "";
                focalPerson = mBinding.etFocalPersonName.getText() != null ? mBinding.etFocalPersonName.getText().toString() : "";
                partyAbbreviation = mBinding.etPartAbbreviation.getText() != null ? mBinding.etPartAbbreviation.getText().toString() : "";
                if (cityId > 0) {

                    if (email.matches(emailPattern)) {
                        mBinding.etEmailLayout.setError(null);

                                if (NTN.length()==13)
                                {
                                    mBinding.etNtnNumLayout.setError(null);
                                    CustomerModel customerModel = new CustomerModel();

                                    customerModel.setIs_update_Loc_Cord(isLocationUpdate);
                                    customerModel.setLoc_Cord_Address(locationAddress);
                                    customerModel.setLoc_Cord(locationString);
                                    customerModel.setCompany_Id(1);
                                    customerModel.setLocation_Id(1);
                                    customerModel.setProject_Id(1);
                                    customerModel.setCountry_Id(1);
                                    customerModel.setSupplier_Id(customerID);
                                    customerModel.setUserID(userID);
                                    customerModel.setSalesManID(0);
                                    customerModel.setUserTypeName("C");
                                    customerModel.setPartyName(partyName);
                                    customerModel.setCr_Limit(crLimitDays);
                                    customerModel.setCr_Limit_Amount(crLimitAmount);
                                    customerModel.setApply_Cr_Limit(applyCredit);
                                    customerModel.setEmail(email);
                                    customerModel.setEMail_BCC(emailBCC);
                                    customerModel.setEMail_CC(emailCC);
                                    customerModel.setCity_Id(cityId);
                                    customerModel.setIs_Company(isCompany);
                                    customerModel.setPrompt_Type(creditLimitPromptType);
                                    customerModel.setContact_PersonsList(contactPersonsList);
                                    customerModel.setComments(comment);
                                    customerModel.setInstruction(instruction);
                                    customerModel.setAddress(address);
                                    customerModel.setPayee(payee);
                                    customerModel.setFax_No(fax);
                                    customerModel.setContacts(contact);
                                    customerModel.setNTN(NTN);
                                    customerModel.setSales_Tax_No(saleTax);
                                    customerModel.setCNICNo(focalPeronCNIC);
                                    customerModel.setFocal_Person(focalPerson);
                                    customerModel.setPartyAbbreviation(partyAbbreviation);
                                    customerModel.setUser_Sub_Type("Customer");


                                    apiCallForSavingCustomer(customerModel);
                                }
                                else
                                {
                                    mBinding.etNtnNumLayout.setError("Please enter valid NTN ");
                                    mBinding.etNtnNumLayout.requestFocus();
                                }





                    } else {
                        mBinding.etEmailLayout.setError("Enter Valid Email Format");
                        mBinding.etEmailLayout.requestFocus();
                    }

                }

            } catch (Exception e) {
                Toast.makeText(requireContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }


    }

    private void getCustomerByCode() {


        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Call<GetCustomerResponse> call = ApiClient.getInstance().getApi().getCustomerByCode(1,1,customerCode);

        call.enqueue(new Callback<GetCustomerResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetCustomerResponse> call, @NotNull Response<GetCustomerResponse> response) {

                if (response.isSuccessful())
                {
                    if (response.body()!=null)
                    {
                        getCustomerResponse= response.body();
                        setUpFields();

                    }
                }
                else
                {
                    Toast.makeText(requireContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NotNull Call<GetCustomerResponse> call, @NotNull Throwable t) {

            }
        });

    }
    private void setUpFields( ) {
        if (getCustomerResponse!=null)
        {
            if (getCustomerResponse.getCustomerModelList()!=null)
            {
                if (getCustomerResponse.getCustomerModelList().size()>0)
                {
                    CustomerModel customerModel= new CustomerModel();

                    customerModel = getCustomerResponse.getCustomerModelList().get(0);

                    if (customerModel.getLoc_Cord_Address()!=null)
                    {
                        if (customerModel.getLoc_Cord_Address().length()>0)
                        {
                            mBinding.locationCheckbox.setChecked(true);
                            mBinding.locationCheckbox.setText(customerModel.getLoc_Cord_Address());
                            locationAddress=customerModel.getLoc_Cord_Address();
                            locationString= customerModel.getLoc_Cord();
                        }


                    }

                    mBinding.citySpinner.setSelection(cityList.indexOf(cityHashMapForName.get(customerModel.getCity_Id())));


                    isCompany = customerModel.isIs_Company();
                    applyCredit = customerModel.isApply_Cr_Limit();

                    customerID = customerModel.getSupplier_Id();

                    if (customerModel.getPartyName()!=null)
                    {
                        mBinding.etPartName.setText(customerModel.getPartyName());
                        partyName = customerModel.getPartyName();
                    }
                    if (customerModel.getPartyAbbreviation()!=null)
                    {
                        mBinding.etPartAbbreviation.setText(customerModel.getPartyAbbreviation());
                        partyAbbreviation = customerModel.getPartyAbbreviation();
                    }

                    if (customerModel.getFocal_Person()!=null)
                    {

                        mBinding.etFocalPersonName.setText(customerModel.getFocal_Person());
                        focalPerson = customerModel.getFocal_Person();
                    }

                    if (customerModel.getCNICNo()!=null)
                    {
                        mBinding.etFocalPersonCNIC.setText(customerModel.getCNICNo());
                        focalPeronCNIC = customerModel.getCNICNo();
                    }

                    if (customerModel.getSales_Tax_No()!=null)
                    {
                        mBinding.etSaleTax.setText(customerModel.getSales_Tax_No());
                        saleTax = customerModel.getSales_Tax_No();
                    }

                    if (customerModel.getNTN()!=null)
                    {
                        mBinding.etNtnNum.setText(customerModel.getNTN());
                        NTN = customerModel.getNTN() ;
                    }

                    mBinding.companyCheckBox.setChecked(customerModel.isIs_Company());



                    crLimitDays = (int) customerModel.getCr_Limit();
                    mBinding.etCreditDays.setText(String.valueOf(customerModel.getCr_Limit()));

                    mBinding.etCreditLimit.setText(String.valueOf(customerModel.getCr_Limit_Amount()));
                    crLimitAmount = customerModel.getCr_Limit_Amount();

                    switch (customerModel.getPrompt_Type()) {
                        case "1":
                            mBinding.creditLimitNaRadio.setChecked(true);
                            creditLimitPromptType = "1";
                            break;
                        case "2":
                            mBinding.creditLimitWarningRadio.setChecked(true);
                            creditLimitPromptType = "2";


                            break;
                        case "3":
                            mBinding.creditLimitCriticalRadio.setChecked(true);
                            creditLimitPromptType = "2";


                            break;
                    }


                    mBinding.applyCreditLimitCheckBox.setChecked(customerModel.isApply_Cr_Limit());


                    if (customerModel.getEmail()!=null)
                    {
                        mBinding.etEmail.setText(customerModel.getEmail());
                        email = customerModel.getEmail();
                    }
                    if ( customerModel.getEMail_CC()!=null)
                    {
                        mBinding.etEmailCc.setText(customerModel.getEMail_CC());
                        emailCC = customerModel.getEMail_CC();
                    }
                    if (customerModel.getEMail_BCC()!=null)
                    {
                        mBinding.etBccEmail.setText(customerModel.getEMail_BCC());
                        emailBCC = customerModel.getEMail_BCC();
                    }
                    if (customerModel.getContacts()!=null)
                    {
                        mBinding.etContact.setText(customerModel.getContacts());
                        contact = customerModel.getContacts();
                    }

                    if (customerModel.getFax_No()!=null)
                    {
                        mBinding.etFax.setText(customerModel.getFax_No());
                        fax = customerModel.getFax_No();
                    }

                    if (customerModel.getAddress()!=null)
                    {
                        mBinding.etAddress.setText(customerModel.getAddress());
                        address = customerModel.getAddress();
                    }
                    if ( customerModel.getPayee()!=null)
                    {
                        mBinding.etPayee.setText(customerModel.getPayee());
                        payee = customerModel.getPayee();
                    }
                    if (customerModel.getInstruction()!=null)
                    {
                        mBinding.etInstruction.setText(customerModel.getInstruction());
                        instruction = customerModel.getInstruction();
                    }
                    if (customerModel.getComments()!=null)
                    {
                        mBinding.etComment.setText(customerModel.getComments());
                        comment = customerModel.getComments();
                    }



                }


            }

            if (getCustomerResponse.getContactPersonsList()!=null)
            {

                contactPersonsList.addAll(getCustomerResponse.getContactPersonsList());
                adapter.setContactPersonsList(contactPersonsList);
            }


        }

    }


    private void apiCallForSavingCustomer(CustomerModel customerModel) {

        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<UpdateStatus> call = ApiClient.getInstance().getApi().insertCustomer(customerModel);

        call.enqueue(new Callback<UpdateStatus>() {
            @Override
            public void onResponse(@NotNull Call<UpdateStatus> call, @NotNull Response<UpdateStatus> response) {
                if (response.body() != null) {
                    UpdateStatus updateStatus = response.body();
                    Toast.makeText(requireContext(), updateStatus.getStrMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if (updateStatus.getStatus()==1) {
                        reFreshForm();
                    }

                } else {
                    Toast.makeText(requireContext(), " " + response.errorBody(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }

            }

            @Override
            public void onFailure(@NotNull Call<UpdateStatus> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " on Failure" + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    private void reFreshForm() {

        mBinding.etPartName.setText(" ");
        mBinding.etPartAbbreviation.setText(" ");
        mBinding.etFocalPersonName.setText(" ");
        mBinding.etFocalPersonCNIC.setText(" ");
        mBinding.companyCheckBox.setChecked(false);
        mBinding.etSaleTax.setText(" ");
        mBinding.etNtnNum.setText(" ");
        mBinding.etCreditDays.setText(" ");
        mBinding.etCreditLimit.setText(" ");
        mBinding.creditLimitNaRadio.setChecked(true);
        mBinding.applyCreditLimitCheckBox.setChecked(false);
        mBinding.etEmail.setText(" ");
        mBinding.etBccEmail.setText(" ");
        mBinding.etEmailCc.setText(" ");
        mBinding.etContact.setText(" ");
        mBinding.etFax.setText(" ");
        mBinding.etAddress.setText(" ");
        mBinding.etPayee.setText(" ");
        mBinding.etInstruction.setText(" ");
        mBinding.etComment.setText(" ");
        contactPersonsList.clear();
        if (contactPersonsList!=null)
        {
            adapter.setContactPersonsList(contactPersonsList);

        }
        mBinding.etEmailLayout.setError(null);
        mBinding.etEmailCcLayout.setError(null);
        mBinding.etEmailBccLayout.setError(null);
        mBinding.locationCheckbox.setText("");
        mBinding.locationCheckbox.setChecked(false);
    }

    private void showDialog() {

        AddContactDialogBinding binding = AddContactDialogBinding.inflate(getLayoutInflater());

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(binding.getRoot()).setCancelable(true).create();
        alertDialog.show();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.etName.getText() != null ? binding.etName.getText().toString() : " ";
                String phone = binding.etPhone.getText() != null ? binding.etPhone.getText().toString() : " ";
                String email = binding.etEmail.getText() != null ? binding.etEmail.getText().toString() : " ";
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (!name.equals("")) {
                    if (phone.length() ==11) {
                        if (email.matches(emailPattern)) {
                            ContactPersons contactPersons = new ContactPersons();
                            contactPersons.setContact_Person_ContactNo(phone);
                            if (binding.etDesignation.getText() != null) {
                                contactPersons.setContact_Person_Design(binding.etDesignation.getText().toString());

                            }
                            contactPersons.setContact_Person_Email(binding.etEmail.getText().toString());
                            contactPersons.setContact_Person_Name(name);
                            contactPersonsList.add(contactPersons);
                            adapter.setContactPersonsList(contactPersonsList);
                            alertDialog.dismiss();
                        } else {
                            binding.emailLayout.setError("please enter valid email ");
                        }

                    } else {
                        binding.etPhoneLayout.setError("Enter Phone Number");
                    }

                } else {
                    binding.etNameLayout.setError("Enter name");
                }



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
                    if (response.body() != null) {
                        cityHashMapForID.clear();
                        cityList.clear();
                        List<RegionModel> regionModelList = new ArrayList<>();
                        regionModelList = response.body();
                        for (RegionModel model : regionModelList) {
                            cityList.add(model.getName());
                            cityHashMapForID.put(model.getName(), model.getRegionId());
                            cityHashMapForName.put(model.getRegionId(), model.getName());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, cityList);
                            mBinding.citySpinner.setAdapter(adapter);
                        }
                    }

                } else {
                    Toast.makeText(requireContext(), " " + response.message(), Toast.LENGTH_SHORT).show();
                    if (response.message().equals("Unauthorized")) {
                        CustomsDialog.getInstance().loginAgain(requireActivity(), requireContext());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RegionModel>> call, @NotNull Throwable t) {
                Toast.makeText(requireContext(), " " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }

    public void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();

        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }


    }

    public boolean isNetworkConnected() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return connected = true;
        } else {
            return connected = false;
        }

    }
}