package com.kfc.productcar.bean;

/**
 * @author fancheng.kong
 * @CreateTime 2016/11/29  11:34
 * @PackageName com.kfc.productcar.bean
 * @ProjectName ProductCar
 * @Email kfc1301478241@163.com
 */

public class Modles {
    /**
     * code : 0
     * msg : success
     * data : [{"url":"http://182.92.6.207:8081/imgad/20161114/top_e.html","title":"奔驰E级轿车","img":"http://118.178.227.119/data/attachment/forum/201611/14/193146gkwwjk3w3jmu3y13.png"},{"url":"http://123.57.17.189:8085/a/top_one.html","title":"S320L商务型","img":"http://118.178.227.119/data/attachment/forum/201611/07/144311p485hhdgfhg87gfh.png"},{"url":"http://i.svrvr.com/?a=wapview&id=v31038&code=041O3CtW0osvaY1v0FwW0g7ztW0O3Ctm&state=blinq","title":"BJ 80VR展示","img":"http://118.178.227.119/data/attachment/forum/201611/14/193424w3fkw9oac3993fs3.png"}]
     */


        /**
         * url : http://182.92.6.207:8081/imgad/20161114/top_e.html
         * title : 奔驰E级轿车
         * img : http://118.178.227.119/data/attachment/forum/201611/14/193146gkwwjk3w3jmu3y13.png
         */

        private String url;
        private String title;
        private String img;

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

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

}
