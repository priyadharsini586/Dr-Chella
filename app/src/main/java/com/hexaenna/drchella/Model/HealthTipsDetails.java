package com.hexaenna.drchella.Model;

import java.util.ArrayList;

/**
 * Created by admin on 11/29/2017.
 */

public class HealthTipsDetails {
   private String status_code,status_message,success;
   private ArrayList<Tips>tips = new ArrayList<>();

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

    public ArrayList<Tips> getTips() {
        return tips;
    }

    public void setTips(ArrayList<Tips> tips) {
        this.tips = tips;
    }

    public class Tips {
        String title,tips,dt_time,tips_pic;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public String getDt_time() {
            return dt_time;
        }

        public void setDt_time(String dt_time) {
            this.dt_time = dt_time;
        }

        public String getTips_pic() {
            return tips_pic;
        }

        public void setTips_pic(String tips_pic) {
            this.tips_pic = tips_pic;
        }
    }

}
