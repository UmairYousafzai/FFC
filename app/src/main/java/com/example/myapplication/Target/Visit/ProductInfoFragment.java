package com.example.myapplication.Target.Visit;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.myapplication.R;
import com.google.android.material.button.MaterialButton;

public class ProductInfoFragment extends Fragment {
    ImageView back_btn,search_btn;
    RelativeLayout relative_search;
    NavController navController;
    LinearLayout linear_product_a,linear_product_b,linear_product_c,linear_product_d;
    MaterialButton presentation_btn1,presentation_btn2,presentation_btn3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View root = inflater.inflate(R.layout.fragment_product_info, container, false);
        init(root);

        relative_search.setVisibility(View.GONE);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_visit);
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relative_search.setVisibility(View.VISIBLE);
            }
        });
        presentation_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_product_slider);
            }
        });
        presentation_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_product_slider);
            }
        });
        presentation_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_product_slider);
            }
        });
        /*linear_product_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_product_slider);
            }
        });*/

        return root;
    }

    private void init(View root) {
        back_btn = root.findViewById(R.id.back_btn);
        search_btn = root.findViewById(R.id.search_btn);
        relative_search = root.findViewById(R.id.relative_search);

        linear_product_a = root.findViewById(R.id.linear_product_a);
        linear_product_b = root.findViewById(R.id.linear_product_b);
        linear_product_c = root.findViewById(R.id.linear_product_c);

        presentation_btn1 = root.findViewById(R.id.presentation_btn1);
        presentation_btn2 = root.findViewById(R.id.presentation_btn2);
        presentation_btn3 = root.findViewById(R.id.presentation_btn3);
        //linear_product_d = root.findViewById(R.id.linear_product_d);
    }
}