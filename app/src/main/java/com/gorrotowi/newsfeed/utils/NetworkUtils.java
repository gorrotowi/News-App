package com.gorrotowi.newsfeed.utils;

import android.util.Log;

import com.gorrotowi.newsfeed.entitys.ItemNew;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gorro on 29/01/17.
 */

public class NetworkUtils {

    private static String TAG = NetworkUtils.class.getSimpleName();

    public NetworkUtils() {
    }

    public static List<ItemNew> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        JSONObject response = null;

        try {
            response = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return getNewsList(response);
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Building URL", e);
        }
        return url;
    }

    private static JSONObject makeHttpRequest(URL url) throws IOException {
        JSONObject jsonResponse = null;
        if (url == null) {
            Log.e(TAG, "makeHttpRequest: it's null");
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException | JSONException e) {
            Log.e(TAG, "Problem getting books info", e);
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static JSONObject readFromStream(InputStream inputStream) throws IOException, JSONException {
        StringBuilder outPutString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                outPutString.append(line);
                line = reader.readLine();
            }
        }
        return new JSONObject(outPutString.toString());
    }

    private static List<ItemNew> getNewsList(JSONObject response) {
        List<ItemNew> itemNews = new ArrayList<>();
        if (response != null) {
            try {
                JSONObject jsonResponse = response.getJSONObject("response");
                if (jsonResponse.has("results")) {
                    JSONArray results = jsonResponse.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        String title = "";
                        String subtitle = "";
                        String section = "";
                        String url = "";
                        String urlImg = "";
                        JSONObject jsonNew = results.getJSONObject(i);
                        if (jsonNew.has("sectionName")) {
                            section = jsonNew.getString("sectionName");
                        }
                        if (jsonNew.has("webTitle")) {
                            title = jsonNew.getString("webTitle");
                        }
                        if (jsonNew.has("fields")) {
                            subtitle = jsonNew.getJSONObject("fields").getString("trailText");
                        }
                        if (jsonNew.has("webUrl")) {
                            url = jsonNew.getString("webUrl");
                        }
                        if (jsonNew.has("fields")) {
                            urlImg = jsonNew.getJSONObject("fields").getString("thumbnail");
                        }
                        ItemNew item = new ItemNew(url, urlImg, title, subtitle, section);
                        itemNews.add(item);
                    }
                }

            } catch (JSONException e) {
                Log.e(TAG, "Problem parsing book JSON", e);
            }
        }
        return itemNews;
    }

}
