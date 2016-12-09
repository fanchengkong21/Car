package com.kfc.productcar.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author fancheng.kong
 * @CreateTime 2016/8/18 13:31
 * @PackageName com.kfc.carcontrol.utils
 * @ProjectName CarControl
 * @Email kfc1301478241@163.com
 */

/**
 * 网络工具类
 */
public class NetWorkUtils {
    private final Context mContext;
    public NetworkInfo.State wifiState = null;
    public NetworkInfo.State mobileState = null;

    public NetWorkUtils(Context context) {
        mContext = context;
    }

    public enum NetWorkState {
        WIFI, MOBILE, NONE;
    }

    /**
     * 获取当前的网络状态
     *
     * @return
     */
    public NetWorkState getConnectState() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        manager.getActiveNetworkInfo();
        wifiState = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
        mobileState = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
        if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
            return NetWorkState.MOBILE;
        } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState
                && NetworkInfo.State.CONNECTED != mobileState) {
            return NetWorkState.NONE;
        } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
            return NetWorkState.WIFI;
        }
        return NetWorkState.NONE;
    }
}
