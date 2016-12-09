package com.kfc.productcar.bean;

import java.io.Serializable;

/**
 * @author fancheng.kong
 * @CreateTime 2016/12/8  16:32
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class ChatModel implements Serializable {
    private String type;
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
        private String message;
        private String msgfromid;
        private String msgfrom;
        private String avatar;
        private String dateline;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMsgfromid() {
            return msgfromid;
        }

        public void setMsgfromid(String msgfromid) {
            this.msgfromid = msgfromid;
        }

        public String getMsgfrom() {
            return msgfrom;
        }

        public void setMsgfrom(String msgfrom) {
            this.msgfrom = msgfrom;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDateline() {
            return dateline;
        }

        public void setDateline(String dateline) {
            this.dateline = dateline;
        }

}
