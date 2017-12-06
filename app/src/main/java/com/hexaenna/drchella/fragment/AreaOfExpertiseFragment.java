package com.hexaenna.drchella.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.hexaenna.drchella.R;

public class AreaOfExpertiseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_area_of_expertise_fragment, container, false);
        WebView webHome = (WebView) rootView.findViewById(R.id.webHome);
        webHome.loadUrl("file:///android_asset/expertise.html");
        return rootView;
    }
}
