package com.example.myapplication.AttendanceActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.R;
import com.example.myapplication.utils.SharedPreferenceHelper;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AttendanceFragment extends Fragment {
    MaterialButton CheckInButton,TakePhotoButton;
    FfcDatabase ffcDatabase;
    NavController navController;

    File photoFile = null;
    static final int CAPTURE_IMAGE_REQUEST = 0;
    String mCurrentPhotoPath;
    private ImageView camera_img;
    private boolean anypermissionMissing = true;

    @Override
     public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri photoURI = FileProvider.getUriForFile(getActivity(),
                    "com.example.myapplication.fileprovider",
                    photoFile);
            //   SharedPreferenceHelper.getInstance(getApplicationContext()).setProfilePhotoPath(photoFile.getAbsolutePath());
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photoURI);
                Log.d("image", String.valueOf(bitmap));
                camera_img.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
        }
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ffcDatabase = FfcDatabase.getInstance(getContext());
        SharedPreferenceHelper.getInstance(getContext()).setActivity("Attendance");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_attendance, container, false);
        List<String> config = ffcDatabase.dao().getOnConfigurations();

        //requestStoragePermission();

        CheckInButton = root.findViewById(R.id.CheckInButton);
        TakePhotoButton = root.findViewById(R.id.TakePhotoButton);
        camera_img = root.findViewById(R.id.camera_img);


        TakePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        CheckInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(config.contains("Route_Activity")){
                    navController.navigate(R.id.nav_target_main);
                }
                else{
                    navController.navigate(R.id.nav_target_sub_menu);
                }
            }
        });


        return root;
    }

    private void captureImage() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        } else {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                try {

                    photoFile = createImageFile();
                    //   displayMessage(getBaseContext(),photoFile.getAbsolutePath());
                    Log.i("Mayank", photoFile.getAbsolutePath());

                    // Continue only if the File was successfully created


                    // SharedPreferenceHelper.getInstance(getApplicationContext()).setProfilePhotoPath(photoFile.getAbsolutePath());


                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.example.myapplication.fileprovider",
                            photoFile);


                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST);

                } catch (Exception ex) {
                    // Error occurred while creating the File
                    // displayMessage(getBaseContext(),ex.getMessage().toString());
                }


            } else {
                //    displayMessage(getBaseContext(),"Nullll");
            }
        }


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        TakePhotoButton.setVisibility(View.GONE);
        CheckInButton.setVisibility(View.VISIBLE);

        Log.d("path hai ", mCurrentPhotoPath);


        return image;
    }

}