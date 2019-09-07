package com.example.mysimplenews.image.model;

import com.example.mysimplenews.beans.ImageBean;
import com.example.mysimplenews.common.Urls;
import com.example.mysimplenews.image.ImageJsonUtils;
import com.example.mysimplenews.utils.MyNetWorkCallBack;
import com.example.mysimplenews.utils.OkHttpUtils;

import java.util.List;

public class ImageModelImpl implements ImageModel {
    @Override
    public void loadImageList(final ImageModelImpl.onLoadImageListListener listener) {
        String url= Urls.IMAGES_URL;
        OkHttpUtils.getInstance().doGet(url, new MyNetWorkCallBack() {
            @Override
            public void onError(Exception e) {
                listener.onFailure("load image list failure",e);
            }

            @Override
            public void onSuccess(String successMsg) {
                List<ImageBean> imageBeanList = ImageJsonUtils.readJsonImageBeans(successMsg);
                listener.onSuccess(imageBeanList);
            }
        });

    }

    public interface onLoadImageListListener{
        void onSuccess(List<ImageBean>list);
        void onFailure(String msg,Exception e);
    }
}
