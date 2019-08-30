package com.example.mysimplenews.news;

import android.util.Log;

import com.example.mysimplenews.beans.NewsBean;
import com.example.mysimplenews.beans.NewsDetailBean;
import com.example.mysimplenews.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

public class NewsJsonUtils {
    public static final String TAG="NewsJsonUtils";
    /*
    * 将获取到的json转换为新闻列表对象
    * */
    public static List<NewsBean> readJsonNewsBean(String res,String value){
        List<NewsBean> beans=new ArrayList<NewsBean>();

        try {
            JsonParser parser=new JsonParser();
            JsonObject jsonObject=parser.parse(res).getAsJsonObject();
            JsonElement jsonElement=jsonObject.get(value);
            if (jsonElement==null)return null;
            JsonArray jsonArray=jsonElement.getAsJsonArray();
            for (int i = 0; i <jsonArray.size() ; i++) {
                JsonObject jo=jsonArray.get(i).getAsJsonObject();
                if (jo.has("skipType") && "special".equals(jo.get("skipType").getAsString())) {
                    continue;
                }
                if (jo.has("TAGS") && !jo.has("TAG")) {
                    continue;
                }

                if (!jo.has("imgextra")) {
                    NewsBean news = JsonUtils.deserialize(jo, NewsBean.class);
                    beans.add(news);
                }
            }
        } catch (JsonSyntaxException e) {
            Log.e(TAG,"read Jsonbeans error");
        }
        return beans;
    }

    public  static NewsDetailBean readJsonNewsDetailBean(String res,String docid){
        NewsDetailBean newsDetailBean=null;

        try {
            JsonParser parser=new JsonParser();
            JsonObject jsonObject=parser.parse(res).getAsJsonObject();
            JsonElement jsonElement=jsonObject.get(docid);
            if (jsonElement==null)
                return null;
            newsDetailBean=JsonUtils.deserialize(jsonElement.getAsJsonObject(),NewsDetailBean.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return newsDetailBean;
    }
}
