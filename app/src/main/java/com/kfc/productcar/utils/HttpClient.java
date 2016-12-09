package com.kfc.productcar.utils;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

/**
 * @author fancheng.kong
 * @CreateTime 2016/7/26 10:38
 * @PackageName com.kfc.carcalm
 * @ProjectName CarCalm
 * @Email kfc1301478241@163.com
 */

public class HttpClient {
    private static final String BASE_URL = "http://118.178.227.119/";
    private static AsyncHttpClient client = new AsyncHttpClient();
    static
    {
        client.setTimeout(11000);   //设置链接超时，如果不设置，默认为10s


    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Context context) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);
        client.get(getAbsoluteUrl(url), params, responseHandler);

    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler, Context context) {
        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
        client.setCookieStore(myCookieStore);
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post1(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
