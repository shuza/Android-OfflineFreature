package me.shuza.offlinefreaturedemo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvName)
    TextView tvName;

    @BindView(R.id.tvEmail)
    TextView tvEmail;

    @BindView(R.id.tvBlog)
    TextView tvBlog;

    @BindView(R.id.btnLoad)
    Button btnLoad;

    private final String END_POINT = "http://www.mocky.io/v2/5a25878e2e0000392aa90676";
    RealmDAO realmDAO;

    private ProgressDialog progressDialog;
    private Observer<ResponsePojo> subscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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
                tvName.setText("Name : " + data.getName());
                tvEmail.setText("Email : " + data.getEmail());
                tvBlog.setText("Blog : " + data.getBlog());
                realmDAO.saveResponse(data, END_POINT);
            }

            @Override
            public void onError(Throwable t) {
                LogUtil.printLogMessage("error response", t.getMessage());

                progressDialog.dismiss();
                tvName.setText("error :   " + t.getMessage());
                tvEmail.setText("error");
                tvBlog.setText("error");
            }

            @Override
            public void onComplete() {

            }
        };
    }

    @OnClick(R.id.btnLoad)
    public void showData(View view) {
        progressDialog.show();
        getDataFromAPI();
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
