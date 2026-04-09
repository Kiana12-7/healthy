package com.example.myapplication.utils;

import android.content.Context;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

public class OkHttpUtil {

    private static OkHttpClient client;

    // 全局获取一个带自动Cookie管理的 OkHttpClient
    public static OkHttpClient getClient(Context context) {
        if (client == null) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .cookieJar(PersistentCookieJar.getInstance(context))
                    .build();
        }
        return client;
    }

    public static OkHttpClient getClientWithLogging(Context context) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        return getClient(context).newBuilder()
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
    }
}