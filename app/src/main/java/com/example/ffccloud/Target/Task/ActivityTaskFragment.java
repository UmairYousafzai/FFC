package com.example.ffccloud.Target.Task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.ffccloud.databinding.FragmentActivityTaskBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

public class ActivityTaskFragment extends Fragment {

    private FragmentActivityTaskBinding mBinding;
    private int mDay, mMonth, mYear, key;
    private int mHour, mMinute;

    private String mDate;
    private String time;
    private String purposeString="",remarksString="";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentActivityTaskBinding.inflate(inflater,container,false);

        String title = ActivityTaskFragmentArgs.fromBundle(getArguments()).getActivityType();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
        mBinding.heading.setText(title);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    btnListener();
    }

    private void btnListener() {
        mBinding.datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int checkMonth= month%10, checkday = (dayOfMonth%10);;
                        String mMonth,mDay;
                        if (checkMonth>0&&month<9)
                        {
                            mMonth= "0"+(month+1);
                        }
                        else
                        {
                            mMonth=String.valueOf(month+1);

                        }

                        if (checkday>0 && dayOfMonth<10){
                            mDay = "0" + (dayOfMonth);

                        }
                        else
                        {
                            mDay= String.valueOf(dayOfMonth);

                        }
                        mDate = mMonth + "/" + mDay + "/" + year;
                        mBinding.datePicker.setText(mDate);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        mBinding.timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                mHour = calendar.get(Calendar.HOUR_OF_DAY);
                mMinute = calendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = String.valueOf(hourOfDay) + " : " + String.valueOf(minute);
                        mBinding.timePicker.setText(time);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        mBinding.voiceRecordBtnEmpRemarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        mBinding.voiceRecordBtnPurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

                try {
                    startActivityForResult(intent, 2);
                } catch (Exception e) {
                    Toast.makeText(requireContext(), " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText("Save SuccessFully")
                        .show();
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                remarksString= remarksString+" "+result.get(0);
                mBinding.empRemarks.setText(remarksString);
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                purposeString = purposeString+" "+result.get(0);
                mBinding.purposeText.setText(purposeString);
            }
        }

    }
}