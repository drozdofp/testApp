package com.pavel.tinkoff_news.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pavel.tinkoff_news.R;
import com.pavel.tinkoff_news.apapters.NewsAdapter;
import com.pavel.tinkoff_news.api.ApiClient;
import com.pavel.tinkoff_news.api.TargetCallback;
import com.pavel.tinkoff_news.api.RequestService;
import com.pavel.tinkoff_news.contract.JsonConstants;
import com.pavel.tinkoff_news.entities.News;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by pavel on 18.08.2016.
 */
public class NewsListActivity extends AppCompatActivity implements NewsAdapter.OnItemNewsClickListener {

    private Toolbar toolbar;
    private SwipeRefreshLayout srlNewsRefresh;
    private RecyclerView rvNews;

    private RequestService requestService;

    private TargetCallback<JsonObject, SwipeRefreshLayout> downloadNewsCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestService = ApiClient.createService(RequestService.class);

        setContentView(R.layout.activity_news_list);

        findViews();

        downloadNewsCallback = new NewsCallback();

        initToolbar();
        initList();

        downloadNews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        downloadNewsCallback.setTarget(srlNewsRefresh);
    }

    @Override
    protected void onStop() {
        super.onStop();
        downloadNewsCallback.setTarget(null);
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        srlNewsRefresh = (SwipeRefreshLayout) findViewById(R.id.srl_news_refresh);
        rvNews = (RecyclerView) findViewById(R.id.rv_news);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
    }

    private void initList() {
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        newsLayoutManager.setSmoothScrollbarEnabled(true);
        rvNews.setLayoutManager(newsLayoutManager);
        rvNews.addItemDecoration(new SimpleDividerItemDecoration(this));
        rvNews.setItemAnimator(new DefaultItemAnimator());
        rvNews.setAdapter(new NewsAdapter(this, getNewsFromRealm(), this));

        srlNewsRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadNews();
            }
        });
    }

    private RealmResults<News> getNewsFromRealm() {
        return Realm.getDefaultInstance().where(News.class).findAllSorted(JsonConstants.PIBLICATION_DATE, Sort.DESCENDING);
    }

    public void downloadNews() {
        requestService.getListNews().enqueue(downloadNewsCallback);
    }

    @Override
    public void onNewsClick(int newsId) {
        Intent intent = new Intent(this, FullNewsActivity.class);
        intent.putExtra(FullNewsActivity.NEWS_ID_KEY, newsId);
        startActivity(intent);
    }

    private static class NewsCallback extends TargetCallback<JsonObject, SwipeRefreshLayout> {

        @Override
        protected void onSuccess(Call<JsonObject> call, Response<JsonObject> response) {
            SwipeRefreshLayout swipeRefreshLayout = getTarget();
            JsonObject object = response.body();
            if (object != null) {
                if (object.get(JsonConstants.PAYLOAD) != null && !object.get(JsonConstants.PAYLOAD).isJsonNull()) {
                    JsonArray newsArray = object.get(JsonConstants.PAYLOAD).getAsJsonArray();
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    for (JsonElement newsObject : newsArray) {
                        News news = News.createFromJson(newsObject.getAsJsonObject());
                        realm.copyToRealmOrUpdate(news);
                    }
                    realm.commitTransaction();
                }
            }

            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        protected void onFail(Call<JsonObject> call) {
            SwipeRefreshLayout swipeRefreshLayout = getTarget();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(swipeRefreshLayout.getContext(), R.string.download_news_problem, Toast.LENGTH_SHORT).show();
        }
    }
}
