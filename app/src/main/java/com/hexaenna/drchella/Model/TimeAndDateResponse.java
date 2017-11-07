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
    String status_code,status_message,success,app_sno = null;
    @SerializedName("Blocked_Array")
    @Expose
    ArrayList<String> Blocked_Array = null ;
    @SerializedName("Booked_Array")
    @Expose
    ArrayList<String> Booked_Array = new ArrayList<>();

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
}
