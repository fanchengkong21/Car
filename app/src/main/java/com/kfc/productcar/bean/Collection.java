package com.kfc.productcar.bean;

/**
 * @author fancheng.kong
 * @CreateTime 2016/12/1  14:00
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class Collection {

        private String url;
        private String title;
        private String forumname;
        private String recommend_add;
        private int favtimes;
        private String pid;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getForumname() {
            return forumname;
        }

        public void setForumname(String forumname) {
            this.forumname = forumname;
        }

        public String getRecommend_add() {
            return recommend_add;
        }

        public void setRecommend_add(String recommend_add) {
            this.recommend_add = recommend_add;
        }

        public int getFavtimes() {
            return favtimes;
        }

        public void setFavtimes(int favtimes) {
            this.favtimes = favtimes;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

}
