package com.example.mysimplenews.news.view;

import com.example.mysimplenews.beans.NewsBean;

import java.util.List;

public interface NewsView {
    void showProgress();
    void hideProgress();
    void showLoadFailMsg();
    void addNews(List<NewsBean> list);
}
