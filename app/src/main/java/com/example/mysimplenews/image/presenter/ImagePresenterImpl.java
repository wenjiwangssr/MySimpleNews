package com.example.mysimplenews.image.presenter;



import com.example.mysimplenews.beans.ImageBean;
import com.example.mysimplenews.image.model.ImageModel;
import com.example.mysimplenews.image.model.ImageModelImpl;
import com.example.mysimplenews.image.view.ImageView;

import java.util.List;

public class ImagePresenterImpl implements ImagePresenter, ImageModelImpl.onLoadImageListListener {
    private ImageModel mImageModel;
    private ImageView mImageView;

    public ImagePresenterImpl(ImageView imageView) {
        this.mImageView = imageView;
        this.mImageModel=new ImageModelImpl();
    }

    @Override
    public void onSuccess(List<ImageBean> list) {
        mImageView.addImage(list);
        mImageView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mImageView.hideProgress();
        mImageView.showLoadFailMsg();
    }

    @Override
    public void loadImageList() {
        mImageView.showProgress();
        mImageModel.loadImageList(this);
    }
}
