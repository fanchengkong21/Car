package com.kfc.productcar.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author fancheng.kong
 * @CreateTime 2016/8/10 15:10
 * @PackageName com.kfc.computercollected.utils
 * @ProjectName ComputerCollected
 * @Email kfc1301478241@163.com
 */

public class ReadJson {
    /**
     * 从assets路径下读取对应的json文件转String输出
     * @param mContext
     * @param fileName
     * @return
     */
    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();

    }
}
