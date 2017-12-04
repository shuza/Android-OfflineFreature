package me.shuza.offlinefreaturedemo;

import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * :=  created by:  Shuza
 * :=  create date:  12/5/2017
 * :=  (C) CopyRight Shuza
 * :=  www.shuza.me
 * :=  shuza.sa@gmail.com
 * :=  Fun  :  Coffee  :  Code
 **/

public class RealmDAO {
    private Realm realm;

    public RealmDAO(Context context) {
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    public void saveResponse(ResponsePojo data, String url) {
        LogUtil.printLogMessage("save_response", "start");
        Gson gson = new Gson();
        String response = gson.toJson(data);

        DataModel model = new DataModel();
        model.setUrl(url);
        model.setResponse(response);

        realm.beginTransaction();
        realm.insertOrUpdate(model);
        realm.commitTransaction();

        LogUtil.printLogMessage("save_response", "finished");
    }

    public Observable<ResponsePojo> getOfflineResponse(String key) {
        LogUtil.printLogMessage("get_offline_response", key);
        DataModel model = realm.where(DataModel.class).equalTo("url", key).findFirst();
        if (model != null) {
            Gson gson = new Gson();
            ResponsePojo responsePojo = gson.fromJson(model.getResponse(), ResponsePojo.class);
            return Observable.just(responsePojo);
        }
        return Observable.just(new ResponsePojo());
    }
}


















