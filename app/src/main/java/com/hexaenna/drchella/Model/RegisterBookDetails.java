package com.hexaenna.drchella.Model;

/**
 * Created by admin on 11/7/2017.
 */

public class RegisterBookDetails {
   private String name,age,gender,applicantNumber,patientNumber,place,e_mailid,address;
    private static RegisterBookDetails ourInstance = new RegisterBookDetails();
    private RegisterBookDetails(){}

    public static RegisterBookDetails getInstance() {
        return ourInstance;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getApplicantNumber() {
        return applicantNumber;
    }

    public void setApplicantNumber(String applicantNumber) {
        this.applicantNumber = applicantNumber;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getE_mailid() {
        return e_mailid;
    }

    public void setE_mailid(String e_mailid) {
        this.e_mailid = e_mailid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
