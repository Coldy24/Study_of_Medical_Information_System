package com.dof.jaeseonlee.patient;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ca.uhn.fhir.util.UrlUtil;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1_CARENAME = "param1";
    private static final String ARG_PARAM2_CAREPHONENUM = "param2";
    private static final String ARG_PARAM3_PATIENTNAME = "param3";
    private static final String ARG_PARAM4_PATIENTGENDER = "param4";
    private static final String ARG_PARAM5_PATIENTBIRTHDATE = "param5";


    // TODO: Rename and change types of parameters
    public static String careName;
    public static String carePhoneNum;
    public static String patientName;
    public static String patientGender;
    public static String patientBirthDate;

    private TextView nameTextView;
    private TextView genderTextView;
    private TextView birthDateTextView;
    private TextView careNameTextView;
    private TextView carePhoneNumTextView;



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
    public static HomeFragment newInstance(String patientName, String patientGender, String patientBirthDate, String careName, String carePhoneNum) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1_CARENAME, careName);
        args.putString(ARG_PARAM2_CAREPHONENUM, carePhoneNum);
        args.putString(ARG_PARAM3_PATIENTNAME, patientName);
        args.putString(ARG_PARAM4_PATIENTGENDER, patientGender);
        args.putString(ARG_PARAM5_PATIENTBIRTHDATE, patientBirthDate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            careName = getArguments().getString(ARG_PARAM1_CARENAME);
            carePhoneNum = getArguments().getString(ARG_PARAM2_CAREPHONENUM);
            patientName = getArguments().getString(ARG_PARAM3_PATIENTNAME);
            patientGender = getArguments().getString(ARG_PARAM4_PATIENTGENDER);
            patientBirthDate = getArguments().getString(ARG_PARAM5_PATIENTBIRTHDATE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        nameTextView = (TextView)view.findViewById(R.id.homeName);
        genderTextView = (TextView)view.findViewById(R.id.homeGender);
        birthDateTextView = (TextView)view.findViewById(R.id.homeBirthDate);
        careNameTextView = (TextView)view.findViewById(R.id.homeCareName);
        carePhoneNumTextView = (TextView)view.findViewById(R.id.homeCarePhoneNumView);

        changeHomeTextView();
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.homeBluetoothDeviceConnectionButton:

                break;

            case R.id.homeHRMCheckButton:

                break;

            case R.id.homeCallButton:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+carePhoneNum));
                startActivity(intent);
                break;
        }
    }

    public void changeHomeTextView(){

        nameTextView.setText(patientName);
        genderTextView.setText(patientGender);
        birthDateTextView.setText(patientBirthDate);
        careNameTextView.setText(careName);
        carePhoneNumTextView.setText(carePhoneNum);
    }

}
