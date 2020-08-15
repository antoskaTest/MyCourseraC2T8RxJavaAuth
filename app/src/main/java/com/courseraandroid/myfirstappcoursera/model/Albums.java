package com.courseraandroid.myfirstappcoursera.model;

import java.io.Serializable;
import java.util.List;

public class Albums {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 0
         * name : string
         * songs_count : 0
         * release_date : 2020-08-06T15:57:35.021Z
         */

        private int id;
        private String name;
        private int songs_count;
        private String release_date;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSongs_count() {
            return songs_count;
        }

        public void setSongs_count(int songs_count) {
            this.songs_count = songs_count;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }
    }
}
