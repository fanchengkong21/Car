package com.kfc.productcar.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/28  21:38
 * @PackageName com.kfc.productcar.utils
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        Glide.with(context).load(path).into(imageView);
    }
}
