package com.kfc.productcar.bean;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/25  10:26
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class UserInfo {
    /**
     * code : 0
     * msg : success
     * data : {"groupid":"11","grouptitle":"注册会员","extcredits1":18,"extcredits2":"36","avatar":"http://118.178.227.119/uc_server/data/avatar/000/00/00/03_avatar_middle.jpg?1480040211","sightml":"","realname":"你猜","sex":"1","birth":"2016.4.9","birthprovince":"山西省","birthcity":"太原市","birthdist":"尖草坪区","resideprovince":"云南省","residecity":"思茅市","residedist":"翠云区"}
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

    @Override
    public String toString() {
        return "UserInfo{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public static class DataBean {
        /**
         * groupid : 11
         * grouptitle : 注册会员
         * extcredits1 : 18
         * extcredits2 : 36
         * avatar : http://118.178.227.119/uc_server/data/avatar/000/00/00/03_avatar_middle.jpg?1480040211
         * sightml :
         * realname : 你猜
         * sex : 1
         * birth : 2016.4.9
         * birthprovince : 山西省
         * birthcity : 太原市
         * birthdist : 尖草坪区
         * resideprovince : 云南省
         * residecity : 思茅市
         * residedist : 翠云区
         */

        private String groupid;
        private String grouptitle;
        private int extcredits1;
        private String extcredits2;
        private String avatar;
        private String sightml;
        private String realname;
        private String sex;
        private String birth;
        private String birthprovince;
        private String birthcity;
        private String birthdist;
        private String resideprovince;
        private String residecity;
        private String residedist;

        public String getGroupid() {
            return groupid;
        }

        public void setGroupid(String groupid) {
            this.groupid = groupid;
        }

        public String getGrouptitle() {
            return grouptitle;
        }

        public void setGrouptitle(String grouptitle) {
            this.grouptitle = grouptitle;
        }

        public int getExtcredits1() {
            return extcredits1;
        }

        public void setExtcredits1(int extcredits1) {
            this.extcredits1 = extcredits1;
        }

        public String getExtcredits2() {
            return extcredits2;
        }

        public void setExtcredits2(String extcredits2) {
            this.extcredits2 = extcredits2;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getSightml() {
            return sightml;
        }

        public void setSightml(String sightml) {
            this.sightml = sightml;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getBirthprovince() {
            return birthprovince;
        }

        public void setBirthprovince(String birthprovince) {
            this.birthprovince = birthprovince;
        }

        public String getBirthcity() {
            return birthcity;
        }

        public void setBirthcity(String birthcity) {
            this.birthcity = birthcity;
        }

        public String getBirthdist() {
            return birthdist;
        }

        public void setBirthdist(String birthdist) {
            this.birthdist = birthdist;
        }

        public String getResideprovince() {
            return resideprovince;
        }

        public void setResideprovince(String resideprovince) {
            this.resideprovince = resideprovince;
        }

        public String getResidecity() {
            return residecity;
        }

        public void setResidecity(String residecity) {
            this.residecity = residecity;
        }

        public String getResidedist() {
            return residedist;
        }

        public void setResidedist(String residedist) {
            this.residedist = residedist;
        }
    }
}
