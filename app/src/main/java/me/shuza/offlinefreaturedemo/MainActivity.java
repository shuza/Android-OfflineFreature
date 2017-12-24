package me.shuza.offlinefreaturedemo;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import me.shuza.offlinefreaturedemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private final String END_POINT = "http://www.mocky.io/v2/5a25878e2e0000392aa90676";
    RealmDAO realmDAO;

    private ActivityMainBinding binding;
    private ProgressDialog progressDialog;
    private Observer<ResponsePojo> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnLoad.setOnClickListener((view -> {
            progressDialog.show();
            getDataFromAPI();
        }));

        realmDAO = new RealmDAO(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        subscriber = new Observer<ResponsePojo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ResponsePojo data) {
                progressDialog.dismiss();
                binding.tvName.setText("Name : " + data.getName());
                binding.tvEmail.setText("Email : " + data.getEmail());
                binding.tvBlog.setText("Blog : " + data.getBlog());
                realmDAO.saveResponse(data, END_POINT);
            }

            @Override
            public void onError(Throwable t) {
                LogUtil.printLogMessage("error response", t.getMessage());

                progressDialog.dismiss();
                binding.tvName.setText("error :   " + t.getMessage());
                binding.tvEmail.setText("error");
                binding.tvBlog.setText("error");
            }

            @Override
            public void onComplete() {

            }
        };
    }


    public void getDataFromAPI() {
        Observable<ResponsePojo> observable;
        if (NetworkManager.isInternetAvailable(this)) {
            ApiService apiService = NetworkManager.getRetrofit().create(ApiService.class);
            observable = apiService.getDataFromAPI()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        } else {
            observable = realmDAO.getOfflineResponse(END_POINT);
        }
        observable.subscribe(subscriber);
    }
}
