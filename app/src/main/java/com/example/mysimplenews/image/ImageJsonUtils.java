package com.example.mysimplenews.image;

import com.example.mysimplenews.beans.ImageBean;
import com.example.mysimplenews.utils.JsonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class ImageJsonUtils {
    public static final String TAG="ImageJsonUtils";

    /**
     * Read json image beans list.
     * 将获取到的json转换为图片列表对象
     * @param resource the resource
     * @return the list
     */
    public static List<ImageBean> readJsonImageBeans(String resource){

        List<ImageBean> list=new ArrayList<>();
        JsonParser parser=new JsonParser();
        JsonArray jsonArray=parser.parse(resource).getAsJsonArray();
        for (int i = 0; i <jsonArray.size() ; i++) {
            JsonObject object=jsonArray.get(i).getAsJsonObject();
            ImageBean imageBean= JsonUtils.deserialize(object,ImageBean.class);
            list.add(imageBean);
        }
        return list;
    }
}
