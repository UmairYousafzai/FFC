package com.example.ffccloud.utils;

import android.util.Log;

import androidx.databinding.InverseMethod;


public class Converter {

    @InverseMethod("stringToInt")
    public static String intToString(int value)
    {

        return String.valueOf(value);
    }

    public static int stringToInt(String value)
    {
        int  number=0;
       try
       {
           number= Integer.parseInt(value);

       }
       catch (Exception e)
       {
           Log.e("Conversion error: ",""+e.getMessage());
       }

       return number;

    }
}
