package com.hexaenna.drchella.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.hexaenna.drchella.R;

public class SplashActivity extends AppCompatActivity {

    LinearLayout ldtSplash;
    private Animation animBounce;
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        ldtSplash = (LinearLayout) findViewById(R.id.ldtSplash);

        animBounce = AnimationUtils.loadAnimation(this, R.anim.splash_bounce);
        animBounce.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(),RegistrationActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.right_in, R.anim.left_out);
                        SplashActivity.this.finish();
                    }
                },SPLASH_DISPLAY_LENGTH);


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        ldtSplash.startAnimation(animBounce);
    }
}
