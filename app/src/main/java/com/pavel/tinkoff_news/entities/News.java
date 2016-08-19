package com.pavel.tinkoff_news.entities;

import com.google.gson.JsonObject;
import com.pavel.tinkoff_news.contract.JsonConstants;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by pavel on 18.08.2016.
 */
public class News extends RealmObject {
    private static final int DEFAULT_ID = -1;

    @PrimaryKey
    private int id;
    private String text;
    private long publicationDate;

    public News() {
        this.id = DEFAULT_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(long publicationDate) {
        this.publicationDate = publicationDate;
    }

    public static News createFromJson(JsonObject object) {
        if (object != null) {

            News news = new News();

            if (object.get(JsonConstants.ID) != null && !object.get(JsonConstants.ID).isJsonNull()) {
                news.setId(object.get(JsonConstants.ID).getAsInt());
            }
            if (object.get(JsonConstants.TEXT) != null && !object.get(JsonConstants.TEXT).isJsonNull()) {
                news.setText(object.get(JsonConstants.TEXT).getAsString());
            }
            if (object.get(JsonConstants.PIBLICATION_DATE) != null && !object.get(JsonConstants.PIBLICATION_DATE).isJsonNull()) {
                JsonObject publicationDateObject = object.get(JsonConstants.PIBLICATION_DATE).getAsJsonObject();
                if (publicationDateObject.get(JsonConstants.MILISECONDS) != null && !publicationDateObject.get(JsonConstants.MILISECONDS).isJsonNull()) {
                    news.setPublicationDate(publicationDateObject.get(JsonConstants.MILISECONDS).getAsLong());
                }
            }
            return news.getId() != DEFAULT_ID ? news : null;
        }
        return null;
    }
}
