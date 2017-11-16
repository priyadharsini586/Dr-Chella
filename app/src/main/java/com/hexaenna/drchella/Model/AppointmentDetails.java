package com.hexaenna.drchella.Model;

import java.util.ArrayList;

/**
 * Created by admin on 11/16/2017.
 */

public class AppointmentDetails {

   private String city,date,time,confm_no,city_id,booking_time,patient_name;
    private static AppointmentDetails ourInstance = new AppointmentDetails();
    private AppointmentDetails(){}

    public static AppointmentDetails getInstance() {
        return ourInstance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getConfm_no() {
        return confm_no;
    }

    public void setConfm_no(String confm_no) {
        this.confm_no = confm_no;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }
}
