package com.hexaenna.drchella.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.animation_file.StaggeredAnimationGroup;
import com.soundcloud.android.crop.Crop;

import java.io.File;


public class TestimonyFragment extends Fragment implements MoreItemsActivity.OnBackPressedListener, View.OnClickListener {

    View view;
    LinearLayout ldtMenu;
    ImageView imgSendImage;
    boolean isOpen = false;
    LinearLayout ldtCamera,lstGallery;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testimony, container, false);
        ldtMenu = (LinearLayout) view.findViewById(R.id.ldtMenu);
        imgSendImage = (ImageView) view.findViewById(R.id.imgSendImage);
        final StaggeredAnimationGroup group = view.findViewById(R.id.group);
        group.hide();
        imgSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    group.hide();
                    isOpen = false;
                } else {
                    group.show();
                    isOpen = true;
                }
            }
        });
        lstGallery = (LinearLayout) view.findViewById(R.id.lstGallery);
        lstGallery.setOnClickListener(this);
        ldtCamera  = (LinearLayout) view.findViewById(R.id.ldtCamera);
        ldtCamera.setOnClickListener(this);

        ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
        return view;
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.lstGallery:
                Crop.pickImage((MoreItemsActivity)getActivity());
                break;
            case R.id.ldtCamera:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        Log.e("result called","result called");

        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(getActivity());
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {

            Uri imageUri = Crop.getOutput(result);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(getActivity(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }else
        {
//            ic_profile.setImageBitmap(StringToBitMap(dateResponse.getPhoto()));
        }
    }
}