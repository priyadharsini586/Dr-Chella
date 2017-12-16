package com.hexaenna.drchella.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.messaging.FirebaseMessaging;
import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.activity.MoreItemsActivity;
import com.hexaenna.drchella.utils.Config;

public class SettingFragment extends Fragment  implements MoreItemsActivity.OnBackPressedListener {

    Switch txtSwithOn;
    View rootView;
    TextView txtOn;
    DatabaseHandler databaseHandler;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        databaseHandler = new DatabaseHandler(getActivity());
        txtSwithOn = (Switch) rootView.findViewById(R.id.txtSwithOn);
        txtOn = (TextView) rootView.findViewById(R.id.txtOn);
        txtSwithOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean on = ((Switch) v).isChecked();
                if(on)
                {
                    //Do something when switch is on/checked
                    txtOn.setText("On");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Config.TOPIC_GLOBAL);
                    databaseHandler.updateNotify("0");
                    Log.e("sub","subscribeToTopic");
                }
                else
                {
                    //Do something when switch is off/unchecked
                    txtOn.setText("Off");
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    databaseHandler.updateNotify("1");
                    Log.e("sub","unsubscribeToTopic");
                }
            }
        });

        if (!databaseHandler.checkTableForNotify())
            getDataBaseConnect();
        String notify = databaseHandler.getNotify("0");
        //0 means off,1 means on
        if (notify.equals("1"))
        {
            txtSwithOn.setChecked(false);
            txtOn.setText("Off");
        }else if (notify.equals("0"))
        {
            txtSwithOn.setChecked(true);
            txtOn.setText("On");
        }
        Log.e("notify",notify);
        ((MoreItemsActivity) getActivity()).setOnBackPressedListener(this);
        return rootView;
    }

    private void getDataBaseConnect() {

        databaseHandler.addNotify("1");
    }

    @Override
    public void onBackPressed() {
        getActivity().finish();

    }
}
