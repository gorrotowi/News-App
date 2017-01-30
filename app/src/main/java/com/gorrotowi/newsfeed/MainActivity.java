package com.gorrotowi.newsfeed;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gorrotowi.newsfeed.adapters.AdapterNews;
import com.gorrotowi.newsfeed.entitys.ItemNew;
import com.gorrotowi.newsfeed.utils.CheckStatusNetwork;
import com.gorrotowi.newsfeed.utils.NewsLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<ItemNew>> {

    @BindView(R.id.rcNews)
    RecyclerView rcNews;
    @BindView(R.id.spRefresh)
    SwipeRefreshLayout spRefresh;
    @BindView(R.id.txtNoData)
    TextView txtNoData;
    @BindView(R.id.loadingIndicator)
    ProgressBar loadingIndicator;

    AdapterNews adapterNews;
    String baseurl = "http://content.guardianapis.com/search?order-by=newest&show-elements=image&show-fields=trailText,thumbnail&q=politics&api-key=";
    String key = "your_api_key|test";

    String urlGuardian = baseurl + key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        CheckStatusNetwork.init(this);

        txtNoData.setVisibility(View.GONE);
        loadingIndicator.setVisibility(View.VISIBLE);

        adapterNews = new AdapterNews(this, new ArrayList<ItemNew>());
        rcNews.setLayoutManager(new LinearLayoutManager(this));

        rcNews.setAdapter(adapterNews);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CheckStatusNetwork.isConnected()) {
            loadingIndicator.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
            makeRequest(urlGuardian);
        } else {
            txtNoData.setVisibility(View.VISIBLE);
            txtNoData.setText(R.string.error_networkstatus_no_connected);
        }

        spRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (CheckStatusNetwork.isConnected()) {
                    loadingIndicator.setVisibility(View.VISIBLE);
                    txtNoData.setVisibility(View.GONE);
                    makeRequest(urlGuardian);
                } else {
                    txtNoData.setVisibility(View.VISIBLE);
                    txtNoData.setText(R.string.error_networkstatus_no_connected);
                    spRefresh.setRefreshing(false);
                }
            }
        });

    }

    private void makeRequest(String urlGuardian) {
        Log.e("URL->>>", "makeRequest: " + urlGuardian);
        LoaderManager loaderManager = getSupportLoaderManager();
        Bundle args = new Bundle();
        args.putString("url", urlGuardian);
        loaderManager.initLoader(1, args, this);
    }

    @Override
    public Loader<List<ItemNew>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, args.getString("url"));
    }

    @Override
    public void onLoadFinished(Loader<List<ItemNew>> loader, List<ItemNew> data) {
        loadingIndicator.setVisibility(View.GONE);
        txtNoData.setText(R.string.empty_txt_message);
        spRefresh.setRefreshing(false);
        rcNews.setAdapter(null);
        if (data != null && !data.isEmpty()) {
            adapterNews = new AdapterNews(this, data);
            rcNews.setAdapter(adapterNews);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<ItemNew>> loader) {
        rcNews.setAdapter(null);
    }
}
