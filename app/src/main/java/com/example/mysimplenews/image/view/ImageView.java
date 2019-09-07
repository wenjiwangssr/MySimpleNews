package com.example.mysimplenews.image.view;

import com.example.mysimplenews.beans.ImageBean;

import java.util.List;

public interface ImageView {
    void addImage(List<ImageBean>list);
    void showProgress();
    void hideProgress();
    void showLoadFailMsg();
}
