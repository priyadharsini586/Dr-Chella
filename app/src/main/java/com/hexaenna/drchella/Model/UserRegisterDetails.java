package com.hexaenna.drchella.Model;

/**
 * Created by admin on 11/14/2017.
 */

public class UserRegisterDetails {
    private String mobileNum,uniqueId,otp,e_mail,userName;
    private static UserRegisterDetails ourInstance = new UserRegisterDetails();
    private UserRegisterDetails(){}

    public static UserRegisterDetails getInstance() {
        return ourInstance;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
