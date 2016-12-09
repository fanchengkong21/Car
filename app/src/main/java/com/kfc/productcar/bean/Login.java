package com.kfc.productcar.bean;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/20  11:45
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class Login {


    /**
     * code : 0
     * msg : 欢迎您回来，注册会员 111，现在将转入登录前页面
     * data : {"username":"111","usergroup":"注册会员","uid":"3","groupid":"11","syn":0,"owngroup":"0","owngroupname":""}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * username : 111
         * usergroup : 注册会员
         * uid : 3
         * groupid : 11
         * syn : 0
         * owngroup : 0
         * owngroupname :
         */

        private String username;
        private String usergroup;
        private String uid;
        private String groupid;
        private int syn;
        private String owngroup;
        private String owngroupname;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getUsergroup() {
            return usergroup;
        }

        public void setUsergroup(String usergroup) {
            this.usergroup = usergroup;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public int getSyn() {
            return syn;
        }

        public void setSyn(int syn) {
            this.syn = syn;
        }

        public String getOwngroup() {
            return owngroup;
        }

        public void setOwngroup(String owngroup) {
            this.owngroup = owngroup;
        }

        public String getOwngroupname() {
            return owngroupname;
        }

        public void setOwngroupname(String owngroupname) {
            this.owngroupname = owngroupname;
        }
    }
}
