package com.pavel.tinkoff_news.entities;

import com.google.gson.JsonObject;
import com.pavel.tinkoff_news.contract.JsonConstants;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by pavel on 18.08.2016.
 */
public class ContentNews extends RealmObject {
    private static final int DEFAULT_ID = -1;

    @PrimaryKey
    private int id;
    private String content;


    public ContentNews() {
        this.id = DEFAULT_ID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static ContentNews createFromJson(JsonObject object) {
        if (object != null) {

            ContentNews contentNews = new ContentNews();

            if (object.get(JsonConstants.TITLE) != null && !object.get(JsonConstants.TITLE).isJsonNull()) {
                JsonObject titleJson = object.get(JsonConstants.TITLE).getAsJsonObject();
                if (titleJson.get(JsonConstants.ID) != null && !titleJson.get(JsonConstants.ID).isJsonNull()) {
                    contentNews.setId(titleJson.get(JsonConstants.ID).getAsInt());
                }
            }
            if (object.get(JsonConstants.CONTENT) != null && !object.get(JsonConstants.CONTENT).isJsonNull()) {
                contentNews.setContent(object.get(JsonConstants.CONTENT).getAsString());
            }

            return contentNews.getId() != DEFAULT_ID ? contentNews : null;
        }
        return null;
    }
}
