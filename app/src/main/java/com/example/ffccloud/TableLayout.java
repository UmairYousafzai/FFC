package com.example.ffccloud;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.databinding.FragmentTableLayoutBinding;
import com.example.ffccloud.utils.CustomsDialog;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.SyncDataToDB;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableLayout extends Fragment {
    private FragmentTableLayoutBinding mBinding;
    private List<FilteredDoctoredModel> filteredDoctoredModelList = new ArrayList<>();
    private final List<TextView> idTextViewList = new ArrayList<>();
    private final List<TextView> nameTextViewList = new ArrayList<>();
    private final List<TextView> phoneTextViewList = new ArrayList<>();
    private final List<TextView> dobTextViewList = new ArrayList<>();
    private final List<TextView> addressTextViewList = new ArrayList<>();
    private final List<TextView> classificationTextViewList = new ArrayList<>();
    private final List<TextView> gradeTextViewList = new ArrayList<>();
    private boolean backwardBtnCheck = false, forwardBtnCheck = false;
    private int forwardCounter = 0, backwardCounter = 0, dataCounter = 0, startIndex = 0, endIndex = 0;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();

        mBinding = FragmentTableLayoutBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        setUpTextViewsLists();
        setUpTable();
    }

    private void setUpTextViewsLists() {
        idTextViewList.add(mBinding.tvId1);
        idTextViewList.add(mBinding.tvId2);
        idTextViewList.add(mBinding.tvId3);
        idTextViewList.add(mBinding.tvId4);
        idTextViewList.add(mBinding.tvId5);

        nameTextViewList.add(mBinding.tvName1);
        nameTextViewList.add(mBinding.tvName2);
        nameTextViewList.add(mBinding.tvName3);
        nameTextViewList.add(mBinding.tvName4);
        nameTextViewList.add(mBinding.tvName5);

        phoneTextViewList.add(mBinding.tvPhone1);
        phoneTextViewList.add(mBinding.tvPhone2);
        phoneTextViewList.add(mBinding.tvPhone3);
        phoneTextViewList.add(mBinding.tvPhone4);
        phoneTextViewList.add(mBinding.tvPhone5);

        dobTextViewList.add(mBinding.tvDob1);
        dobTextViewList.add(mBinding.tvDob2);
        dobTextViewList.add(mBinding.tvDob3);
        dobTextViewList.add(mBinding.tvDob4);
        dobTextViewList.add(mBinding.tvDob5);

        addressTextViewList.add(mBinding.tvAddress1);
        addressTextViewList.add(mBinding.tvAddress2);
        addressTextViewList.add(mBinding.tvAddress3);
        addressTextViewList.add(mBinding.tvAddress4);
        addressTextViewList.add(mBinding.tvAddress5);

        classificationTextViewList.add(mBinding.tvClassification1);
        classificationTextViewList.add(mBinding.tvClassification2);
        classificationTextViewList.add(mBinding.tvClassification3);
        classificationTextViewList.add(mBinding.tvClassification4);
        classificationTextViewList.add(mBinding.tvClassification5);

        gradeTextViewList.add(mBinding.tvGrade1);
        gradeTextViewList.add(mBinding.tvGrade2);
        gradeTextViewList.add(mBinding.tvGrade3);
        gradeTextViewList.add(mBinding.tvGrade4);
        gradeTextViewList.add(mBinding.tvGrade5);


    }

    private void setUpTable() {
        getDoctorList();

        mBinding.sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float deg = (mBinding.sortBtn.getRotation() == 180F) ? 0F : 180F;
                mBinding.sortBtn.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());
            }
        });

        mBinding.forwardPageBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                if (forwardBtnCheck) {

                    if (filteredDoctoredModelList.size() > dataCounter) {
                        int check = filteredDoctoredModelList.size() - dataCounter;
                        backwardBtnCheck = true;
                        mBinding.backwardPageBtn.setEnabled(true);
                        if (check >= 5) {
                            startIndex= dataCounter;
                            for (int i = 0; i < 5; i++, dataCounter++) {
                                FilteredDoctoredModel model = filteredDoctoredModelList.get(dataCounter + 1);
                                idTextViewList.get(i).setText(String.valueOf(model.getDoctorId()));
                                idTextViewList.get(i).setTooltipText(String.valueOf(model.getDoctorId()));

                                nameTextViewList.get(i).setText(model.getName());
                                nameTextViewList.get(i).setTooltipText(model.getName());

                                addressTextViewList.get(i).setText(model.getAddress());
                                addressTextViewList.get(i).setTooltipText(model.getAddress());

                                dobTextViewList.get(i).setText(model.getDOB());
                                dobTextViewList.get(i).setTooltipText(model.getDOB());

                                phoneTextViewList.get(i).setText(model.getPhone());
                                phoneTextViewList.get(i).setTooltipText(model.getPhone());

                                classificationTextViewList.get(i).setText(model.getClassificationTitle());
                                classificationTextViewList.get(i).setTooltipText(model.getClassificationTitle());

                                gradeTextViewList.get(i).setText(model.getGradeTitle());
                                gradeTextViewList.get(i).setTooltipText(model.getGradeTitle());

                            }
                            if (dataCounter == filteredDoctoredModelList.size()) {
                                mBinding.forwardPageBtn.setEnabled(false);
                                forwardBtnCheck = false;
                            }
                            String pageIndex = (startIndex+1) +" to " + (dataCounter +1) + " of " + filteredDoctoredModelList.size();
                            mBinding.tvPageIndex.setText(pageIndex);
                        } else if (check != 0) {
                            startIndex= dataCounter;
                            for (int i = 0; dataCounter < filteredDoctoredModelList.size() - 1; i++, dataCounter++) {
                                FilteredDoctoredModel model = filteredDoctoredModelList.get(dataCounter + 1);
                                idTextViewList.get(i).setText(String.valueOf(model.getDoctorId()));
                                idTextViewList.get(i).setTooltipText(String.valueOf(model.getDoctorId()));

                                nameTextViewList.get(i).setText(model.getName());
                                nameTextViewList.get(i).setTooltipText(model.getName());

                                addressTextViewList.get(i).setText(model.getAddress());
                                addressTextViewList.get(i).setTooltipText(model.getAddress());

                                dobTextViewList.get(i).setText(model.getDOB());
                                dobTextViewList.get(i).setTooltipText(model.getDOB());

                                phoneTextViewList.get(i).setText(model.getPhone());
                                phoneTextViewList.get(i).setTooltipText(model.getPhone());

                                classificationTextViewList.get(i).setText(model.getClassificationTitle());
                                classificationTextViewList.get(i).setTooltipText(model.getClassificationTitle());

                                gradeTextViewList.get(i).setText(model.getGradeTitle());
                                gradeTextViewList.get(i).setTooltipText(model.getGradeTitle());


                            }
                            String pageIndex =( startIndex+1) +" to " + (dataCounter +1) + " of " + filteredDoctoredModelList.size();
                            mBinding.tvPageIndex.setText(pageIndex);
                            if (dataCounter == filteredDoctoredModelList.size()) {
                                mBinding.forwardPageBtn.setEnabled(false);
                                forwardBtnCheck = false;
                            }
                        }
                    }


                }
            }
        });

        mBinding.backwardPageBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (backwardBtnCheck) {

                    backwardCounter = dataCounter - 5;
                    mBinding.forwardPageBtn.setEnabled(true);
                    forwardBtnCheck = true;

                    if (backwardCounter > 5) {
                        startIndex= backwardCounter;

                        for (int i = 4; i >= 0; i--, backwardCounter--, dataCounter--) {
                            FilteredDoctoredModel model = filteredDoctoredModelList.get(backwardCounter);
                            idTextViewList.get(i).setText(String.valueOf(model.getDoctorId()));
                            idTextViewList.get(i).setTooltipText(String.valueOf(model.getDoctorId()));

                            nameTextViewList.get(i).setText(model.getName());
                            nameTextViewList.get(i).setTooltipText(model.getName());

                            addressTextViewList.get(i).setText(model.getAddress());
                            addressTextViewList.get(i).setTooltipText(model.getAddress());

                            dobTextViewList.get(i).setText(model.getDOB());
                            dobTextViewList.get(i).setTooltipText(model.getDOB());

                            phoneTextViewList.get(i).setText(model.getPhone());
                            phoneTextViewList.get(i).setTooltipText(model.getPhone());

                            classificationTextViewList.get(i).setText(model.getClassificationTitle());
                            classificationTextViewList.get(i).setTooltipText(model.getClassificationTitle());

                            gradeTextViewList.get(i).setText(model.getGradeTitle());
                            gradeTextViewList.get(i).setTooltipText(model.getGradeTitle());

                        }
                        startIndex= backwardCounter;
                        String pageIndex = (startIndex+1) +" to" + (dataCounter +1) + " of " + filteredDoctoredModelList.size();

                        mBinding.tvPageIndex.setText(pageIndex);
                    } else if (backwardCounter != 0) {
                        startIndex= backwardCounter;
                        for (int i = backwardCounter; i >= 0; i--, backwardCounter--, dataCounter--) {
                            FilteredDoctoredModel model = filteredDoctoredModelList.get(backwardCounter);
                            idTextViewList.get(i).setText(String.valueOf(model.getDoctorId()));
                            idTextViewList.get(i).setTooltipText(String.valueOf(model.getDoctorId()));

                            nameTextViewList.get(i).setText(model.getName());
                            nameTextViewList.get(i).setTooltipText(model.getName());

                            addressTextViewList.get(i).setText(model.getAddress());
                            addressTextViewList.get(i).setTooltipText(model.getAddress());

                            dobTextViewList.get(i).setText(model.getDOB());
                            dobTextViewList.get(i).setTooltipText(model.getDOB());

                            phoneTextViewList.get(i).setText(model.getPhone());
                            phoneTextViewList.get(i).setTooltipText(model.getPhone());

                            classificationTextViewList.get(i).setText(model.getClassificationTitle());
                            classificationTextViewList.get(i).setTooltipText(model.getClassificationTitle());

                            gradeTextViewList.get(i).setText(model.getGradeTitle());
                            gradeTextViewList.get(i).setTooltipText(model.getGradeTitle());


                        }
                        startIndex= backwardCounter;
                        String pageIndex = (startIndex+1) +" to " + (dataCounter +1) + " of " + filteredDoctoredModelList.size();
                        mBinding.tvPageIndex.setText(pageIndex);
                        backwardBtnCheck = false;
                        mBinding.backwardPageBtn.setEnabled(false);

                    }


                }

            }
        });

    }


    private void getDoctorList() {


        int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        Call<List<FilteredDoctoredModel>> call = ApiClient.getInstance().getApi().GetFilteredDoctorsByEmployeeId(token, id);

        call.enqueue(new Callback<List<FilteredDoctoredModel>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NotNull Call<List<FilteredDoctoredModel>> call, @NotNull Response<List<FilteredDoctoredModel>> response) {
                if (response.body() != null) {
                    List<FilteredDoctoredModel> list = response.body();
                    if (list.size() > 0) {
                        filteredDoctoredModelList = list;


                        if (filteredDoctoredModelList.size() > 5) {
                            forwardBtnCheck = true;

                        } else {
                            forwardBtnCheck = false;
                            mBinding.forwardPageBtn.setEnabled(false);
                            mBinding.forwardPageBtn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.slategray, null));
                        }

                        if (filteredDoctoredModelList.size() >= 5) {
                            for (int i = 0; i < 5; i++) {
                                FilteredDoctoredModel model = filteredDoctoredModelList.get(i);
                                idTextViewList.get(i).setText(String.valueOf(model.getDoctorId()));
                                idTextViewList.get(i).setTooltipText(String.valueOf(model.getDoctorId()));

                                nameTextViewList.get(i).setText(model.getName());
                                nameTextViewList.get(i).setTooltipText(model.getName());

                                addressTextViewList.get(i).setText(model.getAddress());
                                addressTextViewList.get(i).setTooltipText(model.getAddress());

                                dobTextViewList.get(i).setText(model.getDOB());
                                dobTextViewList.get(i).setTooltipText(model.getDOB());

                                phoneTextViewList.get(i).setText(model.getPhone());
                                phoneTextViewList.get(i).setTooltipText(model.getPhone());

                                classificationTextViewList.get(i).setText(model.getClassificationTitle());
                                classificationTextViewList.get(i).setTooltipText(model.getClassificationTitle());

                                gradeTextViewList.get(i).setText(model.getGradeTitle());
                                gradeTextViewList.get(i).setTooltipText(model.getGradeTitle());
                                dataCounter = i;
                            }
                            String pageIndex = "1 to " + (dataCounter +1)+ " of " + filteredDoctoredModelList.size();
                            mBinding.tvPageIndex.setText(pageIndex);


                        } else {
                            int i = 0;
                            for (FilteredDoctoredModel model : filteredDoctoredModelList) {
                                idTextViewList.get(i).setText(String.valueOf(model.getDoctorId()));
                                nameTextViewList.get(i).setText(model.getName());
                                addressTextViewList.get(i).setText(model.getAddress());
                                dobTextViewList.get(i).setText(model.getDOB());
                                phoneTextViewList.get(i).setText(model.getPhone());
                                classificationTextViewList.get(i).setText(model.getClassificationTitle());
                                gradeTextViewList.get(i).setText(model.getGradeTitle());
                                i++;
                                dataCounter++;
                            }
                            String pageIndex = "1 to" +(dataCounter +1) + " of " + filteredDoctoredModelList.size();
                            mBinding.tvPageIndex.setText(pageIndex);
                        }

                    }


                } else {
                     CustomsDialog.getInstance().loginAgain(requireActivity(),requireContext() );

                }
            }

            @Override
            public void onFailure(@NotNull Call<List<FilteredDoctoredModel>> call, @NotNull Throwable t) {

                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

}