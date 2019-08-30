package com.example.mysimplenews.news.model;

public interface NewsModel {

    void loadNews(String url,int type,OnLoadNewsListListener listener);

    void loadNewsDetail(String docid,OnLoadNewsDetailListener listener);
}
