package com.abcs.huaqiaobang.tljr.news.bean;

public class CountrySortModel {

    private String sortLetters;
    private String desc;
    private String country;
    private String date;
    private String country_imgurl;
    private String species;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    private String subject;

    public String getCountry_imgurl() {
        return country_imgurl;
    }

    public void setCountry_imgurl(String country_imgurl) {
        this.country_imgurl = country_imgurl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
