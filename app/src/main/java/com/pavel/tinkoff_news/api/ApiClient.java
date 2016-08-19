package com.pavel.tinkoff_news.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pavel on 18.08.2016.
 */
public class ApiClient {
    private static final String BASE_URL = "https://api.tinkoff.ru";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder = new Retrofit.Builder();

    public static <S> S createService(Class<S> serviceClass) {
        httpClient.interceptors().add(new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
                .setLevel(HttpLoggingInterceptor.Level.HEADERS));

        Retrofit retrofit = builder
                .baseUrl(BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }

}
