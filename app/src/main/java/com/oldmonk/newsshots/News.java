package com.oldmonk.newsshots;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class News {

    private String title, content, urlToImage, urlToSource;

    public News(String title) {
        this.title = title;
        this.urlToImage = null;
        this.content = null;
        this.urlToSource = null;
    }

    public News(String title, String content, String urlToImage, String urlToSource) {
        this.title = title;
        this.content = content;
        this.urlToImage = urlToImage;
        this.urlToSource = urlToSource;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getUrlToSource() {
        return urlToSource;
    }

    public void setUrlToSource(String urlToSource) {
        this.urlToSource = urlToSource;
    }

    public static ArrayList<News> getNewsFromJsonArray(JSONArray jsonArray, int n){
        ArrayList<News> results = new ArrayList<>(n);
        for(int i=0; i<n; i++){
            try {
                JSONObject temp= jsonArray.getJSONObject(i);
                News x = new News(temp.getString(AppConfig.JSON_NEWS_TITLE));
                x.setUrlToImage(temp.getString(AppConfig.JSON_NEWS_URL_TO_IMAGE));
                x.setUrlToSource(temp.getString(AppConfig.JSON_URL_TO_NEWS_SOURCE));
                //not completed content
                //x.setContent(temp.getString());

                results.add(x);

            } catch (JSONException e) {
                e.printStackTrace();
                Log.d(News.class.getName(), "couldnt retrieve object "+i);
            }

        }
        return results;
    }
}
