package com.hexaenna.drchella.Model;

/**
 * Created by Priya Mohan on 03-11-2017.
 */

public class RegisterRequestAndResponse {

    public String status_code,status_message,success;

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

}
