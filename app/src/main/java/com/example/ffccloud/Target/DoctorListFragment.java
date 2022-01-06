package com.example.ffccloud.Target;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ffccloud.ModelClasses.ClassificationModel;
import com.example.ffccloud.FilteredDoctoredModel;
import com.example.ffccloud.ModelClasses.GradingModel;
import com.example.ffccloud.NetworkCalls.ApiClient;
import com.example.ffccloud.R;
import com.example.ffccloud.Target.Adapters.FilterDoctorRecyclerAdapter;
import com.example.ffccloud.Target.utils.DoctorViewModel;
import com.example.ffccloud.databinding.FragmentDoctorListBinding;
import com.example.ffccloud.databinding.FragmentSalesOrderListBinding;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.example.ffccloud.utils.SyncDataToDB;
import com.example.ffccloud.utils.UserViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorListFragment extends Fragment {

    private NavController navController;
    private FragmentDoctorListBinding mBinding;
    private boolean isSlideDown = false;
    private final String[] classificationArray = new String[20];
    private final String[] qualificationArray = new String[20];
    private final String[] gradesArray = new String[20];
    private DoctorViewModel doctorViewModel;
    private FilterDoctorRecyclerAdapter adapter;
    private List<FilteredDoctoredModel> filteredDoctoredModelList = new ArrayList<>();
    private ProgressDialog progressDialog;
    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentDoctorListBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(this);
        Log.e("onViewCreated", "on view created me ha");
        // styling searchView

        progressDialog = new ProgressDialog(requireContext());

        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);


    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.e("onViewStateRestored", "on view restored me ha");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart", "on start me ha");


    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume", "on resume me ha ");


        btnListener();

        userViewModel.getAllClassification().observe(getViewLifecycleOwner(), new Observer<List<ClassificationModel>>() {
            @Override
            public void onChanged(List<ClassificationModel> list) {
                setClassificationSpinner(list);
            }
        });

        userViewModel.getAllGrades().observe(getViewLifecycleOwner(), new Observer<List<GradingModel>>() {
            @Override
            public void onChanged(List<GradingModel> gradingModels) {
                setGradesSpinner(gradingModels);
            }
        });


        setUpRecyclerView();

        setPullToFresh();
        doctorViewModel = new ViewModelProvider(this).get(DoctorViewModel.class);
        doctorViewModel.deleteAllSchedule();

        if (SharedPreferenceHelper.getInstance(requireContext()).getFlterDocListState()) {
            getDoctorList();
            SharedPreferenceHelper.getInstance(requireContext()).setFlterDocListState(false);
        } else {
            doctorViewModel.getAllFilterDoctor().observe(getViewLifecycleOwner(), new Observer<List<FilteredDoctoredModel>>() {
                @Override
                public void onChanged(List<FilteredDoctoredModel> list) {
                    adapter.setFilteredDoctoredModelList(list);
                    filteredDoctoredModelList = list;
                    dismissProgressbar();

                }
            });
        }

    }


    public void dismissProgressbar() {
        if (adapter.getItemCount() == filteredDoctoredModelList.size()) {
            progressDialog.dismiss();
        }
    }

    private void setUpRecyclerView() {


        adapter = new FilterDoctorRecyclerAdapter(this);
        mBinding.doctorListRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));
        mBinding.doctorListRecyclerview.setAdapter(adapter);


    }


    private void getDoctorList() {

        int id = SharedPreferenceHelper.getInstance(requireContext()).getEmpID();
        String token = SharedPreferenceHelper.getInstance(requireContext()).getToken();
        Call<List<FilteredDoctoredModel>> call = ApiClient.getInstance().getApi().GetFilteredDoctorsByEmployeeId(token, id);

        call.enqueue(new Callback<List<FilteredDoctoredModel>>() {
            @Override
            public void onResponse(Call<List<FilteredDoctoredModel>> call, Response<List<FilteredDoctoredModel>> response) {
                if (response.body() != null) {
                    List<FilteredDoctoredModel> list = response.body();
                    if (list.size() > 0) {
                        doctorViewModel.insertFilterDoctors(list);
                        filteredDoctoredModelList = list;
                        adapter.setFilteredDoctoredModelList(list);
                        dismissProgressbar();

                    }
                } else {
                    new SyncDataToDB(requireActivity().getApplication(), requireContext(),requireActivity()).loginAgain(response.message());
                }
            }

            @Override
            public void onFailure(Call<List<FilteredDoctoredModel>> call, Throwable t) {

                Toast.makeText(requireContext(), "" + t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    private void setClassificationSpinner(List<ClassificationModel> list) {
        int size = 0;
        if (list != null) {
            for (int i = 0; i < (list.size() + 1); i++) {
                if (i == 0) {
                    classificationArray[i] = "All";
                } else {
                    classificationArray[i] = list.get(i - 1).getClassification_Title();

                }
                size = i;
            }

            String[] classificationArrayFilter = new String[size];

            System.arraycopy(classificationArray, 0, classificationArrayFilter, 0, size);

            ArrayAdapter adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, classificationArrayFilter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mBinding.classificationSpinner.setAdapter(adapter);
        }

    }

    private void setGradesSpinner(List<GradingModel> list) {

        int size = 0;

        if (list != null) {
            for (int i = 0; i < (list.size() + 1); i++) {
                if (i == 0) {
                    gradesArray[i] = "All";
                } else {
                    gradesArray[i] = list.get(i - 1).getGrade_Title();

                }
                size = i;
            }
            String[] gradeArrayFilter = new String[size];
            System.arraycopy(gradesArray, 0, gradeArrayFilter, 0, size);

            ArrayAdapter adapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, gradeArrayFilter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mBinding.gradeSpinner.setAdapter(adapter);

        }

    }


    public void btnListener() {


        mBinding.setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String classification = mBinding.classificationSpinner.getSelectedItem().toString();
                String grade = mBinding.gradeSpinner.getSelectedItem().toString();

                doctorViewModel.getAllQueryFilterDoctor(classification, grade).observe(getViewLifecycleOwner(), new Observer<List<FilteredDoctoredModel>>() {
                    @Override
                    public void onChanged(List<FilteredDoctoredModel> filteredDoctoredModels) {
                        adapter.setFilteredDoctoredModelList(filteredDoctoredModels);
                    }
                });

                Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                mBinding.filterLayout.setAnimation(animation);
                mBinding.filterLayout.setVisibility(View.GONE);
                isSlideDown = false;
            }
        });
        mBinding.fabAddDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                navController.navigate(R.id.doctorFormFragment);
            }
        });
    }




    public void setPullToFresh() {
        mBinding.refreshDocLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.refreshDocLayout.setRefreshing(false);
                doctorViewModel.deleteAllFilterDoctor();
                getDoctorList();
                adapter.setFilteredDoctoredModelList(filteredDoctoredModelList);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.filter).setVisible(true);
        menu.findItem(R.id.search).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.filter) {
            if (mBinding.filterLayout.getVisibility() == View.GONE) {
                Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down);
                mBinding.filterLayout.setAnimation(animation);
                mBinding.filterLayout.setVisibility(View.VISIBLE);

            } else {
                Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up);
                mBinding.filterLayout.setAnimation(animation);
                mBinding.filterLayout.setVisibility(View.GONE);
            }
        } else if (item.getItemId() == R.id.search) {


            final SearchView searchView = (SearchView) item.getActionView();

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    return false;

                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);

                    return false;
                }
            });
        }
        return true;
    }
}

