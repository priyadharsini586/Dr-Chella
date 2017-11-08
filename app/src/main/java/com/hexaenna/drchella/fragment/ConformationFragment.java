package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.RegisterBookDetails;
import com.hexaenna.drchella.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConformationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConformationFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters

    TextView name,age,gender,appMblNum,patientMblNum,place,e_mail_id,address;
    View mainView;

    public ConformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConformationFragment newInstance(String param1, String param2) {
        ConformationFragment fragment = new ConformationFragment();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        if (databaseHandler.getContact("0").equals("English"))
        {
            mainView = inflater.inflate(R.layout.fragment_conformation, container, false);


        }else if (databaseHandler.getContact("0").equals("Tamil"))
        {
            mainView = inflater.inflate(R.layout.tamil_fragment_conformation, container, false);


        }


        RegisterBookDetails registerBookDetails = RegisterBookDetails.getInstance();

        name = (TextView) mainView.findViewById(R.id.name);
        name.setText(registerBookDetails.getName());
        age = (TextView) mainView.findViewById(R.id.age);
        age.setText(registerBookDetails.getAge());

        appMblNum = (TextView) mainView.findViewById(R.id.apllicantNum);
        appMblNum.setText("+91 " +registerBookDetails.getApplicantNumber());

        patientMblNum = (TextView) mainView.findViewById(R.id.patientNum);
        patientMblNum.setText("+91 " +registerBookDetails.getPatientNumber());
        place = (TextView) mainView.findViewById(R.id.place);
        place.setText(registerBookDetails.getPlace());
        e_mail_id = (TextView) mainView.findViewById(R.id.e_mail);
        e_mail_id.setText(registerBookDetails.getE_mailid());
        address = (TextView) mainView.findViewById(R.id.address);
        address.setText(registerBookDetails.getAddress());
        gender = (TextView) mainView.findViewById(R.id.gender);
        gender.setText(registerBookDetails.getGender());

        return mainView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
