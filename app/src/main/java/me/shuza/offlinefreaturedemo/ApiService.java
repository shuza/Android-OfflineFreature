package me.shuza.offlinefreaturedemo;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * :=  created by:  Shuza
 * :=  create date:  11/15/2017
 * :=  (C) CopyRight Shuza
 * :=  www.shuza.me
 * :=  shuza.sa@gmail.com
 * :=  Fun  :  Coffee  :  Code
 **/

public interface ApiService {
    @POST("dummy_response.php")
    Observable<ResponsePojo> getDataFromAPI();
}
