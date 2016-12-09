package com.kfc.productcar.bean;

import java.util.List;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/20  11:45
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class User {


    private int code;
    private String msg;
    private List<?> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
