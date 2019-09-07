package com.example.mysimplenews.image.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mysimplenews.R;
import com.example.mysimplenews.beans.ImageBean;
import com.example.mysimplenews.image.ImageAdapter;
import com.example.mysimplenews.image.presenter.ImagePresenter;
import com.example.mysimplenews.image.presenter.ImagePresenterImpl;
import com.example.mysimplenews.image.view.ImageView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ImageFragment extends Fragment implements ImageView, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG="ImageFragment";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ImageAdapter mAdapter;
    private List<ImageBean> mData;
    private ImagePresenter mImagePresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImagePresenter=new ImagePresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_image,null);
        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecyclerView=(RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter=new ImageAdapter(getActivity().getApplicationContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(mOnScrollListener);
        onRefresh();//加载图片列表
        return view;
    }

    private RecyclerView.OnScrollListener mOnScrollListener=new RecyclerView.OnScrollListener() {
        private int lastVisibleItem;
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem+1==mAdapter.getItemCount()){
                Snackbar.make(getActivity().findViewById(R.id.drawer_layout),"一次只加载20条，请刷新",Snackbar.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem=mLayoutManager.findLastVisibleItemPosition();
        }
    };

    @Override
    public void addImage(List<ImageBean> list) {
        if (mData==null){
            mData=new ArrayList<>();
        }
        mData.clear();
        mData.addAll(list);
        mAdapter.setmData(mData);
    }

    @Override
    public void showProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void showLoadFailMsg() {
        if (isAdded()){
            View view=getActivity()==null?mRecyclerView.getRootView():getActivity().findViewById(R.id.drawer_layout);
            Snackbar.make(view,"加载数据失败",Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefresh() {
        mImagePresenter.loadImageList();
    }
}
