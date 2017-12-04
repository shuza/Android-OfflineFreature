package me.shuza.offlinefreaturedemo;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * :=  created by:  Shuza
 * :=  create date:  11/15/2017
 * :=  (C) CopyRight Shuza
 * :=  www.shuza.me
 * :=  shuza.sa@gmail.com
 * :=  Fun  :  Coffee  :  Code
 **/

public class NetworkManager {
    public static String BASE_URL = "http://shuza.me/tmp_file/";

    public static Retrofit getRetrofit(Context context) {
        File httpCacheFile = new File(context.getCacheDir(), "demoHttpCache");
        Cache cache = new Cache(httpCacheFile, 10 * 1024 * 1024);

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(chain -> {
                    try {
                        LogUtil.printLogMessage("online request", "try to get response from server");
                        return chain.proceed(chain.request());
                    } catch (Exception e) {
                        Request offlineRequest = chain.request().newBuilder()
                                .header("Cache-Control", "public, only-if-cached, "
                                        + "max-stale=" + 60 * 60 * 24)
                                .build();
                        LogUtil.printLogMessage("offline request", "get response from cache");
                        return chain.proceed(offlineRequest);
                    }
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(BASE_URL)
                .build();
        return retrofit;

    }
}