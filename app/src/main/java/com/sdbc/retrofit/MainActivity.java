package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.AES;
import retrofit.APIConstant;
import retrofit.HttpService;
import retrofit.HttpRequestParamsUtils;
import retrofit.RetrofitClient;
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
        initRequest();
        try {
            Log.d("解密：", AES.decrypt2Str("tiEPa7VkwLox/Uh2ybZg2Yx26A08ylTpstCt3mj43/izzU3CYZCqWDWw9vDZch8oA4hWLbordpPO" +
                    "1SPo5sk+mtRz4tZCsgRl7JdYiOoPTZBlptMK6hnQIX7qI/EzH9dk", APIConstant.COMMENT_DECODE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRequest() {
        Map<String, String> map = new HashMap<>();
        map.put("userName", "13205852585");
        map.put("userPwd", "45454");
        map.put("isCompany", "0");
        map.put("mobileInfo", "android");
        map.put("appVersion", "1");
        map.put("platform", "1");
        //RxJava
        RetrofitClient
                .getInstance()
                .create(HttpService.class)
                .test1(HttpRequestParamsUtils.paramsConvert(map, "user/login"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d("CID", "执行成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvContent.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(String body) {
                        tvContent.setText(body);
                    }
                });
    }


}
