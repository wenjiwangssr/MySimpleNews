package com.example.mysimplenews.news.presenter;

import android.util.Log;

import com.example.mysimplenews.beans.NewsBean;
import com.example.mysimplenews.common.Urls;
import com.example.mysimplenews.news.model.NewsModel;
import com.example.mysimplenews.news.model.NewsModelImpl;
import com.example.mysimplenews.news.model.OnLoadNewsListListener;
import com.example.mysimplenews.news.view.NewsView;
import com.example.mysimplenews.news.widget.NewsFragment;

import java.util.List;

public class NewsPresenterImpl implements NewsPresenter, OnLoadNewsListListener {
    private static final String TAG="NewsPresenterImpl";
    private NewsModel mNewsModel;
    private NewsView mNewsView;

    public NewsPresenterImpl(NewsView newsView) {
        mNewsView = newsView;
        mNewsModel=new  NewsModelImpl();
    }



    @Override
    public void onSuccess(List<NewsBean> list) {
        mNewsView.hideProgress();
        mNewsView.addNews(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.showLoadFailMsg();
    }

    @Override
    public void loadNews(int type, int page) {
        String url=getUrl(type,page);
        Log.d(TAG,url);
        //只有第一页或者刷新时才显示刷新进度条
        if (page==0)mNewsView.showProgress();
        mNewsModel.loadNews(url,type,this);


    }

    private String getUrl(int type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }
}
