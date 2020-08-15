package com.courseraandroid.myfirstappcoursera.model;

import java.util.List;

public class Album {

    /**
     * data : {"id":0,"name":"string","songs":[{"id":0,"name":"string","duration":"string"}],"release_date":"2020-08-06T15:52:03.253Z"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 0
         * name : string
         * songs : [{"id":0,"name":"string","duration":"string"}]
         * release_date : 2020-08-06T15:52:03.253Z
         */

        private int id;
        private String name;
        private String release_date;
        private List<Song.DataBean> songs;

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

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public List<Song.DataBean> getSongs() {
            return songs;
        }

        public void setSongs(List<Song.DataBean> songs) {
            this.songs = songs;
        }

    }
}
