package com.example.ffccloud.Target;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import static android.content.ContentValues.TAG;

public class ScanFragment {

    NavController navController;

//    @Override
//    public void onCreate(Bundle state) {
//        super.onCreate(state);
//        ScannerView = new ZXingScannerView(getActivity());   // Programmatically initialize the scanner view
//        //setContentView(ScannerView);                // Set the scanner view as the content view
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        navController = Navigation.findNavController(view);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//
//        ScannerView = new ZXingScannerView(getActivity());
//        View root = ScannerView;
//
//        return root;
//    }
//
//    @Override
//    public void handleResult(Result rawResult) {
//        // Do something with the result here
//        Log.v(TAG, rawResult.getText()); // Prints scan results
//        Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)
//
//        // If you would like to resume scanning, call this method below:
//        ScannerView.resumeCameraPreview(this);
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        ScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        ScannerView.startCamera();          // Start camera on resume
//        ScannerView.resumeCameraPreview(this);
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        ScannerView.stopCamera();           // Stop camera on pause
//    }
}