package com.example.mysimplenews.utils;

/**
 * Created by asus on 2018/5/2.
 */


import com.example.mysimplenews.main.widget.App;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * OkHttp的工具类
 * <p>
 * OKHttp具有高并发的优点，而且为了避免重复创建销毁对象造成内存开销。
 * 此处我们将OkHttpUtils封装为单利模式
 * </p>
 */
public class OkHttpUtils {

    private OkHttpClient okHttpClient;
    private static OkHttpUtils okHttpUtils;

    /**
     * 构造函数私有化
     */
    private OkHttpUtils() {


        okHttpClient = new OkHttpClient.Builder()

//                .cache(cache)
                .build();
    }



    /**
     * 获取Cache对象
     *
     * @return
     */
    private Cache getCache() {
        // App.context.getCacheDir()获取App的dir目录   文件大小
        Cache cache = new Cache(App.context.getCacheDir(), 1024 * 1024 * 10);
        return cache;
    }

    /**
     * 提供一个公共的，静态的，返回值为当前类的本身的方法
     *
     * @return 本类对象
     */
    public static OkHttpUtils getInstance() {
        if (okHttpUtils == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpUtils == null) {
                    okHttpUtils = new OkHttpUtils();
                }
            }
        }
        return okHttpUtils;
    }


    /**
     * 发送无参的get请求
     *
     * @param url      请求地址
     * @param callBack 请求的回调
     */
    public void doGet(String url, final MyNetWorkCallBack callBack) {
        Request request = new Request.Builder()
                .url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                App.context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onError(e);
                            }
                        });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();

                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(responseStr);
                    }
                });
//                callBack.onSuccess(responseStr);


            }
        });
    }

    /**
     * 发送同步的get请求
     *
     * @return 请求结果转为字符串
     */
    public String doGetTong(String url) {
        Request request = new Request.Builder().url(url).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            String responseStr = response.body().string();
            return responseStr;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 带参数的get请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 请求的回调
     */
    public void doGet(String url, Map<String, String> params, final MyNetWorkCallBack callBack) {
        if (!params.isEmpty() && params != null) {
            StringBuilder sbUrl = new StringBuilder(url);
            sbUrl.append("?");
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                sbUrl.append(key).append("=").append(params.get(key)).append("&");
            }
            url = sbUrl.substring(0, sbUrl.length() - 1);
        }
        Request request = new Request.Builder()
                .url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }


            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(responseStr);
                    }
                });
            }
        });
    }


    /**
     * 发送带请求头的get请求
     *
     * @param url      请求地址
     * @param headers  请求头
     * @param callBack 请求的回调
     */
    public void doGetHeader(String url, Map<String, String> headers, final MyNetWorkCallBack callBack) {
        Request.Builder builder = new Request.Builder().url(url);
        if (headers.isEmpty() && headers != null) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseStr = response.body().string();

                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(responseStr);
                    }
                });
            }
        });
    }

    /**
     * 发送无参的post请求
     * 无参的post请求与get请求没有区别，完全可以当做无参的get请求处理
     * 就不写了，直接看无参的get请求即可
     *
     * @param url      请求地址
     * @param callBack 请求的回调
     */
//    public void doPost(String url, MyNetWorkCallBack callBack) {
//        doGet(url, callBack,c);
//    }

    /**
     * 发送同的post请求
     * 无参同步的post请求与同步get请求没有区别，完全可以当做无参同步的get请求处理
     * 就不写了，直接看无参同步的get请求即可
     *
     * @return 请求结果转为字符串
     */
    public String doPostTong(String url) {
        String request = doGetTong(url);
        return request;
    }


    /**
     * 发送有参的post请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callBack 请求的回调
     */
    public void doPost(String url, Map<String, String> params, final MyNetWorkCallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params.isEmpty() && params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                builder.add(key, params.get(key));
            }
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().post(formBody).url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseStr = response.body().string();

                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(responseStr);
                    }
                });
            }
        });
    }

    /**
     * 发送带请求头的post请求
     *
     * @param url      请求地址
     * @param headers  请求头信息
     * @param callBack 请求的回调
     */
    public void doPostHeader(String url, Map<String, String> headers, final MyNetWorkCallBack callBack) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        /**
         * 添加参数
         */
        FormBody formBody = formBuilder.build();
        Request.Builder builder = new Request.Builder().post(formBody);
        if (headers.isEmpty() && headers != null) {
            Set<String> keySet = headers.keySet();
            for (String key : keySet) {
                builder.addHeader(key, headers.get(key));
            }
        }
        Request request = builder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                //切换回主线程循环执行
                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String responseStr = response.body().string();

                App.context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(responseStr);
                    }
                });
            }
        });

    }

}
