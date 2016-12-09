package com.kfc.productcar.bean;

import java.util.List;

/**
 * @author fancheng.kong
 * @CreateTime 2016/12/5  13:57
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class Random {
    /**
     * code : 0
     * msg : success
     * data : [{"uid":"68","username":"郭延光","avtar":"http://118.178.227.119/uc_server/data/avatar/000/00/00/68_avatar_small.jpg?1480917275","mobile":"18611799343","service_phone":"13141318880"}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * uid : 68
         * username : 郭延光
         * avtar : http://118.178.227.119/uc_server/data/avatar/000/00/00/68_avatar_small.jpg?1480917275
         * mobile : 18611799343
         * service_phone : 13141318880
         */

        private String uid;
        private String username;
        private String avtar;
        private String mobile;
        private String service_phone;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getAvtar() {
            return avtar;
        }

        public void setAvtar(String avtar) {
            this.avtar = avtar;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getService_phone() {
            return service_phone;
        }

        public void setService_phone(String service_phone) {
            this.service_phone = service_phone;
        }
    }
}
