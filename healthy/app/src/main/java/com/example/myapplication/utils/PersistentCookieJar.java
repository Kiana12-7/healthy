package com.example.myapplication.utils;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PersistentCookieJar implements CookieJar {
    private static final String COOKIE_PREFS = "okhttp_cookies";
    private final SharedPreferences cookiePrefs;

    // 内存缓存
    private final Map<String, List<Cookie>> cookieStore = new ConcurrentHashMap<>();

    // 单例
    private static volatile PersistentCookieJar instance;

    public static PersistentCookieJar getInstance(Context context) {
        if (instance == null) {
            synchronized (PersistentCookieJar.class) {
                if (instance == null) {
                    instance = new PersistentCookieJar(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private PersistentCookieJar(Context context) {
        Log.d("CookieJar", "CookieJar instance created: " + this.hashCode());
        cookiePrefs = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        loadCookiesFromLocal();
    }

    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        Log.d("CookieJar", "Save from: " + url.host() + ", Count: " + cookies.size());

        if (!cookies.isEmpty()) {
            String host = url.host();

            // 1. 创建一个全新的列表，用于存放最终要保存的 Cookie
            List<Cookie> newCookies = new ArrayList<>();

            // 2. 获取旧的 Cookie 列表（如果有）
            List<Cookie> oldCookies = cookieStore.get(host);

            // 3. 把旧的 Cookie 加进去（排除掉即将被替换的同名 Cookie）
            if (oldCookies != null) {
                for (Cookie oldCookie : oldCookies) {
                    boolean shouldKeep = true;
                    for (Cookie newCookie : cookies) {
                        if (oldCookie.name().equals(newCookie.name())) {
                            shouldKeep = false; // 发现同名新 Cookie，旧的就不要了
                            break;
                        }
                    }
                    if (shouldKeep) {
                        newCookies.add(oldCookie);
                    }
                }
            }

            // 4. 把新的 Cookie 加进去（顺便过滤掉已过期的）
            long now = System.currentTimeMillis();
            for (Cookie cookie : cookies) {
                Log.d("CookieJar", "Saving: " + cookie.name() + "=" + cookie.value());
                if (cookie.expiresAt() > now) {
                    newCookies.add(cookie);
                }
            }

            // 5. 用新列表完全替换旧列表
            cookieStore.put(host, newCookies);

            // 6. 持久化到本地
            saveCookiesToLocal(host, newCookies);
        }
    }

    @NonNull
    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {
        Log.d("CookieJar", "Load for: " + url.host() + ", Instance: " + this.hashCode());

        List<Cookie> cookies = cookieStore.get(url.host());
        List<Cookie> validCookies = new ArrayList<>();

        if (cookies != null) {
            long now = System.currentTimeMillis();
            for (Cookie cookie : cookies) {
                if (cookie.expiresAt() > now) {
                    validCookies.add(cookie);
                }
            }
        }

        Log.d("CookieJar", "Loaded: " + validCookies);
        return validCookies;
    }

    private void loadCookiesFromLocal() {
        Map<String, ?> allPrefs = cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : allPrefs.entrySet()) {
            String host = entry.getKey();
            String setCookieStr = (String) entry.getValue();

            try {
                // 使用 "Set-Cookie" 头来解析
                Headers headers = Headers.of("Set-Cookie", setCookieStr);
                HttpUrl httpUrl = Objects.requireNonNull(HttpUrl.parse("http://" + host));

                List<Cookie> cookies = Cookie.parseAll(httpUrl, headers);
                if (!cookies.isEmpty()) {
                    cookieStore.put(host, cookies);
                }
            } catch (Exception e) {
                Log.e("CookieJar", "Failed to parse cookie for host: " + host, e);
            }
        }
    }

    private void saveCookiesToLocal(String host, List<Cookie> cookies) {
        if (cookies.isEmpty()) {
            cookiePrefs.edit().remove(host).apply();
            return;
        }

        StringBuilder sb = new StringBuilder();
        if (!cookies.isEmpty()) {
            sb.append(cookies.get(0).toString());
        }

        cookiePrefs.edit().putString(host, sb.toString()).apply();
    }

    public void clearCookies() {
        cookieStore.clear();
        cookiePrefs.edit().clear().apply();
    }
}