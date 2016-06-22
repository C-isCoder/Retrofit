package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import retrofit.AES;
import retrofit.APIConstant;
import retrofit.BaseCallModel;
import retrofit.BcCallback;
import retrofit.DemoService;
import retrofit.LoginData;
import retrofit.NewsData;
import retrofit.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DemoService go = RetrofitClient
                .getInstance()
                .create(DemoService.class);
        Map<String, String> map = new HashMap<>();
        map.put("q", "英雄联盟");
        map.put("key", "d0efcc052db3181db11f0e35db1f56b4");
        map.put("dtype", "json");

//        Map<String, String> map1 = new HashMap<String, String>();
//        map1.put("userName", "17686616852");
//        map1.put("pwd", "123456");
//        map1.put("Integer", "android");
//        map1.put("isCompany", "0");
//        map1.put("appVersion", "1.0");
//        map1.put("action", "user/login");
//        BcCallback<BaseCallModel<LoginData>> callback = go.toPostService(map1);

        //RxJava
        Observable<BaseCallModel<List<NewsData>>> observable = go.rxGetNewsData(map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseCallModel<List<NewsData>>>() {
                    @Override
                    public void onCompleted() {
                        Log.d("CID", "执行成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvContent.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(BaseCallModel<List<NewsData>> newsDatas) {
                        tvContent.setText(newsDatas.result.toString());
                    }
                });

    }
}
