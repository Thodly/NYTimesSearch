package com.example.thodlydugue.nytimessearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by thodlydugue on 7/26/2017.
 */

public class Article implements Serializable {

    String webUrl;
    String headline;
    String thumbNail;
    String snippet;
    String leadParagraph;
    String source;
    SimpleDateFormat pubDate;
    String newsDesk;

    public String getWebUrl() {
        return webUrl;
    }
    public String getHeadline() {
        return headline;
    }
    public String getThumbNail() {
        return thumbNail;
    }
    public String getSnippet() {return snippet;}
    public String getLeadParagraph() {return leadParagraph;}
    public String getSource() {return source;}
    public SimpleDateFormat getPubDate() {return pubDate;}
    public String getNewsDesk() {return newsDesk;}

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.newsDesk = jsonObject.getString("news_desk");

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() >0){
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.thumbNail = "http://www.nytimes.com/"+multimediaJson.getString("url");
            }else
                this.thumbNail = "";

        } catch (JSONException e) {

        }
    }
    public static ArrayList<Article> fromJSONArray(JSONArray array){
        ArrayList<Article> articles = new ArrayList<Article>();
        for (int x=0; x<array.length(); x++){
            try{
                articles.add(new Article(array.getJSONObject(x)));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        return articles;
    }
}