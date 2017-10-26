package com.hexaenna.drchella.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hexaenna.drchella.R;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_activity);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSubmit:
                Intent intent = new Intent(getApplicationContext(),BookAppointmentActivity.class);
                startActivity(intent);
                break;
        }
    }
}
