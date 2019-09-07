package com.example.mysimplenews.main.widget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.mysimplenews.R;
import com.example.mysimplenews.about.widget.AboutFragment;
import com.example.mysimplenews.image.widget.ImageFragment;
import com.example.mysimplenews.main.presenter.MainPresenter;
import com.example.mysimplenews.main.presenter.MainPresenterImpl;
import com.example.mysimplenews.main.view.MainView;
import com.example.mysimplenews.news.widget.NewsFragment;
import com.google.android.material.navigation.NavigationView;

import weather.widget.WeatherFragment;

public class MainActivity extends AppCompatActivity implements MainView {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;//监听器
    private Toolbar mToolbar;
    private NavigationView mNavigationView;
    private MainPresenter mMainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        App.context =this;//供工具类OkhttpUtils中runOnUiThread使用

        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerToggle=new ActionBarDrawerToggle(this,mDrawerLayout,mToolbar,R.string.drawer_open,R.string.drawer_close){
            //监听器 drawLayout打开，关闭，滑动时调用
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };

        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //navigationView 就是上面一个头像，下面几个item，通过布局文件中app:headerLayout，app:menu设置
        mNavigationView=(NavigationView)findViewById(R.id.navigation_view);
        setupDrawerContent(mNavigationView);

        mMainPresenter=new MainPresenterImpl(this);
        switch2News();//默认新闻界面
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    //设置边侧栏监听
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mMainPresenter.switchNavigation(menuItem.getItemId());
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public void switch2News() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new NewsFragment()).commit();
        //replace 关闭R.id.frame_content视图的所有fragment，新建一个fragment
        mToolbar.setTitle(R.string.navigation_news);
    }

    @Override
    public void switch2Images() {


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new ImageFragment()).commit();
        mToolbar.setTitle(R.string.navigation_images);

    }

    @Override
    public void switch2Weather() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new WeatherFragment()).commit();
        mToolbar.setTitle(R.string.navigation_weather);
    }

    @Override
    public void switch2About() {

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new AboutFragment()).commit();
        mToolbar.setTitle(R.string.navigation_about);

    }
}
