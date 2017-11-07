package com.hexaenna.drchella.Model;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by admin on 11/2/2017.
 */

public class BookingDetails {
    private String city,selectedCity,selectedDate,selectedTime,e_mail,appSeno;
    int selectedPosition = -1;
    ArrayList<String> bookedList,blockedList;
    private static BookingDetails ourInstance = new BookingDetails();
    private BookingDetails(){}

    public static BookingDetails getInstance() {
        return ourInstance;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSelectedCity() {
        return selectedCity;
    }

    public void setSelectedCity(String selectedCity) {
        this.selectedCity = selectedCity;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(String selectedTime) {
        this.selectedTime = selectedTime;
    }

    public ArrayList<String> getBookedList() {
        return bookedList;
    }

    public void setBookedList(ArrayList<String> bookedList) {
        this.bookedList = bookedList;
    }

    public ArrayList<String> getBlockedList() {
        return blockedList;
    }

    public void setBlockedList(ArrayList<String> blockedList) {
        this.blockedList = blockedList;
    }


    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getAppSeno() {
        return appSeno;
    }

    public void setAppSeno(String appSeno) {
        this.appSeno = appSeno;
    }
}
