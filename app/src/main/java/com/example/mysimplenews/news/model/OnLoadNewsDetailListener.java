package com.example.mysimplenews.news.model;


import com.example.mysimplenews.beans.NewsDetailBean;



public interface OnLoadNewsDetailListener {
    void onSuccess(NewsDetailBean newsDetailBean);
    void onFailure(String msg,Exception e);
}
