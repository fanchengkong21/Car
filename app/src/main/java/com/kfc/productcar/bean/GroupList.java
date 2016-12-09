package com.kfc.productcar.bean;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/23  13:56
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class GroupList {
    private String id;
    private String name;
    private String img;
    private String province;
    private String city;
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
