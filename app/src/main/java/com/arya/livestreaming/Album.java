package com.arya.livestreaming;


public class Album {
    private String name;
    private int numOfSongs;
    private int thumbnail;
    private String playBackURL;

    public Album() {
    }

    public Album(String playBackURL, String name, int numOfSongs, int thumbnail) {
        this.playBackURL = playBackURL;
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayBackURL() {
        return playBackURL;
    }

    public void setPlayBackURL(String playBackURL) {
        this.playBackURL = playBackURL;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
