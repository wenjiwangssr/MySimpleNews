package com.example.mysimplenews.utils;



import android.os.Handler;
import android.os.Looper;
import android.text.GetChars;

import com.google.gson.internal.$Gson$Types;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


import okhttp3.Call;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The type Ok http utils.
 */
public class OkHttpUtils {
    private static final String TAG = "OkHttpUtils";

    private static OkHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private static Handler mHandler;

    private OkHttpUtils(){
        mOkHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl,List<Cookie>> cookieStore=new HashMap<>();
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        cookieStore.put(httpUrl,list);
                    }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        List<Cookie> cookies=cookieStore.get(httpUrl);
                        return cookies!=null?cookies:new ArrayList<Cookie>();
                    }
                })
                .build();

        //获取主线程的handler
        mHandler=new Handler(Looper.getMainLooper());
    }
    public static OkHttpUtils getmInstance(){
        if (mInstance == null){
            synchronized (OkHttpUtils.class){
                if (mInstance== null){
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }


    /**
     * 构造Get请求，封装对用的Request请求，实现方法
     * @param url  访问路径
     * @param realCallback  接口回调
     */

    private  void getRequest(String url, final RealCallback realCallback){
        Request request=new Request.Builder().url(url).get().build();
        deliveryResult(realCallback,mOkHttpClient.newCall(request));
    }

    /**
     * 构造post 请求，封装对用的Request请求，实现方法
     * @param url 请求的url
     * @param requestBody 请求参数
     * @param realCallback 结果回调的方法
     */
    private void postResult(String url,RequestBody requestBody,final RealCallback realCallback){
        Request request=new Request.Builder().url(url).post(requestBody).build();
        deliveryResult(realCallback,mOkHttpClient.newCall(request));
    }

    /**
     * 处理请求结果的回调：主线程切换
     * @param realCallback
     * @param call
     */
    private void deliveryResult(final RealCallback realCallback,Call call){
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull final Call call, @NotNull final IOException e) {
                sendFailureTread(call,e,realCallback);
            }

            @Override
            public void onResponse(@NotNull final Call call, @NotNull final Response response) throws IOException {
                sendSuccessTread(call,response,realCallback);
            }
        });

    }

    private void sendSuccessTread(final Call call,final Response response,final RealCallback realCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                realCallback.onResponse(call, response);
            }
        });
    }

    private void sendFailureTread(final Call call,final IOException e,final RealCallback realCallback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                realCallback.onFailure(call, e);
            }
        });
        
    }


    /**********************对外接口************************/

    /**
     * post请求
     * @param url  请求url
     * @param requestBody 请求参数
     * @param realCallback 请求回调
     */
    public  void post(String url, RequestBody requestBody, final RealCallback realCallback) {
        postResult(url,requestBody,realCallback);

    }
    /**
     * get请求
     * @param url  请求url
     * @param realCallback  请求回调
     */
    public  void get(String url, final RealCallback realCallback){
        getRequest(url,realCallback);
    }


    public static abstract class RealCallback{
        /*
         * 请求成功回调*/
        public abstract void onResponse(Call call, Response response);
        /*
         * 请求失败回调*/
        public abstract void onFailure(Call call, IOException e);

    }

    /**
     * post请求参数类
     */
    public static class Param {

        String key;
        String value;

        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

    }





}


