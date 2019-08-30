package com.example.mysimplenews.news.model;

import com.example.mysimplenews.beans.NewsBean;

import java.util.List;

public interface OnLoadNewsListListener {
    void onSuccess(List<NewsBean> list);
    void onFailure(String msg,Exception e);
}
