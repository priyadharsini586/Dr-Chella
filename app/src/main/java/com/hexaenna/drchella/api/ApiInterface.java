package com.hexaenna.drchella.api;

import com.hexaenna.drchella.Model.AllAppointmentDetails;
import com.hexaenna.drchella.Model.AppointmentDetails;
import com.hexaenna.drchella.Model.GetExcelModel;
import com.hexaenna.drchella.Model.HealthTipsDetails;
import com.hexaenna.drchella.Model.MessageRequestAndResponse;
import com.hexaenna.drchella.Model.RegisterRequestAndResponse;
import com.hexaenna.drchella.Model.TestimonyDetails;
import com.hexaenna.drchella.Model.TimeAndDateResponse;
import com.hexaenna.drchella.Model.VideoDetails;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @FormUrlEncoded
    @POST("book_appoinment.php")
    Call<TimeAndDateResponse> book_appintment(@Field("x") JSONObject bookObject);

    @FormUrlEncoded
    @POST("delete_appointment.php")
    Call<TimeAndDateResponse> cancel_appintment(@Field("x") JSONObject bookObject);


    @FormUrlEncoded
    @POST("recent_apntmnt.php")
    Call<TimeAndDateResponse> recent_appintment(@Field("x") JSONObject bookObject);


    @POST("sendmessage.aspx")
    Call<String> sendMessage(@Query("account") String account, @Query("password") String password, @Query("sender") String sender, @Query("message") String message, @Query("mobile") String mobile, @Query("messageid") String messageid);


    @FormUrlEncoded
    @POST("get_appoinment_details.php")
    Call<AppointmentDetails> appointment_details(@Field("x") JSONObject bookObject);

    @FormUrlEncoded
    @POST("profile_pic.php")
    Call<TimeAndDateResponse> getProfilePic(@Field("x") JSONObject bookObject);

    @FormUrlEncoded
    @POST("yours_appntmnts.php")
    Call<AllAppointmentDetails> allAppointment(@Field("x") JSONObject bookObject);

    @FormUrlEncoded
    @POST("delete_appointment.php")
    Call<AllAppointmentDetails> deleteAppointment(@Field("x") JSONObject bookObject);

    @POST("health_tips.php")
    Call<HealthTipsDetails> getHealthTips();

    @FormUrlEncoded
    @POST("change_language.php")
    Call<HealthTipsDetails> changeLanguage(@Field("x") JSONObject bookObject);


    @FormUrlEncoded
    @POST("testimony.php")
    Call<TestimonyDetails> sendTestimony(@Field("x") JSONObject object);

    @POST("display_testimony.php")
    Call<TestimonyDetails> getTestimonyDetails();

    @POST("get_video.php")
    Call<VideoDetails> getVideo();

    @POST("get_notification.php")
    Call<TimeAndDateResponse> getNotification();

    @FormUrlEncoded
    @POST("appointmnt_report.php")
    Call<GetExcelModel>getExcelDetails(@Field("x") JSONObject email);

    @FormUrlEncoded
    @POST("check_appointment.php")
    Call<TimeAndDateResponse>getReport(@Field("x") JSONObject email);

    @FormUrlEncoded
    @POST("update_phone.php")
    Call<RegisterRequestAndResponse>update_PhoneNumber(@Field("x") JSONObject email);
}

