package me.shuza.offlinefreaturedemo;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * :=  created by:  Shuza
 * :=  create date:  12/4/2017
 * :=  (C) CopyRight Shuza
 * :=  www.shuza.me
 * :=  shuza.sa@gmail.com
 * :=  Fun  :  Coffee  :  Code
 **/

public class DataModel extends RealmObject {
    @PrimaryKey
    String url;

    String response;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
