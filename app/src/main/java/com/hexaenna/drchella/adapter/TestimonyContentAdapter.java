package com.hexaenna.drchella.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexaenna.drchella.Db.DatabaseHandler;
import com.hexaenna.drchella.Model.TestimonyDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.image_cache.ImageLoader;
import com.hexaenna.drchella.utils.UtilsClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 12/2/2017.
 */

public class TestimonyContentAdapter extends ArrayAdapter<TestimonyDetails> {

    private Activity activity;
    private List<TestimonyDetails> messages;
    ImageLoader imageLoader;
    public TestimonyContentAdapter(Activity context, int resource, List<TestimonyDetails> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.messages = objects;
        imageLoader=new ImageLoader(context.getApplicationContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        TestimonyDetails chatBubble = getItem(position);
        int viewType = getItemViewType(position);
        Log.e("message position"," "+chatBubble.myMessage);
        if (!chatBubble.myMessage) {
            layoutResource = R.layout.left_bubble_chat;
        } else {
            layoutResource = R.layout.right_chat_bubble;
        }

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        SimpleDateFormat geivenDateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm a");
        String formattedDate= " ";
        try {
            Date date =geivenDateFormat.parse(chatBubble.getDate());
            geivenDateFormat.applyPattern("dd MMM yy/hh:mm a");
            formattedDate = geivenDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] date = formattedDate.split("/");
        holder.txtDate.setText(date[0]);
        holder.txtTime.setText(date[1]);
        //set message content
        holder.msg.setText(chatBubble.getContent());
        holder.testUserName.setTextColor(chatBubble.getColorCode());
        holder.testUserName.setText(chatBubble.getName());
        if (chatBubble.getFrom().equals("server")) {
            if (chatBubble.getProfilePic() != null) {
                imageLoader.DisplayImage(chatBubble.getProfilePic(), holder.ic_profile, R.mipmap.ic_testimony_user_name);
            } else {
                holder.ic_profile.setImageDrawable(activity.getResources().getDrawable(R.mipmap.ic_testimony_user_name));
            }
            if (chatBubble.getTestimonyPic() != null) {
//            holder.imgMsg.setImageBitmap(chatBubble.getImageBitmap());
                holder.imgMsg.setVisibility(View.VISIBLE);
                imageLoader.DisplayImage(chatBubble.getTestimonyPic(), holder.imgMsg, R.drawable.default_image_testimony);
            } else
                holder.imgMsg.setVisibility(View.GONE);
        }else if (chatBubble.getFrom().equals("me"))
        {
            DatabaseHandler databaseHandler = new DatabaseHandler(activity);
            String[] userDetails =  databaseHandler.getUserName("0");
            UtilsClass utilsClass = new UtilsClass();
            Bitmap bitmap = utilsClass.StringToBitMap(userDetails[2]);
            if (bitmap != null) {
                holder.ic_profile.setImageBitmap(bitmap);
            }
        }

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime. Value 2 is returned because of left and right views.
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    private class ViewHolder {
        private TextView msg,testUserName,txtDate,txtTime;
        ImageView imgMsg;
        CircleImageView ic_profile;
        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.txt_msg);
            txtTime = (TextView) v.findViewById(R.id.txtTime);
            txtDate = (TextView) v.findViewById(R.id.txtDate);
            imgMsg = (ImageView) v.findViewById(R.id.imgMsg);
            testUserName = (TextView) v.findViewById(R.id.testUserName);
            ic_profile = (CircleImageView) v.findViewById(R.id.ic_profile);
        }
    }
}
