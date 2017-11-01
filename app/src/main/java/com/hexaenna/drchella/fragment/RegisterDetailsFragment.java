package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import com.hexaenna.drchella.R;

public class RegisterDetailsFragment extends Fragment {

    View mainView;
    MaterialBetterSpinner spnSirName;
    public RegisterDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_register_details, container, false);
        spnSirName = (MaterialBetterSpinner) mainView.findViewById(R.id.surname);
        String[] SPINNERLIST = {"Mr", "Mrs", "Ms", "Dr","Er"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.city_list_items, SPINNERLIST);
        spnSirName.setAdapter(arrayAdapter);
        return mainView;
    }

    // TODO: Rename method, update argument and hook method into UI event

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}

