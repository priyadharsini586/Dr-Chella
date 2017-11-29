package com.hexaenna.drchella.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.Model.UserRegisterDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.adapter.SectionsPagerAdapter;
import com.hexaenna.drchella.api.ApiClient;
import com.hexaenna.drchella.api.ApiInterface;
import com.hexaenna.drchella.fragment.AreaOfExpertiseFragment;
import com.hexaenna.drchella.fragment.DrTalksActivity;
import com.hexaenna.drchella.fragment.HomeFragment;
import com.hexaenna.drchella.fragment.MoreFragment;
import com.hexaenna.drchella.fragment.ProfileFragment;
import com.hexaenna.drchella.utils.Config;
import com.hexaenna.drchella.utils.Constants;
import com.hexaenna.drchella.service.NetworkChangeReceiver;
import com.hexaenna.drchella.utils.LoadImageTask;
import com.hexaenna.drchella.utils.NotificationUtils;
import com.hexaenna.drchella.utils.UtilsClass;
import com.soundcloud.android.crop.Crop;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hexaenna.drchella.utils.UtilsClass.setBadgeCount;

public class HomeActivity extends AppCompatActivity implements  LoadImageTask.Listener  {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    TextView txtName,txtMobileNumber;
    ImageView ic_profile;
    ApiInterface apiInterface;
    LinearLayout ldtCircle;
    String isConnection = null;
    TSnackbar snackbar;
    NetworkChangeReceiver networkChangeReceiver;
    View snackbarView;
    CoordinatorLayout main_content;
    ProgressBar progress_app;
    ImageView imgNext;
    Animation animSlide;
    TimeAndDateResponse dateResponse = new TimeAndDateResponse();
     BroadcastReceiver mRegistrationBroadcastReceiver;
    String count = "0";
    int imgCount = 0;
    Menu menuNotification;
    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        networkChangeReceiver = new NetworkChangeReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                if (isConnection == null) {
                    Bundle b = intent.getExtras();
                    isConnection = b.getString(Constants.MESSAGE);
                    Log.e("newmesage from activiyt", "" + isConnection);
                    getNetworkState();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Constants.BROADCAST);
        this.registerReceiver(networkChangeReceiver,
                intentFilter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        databaseHandler = new DatabaseHandler(getApplicationContext());
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),HomeActivity.this,"home");

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager();
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),BookAppointmentActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(mSectionsPagerAdapter.getTabView(i));
        }
        tabLayout.getTabAt(0).getCustomView().setSelected(true);
        tabLayout.addOnTabSelectedListener(OnTabSelectedListener);
        mSectionsPagerAdapter.SetOnSelectView(tabLayout,0);
        txtMobileNumber = (TextView) findViewById(R.id.txtMobileNumber);
        txtName = (TextView) findViewById(R.id.txtName);

        ic_profile=(ImageView) findViewById(R.id.ic_profile);
        ldtCircle = (LinearLayout) findViewById(R.id.ldtCircle);
        ldtCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnection != null) {
                    ic_profile.setImageDrawable(null);
                    Crop.pickImage(HomeActivity.this);
                    Log.e("connection",isConnection);
                }else
                {
                    Log.e("connection","" + isConnection);
                }
            }
        });


        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        String[] userDetails =  databaseHandler.getUserName("0");
        txtMobileNumber.setText(userDetails[1]);
        txtName.setText(userDetails[0]);
        UtilsClass utilsClass = new UtilsClass();
        Bitmap bitmap = utilsClass.StringToBitMap(userDetails[2]);
        if (bitmap != null)
            ic_profile.setImageBitmap(bitmap);

        progress_app = (ProgressBar) findViewById(R.id.progress_app);
        progress_app.setVisibility(View.GONE);
        main_content = (CoordinatorLayout) findViewById(R.id.main_content);


        imgNext = (ImageView) findViewById(R.id.imgNext);

// Load the animation like this
        animSlide = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide);

