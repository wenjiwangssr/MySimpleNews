package com.example.mysimplenews.news.widget;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.example.mysimplenews.R;
import com.example.mysimplenews.beans.NewsBean;
import com.example.mysimplenews.news.presenter.NewsDetailPresenter;
import com.example.mysimplenews.news.presenter.NewsDetailPresenterImpl;
import com.example.mysimplenews.news.view.NewsDetailView;
import com.example.mysimplenews.utils.ImageLoaderUtils;
import com.example.mysimplenews.utils.ToolsUtil;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import me.hz89.swipeback.SwipeBackLayout;
import me.hz89.swipeback.app.SwipeBackActivity;

/*
* 新闻详情页面*/
public class NewsDetailActivity extends SwipeBackActivity implements NewsDetailView {
    private NewsBean mNews;
    private HtmlTextView mTVNewsContent;
    private NewsDetailPresenter mNewsDetailPresenter;
    private ProgressBar mProgressBar;
    private SwipeBackLayout mSwipeBackLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        mProgressBar=(ProgressBar)findViewById(R.id.progress);
        mTVNewsContent=(HtmlTextView)findViewById(R.id.htNewsContent);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mSwipeBackLayout=getSwipeBackLayout();
        mSwipeBackLayout.setEdgeSize(ToolsUtil.getWidthInPx(this));
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        mNews=(NewsBean)getIntent().getSerializableExtra("news");//??????

        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.coolapsing_toolbar);
        collapsingToolbarLayout.setTitle(mNews.getTitle());

        ImageLoaderUtils.display(getApplicationContext(),(ImageView)findViewById(R.id.ivImage),mNews.getImgsrc());
        mNewsDetailPresenter=new NewsDetailPresenterImpl(getApplication(),this);
        mNewsDetailPresenter.loadNewsDetail(mNews.getDocid());




    }

    @Override
    public void showNewsDetailContent(String newsDetailContent) {
        mTVNewsContent.setHtmlFromString(newsDetailContent,new HtmlTextView.LocalImageGetter());
    }

    @Override
    public void showProgress() {mProgressBar.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {mProgressBar.setVisibility(View.GONE);

    }
}
