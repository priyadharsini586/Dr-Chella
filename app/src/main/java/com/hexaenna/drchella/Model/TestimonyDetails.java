package com.hexaenna.drchella.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by admin on 12/1/2017.
 */

public class TestimonyDetails {

    Bitmap imageBitmap;
    public String content,status_code,status_message,success,testimonyPic,profilePic,name,from,date,time,email;
    public boolean myMessage;
    private static TestimonyDetails ourInstance = new TestimonyDetails();
    int colorCode;
    ArrayList<Tips>tips;
    public TestimonyDetails(){}

    public TestimonyDetails(String content, boolean myMessage,Bitmap msgBitmap, int colorCode) {
        this.content = content;
        this.myMessage = myMessage;
        this.imageBitmap = msgBitmap;
        this.colorCode = colorCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTestimonyPic() {
        return testimonyPic;
    }

    public void setTestimonyPic(String testimonyPic) {
        this.testimonyPic = testimonyPic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getColorCode() {
        return colorCode;
    }

    public void setColorCode(int colorCode) {
        this.colorCode = colorCode;
    }

    public ArrayList<Tips> getTips() {
        return tips;
    }

    public void setTips(ArrayList<Tips> tips) {
        this.tips = tips;
    }

    public static TestimonyDetails getInstance() {
        return ourInstance;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isMyMessage() {
        return myMessage;
    }

    public void setMyMessage(boolean myMessage) {
        this.myMessage = myMessage;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public class Tips
    {
        String email,content,dt_time,tstmny_pic,profile_pic,name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDt_time() {
            return dt_time;
        }

        public void setDt_time(String dt_time) {
            this.dt_time = dt_time;
        }

        public String getTstmny_pic() {
            return tstmny_pic;
        }

        public void setTstmny_pic(String tstmny_pic) {
            this.tstmny_pic = tstmny_pic;
        }

        public String getProfile_pic() {
            return profile_pic;
        }

        public void setProfile_pic(String profile_pic) {
            this.profile_pic = profile_pic;
        }
    }
}
