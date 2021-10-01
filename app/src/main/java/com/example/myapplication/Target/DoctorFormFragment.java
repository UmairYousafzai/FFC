package com.example.myapplication.Target;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.FilteredDoctorInfomationModel;
import com.example.myapplication.MainActivity;
import com.example.myapplication.ModelClasses.ClassificationModel;
import com.example.myapplication.ModelClasses.GradingModel;
import com.example.myapplication.ModelClasses.QualificationModel;
import com.example.myapplication.ModelClasses.SaveDoctorModel;
import com.example.myapplication.Target.utils.DoctorViewModel;
import com.example.myapplication.databinding.CustomSelectProfileImageDialogBinding;
import com.example.myapplication.databinding.FragmentDoctorFormBinding;
import com.example.myapplication.utils.CONSTANTS;
import com.example.myapplication.utils.Permission;
import com.example.myapplication.utils.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class DoctorFormFragment extends Fragment {

    private FragmentDoctorFormBinding mBinding;
    private UserViewModel userViewModel;
    private ArrayList<String> gradeArray=new ArrayList<>(), genderArray = new ArrayList<>(),classificationArray = new ArrayList<>(),qualificationArray=new ArrayList<>();

    private List<ClassificationModel> classificationModelList;
    private List<QualificationModel> qualificationModelList;
    private List<GradingModel> gradingModelList;
    private final HashMap<String, Integer> classificatoinHashMapForId = new HashMap<>();
    private final HashMap<String, Integer> gradingHashMapForId = new HashMap<>();
    private final HashMap<String, Integer> qualificationHashMapForId = new HashMap<>();

    private HashMap<Integer, String> qualificationHashMapForTitle = new HashMap<>();
    private HashMap<Integer, String> classificationHashMapForTitle = new HashMap<>();
    private HashMap<Integer, String> gradingHashMapForTitle = new HashMap<>();

    private ArrayAdapter<String> genderAdapter;
    private ArrayAdapter<String> classificationAdapter;
    private ArrayAdapter<String> qualificationAdapter;
    private ArrayAdapter<String> gradingAdapter;

    private NavController navController;
    private DoctorViewModel doctorViewModel;
    private FilteredDoctorInfomationModel receivingDoctor;
    private SaveDoctorModel saveDoctorModel= new SaveDoctorModel();

    int timing;

    private ProgressDialog progressDialog;

    private Bitmap selectedImage;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("oncreate"," dcotor form key on create  me ha");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDoctorFormBinding.inflate(inflater,container,false);

        ((MainActivity) requireActivity()).getSupportActionBar().show();
        Log.e("oncreateView"," dcotor form key on create view me ha");

        doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("onViewCreated","dcotor form key on view created me ha");

        navController = NavHostFragment.findNavController(this);

        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();

         receivingDoctor= DoctorFormFragmentArgs.fromBundle(getArguments()).getFilterDoc();
        btnListener();

        if (receivingDoctor!=null)
        {

            getUserSyncData();

            populateData(receivingDoctor);


        }
        else
        {
            getUserSyncData();

        }


    }

    private void populateData(FilteredDoctorInfomationModel doctor) {
        getUserSyncData();

        saveDoctorModel.setAddress(doctor.getAddress());
        saveDoctorModel.setClassification_Id(doctor.getClassificationId());
        saveDoctorModel.setDOB(doctor.getDOB());
        saveDoctorModel.setDoc_Status(0);
        saveDoctorModel.setDoctor_Id(doctor.getDoctorId());
        saveDoctorModel.setEmail(doctor.getEmail());
        saveDoctorModel.setGender(doctor.getGender());
        saveDoctorModel.setGrade_Id(doctor.getGradeId());
        saveDoctorModel.setName(doctor.getName());
        saveDoctorModel.setPhone(doctor.getPhone());
        saveDoctorModel.setQualification_Id(doctor.getQualificationId());
        saveDoctorModel.setTimings(doctor.getTimings());
        doctorViewModel.insertSchedule(doctor.getScheduleModels());

        mBinding.idDocName.setText(saveDoctorModel.getName());
        mBinding.idDocPhone.setText(saveDoctorModel.getPhone());
        mBinding.idDocEmail.setText(saveDoctorModel.getEmail());
        mBinding.idDocAdress.setText(saveDoctorModel.getAddress());
        mBinding.datofBirthmaterialDesign.setText(saveDoctorModel.getDOB());


        if (saveDoctorModel.getGender()==1)
        {
            mBinding.genderDropdown.setText("Male");

        }
        else if (saveDoctorModel.getGender()==2)
        {
            mBinding.genderDropdown.setText("Female");

        }
        else if (saveDoctorModel.getGender()==3)
        {
            mBinding.genderDropdown.setText("Other");

        }


        if (saveDoctorModel.getTimings()==1)
        {
            mBinding.radioButton1.setChecked(true);
            timing=1;
        }
        else if (saveDoctorModel.getTimings()==2)
        {
            mBinding.radioButton2.setChecked(true);
            timing=2;
        }




    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("onViewStateRestored","dcotor form key on view State restored  me ha");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart","dcotor form key on start  me ha");

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume","dcotor form key on Resume me ha ");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPause","dcotor form key on pause me ha");

    }


    @Override
    public void onStop() {
        super.onStop();
        Log.e("onStop","dcotor form key on stop me ha");


    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("onSaveInstanceState"," dcotor form key on save instance state me ha");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        Log.e("onDestroyView","dcotor form key on destroy view me ha");

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("onDestroy","doctor form key on destroy me ha");
        if (((AppCompatActivity)getActivity()).getSupportActionBar().isShowing()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        doctorViewModel.deleteAllSchedule();
    }
    public void getUserSyncData()
    {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


        userViewModel.getAllClassification().observe(getViewLifecycleOwner(), new Observer<List<ClassificationModel>>() {
            @Override
            public void onChanged(List<ClassificationModel> classificationModels) {

                classificationModelList =classificationModels;

                String classification = classificationHashMapForTitle.get(saveDoctorModel.getClassification_Id());
                mBinding.classficationDropdown.setText(classification);
                setclassificationSpinners();


            }
        });
        userViewModel.getAllQualification().observe(getViewLifecycleOwner(), new Observer<List<QualificationModel>>() {
            @Override
            public void onChanged(List<QualificationModel> qualificationModels) {
                qualificationModelList = qualificationModels;
                String qualification = qualificationHashMapForTitle.get(saveDoctorModel.getQualification_Id());

                mBinding.qualificationDropdown.setText(qualification);
                setQualificationSpinners();

            }
        });
        userViewModel.getAllGrades().observe(getViewLifecycleOwner(), new
                Observer<List<GradingModel>>() {
                    @Override
                    public void onChanged(List<GradingModel> gradingModels) {
                        gradingModelList= gradingModels;
                        String grade = gradingHashMapForTitle.get(saveDoctorModel.getGrade_Id());
                        mBinding.gradeDropdown.setText(grade);
                        setGradeSpinners();

                        progressDialog.dismiss();

                    }
                });
    }


    public void setclassificationSpinners()
    {
        genderArray.clear();
        classificationArray.clear();
        genderArray.add("Male");
        genderArray.add("Female");
        genderArray.add("Other");

        for (ClassificationModel model:classificationModelList)
        {
            classificationArray.add(model.getClassification_Title());
            classificatoinHashMapForId.put(model.getClassification_Title(), model.getClassification_Id());
            classificationHashMapForTitle.put(model.getClassification_Id(),model.getClassification_Title());
        }

         genderAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genderArray);
         classificationAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,classificationArray);
        mBinding.classficationDropdown.setAdapter(classificationAdapter);
        mBinding.genderDropdown.setAdapter(genderAdapter);


    }

    public void setQualificationSpinners()
    {

        qualificationArray.clear();



        for (QualificationModel model:qualificationModelList)
        {
            qualificationArray.add(model.getQualification_Title());
            qualificationHashMapForId.put(model.getQualification_Title(), model.getQualification_Id());
            qualificationHashMapForTitle.put(model.getQualification_Id(),model.getQualification_Title());

        }


        qualificationAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,qualificationArray);
        mBinding.qualificationDropdown.setAdapter(qualificationAdapter);


    }

    public void setGradeSpinners()
    {

        gradeArray.clear();



        for (GradingModel model:gradingModelList)
        {
            gradeArray.add(model.getGrade_Title());
            gradingHashMapForId.put(model.getGrade_Title(), model.getGrade_Id());
            gradingHashMapForTitle.put(model.getGrade_Id(),model.getGrade_Title());

        }


        gradingAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item,gradeArray);
        mBinding.gradeDropdown.setAdapter(gradingAdapter);


    }

    public void btnListener()
    {
        Permission permission= new Permission(requireContext(),requireActivity());
        mBinding.docTimingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton= mBinding.getRoot().findViewById(checkedId);
                if (radioButton.getText().toString().equals("Morning"))
                {
                    timing=1;
                    Toast.makeText(requireContext(), "Time Selected " + radioButton.getText().toString(),Toast.LENGTH_SHORT).show();
                }
                else if (radioButton.getText().toString().equals("Evening"))
                {
                    timing=2;
                    Toast.makeText(requireContext(), "Time Selected " + radioButton.getText().toString(),Toast.LENGTH_SHORT).show();

                }

            }
        });

        mBinding.datofBirthmaterialDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear,mDay,mMonth;
                Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                mMonth = calendar.get(Calendar.MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String mDate;
                        mDate=month+"/"+dayOfMonth+"/"+year;
                        mBinding.datofBirthmaterialDesign.setText(mDate);
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        mBinding.idAddDocSchedulefloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name, phone, email, address, dateOfBirth, classification, grade, qualification,gender;
                int classificationId, qualificationId, gradeId, timingId;
                name = mBinding.idDocName.getText().toString();
                phone = mBinding.idDocPhone.getText().toString();
                email = mBinding.idDocEmail.getText().toString();
                address = mBinding.idDocAdress.getText().toString();
                dateOfBirth = mBinding.datofBirthmaterialDesign.getText().toString();
                classification = mBinding.classficationDropdown.getText().toString();
                qualification = mBinding.qualificationDropdown.getText().toString();
                grade = mBinding.gradeDropdown.getText().toString();
                gender =  mBinding.genderDropdown.getText().toString();

                if (name.isEmpty()) {
                    mBinding.idDocName.requestFocus();
                    mBinding.idDocName.setError("Enter Name");
                } else if (phone.isEmpty()) {
                    mBinding.idDocPhone.requestFocus();
                    mBinding.idDocPhone.setError("Enter Phone Number");
                } else if (email.isEmpty()) {
                    mBinding.idDocEmail.requestFocus();
                    mBinding.idDocEmail.setError("Enter Email");
                } else if (address.isEmpty()) {
                    mBinding.idDocAdress.requestFocus();
                    mBinding.idDocAdress.setError("Enter Address");
                } else if (dateOfBirth.isEmpty()) {
                    mBinding.datofBirthmaterialDesign.requestFocus();
                    mBinding.datofBirthmaterialDesign.setError("Select Date Of Birth");
                } else if (classification.isEmpty()) {
                    mBinding.classficationDropdown.requestFocus();
                    mBinding.classficationDropdown.setError("Select Classification");
                } else if (qualification.isEmpty()) {
                    mBinding.qualificationDropdown.requestFocus();
                    mBinding.classficationDropdown.setError("Select Qualification");
                } else if (grade.isEmpty())
                {
                    mBinding.gradeDropdown.requestFocus();
                    mBinding.gradeDropdown.setError("Select Grade");
                }
                else if (gender.isEmpty())
                {
                    mBinding.genderDropdown.requestFocus();
                    mBinding.genderDropdown.setError("Select Gender");
                }
                else {
                    SaveDoctorModel saveDoctorModel = new SaveDoctorModel();

                    if (selectedImage!=null)
                    {
                        String path= getBytesFromBitmap(selectedImage);
                        saveDoctorModel.setImagePath(path);
                    }
                    else
                    {
                        saveDoctorModel.setImagePath("");
                    }
                    gradeId = gradingHashMapForId.get(grade);
                    qualificationId =  qualificationHashMapForId.get(qualification);
                    classificationId = classificatoinHashMapForId.get(classification);

                    saveDoctorModel.setName(name);
                    saveDoctorModel.setPhone(phone);
                    saveDoctorModel.setAddress(address);
                    saveDoctorModel.setEmail(email);
                    saveDoctorModel.setClassification_Id(classificationId);
                    saveDoctorModel.setDOB(dateOfBirth);
                    saveDoctorModel.setQualification_Id(qualificationId);
                    saveDoctorModel.setGrade_Id(gradeId);
                    saveDoctorModel.setTimings(timing);
                    saveDoctorModel.setDoctor_Id(0);
                    DoctorFormFragmentDirections.ActionDoctorFormFragmentToAddScheduleFragment action = DoctorFormFragmentDirections.actionDoctorFormFragmentToAddScheduleFragment();
                    action.setDoctorToSave(saveDoctorModel);
                    navController.navigate(action);

                }
            }
        });

        mBinding.idAddProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomSelectProfileImageDialogBinding dialogBinding = CustomSelectProfileImageDialogBinding.inflate(getLayoutInflater());
                AlertDialog alertDialog = new AlertDialog.Builder(requireContext())
                        .setView(dialogBinding.getRoot())
                        .setCancelable(false).create();
                alertDialog.show();

                dialogBinding.takePhotoBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            permission.getCameraPermission();
                        } else {
                            Intent cameraIntent= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                            if (cameraIntent.resolveActivity(requireContext().getPackageManager())!=null)
                            {
                                startActivityForResult(cameraIntent,0);
                            }

                        }
                        alertDialog.dismiss();

                    }
                });

                dialogBinding.selectFromGalleryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissionGallery();

                        } else {
                            Intent galleryIntent= new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                            startActivityForResult(galleryIntent,1);
                        }
                        alertDialog.dismiss();


                    }
                });
                dialogBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode!=RESULT_CANCELED)
        {
            switch (requestCode)
            {
                case 0:
                    if (resultCode==RESULT_OK&&data!=null)
                    {
                        selectedImage= (Bitmap) data.getExtras().get("data");
                        mBinding.idDocImage.setImageBitmap(selectedImage);
                    }
                    break;

                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImageUri =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = requireActivity().getContentResolver().query(selectedImageUri,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                selectedImage= BitmapFactory.decodeFile(picturePath);
                                mBinding.idDocImage.setImageBitmap(selectedImage);
                                cursor.close();
                            }
                        }

                    }
                    break;
            }

        }
    }
    public String getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        String imgString = Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP);
        return imgString;
    }

    public void requestPermissionCamera() {


        String[] permissionArray = new String[]{Manifest.permission.CAMERA};
        SweetAlertDialog alertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE);

        alertDialog.setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ActivityCompat.requestPermissions(requireActivity(),
                                permissionArray,
                                CONSTANTS.PERMISSION_REQUEST_CODE_FOR_CAMERA);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });


        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA) ) {

            alertDialog.setTitle("Permission Needed");
            alertDialog.setContentText("Camera Permission Needed For Taking Profile Picture. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE_FOR_CAMERA);

        }


    }
    public void requestPermissionGallery() {


        String[] permissionArray = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        SweetAlertDialog alertDialog = new SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE);

        alertDialog.setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        ActivityCompat.requestPermissions(requireActivity(),
                                permissionArray,
                                CONSTANTS.PERMISSION_REQUEST_CODE_FOR_GALLERY);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                });


        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.CAMERA) ) {

            alertDialog.setTitle("Permission Needed");
            alertDialog.setContentText("Camera Permission Needed For Taking Profile Picture. ");
            alertDialog.show();


        } else {
            ActivityCompat.requestPermissions(requireActivity(), permissionArray, CONSTANTS.PERMISSION_REQUEST_CODE_FOR_GALLERY);

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CONSTANTS.PERMISSION_REQUEST_CODE_FOR_GALLERY) {
            if (permissions.length > 0 && grantResults.length == permissions.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {


                    }
                }


            }

        }
        else if (requestCode == CONSTANTS.PERMISSION_REQUEST_CODE_FOR_CAMERA)
        {
            if (permissions.length > 0 && grantResults.length == permissions.length) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    } else {


                    }
                }


            }
        }

    }
}