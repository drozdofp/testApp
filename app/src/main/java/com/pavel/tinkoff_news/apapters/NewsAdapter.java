package com.pavel.tinkoff_news.apapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pavel.tinkoff_news.R;
import com.pavel.tinkoff_news.entities.News;
import com.pavel.tinkoff_news.helpers.DateHelper;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by pavel on 19.08.2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private RealmResults<News> newsResult;
    private OnItemNewsClickListener onItemNewsClickListener;

    public NewsAdapter(Context context, RealmResults<News> newsResult, OnItemNewsClickListener onItemNewsClickListener) {
        this.context = context;
        this.newsResult = newsResult;
        this.onItemNewsClickListener = onItemNewsClickListener;
        this.newsResult.addChangeListener(new RealmChangeListener<RealmResults<News>>() {
            @Override
            public void onChange(RealmResults<News> element) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.setNews(newsResult.get(position));
    }

    @Override
    public int getItemCount() {
        return newsResult != null ? newsResult.size() : 0;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvTitleNews;
        private TextView tvPublicationDateNews;
        private News currentNews;

        public NewsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitleNews = (TextView) itemView.findViewById(R.id.tv_title_news);
            tvPublicationDateNews = (TextView) itemView.findViewById(R.id.tv_publication_date_news);
        }

        public void setNews(News currentNews) {
            this.currentNews = currentNews;
            tvTitleNews.setText(Html.fromHtml(currentNews.getText()));
            String publicationString = String.format("%s %s", context.getString(R.string.publication_date), DateHelper.convertTimeStampToDateFormat(currentNews.getPublicationDate()));
            tvPublicationDateNews.setText(publicationString);
        }

        @Override
        public void onClick(View v) {
            if (onItemNewsClickListener != null) {
                onItemNewsClickListener.onNewsClick(currentNews.getId());
            }
        }
    }

    public interface OnItemNewsClickListener {
        void onNewsClick(int newsId);
    }
}

