package com.example.mysimplenews.news.presenter;

import android.content.Context;

import com.example.mysimplenews.beans.NewsDetailBean;
import com.example.mysimplenews.news.model.NewsModel;
import com.example.mysimplenews.news.model.NewsModelImpl;
import com.example.mysimplenews.news.model.OnLoadNewsDetailListener;
import com.example.mysimplenews.news.view.NewsDetailView;

import java.util.IdentityHashMap;

public class NewsDetailPresenterImpl implements NewsDetailPresenter, OnLoadNewsDetailListener {

    private Context mContext;
    private NewsDetailView mNewsDetailView;
    private NewsModel mNewsModel;

    public NewsDetailPresenterImpl(Context context, NewsDetailView newsDetailView) {
        this.mContext= context;
        this.mNewsDetailView = newsDetailView;
        mNewsModel = new NewsModelImpl();
    }

    @Override
    public void loadNewsDetail(final String docid) {

        mNewsDetailView.showProgress();
        mNewsModel.loadNewsDetail(docid,this);
    }

    @Override
    public void onSuccess(NewsDetailBean newsDetailBean) {
        if (newsDetailBean!=null){
            mNewsDetailView.showNewsDetailContent(newsDetailBean.getBody());
        }
        mNewsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsDetailView.hideProgress();
    }
}
