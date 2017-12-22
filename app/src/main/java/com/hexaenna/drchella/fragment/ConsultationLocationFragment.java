package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;

import java.util.ArrayList;
import java.util.Random;


public class ConsultationLocationFragment extends Fragment implements MoreItemsActivity.OnBackPressedListener {

    View view;
    ArrayList<String> hospitalName,hospitalAddress,hospitalNum;
    LinearLayout mainView;
    TextView txtHospitalName,txtHospitalAddress,txtHospitalNum,txtHospitalName_content;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_consultation_location, container, false);
        mainView = (LinearLayout) view.findViewById(R.id.ldtMainView);
        getHospitalName();
        getHospitalAddress();
        getHospitalNum();
        for (int i=0 ; i < getHospitalName().size() ;i++)
        {
            LinearLayout myLayout = (LinearLayout)mainView.findViewById(R.id.ldtLocationView);
            int[] androidColors = getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            View consulation = getLayoutInflater(null).inflate(R.layout.fragment_consultation_location, myLayout, false);
            txtHospitalName = (TextView) consulation.findViewById(R.id.txtHospitalName);
            txtHospitalAddress = (TextView) consulation.findViewById(R.id.txtHospitalAddress);
            txtHospitalNum = (TextView) consulation.findViewById(R.id.txtHospitalNum);
            txtHospitalName_content = (TextView) consulation.findViewById(R.id.txtHospitalName_content);
            if (getHospitalName().size()-1 == i)
            {
                txtHospitalName_content.setVisibility(View.VISIBLE);
                txtHospitalName_content.setText("(Peripheral Center)");
            }
            txtHospitalName.setText(hospitalName.get(i));
            txtHospitalName.setTextColor(randomAndroidColor);
            txtHospitalAddress.setText(hospitalAddress.get(i));
            txtHospitalNum.setText(hospitalNum.get(i));

            mainView.addView(consulation);

        }
        ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
        return view;
    }


    public ArrayList getHospitalNum()
    {
        hospitalNum = new ArrayList<>();
        hospitalNum.add(getActivity().getResources().getString(R.string.erode_hospital_Phone));
        hospitalNum.add(getActivity().getResources().getString(R.string.chennai_hospital_Phone));
        hospitalNum.add(getActivity().getResources().getString(R.string.coimbatore_hospital_Phone));
        hospitalNum.add(getActivity().getResources().getString(R.string.namakkal_hospital_Phone));
        hospitalNum.add(getActivity().getResources().getString(R.string.may_hospital_Phone));
        hospitalNum.add(getActivity().getResources().getString(R.string.kollidam_hospital_Phone));
        return hospitalNum;
    }
    public ArrayList getHospitalName()
    {
        hospitalName = new ArrayList<>();
        hospitalName.add(getActivity().getResources().getString(R.string.erode_hospital));
        hospitalName.add(getActivity().getResources().getString(R.string.chennai_hospital));
        hospitalName.add(getActivity().getResources().getString(R.string.coimbatore_hospital));
        hospitalName.add(getActivity().getResources().getString(R.string.namakkal_hospital));
        hospitalName.add(getActivity().getResources().getString(R.string.mayiladu_hospital));
        hospitalName.add(getActivity().getResources().getString(R.string.kollidam_hospital));
        return hospitalName;
    }

    public ArrayList getHospitalAddress()
    {
        hospitalAddress = new ArrayList<>();
        hospitalAddress.add(getActivity().getResources().getString(R.string.erode_hospital_address));
        hospitalAddress.add(getActivity().getResources().getString(R.string.chennai_hospital_address));
        hospitalAddress.add(getActivity().getResources().getString(R.string.coimbatore_hospital_address));
        hospitalAddress.add(getActivity().getResources().getString(R.string.namakkal_hospital_address));
        hospitalAddress.add(getActivity().getResources().getString(R.string.may_hospital_address));
        hospitalAddress.add(getActivity().getResources().getString(R.string.kollidam_hospital_address));
        return hospitalAddress;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onBackPressed() {
        getActivity().finish();
    }
}
