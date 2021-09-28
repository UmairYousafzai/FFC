package com.example.myapplication.LocalTravel;

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


public class LocalTravelFragment extends Fragment {
    LinearLayout local_target,local_privatetravel,office;
    private NavController navController;
    FfcDatabase ffcDatabase;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        ffcDatabase = FfcDatabase.getInstance(getContext());
        SharedPreferenceHelper.getInstance(getContext()).setActivity("Local Travel");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_local_travel, container, false);
        init(root);

        local_target.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_target_main);
            }
        });
        local_privatetravel.setOnClickListener(new View.OnClickListener() {
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
   //             navController.navigate(R.id.nav_private_travel);
            }
        });
        office.setOnClickListener(new View.OnClickListener() {
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
        //        navController.navigate(R.id.nav_office);

            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                //navController.navigate(R.id.GetDocByEmpFragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return root;
    }

    private void init(View root) {
        local_target = root.findViewById(R.id.local_target);
        local_privatetravel = root.findViewById(R.id.local_privatetravel);
        office = root.findViewById(R.id.office);
    }
}