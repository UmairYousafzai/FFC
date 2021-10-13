package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.Login.GetUserInfoModel;
import com.example.myapplication.databinding.CustomDialogBinding;
import com.example.myapplication.databinding.CustomSelectActivityDialogBinding;
import com.example.myapplication.databinding.FragmentMeetingBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class MeetingFragment extends Fragment {

    private FragmentMeetingBinding mBinding;
    private int mDay, mMonth, mYear, key;
    private int mHour, mMinute;

    private String mDate;
    private String time;

    private ArrayAdapter<String> participantsAdapter;
    private ArrayAdapter<String> relatedToAdapter;

    private FfcDatabase ffcDatabase;
     

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        mBinding = FragmentMeetingBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ffcDatabase = FfcDatabase.getInstance(requireContext());
        GetUserInfoModel loginUser = ffcDatabase.dao().getLoginUser();
        mBinding.hostTextview.setText(loginUser.getUserName());

        btnListener();
        setupSpinner();
    }

    private void setupSpinner() {
        ArrayList<String> participantsList = new ArrayList<>();
        participantsList.add("none");
        participantsList.add("Employee");
        participantsList.add("Doctor");
        participantsList.add("Customer");
        participantsList.add("Vendor");
        participantsAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, participantsList);
        mBinding.participantsSpinner.setAdapter(participantsAdapter);

        ArrayList<String> relatedToList = new ArrayList<>();
        relatedToList.add("none");
        relatedToList.add("Lead");
        relatedToList.add("Doctor");
        relatedToList.add("Customer");
        relatedToList.add("Vendor");
        relatedToList.add("Contact");
        relatedToAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, relatedToList);
        mBinding.relatedToSpinner.setAdapter(relatedToAdapter);

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
                }


            }
        });
    }

    private void showParticipantsDialog()
    {
        CustomDialogBinding dialogBinding= CustomDialogBinding.inflate(getLayoutInflater());
        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).setView(dialogBinding.getRoot()).setCancelable(true).create();
        alertDialog.show();
    }
}