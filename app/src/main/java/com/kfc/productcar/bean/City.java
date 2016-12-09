package com.kfc.productcar.bean;

import com.kfc.productcar.utils.PinYinUtils;

/**
 * author zaaach on 2016/1/26.
 */
public class City implements Comparable<City>{
        private String id;
        private String name;
        private String firstchar;

        public void setFirstchar(String firstchar) {
        this.firstchar = firstchar;
        }

        public String getFirstchar() {
        return firstchar;
        }
        public City(String name) {
                this.name = name;
                firstchar = PinYinUtils.getPinyin(name);
        }


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


        @Override
        public int compareTo(City city) {
                return firstchar.compareTo(city.getFirstchar());
        }

        @Override
        public String toString() {
                return "City{" +
                        "name='" + name + '\'' +
                        ", pinYin='" + firstchar + '\'' +
                        '}';
        }
}
