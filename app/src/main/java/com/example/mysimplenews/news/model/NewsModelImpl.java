package com.example.mysimplenews.news.model;

import com.example.mysimplenews.beans.NewsBean;
import com.example.mysimplenews.beans.NewsDetailBean;
import com.example.mysimplenews.common.Urls;
import com.example.mysimplenews.news.widget.NewsFragment;
import com.example.mysimplenews.news.NewsJsonUtils;
import com.example.mysimplenews.utils.OkHttpUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * The type News model.
 */
public class NewsModelImpl implements NewsModel {
    private OkHttpUtils mOkHttpUtils=OkHttpUtils.getmInstance();

/**
 *加载新闻列表
 * @param url
 * @param listener
 */

    @Override
    public void loadNews(String url, final int type, final OnLoadNewsListListener listener) {
        OkHttpUtils.RealCallback realCallback=new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                List<NewsBean> newsBeanList= NewsJsonUtils.readJsonNewsBean(response.toString(),getID(type));
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure("load news list failure",e);
            }
        };
        mOkHttpUtils.get(url,realCallback);
    }

    /**
     * 加载新闻详情
     * @param docid
     * @param listener
     */
    @Override
    public void loadNewsDetail(final String docid, final OnLoadNewsDetailListener listener) {
        String url=getDetailUrl(docid);
        OkHttpUtils.RealCallback realCallback=new OkHttpUtils.RealCallback() {
            @Override
            public void onResponse(Call call, Response response) {
                NewsDetailBean newsDetailBean=NewsJsonUtils.readJsonNewsDetailBean(response.toString(),docid);
                listener.onSuccess(newsDetailBean);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure("load news detail message failure",e);
            }

        };
        mOkHttpUtils.get(url,realCallback);

    }
    /**
     * 获取ID
     * @param type
     * @return
     */
    private String getID(int type) {
        String id;
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }

    private String getDetailUrl(String docId) {
        StringBuffer sb = new StringBuffer(Urls.NEW_DETAIL);
        sb.append(docId).append(Urls.END_DETAIL_URL);
        return sb.toString();
    }
}
