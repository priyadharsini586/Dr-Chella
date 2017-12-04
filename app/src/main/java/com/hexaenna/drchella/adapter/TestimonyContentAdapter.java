package com.hexaenna.drchella.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hexaenna.drchella.Model.TestimonyDetails;
import com.hexaenna.drchella.R;
import com.hexaenna.drchella.image_cache.ImageLoader;

import java.util.List;
import java.util.Random;

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


        //set message content
        holder.msg.setText(chatBubble.getContent());
        holder.testUserName.setTextColor(chatBubble.getColorCode());

        if (chatBubble.getTestimonyPic() != null) {
//            holder.imgMsg.setImageBitmap(chatBubble.getImageBitmap());
            imageLoader.DisplayImage(chatBubble.getTestimonyPic(), holder.imgMsg);
        }
        else
            holder.imgMsg.setVisibility(View.GONE);

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
        private TextView msg,testUserName;
        ImageView imgMsg;
        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.txt_msg);
            imgMsg = (ImageView) v.findViewById(R.id.imgMsg);
            testUserName = (TextView) v.findViewById(R.id.testUserName);
        }
    }
}
