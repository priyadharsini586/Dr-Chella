package com.hexaenna.drchella.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/24/2017.
 */

public class AllAppointmentDetails {

    public String status_code,status_message,success;
    public List<Appoinmentslist> appoinments = new ArrayList<>();

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

    public List<Appoinmentslist> getAppoinments() {
        return appoinments;
    }

    public void setAppoinments(List<Appoinmentslist> appoinments) {
        this.appoinments = appoinments;
    }

    public class Appoinmentslist
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
