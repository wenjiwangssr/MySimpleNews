package com.example.mysimplenews.news.widget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.example.mysimplenews.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {
public static final int NEWS_TYPE_TOP=0;
public static final int NEWS_TYPE_NBA=1;
public static final int NEWS_TYPE_CARS=2;
public static final int NEWS_TYPE_JOKES=3;
private TabLayout mTabLayout;
private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_news,null);
        mTabLayout=(TabLayout)view.findViewById(R.id.tab_layout);
        mViewPager=(ViewPager)view.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);

        setupViewPager(mViewPager);

        mTabLayout.addTab(mTabLayout.newTab().setText("头条"));
        mTabLayout.addTab(mTabLayout.newTab().setText("NBA"));
        mTabLayout.addTab(mTabLayout.newTab().setText("汽车"));
        mTabLayout.addTab(mTabLayout.newTab().setText("笑话"));
        mTabLayout.setupWithViewPager(mViewPager);


        return view;
    }
    private void setupViewPager(ViewPager mViewPager){
        //fragment中嵌套使用fragment一定要用getChildFragmentManager(),否则会报错
        MyPagerAdapter adapter=new MyPagerAdapter(getChildFragmentManager());
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_TOP),getString(R.string.top));
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_NBA),"NBA");
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_CARS),"汽车");
        adapter.addFragment(NewsListFragment.newInstance(NEWS_TYPE_JOKES),"笑话");
        mViewPager.setAdapter(adapter);

    }
    public static class MyPagerAdapter extends FragmentPagerAdapter{
        private final List<Fragment> mFragments=new ArrayList<Fragment>();
        private final List<String>mFragmentTitle=new ArrayList<String>();
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment,String title){
            mFragments.add(fragment);
            mFragmentTitle.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitle.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();


        }
    }
}
