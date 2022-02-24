package com.example.ffccloud.meeting;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.R;
import com.example.ffccloud.databinding.CustomAlertDialogBinding;
import com.example.ffccloud.databinding.CustomDialogBinding;
import com.example.ffccloud.databinding.FragmentMeetingBinding;
import com.example.ffccloud.meeting.adapter.EmployeeListAdapter;
import com.example.ffccloud.meeting.viewmodel.MeetingViewModel;
import com.example.ffccloud.model.DoctorsByAreaIdsModel;
import com.example.ffccloud.model.Employee;
import com.example.ffccloud.model.Meeting;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MeetingFragment extends Fragment {

    private FragmentMeetingBinding mBinding;
    private int mDay, mMonth, mYear, key;
    private int mHour, mMinute;

    private String mDate;
    private String time;
    private MeetingViewModel viewModel;
    private List<Employee> employeeList= new ArrayList<>();
    private String empIDs="",employees="",meetingType="";
    private GetUserInfoModel loginUser;
    private NavController navController;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        mBinding = FragmentMeetingBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        navController= NavHostFragment.findNavController(this);

        mBinding.setViewModel(viewModel);


        FfcDatabase ffcDatabase = FfcDatabase.getInstance(requireContext());
         loginUser = ffcDatabase.dao().getLoginUser();
        mBinding.hostTextview.setText(loginUser.getUserName());

        btnListener();
        getLiveData();
    }

    private void getLiveData() {
        viewModel.getToastMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                    Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
                }
            }
        });

         viewModel.getServerResponseMessage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (s!=null)
                {
                    Toast.makeText(requireContext(), ""+s, Toast.LENGTH_SHORT).show();
                    navController.popBackStack(R.id.meetingFragment,true);
                }
            }
        });


        viewModel.getMeetingMutableLiveData().observe(getViewLifecycleOwner(), new Observer<Meeting>() {
            @Override
            public void onChanged(Meeting meeting) {
                if (meeting!=null)
                {
                    showWarningDialog(meeting);
                }
            }
        });
    }

    private void showWarningDialog(Meeting meeting) {
        CustomAlertDialogBinding dialogBinding = CustomAlertDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(false).create();

            dialogBinding.title.setText("Meeting");
            dialogBinding.body.setText("Are You Sure You Want To Save?");





        alertDialog.show();
        dialogBinding.btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveMeeting(meeting);


                alertDialog.dismiss();
            }
        });
        dialogBinding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void saveMeeting(Meeting meeting) {

        String fromDate="",toDate="";
        if (loginUser!=null)
        {
            meeting.setEmp_Host_Id(loginUser.getID());
        }


        if (mBinding.fromDatePicker.getText()!=null)
        {
            if (mBinding.fromTimePicker.getText()!=null)
            {
               fromDate=mBinding.fromDatePicker.getText().toString()+" "+mBinding.fromTimePicker.getText().toString();
            }
        }

        if (mBinding.toDatePicker.getText()!=null)
        {
            if (mBinding.toTimePicker.getText()!=null)
            {
                toDate=mBinding.toDatePicker.getText().toString()+" "+mBinding.toTimePicker.getText().toString();
            }
        }
        if (!meetingType.isEmpty())
        {
            if (!fromDate.isEmpty())
            {
                mBinding.fromDatePicker.setError(null);
                if (!toDate.isEmpty())
                {
                    mBinding.toDatePicker.setError(null);

                    if (!empIDs.isEmpty())
                    {
                        mBinding.selectEmployeeLayout.setError(null);

                        meeting.setWork_From_Date(fromDate);
                        meeting.setWork_To_Date(toDate);
                        meeting.setWork_Plan_Type(meetingType);
                        meeting.setEmp_Ids(empIDs);
                        viewModel.saveMeeting(meeting);
                    }
                    else
                    {
                        mBinding.selectEmployeeLayout.setError("Please Select Employees");
                        mBinding.selectEmployeeLayout.requestFocus();
                    }

                }
                else
                {
                    mBinding.fromDatePicker.setError("Please Select Date");
                    Toast.makeText(requireContext(), "Please To Date", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                mBinding.fromDatePicker.setError("Please Select Date");
                Toast.makeText(requireContext(), "Please From Date", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(requireContext(), "Please Select Meeting Type", Toast.LENGTH_SHORT).show();
        }


    }


    private void btnListener() {

        mBinding.addParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showParticipantsDialog();
            }
        });

        mBinding.fromDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int checkMonth = month % 10, checkday = (dayOfMonth % 10);
                        ;
                        String mMonth, mDay;
                        if (checkMonth > 0 && month < 10) {
                            mMonth = "0" + (month + 1);
                        } else {
                            mMonth = String.valueOf(month + 1);

                        }

                        if (checkday > 0 && dayOfMonth < 9) {
                            mDay = "0" + (dayOfMonth);

                        } else {
                            mDay = String.valueOf(dayOfMonth);

                        }
                        mDate = mMonth + "/" + mDay + "/" + year;
                        mBinding.fromDatePicker.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        mBinding.toDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int checkMonth = month % 10, checkday = (dayOfMonth % 10);
                        ;
                        String mMonth, mDay;
                        if (checkMonth > 0 && month < 9) {
                            mMonth = "0" + (month + 1);
                        } else {
                            mMonth = String.valueOf(month + 1);

                        }

                        if (checkday > 0 && dayOfMonth < 9) {
                            mDay = "0" + (dayOfMonth);

                        } else {
                            mDay = String.valueOf(dayOfMonth);

                        }
                        mDate = mMonth + "/" + mDay + "/" + year;
                        mBinding.toDatePicker.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        mBinding.fromTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                        mBinding.fromTimePicker.setText(time);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        mBinding.toTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                        mBinding.toTimePicker.setText(time);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        radio_btnListener();

    }

    public void radio_btnListener() {
        mBinding.meetingRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.meetingRadioBtn.isChecked()) {
                    mBinding.meetingRadioBtn.setChecked(true);
                    mBinding.saminarRadioBtn.setChecked(false);
                    mBinding.tradeShowRadioBtn.setChecked(false);
                    mBinding.wabinarRadioBtn.setChecked(false);
                    mBinding.deamoRadioBtn.setChecked(false);
                    meetingType=  mBinding.meetingRadioBtn.getText().toString();
                }

            }
        });
        mBinding.saminarRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.saminarRadioBtn.isChecked()) {
                    mBinding.meetingRadioBtn.setChecked(false);
                    mBinding.saminarRadioBtn.setChecked(true);
                    mBinding.tradeShowRadioBtn.setChecked(false);
                    mBinding.wabinarRadioBtn.setChecked(false);
                    mBinding.deamoRadioBtn.setChecked(false);
                    meetingType=  mBinding.saminarRadioBtn.getText().toString();

                }

            }
        });
        mBinding.tradeShowRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.tradeShowRadioBtn.isChecked()) {
                    mBinding.meetingRadioBtn.setChecked(false);
                    mBinding.saminarRadioBtn.setChecked(false);
                    mBinding.tradeShowRadioBtn.setChecked(true);
                    mBinding.wabinarRadioBtn.setChecked(false);
                    mBinding.deamoRadioBtn.setChecked(false);
                    meetingType=  mBinding.tradeShowRadioBtn.getText().toString();

                }

            }
        });
        mBinding.wabinarRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.wabinarRadioBtn.isChecked()) {
                    mBinding.meetingRadioBtn.setChecked(false);
                    mBinding.saminarRadioBtn.setChecked(false);
                    mBinding.tradeShowRadioBtn.setChecked(false);
                    mBinding.wabinarRadioBtn.setChecked(true);
                    mBinding.deamoRadioBtn.setChecked(false);
                    meetingType=  mBinding.wabinarRadioBtn.getText().toString();

                }

            }
        });

        mBinding.deamoRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.deamoRadioBtn.isChecked()) {
                    mBinding.meetingRadioBtn.setChecked(false);
                    mBinding.saminarRadioBtn.setChecked(false);
                    mBinding.tradeShowRadioBtn.setChecked(false);
                    mBinding.wabinarRadioBtn.setChecked(false);
                    mBinding.deamoRadioBtn.setChecked(true);
                    meetingType=  mBinding.deamoRadioBtn.getText().toString();

                }


            }
        });
    }

    private void showParticipantsDialog()
    {
        employeeList.clear();

        EmployeeListAdapter adapter= new EmployeeListAdapter();

        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        viewModel.getEmployees();
        CustomDialogBinding dialogBinding= CustomDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(true).create();
        alertDialog.show();

        dialogBinding.employeeListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        dialogBinding.employeeListRecyclerview.setAdapter(adapter);

        adapter.setmListener(new EmployeeListAdapter.EmployeeRecyclerItemListener() {
            @Override
            public void onItemListener(Employee model, boolean add) {

                if (add)
                {
                    employeeList.add(model);

                }else
                {
                    employeeList.remove(model);
                }
            }
        });

        viewModel.getEmployeeMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employees) {

                if (employees!=null)
                {
                    progressDialog.dismiss();
                    adapter.setEmployeeList(employees);
                }
            }
        });

        dialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });
        dialogBinding.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                employees="";
                empIDs="";
                for (Employee model : employeeList) {
                    employees = employees + model.getUserName() + ", ";
                    empIDs = empIDs + ( (int)model.getId() )+ ", ";
                }
                mBinding.tvSelectEmployee.setText(employees);
                alertDialog.dismiss();

            }
        });
        dialogBinding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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


}