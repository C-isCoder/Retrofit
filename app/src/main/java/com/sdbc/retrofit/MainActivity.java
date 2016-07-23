package com.sdbc.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit.HttpService;
import retrofit.ParameterUtils;
import retrofit.RetrofitClient;
import retrofit.UserData;
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
    }

    private void initRequest() {
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("mobile", "17686616852");
        loginMap.put("pwd", "123456");
        String mobile = "17686616852";
        String pwd = "123456";
        String sign = ParameterUtils.MD5("content=" + ParameterUtils.JsonConvert(loginMap));
        RetrofitClient
                .getInstance()
                .create(HttpService.class)
                .login(sign, mobile, pwd)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UserData>() {
                    @Override
                    public void onCompleted() {
                        Log.d("CID", "success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        tvContent.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(UserData data) {
                        tvContent.setText(data.kid);
                    }
                });
    }


}
