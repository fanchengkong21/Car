package com.kfc.productcar.utils;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/20  11:36
 * @PackageName com.kfc.productcar.utils
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class Cook {

        private static List<Cookie> cookies;

        public static List<Cookie> getCookies() {
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }

        public static void setCookies(List<Cookie> cookies) {
            Cook.cookies = cookies;
        }



}
