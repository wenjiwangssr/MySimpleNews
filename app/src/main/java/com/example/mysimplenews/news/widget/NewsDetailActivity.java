package com.example.mysimplenews.news.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mysimplenews.R;
import com.example.mysimplenews.news.view.NewsDetailView;

import me.hz89.swipeback.app.SwipeBackActivity;

/*
* 新闻详情页面*/
public class NewsDetailActivity extends SwipeBackActivity implements NewsDetailView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
    }

    @Override
    public void showNewsDetailContent(String newsDetailContent) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }
}
