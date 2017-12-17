package com.hexaenna.drchella.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin on 11/4/2017.
 */

public class TimeAndDateResponse {
   private   String status_code,status_message,success,app_sno = null,city_id,date,time,uniqid,photo,profile_pic,mobile,name,lang,title,notification;
    public List<appoinments> appoinments = new ArrayList<>();
    @SerializedName("Blocked_Array")
    @Expose
   private ArrayList<String> Blocked_Array = null ;
    @SerializedName("Booked_Array")
    @Expose
  private   ArrayList<String> Booked_Array = new ArrayList<>();

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

    public ArrayList<String> getBlocked_Array() {
        return Blocked_Array;
    }

    public void setBlocked_Array(ArrayList<String> blocked_Array) {
        Blocked_Array = blocked_Array;
    }

    public ArrayList<String> getBooked_Array() {
        return Booked_Array;
    }

    public void setBooked_Array(ArrayList<String> booked_Array) {
        Booked_Array = booked_Array;
    }

    public String getApp_sno() {
        return app_sno;
    }

    public void setApp_sno(String app_sno) {
        this.app_sno = app_sno;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
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

    public String getUniqid() {
        return uniqid;
    }

    public void setUniqid(String uniqid) {
        this.uniqid = uniqid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<TimeAndDateResponse.appoinments> getAppoinments() {
        return appoinments;
    }

    public void setAppoinments(List<appoinments> appoinments) {
        this.appoinments = appoinments;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public class appoinments
    {
        private String city_id,date,time,ptnt_name;

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
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

        public String getPtnt_name() {
            return ptnt_name;
        }

        public void setPtnt_name(String ptnt_name) {
            this.ptnt_name = ptnt_name;
        }
    }
}
