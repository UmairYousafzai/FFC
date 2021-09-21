package com.example.myapplication.TargetMenu;

import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.Database.FfcDatabase;
import com.example.myapplication.R;
import com.example.myapplication.RoueActivity.Route;
import com.example.myapplication.RoueActivity.RouteActivityModel;
import com.example.myapplication.utils.SharedPreferenceHelper;

import java.time.LocalTime;
import java.util.List;

public class TargetMainFragment extends Fragment {
    LinearLayout nav_to_target,nav_to_localTravel,nav_to_privateTravel,nav_to_office;
    private NavController navController;
    private FfcDatabase ffcDatabase;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        ffcDatabase = FfcDatabase.getInstance(getContext());
        SharedPreferenceHelper.getInstance(getContext()).setActivity("Target");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_target_main, container, false);
        init(root);

        nav_to_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_target_sub_menu);
            }
        });
        nav_to_localTravel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {

                Route route = new Route();

                String end_cood = route.EndActivity();
                ffcDatabase.dao().setEnd(String.valueOf(LocalTime.now()),end_cood);


                RouteActivityModel routeActivityModel = route.StartActivity(getContext(),"Local Travel","local travel",0,
                        String.valueOf(LocalTime.now()),false);

                ffcDatabase.dao().insertRoute(routeActivityModel);
                List<RouteActivityModel> u = ffcDatabase.dao().getAll();
                for(int i = 0 ; i<u.size();i++){
                    Toast.makeText(getActivity(), u.get(i).getMain_Activity() + "|"
                            + u.get(i).getSub_Activity() + "|" + String.valueOf(u.get(i).getTask_Id()) + "|" +
                            u.get(i).getStart_date_time() + "|" + u.get(i).getStart_coordinates() + "|" +
                            u.get(i).getEnd_date_time() + "|" + u.get(i).getEnd_coordinates() + "|" , Toast.LENGTH_LONG).show();
                }
                navController.navigate(R.id.nav_local_travel);
            }
        });
        nav_to_privateTravel.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                Route route = new Route();

                String end_cood = route.EndActivity();
                ffcDatabase.dao().setEnd(String.valueOf(LocalTime.now()),end_cood);


                RouteActivityModel routeActivityModel = route.StartActivity(getContext(),"Private Travel","private travel",0,
                        String.valueOf(LocalTime.now()),false);

                ffcDatabase.dao().insertRoute(routeActivityModel);
                List<RouteActivityModel> u = ffcDatabase.dao().getAll();
                for(int i = 0 ; i<u.size();i++){
                    Toast.makeText(getActivity(), u.get(i).getMain_Activity() + "|"
                            + u.get(i).getSub_Activity() + "|" + String.valueOf(u.get(i).getTask_Id()) + "|" +
                            u.get(i).getStart_date_time() + "|" + u.get(i).getStart_coordinates() + "|" +
                            u.get(i).getEnd_date_time() + "|" + u.get(i).getEnd_coordinates() + "|" , Toast.LENGTH_LONG).show();
                }
                navController.navigate(R.id.nav_private_travel);
            }
        });
        nav_to_office.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                Route route = new Route();

                String end_cood = route.EndActivity();
                ffcDatabase.dao().setEnd(String.valueOf(LocalTime.now()),end_cood);


                RouteActivityModel routeActivityModel = route.StartActivity(getContext(),"Office","office",0,
                        String.valueOf(LocalTime.now()),false);

                ffcDatabase.dao().insertRoute(routeActivityModel);
                List<RouteActivityModel> u = ffcDatabase.dao().getAll();
                for(int i = 0 ; i<u.size();i++){
                    Toast.makeText(getActivity(), u.get(i).getMain_Activity() + "|"
                            + u.get(i).getSub_Activity() + "|" + String.valueOf(u.get(i).getTask_Id()) + "|" +
                            u.get(i).getStart_date_time() + "|" + u.get(i).getStart_coordinates() + "|" +
                            u.get(i).getEnd_date_time() + "|" + u.get(i).getEnd_coordinates() + "|" , Toast.LENGTH_LONG).show();
                }

                navController.navigate(R.id.nav_office);

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                navController.navigate(R.id.nav_home);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;
    }

    private void init(View root) {
        nav_to_target = root.findViewById(R.id.nav_to_target);
        nav_to_localTravel = root.findViewById(R.id.nav_to_localTravel);
        nav_to_privateTravel = root.findViewById(R.id.nav_to_privateTravel);
        nav_to_office = root.findViewById(R.id.nav_to_office);
    }
}