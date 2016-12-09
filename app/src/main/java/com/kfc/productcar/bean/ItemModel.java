package com.kfc.productcar.bean;

import java.io.Serializable;

/**
 * @author fancheng.kong
 * @CreateTime 2016/12/8  16:33
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class ItemModel implements Serializable {


    public int type;
    public Object object;

    public ItemModel(int type, Object object) {
        this.type = type;
        this.object = object;
    }
}