package com.hexaenna.drchella.fragment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.TestimonyDetails;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.adapter.TestimonyAdapter;
import com.hexaenna.drchella.animation_file.StaggeredAnimationGroup;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.utils.UtilsClass;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private TestimonyAdapter adapter;
    ListView list_msg;
    Button sendTestimony;
    EditText edtContent;
    NetworkChangeReceiver networkChangeReceiver;
    ApiInterface apiInterface;
    String isConnection = null,send = "";

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

        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
//                if (isConnection == null) {
                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    getNetworkState();
                }

//                }

            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        getActivity().registerReceiver(networkChangeReceiver,
                intentFilter);
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
        adapter = new TestimonyAdapter(getActivity(), R.layout.left_bubble_chat, ChatBubbles);
        list_msg.setAdapter(adapter);
        sendTestimony = (Button) view.findViewById(R.id.sendTestimony);
        edtContent = (EditText) view.findViewById(R.id.edtContent);
        sendTestimony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtContent.getText().toString().trim().equals("")) {
                    Toast.makeText(getActivity(), "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    sendTestimony();

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

    @Override
    public void onStop() {
        super.onStop();
        if (networkChangeReceiver == null)
        {
            Log.e("reg","Do not unregister receiver as it was never registered");
        }
        else
        {
            Log.e("reg","Unregister receiver");
            getActivity().unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
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

    private void displayTestimony()
    {
        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED) && send.equals("")) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                Call<TestimonyDetails> call = apiInterface.getTestimonyDetails();
                call.enqueue(new Callback<TestimonyDetails>() {
                    @Override
                    public void onResponse(Call<TestimonyDetails> call, Response<TestimonyDetails> response) {
                        if (response.isSuccessful()) {
                            TestimonyDetails testimonyDetails = response.body();
                            if (testimonyDetails.getTips() != null) {

                                for (int j = 0; j < testimonyDetails.getTips().size(); j++) {
                                    TestimonyDetails.Tips tips = testimonyDetails.getTips().get(j);
                                    int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                                    int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

                                    TestimonyDetails details = new TestimonyDetails();
                                    details.setContent(tips.getContent());
                                    details.setMyMessage(myMessage);
                                    details.setName(tips.getName());
                                    details.setDate(tips.getDt_time());
                                    details.setProfilePic(tips.getProfile_pic());
                                    details.setEmail(tips.getEmail());
                                    details.setFrom("server");
                                    if (!tips.getTstmny_pic().equals(""))
                                        details.setTestimonyPic(tips.getTstmny_pic());
                                    details.setColorCode(randomAndroidColor);
                                    if (!tips.getContent().equals("") )
                                        ChatBubbles.add(details);

                                }
                                send = "send";
                                Collections.reverse(ChatBubbles);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TestimonyDetails> call, Throwable t) {

                    }
                });
            }
        }
    }
    private void sendTestimony()
    {
        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                apiInterface = ApiClient.getClient().create(ApiInterface.class);
                JSONObject jsonObject = new JSONObject();
                final UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
               final UtilsClass utilsClass = new UtilsClass();
                try {
                    jsonObject.put("user_email",userRegisterDetails.getE_mail());
                    if (testimonyDetails.getImageBitmap() != null) {
                        jsonObject.put("image", utilsClass.BitMapToString(testimonyDetails.getImageBitmap()));
                    }
                    jsonObject.put("content",edtContent.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Call<TestimonyDetails> call = apiInterface.sendTestimony(jsonObject);
                call.enqueue(new Callback<TestimonyDetails>() {
                    @Override
                    public void onResponse(Call<TestimonyDetails> call, Response<TestimonyDetails> response) {
                        if (response.isSuccessful()) {

                            TestimonyDetails testimonyDetail = response.body();
                            if (testimonyDetail.getStatus_code().equals(Constants.status_code1))
                            {
                                int[] androidColors = getResources().getIntArray(R.array.androidcolors);
                                int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];

                                TestimonyDetails details = new TestimonyDetails();

                                details.setContent(edtContent.getText().toString().trim());
                                myMessage = true;
                                details.setMyMessage(myMessage);
                                details.setName(userRegisterDetails.getUserName());
                                details.setFrom("me");

                                DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
                                String[] userDetails =  databaseHandler.getUserName("0");
                                details.setName(userDetails[0]);
                                details.setDate(testimonyDetail.getDate());
                                details.setEmail(userRegisterDetails.getE_mail());
                                details.setProfilePic(userDetails[2]);
                                if (testimonyDetails.getImageBitmap() != null)
                                    details.setTestimonyPic(utilsClass.BitMapToString(testimonyDetails.getImageBitmap()));
                                details.setColorCode(randomAndroidColor);

                                ChatBubbles.add(details);

                                myMessage = false;
                                adapter.notifyDataSetChanged();
                                edtContent.setText("");
                                imgCloseImg.performClick();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TestimonyDetails> call, Throwable t) {

                    }
                });
            }
        }
    }

    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {



            } else if (isConnection.equals(Constants.NETWORK_CONNECTED)) {

                if (send.equals(""))
                {
                    displayTestimony();
                }
            }
        }
    }

}