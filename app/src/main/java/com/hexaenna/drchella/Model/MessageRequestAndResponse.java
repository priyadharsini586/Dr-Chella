package com.hexaenna.drchella.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by admin on 11/14/2017.
 */

public class MessageRequestAndResponse {

    @SerializedName("CODE")
    @Expose
    private int  CODE;

    public int getCode() {
        return CODE;
    }

    public void setCode(int code) {
        this.CODE = code;
    }
}
