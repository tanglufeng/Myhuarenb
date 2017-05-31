package com.abcs.haiwaigou.local.beans;

import java.util.List;

/**
 * 项目名称：com.abcs.haiwaigou.local.beans
 * 作者：zds
 * 时间：2017/5/11 16:42
 */

public class LocaHotCity {

        public String country_name;
        public List<HotCityEntry> citys;

        public LocaHotCity(String country_name, List<HotCityEntry> citys) {
                this.country_name = country_name;
                this.citys = citys;
        }
}
