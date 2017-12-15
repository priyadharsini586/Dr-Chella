package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.utils.Constants;


public class ProfileFragment extends Fragment implements MoreItemsActivity.OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    WebView webHome;
    View view;
    LinearLayout ldtMain;

    public ProfileFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ldtMain = (LinearLayout) view.findViewById(R.id.ldtMain);
        webHome = (WebView) view.findViewById(R.id.webHome);
        ImageView imgContact = (ImageView) view.findViewById(R.id.imgContact);
        if (mParam2.equals(Constants.profile)){
            ldtMain.setVisibility(View.VISIBLE);
            webHome.loadUrl("file:///android_asset/profile.html");
            imgContact.setVisibility(View.GONE);
        }else if (mParam2.equals(Constants.privacy_policy))
        {
            ldtMain.setVisibility(View.GONE);
            webHome.loadUrl("file:///android_asset/privacy_policy.html");
            ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
            imgContact.setVisibility(View.GONE);
        }else if (mParam2.equals(Constants.terms_and_condition))
        {
            ldtMain.setVisibility(View.GONE);
            webHome.loadUrl("file:///android_asset/terms_conditions.html");
            ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
            imgContact.setVisibility(View.GONE);
        }
        else if (mParam2.equals(Constants.contact))
        {
            ldtMain.setVisibility(View.GONE);
            webHome.setVisibility(View.GONE);
            imgContact.setVisibility(View.VISIBLE);
            ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
        }

        return view;
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



