package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.BookAppointmentActivity;

import fr.ganfra.materialspinner.MaterialSpinner;

public class RegisterDetailsFragment extends Fragment {

    View mainView;
    MaterialSpinner spnSirName;
    Toolbar mToolbar;
    LinearLayout ldtCity,ldtPreviosFragment;
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

        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        if (databaseHandler.getContact("0").equals("English"))
        {
            mainView = inflater.inflate(R.layout.fragment_register_details, container, false);


        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            mainView = inflater.inflate(R.layout.tamil_fragment_register_details, container, false);

        }

        spnSirName = (MaterialSpinner) mainView.findViewById(R.id.surname);
        String[] SPINNERLIST = {"Mr", "Mrs", "Ms", "Dr","Er"};

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.city_list_items, SPINNERLIST);
        spnSirName.setAdapter(arrayAdapter);
        spnSirName.setSelection(1);
        spnSirName.setPaddingSafe(5,5,15,5);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        ldtCity = (LinearLayout) mToolbar.findViewById(R.id.ldtCity);
        ldtCity.setVisibility(View.INVISIBLE);

        ldtPreviosFragment = (LinearLayout) mainView.findViewById(R.id.ldtPreviosFragment);
        ldtPreviosFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.fragment_container, new DateAndTimeFragment(), "DATE_AND_TIME_FRAGMENT");
                fragmentTransaction.addToBackStack("DATE_AND_TIME_FRAGMENT");
                fragmentTransaction.commit();
                BookAppointmentActivity.ldtBookingDetails.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                BookAppointmentActivity.txtBooking.setTextColor(getActivity().getResources().getColor(R.color.black));
            }
        });

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

