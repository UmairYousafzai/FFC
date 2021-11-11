package com.example.ffccloud.Target.FollowUp;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
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
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ffccloud.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class FollowUpFragment extends Fragment {
    NavController navController;
    AutoCompleteTextView auto_stage, auto_status, auto_interest_level;
    TextView visit_save,btn_followup, btn_visit, btn_sample, btn_recovery,btn_manual,btn_scan,btn_series;
    ImageView back_btn,btn_map,close_map;
    NestedScrollView visit_scrolll;
    Animation animEnterLeft,animEnterRight;
    RelativeLayout relative_map;
    SupportMapFragment mapFragment;

    Dialog mydialog;

    TextInputEditText edit_text_views_of_principal,edit_text_subjects_wants_to_change,edit_text_person_in_contact,edit_text_person_in_contact_phone,edit_text_remarks,edit_text_purpose;
    TextInputLayout text_views_of_principal;
    TextView text_hint1,text_hint2,text_hint3,text_hint4,text_hint5,text_hint6;

    TextView btn_interest_high,btn_interest_medium, btn_interest_low;

    private BottomSheetBehavior bottomSheetBehavior;

    private final int REQ_CODE_SPEECH_INPUT = 100;
    ImageView voice_record_btn;

    LinearLayout linear_call,linear_message,linear_product_details,linear_history,linear_find,linear_about;
    private String docPhoneNo,docName;

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
            googleMap.addMarker(new MarkerOptions().position(sydney).title("The Educators School"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        mydialog = new Dialog(getActivity(), android.R.style.Theme_Light);
        mydialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);

        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_follow_up, container, false);

        init(root);

        final LinearLayout linearLayout = root.findViewById(R.id.design_bottom_sheet);
        final RelativeLayout relativeLayout = root.findViewById(R.id.main_content);

        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);


        String[] stage = new String[]{
                "Suspect",
                "Prospect",
                "Completed",
                "Canceled"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, stage);
        auto_stage.setAdapter(adapter);

        String[] status = new String[]{
                "Success",
                "Cancel",
                "Pending"
        };

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, status);
        auto_status.setAdapter(adapter1);

        visit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String principal_reviews = edit_text_views_of_principal.getText().toString();
                String subjects_change = edit_text_subjects_wants_to_change.getText().toString();
                String person_in_contact = edit_text_person_in_contact.getText().toString();
                String person_in_contact_phone = edit_text_person_in_contact_phone.getText().toString();
                String remarks = edit_text_remarks.getText().toString();
                String purpose = edit_text_purpose.getText().toString();

                text_hint1.setVisibility(GONE);
                text_hint2.setVisibility(GONE);
                text_hint3.setVisibility(GONE);
                text_hint4.setVisibility(GONE);
                text_hint5.setVisibility(GONE);
                text_hint6.setVisibility(GONE);

                Drawable img = getContext().getResources().getDrawable(R.drawable.ic_man);
                img.setBounds(0, 0, 60, 60);
                edit_text_views_of_principal.setCompoundDrawables(null, null, img, null);

                Drawable img2 = getContext().getResources().getDrawable(R.drawable.ic_product_info);
                img2.setBounds(0, 0, 60, 60);
                edit_text_subjects_wants_to_change.setCompoundDrawables(null, null, img2, null);

                Drawable img7 = getContext().getResources().getDrawable(R.drawable.ic_men_circle);
                img7.setBounds(0, 0, 60, 60);
                edit_text_person_in_contact.setCompoundDrawables(null, null, img7, null);

                Drawable img8 = getContext().getResources().getDrawable(R.drawable.ic_calll);
                img8.setBounds(0, 0, 60, 60);
                edit_text_person_in_contact_phone.setCompoundDrawables(null, null, img8, null);

                Drawable img9 = getContext().getResources().getDrawable(R.drawable.ic_star_rate);
                img9.setBounds(0, 0, 60, 60);
                edit_text_remarks.setCompoundDrawables(null, null, img9, null);

                Drawable img10 = getContext().getResources().getDrawable(R.drawable.ic_text_snippet);
                img10.setBounds(0, 0, 60, 60);
                edit_text_purpose.setCompoundDrawables(null, null, img10, null);

                if(principal_reviews.isEmpty() || principal_reviews.equals(null)){
                    text_hint1.setVisibility(View.VISIBLE);
                    Drawable img3 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img3.setBounds(0, 0, 60, 60);
                    edit_text_views_of_principal.setCompoundDrawables(null, null, img3, null);
                }
                if(subjects_change.isEmpty() || subjects_change.equals(null)){
                    text_hint2.setVisibility(View.VISIBLE);
                    Drawable img4 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img4.setBounds(0, 0, 60, 60);
                    edit_text_subjects_wants_to_change.setCompoundDrawables(null, null, img4, null);
                }
                if(person_in_contact.isEmpty() || person_in_contact.equals(null)){
                    text_hint3.setVisibility(View.VISIBLE);
                    Drawable img88 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img88.setBounds(0, 0, 60, 60);
                    edit_text_person_in_contact.setCompoundDrawables(null, null, img88, null);
                }
                if(person_in_contact_phone.isEmpty() || person_in_contact_phone.equals(null)){
                    text_hint4.setVisibility(View.VISIBLE);
                    Drawable img11 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img11.setBounds(0, 0, 60, 60);
                    edit_text_person_in_contact_phone.setCompoundDrawables(null, null, img11, null);
                }
                if(remarks.isEmpty() || remarks.equals(null)){
                    text_hint5.setVisibility(View.VISIBLE);
                    Drawable img12 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img12.setBounds(0, 0, 60, 60);
                    edit_text_remarks.setCompoundDrawables(null, null, img12, null);
                }
                if(purpose.isEmpty() || purpose.equals(null)){
                    text_hint6.setVisibility(View.VISIBLE);
                    Drawable img13 = getContext().getResources().getDrawable(R.drawable.ic_baseline_error_outline_24);
                    img13.setBounds(0, 0, 60, 60);
                    edit_text_purpose.setCompoundDrawables(null, null, img13, null);
                }
                if(!principal_reviews.isEmpty() && !subjects_change.isEmpty() && !person_in_contact.isEmpty() && !person_in_contact_phone.isEmpty() && !remarks.isEmpty() && !purpose.isEmpty()){

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
                    }, 4000);

                    text_hint1.setVisibility(GONE);
                    text_hint2.setVisibility(GONE);
                    text_hint3.setVisibility(GONE);
                    text_hint4.setVisibility(GONE);
                    text_hint5.setVisibility(GONE);
                    text_hint6.setVisibility(GONE);

                    Drawable img5 = getContext().getResources().getDrawable(R.drawable.ic_man);
                    img5.setBounds(0, 0, 60, 60);
                    edit_text_views_of_principal.setCompoundDrawables(null, null, img5, null);

                    Drawable img6 = getContext().getResources().getDrawable(R.drawable.ic_product_info);
                    img6.setBounds(0, 0, 60, 60);
                    edit_text_subjects_wants_to_change.setCompoundDrawables(null, null, img6, null);

                    Drawable img99 = getContext().getResources().getDrawable(R.drawable.ic_men_circle);
                    img99.setBounds(0, 0, 60, 60);
                    edit_text_person_in_contact.setCompoundDrawables(null, null, img99, null);

                    Drawable img14 = getContext().getResources().getDrawable(R.drawable.ic_calll);
                    img14.setBounds(0, 0, 60, 60);
                    edit_text_person_in_contact_phone.setCompoundDrawables(null, null, img14, null);

                    Drawable img15 = getContext().getResources().getDrawable(R.drawable.ic_star_rate);
                    img15.setBounds(0, 0, 60, 60);
                    edit_text_remarks.setCompoundDrawables(null, null, img15, null);

                    Drawable img16 = getContext().getResources().getDrawable(R.drawable.ic_text_snippet);
                    img16.setBounds(0, 0, 60, 60);
                    edit_text_purpose.setCompoundDrawables(null, null, img16, null);
                }
            }
        });

        btn_interest_high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_interest_high.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_interest_medium.setBackgroundColor(Color.parseColor("#4D689EB8"));
                btn_interest_low.setBackgroundColor(Color.parseColor("#4D689EB8"));

                btn_interest_high.setTextColor(Color.parseColor("#FFFFFF"));
                btn_interest_medium.setTextColor(Color.parseColor("#000000"));
                btn_interest_low.setTextColor(Color.parseColor("#000000"));
            }
        });

        btn_interest_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_interest_high.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_interest_medium.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_interest_low.setBackgroundColor(Color.parseColor("#4D689EB8"));

                btn_interest_high.setTextColor(Color.parseColor("#FFFFFF"));
                btn_interest_medium.setTextColor(Color.parseColor("#FFFFFF"));
                btn_interest_low.setTextColor(Color.parseColor("#000000"));
            }
        });

        btn_interest_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_interest_high.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_interest_medium.setBackgroundColor(Color.parseColor("#689EB8"));
                btn_interest_low.setBackgroundColor(Color.parseColor("#689EB8"));

                btn_interest_high.setTextColor(Color.parseColor("#FFFFFF"));
                btn_interest_medium.setTextColor(Color.parseColor("#FFFFFF"));
                btn_interest_low.setTextColor(Color.parseColor("#FFFFFF"));
            }
        });

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
        /*linear_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.nav_google_map);
            }
        });*/
        linear_history.setOnClickListener(new View.OnClickListener() {
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

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.target_fragment);
            }
        });

        voice_record_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //speak();
                promptSpeechInput();
            }
        });

        return root;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    edit_text_views_of_principal.setText(result.get(0));
                }
                break;
            }

        }
    }
    private void init(View root) {
        /*btn_manual = root.findViewById(R.id.btn_manual);
        btn_scan = root.findViewById(R.id.btn_scan);
        btn_series = root.findViewById(R.id.btn_series);*/

        back_btn = root.findViewById(R.id.back_btn);
        auto_stage = root.findViewById(R.id.auto_stage);
        auto_status = root.findViewById(R.id.auto_status);
        //auto_interest_level = root.findViewById(R.id.auto_interest_level);

        visit_scrolll= root.findViewById(R.id.visit_scrolll);
        btn_map = root.findViewById(R.id.btn_map);

        //btn_visit = root.findViewById(R.id.btn_visit);
/*        btn_followup = root.findViewById(R.id.btn_followup);
        btn_sample = root.findViewById(R.id.btn_sample);
        btn_recovery = root.findViewById(R.id.btn_recovery);*/
        visit_save = root.findViewById(R.id.visit_save);

        //linear_layout_btn_visit_followup = root.findViewById(R.id.linear_layout_btn_visit_followup);
        //linear_layout_btn_sample_recovery = root.findViewById(R.id.linear_layout_btn_sample_recovery);

        close_map = root.findViewById(R.id.close_map);
        //linear_area11 = root.findViewById(R.id.linear_area11);
        relative_map = root.findViewById(R.id.relative_map);

        text_views_of_principal = root.findViewById(R.id.text_views_of_principal);
        edit_text_views_of_principal = root.findViewById(R.id.edit_text_views_of_principal);
        edit_text_subjects_wants_to_change = root.findViewById(R.id.edit_text_subjects_wants_to_change);
        edit_text_person_in_contact = root.findViewById(R.id.edit_text_person_in_contact);
        edit_text_person_in_contact_phone = root.findViewById(R.id.edit_text_person_in_contact_phone);
        edit_text_remarks = root.findViewById(R.id.edit_text_remarks);
        edit_text_purpose = root.findViewById(R.id.edit_text_purpose);

        text_hint1 = root.findViewById(R.id.text_hint1);
        text_hint2 = root.findViewById(R.id.text_hint2);
        text_hint3 = root.findViewById(R.id.text_hint3);
        text_hint4 = root.findViewById(R.id.text_hint4);
        text_hint5 = root.findViewById(R.id.text_hint5);
        text_hint6 = root.findViewById(R.id.text_hint6);

        btn_interest_high = root.findViewById(R.id.btn_interest_high);
        btn_interest_medium = root.findViewById(R.id.btn_interest_medium);
        btn_interest_low = root.findViewById(R.id.btn_interest_low);

        voice_record_btn = root.findViewById(R.id.voice_record_btn);

        linear_call = root.findViewById(R.id.linear_call);
        linear_message = root.findViewById(R.id.linear_message);
        linear_product_details = root.findViewById(R.id.linear_product_details);
        linear_history = root.findViewById(R.id.linear_history);
        linear_find = root.findViewById(R.id.linear_find);
        linear_about = root.findViewById(R.id.linear_about);

    }
}