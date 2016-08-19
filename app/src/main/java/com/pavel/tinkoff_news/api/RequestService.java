package com.pavel.tinkoff_news.api;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by pavel on 18.08.2016.
 */
public interface RequestService {

    @GET("/v1/news")
    Call<JsonObject> getListNews();

    @GET("/v1/news_content")
    Call<JsonObject> getNewsById(@Query("id") int id);
}
