package com.example.ffccloud.utils;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseMethod;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CustomBindingAdapter {

    @BindingAdapter("setAdapter")
    public static void setAdapter(RecyclerView recyclerView,RecyclerView.Adapter<?> adapter)
    {
        if (adapter!=recyclerView.getAdapter())
        {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
        }

        Log.e("setAdapter error:",adapter.toString());

    }
    @BindingAdapter("setCustomSpinnerAdapter")
    public static void setAdapter(AutoCompleteTextView textView, ArrayAdapter<?> adapter) {
        textView.setAdapter(adapter);
    }
     @BindingAdapter("setSpinnerAdapter")
    public static void setAdapter(Spinner spinner , ArrayAdapter<?> adapter)
    {
        spinner.setAdapter(adapter);
    }

    @InverseMethod("positionToAction")
    public static int actionToPosition(String action)
    {

        if (action.equals("Post"))
        {
            return 1;
        }
        else if (action.equals("Cancel"))
        {
            return 2;
        }
        else
        {
            return 0;
        }
    }

    public static String positionToAction(int position)
    {
        if (position==1)
        {
            return "Post";
        }
        else if (position==2)
        {
            return "Cancel";
        }
        else
        {
            return "Un-Approved";
        }
    }
}
