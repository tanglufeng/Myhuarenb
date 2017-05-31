package com.abcs.sociax.t4.android.bean;/**
 * Created by Administrator on 2017/2/18.
 */

/**
 * User: zds
 * Date: 2017-02-18
 * Time: 14:55
 * FIXME
 */
public class CeSu {


    /**
     * id : 1
     * server_name : 中国
     * server_url : https://newapi.tuling.me
     */

    public String id;
    public String currWanSu;
    public String serverName;
    public String serverUrl;

    public String getCurrWanSu() {
        return currWanSu;
    }

    public void setCurrWanSu(String currWanSu) {
        this.currWanSu = currWanSu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
