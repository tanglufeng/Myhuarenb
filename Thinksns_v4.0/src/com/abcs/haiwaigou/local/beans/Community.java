package com.abcs.haiwaigou.local.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zjz on 2016/9/1.
 */
public class Community implements Serializable{


    private String  id;
    private String icon;
    private String orgExplain;
    private int orgMemberNum;
    private String orgName;

    private String  isActive;
    private String isActiveView;

    private List<LablesBean> lables;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOrgExplain() {
        return orgExplain;
    }

    public void setOrgExplain(String orgExplain) {
        this.orgExplain = orgExplain;
    }

    public int getOrgMemberNum() {
        return orgMemberNum;
    }

    public void setOrgMemberNum(int orgMemberNum) {
        this.orgMemberNum = orgMemberNum;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsActiveView() {
        return isActiveView;
    }

    public void setIsActiveView(String isActiveView) {
        this.isActiveView = isActiveView;
    }

    public List<LablesBean> getLables() {
        return lables;
    }

    public void setLables(List<LablesBean> lables) {
        this.lables = lables;
    }

    public static class LablesBean {
        private String lable;

        public String getLable() {
            return lable;
        }

        public void setLable(String lable) {
            this.lable = lable;
        }
    }
}
