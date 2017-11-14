package com.hexaenna.drchella.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by admin on 11/14/2017.
 */

public class SendSMSApiClient {

        public static Retrofit retrofit = null;
        public static final String BASE_URL = "http://www.1message.com/services/";

        public static Retrofit getClient()
        {
            if (retrofit == null)
            {

                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .build();
            }
            return retrofit;
        }

}
