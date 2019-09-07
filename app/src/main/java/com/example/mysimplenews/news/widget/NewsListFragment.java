package com.example.mysimplenews.news.widget;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.mysimplenews.R;
import com.example.mysimplenews.beans.NewsBean;
import com.example.mysimplenews.common.Urls;
import com.example.mysimplenews.news.NewsAdapter;
import com.example.mysimplenews.news.presenter.NewsPresenter;
import com.example.mysimplenews.news.presenter.NewsPresenterImpl;
import com.example.mysimplenews.news.view.NewsView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NewsListFragment extends Fragment implements NewsView,SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG="NewsListFragment";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private NewsAdapter mNewsAdapter;
    private List<NewsBean>mData;
    private NewsPresenter mNewsPresenter;

    private int mType=NewsFragment.NEWS_TYPE_TOP;
    private int pageIndex = 0;

    public static NewsListFragment newInstance(int type){
        Bundle args=new Bundle();

        NewsListFragment fragment=new NewsListFragment();

        args.putInt("type",type);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsPresenter=new NewsPresenterImpl(this);
        mType=getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_newslist,null);

        mSwipeRefreshWidget=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);

        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,R.color.primary_dark,R.color.primary_light,R.color.accent);


        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView =(RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNewsAdapter=new NewsAdapter(getActivity().getApplicationContext());
        mNewsAdapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mData.size()<=0)return;
                NewsBean news=mNewsAdapter.getItem(position);
                Intent intent=new Intent(getActivity(),NewsDetailActivity.class);
                intent.putExtra("news",news);//lei了，向NewsDetailActivity传递数据NewsBean news

                //makeSceneTransitionAnimation共享元素动画
                //新的activity产生一个过渡动画，动画由两个页面的相同元素生成，这种效果只支持5.0以上的手机。
                ////两个Activity设置相同的控件 ,相同的ID,系统自动生成动画
                View transitionView=view.findViewById(R.id.ivNews);
                ActivityOptionsCompat options=ActivityOptionsCompat.makeSceneTransitionAnimation
                        (getActivity(),transitionView,"transition_news_img");

                ActivityCompat.startActivity(getActivity(),intent,options.toBundle());
            }
        });

        mRecyclerView.setAdapter(mNewsAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItem;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE&&lastVisibleItem+1==mNewsAdapter.getItemCount()
                        &&mNewsAdapter.isShowFooter()){
                    //Loading more
                    Log.d(TAG,"loading more data");
                    mNewsPresenter.loadNews(mType,pageIndex+ Urls.PAZE_SIZE);
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem=mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
        onRefresh();
        return view;

    }

    @Override
    public void onRefresh() {

        pageIndex=0;
        if (mData!=null)
            mData.clear();
        mNewsPresenter.loadNews(mType,pageIndex);

    }

    @Override
    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        if (pageIndex==0){
            mNewsAdapter.isShowFooter(false);
            mNewsAdapter.notifyDataSetChanged();
        }
        View view=getActivity()==null?mRecyclerView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        Snackbar.make(view,"Failed to load data",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void addNews(List<NewsBean> list) {
        mNewsAdapter.isShowFooter(true);
        if (mData==null){
            mData=new ArrayList<NewsBean>();

        }
        mData.addAll(list);
        if (pageIndex == 0){
            mNewsAdapter.setmData(mData);
        }else{
            //if exists no more data,hide footer layout
            if (list==null||list.size()==0){
                mNewsAdapter.isShowFooter(false);
            }
            mNewsAdapter.notifyDataSetChanged();
        }
        pageIndex+=Urls.PAZE_SIZE;
    }
}
