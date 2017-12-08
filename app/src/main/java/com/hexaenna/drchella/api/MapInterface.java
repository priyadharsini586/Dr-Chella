package com.hexaenna.drchella.api;

import com.hexaenna.drchella.Model.MapDetails;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by admin on 12/8/2017.
 */

public interface MapInterface {

    @GET("maps/api/geocode/json?")
    Call<MapDetails> getLanLont(@Query("address") String address, @Query("sensor") String sensor);
   /* public static Retrofit retrofit = null;
    public static final String BASE_URL = "https://maps.google.com";
    public static Retrofit getClient()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }*/

}