// Start the animation like this
        animSlide.setRepeatCount(Animation.INFINITE);
        imgNext.startAnimation(animSlide);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);



                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    Log.e("notification count", String.valueOf(imgCount));
                    String message = intent.getStringExtra("message");
                    imgCount = imgCount + 1;
                    Log.e("notification aftercount", String.valueOf(imgCount));
                    count = String.valueOf(imgCount);
                    MenuItem itemCart = menuNotification.findItem(R.id.action_cart);
                    LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
                    setBadgeCount(context, icon, count);
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();


                }
            }
        };

    }


    private void setupViewPager() {
        mSectionsPagerAdapter.addFrag(new HomeFragment(), "Home");
        mSectionsPagerAdapter.addFrag(new ProfileFragment(), "Dr Profile");
        mSectionsPagerAdapter.addFrag(new DrTalksActivity(), "Dr Talks");
        mSectionsPagerAdapter.addFrag(new AreaOfExpertiseFragment(), "Expert");
        mSectionsPagerAdapter.addFrag(new MoreFragment(), "More");

    }


    private TabLayout.OnTabSelectedListener OnTabSelectedListener = new TabLayout.OnTabSelectedListener(){
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            int c = tab.getPosition();
            progress_app.setVisibility(View.GONE);
            mSectionsPagerAdapter.SetOnSelectView(tabLayout,c);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            int c = tab.getPosition();
            mSectionsPagerAdapter.SetUnSelectView(tabLayout,c);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        menuNotification = menu;
        MenuItem itemCart = menu.findItem(R.id.action_cart);
        LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
        setBadgeCount(this, icon, count);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cart:
                MenuItem itemCart = menuNotification.findItem(R.id.action_cart);
                LayerDrawable icon = (LayerDrawable) itemCart.getIcon();
                count = "0";
                imgCount = Integer.parseInt(count);
                setBadgeCount(this, icon, count);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            Uri imageUri = Crop.getOutput(result);
            setProfile(imageUri);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }else
        {
            ic_profile.setImageBitmap(StringToBitMap(dateResponse.getPhoto()));
        }
    }

    public void setProfile(final Uri uri) {
        ic_profile.setImageResource(R.mipmap.ic_profile);
        if (isConnection != null) {
            Bitmap bitmap = null;
            progress_app.setVisibility(View.VISIBLE);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap = getResizedBitmap(bitmap,100,100);
            if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                apiInterface = ApiClient.getClient().create(ApiInterface.class);

                JSONObject jsonObject = new JSONObject();
                Calendar cal = Calendar.getInstance();
                final DateFormat[] dateForRequest = {new SimpleDateFormat("dd.MM.yyyy")};
                String formattedDate = dateForRequest[0].format(cal.getTime());
                try {
                    UserRegisterDetails userRegisterDetails = UserRegisterDetails.getInstance();
                    jsonObject.put("email", userRegisterDetails.getE_mail());
                    jsonObject.put("photo", BitMapToString(bitmap));
                    jsonObject.put("act", "update");


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Call<RegisterRequestAndResponse> call = apiInterface.registerDetails(jsonObject);
                call.enqueue(new Callback<RegisterRequestAndResponse>() {
                    @Override
                    public void onResponse(Call<RegisterRequestAndResponse> call, Response<RegisterRequestAndResponse> response) {
                        if (response.isSuccessful()) {

                            RegisterRequestAndResponse registerRequestAndResponse = response.body();
                            if (registerRequestAndResponse.getStatus_code() != null) {

                                if (registerRequestAndResponse.getStatus_code().equals(Constants.status_code1)) {

                                    if (registerRequestAndResponse.getProfile_pic() != null) {
                                        ic_profile.setImageURI(uri);
                                        progress_app.setVisibility(View.GONE);

                                        new LoadImageTask(HomeActivity.this).execute(registerRequestAndResponse.getProfile_pic());
                                    }else
                                    {
                                        Log.e("url empty","empty");
                                    }
                                }
                            }
                        }
                    }


                    @Override
                    public void onFailure(Call<RegisterRequestAndResponse> call, Throwable t) {
                        Log.e("output", t.getMessage());
                    }
                });
            } else {
                snackbar = TSnackbar
                        .make(main_content, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Action Button", "onClick triggered");

                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
                snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setTypeface(null, Typeface.BOLD);
                snackbar.show();
            }
        }

    }

    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap=BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }

    private void getNetworkState() {

        if (isConnection != null) {
            if (isConnection.equals(Constants.NETWORK_NOT_CONNECTED)) {
                snackbar = TSnackbar
                        .make(main_content, "No Internet Connection !", TSnackbar.LENGTH_INDEFINITE)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Log.d("Action Button", "onClick triggered");

                            }
                        });
                snackbar.setActionTextColor(Color.parseColor("#4ecc00"));
                snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.parseColor("#E43F3F"));
                TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
                textView.setTextColor(Color.YELLOW);
                textView.setTypeface(null, Typeface.BOLD);
                snackbar.show();


            } else if (isConnection.equals(Constants.NETWORK_CONNECTED)) {
                if (snackbar != null) {
                    snackbar.dismiss();
                }

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
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
            this.unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
    }



    @Override
    public void onImageLoaded(Bitmap bitmap) {

        UtilsClass utilsClass  = new UtilsClass();
        String strBitmap = utilsClass.BitMapToString(bitmap);
        databaseHandler.updateProfilePic("0",strBitmap);
        Log.e("image loading","image load");
    }

    @Override
    public void onError() {
        Toast.makeText(this, "Error Loading Image !", Toast.LENGTH_SHORT).show();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
