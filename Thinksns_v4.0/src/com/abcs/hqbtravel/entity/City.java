package com.abcs.hqbtravel.entity;

import java.io.Serializable;

/**
 * author zaaach on 2016/1/26.
 */
public class City implements Serializable{

    private String name;
    private String pinyin;
    /**
     * areaCnName : 亚洲
     * areaId : 10
     * areaEnName : Asia
     */

    private String areaCnName;
    private String  areaId;
    private String areaEnName;
    /**
     * countryId : 11
     * cityCount : 41
     * countryCnName : 中国
     * countryEnName : China
     */


    private String  countryId;
    private String cityCount;
    private String countryCnName;
    private String countryEnName;
    private String countryInitial;
    /**
     * city_id : 50
     * catename_en : Hong Kong
     * cate_name : 香港
     */

    private String city_id;
    private String catename_en;
    private String cate_name;

    public City() {}

    public City(String name, String pinyin) {
        this.name = name;
        this.pinyin = pinyin;
    }

    public String getCountryInitial() {
        return countryInitial;
    }

    public void setCountryInitial(String countryInitial) {
        this.countryInitial = countryInitial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getAreaCnName() {
        return areaCnName;
    }

    public void setAreaCnName(String areaCnName) {
        this.areaCnName = areaCnName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaEnName() {
        return areaEnName;
    }

    public void setAreaEnName(String areaEnName) {
        this.areaEnName = areaEnName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCityCount() {
        return cityCount;
    }

    public void setCityCount(String cityCount) {
        this.cityCount = cityCount;
    }

    public String getCountryCnName() {
        return countryCnName;
    }

    public void setCountryCnName(String countryCnName) {
        this.countryCnName = countryCnName;
    }

    public String getCountryEnName() {
        return countryEnName;
    }

    public void setCountryEnName(String countryEnName) {
        this.countryEnName = countryEnName;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCatename_en() {
        return catename_en;
    }

    public void setCatename_en(String catename_en) {
        this.catename_en = catename_en;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }
}
