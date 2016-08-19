package com.pavel.tinkoff_news.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pavel.tinkoff_news.R;
import com.pavel.tinkoff_news.api.ApiClient;
import com.pavel.tinkoff_news.api.TargetCallback;
import com.pavel.tinkoff_news.api.RequestService;
import com.pavel.tinkoff_news.contract.JsonConstants;
import com.pavel.tinkoff_news.entities.ContentNews;
import com.pavel.tinkoff_news.entities.News;
import com.pavel.tinkoff_news.helpers.DateHelper;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by pavel on 18.08.2016.
 */
public class FullNewsActivity extends AppCompatActivity {

    public static final String NEWS_ID_KEY = "news_id_key";

    private Toolbar toolbar;
    private TextView tvTitleNews;
    private TextView tvContentNews;
    private TextView tvPublicationDateNews;

    private RequestService requestService;
    private TargetCallback<JsonObject, FullNewsActivity> downloadNewsCallback;

    private News news;

    private int idNews;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestService = ApiClient.createService(RequestService.class);

        downloadNewsCallback = new ConcreteNewsCallback();

        setContentView(R.layout.activity_full_news);

        findViews();

        initToolbar();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(NEWS_ID_KEY)) {
                idNews = bundle.getInt(NEWS_ID_KEY, -1);
            }
        }
        if (idNews == -1) {
            throw new IllegalArgumentException("No id for News");
        }

        news = getNewsFromRealm(idNews);
        ContentNews contentNews = getContentNewsFromRealm(idNews);

        if (contentNews != null) {
            showFullNews(contentNews);
        } else {
            downloadNewsById(idNews);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadNewsCallback.setTarget(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        downloadNewsCallback.setTarget(null);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitleNews = (TextView) findViewById(R.id.tv_title_news);
        tvContentNews = (TextView) findViewById(R.id.tv_content_news);
        tvPublicationDateNews = (TextView) findViewById(R.id.tv_publication_date_news);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    private void showFullNews(ContentNews contentNews) {
        tvTitleNews.setText(Html.fromHtml(news.getText()));
        tvContentNews.setText(Html.fromHtml(contentNews.getContent()));
        String publicationString = String.format("%s %s", getString(R.string.publication_date), DateHelper.convertTimeStampToDateFormat(news.getPublicationDate()));
        tvPublicationDateNews.setText(publicationString);
    }

    private News getNewsFromRealm(int id) {
        return Realm.getDefaultInstance().where(News.class).equalTo(JsonConstants.ID, id).findFirst();
    }

    private ContentNews getContentNewsFromRealm(int id) {
        return Realm.getDefaultInstance().where(ContentNews.class).equalTo(JsonConstants.ID, id).findFirst();
    }

    private void downloadNewsById(int idNews) {
        requestService.getNewsById(idNews).enqueue(downloadNewsCallback);
    }

    private static class ConcreteNewsCallback extends TargetCallback<JsonObject, FullNewsActivity> {

        @Override
        protected void onSuccess(Call<JsonObject> call, Response<JsonObject> response) {
            FullNewsActivity fullNewsActivity = getTarget();

            JsonObject object = response.body();
            if (object != null) {
                if (object.get(JsonConstants.PAYLOAD) != null && !object.get(JsonConstants.PAYLOAD).isJsonNull()) {
                    JsonObject newsObject = object.get(JsonConstants.PAYLOAD).getAsJsonObject();
                    if (newsObject != null) {
                        ContentNews contentNews = ContentNews.createFromJson(newsObject);
                        if (contentNews != null) {

                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(contentNews);
                            realm.commitTransaction();

                            fullNewsActivity.showFullNews(contentNews);
                            return;
                        }
                    }
                }
            }
            Toast.makeText(getTarget(), R.string.download_news_problem, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onFail(Call<JsonObject> call) {
            Toast.makeText(getTarget(), R.string.download_news_problem, Toast.LENGTH_SHORT).show();
        }
    }
}
