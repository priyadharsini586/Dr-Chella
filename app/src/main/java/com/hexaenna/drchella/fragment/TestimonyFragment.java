package com.hexaenna.drchella.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hexaenna.drchella.Model.TestimonyDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.adapter.TestimonyContentAdapter;
import com.hexaenna.drchella.animation_file.StaggeredAnimationGroup;
import com.hexaenna.drchella.animation_file.Utils;
import com.hexaenna.drchella.utils.UtilsClass;
import com.soundcloud.android.crop.Crop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class TestimonyFragment extends Fragment implements MoreItemsActivity.OnBackPressedListener, View.OnClickListener {

    View view;
    LinearLayout ldtMenu;
    ImageView imgSendImage,imgCloseImg;
    boolean isOpen = false;
    LinearLayout ldtCamera,lstGallery;
    public static Activity context;
     StaggeredAnimationGroup group;
    ImageView imgSendImg;
    Bitmap bitmap = null;
    TestimonyDetails testimonyDetails = TestimonyDetails.getInstance();
    RelativeLayout rldSendImage;
    int CAMERA_CODE =100;
    UtilsClass utilsClass;
    Uri  imageUri;
    boolean myMessage = true;
    private List<TestimonyDetails> ChatBubbles;
    private ArrayAdapter<TestimonyDetails> adapter;
    ListView list_msg;
    Button sendTestimony;
    EditText edtContent;
    public static Activity TestimonyFragment()
    {
        return context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_testimony, container, false);
        ldtMenu = (LinearLayout) view.findViewById(R.id.ldtMenu);
        imgSendImage = (ImageView) view.findViewById(R.id.imgSendImage);
        group = view.findViewById(R.id.group);
        group.hide();
        context = getActivity();
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
        utilsClass = new UtilsClass();
        lstGallery = (LinearLayout) view.findViewById(R.id.lstGallery);
        lstGallery.setOnClickListener(this);
        ldtCamera  = (LinearLayout) view.findViewById(R.id.ldtCamera);
        ldtCamera.setOnClickListener(this);

        imgSendImg = (ImageView) view.findViewById(R.id.imgSendImg);
        rldSendImage = (RelativeLayout) view.findViewById(R.id.rldSendImage);
        rldSendImage.setVisibility(View.GONE);
        imgCloseImg = (ImageView) view.findViewById(R.id.imgCloseImg);
        imgCloseImg.setOnClickListener(this);
        ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);

        list_msg = (ListView) view.findViewById(R.id.list_msg);
        ChatBubbles = new ArrayList<>();
        adapter = new TestimonyContentAdapter(getActivity(), R.layout.left_bubble_chat, ChatBubbles);
        list_msg.setAdapter(adapter);
        sendTestimony = (Button) view.findViewById(R.id.sendTestimony);
        edtContent = (EditText) view.findViewById(R.id.edtContent);
        sendTestimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtContent.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    TestimonyDetails ChatBubble = new TestimonyDetails(edtContent.getText().toString(), myMessage);
                    ChatBubbles.add(ChatBubble);
                    adapter.notifyDataSetChanged();
                    edtContent.setText("");
                    if (myMessage) {
                        myMessage = false;
                    } else {
                        myMessage = true;
                    }
                }
            }
        });
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
                group.hide();
                break;
            case R.id.ldtCamera:
                cameraIntent();
                group.hide();
                break;
            case R.id.imgCloseImg:
                rldSendImage.setVisibility(View.GONE);
                testimonyDetails.setImageBitmap(null);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {


            if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
                beginCrop(result.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, result);
            }else if (requestCode == CAMERA_CODE && resultCode == Activity.RESULT_OK)
            {
                if (imageUri != null) {
                    Bitmap thumbnail = null;
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(TestimonyFragment.context.getContentResolver(), imageUri);
                        Uri tempUri = getImageUri(TestimonyFragment.context, thumbnail);
                        beginCrop(tempUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                testimonyDetails.setImageBitmap(thumbnail);
                }

//                imgView.setImageBitmap(thumbnail);

              /*  Bundle extras = result.getExtr  as();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                bitmap = utilsClass.getResizedBitmap(imageBitmap,500,500);
                testimonyDetails.setImageBitmap(bitmap);*/
              /*  Uri tempUri = getImageUri(TestimonyFragment.context, imageBitmap);
                beginCrop(tempUri);*/
            }

    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(TestimonyFragment.context.getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(TestimonyFragment.context);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == Activity.RESULT_OK) {

            Uri imageUri = Crop.getOutput(result);
            setImage(imageUri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(TestimonyFragment.context, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }else
        {
//            ic_profile.setImageBitmap(StringToBitMap(dateResponse.getPhoto()));
        }
    }

    public void setImage(Uri uri)
    {

        try {
            bitmap = MediaStore.Images.Media.getBitmap(TestimonyFragment.context.getContentResolver(), uri);
            UtilsClass utilsClass1 = new UtilsClass();
            bitmap = utilsClass1.getResizedBitmap(bitmap,500,500);
            testimonyDetails.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onResume() {
        super.onResume();

            if (testimonyDetails.getImageBitmap() != null) {
                imgSendImg.setImageBitmap(testimonyDetails.getImageBitmap());
                rldSendImage.setVisibility(View.VISIBLE);
                Log.e("onResume called", "on resume called in frag");
            }


    }
    private void cameraIntent()
    {
       ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = TestimonyFragment.context.getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_CODE);

       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CODE);*/
    }



}