package com.example.ffccloud.Target.Visit;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.SystemClock;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.ffccloud.R;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ProductSliderFragment extends Fragment {
    private ViewPager viewpager;
    private LinearLayout liner;
    private SlideAdapter myadapter;
    TextView chronometer;
    long startTime, timeInMilliseconds = 0;
    Handler customHandler = new Handler();

    /*String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;*/

    private TextView[] mdots;
    TextView pres_btn,btn_stop;
    private Button next,back;

    private int mCureentPage;
    NavController navController;

    public ProductSliderFragment() {
    }

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
        View root = inflater.inflate(R.layout.fragment_product_slider, container, false);

        viewpager=root.findViewById(R.id.viewpager);
        next=root.findViewById(R.id.nextBtn);
        back=root.findViewById(R.id.backBtn);
        liner=root.findViewById(R.id.dots);
        pres_btn = root.findViewById(R.id.pres_btn);
        btn_stop = root.findViewById(R.id.btn_stop);
        chronometer = root.findViewById(R.id.chronometerTimer);
        //linear_choronometer = root.findViewById(R.id.linear_choronometer);
        //chronometer.setBase(SystemClock.elapsedRealtime());
        myadapter=new SlideAdapter(getActivity());
        viewpager.setAdapter(myadapter);
        adddots(0);

        viewpager.addOnPageChangeListener(viewlistener);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage+1);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpager.setCurrentItem(mCureentPage-1);
            }
        });
        /*random = new Random();*/
        pres_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        /*pres_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    pres_btn.setEnabled(false);

                    Toast.makeText(getActivity(), "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }
            }
        });*/

        return root;
    }

    private void stop() {
        customHandler.removeCallbacks(updateTimerThread);
    }

    private void start() {
        startTime = SystemClock.uptimeMillis();
        customHandler.postDelayed(updateTimerThread, 0);
    }

    public static String getDateFromMillis(long d) {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(d);
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            chronometer.setText(getDateFromMillis(timeInMilliseconds));
            customHandler.postDelayed(this, 1000);
        }
    };

    /*public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }*/
    //}

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    public void adddots(int i){

        mdots=new TextView[2];
        liner.removeAllViews();

        for (int x=0;x<mdots.length;x++){

            mdots[x]=new TextView(getActivity());
            mdots[x].setText(Html.fromHtml("&#8226;"));
            mdots[x].setTextSize(35);
            mdots[x].setTextColor(getResources().getColor(R.color.grey));

            liner.addView(mdots[x]);
        }
        if (mdots.length>0){

            mdots[i].setTextColor(getResources().getColor(R.color.white));

        }

    }
    ViewPager.OnPageChangeListener viewlistener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            adddots(position);
            mCureentPage = position;

            if (position==0){
                next.setEnabled(true);
                back.setEnabled(false);
                back.setVisibility(View.INVISIBLE);

                next.setText("NEXT");
                back.setText("");
            }
            else if(position==mdots.length-1){

                next.setEnabled(true);
                back.setEnabled(true);
                back.setVisibility(View.VISIBLE);

                next.setText("FINISH");
                back.setText("BACK");

            }
            else {
                next.setEnabled(true);
                back.setEnabled(true );
                back.setVisibility(View.VISIBLE);

                next.setText("NEXT");
                back.setText("BACK");
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}