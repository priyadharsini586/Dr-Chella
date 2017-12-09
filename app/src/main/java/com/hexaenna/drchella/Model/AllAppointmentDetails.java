package com.hexaenna.drchella.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 11/24/2017.
 */

public class AllAppointmentDetails {

    public String status_code,status_message,success,s_no;
    public List<Appoinmentslist> appoinments = new ArrayList<>();
    private static AllAppointmentDetails ourInstance = new AllAppointmentDetails();

    public static AllAppointmentDetails getInstance() {
        return ourInstance;
    }
    public String getS_no() {
        return s_no;
    }

    public void setS_no(String s_no) {
        this.s_no = s_no;
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

    public List<Appoinmentslist> getAppoinments() {
        return appoinments;
    }

    public void setAppoinments(List<Appoinmentslist> appoinments) {
        this.appoinments = appoinments;
    }

    public class Appoinmentslist
    {
        private String city_id,date,time,ptnt_name,sno;

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

        public String getSno() {
            return sno;
        }

        public void setSno(String sno) {
            this.sno = sno;
        }
    }
}
