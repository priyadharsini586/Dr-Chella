package com.hexaenna.drchella.api;

import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.TimeAndDateResponse;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 10/13/2017.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("check_login.php")
    Call<RegisterRequestAndResponse> checkEmail(@Field("x") JSONObject email);

    @FormUrlEncoded
    @POST("register.php")
    Call<RegisterRequestAndResponse> registerDetails(@Field("x") JSONObject registerRequest);

    @FormUrlEncoded
    @POST("verify_register.php")
    Call<RegisterRequestAndResponse> verify_OTP(@Field("x") JSONObject verifyRequest);

    @FormUrlEncoded
    @POST("resend_vcode.php")
    Call<RegisterRequestAndResponse> reSendOTP(@Field("x") JSONObject verifyRequest);

    @FormUrlEncoded
    @POST("check_time.php")
    Call<TimeAndDateResponse> check_time(@Field("x") JSONObject checkTimeObject);

    @FormUrlEncoded
    @POST("is_blocked.php")
    Call<TimeAndDateResponse> isBlocked(@Field("x") JSONObject checkTimeObject);

    @FormUrlEncoded
    @POST("block_appoinment.php")
    Call<TimeAndDateResponse> block_appintment(@Field("x") JSONObject checkTimeObject);
}

