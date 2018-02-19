package com.hexaenna.drchella.Model;

import java.util.ArrayList;

/**
 * Created by admin on 2/16/2018.
 */

public class HistoryDetails {

    String status_code,status_message,success;
    ArrayList<Appointment_List> appoinments;

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

    public ArrayList<Appointment_List> getAppoinments() {
        return appoinments;
    }

    public void setAppoinments(ArrayList<Appointment_List> appoinments) {
        this.appoinments = appoinments;
    }

    public class Appointment_List
    {
        String date,app_list;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getApp_list() {
            return app_list;
        }

        public void setApp_list(String app_list) {
            this.app_list = app_list;
        }
    }
}
