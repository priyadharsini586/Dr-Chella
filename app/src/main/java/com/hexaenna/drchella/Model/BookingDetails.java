package com.hexaenna.drchella.Model;

/**
 * Created by admin on 11/2/2017.
 */

public class BookingDetails {
    public String city;
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
}
