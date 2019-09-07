package weather.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.example.mysimplenews.beans.WeatherBean;
import com.example.mysimplenews.common.Urls;
import com.example.mysimplenews.utils.LogUtils;
import com.example.mysimplenews.utils.MyNetWorkCallBack;
import com.example.mysimplenews.utils.OkHttpUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import weather.WeatherJsonUtils;

public class WeatherModelImpl implements WeatherModel {

    public static final String TAG="WeatherModelImpl";

    @Override
    public void loadWeatherData(String cityName, final LoadWeatherListener listener) {
//        try {
//            String url= Urls.WEATHER + URLEncoder.encode(cityName,"utf-8");
//            OkHttpUtils.RealCallback callback=new OkHttpUtils.RealCallback() {
//                @Override
//                public void onResponse(Call call, Response response) {
//                    List<WeatherBean> list= null;
//                    try {
//                        list = WeatherJsonUtils.getWeatherInfo(response.body().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    listener.onSuccess(list);
//                }
//
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    listener.onFailure("load weather data failure",e);
//                }
//            };
//            OkHttpUtils.getmInstance().get(url,callback);
//        } catch (UnsupportedEncodingException e) {
//            Log.e(TAG,"url encode failure",e);
//        }
        try {

            String url= Urls.WEATHER + URLEncoder.encode(cityName,"utf-8");

            OkHttpUtils.getInstance().doGet(url, new MyNetWorkCallBack() {
                @Override
                public void onError(Exception e) {

                }

                @Override
                public void onSuccess(String successMsg) {
                    List<WeatherBean> list=null;
                    list=WeatherJsonUtils.getWeatherInfo(successMsg);
                    listener.onSuccess(list);
                }
            });


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void loadLocation(Context context, final LoadLocationListener listener) {

        LocationManager locationManager=(LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            &&context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                Log.e(TAG,"loaction failure");
                listener.onFailure("location failure",null);
                return;
            }
        }
        Location location =locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location==null){
            Log.e(TAG,"location failure");
            listener.onFailure("location failure",null);
            return;
        }
        double latitude=location.getLatitude();
        double longitude=location.getLongitude();
        
        String url=getLocationURL(latitude,longitude);
//        OkHttpUtils.RealCallback callback=new OkHttpUtils.RealCallback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                String city = null;
//                try {
//                    city = WeatherJsonUtils.getCity(response.body().string());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                if (TextUtils.isEmpty(city)){
//                    LogUtils.e(TAG, "load location info failure.");
//                    listener.onFailure("load location info failure.", null);
//                }else listener.onSuccess(city);
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                LogUtils.e(TAG, "load location info failure.");
//                listener.onFailure("load location info failure.", e);
//            }
//        };
//        OkHttpUtils.getmInstance().get(url,callback);

        OkHttpUtils.getInstance().doGet(url,new MyNetWorkCallBack() {
            @Override
            public void onError(Exception e) {

            }

            @Override
            public void onSuccess(String successMsg) {
                String city=null;
                city=WeatherJsonUtils.getCity(successMsg);
                listener.onSuccess(city);
            }
        });



    }

    private String getLocationURL(double latitude, double longitude) {
        StringBuffer bf=new StringBuffer(Urls.INTERFACE_LOCATION);
        bf.append("?output=json").append("&referer=32D45CBEEC107315C553AD1131915D366EEF79B4");
        bf.append("&location=").append(latitude).append(",").append(longitude);
        Log.d(TAG,bf.toString());
        return bf.toString();
    }

    public interface LoadWeatherListener{
    void onSuccess(List<WeatherBean> list);
    void onFailure(String msg,Exception e);
}

    public interface LoadLocationListener{
        void onSuccess(String cityName);
        void onFailure(String msg,Exception e);
    }
}
