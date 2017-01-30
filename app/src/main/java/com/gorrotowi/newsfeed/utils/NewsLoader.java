package com.gorrotowi.newsfeed.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.gorrotowi.newsfeed.entitys.ItemNew;

import java.util.List;

/**
 * Created by Gorro on 29/01/17.
 */

public class NewsLoader extends AsyncTaskLoader<List<ItemNew>> {

    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<ItemNew> loadInBackground() {
        if (url == null) {
            return null;
        } else {
            return NetworkUtils.fetchNewsData(url);
        }
    }
}
