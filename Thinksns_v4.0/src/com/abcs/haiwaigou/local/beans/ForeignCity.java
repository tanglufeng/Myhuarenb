package com.abcs.haiwaigou.local.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjz on 2016/8/30.
 */
public class ForeignCity implements Serializable{

    private String  countryId;
    private String keyTitle;
    private String countryEnName;

    private List<HrqCitiesBean> hrqCities;
    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getKeyTitle() {
        return keyTitle;
    }

    public void setKeyTitle(String keyTitle) {
        this.keyTitle = keyTitle;
    }

    public String getCountryEnName() {
        return countryEnName;
    }

    public void setCountryEnName(String countryEnName) {
        this.countryEnName = countryEnName;
    }

    public List<HrqCitiesBean> getHrqCities() {
        return hrqCities;
    }

    public void setHrqCities(List<HrqCitiesBean> hrqCities) {
        this.hrqCities = hrqCities;
    }

    public static class HrqCitiesBean {
        private String  createTime;
        private String cityName;
        private String cityId;
        private String countryEnName;
        private String id;

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public Object getCountryEnName() {
            return countryEnName;
        }

        public void setCountryEnName(String countryEnName) {
            this.countryEnName = countryEnName;
        }

        public Object getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
