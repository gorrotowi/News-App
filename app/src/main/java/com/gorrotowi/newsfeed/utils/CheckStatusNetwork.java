package com.gorrotowi.newsfeed.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author @Gorrotowi
 */

public class CheckStatusNetwork {

    private static ConnectivityManager connectivityManager;

    public static void init(Context ctx) {
        connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

    }

    public static boolean isConnected() {
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

}