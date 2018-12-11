package com.dof.jaeseonlee.patient;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1_CARENAME = "param1";
    private static final String ARG_PARAM2_CAREPHONENUM = "param2";
    private static final String ARG_PARAM3_PATIENTFAMILYNAME = "param3";
    private static final String ARG_PARAM4_PATIENTGIVENNAME = "param4";
    private static final String ARG_PARAM5_PATIENTGENDER = "param5";
    private static final String ARG_PARAM6_PATIENTBIRTHDATE = "param6";
    private static final String ARG_PARAM7_PATIENTPHONENUMBER = "param7";

    public View view = null;

    // TODO: Rename and change types of parameters
    public static int hrm = 0;
    public static String careName;
    public static String carePhoneNum;
    public static String patientFamilyName;
    public static String patientGivenName;
    public static String patientGender;
    public static String patientBirthDate;
    private static String patientPhoneNumber;

    private TextView hrmTextView;
    private ImageView statusImageView;
    private TextView nameTextView;
    private TextView genderTextView;
    private TextView birthDateTextView;
    private TextView careNameTextView;
    private TextView carePhoneNumTextView;
    private Button deviceConnectionButton;
    private Button deviceCheckHRMButton;
    private Button callCarePersonButton;


    private Context context;


    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String patientFamilyName, String patientGivenName, String patientGender, String patientBirthDate, String careName, String carePhoneNum, String patientPhoneNumber) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1_CARENAME, careName);
        args.putString(ARG_PARAM2_CAREPHONENUM, carePhoneNum);
        args.putString(ARG_PARAM3_PATIENTFAMILYNAME, patientFamilyName);
        args.putString(ARG_PARAM4_PATIENTGIVENNAME, patientGivenName);
        args.putString(ARG_PARAM5_PATIENTGENDER, patientGender);
        args.putString(ARG_PARAM6_PATIENTBIRTHDATE, patientBirthDate);
        args.putString(ARG_PARAM7_PATIENTPHONENUMBER, patientPhoneNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            careName = getArguments().getString(ARG_PARAM1_CARENAME);
            carePhoneNum = getArguments().getString(ARG_PARAM2_CAREPHONENUM);
            patientFamilyName = getArguments().getString(ARG_PARAM3_PATIENTFAMILYNAME);
            patientGivenName = getArguments().getString(ARG_PARAM4_PATIENTGIVENNAME);
            patientGender = getArguments().getString(ARG_PARAM5_PATIENTGENDER);
            patientBirthDate = getArguments().getString(ARG_PARAM6_PATIENTBIRTHDATE);
            patientPhoneNumber = getArguments().getString(ARG_PARAM7_PATIENTPHONENUMBER);



        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();
        nameTextView = (TextView)view.findViewById(R.id.homeName);
        genderTextView = (TextView)view.findViewById(R.id.homeGender);
        birthDateTextView = (TextView)view.findViewById(R.id.homeBirthDate);
        careNameTextView = (TextView)view.findViewById(R.id.homeCareName);
        carePhoneNumTextView = (TextView)view.findViewById(R.id.homeCarePhoneNumView);
        hrmTextView = (TextView)view.findViewById(R.id.homeHRMView);
        statusImageView = (ImageView)view.findViewById(R.id.homeStatusPicture);

        deviceConnectionButton = (Button)view.findViewById(R.id.homeBluetoothDeviceConnectionButton);
        deviceCheckHRMButton = (Button)view.findViewById(R.id.homeHRMCheckButton);
        callCarePersonButton = (Button)view.findViewById(R.id.homeCallButton);

        deviceConnectionButton.setOnClickListener(this);
        deviceCheckHRMButton.setOnClickListener(this);
        callCarePersonButton.setOnClickListener(this);

        changeHomeTextView();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.homeBluetoothDeviceConnectionButton:
                new BluetoothCommunicate(context,view).execute(1);
                break;

            case R.id.homeHRMCheckButton:
                new BluetoothCommunicate(context,view).execute(0);
                String hrmStringTemp = null;
                hrmStringTemp = hrmTextView.getText().toString();
                try{
                    hrm = Integer.parseInt(hrmStringTemp);
                }catch (Exception e){
                    Log.e("HomeFragment hrmString -> int",e.toString());
                }

                try {
                    Date tempBirthDate = new SimpleDateFormat("yyyy-MM-dd").parse(patientBirthDate);
                    Boolean isMan = false;
                    if(patientGender.equals("남")) isMan = true;
                    new DataToFhirResources(patientFamilyName,patientGivenName,patientPhoneNumber,isMan,tempBirthDate,hrm);
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                break;

            case R.id.homeCallButton:
                Log.e("onClickCallButton", carePhoneNum);
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+carePhoneNum));
                startActivity(intent);
                break;
        }
    }

    public void changeHomeTextView(){
        hrmTextView.setText(Integer.toString(hrm));
        nameTextView.setText(patientFamilyName + patientGivenName);
        genderTextView.setText(patientGender);
        birthDateTextView.setText(patientBirthDate);
        careNameTextView.setText(careName);
        carePhoneNumTextView.setText(carePhoneNum);

        if (hrm > 50 && hrm < 100) {
            statusImageView.setImageResource(R.drawable.normal_big);
        }
        else if (hrm == 0  ) {
            statusImageView.setImageResource(R.drawable.none);
        }
        else{
            statusImageView.setImageResource(R.drawable.danger_big);
        }
    }




}
