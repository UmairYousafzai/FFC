package com.example.myapplication.Target.Sample;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.nex3z.notificationbadge.NotificationBadge;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SampleFragment extends Fragment {
    public static TextView scan_result;
    TextView add_btn,series_add_btn,series_scan_btn,series_text_hint1;
    TextView series_view,btn_manual,btn_scan,btn_series;
    RelativeLayout relative_manual_form;
    NavController navController;
    AutoCompleteTextView auto_text_classes,auto_text_subjects;
    Animation animEnterLeft;
    ImageView scan_view,visit_save,btn_history,back_btn,btn_map,close_map;
    NestedScrollView sample_scrolll;
    LinearLayout linear_add_new,linear_history,linear_area11;
    RelativeLayout relative_map,relative_series_view;
    SupportMapFragment mapFragment;

    TextView text_hint1,text_hint2,text_hint3;
    TextInputEditText edit_quantity;
    RelativeLayout linear_scan;
    private String docPhoneNo,docName;
    TextInputEditText edit_series_number;

    LinearLayout linear_history4,linear_history5,linear_history6,linear_history7,linear_history8;
    TextView history_class4,subject_class4,quantity_class4;
    TextView history_class5,subject_class5,quantity_class5;
    TextView history_class6,subject_class6,quantity_class6;
    TextView history_class7,subject_class7,quantity_class7;
    TextView history_class8,subject_class8,quantity_class8;
    int click_count = 0;
    int badge_count = 0;

    NotificationBadge badge;
    private BottomSheetBehavior bottomSheetBehavior;
    LinearLayout linear_call,linear_message,linear_product_details,linear_historyy,linear_find,linear_about;

    ScrollView sample_scroll;
    LinearLayout linearLayout,linear_area22;
    RelativeLayout relative_map2;
    ImageView close_map2;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(31.4805, 74.3239);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("The Pak Angels School"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sample, container, false);
        init(root);

        linearLayout = root.findViewById(R.id.design_bottom_sheet);
        RelativeLayout relativeLayout = root.findViewById(R.id.main_content);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        btn_series.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_series.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_manual.setBackgroundColor(Color.parseColor("#4D689EB8"));
                btn_scan.setBackgroundColor(Color.parseColor("#4D689EB8"));
                btn_series.setTextColor(Color.parseColor("#FFFFFF"));
                btn_manual.setTextColor(Color.parseColor("#000000"));
                btn_scan.setTextColor(Color.parseColor("#000000"));
                /*manual_scroll.setVisibility(View.GONE);
                text_scan.setVisibility(GONE);
                series_scroll.setVisibility(View.VISIBLE);*/
                animEnterLeft = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.enter_from_left2);

                btn_series.startAnimation(animEnterLeft);

                linear_add_new.setVisibility(View.GONE);
                linear_scan.setVisibility(GONE);

                relative_series_view.startAnimation(animEnterLeft);
                relative_series_view.setVisibility(View.VISIBLE);

                linear_history.setVisibility(View.GONE);
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_series.setBackgroundColor(Color.parseColor("#4D689EB8"));
                btn_manual.setBackgroundColor(Color.parseColor("#4D689EB8"));
                btn_scan.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_scan.setTextColor(Color.parseColor("#FFFFFF"));
                btn_series.setTextColor(Color.parseColor("#000000"));
                btn_manual.setTextColor(Color.parseColor("#000000"));

                animEnterLeft = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.enter_from_left2);

                btn_scan.startAnimation(animEnterLeft);

                linear_add_new.setVisibility(View.GONE);
                relative_series_view.setVisibility(GONE);

                linear_scan.startAnimation(animEnterLeft);
                linear_scan.setVisibility(View.VISIBLE);

                linear_history.setVisibility(View.GONE);

                //manual_scroll.setVisibility(GONE);

                /*manual_scroll.setVisibility(GONE);
                text_scan.setVisibility(View.VISIBLE);
                series_scroll.setVisibility(GONE);*/
            }
        });
        btn_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_series.setBackgroundColor(Color.parseColor("#4D689EB8"));
                btn_manual.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_scan.setBackgroundColor(Color.parseColor("#4D689EB8"));
                btn_manual.setTextColor(Color.parseColor("#FFFFFF"));
                btn_series.setTextColor(Color.parseColor("#000000"));
                btn_scan.setTextColor(Color.parseColor("#000000"));

                animEnterLeft = AnimationUtils.loadAnimation(getActivity(),
                        R.anim.enter_from_left2);

                btn_manual.startAnimation(animEnterLeft);
                linear_add_new.startAnimation(animEnterLeft);

                //relative_manual_form.setVisibility(View.VISIBLE);
                linear_add_new.setVisibility(View.VISIBLE);

                linear_scan.setVisibility(GONE);
                linear_history.setVisibility(View.GONE);
                relative_series_view.setVisibility(GONE);
               /* manual_scroll.setVisibility(View.VISIBLE);
                text_scan.setVisibility(GONE);
                series_scroll.setVisibility(GONE);*/
            }
        });
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear_add_new.setVisibility(GONE);
                linear_scan.setVisibility(GONE);
                relative_series_view.setVisibility(GONE);

                linear_history.setAnimation(animEnterLeft);
                linear_history.setVisibility(View.VISIBLE);

            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.target_fragment);
            }
        });

        /*btn_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_scrolll.setVisibility(View.GONE);
                linear_area11.setVisibility(View.VISIBLE);
                relative_map.setVisibility(View.VISIBLE);
            }
        });*/
        close_map2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_scroll.setVisibility(VISIBLE);
                linearLayout.setVisibility(VISIBLE);
                relative_map2.setVisibility(GONE);
                linear_area22.setVisibility(GONE);

            }
        });

        series_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String series_num = edit_series_number.getText().toString();
                series_text_hint1.setVisibility(GONE);
                Drawable img = getContext().getResources().getDrawable(R.drawable.ic_book);
                img.setBounds(0, 0, 60, 60);
                edit_series_number.setCompoundDrawables(null, null, img, null);

                if(series_num.isEmpty()){
                    series_text_hint1.setVisibility(View.VISIBLE);
                    Drawable img3 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img3.setBounds(0, 0, 60, 60);
                    edit_series_number.setCompoundDrawables(null, null, img3, null);
                }

                if(!series_num.isEmpty()){
                    final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                    //pDialog[0].getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Saving...");
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    final Handler[] handler = {new Handler(Looper.getMainLooper())};
                    handler[0].postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.cancel();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Series saved successfully")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.cancel();
                                        }
                                    })
                                    .show();
                        }
                    }, 3000);

                    series_text_hint1.setVisibility(GONE);

                    Drawable img5 = getContext().getResources().getDrawable(R.drawable.ic_book);
                    img5.setBounds(0, 0, 60, 60);
                    edit_series_number.setCompoundDrawables(null, null, img5, null);
                }
            }
        });

        series_scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.Scan);
            }
        });
        scan_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.Scan);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String classes = auto_text_classes.getText().toString();
                String subjects = auto_text_subjects.getText().toString();
                String quantity = edit_quantity.getText().toString();

                text_hint1.setVisibility(GONE);
                text_hint2.setVisibility(GONE);
                text_hint3.setVisibility(GONE);

                Drawable img = getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_down);
                img.setBounds(0, 0, 60, 60);
                auto_text_classes.setCompoundDrawables(null, null, img, null);

                Drawable img2 = getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_down);
                img2.setBounds(0, 0, 60, 60);
                auto_text_subjects.setCompoundDrawables(null, null, img2, null);

                Drawable img7 = getContext().getResources().getDrawable(R.drawable.ic_cart);
                img7.setBounds(0, 0, 60, 60);
                edit_quantity.setCompoundDrawables(null, null, img7, null);

                if(classes.isEmpty()){
                    text_hint1.setVisibility(View.VISIBLE);
                    Drawable img3 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img3.setBounds(0, 0, 60, 60);
                    auto_text_classes.setCompoundDrawables(null, null, img3, null);
                }
                if(subjects.isEmpty()){
                    text_hint2.setVisibility(View.VISIBLE);
                    Drawable img4 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img4.setBounds(0, 0, 60, 60);
                    auto_text_subjects.setCompoundDrawables(null, null, img4, null);
                }
                if(quantity.isEmpty()){
                    text_hint3.setVisibility(View.VISIBLE);
                    Drawable img12 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img12.setBounds(0, 0, 60, 60);
                    edit_quantity.setCompoundDrawables(null, null, img12, null);
                }
                if(!classes.isEmpty() && !subjects.isEmpty() && ! quantity.isEmpty()){
                    final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                    //pDialog[0].getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Saving...");
                    pDialog.setCanceledOnTouchOutside(false);
                    pDialog.setCancelable(false);
                    pDialog.show();

                    badge.setNumber(++badge_count);

                    click_count = click_count+1;
                    if(click_count==1){
                        linear_history4.setVisibility(View.VISIBLE);
                        history_class4.setText(classes);
                        subject_class4.setText(subjects);
                        quantity_class4.setText(quantity);
                    }
                    if(click_count==2){
                        linear_history5.setVisibility(View.VISIBLE);
                        history_class5.setText(classes);
                        subject_class5.setText(subjects);
                        quantity_class5.setText(quantity);
                    }
                    if(click_count==3){
                        linear_history6.setVisibility(View.VISIBLE);
                        history_class6.setText(classes);
                        subject_class6.setText(subjects);
                        quantity_class6.setText(quantity);
                    }
                    if(click_count==4){
                        linear_history7.setVisibility(View.VISIBLE);
                        history_class7.setText(classes);
                        subject_class7.setText(subjects);
                        quantity_class7.setText(quantity);
                    }
                    if(click_count==5){
                        linear_history8.setVisibility(View.VISIBLE);
                        history_class8.setText(classes);
                        subject_class8.setText(subjects);
                        quantity_class8.setText(quantity);
                    }

                    final Handler[] handler = {new Handler(Looper.getMainLooper())};
                    handler[0].postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pDialog.cancel();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Data saved successfully")
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.cancel();
                                        }
                                    })
                                    .show();
                        }
                    }, 3000);

                    text_hint1.setVisibility(GONE);
                    text_hint2.setVisibility(GONE);
                    text_hint3.setVisibility(GONE);

                    Drawable img5 = getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_down);
                    img5.setBounds(0, 0, 60, 60);
                    auto_text_classes.setCompoundDrawables(null, null, img5, null);

                    Drawable img6 = getContext().getResources().getDrawable(R.drawable.ic_arrow_drop_down);
                    img6.setBounds(0, 0, 60, 60);
                    auto_text_subjects.setCompoundDrawables(null, null, img6, null);

                    Drawable img99 = getContext().getResources().getDrawable(R.drawable.ic_cart);
                    img99.setBounds(0, 0, 60, 60);
                    edit_quantity.setCompoundDrawables(null, null, img99, null);
                }

            }
        });

        visit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                //pDialog[0].getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Saving...");
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.setCancelable(false);
                pDialog.show();

                final Handler[] handler = {new Handler(Looper.getMainLooper())};
                handler[0].postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pDialog.cancel();
                        new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Data saved Successfully")
                                .setContentText("Do you want to schedule next activity?")
                                .setCancelText("No")
                                .setConfirmText("Yes")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.cancel();
                                        //navController.navigate(R.id.FormFragment);
                                    }
                                })
                                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        sweetAlertDialog.dismiss();
                                        navController.navigate(R.id.target_fragment);
                                    }
                                })
                                .show();
                    }
                }, 3000);
            }
        });

        String[] classes = new String[]{
                "Play",
                "Nursery",
                "Prep",
                "One",
                "Two",
                "Three",
                "Four",
                "Five",
                "Six",
                "Seven"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, classes);
        auto_text_classes.setAdapter(adapter);

        String[] subjects = new String[]{
                "English",
                "Urdu",
                "Math",
                "Islamayat",
                "S. Study",
                "Gen. Know",
                "Science",
                "Computer",
                "Drawing",
                "Table Book",
                "Rhymes",
                "Dairy",
                "English (Writing)",
                "Urdu (Writing)",
                "Math (Writing)"
        };

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, subjects);
        auto_text_subjects.setAdapter(adapter2);

        linear_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uri number = Uri.parse("tel:" + docPhoneNo);
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                startActivity(callIntent);
            }
        });
        linear_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("smsto:" + Uri.encode(docPhoneNo)));
                //intent.putExtra("sms_body", "Hello " + docName + ":" + "\n Hope You are Good in health");
                startActivity(intent);
            }
        });
        linear_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sample_scroll.setVisibility(GONE);
                linearLayout.setVisibility(GONE);
                relative_map2.setVisibility(VISIBLE);
                linear_area22.setVisibility(VISIBLE);
            }
        });
        linear_historyy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_history);
            }
        });
        linear_product_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_product_info);
            }
        });
        linear_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "About Button", Toast.LENGTH_SHORT).show();
            }
        });


        return root;
    }

    private void init(View root) {
        btn_manual = root.findViewById(R.id.btn_manual);
        btn_scan = root.findViewById(R.id.btn_scan);
        btn_series = root.findViewById(R.id.btn_series);

        btn_history = root.findViewById(R.id.btn_history);

        linear_add_new = root.findViewById(R.id.linear_add_new);

        linear_history = root.findViewById(R.id.linear_history);

        auto_text_classes = root.findViewById(R.id.auto_text_classes);
        auto_text_subjects = root.findViewById(R.id.auto_text_subjects);
        edit_quantity = root.findViewById(R.id.edit_quantity);

        //manual_scroll = root.findViewById(R.id.manual_scroll);

        //relative_manual_form = root.findViewById(R.id.relative_manual_form);

        back_btn = root.findViewById(R.id.back_btn);

        sample_scrolll= root.findViewById(R.id.sample_scrolll);
        btn_map = root.findViewById(R.id.btn_map);

        scan_view = root.findViewById(R.id.scan_view);

        close_map2 = root.findViewById(R.id.close_map2);
        linear_area11 = root.findViewById(R.id.linear_area11);
        relative_map = root.findViewById(R.id.relative_map);

        visit_save = root.findViewById(R.id.visit_save);

        text_hint1 = root.findViewById(R.id.text_hint1);
        text_hint2 = root.findViewById(R.id.text_hint2);
        text_hint3 = root.findViewById(R.id.text_hint3);

        add_btn = root.findViewById(R.id.add_btn);

        //scan_result = root.findViewById(R.id.scan_result);
        linear_scan = root.findViewById(R.id.linear_scan);

        relative_series_view = root.findViewById(R.id.relative_series_view);

        series_add_btn = root.findViewById(R.id.series_add_btn);
        series_scan_btn = root.findViewById(R.id.series_scan_btn);

        edit_series_number = root.findViewById(R.id.edit_series_number);
        series_text_hint1 = root.findViewById(R.id.series_text_hint1);

        linear_history4 = root.findViewById(R.id.linear_history4);
        history_class4 = root.findViewById(R.id.history_class4);
        subject_class4 = root.findViewById(R.id.subject_class4);
        quantity_class4 = root.findViewById(R.id.quantity_class4);

        linear_history5 = root.findViewById(R.id.linear_history5);
        history_class5 = root.findViewById(R.id.history_class5);
        subject_class5 = root.findViewById(R.id.subject_class5);
        quantity_class5 = root.findViewById(R.id.quantity_class5);

        linear_history6 = root.findViewById(R.id.linear_history6);
        history_class6 = root.findViewById(R.id.history_class6);
        subject_class6 = root.findViewById(R.id.subject_class6);
        quantity_class6 = root.findViewById(R.id.quantity_class6);

        linear_history7 = root.findViewById(R.id.linear_history7);
        history_class7 = root.findViewById(R.id.history_class7);
        subject_class7 = root.findViewById(R.id.subject_class7);
        quantity_class7 = root.findViewById(R.id.quantity_class7);

        linear_history8 = root.findViewById(R.id.linear_history8);
        history_class8 = root.findViewById(R.id.history_class8);
        subject_class8 = root.findViewById(R.id.subject_class8);
        quantity_class8 = root.findViewById(R.id.quantity_class8);

        badge = root.findViewById(R.id.badge);

        linear_call = root.findViewById(R.id.linear_call);
        linear_message = root.findViewById(R.id.linear_message);
        linear_product_details = root.findViewById(R.id.linear_product_details);
        linear_historyy = root.findViewById(R.id.linear_historyy);
        linear_find = root.findViewById(R.id.linear_find);
        linear_about = root.findViewById(R.id.linear_about);

        sample_scroll = root.findViewById(R.id.sample_scroll);
        relative_map2 = root.findViewById(R.id.relative_map2);
        linear_area22 = root.findViewById(R.id.linear_area22);

    }
}