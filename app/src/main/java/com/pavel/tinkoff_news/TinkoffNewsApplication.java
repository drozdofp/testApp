package com.pavel.tinkoff_news;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by pavel on 19.08.2016.
 */
public class TinkoffNewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        initDataBaseRealm();
    }

    private void initDataBaseRealm() {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this).name("tinkoff_news.realm").deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfig);
    }
}
