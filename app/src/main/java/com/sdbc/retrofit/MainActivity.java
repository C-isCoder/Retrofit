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
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit.AES;
import retrofit.APIConstant;
import retrofit.BaseCallModel;
import retrofit.BcCallback;
import retrofit.DemoService;
import retrofit.JuheCallModel;
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
        try {
            Log.d("CID", "解密：" + AES.decrypt2Str("tiEPa7VkwLox/Uh2ybZg2Yx26A08ylTpstCt3mj43/izzU3CYZCqWDWw9vDZch8oA4hWLbordpPO" +
                    "1SPo5sk+mtRz4tZCsgRl7JdYiOoPTZBlptMK6hnQIX7qI/EzH9dk", APIConstant.COMMENT_DECODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        DemoService go = RetrofitClient
                .getInstance()
                .create(DemoService.class);
        //百倡登录
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("userName", "17686616852");
        map1.put("pwd", "123456");
        map1.put("Integer", "android");
        map1.put("isCompany", "0");
        map1.put("appVersion", "1.0");

        Map<String, Object> map2 = new HashMap<String, Object>();
        map2.put("action", "user/login");
        map2.put("params", map1);
        Call<BaseCallModel<LoginData>> loginCall = go.loginService(map2);
        loginCall.enqueue(new Callback<BaseCallModel<LoginData>>() {
            @Override
            public void onResponse(Call<BaseCallModel<LoginData>> call,
                                   Response<BaseCallModel<LoginData>> response) {
                if (response.isSuccessful()) {
                    String responseBody = response.toString();
                    tvContent.setText(responseBody);
                }
            }

            @Override
            public void onFailure(Call<BaseCallModel<LoginData>> call, Throwable t) {
                tvContent.setText("错误：" + t.getMessage());
            }
        });


        Map<String, String> map = new HashMap<>();
        map.put("q", "英雄联盟");
        map.put("key", "d0efcc052db3181db11f0e35db1f56b4");
        map.put("dtype", "json");

        //Retrofit
//        Call<JuheCallModel<List<NewsData>>> callback = go.postService(map);
//        callback.enqueue(new Callback<JuheCallModel<List<NewsData>>>() {
//            @Override
//            public void onResponse(Call<JuheCallModel<List<NewsData>>> call,
//                                   Response<JuheCallModel<List<NewsData>>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<JuheCallModel<List<NewsData>>> call, Throwable t) {
//
//            }
//        });
        //RxJava
//        Observable<String> observable = go.rxGetNewsData(map);
//        observable.subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//                        Log.d("CID", "执行成功");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        tvContent.setText(e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(String newsDatas) {
//                        tvContent.setText(newsDatas);
//                    }
//                });

    }
}
